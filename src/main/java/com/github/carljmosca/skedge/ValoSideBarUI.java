/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.carljmosca.skedge;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Widgetset;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.shared.ui.label.ContentMode;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.i18n.annotation.EnableI18N;
import org.vaadin.spring.sidebar.components.AbstractSideBar;
import org.vaadin.spring.sidebar.components.ValoSideBar;

@SpringUI
@Theme("mytheme") // A custom theme based on Valo
@Widgetset("AppWidgetset")
@EnableI18N
public class ValoSideBarUI extends AbstractSideBarUI {

    private static final long serialVersionUID = 8354731325136858956L;

    @Autowired
    ValoSideBar sideBar;
    @Autowired
    private HttpSession httpSession;

    public static final String ERROR_ATTRIBUTE = "ERROR";
    public static final String EMAIL_ATTRIBUTE = "EMAIL";

    protected boolean redactionNoticedAcknowledged;
    private final String requiredText = "<font color=\"red\"><span style=\"float:right\">* = Required</span></font>";
    protected String error;
    protected String email;
    protected boolean loggedIn;
    protected String court;

    public ValoSideBarUI() {

    }

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        super.init(vaadinRequest);

        CssLayout header = new CssLayout();
        Label lblTitle = new Label("<center>Sledge</center>");

        error = (String) httpSession.getAttribute(ERROR_ATTRIBUTE);
        email = (String) httpSession.getAttribute(EMAIL_ATTRIBUTE);
        //loggedIn = email != null && !email.isEmpty();
        loggedIn = true;

        lblTitle.addStyleName(ValoTheme.LABEL_H2);
        lblTitle.setContentMode(ContentMode.HTML);
        lblTitle.setSizeFull();

        header.addComponent(lblTitle);

        sideBar.setHeader(header);
        sideBar.setVisible(loggedIn);
        if (!loggedIn) {

        } else {

        }
    }

    @Override
    protected AbstractSideBar getSideBar() {
        return sideBar;
    }

    public String getRequiredText() {
        return requiredText;
    }

    public static ValoSideBarUI get() {
        return (ValoSideBarUI) UI.getCurrent();
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public String getEmail() {
        return email;
    }

}
