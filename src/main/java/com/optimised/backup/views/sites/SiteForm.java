package com.optimised.backup.views.sites;

import com.optimised.backup.data.entity.Site;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.shared.Registration;

public class SiteForm extends FormLayout {
    public void setSite(Site site) {
        binder.setBean(site);
    }

    IntegerField storeNumber = new IntegerField("Store Number");
    TextField name = new TextField("Name");
    IntegerField siteNumber = new IntegerField("Site Number");
    Button save = new Button("Save");
    Button close = new Button("Cancel");
    Binder<Site> binder = new BeanValidationBinder<>(Site.class);


    public SiteForm() {
        addClassName("site-form");
        binder.bindInstanceFields(this);
        name.setReadOnly(true);
        siteNumber.setReadOnly(true);
        add(
                storeNumber,
                name,
                siteNumber,
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
    public static abstract class SiteFormEvent extends ComponentEvent<SiteForm> {
        private Site site;

        protected SiteFormEvent(SiteForm source, Site site) {
            super(source, false);
            this.site = site;
        }

        public Site getSite() {
            return site;
        }
    }

    public static class SaveEvent extends SiteFormEvent {
        SaveEvent(SiteForm source, Site site) {
            super(source, site);
        }
    }

    public static class DeleteEvent extends SiteFormEvent {
        DeleteEvent(SiteForm source, Site site) {
            super(source, site);
        }

    }

    public static class CloseEvent extends SiteFormEvent {
        CloseEvent(SiteForm source) {
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
