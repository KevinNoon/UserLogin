package com.optimised.backup.views.settings;

import com.optimised.backup.data.entity.Path;
import com.optimised.backup.data.service.PathService;
import com.optimised.backup.views.MainLayout;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;

@RolesAllowed("ADMIN")
@PageTitle("Paths")
@Route(value = "paths", layout = MainLayout.class)
public class PathsView extends VerticalLayout {

    Grid<Path> grid= new Grid<>(Path.class);
    PathForm form;
    PathService pathService;

    public PathsView(PathService pathService){
        this.pathService = pathService;
        addClassName("list-view");
        setSizeFull();
        configureGrid();
        configureForm();
        add(getToolbar(),getContent());
        updateList();
        closeEditor();
    }

    private HorizontalLayout getToolbar() {

        Button addPathButton = new Button("Add Path");
        addPathButton.addClickListener(click -> addPath());
        var toolbar = new HorizontalLayout(addPathButton);
        toolbar.addClassName("toolbar");
        return toolbar;
    }

    private void addPath() {
        grid.asSingleSelect().clear();
        editPath(new Path());
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
        form = new PathForm();
        form.setWidth("25em");
        form.addSaveListener(this::savePath);
        form.addCloseListener(e -> closeEditor());
    }

    private void savePath(PathForm.SaveEvent event) {
        pathService.savePath(event.getPath());
        updateList();
        closeEditor();
    }

    private void configureGrid(){
        grid.addClassNames("path-grid");
        grid.setSizeFull();
        grid.setColumns("name","path_value");
        grid.getColumns().forEach(col -> col.setAutoWidth(true));
        grid.asSingleSelect().addValueChangeListener(event -> editPath(event.getValue()));
    }

    public void editPath(Path path) {
        if (path == null) {
            closeEditor();
        } else {
            form.setPath(path);
            form.setVisible(true);
            addClassName("editing");
        }
    }

    private void closeEditor() {
        form.setPath(null);
        form.setVisible(false);
        removeClassName("editing");
    }

    private void updateList() {
        grid.setItems(pathService.findAllPaths());
    }
}
