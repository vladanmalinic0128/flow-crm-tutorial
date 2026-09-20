package com.example.application.services;

import com.example.application.entities.MemberEntity;
import com.example.application.entities.MentorEntity;
import com.example.application.entities.ObserverEntity;
import com.example.application.entities.PresidentEntity;
import com.example.application.enums.ScriptEnum;
import com.example.application.repositories.MemberRepository;
import com.example.application.repositories.ObserverRepository;
import com.example.application.repositories.PresidentRepository;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Exports the same checks the "Provjera grešaka" page shows (see HealthCheckView), limited to the
 * voting councels of one mentor (GIK member), all of them, or the mobile teams.
 */
@Service
@RequiredArgsConstructor
public class HealthCheckXlsxService {
    private static final String ROOT_PATH = "src/main/resources/generated-documents";

    private final MemberRepository memberRepository;
    private final PresidentRepository presidentRepository;
    private final ObserverRepository observerRepository;
    private final JMBGValidator jmbgValidator;
    private final BankAccountValidator bankAccountValidator;
    private final CyrillicToLatinConverter cyrillicToLatinConverter;

    private record Row(List<String> cells) {
    }

    /**
     * @param entity a mentor with an id for that mentor's councels, id -1 for the mobile teams, no id for everything
     */
    @Transactional(readOnly = true)
    public String generateErrors(MentorEntity entity, String fileTitle, ScriptEnum script) {
        List<MemberEntity> allMembers = memberRepository.findAllWithConstraintDetails();
        List<PresidentEntity> allPresidents = presidentRepository.findAllWithVotingCouncelDetails();

        List<MemberEntity> members = allMembers.stream().filter(m -> inScope(entity, m.getConstraint().getVotingCouncel().getMentor().getId(), m.getConstraint().getVotingCouncel().getCode())).toList();
        List<PresidentEntity> presidents = allPresidents.stream().filter(p -> inScope(entity, p.getVotingCouncel().getMentor().getId(), p.getVotingCouncel().getCode())).toList();
        // Filled-in positions only - an unfilled slot is not an error
        List<MemberEntity> filledMembers = members.stream().filter(this::isFilled).toList();

        XSSFWorkbook workbook = new XSSFWorkbook();
        Styles styles = createStyles(workbook);

        writeSheet(workbook, styles, "Nevalidan JMBG", memberHeader(script), filledMembers.stream()
                .filter(m -> !jmbgValidator.isValidJMBG(m.getJmbg()))
                .map(m -> memberRow(m, script)).toList(), script);

        Map<String, ObserverEntity> observerByJmbg = observerRepository.findAllWithDetailsByStatusId(1).stream()
                .filter(o -> o.getJmbg() != null)
                .collect(Collectors.toMap(ObserverEntity::getJmbg, o -> o, (a, b) -> a));
        writeSheet(workbook, styles, "Posmatrači", concat(memberHeader(script), "Odluka", "Broj na listi"), filledMembers.stream()
                .filter(m -> m.getJmbg() != null && observerByJmbg.containsKey(m.getJmbg()))
                .map(m -> concat(memberRow(m, script), observerByJmbg.get(m.getJmbg()).getStack().getDecisionNumber(),
                        String.valueOf(observerByJmbg.get(m.getJmbg()).getDocumentNumber())))
                .toList(), script);

        Map<String, PresidentEntity> presidentByJmbg = allPresidents.stream()
                .filter(p -> p.getJmbg() != null)
                .collect(Collectors.toMap(PresidentEntity::getJmbg, p -> p, (a, b) -> a));
        writeSheet(workbook, styles, "Predsjednici", concat(memberHeader(script), "Uloga"), filledMembers.stream()
                .filter(m -> m.getJmbg() != null && presidentByJmbg.containsKey(m.getJmbg()))
                .map(m -> {
                    PresidentEntity p = presidentByJmbg.get(m.getJmbg());
                    return concat(memberRow(m, script), (Boolean.TRUE.equals(p.getIsPresident()) ? "Predsjednik na: " : "Zamjenik predsjednika na: ") + p.getVotingCouncel().getCode());
                }).toList(), script);

        writeGroupedSheet(workbook, styles, "Duplikati JMBG",
                List.of("JMBG", "Ime i prezime", "Šifra BO", "Naziv BO", "Politički subjekat", "Pozicija", "Mentor"),
                duplicateJmbgGroups(allMembers, entity, script), script);

        writeSheet(workbook, styles, "Nedostaju podaci", concat(memberHeader(script), "Šta nedostaje"), filledMembers.stream()
                .map(m -> Map.entry(m, missingData(m)))
                .filter(e -> !e.getValue().isEmpty())
                .map(e -> concat(memberRow(e.getKey(), script), String.join(", ", e.getValue())))
                .toList(), script);

        writeGroupedSheet(workbook, styles, "Dupli žiro računi", List.of("Žiro račun", "Ime i prezime", "JMBG", "Šifra BO", "Politički subjekat", "Pozicija", "Mentor"),
                duplicateBankGroups(allMembers, allPresidents, entity, script), script);

        List<Row> invalidBank = new ArrayList<>();
        filledMembers.stream()
                .filter(m -> m.getIsAcknowledged() == null || m.getIsAcknowledged())
                .filter(m -> !isBlank(m.getBankNumber()) && !bankAccountValidator.isValidAccountNumber(m.getBankNumber()))
                .forEach(m -> invalidBank.add(new Row(concat(memberRow(m, script), "Član", m.getBankNumber()))));
        presidents.stream()
                .filter(p -> !p.isEmpty())
                .filter(p -> p.getIsAcknowledged() == null || p.getIsAcknowledged())
                .filter(p -> !isBlank(p.getBankNumber()) && !bankAccountValidator.isValidAccountNumber(p.getBankNumber()))
                .forEach(p -> invalidBank.add(new Row(concat(presidentRow(p, script), "Predsjednik", p.getBankNumber()))));
        writeSheet(workbook, styles, "Nevalidan žiro račun", concat(memberHeader(script), "Tip", "Žiro račun"),
                invalidBank.stream().map(Row::cells).toList(), script);

        return save(fileTitle, workbook);
    }

