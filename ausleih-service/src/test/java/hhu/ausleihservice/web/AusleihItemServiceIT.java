package hhu.ausleihservice.web;

import hhu.ausleihservice.dataaccess.AbholortRepository;
import hhu.ausleihservice.dataaccess.AusleihItemRepository;
import hhu.ausleihservice.dataaccess.AusleiheRepository;
import hhu.ausleihservice.dataaccess.PersonRepository;
import hhu.ausleihservice.databasemodel.*;
import hhu.ausleihservice.web.service.AusleihItemService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@DataJpaTest
public class AusleihItemServiceIT {

	AusleihItemService ausleihItemService;

	@Autowired
	private AusleihItemRepository ausleihItemRepository;
	@Autowired
	private PersonRepository personRepository;
	@Autowired
	private AbholortRepository abholortRepository;
	@Autowired
	private AusleiheRepository ausleiheRepository;

	@Before
	public void onStartup() {
		System.out.println("Populating the database");

		Abholort ort1 = new Abholort();
		Abholort ort2 = new Abholort();
		Abholort ort3 = new Abholort();
		Abholort ort4 = new Abholort();

		ort1.setBeschreibung("Höhle");
		ort2.setBeschreibung("Garage");
		ort3.setBeschreibung("Haus");
		ort4.setBeschreibung("Verloren");

		ort1.setLatitude(51.227741);
		ort1.setLongitude(6.773456);

		ort2.setLatitude(51.227741);
		ort2.setLongitude(6.773456);

		ort3.setLatitude(51.227741);
		ort3.setLongitude(6.773456);

		ort4.setLatitude(51.227741);
		ort4.setLongitude(6.773456);

		Set<Abholort> orte1 = new HashSet<>();
		Set<Abholort> orte2 = new HashSet<>();
		Set<Abholort> orte3 = new HashSet<>();

		orte1.add(ort1);
		orte2.add(ort2);
		orte2.add(ort3);
		orte3.add(ort4);


		Person person1 = new Person();
		Person person2 = new Person();
		Person person3 = new Person();
		Person person4 = new Person();
		Person person5 = new Person();

		person1.setVorname("Gerold");
		person1.setNachname("Steiner");
		person2.setVorname("Volker");
		person2.setNachname("Racho");
		person3.setVorname("Wilma");
		person3.setNachname("Pause");
		person4.setVorname("AdminVorname");
		person4.setNachname("AdminNachname");
		person5.setVorname("asdffsdag");
		person5.setNachname("sbsbsew");

		person1.setUsername("Miner4lwasser");
		person2.setUsername("Kawumms");
		person3.setUsername("Kautschkartoffel3000");
		person4.setUsername("admin");
		person5.setUsername("user");

		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

		person1.setPassword(encoder.encode("pl4tsh"));
		person2.setPassword(encoder.encode("rumbl7"));
		person3.setPassword(encoder.encode("ichwillschlafen123"));
		person4.setPassword(encoder.encode("123"));
		person5.setPassword(encoder.encode("123"));

		person1.setRole(Role.ADMIN); //Role.USER is automatically set in constructor
		person4.setRole(Role.ADMIN);

		person1.setEmail("sleeping@home.com");
		person2.setEmail("notWorking@uni.com");
		person3.setEmail("screaming@computer.de");
		person4.setEmail("admin@uni-dusseldorf.de");
		person5.setEmail("asdf@gmail.com");

		person1.setAbholorte(orte1);
		person2.setAbholorte(orte2);
		person3.setAbholorte(orte3);


		AusleihItem item1 = new AusleihItem();
		AusleihItem item2 = new AusleihItem();
		AusleihItem item3 = new AusleihItem();

		item1.setTitel("Stift");
		item2.setTitel("Fahrrad");
		item3.setTitel("Pfeil");

		item1.setBeschreibung("Zum stiften gehen");
		item2.setBeschreibung("Falls man sich radlos fühlt");
		item3.setBeschreibung("Wenn man den Bogen schon raus hat");

		item1.setTagessatz(3);
		item2.setTagessatz(8);
		item3.setTagessatz(100);

		item1.setKautionswert(34);
		item2.setKautionswert(1245);
		item3.setKautionswert(55);

		item1.setAbholort(ort1);
		item2.setAbholort(ort3);
		item3.setAbholort(ort4);

		LocalDate ersterMai = LocalDate.of(2019, 5, 1);

		item1.setAvailableFrom(ersterMai.plusDays(1));
		item2.setAvailableFrom(ersterMai.plusDays(3));
		item3.setAvailableFrom(ersterMai.plusDays(4));

		item1.setAvailableTill(ersterMai.plusDays(8));
		item2.setAvailableTill(ersterMai.plusDays(10));
		item3.setAvailableTill(ersterMai.plusDays(11));

		person1.addItem(item1);
		person2.addItem(item2);
		person3.addItem(item3);

		this.abholortRepository.save(ort1);
		this.abholortRepository.save(ort2);
		this.abholortRepository.save(ort3);
		this.abholortRepository.save(ort4);

		this.personRepository.save(person1);
		this.personRepository.save(person2);
		this.personRepository.save(person3);
		this.personRepository.save(person4);
		this.personRepository.save(person5);

		this.ausleihItemRepository.save(item1);
		this.ausleihItemRepository.save(item2);
		this.ausleihItemRepository.save(item3);

		Ausleihe ausleihe1 = new Ausleihe();
		ausleihe1.setReservationId(0L);
		ausleihe1.setStartDatum(ersterMai.plusDays(4));
		ausleihe1.setEndDatum(ersterMai.plusDays(6));

		Ausleihe ausleihe2 = new Ausleihe();
		ausleihe2.setReservationId(1L);
		ausleihe2.setStartDatum(ersterMai.plusDays(2));
		ausleihe2.setEndDatum(ersterMai.plusDays(3));
		ausleihe2.setAusleiher(person3);
		ausleihe2.setKonflikt(true);

		item3.addAusleihe(ausleihe1);
		item1.addAusleihe(ausleihe2);

		person2.addAusleihe(ausleihe1);
		person3.addAusleihe(ausleihe2);

		this.ausleiheRepository.save(ausleihe1);
		this.ausleiheRepository.save(ausleihe2);
		this.ausleihItemRepository.save(item3);
		this.ausleihItemRepository.save(item1);

		ausleihItemService = new AusleihItemService(ausleihItemRepository);
	}

	@Test
	public void testSimpleSearch_1_nullQuery() {
		List<AusleihItem> simple = ausleihItemService.simpleSearch(null);
		assertEquals(3, simple.size());
		assertTrue(simple.containsAll(ausleihItemRepository.findAll()));
	}

	@Test
	public void testExtendedSearch_1_nullQuery() {
		List<AusleihItem> searchedList = ausleihItemService.extendedSearch(null,
				2147483647,
				2147483647,
				LocalDate.of(2019, 5, 5),
				LocalDate.of(2019, 5, 6));

		assertEquals(3, searchedList.size());
		assertTrue(searchedList.containsAll(ausleihItemRepository.findAll()));
	}

	@Test
	public void testExtendedSearch_1_dates() {
		List<AusleihItem> searchedList = ausleihItemService.extendedSearch(null,
				2147483647,
				2147483647,
				LocalDate.of(2019, 5, 4),
				LocalDate.of(2019, 5, 5));

		assertFalse(searchedList.containsAll(ausleihItemRepository.findAll()));
		assertEquals(2, searchedList.size());
	}

}