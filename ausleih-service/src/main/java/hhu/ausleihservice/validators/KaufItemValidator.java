package hhu.ausleihservice.validators;

import hhu.ausleihservice.databasemodel.AusleihItem;
import hhu.ausleihservice.databasemodel.KaufItem;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import java.time.LocalDate;

@Component
public class KaufItemValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		return AusleihItem.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		KaufItem item = (KaufItem) target;

		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "titel", Messages.notEmpty);
		if (item.getTitel().length() < 4 || item.getTitel().length() > 40) {
			errors.rejectValue("titel", Messages.sizeTitle);
		}

		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "beschreibung", Messages.notEmpty);
		if (item.getBeschreibung().length() < 4 || item.getBeschreibung().length() > 4000) {
			errors.rejectValue("beschreibung", Messages.sizeItemDescription);
		}

		if (item.getKaufpreis() != null) {
			if (item.getKaufpreis() <= 0) {
				errors.rejectValue("kaufpreis", Messages.negativeValue);
			}
		} else {
			errors.rejectValue("kaufpreis", Messages.notEmpty);
		}

		if (item.getAbholort() == null) {
			ValidationUtils.rejectIfEmpty(errors, "abholort", Messages.notEmpty);
		}
	}
}
