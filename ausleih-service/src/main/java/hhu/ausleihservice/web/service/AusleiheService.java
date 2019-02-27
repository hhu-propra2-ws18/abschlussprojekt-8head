package hhu.ausleihservice.web.service;

import hhu.ausleihservice.dataaccess.AusleiheRepository;
import hhu.ausleihservice.databasemodel.Ausleihe;
import hhu.ausleihservice.web.responsestatus.PersonNichtVorhanden;
import org.springframework.stereotype.Service;

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

	public List<Ausleihe> findAllByAusleiherId(Long id) {
		return ausleiheRepository.findAllByAusleiherId(id);
	}


}
