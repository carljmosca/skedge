package org.vaadin.presentation.views;

import java.util.ArrayList;

import javax.annotation.PostConstruct;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.vaadin.backend.PersonService;
import org.vaadin.backend.domain.Person;
import org.vaadin.cdiviewmenu.ViewMenuItem;
import org.vaadin.presentation.AppUI;
import org.vaadin.presentation.ScreenSize;
import org.vaadin.presentation.views.PersonEvent.Type;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.fields.MTable;
import org.vaadin.viritin.fields.MValueChangeEvent;
import org.vaadin.viritin.fields.MValueChangeListener;
import org.vaadin.viritin.label.Header;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

import com.vaadin.cdi.CDIView;
import com.vaadin.event.FieldEvents;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;

/**
 * A view that lists Customers in a Table and lets user to choose one for
 * editing. There is also RIA features like on the fly filtering.
 */
@CDIView("persons")
@ViewMenuItem(icon = FontAwesome.USERS, order = ViewMenuItem.BEGINNING)
public class PersonListView extends MVerticalLayout implements View {

    @Inject
    private PersonService service;

    @Inject
    PersonForm personEditor;

    // Introduce and configure some UI components used on this view
    MTable<Person> personTable = new MTable(Person.class).withFullWidth().
            withFullHeight();

    MHorizontalLayout mainContent = new MHorizontalLayout(personTable).
            withFullWidth().withMargin(false);

    TextField filter = new TextField();

    Header header = new Header("Customers").setHeaderLevel(2);

    Button addButton = new MButton(FontAwesome.EDIT,
            new Button.ClickListener() {

                @Override
                public void buttonClick(Button.ClickEvent event) {
                    addPerson();
                }
            });

    @PostConstruct
    public void init() {

        /*
         * Add value change listener to table that opens the selected customer into
         * an editor.
         */
        personTable.addMValueChangeListener(new MValueChangeListener<Person>() {

            @Override
            public void valueChange(MValueChangeEvent<Person> event) {
                editPerson(event.getValue());
            }
        });

        /*
         * Configure the filter input and hook to text change events to
         * repopulate the table based on given filter. Text change
         * events are sent to the server when e.g. user holds a tiny pause
         * while typing or hits enter.
         * */
        filter.setInputPrompt("Filter customers...");
        filter.addTextChangeListener(new FieldEvents.TextChangeListener() {
            @Override
            public void textChange(FieldEvents.TextChangeEvent textChangeEvent) {
                listPersons(textChangeEvent.getText());
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

        listPersons();
    }

    /**
     * Do the application layout that is optimized for the screen size.
     * <p>
     * Like in Java world in general, Vaadin developers can modularize their
     * helpers easily and re-use existing code. E.g. In this method we are using
     * extended versions of Vaadins basic layout that has "fluent API" and this
     * way we get bit more readable code. Check out vaadin.com/directory for a
     * huge amount of helper libraries and custom components. They might be
     * valuable for your productivity.
     * </p>
     */
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

    /**
     * Similarly to layouts, we can adapt component configurations based on the
     * client details. Here we configure visible columns so that a sane amount
     * of data is displayed for various devices.
     */
    private void adjustTableColumns() {
        if (ScreenSize.getScreenSize() == ScreenSize.LARGE) {
            personTable.setVisibleColumns("firstName", "lastName", "email",
                    "status");
            personTable.setColumnHeaders("First name", "Last name", "Email",
                    "Status");
        } else {
            // Only show one (generated) column with combined first + last name
            if (personTable.getColumnGenerator("name") == null) {
                personTable.addGeneratedColumn("name",
                        new Table.ColumnGenerator() {
                            @Override
                            public Object generateCell(Table table, Object o,
                                    Object o2) {
                                Person c = (Person) o;
                                return c.getFirstName() + " " + c.getLastName();
                            }
                        });
            }
            if (ScreenSize.getScreenSize() == ScreenSize.MEDIUM) {
                personTable.setVisibleColumns("name", "email");
                personTable.setColumnHeaders("Name", "Email");
            } else {
                personTable.setVisibleColumns("name");
                personTable.setColumnHeaders("Name");
            }
        }
    }

    /* ******* */
    // Controller methods.
    //
    // In a big project, consider using separate controller/presenter
    // for improved testability. MVP is a popular pattern for large
    // Vaadin applications.
    private void listPersons() {
        // Here we just fetch data straight from the EJB.
        //
        // If you expect a huge amount of data, do proper paging,
        // or use lazy loading.
        // See: https://github.com/viritin/viritin/wiki/Lazy-loading-in-Viritin
        if(filter.getValue() == null) {
            personTable.setBeans(new ArrayList<>(service.findAll()));
            personTable.sort();
        } else {
            listPersons(filter.getValue());
        }
    }

    private void listPersons(String filterString) {
        personTable.setBeans(new ArrayList<>(service.findByName(filterString)));
        personTable.sort();
    }

    void editPerson(Person person) {
        if (person != null) {
            openEditor(person);
        } else {
            closeEditor();
        }
    }

    void addPerson() {
        openEditor(new Person());
    }

    private void openEditor(Person person) {
        personEditor.setEntity(person);
        // display next to table on desktop class screens
        if (ScreenSize.getScreenSize() == ScreenSize.LARGE) {
            mainContent.addComponent(personEditor);
            personEditor.focusFirst();
        } else {
            // Replace this view with the editor in smaller devices
            AppUI.get().getContentLayout().
                    replaceComponent(this, personEditor);
        }
    }

    private void closeEditor() {
        // As we display the editor differently in different devices,
        // close properly in each modes
        if (personEditor.getParent() == mainContent) {
            mainContent.removeComponent(personEditor);
        } else {
            AppUI.get().getContentLayout().
                    replaceComponent(personEditor, this);
        }
    }

    /* These methods gets called by the CDI event system, which is also
     * available for Vaadin UIs when using Vaadin CDI add-on. In this
     * example events are arised from CustomerForm. The CDI event system
     * is a great mechanism to decouple components.
     */
    void saveCustomer(@Observes @PersonEvent(Type.SAVE) Person customer) {
        listPersons();
        closeEditor();
    }

    void resetPerson(@Observes @PersonEvent(Type.REFRESH) Person person) {
        listPersons();
        closeEditor();
    }

    void deletePerson(@Observes @PersonEvent(Type.DELETE) Person person) {
        closeEditor();
        listPersons();
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {

    }

}
