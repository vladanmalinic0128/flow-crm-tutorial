package com.example.application.views.login;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.login.LoginI18n;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterListener;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;

@AnonymousAllowed
@Route("login")
@PageTitle("Login")
public class LoginView extends VerticalLayout implements BeforeEnterListener {

    private final LoginForm login = new LoginForm();

    public LoginView() {
        LoginI18n i18n = LoginI18n.createDefault();

        LoginI18n.Form i18nForm = i18n.getForm();
        i18nForm.setTitle("Prijava na sistem");
        i18nForm.setUsername("Korisničko ime");
        i18nForm.setPassword("Lozinka");
        i18nForm.setSubmit("Prijavite se");
        i18n.setForm(i18nForm);

        LoginI18n.ErrorMessage i18nErrorMessage = i18n.getErrorMessage();
        i18nErrorMessage.setTitle("Pogrešni kredencijali");
        i18nErrorMessage.setMessage(
                "Molimo Vas da pokušate ponovo ili se javite administratoru na mejl");
        i18n.setErrorMessage(i18nErrorMessage);

        addClassName("login-view");
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);

        login.setAction("login");
        login.setI18n(i18n);
        login.setForgotPasswordButtonVisible(false);

        add(getLogo(), login);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        if(event.getLocation()
                .getQueryParameters()
                .getParameters()
                .containsKey("error")) {
            login.setError(true);
        }
    }

    private HorizontalLayout getLogo() {
        Image appLogo = new Image("images/logo.png", "MyApp Logo");
        appLogo.setWidth("180px");
        appLogo.getStyle().set("margin", "0 var(--lumo-space-m)");

        Image votingLogo = new Image("images/election24.png", "MyApp Logo");
        votingLogo.setWidth("80px");
        votingLogo.getStyle().set("margin", "0 var(--lumo-space-m)");

        HorizontalLayout logoLayout = new HorizontalLayout(appLogo, votingLogo);
        logoLayout.setWidthFull();
        logoLayout.setPadding(false);
        logoLayout.setSpacing(false);
        logoLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        logoLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        logoLayout.getStyle().set("padding-top", "10px");

        return logoLayout;
    }
}
