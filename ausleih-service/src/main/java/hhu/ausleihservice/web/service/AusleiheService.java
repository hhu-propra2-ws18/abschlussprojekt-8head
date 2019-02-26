package hhu.ausleihservice.web.service;

import hhu.ausleihservice.dataaccess.AusleiheRepository;
import hhu.ausleihservice.databasemodel.Ausleihe;
import hhu.ausleihservice.databasemodel.Person;
import hhu.ausleihservice.databasemodel.Status;
import hhu.ausleihservice.web.responsestatus.PersonNichtVorhanden;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AusleiheService {

	private AusleiheRepository ausleiheRepository;

	public AusleiheService(AusleiheRepository ausleiheRepository) {
		this.ausleiheRepository = ausleiheRepository;
	}

	public void save(Ausleihe ausleihe) {
		ausleiheRepository.save(ausleihe);
	}

	public List<Ausleihe> findAll() {
		return ausleiheRepository.findAll();
	}

	@Scheduled(cron = "0 0 1 * * ?")
	public void updateAllAusleihenDaily() {
		LocalDate currnetDate = LocalDate.now();

		System.out.println("Triggering update");
		for (Ausleihe ausleihe : findAll()) {
			checkAusleiheTooLate(ausleihe, currnetDate);
		}
	}

	private void checkAusleiheTooLate(Ausleihe ausleihe, LocalDate date) {
		if (ausleihe.getStatus().equals(Status.AUSGELIEHEN)) {
			if (ausleihe.getEndDatum().isBefore(date)) {
				ausleihe.setStatus(Status.RUECKGABE_VERPASST);
			}
		}
	}

	public List<Ausleihe> findAllConflicts() {
		return ausleiheRepository.findByKonflikt(true);
	}

	public Ausleihe findById(Long id) {
		Optional<Ausleihe> ausleihe = ausleiheRepository.findById(id);
		if (!ausleihe.isPresent()) {
			throw new PersonNichtVorhanden();
		}
		return ausleihe.get();
	}

	public List<Ausleihe> findLateAusleihen(Person person) {
		List<Ausleihe> lateAusleihen = new ArrayList<>();

		for (Ausleihe ausleihe : person.getAusleihen()) {
			if (ausleihe.getStatus().equals(Status.RUECKGABE_VERPASST)) {
				lateAusleihen.add(ausleihe);
			}
		}

		return lateAusleihen;
	}
}
