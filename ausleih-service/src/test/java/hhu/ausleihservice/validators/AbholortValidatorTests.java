package hhu.ausleihservice.validators;

import hhu.ausleihservice.databasemodel.Abholort;
import org.junit.Test;
import org.springframework.validation.BindingResult;
import org.springframework.validation.DataBinder;

import static org.junit.Assert.*;

public class AbholortValidatorTests {

	//Tests for supports(Class<?> clazz)
	@Test
	public void supportsValidClass() {
		ItemValidator itemValidator = new ItemValidator();
		assertTrue(itemValidator.supports(Abholort.class));
	}

	@Test
	public void supportsInvalidClassFalse() {
		ItemValidator itemValidator = new ItemValidator();
		assertFalse(itemValidator.supports(Integer.class));
	}


	//Tests for validate(Object target, Errors errors)
	@Test
	public void validateEmptyLatitude() {
		AbholortValidator abholortValidator = new AbholortValidator();
		Abholort abholort = new Abholort();

		abholort.setId(0L);
		abholort.setLongitude(0.0);
		abholort.setBeschreibung("Uganda");

		DataBinder dataBinder = new DataBinder(abholort);
		dataBinder.setValidator(abholortValidator);
		dataBinder.validate();
		BindingResult bindingResult = dataBinder.getBindingResult();

		assertTrue(bindingResult.hasErrors());
		assertEquals(1, bindingResult.getErrorCount());
		assertNotNull(bindingResult.getFieldError("latitude"));
		assertEquals(Messages.notEmpty, bindingResult.getFieldError("latitude").getCode());
	}

}
