package com.optimised.backup.views.about;

import com.optimised.backup.views.MainLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.flow.theme.lumo.LumoUtility.Margin;
import jakarta.annotation.security.RolesAllowed;
import org.vaadin.olli.FileDownloadWrapper;

import java.io.ByteArrayInputStream;
import java.io.IOException;

@PageTitle("About")
@Route(value = "about", layout = MainLayout.class)
@RouteAlias(value = "", layout = MainLayout.class)
@RolesAllowed({"ADMIN","USER"})
@AnonymousAllowed
public class AboutView extends VerticalLayout {

    public AboutView() {
        setSpacing(false);

        Image img = new Image("images/empty-plant.png", "placeholder plant");
        img.setWidth("200px");
        add(img);

        H2 header = new H2("This place intentionally left empty");
        header.addClassNames(Margin.Top.XLARGE, Margin.Bottom.MEDIUM);
        add(header);
        add(new Paragraph("It’s a place where you can grow your own UI 🤗"));
        Button test = new Button("Test");
        test.addClickListener(buttonClickEvent -> {
            Anchor anchor = new Anchor("sample.pdf", "Download PDF");
            anchor.getElement().setAttribute("download", "downloaded-file-name.pdf");
            add(anchor);
        });
        add(test);
        Button button = new Button("Click to download");
        FileDownloadWrapper buttonWrapper = new FileDownloadWrapper(
                new StreamResource("foo.txt", () -> new ByteArrayInputStream("foo".getBytes())));
        buttonWrapper.wrapComponent(button);
        add(buttonWrapper);


        setSizeFull();
        setJustifyContentMode(JustifyContentMode.CENTER);
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        getStyle().set("text-align", "center");
    }

}
