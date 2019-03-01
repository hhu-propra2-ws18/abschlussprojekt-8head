package hhu.ausleihservice.validators;

import hhu.ausleihservice.databasemodel.Kauf;
import hhu.ausleihservice.databasemodel.KaufItem;
import hhu.ausleihservice.databasemodel.Status;
import hhu.ausleihservice.web.service.ProPayService;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class KaufValidator implements Validator {

	private ProPayService proPayService;

	public KaufValidator(ProPayService proPayService) {
		this.proPayService = proPayService;
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return Kauf.class.equals(clazz);
	}

	public void validate(Object target, Errors errors) {

		Kauf kauf = (Kauf) target;

		ValidationUtils.rejectIfEmpty(errors, "item", Messages.notEmpty);

		ValidationUtils.rejectIfEmpty(errors, "kaeufer", Messages.notEmpty);

		KaufItem kaufItem = kauf.getItem();
		if (kaufItem.getStatus() != null && kaufItem.getStatus().equals(Status.VERKAUFT)) {
			errors.rejectValue("item", Messages.schonVerkauft);
		}
		if (kauf.getKaeufer() != null) {
			if (kauf.getKaeufer().equals(kauf.getItem().getBesitzer())) {
				errors.rejectValue("item", Messages.ownItemKauf);
			}
		}
		if (!proPayService.isAvailable()) {
			errors.rejectValue("kaeufer", Messages.propayUnavailable);

		} else if (kauf.getKaeufer() != null && kaufItem.getBesitzer() != null) {
			double kontostand = proPayService.getProPayKontostand(kauf.getKaeufer());
			if (kontostand < kaufItem.getKaufpreis()) {
				errors.rejectValue("kaeufer", Messages.notEnoughMoney);
			}
		}
	}

}
