package hhu.ausleihservice.validators;

import hhu.ausleihservice.databasemodel.Person;
import hhu.ausleihservice.web.service.PersonService;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class PersonValidator implements Validator {

	private PersonService personService;

	public PersonValidator(PersonService personService) {
		this.personService = personService;
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return Person.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		Person person = (Person) target;

		if (person.getId() == null || personService.findOptionalById(person.getId()).isPresent()) {
			if (person.getPassword().length() != 0) {
				if (person.getPassword().trim().length() == 0) {
					errors.rejectValue("password", Messages.notEmpty);
				}
				if (person.getPassword().length() < 3 || person.getPassword().length() > 100) {
					errors.rejectValue("password", Messages.passwordSize);
				}
			}
		} else {
			ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", Messages.notEmpty);
			if (person.getPassword().length() < 3 || person.getPassword().length() > 100) {
				errors.rejectValue("password", Messages.passwordSize);
			}
		}

		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email", Messages.invalidEmail);
		// Email format Regular Expression from RFC 2822
		if (!(person.getEmail() == null)) {
			if (!person.getEmail().matches(
					"(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-"
							+ "]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-"
							+ "\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9]("
							+ "?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:"
							+ "25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]"
							+ "?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21"
							+ "-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])")) {
				errors.rejectValue("email", Messages.invalidEmail);
			}
		}
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "vorname", Messages.notEmpty);
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "nachname", Messages.notEmpty);
	}
}
