package com.interview.frontend.view;

import com.interview.frontend.model.InterviewDto;
import com.interview.frontend.service.InterviewService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

@Route
public class MainView extends VerticalLayout {

    final Grid<InterviewDto> grid;
    final TextField filterName;
    final TextField filterSurname;
    final TextField filterEmail;
    final TextField filterMobile;
    private final InterviewService service;
    private final InterviewEditor editor;
    private final Button addNewBtn;

    public MainView(InterviewService service, InterviewEditor editor) {
        this.service = service;
        this.editor = editor;
        this.grid = new Grid<>(InterviewDto.class);
        this.filterName = new TextField();
        this.filterSurname = new TextField();
        this.filterEmail = new TextField();
        this.filterMobile = new TextField();
        this.addNewBtn = new Button("New customer", VaadinIcon.PLUS.create());

        filterName.setWidth("140px");
        filterSurname.setWidth("140px");
        filterEmail.setWidth("140px");
        filterMobile.setWidth("140px");

        // add filters
        HorizontalLayout addBtnAction = new HorizontalLayout(addNewBtn);
        HorizontalLayout filterActions = new HorizontalLayout(filterName, filterSurname, filterEmail, filterMobile);
        add(addBtnAction,filterActions, grid, editor);

        grid.setHeight("300px");
        grid.setColumns("id", "name", "surname", "email", "mobile", "interviewDateTime", "salaryExpectation", "commentForCandidate");
        grid.getColumnByKey("id").setWidth("50px").setFlexGrow(0);

        filterName.setPlaceholder("Find by Name");
        filterSurname.setPlaceholder("Find by Surname");
        filterEmail.setPlaceholder("Find by Email");
        filterMobile.setPlaceholder("Find by Mobile");

        // Replace listing with filtered content when user changes filters
        AtomicReference<String> name = new AtomicReference<>();
        AtomicReference<String> surname = new AtomicReference<>();
        AtomicReference<String> email = new AtomicReference<>();
        AtomicReference<String> mobile = new AtomicReference<>();

        // handle name filter event
        filterName.setValueChangeMode(ValueChangeMode.EAGER);
        filterName.addValueChangeListener(e -> {
            name.set(e.getValue());
            listCustomers(name.get(), surname.get(), email.get(), mobile.get());
        });

        // handle surname filter event
        filterSurname.setValueChangeMode(ValueChangeMode.EAGER);
        filterSurname.addValueChangeListener(e -> {
            surname.set(e.getValue());
            listCustomers(name.get(), surname.get(), email.get(), mobile.get());
        });

        // handle email filter event
        filterEmail.setValueChangeMode(ValueChangeMode.EAGER);
        filterEmail.addValueChangeListener(e -> {
            email.set(e.getValue());
            listCustomers(name.get(), surname.get(), email.get(), mobile.get());
        });

        // handle mobile filter event
        filterMobile.setValueChangeMode(ValueChangeMode.EAGER);
        filterMobile.addValueChangeListener(e -> {
            mobile.set(e.getValue());
            listCustomers(name.get(), surname.get(), email.get(), mobile.get());
        });


        // Connect selected Customer to editor or hide if none is selected
        grid.asSingleSelect().addValueChangeListener(e -> {
            editor.editCustomer(e.getValue());
        });

        // Instantiate and edit new Customer the new button is clicked
        addNewBtn.addClickListener(e -> editor.editCustomer(getEmptyInstance()));

        // Listen changes made by the editor, refresh data from backend
        editor.setChangeHandler(() -> {
            editor.setVisible(false);
            listCustomers(filterName.getValue(), filterSurname.getValue(), filterEmail.getValue(), filterMobile.getValue());
        });

        // Initialize listing
        listCustomers(null, null, null, null);
    }

    void listCustomers(String name, String surname, String email, String mobile) {
        if (isFiltersEmpty(name, surname, email, mobile)) {
            grid.setItems(service.getInterviews());
        } else {
            grid.setItems(service.searchInterviews(name, surname, email, mobile));
        }
    }

    private InterviewDto getEmptyInstance() {
        return InterviewDto.builder()
                .name("")
                .surname("")
                .mobile("")
                .email("")
                .salaryExpectation(0)
                .commentForCandidate("")
                .interviewDateTime(LocalDateTime.now())
                .build();
    }

    private boolean isFiltersEmpty(String name, String surname, String email, String mobile) {
        return (Objects.isNull(name) || name.isEmpty()) &&
                (Objects.isNull(surname) || surname.isEmpty()) &&
                (Objects.isNull(email) || email.isEmpty()) &&
                (Objects.isNull(mobile) || mobile.isEmpty());
    }

}
