package hhu.ausleihservice.validators;

import hhu.ausleihservice.databasemodel.Abholort;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

public class AbholortValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		return Abholort.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		Abholort abholort = (Abholort) target;

		ValidationUtils.rejectIfEmptyOrWhitespace(errors,"beschreibung","NotEmpty");
		if (abholort.getBeschreibung().length() < 6 || abholort.getBeschreibung().length() > 400) {
			errors.rejectValue("beschreibung", "Size.abholortForm.beschreibung");
		}
	}
}
