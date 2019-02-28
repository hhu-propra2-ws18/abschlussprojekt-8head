package hhu.ausleihservice.validators;

import hhu.ausleihservice.databasemodel.Person;
import hhu.ausleihservice.web.service.PersonService;
import org.junit.Test;
import org.springframework.validation.BindingResult;
import org.springframework.validation.DataBinder;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Optional;

public class PersonValidatorTest {

	@Test
	public void whitespaceUsername() {
		PersonService personService = mock(PersonService.class);
		PersonValidator personValidator = new PersonValidator(personService);
		Person person = new Person();
		person.setUsername("		"); // Tabs
		person.setId(5L);
		DataBinder dataBinder = new DataBinder(person);
		dataBinder.setValidator(personValidator);

		Person person2 = new Person();
		person2.setUsername("        "); // Spaces
		person2.setId(6L);
		DataBinder dataBinder2 = new DataBinder(person2);
		dataBinder2.setValidator(personValidator);

		Person person3 = new Person();
		person3.setUsername("        		    	"); // Mixed
		person3.setId(6L);
		DataBinder dataBinder3 = new DataBinder(person3);
		dataBinder3.setValidator(personValidator);

		dataBinder.validate();
		BindingResult bindingResult = dataBinder.getBindingResult();

		dataBinder2.validate();
		BindingResult bindingResult2 = dataBinder2.getBindingResult();

		dataBinder3.validate();
		BindingResult bindingResult3 = dataBinder3.getBindingResult();

		assertEquals(Messages.notEmpty, bindingResult.getFieldError("username").getCode());
		assertEquals(Messages.notEmpty, bindingResult2.getFieldError("username").getCode());
		assertEquals(Messages.notEmpty, bindingResult3.getFieldError("username").getCode());

	}

	@Test
	public void whitespaceVorname() {
		PersonService personService = mock(PersonService.class);
		PersonValidator personValidator = new PersonValidator(personService);
		Person person = new Person();
		person.setVorname("		"); // Tabs
		person.setId(5L);
		DataBinder dataBinder = new DataBinder(person);
		dataBinder.setValidator(personValidator);

		Person person2 = new Person();
		person2.setVorname("        "); // Spaces
		person2.setId(6L);
		DataBinder dataBinder2 = new DataBinder(person2);
		dataBinder2.setValidator(personValidator);

		Person person3 = new Person();
		person3.setVorname("        		    	"); // Mixed
		person3.setId(6L);
		DataBinder dataBinder3 = new DataBinder(person3);
		dataBinder3.setValidator(personValidator);

		dataBinder.validate();
		BindingResult bindingResult = dataBinder.getBindingResult();

		dataBinder2.validate();
		BindingResult bindingResult2 = dataBinder2.getBindingResult();

		dataBinder3.validate();
		BindingResult bindingResult3 = dataBinder3.getBindingResult();

		assertEquals(Messages.notEmpty, bindingResult.getFieldError("vorname").getCode());
		assertEquals(Messages.notEmpty, bindingResult2.getFieldError("vorname").getCode());
		assertEquals(Messages.notEmpty, bindingResult3.getFieldError("vorname").getCode());

	}
	
	@Test
	public void duplicateUsername() {
		PersonService personService = mock(PersonService.class);
		PersonValidator personValidator = new PersonValidator(personService);
		Person person = new Person();
		person.setUsername("user1");
		person.setId(5L);
		DataBinder dataBinder = new DataBinder(person);
		dataBinder.setValidator(personValidator);

		Person person2 = new Person();
		person.setUsername("user1");
		person.setId(6L);
		Optional<Person> optionalPerson = Optional.of(person2);

		when(personService.findByUsername(person.getUsername())).thenReturn(optionalPerson);

		dataBinder.validate();
		BindingResult bindingResult = dataBinder.getBindingResult();

		assertEquals(Messages.duplicateUsername, bindingResult.getFieldError("username").getCode());
	}

	@Test
	public void whitespaceNachname() {
		PersonService personService = mock(PersonService.class);
		PersonValidator personValidator = new PersonValidator(personService);
		Person person = new Person();
		person.setNachname("		"); // Tabs
		person.setId(5L);
		DataBinder dataBinder = new DataBinder(person);
		dataBinder.setValidator(personValidator);

		Person person2 = new Person();
		person2.setNachname("        "); // Spaces
		person2.setId(6L);
		DataBinder dataBinder2 = new DataBinder(person2);
		dataBinder2.setValidator(personValidator);

		Person person3 = new Person();
		person3.setNachname("        		    	"); // Mixed
		person3.setId(6L);
		DataBinder dataBinder3 = new DataBinder(person3);
		dataBinder3.setValidator(personValidator);

		dataBinder.validate();
		BindingResult bindingResult = dataBinder.getBindingResult();

		dataBinder2.validate();
		BindingResult bindingResult2 = dataBinder2.getBindingResult();

		dataBinder3.validate();
		BindingResult bindingResult3 = dataBinder3.getBindingResult();

		assertEquals(Messages.notEmpty, bindingResult.getFieldError("nachname").getCode());
		assertEquals(Messages.notEmpty, bindingResult2.getFieldError("nachname").getCode());
		assertEquals(Messages.notEmpty, bindingResult3.getFieldError("nachname").getCode());

	}

	@Test
	public void correctEmailIsCorrect() {
		PersonService personService = mock(PersonService.class);
		PersonValidator personValidator = new PersonValidator(personService);
		Person person = new Person();
		person.setEmail("test@test.de");
		person.setId(5L);
		DataBinder dataBinder = new DataBinder(person);
		dataBinder.setValidator(personValidator);

		dataBinder.validate();
		BindingResult bindingResult = dataBinder.getBindingResult();

		assertFalse(bindingResult.hasFieldErrors("email"));

	}

