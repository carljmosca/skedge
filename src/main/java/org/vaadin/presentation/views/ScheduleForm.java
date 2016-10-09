/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.vaadin.presentation.views;

import com.vaadin.ui.Component;
import com.vaadin.ui.TextField;
import com.vaadin.ui.themes.ValoTheme;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import org.vaadin.backend.ScheduleService;
import org.vaadin.backend.domain.PersonStatus;
import org.vaadin.backend.domain.ScheduleHeader;
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
    TextField firstName = new MTextField("First name").withFullWidth();
    TextField lastName = new MTextField("Last name").withFullWidth();
    // Select to another entity, options are populated in the init method
    TypedSelect<PersonStatus> status = new TypedSelect().
            withCaption("Status");
    TextField email = new MTextField("Email").withFullWidth();

    @Override
    protected Component createContent() {

        setStyleName(ValoTheme.LAYOUT_CARD);

        return new MVerticalLayout(
                new Header("Edit schedule").setHeaderLevel(3),
                new MFormLayout(
                        firstName,
                        lastName,
                        email,
                        status
                ).withFullWidth(),
                getToolbar()
        ).withStyleName(ValoTheme.LAYOUT_CARD);
    }

    /* "CDI interface" to notify decoupled components. Using traditional API to
     * other componets would probably be easier in small apps, but just
     * demonstrating here how all CDI stuff is available for Vaadin apps.
     */
    @Inject
    @PersonEvent(PersonEvent.Type.SAVE)
    javax.enterprise.event.Event<ScheduleHeader> saveEvent;

    @Inject
    @PersonEvent(PersonEvent.Type.REFRESH)
    javax.enterprise.event.Event<ScheduleHeader> refrehsEvent;

    @Inject
    @PersonEvent(PersonEvent.Type.DELETE)
    javax.enterprise.event.Event<ScheduleHeader> deleteEvent;
}
