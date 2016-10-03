/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.carljmosca.skedge.view;

import com.github.carljmosca.skedge.BaseView;
import com.vaadin.server.FontAwesome;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.ViewScope;
import org.vaadin.spring.sidebar.annotation.FontAwesomeIcon;
import org.vaadin.spring.sidebar.annotation.SideBarItem;

/**
 *
 * @author moscac
 */
@SpringView(name = AboutView.VIEW_NAME)
@SideBarItem(sectionId = "",
        caption = "About",
        order = 10)
@FontAwesomeIcon(FontAwesome.INFO)
@ViewScope
public class AboutView extends BaseView {

    public static final String VIEW_NAME = "about";
    private static final long serialVersionUID = -3961976938437271017L;

}
