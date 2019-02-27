package hhu.ausleihservice.validators;

import hhu.ausleihservice.databasemodel.Kauf;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class KaufValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		return Kauf.class.equals(clazz);
	}

	public void validate(Object target, Errors errors) {

		Kauf kauf = (Kauf) target;

		ValidationUtils.rejectIfEmpty(errors, "item", Messages.notEmpty);

		ValidationUtils.rejectIfEmpty(errors, "kaeufer", Messages.notEmpty);

		if (kauf.getKaeufer() != null) {
			if (kauf.getKaeufer().equals(kauf.getItem().getBesitzer())) {
				errors.rejectValue("ownItem", Messages.ownItemKauf);
			}
		}
	}

}
