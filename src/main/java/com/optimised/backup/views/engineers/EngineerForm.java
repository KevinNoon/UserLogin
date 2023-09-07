package com.optimised.backup.views.engineers;

import com.optimised.backup.data.entity.Engineer;
import com.optimised.backup.security.AuthenticatedUser;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.shared.Registration;

public class EngineerForm extends FormLayout {
    public void setEngineer(Engineer engineer) {
        binder.setBean(engineer);
    }

    TextField fore_name = new TextField("Fore Name");
    TextField last_name = new TextField("Last Name");
    EmailField email = new EmailField("eMail");
    Button save = new Button("Save");
    Button delete = new Button("Delete");
    Button close = new Button("Cancel");
    Binder<Engineer> binder = new BeanValidationBinder<>(Engineer.class);
    private final AuthenticatedUser authenticatedUser;

    public EngineerForm(AuthenticatedUser authenticatedUser) {
        this.authenticatedUser = authenticatedUser;
        addClassName("engineer-form");
        binder.bindInstanceFields(this);
        add(
                fore_name,
                last_name,
                email,
                createButtonsLayout());
    }

    private HorizontalLayout createButtonsLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        save.addClickShortcut(Key.ENTER);
        delete.addClickShortcut(Key.DELETE);
        close.addClickShortcut(Key.ESCAPE);

        save.addClickListener(event -> validateAndSave());
        delete.addClickListener(event -> validateAndDelete());
        close.addClickListener(event -> fireEvent(new CloseEvent(this)));

        binder.addStatusChangeListener(e -> save.setEnabled(binder.isValid()));
        return new HorizontalLayout(save,delete, close);
    }

    private void validateAndSave() {
        if (binder.isValid()) {
            fireEvent(new SaveEvent(this, binder.getBean()));
        }
    }
    public static abstract class EngineerFormEvent extends ComponentEvent<EngineerForm> {
        private Engineer engineer;

        protected EngineerFormEvent(EngineerForm source, Engineer engineer) {
            super(source, false);
            this.engineer = engineer;
        }

        public Engineer getEngineer() {
            return engineer;
        }
    }

    private void validateAndDelete(){
        if (binder.isValid()){
            fireEvent(new DeleteEvent(this,binder.getBean()));
        }
    }


    public static class SaveEvent extends EngineerFormEvent {
        SaveEvent(EngineerForm source, Engineer engineer) {
            super(source, engineer);
        }
    }

    public static class DeleteEvent extends EngineerFormEvent {
        DeleteEvent(EngineerForm source, Engineer engineer) {
            super(source, engineer);
        }

    }

    public static class CloseEvent extends EngineerFormEvent {
        CloseEvent(EngineerForm source) {
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
