package com.optimised.backup.views.backup;

import com.optimised.backup.data.entity.Settings;
import com.optimised.backup.data.entity.Site;
import com.optimised.backup.data.service.EmailService;
import com.optimised.backup.data.service.PathService;
import com.optimised.backup.data.service.SettingsService;
import com.optimised.backup.data.service.SiteService;
import com.optimised.backup.tools.BackupTools;
import com.optimised.backup.views.MainLayout;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.timepicker.TimePicker;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.time.Duration;
import java.time.LocalTime;

@PageTitle("Backup")
@Route(value = "backup", layout = MainLayout.class)
@RolesAllowed({"ADMIN","USER"})
@Uses(Icon.class)
@AnonymousAllowed
public class BackupView extends VerticalLayout {

    Grid<Site> grid= new Grid<>(Site.class);
    TextField filterByName = new TextField();
    IntegerField filterByStoreNo = new IntegerField();

    SiteService siteService;
    PathService pathService;
    EmailService emailService;
    BackupTools backupTools;
    SettingsService settingsService;

    @Autowired
    public BackupView(SiteService siteService, PathService pathService, EmailService emailService, SettingsService settingsService){
        this.siteService = siteService;
        this.pathService = pathService;
        this.emailService = emailService;
        this.settingsService = settingsService;
        addClassName("list-view");
        setSizeFull();
        configureGrid();
        add(getToolbar(), getContent());
        updateList();
        backupTools = new BackupTools();
    }

    private Component getContent() {
        HorizontalLayout content = new HorizontalLayout(grid);
        content.addClassNames("content");
        content.setSizeFull();
        return content;
    }

    private void configureGrid(){
        grid.addClassNames("site-grid");
        grid.setSizeFull();
        grid.setColumns("storeNumber","name","siteNumber","directory","telephone","ipAddr","port");
        grid.getColumns().forEach(col -> col.setAutoWidth(true));
        grid.setSelectionMode(Grid.SelectionMode.MULTI);
    }


    private VerticalLayout getToolbar() {
        filterByName.setPlaceholder("Filter by name...");
        filterByName.setClearButtonVisible(true);
        filterByName.setValueChangeMode(ValueChangeMode.LAZY);
        filterByName.addValueChangeListener(e -> updateList());

        filterByStoreNo.setPlaceholder("Filter by store no...");
        filterByStoreNo.setClearButtonVisible(true);
        filterByStoreNo.setValueChangeMode(ValueChangeMode.LAZY);
        filterByStoreNo.addValueChangeListener(e -> updateList());



        Button addBackupButton = new Button("Backup");
        TimePicker timePicker = new TimePicker();
        timePicker.setValue(LocalTime.of(1, 0));
        timePicker.setStep(Duration.ofMinutes(15));
        timePicker.setValue(settingsService.getSettings().getBackupTime());
        timePicker.addValueChangeListener(timePickerLocalTimeComponentValueChangeEvent -> {
            Settings settings = settingsService.getSettings();
            settings.setBackupTime(timePicker.getValue());
            settingsService.saveSettings(settings);
        });
        Checkbox checkBox = new Checkbox();
        checkBox.setLabel("Autobackup");

        checkBox.setValue(settingsService.getSettings().getAutoBackup());
        checkBox.addClickListener(checkboxClickEvent -> {
            Settings settings = settingsService.getSettings();
            settings.setAutoBackup(checkBox.getValue());
            settingsService.saveSettings(settings);
        });

        String workingPath = pathService.getWorkingPath();
        String cylonPath = pathService.getCylonPath();
        String backupPath = pathService.getBackupPath();
        String backupOldPath = pathService.getBackupOldPath();
        File theDir = new File(backupPath);
        if (!theDir.exists()) {
            theDir.mkdirs();
        }
        addBackupButton.addClickListener(clickEvent -> {
                    grid.getSelectedItems().forEach(site -> {
                        backupTools.Backup(site,cylonPath,workingPath,backupPath,backupOldPath,siteService);
                    });
                }
        );
        var topToolbar = new HorizontalLayout(checkBox,timePicker,addBackupButton);
        var bottomToolbar = new HorizontalLayout(filterByStoreNo,filterByName);
        var toolbar = new VerticalLayout(topToolbar,bottomToolbar);
        toolbar.addClassName("toolbar");
        return toolbar;
    }

    private void updateList() {
        if (!filterByStoreNo.isEmpty()){
            grid.setItems(siteService.findSiteBySiteNo(filterByStoreNo.getValue()));
        } else { grid.setItems(siteService.findAllSites(filterByName.getValue()));}
    }
}
