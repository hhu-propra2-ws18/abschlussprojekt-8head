package hhu.ausleihservice.validators;

import hhu.ausleihservice.databasemodel.Abholort;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class AbholortValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		return Abholort.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		Abholort abholort = (Abholort) target;

		if (abholort.getLatitude() != null) {

			if (abholort.getLatitude() < -90 || abholort.getLatitude() > 90) {
				errors.rejectValue("latitude", Messages.latitudeOutOfBounds);
			}
		} else {
			errors.rejectValue("latitude", Messages.notEmpty);
		}

		if (abholort.getLongitude() != null) {

			if (abholort.getLongitude() < -180 || abholort.getLongitude() > 180) {
				errors.rejectValue("longitude", Messages.longitudeOutOfBounds);
			}
		} else {
			errors.rejectValue("longitude", Messages.notEmpty);
		}

		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "beschreibung", Messages.notEmpty);
		if (abholort.getBeschreibung().length() < 6 || abholort.getBeschreibung().length() > 250) {
			errors.rejectValue("beschreibung", Messages.sizeLocationDescription);
		}
	}
}