	@Test
	public void incorrectEmailIsIncorrect() {
		PersonService personService = mock(PersonService.class);
		PersonValidator personValidator = new PersonValidator(personService);
		Person person = new Person();
		person.setEmail("test@test");
		person.setId(5L);
		DataBinder dataBinder = new DataBinder(person);
		dataBinder.setValidator(personValidator);

		Person person2 = new Person();
		person2.setEmail("testtest.de");
		person2.setId(5L);
		DataBinder dataBinder2 = new DataBinder(person2);
		dataBinder2.setValidator(personValidator);

		Person person3 = new Person();
		person3.setEmail("test.de@test");
		person3.setId(5L);
		DataBinder dataBinder3 = new DataBinder(person3);
		dataBinder3.setValidator(personValidator);

		dataBinder.validate();
		BindingResult bindingResult = dataBinder.getBindingResult();
		dataBinder2.validate();
		BindingResult bindingResult2 = dataBinder.getBindingResult();
		dataBinder3.validate();
		BindingResult bindingResult3 = dataBinder.getBindingResult();

		assertEquals(Messages.invalidEmail, bindingResult.getFieldError("email").getCode());
		assertEquals(Messages.invalidEmail, bindingResult2.getFieldError("email").getCode());
		assertEquals(Messages.invalidEmail, bindingResult3.getFieldError("email").getCode());

	}

	@Test
	public void shortUsername() {
		PersonService personService = mock(PersonService.class);
		PersonValidator personValidator = new PersonValidator(personService);
		Person person = new Person();
		person.setUsername("123");
		person.setId(5L);
		DataBinder dataBinder = new DataBinder(person);
		dataBinder.setValidator(personValidator);

		dataBinder.validate();
		BindingResult bindingResult = dataBinder.getBindingResult();

		assertEquals(Messages.usernameSize, bindingResult.getFieldError("username").getCode());
	}

	@Test
	public void longUsername() {
		PersonService personService = mock(PersonService.class);
		PersonValidator personValidator = new PersonValidator(personService);
		Person person = new Person();
		person.setUsername("Lorem ipsum dolor sit amet, conse");
		person.setId(5L);
		DataBinder dataBinder = new DataBinder(person);
		dataBinder.setValidator(personValidator);

		dataBinder.validate();
		BindingResult bindingResult = dataBinder.getBindingResult();

		assertEquals(Messages.usernameSize, bindingResult.getFieldError("username").getCode());
	}

	@Test
	public void whitespacePassword() {
		PersonService personService = mock(PersonService.class);
		PersonValidator personValidator = new PersonValidator(personService);
		Person person = new Person();
		person.setPassword("		"); // Tabs
		person.setId(5L);
		DataBinder dataBinder = new DataBinder(person);
		dataBinder.setValidator(personValidator);

		Person person2 = new Person();
		person2.setPassword("        "); // Spaces
		person2.setId(6L);
		DataBinder dataBinder2 = new DataBinder(person2);
		dataBinder2.setValidator(personValidator);

		Person person3 = new Person();
		person3.setPassword("        		    	"); // Mixed
		person3.setId(6L);
		DataBinder dataBinder3 = new DataBinder(person3);
		dataBinder3.setValidator(personValidator);

		dataBinder.validate();
		BindingResult bindingResult = dataBinder.getBindingResult();

		dataBinder2.validate();
		BindingResult bindingResult2 = dataBinder2.getBindingResult();

		dataBinder3.validate();
		BindingResult bindingResult3 = dataBinder3.getBindingResult();

		assertEquals(Messages.notEmpty, bindingResult.getFieldError("password").getCode());
		assertEquals(Messages.notEmpty, bindingResult2.getFieldError("password").getCode());
		assertEquals(Messages.notEmpty, bindingResult3.getFieldError("password").getCode());

	}

	@Test
	public void shortPassword() {
		PersonService personService = mock(PersonService.class);
		PersonValidator personValidator = new PersonValidator(personService);
		Person person = new Person();
		person.setPassword("12");
		person.setId(5L);
		DataBinder dataBinder = new DataBinder(person);
		dataBinder.setValidator(personValidator);

		dataBinder.validate();
		BindingResult bindingResult = dataBinder.getBindingResult();

		assertEquals(Messages.passwordSize, bindingResult.getFieldError("password").getCode());
	}

	@Test
	public void longPassword() {
		PersonService personService = mock(PersonService.class);
		PersonValidator personValidator = new PersonValidator(personService);
		Person person = new Person();
		person.setPassword("Lorem ipsum dolor sit amet, consetetur "
				+ "sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut la");
		person.setId(5L);
		DataBinder dataBinder = new DataBinder(person);
		dataBinder.setValidator(personValidator);

		dataBinder.validate();
		BindingResult bindingResult = dataBinder.getBindingResult();

		assertEquals(Messages.passwordSize, bindingResult.getFieldError("password").getCode());
	}
	
	@Test
	public void usernameIsNotDuplicate() {
		PersonService personService = mock(PersonService.class);
		PersonValidator personValidator = new PersonValidator(personService);
		Person person = new Person();
		person.setUsername("user123");
		person.setId(5L);
		DataBinder dataBinder = new DataBinder(person);
		dataBinder.setValidator(personValidator);

		Person person2 = new Person();
		person2.setUsername("user234");
		person2.setId(6L);
		Optional<Person> optionalPerson = Optional.of(person2);

		when(personService.findByUsername(person2.getUsername())).thenReturn(optionalPerson);

		dataBinder.validate();
		BindingResult bindingResult = dataBinder.getBindingResult();
		assertEquals(0, bindingResult.getFieldErrorCount("username"));
	}

}
