/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.vaadin.presentation.views;

import com.vaadin.data.Container;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.DateField;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.themes.ValoTheme;
import java.util.ArrayList;
import javax.annotation.PostConstruct;
import javax.ejb.EJBException;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import org.vaadin.backend.ScheduleService;
import org.vaadin.backend.domain.ScheduleHeader;
import org.vaadin.backend.domain.Shift;
import org.vaadin.backend.domain.Weekday;
import org.vaadin.viritin.MBeanFieldGroup;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.fields.MTable;
import org.vaadin.viritin.fields.MTextField;
import org.vaadin.viritin.fields.TypedSelect;
import org.vaadin.viritin.form.AbstractForm;
import org.vaadin.viritin.label.Header;
import org.vaadin.viritin.layouts.MFormLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

/**
 *
 * @author moscac
 */
@Dependent
public class ScheduleForm extends AbstractForm<ScheduleHeader> {

    @Inject
    ScheduleService service;

    // Prepare some basic field components that our bound to entity property
    // by naming convetion, you can also use PropertyId annotation
    TextField description = new MTextField("Description").withFullWidth();
    DateField beginDate = new DateField("Begin Date");
    DateField endDate = new DateField("End Date");
    // Select to another entity, options are populated in the init method
    TypedSelect<Weekday> weekday = new TypedSelect().
            withCaption("Weekday");
    MTable<Shift> shiftTable = new MTable(Shift.class).withFullWidth().
            withHeight("150px");
    BeanItemContainer<Shift> shiftBeans;
    Button btnAddShift = new MButton(FontAwesome.PLUS_CIRCLE);
    Button btnDeleteShift = new MButton(FontAwesome.MINUS_CIRCLE);

    @Override
    protected Component createContent() {

        setStyleName(ValoTheme.LAYOUT_CARD);
        HorizontalLayout toolBar = getToolbar();
        btnAddShift.setDescription("Add shift");
        btnAddShift.addClickListener((Button.ClickEvent event) -> {
            Shift shift = new Shift();
            shift.setScheduleHeader(getEntity());
            shiftBeans.addBean(shift);
        });
        btnDeleteShift.addClickListener((Button.ClickEvent event) -> {
            if (shiftTable.getValue() != null) {
                shiftBeans.removeItem(shiftTable.getValue());
            }
        });
        btnDeleteShift.setDescription("Delete Shift");
        toolBar.addComponents(btnAddShift, btnDeleteShift);
        MVerticalLayout layout
                = new MVerticalLayout(
                        new Header("Edit schedule").setHeaderLevel(3),
                        new MFormLayout(
                                description,
                                beginDate,
                                endDate,
                                weekday
                        ).withFullWidth(),
                        shiftTable,
                        toolBar
                ).withStyleName(ValoTheme.LAYOUT_CARD);
        shiftBeans = new BeanItemContainer<>(Shift.class, new ArrayList<>());
        shiftTable.setContainerDataSource(shiftBeans);
        shiftTable.setVisibleColumns("weekday", "shiftTime", "employeeCount");
        shiftTable.setColumnHeaders("Weekday", "Shift Time", "Employee Count");
        //shiftTable.setColumnWidth("weekday", 10);
        //shiftTable.setColumnWidth("employeeCount", 5);
        shiftTable.setEditable(true);
        shiftTable.setImmediate(true);
        weekday.setOptions(Weekday.values());
        shiftTable.setTableFieldFactory((Container container, Object itemId, Object propertyId, Component uiContext) -> {
            if ("weekday".equals(propertyId)) {
                TypedSelect<Weekday> wd = new TypedSelect().
                        withCaption("Weekday");
                wd.setOptions(Weekday.values());
                return wd;
            }
            return new TextField();
        });
        return layout;
    }

    @PostConstruct
    void init() {
        setEagerValidation(true);
        //status.setWidthUndefined();
        //status.setOptions(PersonStatus.values());
        setSavedHandler((ScheduleHeader entity1) -> {
            try {
                // make EJB call to save the entity
                if (entity1.getShifts() != null) {
                    entity1.getShifts().clear();
                    entity1.getShifts().addAll(shiftBeans.getItemIds());
                }
                service.saveOrPersist(entity1);
                // fire save event to let other UI components know about
                // the change
                saveEvent.fire(entity1);
            } catch (EJBException e) {
                /*
                * The Customer object uses optimitic locking with the
                * version field. Notify user the editing didn't succeed.
                 */
                Notification.show("Record was not saved "
                        + e.getMessage(),
                        Notification.Type.ERROR_MESSAGE);
                refrehsEvent.fire(entity1);
            }
        });
        setResetHandler((ScheduleHeader entity1) -> {
            refrehsEvent.fire(entity1);
        });
        setDeleteHandler((ScheduleHeader entity1) -> {
            service.deleteEntity(getEntity());
            deleteEvent.fire(getEntity());
        });
        shiftBeans = new BeanItemContainer<>(Shift.class);
        shiftTable.setContainerDataSource(shiftBeans);
    }

    @Override
    public MBeanFieldGroup<ScheduleHeader> setEntity(ScheduleHeader entity) {
        MBeanFieldGroup<ScheduleHeader> fg = super.setEntity(entity);
        shiftBeans.removeAllItems();
        if (getEntity() != null && getEntity().getShifts() != null) {
            shiftBeans.addAll(getEntity().getShifts());
        }
        return fg;
    }

    @Override
    protected void adjustResetButtonState() {
        // always enabled in this form
        getResetButton().setEnabled(true);
        getDeleteButton().setEnabled(getEntity() != null && getEntity().isPersisted());
    }

    /* "CDI interface" to notify decoupled components. Using traditional API to
     * other componets would probably be easier in small apps, but just
     * demonstrating here how all CDI stuff is available for Vaadin apps.
     */
    @Inject
    @ScheduleEvent(ScheduleEvent.Type.SAVE)
    javax.enterprise.event.Event<ScheduleHeader> saveEvent;

    @Inject
    @ScheduleEvent(ScheduleEvent.Type.REFRESH)
    javax.enterprise.event.Event<ScheduleHeader> refrehsEvent;

    @Inject
    @ScheduleEvent(ScheduleEvent.Type.DELETE)
    javax.enterprise.event.Event<ScheduleHeader> deleteEvent;
}