    private boolean inScope(MentorEntity entity, Long mentorId, String councelCode) {
        if (entity.getId() == null)
            return true;
        if (entity.getId() == -1)
            return councelCode.contains("MT") || councelCode.contains("МТ");
        return entity.getId().equals(mentorId);
    }

    /** A position with no first name, last name or JMBG is just an unfilled slot. */
    private boolean isFilled(MemberEntity m) {
        return !m.isEmpty() && (!isBlank(m.getFirstname()) || !isBlank(m.getLastname()) || !isBlank(m.getJmbg()));
    }

    private List<String> missingData(MemberEntity m) {
        List<String> missing = new ArrayList<>();
        if (m.getFirstname() == null || m.getLastname() == null) missing.add("ime ili prezime");
        if (m.getIsMale() == null) missing.add("pol");
        if (m.getQualifications() == null) missing.add("stručna sprema");
        if (m.getJmbg() == null) missing.add("JMBG");
        if (m.getPhoneNumber() == null) missing.add("broj telefona");
        boolean acknowledged = m.getIsAcknowledged() == null || m.getIsAcknowledged();
        if (acknowledged && m.getBankNumber() == null) missing.add("broj žiro računa");
        if (acknowledged && !isBlank(m.getBankNumber()) && m.getBankName() == null) missing.add("naziv banke");
        return missing;
    }

    /**
     * One group per JMBG that appears more than once (if at least one occurrence is in scope), one row per
     * occurrence, JMBG first and then the name - so writeGroupedSheet can merge whatever is identical.
     */
    private List<List<List<String>>> duplicateJmbgGroups(List<MemberEntity> allMembers, MentorEntity entity, ScriptEnum script) {
        Map<String, List<MemberEntity>> byJmbg = allMembers.stream()
                .filter(this::isFilled)
                .filter(m -> !isBlank(m.getJmbg()))
                .collect(Collectors.groupingBy(MemberEntity::getJmbg));
        List<List<List<String>>> groups = new ArrayList<>();
        byJmbg.entrySet().stream().filter(e -> e.getValue().size() > 1).sorted(Map.Entry.comparingByKey()).forEach(e -> {
            boolean relevant = e.getValue().stream().anyMatch(m -> inScope(entity, m.getConstraint().getVotingCouncel().getMentor().getId(), m.getConstraint().getVotingCouncel().getCode()));
            if (!relevant)
                return;
            List<List<String>> group = new ArrayList<>();
            for (MemberEntity m : e.getValue()) {
                List<String> row = memberRow(m, script);
                // memberRow is name, JMBG, ...; here it's JMBG, name, ...
                group.add(new ArrayList<>(List.of(row.get(1), row.get(0), row.get(2), row.get(3), row.get(4), row.get(5), row.get(6))));
            }
            groups.add(group);
        });
        return groups;
    }

