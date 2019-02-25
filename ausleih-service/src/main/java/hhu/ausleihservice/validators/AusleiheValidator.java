package hhu.ausleihservice.validators;

import hhu.ausleihservice.databasemodel.Ausleihe;
import hhu.ausleihservice.databasemodel.Item;
import hhu.ausleihservice.web.service.ItemAvailabilityService;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import java.time.LocalDate;

@Component
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

		ValidationUtils.rejectIfEmpty(errors, "item", Messages.notEmpty);

		if (!availabilityService.isAvailableFromTill(ausleiheItem, ausleihe.getStartDatum(), ausleihe.getEndDatum())) {
			errors.rejectValue("startDatum", Messages.itemNotAvailable);
			errors.rejectValue("endDatum", Messages.itemNotAvailable);
		}

		ValidationUtils.rejectIfEmpty(errors, "ausleiher", Messages.notEmpty);

		if (ausleihe.getAusleiher().equals(ausleiheItem.getBesitzer())) {
			errors.rejectValue("ausleiher", Messages.ownItemAusleihe);
		}
	}

}
