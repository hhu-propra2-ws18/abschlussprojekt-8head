package hhu.ausleihservice.validators;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.LocalDate;

import org.junit.Test;
import org.springframework.validation.BindingResult;
import org.springframework.validation.DataBinder;

import hhu.ausleihservice.databasemodel.Ausleihe;
import hhu.ausleihservice.databasemodel.Item;
import hhu.ausleihservice.databasemodel.Person;
import hhu.ausleihservice.web.service.ItemAvailabilityService;

public class AusleiheValidatorTest {

	@Test
	public void startDatumAfterEndDatum() {
		ItemAvailabilityService availabilityService = mock(ItemAvailabilityService.class);
		Item ausleiheItem = mock(Item.class);
		AusleiheValidator ausleiheValidator = new AusleiheValidator(availabilityService);
		Ausleihe ausleihe = new Ausleihe();
		ausleihe.setStartDatum(LocalDate.of(2000, 5, 5));
		ausleihe.setEndDatum(LocalDate.of(2000, 5, 4));
		ausleihe.setItem(ausleiheItem);
		ausleihe.setAusleiher(new Person());
		when(ausleiheItem.getBesitzer()).thenReturn(new Person());
		
		when(availabilityService.isAvailableFromTill(ausleiheItem, ausleihe.getStartDatum(), ausleihe.getEndDatum()))
		.thenReturn(false);
		
		DataBinder dataBinder = new DataBinder(ausleihe);
		dataBinder.setValidator(ausleiheValidator);
		dataBinder.validate();
		BindingResult bindingResult = dataBinder.getBindingResult();
		assertTrue(bindingResult.hasFieldErrors("startDatum"));
		assertTrue(bindingResult.hasFieldErrors("endDatum"));
	}
}
