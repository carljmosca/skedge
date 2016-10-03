/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.carljmosca.skedge.view;

import com.github.carljmosca.skedge.BaseView;
import com.github.carljmosca.skedge.Sections;
import com.vaadin.server.FontAwesome;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.ViewScope;
import org.vaadin.spring.sidebar.annotation.FontAwesomeIcon;
import org.vaadin.spring.sidebar.annotation.SideBarItem;

/**
 *
 * @author moscac
 */
@SpringView(name = SettingsView.VIEW_NAME)
@SideBarItem(sectionId = Sections.MAIN,
        caption = "Settings",
        order = 900)
@FontAwesomeIcon(FontAwesome.INFO)
@ViewScope
public class SettingsView extends BaseView {

    public static final String VIEW_NAME = "settings";
    private static final long serialVersionUID = -8161532583470696182L;

}
