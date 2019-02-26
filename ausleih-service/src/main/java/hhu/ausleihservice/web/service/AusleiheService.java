package hhu.ausleihservice.web.service;

import hhu.ausleihservice.dataaccess.AusleiheRepository;
import hhu.ausleihservice.databasemodel.Ausleihe;
import hhu.ausleihservice.databasemodel.Status;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class AusleiheService {

	private AusleiheRepository ausleiheRepository;

	AusleiheService(AusleiheRepository ausleiheRepository) {
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


}
