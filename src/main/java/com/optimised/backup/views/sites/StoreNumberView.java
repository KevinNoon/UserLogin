package com.optimised.backup.views.sites;

import com.optimised.backup.data.entity.Site;
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

@RolesAllowed({"ADMIN","USER"})
@PageTitle("Store Number")
@Route(value = "store-number", layout = MainLayout.class)

public class StoreNumberView extends VerticalLayout {

    Grid<Site> grid= new Grid<>(Site.class);
    TextField filterByName = new TextField();
    IntegerField filterByStoreNo = new IntegerField();
    SiteForm form;
    SiteService siteService;

    public StoreNumberView(SiteService siteService){
        this.siteService = siteService;
        addClassName("list-view");
        setSizeFull();
        configureGrid();
        configureForm();
        add(getToolbar(), getContent());
        updateList();
        closeEditor();
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
        form = new SiteForm();
        form.setWidth("25em");
        form.addSaveListener(this::saveSite);
        form.addCloseListener(e -> closeEditor());
    }

    private void saveSite(SiteForm.SaveEvent event) {
        siteService.saveSite(event.getSite());
        updateList();
        closeEditor();
    }

    private void configureGrid(){
        grid.addClassNames("site-grid");
        grid.setSizeFull();
        grid.setColumns("storeNumber","name","siteNumber","directory","telephone","ipAddr","port");
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
        } else { grid.setItems(siteService.findAllSites(filterByName.getValue()));}
    }
}