    /**
     * One group per bank number that appears more than once (members and presidents), if at least one
     * occurrence is in scope, one row per occurrence - so writeGroupedSheet can merge whatever is identical.
     */
    private List<List<List<String>>> duplicateBankGroups(List<MemberEntity> allMembers, List<PresidentEntity> allPresidents, MentorEntity entity, ScriptEnum script) {
        Map<String, List<Object>> byBank = Stream.concat(
                allMembers.stream().filter(m -> !isBlank(m.getBankNumber())),
                allPresidents.stream().filter(p -> !isBlank(p.getBankNumber()))
        ).collect(Collectors.groupingBy(o -> o instanceof MemberEntity m ? m.getBankNumber() : ((PresidentEntity) o).getBankNumber()));

        List<List<List<String>>> groups = new ArrayList<>();
        byBank.entrySet().stream().filter(e -> e.getValue().size() > 1).sorted(Map.Entry.comparingByKey()).forEach(e -> {
            boolean relevant = e.getValue().stream().anyMatch(o -> o instanceof MemberEntity m
                    ? inScope(entity, m.getConstraint().getVotingCouncel().getMentor().getId(), m.getConstraint().getVotingCouncel().getCode())
                    : inScope(entity, ((PresidentEntity) o).getVotingCouncel().getMentor().getId(), ((PresidentEntity) o).getVotingCouncel().getCode()));
            if (!relevant)
                return;
            List<List<String>> group = new ArrayList<>();
            for (Object o : e.getValue()) {
                if (o instanceof MemberEntity m) {
                    group.add(List.of(e.getKey(), text(fullName(m.getFirstname(), m.getLastname()), script), nz(m.getJmbg()),
                            text(m.getConstraint().getVotingCouncel().getCode(), script),
                            (Boolean.TRUE.equals(m.getIsGik()) ? "GIK (" : "(") + m.getConstraint().getPoliticalOrganization().getCode() + ")",
                            text(m.getConstraint().getTitle().getName(), script),
                            text(m.getConstraint().getVotingCouncel().getMentor().getFullname(), script)));
                } else {
                    PresidentEntity p = (PresidentEntity) o;
                    group.add(List.of(e.getKey(), text(fullName(p.getFirstname(), p.getLastname()), script), nz(p.getJmbg()),
                            text(p.getVotingCouncel().getCode(), script), "GIK",
                            Boolean.TRUE.equals(p.getIsPresident()) ? "Predsjednik" : "Zamjenik predsjednika",
                            text(p.getVotingCouncel().getMentor().getFullname(), script)));
                }
            }
            groups.add(group);
        });
        return groups;
    }

    private List<String> memberHeader(ScriptEnum script) {
        return List.of("Ime i prezime", "JMBG", "Šifra BO", "Naziv BO", "Politički subjekat", "Pozicija", "Mentor");
    }

    private List<String> memberRow(MemberEntity m, ScriptEnum script) {
        return List.of(text(fullName(m.getFirstname(), m.getLastname()), script), nz(m.getJmbg()),
                text(m.getConstraint().getVotingCouncel().getCode(), script),
                text(m.getConstraint().getVotingCouncel().getName(), script),
                (Boolean.TRUE.equals(m.getIsGik()) ? "GIK (" : "(") + m.getConstraint().getPoliticalOrganization().getCode() + ")",
                text(m.getConstraint().getTitle().getName(), script),
                text(m.getConstraint().getVotingCouncel().getMentor().getFullname(), script));
    }

