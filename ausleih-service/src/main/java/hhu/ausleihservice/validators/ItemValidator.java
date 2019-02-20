package hhu.ausleihservice.validators;

import hhu.ausleihservice.databasemodel.Item;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class ItemValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		return Item.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		Item item = (Item) target;

		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "titel", Messages.notEmpty);
		if (item.getTitel().length() < 6 || item.getTitel().length() > 40) {
			errors.rejectValue("titel", Messages.sizeTitle);
		}

		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "beschreibung", Messages.notEmpty);
		if (item.getBeschreibung().length() < 6 || item.getBeschreibung().length() > 4000) {
			errors.rejectValue("beschreibung", Messages.sizeTitle);
		}

		if (item.getKautionswert() <= 0) {
			errors.rejectValue("kautionswert", Messages.negativeValue);
		}

	}
}
