package hhu.ausleihservice.validators;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.springframework.validation.BindingResult;
import org.springframework.validation.DataBinder;

import hhu.ausleihservice.databasemodel.Abholort;

public class AbholortValidatorTests {

	//Tests for supports(Class<?> clazz)
	@Test
	public void supportsValidClass() {
		AbholortValidator abholortValidator = new AbholortValidator();
		assertTrue(abholortValidator.supports(Abholort.class));
	}

	@Test
	public void supportsInvalidClassFalse() {
		AbholortValidator abholortValidator = new AbholortValidator();
		assertFalse(abholortValidator.supports(Integer.class));
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
