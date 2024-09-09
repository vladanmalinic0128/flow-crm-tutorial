package com.example.application.views.contracts;

import com.example.application.views.MainLayout;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;

@PermitAll
@Route(value = "unutrasnji-saradnici", layout = MainLayout.class)
public class InternsView extends FormLayout {
}
