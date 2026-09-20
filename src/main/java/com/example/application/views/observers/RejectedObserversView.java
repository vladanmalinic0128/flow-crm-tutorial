package com.example.application.views.observers;

import com.example.application.services.RejectedObserversService;
import com.example.application.services.RejectedObserversService.RejectedObserverRow;
import com.example.application.services.RejectedObserversService.StackRejections;
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

@PageTitle("Odbijeni posmatrači")
@PermitAll
@Route(value = "posmatraci/odbijeni", layout = MainLayout.class)
public class RejectedObserversView extends VerticalLayout {

    public RejectedObserversView(RejectedObserversService rejectedObserversService) {
        setWidth("100%");
        getStyle().set("margin", "0 auto");

        List<StackRejections> stacks = rejectedObserversService.getRejectedByStack();
        if (stacks.isEmpty()) {
            add(new Span("Nema odbijenih posmatrača."));
            return;
        }

        for (StackRejections stack : stacks) {
            Details details = new Details(stack.title() + " - odbijeno: " + stack.rows().size(), buildGrid(stack.rows()));
            details.setOpened(false);
            details.setWidthFull();
            add(details);
        }
    }

    private Grid<RejectedObserverRow> buildGrid(List<RejectedObserverRow> rows) {
        Grid<RejectedObserverRow> grid = new Grid<>();
        grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES, GridVariant.LUMO_WRAP_CELL_CONTENT);
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
