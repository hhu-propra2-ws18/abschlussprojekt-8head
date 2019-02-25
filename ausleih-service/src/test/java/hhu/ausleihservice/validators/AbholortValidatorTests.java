package hhu.ausleihservice.validators;

import hhu.ausleihservice.databasemodel.Abholort;
import org.junit.Test;
import org.springframework.validation.BindingResult;
import org.springframework.validation.DataBinder;
import org.springframework.validation.FieldError;

import java.util.List;

import static org.junit.Assert.*;

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
	public void validateNullLatitude() {
		AbholortValidator abholortValidator = new AbholortValidator();
		Abholort abholort = new Abholort();

		abholort.setId(0L);
		abholort.setLongitude(0.0);
		abholort.setBeschreibung("Uganda");

		DataBinder dataBinder = new DataBinder(abholort);
		dataBinder.setValidator(abholortValidator);
		dataBinder.validate();
		BindingResult bindingResult = dataBinder.getBindingResult();

		FieldError error = bindingResult.getFieldError("latitude");

		assertTrue(bindingResult.hasErrors());
		assertEquals(1, bindingResult.getErrorCount());
		assertNotNull(error);
		assertEquals(Messages.notEmpty, error.getCode());
	}

	@Test
	public void validateNullLongitude() {
		AbholortValidator abholortValidator = new AbholortValidator();
		Abholort abholort = new Abholort();

		abholort.setId(0L);
		abholort.setLatitude(0.0);
		abholort.setBeschreibung("Uganda");

		DataBinder dataBinder = new DataBinder(abholort);
		dataBinder.setValidator(abholortValidator);
		dataBinder.validate();
		BindingResult bindingResult = dataBinder.getBindingResult();

		FieldError error = bindingResult.getFieldError("longitude");

		assertTrue(bindingResult.hasErrors());
		assertEquals(1, bindingResult.getErrorCount());
		assertNotNull(error);
		assertEquals(Messages.notEmpty, error.getCode());
	}

	@Test
	public void validateNullBeschreibung() {
		AbholortValidator abholortValidator = new AbholortValidator();
		Abholort abholort = new Abholort();

		abholort.setId(0L);
		abholort.setLongitude(0.0);
		abholort.setLatitude(0.0);

		DataBinder dataBinder = new DataBinder(abholort);
		dataBinder.setValidator(abholortValidator);
		dataBinder.validate();
		BindingResult bindingResult = dataBinder.getBindingResult();

		List<FieldError> errors = bindingResult.getFieldErrors("beschreibung");

		assertTrue(bindingResult.hasErrors());
		assertEquals(2, bindingResult.getErrorCount());
		assertNotNull(errors);
		assertEquals(2, errors.size());
		assertEquals(Messages.notEmpty, errors.get(0).getCode());
		assertEquals(Messages.sizeLocationDescription, errors.get(1).getCode());
	}

	@Test
	public void validateBeschreibungEmpty() {
		AbholortValidator abholortValidator = new AbholortValidator();
		Abholort abholort = new Abholort();

		abholort.setId(0L);
		abholort.setLongitude(0.0);
		abholort.setLatitude(0.0);
		abholort.setBeschreibung("");

		DataBinder dataBinder = new DataBinder(abholort);
		dataBinder.setValidator(abholortValidator);
		dataBinder.validate();
		BindingResult bindingResult = dataBinder.getBindingResult();

		List<FieldError> errors = bindingResult.getFieldErrors("beschreibung");

		assertTrue(bindingResult.hasErrors());
		assertEquals(2, bindingResult.getErrorCount());
		assertNotNull(errors);
		assertEquals(2, errors.size());
		assertEquals(Messages.notEmpty, errors.get(0).getCode());
		assertEquals(Messages.sizeLocationDescription, errors.get(1).getCode());
	}

	@Test
	public void validateLatitudeTooSmall() {
		AbholortValidator abholortValidator = new AbholortValidator();
		Abholort abholort = new Abholort();

		abholort.setId(0L);
		abholort.setLongitude(0.0);
		abholort.setLatitude(-100.0);
		abholort.setBeschreibung("Uganda");

		DataBinder dataBinder = new DataBinder(abholort);
		dataBinder.setValidator(abholortValidator);
		dataBinder.validate();
		BindingResult bindingResult = dataBinder.getBindingResult();

		FieldError error = bindingResult.getFieldError("latitude");

		assertTrue(bindingResult.hasErrors());
		assertEquals(1, bindingResult.getErrorCount());
		assertNotNull(error);
		assertEquals(Messages.latitudeOutOfBounds, error.getCode());
	}

	@Test
	public void validateLatitudeTooBig() {
		AbholortValidator abholortValidator = new AbholortValidator();
		Abholort abholort = new Abholort();

		abholort.setId(0L);
		abholort.setLongitude(0.0);
		abholort.setLatitude(100.0);
		abholort.setBeschreibung("Uganda");

		DataBinder dataBinder = new DataBinder(abholort);
		dataBinder.setValidator(abholortValidator);
		dataBinder.validate();
		BindingResult bindingResult = dataBinder.getBindingResult();

		FieldError error = bindingResult.getFieldError("latitude");

		assertTrue(bindingResult.hasErrors());
		assertEquals(1, bindingResult.getErrorCount());
		assertNotNull(error);
		assertEquals(Messages.latitudeOutOfBounds, error.getCode());
	}

	@Test
	public void validateLongitudeTooSmall() {
		AbholortValidator abholortValidator = new AbholortValidator();
		Abholort abholort = new Abholort();

		abholort.setId(0L);
		abholort.setLongitude(-200.0);
		abholort.setLatitude(0.0);
		abholort.setBeschreibung("Uganda");

		DataBinder dataBinder = new DataBinder(abholort);
		dataBinder.setValidator(abholortValidator);
		dataBinder.validate();
		BindingResult bindingResult = dataBinder.getBindingResult();

		FieldError error = bindingResult.getFieldError("longitude");

		assertTrue(bindingResult.hasErrors());
		assertEquals(1, bindingResult.getErrorCount());
		assertNotNull(error);
		assertEquals(Messages.longitudeOutOfBounds, error.getCode());
	}

	@Test
	public void validateLongitudeTooBig() {
		AbholortValidator abholortValidator = new AbholortValidator();
		Abholort abholort = new Abholort();

		abholort.setId(0L);
		abholort.setLongitude(200.0);
		abholort.setLatitude(0.0);
		abholort.setBeschreibung("Uganda");

		DataBinder dataBinder = new DataBinder(abholort);
		dataBinder.setValidator(abholortValidator);
		dataBinder.validate();
		BindingResult bindingResult = dataBinder.getBindingResult();

		FieldError error = bindingResult.getFieldError("longitude");

		assertTrue(bindingResult.hasErrors());
		assertEquals(1, bindingResult.getErrorCount());
		assertNotNull(error);
		assertEquals(Messages.longitudeOutOfBounds, error.getCode());
	}

	@Test
	public void validateBeschreibungTooShort() {
		AbholortValidator abholortValidator = new AbholortValidator();
		Abholort abholort = new Abholort();

		abholort.setId(0L);
		abholort.setLongitude(0.0);
		abholort.setLatitude(0.0);
		abholort.setBeschreibung("Ugand");

		DataBinder dataBinder = new DataBinder(abholort);
		dataBinder.setValidator(abholortValidator);
		dataBinder.validate();
		BindingResult bindingResult = dataBinder.getBindingResult();

		FieldError error = bindingResult.getFieldError("beschreibung");

		assertTrue(bindingResult.hasErrors());
		assertEquals(1, bindingResult.getErrorCount());
		assertNotNull(error);
		assertEquals(Messages.sizeLocationDescription, error.getCode());
	}

	@Test
	public void validateBeschreibungTooLong() {
		AbholortValidator abholortValidator = new AbholortValidator();
		Abholort abholort = new Abholort();

		abholort.setId(0L);
		abholort.setLongitude(0.0);
		abholort.setLatitude(0.0);

		DataBinder dataBinder = new DataBinder(abholort);
		abholort.setBeschreibung(
				"0123456789012345678901234567890123456789" +
				"0123456789012345678901234567890123456789" +
				"0123456789012345678901234567890123456789" +
				"0123456789012345678901234567890123456789" +
				"0123456789012345678901234567890123456789" +
				"0123456789012345678901234567890123456789" + //440 characters
				"0123456789012345678901234567890123456789" +
				"0123456789012345678901234567890123456789" +
				"0123456789012345678901234567890123456789" +
				"0123456789012345678901234567890123456789" +
				"0123456789012345678901234567890123456789"
		);
		dataBinder.setValidator(abholortValidator);
		dataBinder.validate();
		BindingResult bindingResult = dataBinder.getBindingResult();

		FieldError error = bindingResult.getFieldError("beschreibung");

		assertTrue(bindingResult.hasErrors());
		assertEquals(1, bindingResult.getErrorCount());
		assertNotNull(error);
		assertEquals(Messages.sizeLocationDescription, error.getCode());
	}


}
