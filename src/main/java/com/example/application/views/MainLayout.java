package com.example.application.views;

import com.example.application.security.SecurityService;
import com.example.application.views.counsels.*;
import com.example.application.views.list.AddingStackForm;
import com.example.application.views.list.ListView;
import com.example.application.views.observers.AddingObserversForm;
import com.example.application.views.observers.ObserverReportsView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.*;
import com.vaadin.flow.theme.lumo.LumoUtility;
import jakarta.annotation.security.PermitAll;

import java.util.stream.Collectors;

@PermitAll
@PageTitle("034B Banja Luka")
@Route(value = "")
@RouteAlias(value = "")
@CssImport("./styles/styles.css")
public class MainLayout extends AppLayout implements RouterLayout {
    private static VerticalLayout currentLayout = null;
    private final SecurityService securityService;
    public MainLayout(SecurityService securityService) {
        this.securityService = securityService;

        HorizontalLayout logoLayout = getLogo();
        Tabs tabs = getPrimaryNavigation();

        Scroller scroller = new Scroller(tabs);
        scroller.setClassName(LumoUtility.Padding.MEDIUM);

        Button logoutButton = new Button("Odjavite se", e -> securityService.logout());
        addToDrawer(logoLayout, scroller, logoutButton);



        DrawerToggle toggle = getDrawerToggle();

        H1 viewTitle = getViewTitle();

        HorizontalLayout subViews = getSecondaryNavigationForOberservers();


        HorizontalLayout wrapper = new HorizontalLayout(toggle, viewTitle);
        wrapper.setAlignItems(FlexComponent.Alignment.START);
        wrapper.setSpacing(false);

        VerticalLayout viewHeader = new VerticalLayout(wrapper, subViews);
        viewHeader.setPadding(false);
        viewHeader.setSpacing(false);


        addToNavbar(viewHeader);
        currentLayout = viewHeader;

        tabs.addSelectedChangeListener(event -> {
            Tab selectedTab = event.getSelectedTab();
            viewTitle.setText(getLabelOfSelectedTab(event));

            clearNavbar();
            HorizontalLayout activeLayout = null;

            if ("Posmatrači".equals(getLabelOfSelectedTab(event))) {
                activeLayout = getSecondaryNavigationForOberservers();
            } else if ("Birački odbori".equals(getLabelOfSelectedTab(event))) {
                activeLayout = getSecondaryNavigationForVotingMembers();
            }

            // Create a wrapper layout to center activeLayout
            HorizontalLayout wrapperLayout = new HorizontalLayout(activeLayout);
            wrapperLayout.setWidth("100%");
            wrapperLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
            wrapperLayout.setAlignItems(FlexComponent.Alignment.CENTER);

            currentLayout = new VerticalLayout(wrapper, wrapperLayout);
            currentLayout.setPadding(false);

            addToDrawer(logoLayout, scroller, logoutButton);
            addToNavbar(currentLayout);
        });

        setPrimarySection(Section.DRAWER);
    }

    private H1 getViewTitle() {
        H1 viewTitle = new H1("Posmatrači");
        viewTitle.getStyle().set("margin", "0");
        viewTitle.getStyle().set("margin-top", "5px");
        return viewTitle;
    }

    private DrawerToggle getDrawerToggle() {
        DrawerToggle toggle = new DrawerToggle();
        toggle.getStyle().set("transform", "scale(1.5)");
        toggle.getStyle().set("margin-top", "11px");
        return toggle;
    }

    private HorizontalLayout getLogo() {
        Image appLogo = new Image("images/logo.png", "MyApp Logo");
        appLogo.setWidth("150px");
        appLogo.getStyle().set("margin", "0 var(--lumo-space-m)");

        HorizontalLayout logoLayout = new HorizontalLayout(appLogo);
        logoLayout.setWidthFull();
        logoLayout.setPadding(false);
        logoLayout.setSpacing(false);
        logoLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        logoLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        logoLayout.getStyle().set("padding-top", "10px");

        return logoLayout;
    }

