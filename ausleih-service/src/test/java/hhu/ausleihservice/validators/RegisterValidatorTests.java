package hhu.ausleihservice.validators;

import hhu.ausleihservice.databasemodel.Person;
import hhu.ausleihservice.web.service.PersonService;
import org.junit.Test;
import org.springframework.validation.BindingResult;
import org.springframework.validation.DataBinder;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class RegisterValidatorTests {


	@Test
	public void duplicateUsername() {
		PersonService personService = mock(PersonService.class);
		RegisterValidator registerValidator = new RegisterValidator(personService);
		Person person = new Person();
		person.setUsername("user1");
		person.setId(5L);
		DataBinder dataBinder = new DataBinder(person);
		dataBinder.setValidator(registerValidator);

		when(personService.existsByUsername(person.getUsername())).thenReturn(true);

		dataBinder.validate();
		BindingResult bindingResult = dataBinder.getBindingResult();

		assertEquals(Messages.duplicateUsername, bindingResult.getFieldError("username").getCode());
	}

}
