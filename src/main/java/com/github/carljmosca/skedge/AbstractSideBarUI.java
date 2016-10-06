/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.carljmosca.skedge;

import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.navigator.SpringViewProvider;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.sidebar.components.AbstractSideBar;

public abstract class AbstractSideBarUI extends UI {

    private static final long serialVersionUID = -7747249047198990160L;

    @Autowired
    SpringViewProvider viewProvider;

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        getPage().setTitle("Skedge");
        final HorizontalLayout rootLayout = new HorizontalLayout();
        rootLayout.setSizeFull();
        setContent(rootLayout);

        final VerticalLayout viewContainer = new VerticalLayout();
        viewContainer.setSizeFull();

        final Navigator navigator = new Navigator(this, viewContainer);
        navigator.setErrorView(new ErrorView());
        navigator.addProvider(viewProvider);
        setNavigator(navigator);

        rootLayout.addComponent(getSideBar());
        rootLayout.addComponent(viewContainer);
        rootLayout.setExpandRatio(viewContainer, 1.0f);
    }

    protected abstract AbstractSideBar getSideBar();

    private class ErrorView extends BaseView {

        private static final long serialVersionUID = -1349484555495574658L;
        private final Label message;

        ErrorView() {
            setMargin(true);
            addComponent(message = new Label());
        }

        @Override
        public void enter(ViewChangeListener.ViewChangeEvent event) {
            message.setValue(String.format("Selected option is not available: %s", event.getViewName()));
        }
    }

    public void setItemFilter(AbstractSideBar.ItemFilter itemFilter) {

    }

}
