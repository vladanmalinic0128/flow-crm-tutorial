package com.example.application.views.observers;

import com.example.application.entities.PoliticalOrganizationEntity;
import com.example.application.services.PoliticalOrganizationService;
import com.example.application.services.RejectedObserversService;
import com.example.application.services.RejectedObserversService.RejectedObserverRow;
import com.example.application.views.MainLayout;
import com.vaadin.flow.component.details.Details;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;

import java.util.List;
import java.util.Map;

@PageTitle("Odbijeni posmatrači")
@PermitAll
@Route(value = "posmatraci/odbijeni", layout = MainLayout.class)
public class RejectedObserversView extends VerticalLayout {

    public RejectedObserversView(PoliticalOrganizationService politicalOrganizationService, RejectedObserversService rejectedObserversService) {
        setWidth("100%");
        getStyle().set("margin", "0 auto");

        Map<Long, List<RejectedObserverRow>> rejectedByOrganization = rejectedObserversService.getRejectedByPoliticalOrganization();
        if (rejectedByOrganization.isEmpty()) {
            add(new Span("Nema odbijenih posmatrača."));
            return;
        }

        for (PoliticalOrganizationEntity organization : politicalOrganizationService.getAll()) {
            List<RejectedObserverRow> rows = rejectedByOrganization.get(organization.getId());
            if (rows == null || rows.isEmpty())
                continue;

            Details details = new Details(organization.getCode() + ": " + organization.getName() + " (" + rows.size() + ")", buildGrid(rows));
            details.setOpened(true);
            details.setWidthFull();
            add(details);
        }
    }

    private Grid<RejectedObserverRow> buildGrid(List<RejectedObserverRow> rows) {
        Grid<RejectedObserverRow> grid = new Grid<>();
        grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES, GridVariant.LUMO_WRAP_CELL_CONTENT);
        grid.addColumn(RejectedObserverRow::stack).setHeader("Odluka").setAutoWidth(true).setFlexGrow(0);
        grid.addColumn(RejectedObserverRow::documentNumber).setHeader("Red. br.").setAutoWidth(true).setFlexGrow(0);
        grid.addColumn(RejectedObserverRow::fullName).setHeader("Ime i prezime").setAutoWidth(true).setFlexGrow(0);
        grid.addColumn(RejectedObserverRow::jmbg).setHeader("JMBG").setAutoWidth(true).setFlexGrow(0);
        grid.addColumn(RejectedObserverRow::reason).setHeader("Razlog odbijanja").setAutoWidth(true).setFlexGrow(0);
        grid.addColumn(RejectedObserverRow::details).setHeader("Detalji").setFlexGrow(1).setWidth("350px");
        grid.setItems(rows);
        grid.setAllRowsVisible(true);
        return grid;
    }
}
