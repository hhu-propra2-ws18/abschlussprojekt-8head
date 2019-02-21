package hhu.ausleihservice.web;

import hhu.ausleihservice.dataaccess.AusleiheRepository;
import hhu.ausleihservice.databasemodel.Ausleihe;
import org.springframework.stereotype.Service;

@Service
public class AusleiheService {

	private AusleiheRepository ausleiheRepository;

	AusleiheService(AusleiheRepository ausleiheRepository) {
		this.ausleiheRepository = ausleiheRepository;
	}

	void save(Ausleihe ausleihe) {
		ausleiheRepository.save(ausleihe);
	}

}
