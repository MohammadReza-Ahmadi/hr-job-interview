package com.interview.frontend.view;

import com.interview.frontend.model.InterviewDto;
import com.interview.frontend.service.InterviewService;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.KeyNotifier;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.converter.StringToIntegerConverter;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.beans.factory.annotation.Autowired;

@SpringComponent
@UIScope
public class InterviewEditor extends VerticalLayout implements KeyNotifier {

	private final InterviewService service;
	private InterviewDto interviewDto;

	/* Fields to edit properties in Customer entity */
	TextField name = new TextField("Name");
	TextField surname = new TextField("Surname");
	TextField mobile = new TextField("Mobile");
	TextField email = new TextField("Email");
	TextField error = new TextField("Error");
	DateTimePicker interviewDateTime = new DateTimePicker("Interview Datetime");
	TextField salaryExpectation = new TextField("Salary Expectation");
	TextField commentForCandidate = new TextField("Comment For Candidate");

	Button save = new Button("Save", VaadinIcon.CHECK.create());
	Button cancel = new Button("Cancel");
	Button delete = new Button("Delete", VaadinIcon.TRASH.create());

	HorizontalLayout actions = new HorizontalLayout(save, cancel, delete);
	HorizontalLayout errors = new HorizontalLayout(error);
	Binder<InterviewDto> binder = new Binder<>(InterviewDto.class);
	private ChangeHandler changeHandler;

	@Autowired
	public InterviewEditor(InterviewService service) {
		this.service = service;
		this.error.setWidth("180px");
		this.error.getStyle().set("color", "#FF0000");
		restError();
		add(name, surname,mobile,email,interviewDateTime,salaryExpectation,commentForCandidate, actions,errors);

		binder.forField(salaryExpectation)
				.withConverter(new StringToIntegerConverter("Not a number"))
				.bind("salaryExpectation")
		;

		// bind using naming convention
		binder.bindInstanceFields(this);

		// Configure and style components
		setSpacing(true);

		save.getElement().getThemeList().add("primary");
		delete.getElement().getThemeList().add("error");

		addKeyPressListener(Key.ENTER, e -> save());

		// wire action buttons to save, delete and reset
		save.addClickListener(e -> save());
		delete.addClickListener(e -> delete());
		cancel.addClickListener(e -> editCustomer(interviewDto));
		setVisible(false);
	}

	void delete() {
		service.deleteInterviewById(interviewDto.getId());
		changeHandler.onChange();
	}

	void save() {
		try {
			service.createInterview(interviewDto);
			changeHandler.onChange();
			restError();
		} catch (Exception e) {
			error.setValue(e.getMessage());
			error.setVisible(true);
		}

	}

	private void restError() {
		error.setValue("");
		error.setVisible(false);
	}

	public interface ChangeHandler {
		void onChange();
	}

	public final void editCustomer(InterviewDto dto) {
		if (dto == null) {
			setVisible(false);
			return;
		}
		final boolean persisted = dto.getId() != null;
		if (persisted) {
			// Find fresh entity for editing
			interviewDto = service.getInterviewById(dto.getId());

		} else {
			interviewDto = dto;
		}

		cancel.setVisible(persisted);
		binder.setBean(interviewDto);
		setVisible(true);

		// Focus first name initially
		name.focus();
	}

	public void setChangeHandler(ChangeHandler h) {
		// ChangeHandler is notified when either save or delete is clicked
		changeHandler = h;
	}

}
