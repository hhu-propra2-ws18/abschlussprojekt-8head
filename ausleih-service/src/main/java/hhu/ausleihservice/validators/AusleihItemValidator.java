package hhu.ausleihservice.validators;

import hhu.ausleihservice.databasemodel.AusleihItem;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import java.time.LocalDate;

@Component
public class AusleihItemValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		return AusleihItem.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		AusleihItem item = (AusleihItem) target;

		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "titel", Messages.notEmpty);
		if (item.getTitel().length() < 4 || item.getTitel().length() > 40) {
			errors.rejectValue("titel", Messages.sizeTitle);
		}

		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "beschreibung", Messages.notEmpty);
		if (item.getBeschreibung().length() < 4 || item.getBeschreibung().length() > 4000) {
			errors.rejectValue("beschreibung", Messages.sizeItemDescription);
		}

		if (item.getKautionswert() != null) {
			if (item.getKautionswert() <= 0) {
				errors.rejectValue("kautionswert", Messages.negativeValue);
			}
		} else {
			errors.rejectValue("kautionswert", Messages.notEmpty);
		}

		if (item.getTagessatz() != null) {

			if (item.getTagessatz() <= 0) {
				errors.rejectValue("tagessatz", Messages.negativeValue);
			}
		} else {
			errors.rejectValue("tagessatz", Messages.notEmpty);
		}

		if (item.getAvailableFrom() != null && item.getAvailableTill() != null) {
			if (item.getAvailableFrom().isAfter(item.getAvailableTill())) {
				errors.rejectValue("availableFrom", Messages.invalidPeriod);
			}
			if (item.getAvailableFrom().isBefore(LocalDate.now())) {
				errors.rejectValue("availableFrom", Messages.invalidAvailableFrom);
			}
		} else {
			errors.rejectValue("availableFrom", Messages.notEmpty);
			errors.rejectValue("availableTill", Messages.notEmpty);
		}

		if (item.getAbholort() == null) {
			ValidationUtils.rejectIfEmpty(errors, "abholort", Messages.notEmpty);
		}
	}
}
