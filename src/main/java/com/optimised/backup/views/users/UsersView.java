package com.optimised.backup.views.users;

import com.optimised.backup.data.entity.User;
import com.optimised.backup.data.service.UserService;
import com.optimised.backup.security.AuthenticatedUser;
import com.optimised.backup.security.SecurityConfiguration;
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
@PageTitle("Cylon Backup Manager")
@Route(value = "users", layout = MainLayout.class)
public class UsersView extends VerticalLayout {

    Grid<User> grid= new Grid<>(User.class);
    UserForm form;
    UserService userService;
    SecurityConfiguration securityConfiguration;
    AuthenticatedUser authenticatedUser;
    public UsersView(UserService userService, SecurityConfiguration securityConfiguration,AuthenticatedUser authenticatedUser){
        this.userService = userService;
        this.securityConfiguration = securityConfiguration;
        this.authenticatedUser = authenticatedUser;
        addClassName("list-view");
        setSizeFull();
        configureGrid();
        configureForm();
        add(getToolbar(),getContent());
        updateList();
        closeEditor();
    }

    private HorizontalLayout getToolbar() {

        Button addUserButton = new Button("Add User");
        addUserButton.addClickListener(click -> addUser());
        var toolbar = new HorizontalLayout(addUserButton);
        toolbar.addClassName("toolbar");
        return toolbar;
    }

    private void addUser() {
        grid.asSingleSelect().clear();
        editUser(new User());
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
        form = new UserForm(userService, securityConfiguration, authenticatedUser);
        form.setWidth("25em");
        form.addSaveListener(this::saveUser);
        form.addDeleteListener(this::deleteUser);
        form.addCloseListener(e -> closeEditor());
    }

    private void saveUser(UserForm.SaveEvent event) {
        userService.save(event.getUser());
        updateList();
        closeEditor();
    }

    private void deleteUser(UserForm.DeleteEvent event) {
        userService.delete(event.getUser().getId());
        updateList();
        closeEditor();
    }

    private void configureGrid(){
        grid.addClassNames("user-grid");
        grid.setSizeFull();
        grid.setColumns("name","username");
        grid.getColumns().forEach(col -> col.setAutoWidth(true));
        grid.asSingleSelect().addValueChangeListener(event -> editUser(event.getValue()));
    }

    public void editUser(User user) {
        if (user == null) {
            closeEditor();
        } else {

            form.setUser(user);
            form.setVisible(true);
            addClassName("editing");
        }
    }

    private void closeEditor() {
        form.setUser(null);
        form.setVisible(false);
        removeClassName("editing");
    }

    private void updateList() {
        grid.setItems(userService.findAllUsers());
    }
}
