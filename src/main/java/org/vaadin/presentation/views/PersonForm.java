package org.vaadin.presentation.views;

import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.vaadin.backend.PersonService;
import org.vaadin.backend.domain.Person;
import org.vaadin.backend.domain.PersonStatus;
import org.vaadin.viritin.fields.MTextField;
import org.vaadin.viritin.fields.TypedSelect;
import org.vaadin.viritin.form.AbstractForm;
import org.vaadin.viritin.label.Header;
import org.vaadin.viritin.layouts.MFormLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

import javax.annotation.PostConstruct;
import javax.ejb.EJBException;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;

/**
 * A UI component built to modify Customer entities. The used superclass
 * provides binding to the entity object and e.g. Save/Cancel buttons by
 * default. In larger apps, you'll most likely have your own customized super
 * class for your forms.
 * <p>
 * Note, that the advanced bean binding technology in Vaadin is able to take
 * advantage also from Bean Validation annotations that are used also by e.g.
 * JPA implementation. Check out annotations in Customer objects email field and
 * how they automatically reflect to the configuration of related fields in UI.
 * </p>
 */
@Dependent
public class PersonForm extends AbstractForm<Person> {

    @Inject
    PersonService service;

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
                new Header("Edit person").setHeaderLevel(3),
                new MFormLayout(
                        firstName,
                        lastName,
                        email,
                        status
                ).withFullWidth(),
                getToolbar()
        ).withStyleName(ValoTheme.LAYOUT_CARD);
    }

    @PostConstruct
    void init() {
        setEagerValidation(true);
        status.setWidthUndefined();
        status.setOptions(PersonStatus.values());
        setSavedHandler(new SavedHandler<Person>() {

            @Override
            public void onSave(Person entity) {
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
                    Notification.show("Record was not saved "
                            + e.getMessage(),
                            Notification.Type.ERROR_MESSAGE);
                    refrehsEvent.fire(entity);
                }
            }
        });
        setResetHandler(new ResetHandler<Person>() {

            @Override
            public void onReset(Person entity) {
                refrehsEvent.fire(entity);
            }
        });
        setDeleteHandler(new DeleteHandler<Person>() {
            @Override
            public void onDelete(Person entity) {
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
    @PersonEvent(PersonEvent.Type.SAVE)
    javax.enterprise.event.Event<Person> saveEvent;

    @Inject
    @PersonEvent(PersonEvent.Type.REFRESH)
    javax.enterprise.event.Event<Person> refrehsEvent;

    @Inject
    @PersonEvent(PersonEvent.Type.DELETE)
    javax.enterprise.event.Event<Person> deleteEvent;
}