    private Tabs getPrimaryNavigation() {
        Tabs tabs = new Tabs();
        tabs.setOrientation(Tabs.Orientation.VERTICAL);

        // Create Tab items with associated RouterLinks
        Tab observersTab = new Tab(VaadinIcon.EYE.create(), new RouterLink("Posmatrači", AddingObserversForm.class));
        Tab boardTab = new Tab(VaadinIcon.DIPLOMA.create(), new RouterLink("Birački odbori", DrawingView.class));
        Tab solutionsTab = new Tab(VaadinIcon.FILE_O.create(), new RouterLink("Ugovori", ListView.class));

        // Add Tabs to the Tabs component
        tabs.add(observersTab, boardTab, solutionsTab);

        tabs.setSelectedTab(observersTab);
        // Set the font size for each tab
        tabs.getChildren().forEach(tab -> {
            tab.getStyle().set("font-size", "20px");
        });

        return tabs;
    }

    private HorizontalLayout getSecondaryNavigationForOberservers() {
        // Create Tabs container
        Tabs tabs = new Tabs();

        // Create and configure tabs
        Tab tab1 = createTab("Akredituj nove posmatrače", AddingObserversForm.class);
        Tab tab2 = createTab("Preuzmi trenutno stanje", ObserverReportsView.class);

        tabs.add(tab1, tab2);
        tabs.setOrientation(Tabs.Orientation.HORIZONTAL);

        tabs.setSelectedTab(tab1);

        // Add some styling
        tabs.addClassName(LumoUtility.JustifyContent.CENTER);
        tabs.addClassName(LumoUtility.Gap.SMALL);
        tabs.addClassName(LumoUtility.Height.MEDIUM);

        // Return a layout that contains the tabs
        HorizontalLayout navigation = new HorizontalLayout(tabs);
        navigation.setWidthFull();
        navigation.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        navigation.setPadding(false);
        navigation.setSpacing(false);

        return navigation;
    }

    private HorizontalLayout getSecondaryNavigationForVotingMembers() {
        // Create Tabs container
        Tabs tabs = new Tabs();

        // Create and configure tabs
        Tab tab1 = createTab("Žrijebanje", DrawingView.class);
        Tab tab2 = createTab("BO po PS", CouncelsByPoliticalOrganizationView.class);
        Tab tab3 = createTab("BO po članovima GIK", CouncelsByMentor.class);
        Tab tab4 = createTab("Ažuriranje podataka", DataUploadView.class);
        Tab tab5 = createTab("Provjera grešaka", HealthCheckView.class);
        Tab tab6 = createTab("Rješenja", ReportsView.class);
        Tab tab7 = createTab("Prikaz po bankama", MembersByBankReportView.class);
        Tab  tab8 = createTab("Rezervni spisak", SubstituteView.class);

        tabs.add(tab1, tab2, tab3, tab4, tab5, tab6, tab7, tab8);
        tabs.setOrientation(Tabs.Orientation.HORIZONTAL);
        tabs.setSelectedTab(tab1);

        // Add some styling
        tabs.addClassName(LumoUtility.JustifyContent.CENTER);
        tabs.addClassName(LumoUtility.Gap.SMALL);
        tabs.addClassName(LumoUtility.Height.MEDIUM);

        // Return a layout that contains the tabs
        HorizontalLayout navigation = new HorizontalLayout(tabs);
        navigation.setWidthFull();
        navigation.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        navigation.setPadding(false);
        navigation.setSpacing(false);

        return navigation;
    }

    private Tab createTab(String viewName, Class<? extends Component> viewClass) {
        // Create a new RouterLink for the tab
        RouterLink link = new RouterLink(viewName, viewClass);

        // Create a new Tab and add the RouterLink to it
        Tab tab = new Tab(link);

        // Add some styling
        tab.addClassNames(LumoUtility.Display.FLEX,
                LumoUtility.AlignItems.CENTER,
                LumoUtility.Padding.Horizontal.MEDIUM,
                LumoUtility.TextColor.SECONDARY,
                LumoUtility.FontWeight.MEDIUM);

        return tab;
    }

    private String getLabelOfSelectedTab(Tabs.SelectedChangeEvent event) {
       return event.getSelectedTab().getChildren().collect(Collectors.toList()).get(1).getElement().getChildren().collect(Collectors.toList()).get(0).toString();
    }

    public void clearNavbar() {
        // Remove all components from the navbar
        getChildren()
                .filter(component -> component.getParent().isPresent() && component.getParent().get() == this)
                .forEach(this::remove);
    }
}
