package com.example.application.views.counsels;

import com.example.application.views.MainLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;

@PermitAll
@Route(value = "bo/update", layout = MainLayout.class)
public class DataUploadView extends VerticalLayout {
}
