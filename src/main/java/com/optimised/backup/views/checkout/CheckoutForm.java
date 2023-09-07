package com.optimised.backup.views.checkout;

import com.optimised.backup.data.entity.Engineer;
import com.optimised.backup.data.entity.Site;
import com.optimised.backup.data.service.EmailService;
import com.optimised.backup.data.service.EngineerService;
import com.optimised.backup.data.service.PathService;
import com.optimised.backup.data.service.SiteService;
import com.optimised.backup.tools.BackupTools;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.shared.Registration;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

public class CheckoutForm extends FormLayout {

    EmailService emailService;
    PathService pathService;

    @Autowired
    EngineerService engineerService;

    SiteService siteService;

    BackupTools backupTools = new BackupTools();

    public void setSite(Site site) {
        binder.setBean(site);
    }

    IntegerField storeNumber = new IntegerField("Store Number");
    TextField name = new TextField("Name");
    IntegerField siteNumber = new IntegerField("Site Number");
    ComboBox<Engineer> engineer = new ComboBox<>();
    Button save = new Button("Save");
    Button close = new Button("Cancel");
    Binder<Site> binder = new BeanValidationBinder<>(Site.class);

    @Autowired
    public CheckoutForm(EngineerService engineerService, EmailService emailService, PathService pathService, SiteService siteService) {
        addClassName("site-form");
        binder.bindInstanceFields(this);
        storeNumber.setReadOnly(true);
        name.setReadOnly(true);
        engineer.setItems(engineerService.findAllEngineers());
        engineer.setItemLabelGenerator(Engineer::getFullName);
        siteNumber.setReadOnly(true);
        this.engineerService = engineerService;
        this.emailService = emailService;
        this.pathService = pathService;
        this.siteService= siteService;
        add(
                storeNumber,
                name,
                siteNumber,
                engineer,
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
    public static abstract class CheckoutFormEvent extends ComponentEvent<CheckoutForm> {
        private Site site;

        protected CheckoutFormEvent(CheckoutForm source, Site site) {
            super(source, false);
            this.site = site;
        }

        public Site getSite() {
            return site;
        }
    }

    public class SaveEvent extends CheckoutFormEvent {
        SaveEvent(CheckoutForm source, Site site) {
            super(source, site);
            if (site.getEngineer().getId() != 1) {
                try {
                    String workingPath = pathService.getWorkingPath();
                    String cylonPath = pathService.getCylonPath();
                    String backupPath = pathService.getBackupPath();
                    String backupOldPath = pathService.getBackupOldPath();

                    String ccbFile = backupTools.Backup(site,cylonPath,workingPath,backupPath,backupOldPath,siteService);
                    emailService.sendMessageWithAttachment(site.getEngineer().getEmail(), "Cylon Backup", "Please rename from *.zip to *.ccb. This backup should be deleted fom you PC once you have returned it to the Bureau", ccbFile);
                    Notification.show("Email send to engineer");
                } catch (IOException e) {
                    Notification.show("Failed to send email to engineer");
                    e.printStackTrace();
                } catch (MessagingException e) {
                    Notification.show("Failed to send email to engineer");
                    e.printStackTrace();
                }
            }
        }
    }

    public static class DeleteEvent extends CheckoutFormEvent {
        DeleteEvent(CheckoutForm source, Site site) {
            super(source, site);
        }

    }

    public static class CloseEvent extends CheckoutFormEvent {
        CloseEvent(CheckoutForm source) {
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
