package com.optimised.backup.views.checkout;

import com.optimised.backup.data.entity.Site;
import com.optimised.backup.data.service.EmailService;
import com.optimised.backup.data.service.EngineerService;
import com.optimised.backup.data.service.PathService;
import com.optimised.backup.data.service.SiteService;
import com.optimised.backup.views.MainLayout;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;

@RolesAllowed("ADMIN")
@PageTitle("Backup Checkout")
@Route(value = "checkout", layout = MainLayout.class)
public class CheckoutView extends VerticalLayout {

    Grid<Site> grid= new Grid<>(Site.class);
    TextField filterByName = new TextField();
    IntegerField filterByStoreNo = new IntegerField();

    SiteService siteService;
    PathService pathService;
    EngineerService engineerService;
    EmailService emailService;
    CheckoutForm form;

    @Autowired
    public CheckoutView(SiteService siteService, PathService pathService, EngineerService engineerService,EmailService emailService){
        this.siteService = siteService;
        this.pathService = pathService;
        this.engineerService = engineerService;
        this.emailService = emailService;
        addClassName("list-view");
        setSizeFull();
        configureGrid();
        configureForm();
        add(getToolbar(), getContent());
        updateList();
    }

    private Component getContent() {
        HorizontalLayout content = new HorizontalLayout(grid, form);
        content.setFlexGrow(2, grid);
        content.setFlexGrow(1, form);
        content.addClassNames("content");
        content.setSizeFull();
        return content;
    }

    private void configureForm() {
        form = new CheckoutForm(engineerService,emailService, pathService,siteService);
        form.setWidth("25em");
        form.addSaveListener(this::saveSite);
        form.addCloseListener(e -> closeEditor());
    }

    private void saveSite(CheckoutForm.SaveEvent event) {
        siteService.saveSite(event.getSite());
        updateList();
        closeEditor();
    }

    private void configureGrid(){
        grid.addClassNames("checkout-grid");
        grid.setSizeFull();
        grid.setColumns("storeNumber","name","siteNumber","engineer.fullName");
        grid.getColumns().forEach(col -> col.setAutoWidth(true));
        grid.asSingleSelect().addValueChangeListener(event -> editSite(event.getValue()));

    }

    private HorizontalLayout getToolbar() {
        filterByName.setPlaceholder("Filter by name...");
        filterByName.setClearButtonVisible(true);
        filterByName.setValueChangeMode(ValueChangeMode.LAZY);
        filterByName.addValueChangeListener(e -> updateList());
        filterByStoreNo.setPlaceholder("Filter by store no...");
        filterByStoreNo.setClearButtonVisible(true);
        filterByStoreNo.setValueChangeMode(ValueChangeMode.LAZY);
        filterByStoreNo.addValueChangeListener(e -> updateList());

        String backupPath = pathService.getBackupPath();
        File theDir = new File(backupPath);
        if (!theDir.exists()) {
            theDir.mkdirs();
        }
        var toolbar = new HorizontalLayout(filterByStoreNo,filterByName);
        toolbar.addClassName("toolbar");
        return toolbar;
    }

    public void editSite(Site site) {
        if (site == null) {
            closeEditor();
        } else {
            form.setSite(site);
            form.setVisible(true);
            addClassName("editing");
        }
    }

    private void closeEditor() {
        form.setSite(null);
        form.setVisible(false);
        removeClassName("editing");
    }

    private void updateList() {
        if (!filterByStoreNo.isEmpty()){
            grid.setItems(siteService.findSiteBySiteNo(filterByStoreNo.getValue()));
        } else { grid.setItems(siteService.findAllUnCheckedOutSites(null));}
    }
}
