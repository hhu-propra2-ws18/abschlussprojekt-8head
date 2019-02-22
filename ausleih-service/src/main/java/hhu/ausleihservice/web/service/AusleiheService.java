package hhu.ausleihservice.web.service;

import hhu.ausleihservice.dataaccess.AusleiheRepository;
import hhu.ausleihservice.databasemodel.Ausleihe;
import org.springframework.stereotype.Service;

@Service
public class AusleiheService {

	private AusleiheRepository ausleiheRepository;

	AusleiheService(AusleiheRepository ausleiheRepository) {
		this.ausleiheRepository = ausleiheRepository;
	}

	public void save(Ausleihe ausleihe) {
		ausleiheRepository.save(ausleihe);
	}

}
