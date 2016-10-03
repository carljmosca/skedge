/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.carljmosca.skedge.view;

import com.github.carljmosca.skedge.BaseView;
import com.github.carljmosca.skedge.Sections;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.ViewScope;
import com.vaadin.ui.Label;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.VerticalLayout;
import javax.annotation.PostConstruct;
import org.vaadin.spring.sidebar.annotation.FontAwesomeIcon;
import org.vaadin.spring.sidebar.annotation.SideBarItem;

@SpringView(name = PersonView.VIEW_NAME)
@SideBarItem(sectionId = Sections.MAIN,
        caption = "People",
        order = 30)
@FontAwesomeIcon(FontAwesome.USERS)
@ViewScope
public class PersonView extends BaseView {

    public static final String VIEW_NAME = "person";
    private static final long serialVersionUID = -4248559650887912416L;
    
    private TabSheet tabSheet;
    private VerticalLayout detailLayout;
    private VerticalLayout allLayout;

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
    }

    public PersonView() {

    }

    @PostConstruct
    void init() {

        setSizeFull();
        
        tabSheet = new TabSheet();
        addComponent(tabSheet);
        
        allLayout = new VerticalLayout();
        detailLayout = new VerticalLayout();

        tabSheet.addTab(allLayout, "All");        
        tabSheet.addTab(detailLayout, "Detail");

        allLayout.addComponent(new Label("test"));
        
        bind();

    }
}
