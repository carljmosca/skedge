/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.vaadin.presentation.views;

import com.vaadin.ui.Component;
import com.vaadin.ui.DateField;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.themes.ValoTheme;
import javax.annotation.PostConstruct;
import javax.ejb.EJBException;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import org.vaadin.backend.ScheduleService;
import org.vaadin.backend.domain.ScheduleHeader;
import org.vaadin.viritin.fields.MTextField;
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
    //TypedSelect<PersonStatus> status = new TypedSelect().
    //        withCaption("Status");

    @Override
    protected Component createContent() {

        setStyleName(ValoTheme.LAYOUT_CARD);

        return new MVerticalLayout(
                new Header("Edit schedule").setHeaderLevel(3),
                new MFormLayout(
                        description,
                        beginDate,
                        endDate
                ).withFullWidth(),
                getToolbar()
        ).withStyleName(ValoTheme.LAYOUT_CARD);
    }

    @PostConstruct
    void init() {
        setEagerValidation(true);
        //status.setWidthUndefined();
        //status.setOptions(PersonStatus.values());
        setSavedHandler(new SavedHandler<ScheduleHeader>() {

            @Override
            public void onSave(ScheduleHeader entity) {
                try {
                    // make EJB call to save the entity
                    service.saveOrPersist(entity);
                    // fire save event to let other UI components know about
                    // the change
                    saveEvent.fire(entity);
                } catch (EJBException e) {
                    /*
                     * The Customer object uses optimitic locking with the 
                     * version field. Notify user the editing didn't succeed.
                     */
                    Notification.show("The schedule was concurrently edited "
                            + "by someone else. Your changes were discarded.",
                            Notification.Type.ERROR_MESSAGE);
                    refrehsEvent.fire(entity);
                }
            }
        });
        setResetHandler(new ResetHandler<ScheduleHeader>() {

            @Override
            public void onReset(ScheduleHeader entity) {
                refrehsEvent.fire(entity);
            }
        });
        setDeleteHandler(new DeleteHandler<ScheduleHeader>() {
            @Override
            public void onDelete(ScheduleHeader entity) {
                service.deleteEntity(getEntity());
                deleteEvent.fire(getEntity());
            }
        });
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
