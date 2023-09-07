package com.optimised.backup.views.settings;

import com.optimised.backup.data.entity.Path;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.shared.Registration;

public class PathForm extends FormLayout {
    public void setPath(Path path) {
        binder.setBean(path);
    }

    TextField name = new TextField("Path");
    TextField path_value = new TextField("Value");
    Button save = new Button("Save");
    Button close = new Button("Cancel");
    Binder<Path> binder = new BeanValidationBinder<>(Path.class);


    public PathForm() {
        addClassName("path-form");
        binder.bindInstanceFields(this);
        add(
                name,
                path_value,
                createButtonsLayout());
    }

    private HorizontalLayout createButtonsLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        save.addClickShortcut(Key.ENTER);
        close.addClickShortcut(Key.ESCAPE);

        save.addClickListener(event -> validateAndSave());
        close.addClickListener(event -> fireEvent(new CloseEvent(this)));

        binder.addStatusChangeListener(e -> save.setEnabled(binder.isValid()));
        return new HorizontalLayout(save, close);
    }

    private void validateAndSave() {
        if (binder.isValid()) {
            fireEvent(new SaveEvent(this, binder.getBean()));
        }
    }
    public static abstract class pathFormEvent extends ComponentEvent<PathForm> {
        private Path path;

        protected pathFormEvent(PathForm source, Path path) {
            super(source, false);
            this.path = path;
        }

        public Path getPath() {
            return path;
        }
    }

    public static class SaveEvent extends pathFormEvent {
        SaveEvent(PathForm source, Path path) {
            super(source, path);
        }
    }

    public static class DeleteEvent extends pathFormEvent {
        DeleteEvent(PathForm source, Path path) {
            super(source, path);
        }

    }

    public static class CloseEvent extends pathFormEvent {
        CloseEvent(PathForm source) {
            super(source, null);
        }
    }

    public Registration addDeleteListener(ComponentEventListener<DeleteEvent> listener) {
        return addListener(DeleteEvent.class, listener);
    }

    public Registration addSaveListener(ComponentEventListener<SaveEvent> listener) {
        return addListener(SaveEvent.class, listener);
    }
    public Registration addCloseListener(ComponentEventListener<CloseEvent> listener) {
        return addListener(CloseEvent.class, listener);
    }
}
