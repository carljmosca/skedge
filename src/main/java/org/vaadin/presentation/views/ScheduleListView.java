/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.vaadin.presentation.views;

import com.vaadin.cdi.CDIView;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button;
import com.vaadin.ui.TextField;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import org.vaadin.backend.ScheduleService;
import org.vaadin.backend.domain.ScheduleHeader;
import org.vaadin.cdiviewmenu.ViewMenuItem;
import org.vaadin.presentation.AppUI;
import org.vaadin.presentation.ScreenSize;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.fields.MTable;
import org.vaadin.viritin.label.Header;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

/**
 *
 * @author moscac
 */
@CDIView("schedules")
@ViewMenuItem(icon = FontAwesome.CALENDAR, order = 100)
public class ScheduleListView extends MVerticalLayout implements View {

    //@Inject
    //private ScheduleService service;

    @Inject
    ScheduleForm scheduleEditor;
    
    // Introduce and configure some UI components used on this view
    MTable<ScheduleHeader> scheduleTable = new MTable(ScheduleHeader.class).withFullWidth().
            withFullHeight();

    MHorizontalLayout mainContent = new MHorizontalLayout(scheduleTable).
            withFullWidth().withMargin(false);

    TextField filter = new TextField();

    Header header = new Header("Schedules").setHeaderLevel(2);

    Button addButton = new MButton(FontAwesome.EDIT,
            new Button.ClickListener() {

        @Override
        public void buttonClick(Button.ClickEvent event) {
            addSchedule();
        }
    });

    @PostConstruct
    private void init() {

    }

    void addSchedule() {
        openEditor(new ScheduleHeader());
    }

    private void openEditor(ScheduleHeader scheduleHeader) {
        scheduleEditor.setEntity(scheduleHeader);
        // display next to table on desktop class screens
        if (ScreenSize.getScreenSize() == ScreenSize.LARGE) {
            mainContent.addComponent(scheduleEditor);
            scheduleEditor.focusFirst();
        } else {
            // Replace this view with the editor in smaller devices
            AppUI.get().getContentLayout().
                    replaceComponent(this, scheduleEditor);
        }
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {

    }

}