    private List<String> presidentRow(PresidentEntity p, ScriptEnum script) {
        return List.of(text(fullName(p.getFirstname(), p.getLastname()), script), nz(p.getJmbg()),
                text(p.getVotingCouncel().getCode(), script), text(p.getVotingCouncel().getName(), script), "GIK",
                Boolean.TRUE.equals(p.getIsPresident()) ? "Predsjednik" : "Zamjenik predsjednika",
                text(p.getVotingCouncel().getMentor().getFullname(), script));
    }

    private String fullName(String firstname, String lastname) {
        return Stream.of(firstname, lastname).filter(Objects::nonNull).collect(Collectors.joining(" "));
    }

    private String text(String value, ScriptEnum script) {
        if (value == null)
            return "";
        return script == ScriptEnum.CYRILLIC ? value : cyrillicToLatinConverter.convert(value);
    }

    private String nz(String value) {
        return value == null ? "" : value;
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }

    private static List<String> concat(List<String> base, String... extra) {
        List<String> result = new ArrayList<>(base);
        result.addAll(Arrays.asList(extra));
        return result;
    }

    private static final int MAX_COLUMN_WIDTH = 55 * 256;
    private static final byte[] TAB_ERRORS = {(byte) 0xC0, 0x39, 0x2B};
    private static final byte[] TAB_OK = {0x27, (byte) 0xAE, 0x60};

    private record Styles(CellStyle header, CellStyle plain, CellStyle zebra, CellStyle groupPlain, CellStyle groupShaded, CellStyle noErrors) {
    }

    private Styles createStyles(XSSFWorkbook workbook) {
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerFont.setColor(IndexedColors.WHITE.getIndex());
        headerFont.setFontHeightInPoints((short) 11);
        CellStyle header = workbook.createCellStyle();
        header.setFont(headerFont);
        header.setFillForegroundColor(new XSSFColor(new byte[]{0x2C, 0x3E, 0x50}, null));
        header.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        header.setAlignment(HorizontalAlignment.CENTER);
        header.setVerticalAlignment(VerticalAlignment.CENTER);
        header.setWrapText(true);
        border(header, BorderStyle.THIN, IndexedColors.GREY_50_PERCENT);

        Font okFont = workbook.createFont();
        okFont.setItalic(true);
        okFont.setColor(IndexedColors.GREEN.getIndex());
        CellStyle noErrors = workbook.createCellStyle();
        noErrors.setFont(okFont);

        return new Styles(header,
                bodyStyle(workbook, null),
                bodyStyle(workbook, new byte[]{(byte) 0xF2, (byte) 0xF6, (byte) 0xFA}),
                bodyStyle(workbook, null),
                bodyStyle(workbook, new byte[]{(byte) 0xE3, (byte) 0xEE, (byte) 0xF9}),
                noErrors);
    }

    private CellStyle bodyStyle(XSSFWorkbook workbook, byte[] fill) {
        CellStyle style = workbook.createCellStyle();
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setWrapText(true);
        border(style, BorderStyle.THIN, IndexedColors.GREY_25_PERCENT);
        if (fill != null) {
            style.setFillForegroundColor(new XSSFColor(fill, null));
            style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        }
        return style;
    }

    private void border(CellStyle style, BorderStyle borderStyle, IndexedColors color) {
        style.setBorderTop(borderStyle);
        style.setBorderBottom(borderStyle);
        style.setBorderLeft(borderStyle);
        style.setBorderRight(borderStyle);
        style.setTopBorderColor(color.getIndex());
        style.setBottomBorderColor(color.getIndex());
        style.setLeftBorderColor(color.getIndex());
        style.setRightBorderColor(color.getIndex());
    }

