package hhu.ausleihservice.web;

import hhu.ausleihservice.databasemodel.Ausleihe;
import hhu.ausleihservice.databasemodel.Item;
import hhu.ausleihservice.databasemodel.Person;
import hhu.ausleihservice.propay.ProPayInterface;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

import java.time.LocalDate;


public class ProPayServiceTest {

	private ProPayInterface proPayInterface = Mockito.mock(ProPayInterface.class);

	private ProPayService proPayService;


	@Before
	public void init() {

	}


	@Test
	public void ueberweiseTagessaetze() {
		Ausleihe ausleihe = new Ausleihe();
		Item fahrrad = new Item();
		Person burak = new Person();
		Person simon = new Person();

		burak.setUsername("bumar100");
		simon.setUsername("siker102");
		fahrrad.setBesitzer(burak);
		fahrrad.setTagessatz(10);
		ausleihe.setItem(fahrrad);
		ausleihe.setAusleiher(simon);
		ausleihe.setStartDatum(LocalDate.now());
		ausleihe.setEndDatum(LocalDate.now().plusDays(1));

		proPayService = new ProPayService(proPayInterface);
		proPayService.ueberweiseTagessaetze(ausleihe);
		Mockito.verify(proPayInterface).transferFunds(ArgumentMatchers.eq("siker102")
				, ArgumentMatchers.eq("bumar100"), ArgumentMatchers.eq(10.0));

		ausleihe.setEndDatum(LocalDate.now().plusDays(5));

		proPayService.ueberweiseTagessaetze(ausleihe);
		Mockito.verify(proPayInterface).transferFunds(ArgumentMatchers.eq("siker102")
				, ArgumentMatchers.eq("bumar100"), ArgumentMatchers.eq(50.0));

	}

	@Test
	public void kautionReservieren() {

	}

	@Test
	public void punishRerservation() {
	}

	@Test
	public void releaseReservation() {
	}

	@Test
	public void getProPayKontostand() {
	}

	@Test
	public void addFunds() {
	}
}
