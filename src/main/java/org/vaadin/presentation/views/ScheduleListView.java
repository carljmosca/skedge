/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.vaadin.presentation.views;

import com.vaadin.cdi.CDIView;
import com.vaadin.event.FieldEvents;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import java.util.ArrayList;
import javax.annotation.PostConstruct;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import org.vaadin.backend.ScheduleService;
import org.vaadin.backend.domain.ScheduleHeader;
import org.vaadin.cdiviewmenu.ViewMenuItem;
import org.vaadin.presentation.AppUI;
import org.vaadin.presentation.ScreenSize;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.fields.MTable;
import org.vaadin.viritin.fields.MValueChangeEvent;
import org.vaadin.viritin.fields.MValueChangeListener;
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

    @Inject
    private ScheduleService service;
    @Inject
    ScheduleForm scheduleEditor;

    // Introduce and configure some UI components used on this view
    MTable<ScheduleHeader> scheduleTable = new MTable(ScheduleHeader.class).withFullWidth().
            withFullHeight();

    MHorizontalLayout mainContent = new MHorizontalLayout(scheduleTable).
            withFullWidth().withMargin(false);

    TextField filter = new TextField();

    Header header = new Header("Schedules").setHeaderLevel(2);

    Button addButton = new MButton(FontAwesome.EDIT, (Button.ClickEvent event) -> {
        addSchedule();
    });

    @PostConstruct
    private void init() {
        /*
         * Add value change listener to table that opens the selected customer into
         * an editor.
         */
        scheduleTable.addMValueChangeListener(new MValueChangeListener<ScheduleHeader>() {

            @Override
            public void valueChange(MValueChangeEvent<ScheduleHeader> event) {
                editSchedule(event.getValue());
            }
        });

        /*
         * Configure the filter input and hook to text change events to
         * repopulate the table based on given filter. Text change
         * events are sent to the server when e.g. user holds a tiny pause
         * while typing or hits enter.
         * */
        filter.setInputPrompt("Filter schedules...");
        filter.addTextChangeListener(new FieldEvents.TextChangeListener() {
            @Override
            public void textChange(FieldEvents.TextChangeEvent textChangeEvent) {
                listSchedules(textChangeEvent.getText());
            }
        });


        /* "Responsive Web Design" can be done with plain Java as well. Here we
         * e.g. do selective layouting and configure visible columns in
         * table based on available width */
        layout();
        adjustTableColumns();
        /* If you wish the UI to adapt on window resize/page orientation
         * change, hook to BrowserWindowResizeEvent */
        UI.getCurrent().setResizeLazy(true);
        Page.getCurrent().addBrowserWindowResizeListener(
                new Page.BrowserWindowResizeListener() {
            @Override
            public void browserWindowResized(
                    Page.BrowserWindowResizeEvent browserWindowResizeEvent) {
                adjustTableColumns();
                layout();
            }
        });

        listSchedules();
    }

    private void layout() {
        removeAllComponents();
        if (ScreenSize.getScreenSize() == ScreenSize.LARGE) {
            addComponents(
                    new MHorizontalLayout(header, filter, addButton)
                            .expand(header)
                            .alignAll(Alignment.MIDDLE_LEFT),
                    mainContent
            );

            filter.setSizeUndefined();
        } else {
            addComponents(
                    header,
                    new MHorizontalLayout(filter, addButton)
                            .expand(filter)
                            .alignAll(Alignment.MIDDLE_LEFT),
                    mainContent
            );
        }
        setMargin(new MarginInfo(false, true, true, true));
        expand(mainContent);
    }

    private void adjustTableColumns() {
        if (ScreenSize.getScreenSize() == ScreenSize.LARGE) {
            scheduleTable.setVisibleColumns("description", "beginDate", "endDate");
            scheduleTable.setColumnHeaders("Description", "Begin Date", "End Date");
        } else {
            // Only show one (generated) column with combined first + last name
//            if (scheduleTable.getColumnGenerator("name") == null) {
//                scheduleTable.addGeneratedColumn("name",
//                        new Table.ColumnGenerator() {
//                    @Override
//                    public Object generateCell(Table table, Object o,
//                            Object o2) {
//                        Person c = (Person) o;
//                        return c.getFirstName() + " " + c.getLastName();
//                    }
//                });
//            }
            if (ScreenSize.getScreenSize() == ScreenSize.MEDIUM) {
                scheduleTable.setVisibleColumns("description", "beginDate");
                scheduleTable.setColumnHeaders("Description", "Begin");
            } else {
                scheduleTable.setVisibleColumns("description");
                scheduleTable.setColumnHeaders("Description");
            }
        }
    }

    void listSchedules() {
        if (filter.getValue() == null) {
            scheduleTable.setBeans(new ArrayList<>(service.findAll()));
            scheduleTable.sort();
        } else {
            listSchedules(filter.getValue());
        }
    }

    void listSchedules(String filterString) {
        scheduleTable.setBeans(new ArrayList<>(service.findByDescription(filterString)));
        scheduleTable.sort();
    }

    void editSchedule(ScheduleHeader scheduleHeader) {
        if (scheduleHeader != null) {
            openEditor(scheduleHeader);
        } else {
            closeEditor();
        }
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

    private void closeEditor() {
        // As we display the editor differently in different devices,
        // close properly in each modes
        if (scheduleEditor.getParent() == mainContent) {
            mainContent.removeComponent(scheduleEditor);
        } else {
            AppUI.get().getContentLayout().
                    replaceComponent(scheduleEditor, this);
        }
    }

    /* These methods gets called by the CDI event system, which is also
     * available for Vaadin UIs when using Vaadin CDI add-on. In this
     * example events are arised from CustomerForm. The CDI event system
     * is a great mechanism to decouple components.
     */
    void saveSchedule(@Observes @ScheduleEvent(ScheduleEvent.Type.SAVE) ScheduleHeader schedule) {
        listSchedules();
        closeEditor();
    }

    void resetSchedule(@Observes @ScheduleEvent(ScheduleEvent.Type.REFRESH) ScheduleHeader schedule) {
        listSchedules();
        closeEditor();
    }

    void deleteSchedule(@Observes @ScheduleEvent(ScheduleEvent.Type.DELETE) ScheduleHeader schedule) {
        closeEditor();
        listSchedules();
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
    }

}
