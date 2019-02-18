package hhu.ausleihservice.web;

import hhu.ausleihservice.dataaccess.AbholortRepository;
import hhu.ausleihservice.databasemodel.Abholort;
import org.springframework.stereotype.Service;

@Service
public class AbholortService {

	private AbholortRepository abholortRepository;

	AbholortService(AbholortRepository abholortRepository) {
		this.abholortRepository = abholortRepository;
	}

	void save(Abholort abholort) {
		abholortRepository.save(abholort);
	}

}