package hhu.ausleihservice.validators;

import java.security.Principal;
import java.time.LocalDate;

import org.springframework.scheduling.support.PeriodicTrigger;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import hhu.ausleihservice.databasemodel.Ausleihe;
import hhu.ausleihservice.databasemodel.Item;
import hhu.ausleihservice.databasemodel.Period;
import hhu.ausleihservice.databasemodel.Person;
import hhu.ausleihservice.web.service.ItemAvailabilityService;

public class AusleiheValidator implements Validator {

	private ItemAvailabilityService availabilityService;

	public AusleiheValidator(ItemAvailabilityService availabilityService) {
		this.availabilityService = availabilityService;
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return Ausleihe.class.equals(clazz);
	}

	public void validate(Object target, Errors errors) {

		Ausleihe ausleihe = (Ausleihe) target;
		Item ausleiheItem = ausleihe.getItem();
		Period period = new Period(ausleihe.getStartDatum(), ausleihe.getEndDatum());

		ValidationUtils.rejectIfEmpty(errors, "item", Messages.notEmpty);

		if (ausleiheItem.getAvailableFrom() != null && ausleiheItem.getAvailableTill() != null) {
			if (ausleiheItem.getAvailableFrom().isAfter(ausleiheItem.getAvailableTill())) {
				errors.rejectValue("availableFrom", Messages.invalidPeriod);
			}
			if (ausleiheItem.getAvailableFrom().isBefore(LocalDate.now())) {
				errors.rejectValue("availableFrom", Messages.invalidAvailableFrom);
			}
		} else {
			errors.rejectValue("availableFrom", Messages.notEmpty);
			errors.rejectValue("availableTill", Messages.notEmpty);
		}

		if (!availabilityService.isAvailableFromTill(ausleiheItem, ausleihe.getStartDatum(), ausleihe.getEndDatum())) {
			errors.rejectValue("startDatum", Messages.itemNotAvailable);
			errors.rejectValue("endDatum", Messages.itemNotAvailable);
		}

		ValidationUtils.rejectIfEmpty(errors, "ausleiher", Messages.notEmpty);
	}

}
