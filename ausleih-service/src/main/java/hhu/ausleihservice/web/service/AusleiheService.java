package hhu.ausleihservice.web.service;

import hhu.ausleihservice.dataaccess.AusleiheRepository;
import hhu.ausleihservice.databasemodel.Ausleihe;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AusleiheService {

	private AusleiheRepository ausleiheRepository;

	public AusleiheService(AusleiheRepository ausleiheRepository) {
		this.ausleiheRepository = ausleiheRepository;
	}

	public void save(Ausleihe ausleihe) {
		ausleiheRepository.save(ausleihe);
	}

	public List<Ausleihe> findAllConflicts(){
		return ausleiheRepository.findByKonflikt(true);
	}

}
