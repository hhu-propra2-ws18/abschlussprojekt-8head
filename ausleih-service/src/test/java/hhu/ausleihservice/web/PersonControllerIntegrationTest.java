package hhu.ausleihservice.web;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.beans.HasProperty.hasProperty;
import static org.hamcrest.beans.HasPropertyWithValue.hasProperty;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.security.Principal;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import hhu.ausleihservice.databasemodel.DatabaseInitializer;
import hhu.ausleihservice.validators.Messages;


@SpringBootTest
@RunWith(SpringRunner.class)
public class PersonControllerIntegrationTest {
	
	MockMvc mockMvc;

	@Autowired
	protected WebApplicationContext wac;

	@Autowired
	PersonController personController;
	
	@Autowired
	DatabaseInitializer dbInit;
	
	@Before
	public void setup() {
		mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
		dbInit.onStartup(mockMvc.getDispatcherServlet().getServletContext());
	}
	
	@Test
	public void simpleUserSearch() throws Exception {
		Principal principal = mock(Principal.class);   
		when(principal.getName()).thenReturn("user");
		mockMvc.perform(post("/benutzersuche").principal(principal).param("query", "Gerold"))
		.andExpect(status().isOk())
		.andExpect(model().attribute("benutzerListe", hasItem(hasProperty("username", is("Miner4lwasser")))));
	}
	
	@Test
	public void takenUsernameRegister() throws Exception {
		mockMvc.perform(post("/register").param("username", "admin"))
		.andExpect(model().attribute("usernameErrors", hasProperty("code", is(Messages.duplicateUsername))));
	}

}