    /** Header row, frozen and filterable, plus a red tab when there are errors and a green one when there are none. */
    private XSSFSheet createSheet(XSSFWorkbook workbook, Styles styles, String title, List<String> header, int errorCount) {
        XSSFSheet sheet = workbook.createSheet(title);
        XSSFRow headerRow = sheet.createRow(0);
        headerRow.setHeightInPoints(24);
        for (int i = 0; i < header.size(); i++) {
            XSSFCell cell = headerRow.createCell(i);
            cell.setCellValue(header.get(i));
            cell.setCellStyle(styles.header());
        }
        sheet.setTabColor(new XSSFColor(errorCount > 0 ? TAB_ERRORS : TAB_OK, null));
        sheet.createFreezePane(0, 1);
        sheet.getPrintSetup().setLandscape(true);
        sheet.setFitToPage(true);
        sheet.getPrintSetup().setFitHeight((short) 0);
        return sheet;
    }

    private void finishSheet(XSSFSheet sheet, List<String> header, int lastRow) {
        for (int i = 0; i < header.size(); i++) {
            sheet.autoSizeColumn(i);
            // Merged cells are ignored by autosize, so add some room, and keep long values from making a column huge
            sheet.setColumnWidth(i, Math.min(sheet.getColumnWidth(i) + 2 * 256, MAX_COLUMN_WIDTH));
        }
        if (lastRow >= 1)
            sheet.setAutoFilter(new CellRangeAddress(0, lastRow, 0, header.size() - 1));
    }

    /** One sheet per check; a check with no errors still gets its sheet, with a single "Nema grešaka." row. */
    private void writeSheet(XSSFWorkbook workbook, Styles styles, String name, List<String> header, List<List<String>> rows, ScriptEnum script) {
        XSSFSheet sheet = createSheet(workbook, styles, text(name, script) + " (" + rows.size() + ")", header, rows.size());
        if (rows.isEmpty()) {
            XSSFCell cell = sheet.createRow(1).createCell(0);
            cell.setCellValue("Nema grešaka.");
            cell.setCellStyle(styles.noErrors());
        }
        int rowIndex = 1;
        for (List<String> cells : rows) {
            XSSFRow row = sheet.createRow(rowIndex++);
            CellStyle style = rowIndex % 2 == 0 ? styles.plain() : styles.zebra();
            for (int i = 0; i < cells.size(); i++) {
                XSSFCell cell = row.createCell(i);
                cell.setCellValue(cells.get(i));
                cell.setCellStyle(style);
            }
        }
        finishSheet(sheet, header, rowIndex - 1);
    }

    /**
     * Rows come in groups (e.g. every occurrence of one duplicate JMBG). Within a group, a column whose
     * value is the same in every row is merged into one cell; where the values differ, each row keeps
     * its own. Groups alternate shading so they read as separate blocks.
     */
    private void writeGroupedSheet(XSSFWorkbook workbook, Styles styles, String name, List<String> header,
                                   List<List<List<String>>> groups, ScriptEnum script) {
        XSSFSheet sheet = createSheet(workbook, styles, text(name, script) + " (" + groups.size() + ")", header, groups.size());
        if (groups.isEmpty()) {
            XSSFCell cell = sheet.createRow(1).createCell(0);
            cell.setCellValue("Nema grešaka.");
            cell.setCellStyle(styles.noErrors());
        }

        int rowIndex = 1;
        boolean useShade = false;
        for (List<List<String>> group : groups) {
            CellStyle style = useShade ? styles.groupShaded() : styles.groupPlain();
            int firstRow = rowIndex;
            for (List<String> cells : group) {
                XSSFRow row = sheet.createRow(rowIndex++);
                for (int i = 0; i < cells.size(); i++) {
                    XSSFCell cell = row.createCell(i);
                    cell.setCellValue(cells.get(i));
                    cell.setCellStyle(style);
                }
            }
            int lastRow = rowIndex - 1;
            for (int col = 0; col < header.size() && lastRow > firstRow; col++) {
                final int column = col;
                boolean identical = group.stream().map(r -> r.get(column)).distinct().count() == 1;
                if (identical)
                    sheet.addMergedRegion(new CellRangeAddress(firstRow, lastRow, col, col));
            }
            useShade = !useShade;
        }
        finishSheet(sheet, header, rowIndex - 1);
    }

    private String save(String fileTitle, XSSFWorkbook workbook) {
        String fileName = ROOT_PATH + File.separator + fileTitle;
        try (FileOutputStream outputStream = new FileOutputStream(fileName)) {
            workbook.write(outputStream);
            workbook.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return fileName;
    }
}
