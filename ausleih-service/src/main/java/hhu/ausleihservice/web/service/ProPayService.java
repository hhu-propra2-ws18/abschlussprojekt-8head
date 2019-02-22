package hhu.ausleihservice.web.service;

import hhu.ausleihservice.databasemodel.Ausleihe;
import hhu.ausleihservice.databasemodel.Person;
import hhu.ausleihservice.propay.ProPayInterface;
import hhu.ausleihservice.propay.ProPayReservation;
import org.springframework.stereotype.Component;

import java.time.Period;

@Component
public class ProPayService {

	private ProPayInterface proPayInterface;

	public ProPayService(ProPayInterface proPayInterface) {
		this.proPayInterface = proPayInterface;
	}

	public void ueberweiseTagessaetze(Ausleihe ausleihe) {
		int tagesSatz = ausleihe.getItem().getTagessatz();
		Period period = Period.between(ausleihe.getStartDatum(), ausleihe.getEndDatum());
		int amount = tagesSatz * period.getDays();
		String ausleiher = ausleihe.getAusleiher().getUsername();
		String besitzer = ausleihe.getItem().getBesitzer().getUsername();
		proPayInterface.transferFunds(ausleiher, besitzer, (double) amount);
	}

	public void kautionReservieren(Ausleihe ausleihe) {
		int kautionswert = ausleihe.getItem().getKautionswert();
		String ausleiher = ausleihe.getAusleiher().getUsername();
		String besitzer = ausleihe.getItem().getBesitzer().getUsername();
		ProPayReservation reservation = proPayInterface.createReservation(ausleiher, besitzer, kautionswert);
		ausleihe.setReservationId(reservation.getId());
	}

	public void punishRerservation(Ausleihe ausleihe) {
		long reservationId = ausleihe.getReservationId();
		String ausleiher = ausleihe.getAusleiher().getUsername();
		proPayInterface.punishReservation(reservationId, ausleiher);
	}

	public void releaseReservation(Ausleihe ausleihe) {
		long reservationId = ausleihe.getReservationId();
		String ausleiher = ausleihe.getAusleiher().getUsername();
		proPayInterface.releaseReservation(reservationId, ausleiher);
	}

	public double getProPayKontostand(Person person) {
		return proPayInterface.getAccountInfo(person.getUsername()).getAmount();
	}

	public void addFunds(Person person, double betrag) {
		proPayInterface.addFunds(person.getUsername(), betrag);
	}
}
