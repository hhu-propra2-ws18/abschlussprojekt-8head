package hhu.ausleihservice.databasemodel;

import hhu.ausleihservice.dataaccess.*;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import javax.servlet.ServletContext;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Component
public class DatabaseInitializer implements ServletContextInitializer {

	private PersonRepository personRepository;
	private AusleihItemRepository ausleihItemRepository;
	private KaufItemRepository kaufItemRepository;
	private AbholortRepository abholortRepository;
	private AusleiheRepository ausleiheRepository;

	public DatabaseInitializer(PersonRepository perRepository,
							   AusleihItemRepository iRepository,
							   KaufItemRepository kaufItemRepository,
							   AbholortRepository abhRepository,
							   AusleiheRepository ausleiheRepository) {
		this.personRepository = perRepository;
		this.ausleihItemRepository = iRepository;
		this.kaufItemRepository = kaufItemRepository;
		this.abholortRepository = abhRepository;
		this.ausleiheRepository = ausleiheRepository;
	}

	@Override
	public void onStartup(ServletContext servletContext) {
		System.out.println("Populating the database");

		Abholort ort1 = new Abholort();
		Abholort ort2 = new Abholort();
		Abholort ort3 = new Abholort();
		Abholort ort4 = new Abholort();
		Abholort ort5 = new Abholort();
		Abholort ort6 = new Abholort();

		ort1.setBeschreibung("Höhle");
		ort2.setBeschreibung("Garage");
		ort3.setBeschreibung("Haus");
		ort4.setBeschreibung("Verloren");
		ort5.setBeschreibung("Zimbabwe");
		ort6.setBeschreibung("Hell");

		ort1.setLatitude(51.227741);
		ort1.setLongitude(6.773456);

		ort2.setLatitude(51.227741);
		ort2.setLongitude(6.773456);

		ort3.setLatitude(51.227741);
		ort3.setLongitude(6.773456);

		ort4.setLatitude(51.227741);
		ort4.setLongitude(6.773456);

		ort5.setLatitude(-19.021);
		ort5.setLongitude(29.839);

		ort6.setLatitude(42.43333);
		ort6.setLongitude(-83.983333);

		Set<Abholort> orte1 = new HashSet<>();
		Set<Abholort> orte2 = new HashSet<>();
		Set<Abholort> orte3 = new HashSet<>();
		Set<Abholort> orte4 = new HashSet<>();
		Set<Abholort> orte5 = new HashSet<>();

		orte1.add(ort1);
		orte2.add(ort2);
		orte2.add(ort3);
		orte3.add(ort4);
		orte4.add(ort5);
		orte5.add(ort6);

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
		person4.setVorname("Adriano");
		person4.setNachname("Minastro");
		person5.setVorname("Umir");
		person5.setNachname("Serjev");

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
		person4.setAbholorte(orte4);
		person5.setAbholorte(orte5);

		AusleihItem item1 = new AusleihItem();
		AusleihItem item2 = new AusleihItem();
		AusleihItem item3 = new AusleihItem();
		AusleihItem item4 = new AusleihItem();
		AusleihItem item5 = new AusleihItem();
		KaufItem kaufItem1 = new KaufItem();

		item1.setTitel("Stift");
		item2.setTitel("Fahrrad");
		item3.setTitel("Pfeil");
		item4.setTitel("Dose Bohnen");
		item5.setTitel("Frittiertes Fahrrad");
		kaufItem1.setTitel("Schwein");

		item1.setBeschreibung("Zum stiften gehen");
		item2.setBeschreibung("Falls man sich radlos fühlt");
		item3.setBeschreibung("Wenn man den Bogen schon raus hat");
		item4.setBeschreibung("Genau die richtige Dosis");
		item5.setBeschreibung("Du hast doch ein Rad ab");
		kaufItem1.setBeschreibung("Um Schwein gehabt zu haben");

		item1.setTagessatz(3);
		item2.setTagessatz(8);
		item3.setTagessatz(100);
		item4.setTagessatz(7);
		item5.setTagessatz(404);

		item1.setKautionswert(34);
		item2.setKautionswert(1245);
		item3.setKautionswert(55);
		item4.setKautionswert(432);
		item5.setKautionswert(606);
		kaufItem1.setKaufpreis(1000);

		item1.setAbholort(ort1);
		item2.setAbholort(ort3);
		item3.setAbholort(ort4);
		item4.setAbholort(ort5);
		item5.setAbholort(ort6);
		kaufItem1.setAbholort(ort1);

		LocalDate mai = LocalDate.of(2019, 4, 30);

		item1.setAvailableFrom(mai.plusDays(1)); //1.Mai
		item2.setAvailableFrom(mai.plusDays(3)); //3.Mai
		item3.setAvailableFrom(mai.plusDays(4)); //...
		item4.setAvailableFrom(mai.plusDays(7));
		item5.setAvailableFrom(mai.plusDays(11));

		item1.setAvailableTill(mai.plusDays(8));
		item2.setAvailableTill(mai.plusDays(10));
		item3.setAvailableTill(mai.plusDays(11));
		item4.setAvailableTill(mai.plusDays(14));
		item5.setAvailableTill(mai.plusDays(18));

		person1.addItem(item1);
		person2.addItem(item2);
		person3.addItem(item3);
		person4.addItem(item4);
		person5.addItem(item5);
		person1.addItem(kaufItem1);

		try {
			item1.setPicture(Files.readAllBytes(
					Paths.get(System.getProperty("user.dir") + "/src/main/resources/static/img/stift.jpg")));
			item2.setPicture(Files.readAllBytes(
					Paths.get(System.getProperty("user.dir") + "/src/main/resources/static/img/fahrrad.jpg")));
			item3.setPicture(Files.readAllBytes(
					Paths.get(System.getProperty("user.dir") + "/src/main/resources/static/img/pfeil.jpg")));
			item4.setPicture(Files.readAllBytes(
					Paths.get(System.getProperty("user.dir") + "/src/main/resources/static/img/bohnen.jpg")));
			item5.setPicture(Files.readAllBytes(
					Paths.get(System.getProperty("user.dir") + "/src/main/resources/static/img/deepFriedFahrrad.jpg")));
			kaufItem1.setPicture(Files.readAllBytes(
					Paths.get(System.getProperty("user.dir") + "/src/main/resources/static/img/schwein.jpeg")));
		} catch (IOException e) {
			System.out.println("Files could not be stored");
		}

		this.abholortRepository.save(ort1);
		this.abholortRepository.save(ort2);
		this.abholortRepository.save(ort3);
		this.abholortRepository.save(ort4);
		this.abholortRepository.save(ort5);
		this.abholortRepository.save(ort6);

		this.personRepository.save(person1);
		this.personRepository.save(person2);
		this.personRepository.save(person3);
		this.personRepository.save(person4);
		this.personRepository.save(person5);

		this.ausleihItemRepository.save(item1);
		this.ausleihItemRepository.save(item2);
		this.ausleihItemRepository.save(item3);
		this.ausleihItemRepository.save(item4);
		this.ausleihItemRepository.save(item5);
		this.kaufItemRepository.save(kaufItem1);

		Ausleihe ausleihe1 = new Ausleihe();
		ausleihe1.setReservationId(0L);
		ausleihe1.setStartDatum(mai.plusDays(4));
		ausleihe1.setEndDatum(mai.plusDays(6));
		ausleihe1.setAusleiher(person2);
		ausleihe1.setStatus(Status.ANGEFRAGT);

		Ausleihe ausleihe2 = new Ausleihe();
		ausleihe2.setReservationId(1L);
		ausleihe2.setStartDatum(mai.plusDays(2));
		ausleihe2.setEndDatum(mai.plusDays(3));
		ausleihe2.setAusleiher(person3);
		ausleihe2.setStatus(Status.ANGEFRAGT);
		ausleihe2.setKonflikt(true);

		Ausleihe ausleihe3 = new Ausleihe();
		ausleihe3.setReservationId(2L);
		ausleihe3.setStartDatum(mai.plusDays(8));
		ausleihe3.setEndDatum(mai.plusDays(8));
		ausleihe3.setAusleiher(person5);
		ausleihe3.setStatus(Status.ANGEFRAGT);

		Ausleihe ausleihe4 = new Ausleihe();
		ausleihe4.setReservationId(3L);
		ausleihe4.setStartDatum(mai.plusDays(15));
		ausleihe4.setEndDatum(mai.plusDays(18));
		ausleihe4.setAusleiher(person4);
		ausleihe4.setStatus(Status.ANGEFRAGT);

		item3.addAusleihe(ausleihe1);
		item1.addAusleihe(ausleihe2);
		item4.addAusleihe(ausleihe3);
		item5.addAusleihe(ausleihe4);

		person2.addAusleihe(ausleihe1);
		person3.addAusleihe(ausleihe2);
		person4.addAusleihe(ausleihe4);
		person5.addAusleihe(ausleihe3);


	}
}
