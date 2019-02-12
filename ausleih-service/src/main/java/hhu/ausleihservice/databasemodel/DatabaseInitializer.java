package hhu.ausleihservice.databasemodel;

import hhu.ausleihservice.dataaccess.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.stereotype.Component;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

@Component
public class DatabaseInitializer implements ServletContextInitializer {

	@Autowired
	ItemRepository itemRepository;

	@Override
	public void onStartup(ServletContext servletContext) throws ServletException {
		System.out.println("Populating the database");

		Person person1 = new Person();
		Person person2 = new Person();
		Person person3 = new Person();

		person1.setVorname("Gerold");
		person1.setName("Steiner");
		person2.setVorname("Volker");
		person2.setName("Racho");
		person3.setVorname("Wilma");
		person3.setName("Pause");

		person1.setUsername("Miner4lwasser");
		person2.setUsername("Kawumms");
		person3.setUsername("Kautschkartoffel3000");

		person1.setEmail("sleeping@home.com");
		person2.setEmail("notWorking@uni.com");
		person3.setEmail("screaming@computer.de");

		Set<Abholort> orte1 = new HashSet<>();
		Set<Abholort> orte2 = new HashSet<>();
		Set<Abholort> orte3 = new HashSet<>();

		Abholort ort1 = new Abholort(); ort1.setBeschreibung("Höhle");
		Abholort ort2 = new Abholort(); ort1.setBeschreibung("Garage");
		Abholort ort3 = new Abholort(); ort1.setBeschreibung("Haus");
		Abholort ort4 = new Abholort(); ort1.setBeschreibung("Verloren");

		orte1.add(ort1);
		orte2.add(ort2);
		orte2.add(ort3);
		orte3.add(ort4);

		person1.setAbholOrte(orte1);
		person2.setAbholOrte(orte2);
		person3.setAbholOrte(orte3);


		Item item1 = new Item();
		Item item2 = new Item();
		Item item3 = new Item();

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

		item1.setStandort(ort1);
		item2.setStandort(ort3);
		item3.setStandort(ort4);

		item1.setAvailableFrom(LocalDate.now().plusDays(1));
		item2.setAvailableFrom(LocalDate.now().plusDays(3));
		item3.setAvailableFrom(LocalDate.now().plusDays(4));

		item1.setAvailableTill(LocalDate.now().plusDays(13));
		item2.setAvailableTill(LocalDate.now().plusDays(35));
		item3.setAvailableTill(LocalDate.now().plusDays(46));

		//TODO add remaining fields
	}
}
