package hhu.ausleihservice.validators;

import hhu.ausleihservice.databasemodel.AusleihItem;
import hhu.ausleihservice.databasemodel.Ausleihe;
import hhu.ausleihservice.web.service.ItemAvailabilityService;
import hhu.ausleihservice.web.service.ProPayService;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class AusleiheAnfragenValidator implements Validator {

	private ItemAvailabilityService availabilityService;
	private ProPayService proPayService;

	public AusleiheAnfragenValidator(ItemAvailabilityService availabilityService, ProPayService proPayService) {
		this.availabilityService = availabilityService;
		this.proPayService = proPayService;
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return Ausleihe.class.equals(clazz);
	}

	public void validate(Object target, Errors errors) {

		Ausleihe ausleihe = (Ausleihe) target;
		AusleihItem ausleiheItem = ausleihe.getItem();

		ValidationUtils.rejectIfEmpty(errors, "item", Messages.notEmpty);
		ValidationUtils.rejectIfEmpty(errors, "ausleiher", Messages.notEmpty);

		if (!availabilityService.isAvailableFromTill(ausleiheItem, ausleihe.getStartDatum(), ausleihe.getEndDatum())) {
			errors.rejectValue("startDatum", Messages.itemNotAvailable);
		}

		if (ausleiheItem != null &&
				ausleihe.getAusleiher() != null &&
				ausleihe.getAusleiher().equals(ausleiheItem.getBesitzer())) {
			errors.rejectValue("ausleiher", Messages.ownItemAusleihe);
		}

		if (!proPayService.isAvailable()) {
			errors.rejectValue("ausleiher", Messages.propayUnavailable);

		} else if ((ausleiheItem != null) && ausleihe.getAusleiher() != null && ausleiheItem.getBesitzer() != null) {
			double kontostand = proPayService.getProPayKontostand(ausleihe.getAusleiher());
			int kautionswert = ausleiheItem.getKautionswert();
			int ausleihDauer = ausleihe.getStartDatum().compareTo(ausleihe.getEndDatum());
			if (kontostand < (kautionswert + ausleiheItem.getTagessatz() * ausleihDauer)) {
				errors.rejectValue("ausleiher", Messages.notEnoughMoney);
			}
		}
	}

}
