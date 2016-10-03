/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.carljmosca.skedge;

import com.github.carljmosca.skedge.view.AboutView;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.HasComponents;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BaseView extends VerticalLayout implements View {

    protected boolean validated;
    private String spacer = new String();


    public BaseView() {

        UI.getCurrent().getNavigator().addViewChangeListener(new ViewChangeListener() {
            private static final long serialVersionUID = -7910182453780421758L;

            @Override
            public boolean beforeViewChange(ViewChangeListener.ViewChangeEvent event) {
                //fieldGroup.isValid()
                if (event.getOldView() == null
                        || event.getOldView() instanceof AboutView) {
                    return true;
                }
                BaseView baseView = (BaseView) event.getOldView();
                if (baseView.validated) {
                    return true;
                }
//                if (event.getOldView() instanceof xxView) {
//
//                }
                return persist(event.getOldView());
            }

            @Override
            public void afterViewChange(ViewChangeListener.ViewChangeEvent event) {
                BaseView baseView = (BaseView) event.getNewView();
                baseView.validated = false;

            }
        });
//        addComponent(claimProgressLayout);
//        addComponent(hLayoutStatusFooter);
    }

    protected void bind() {

    }

    private void processComponents(Iterator<Component> components) {
        while (components.hasNext()) {
            Component component = components.next();
            if (component instanceof Panel) {
            }
            if (component instanceof HasComponents) {
                HasComponents h = (HasComponents) component;
                processComponents(h.iterator());
            }
        }
    }

    protected void persistAndContinue(String nextView) {
        if (persist(null)) {
            UI.getCurrent().getNavigator().navigateTo(nextView);
        }
    }

    private boolean persist(View view) {
        if (view != null) {
            processPanels(view);
        }

        validated = true;
        return true;
    }

    private void validateFields() {
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
    }

    private void processPanels(View view) {
        BaseView baseView = (BaseView) view;
        for (Component c : baseView) {
            processComponent(c);
        }

    }

    private void processComponent(Component c) {
        if (c instanceof BaseView) {
            BaseView b = (BaseView) c;

        }
    }

    
}
