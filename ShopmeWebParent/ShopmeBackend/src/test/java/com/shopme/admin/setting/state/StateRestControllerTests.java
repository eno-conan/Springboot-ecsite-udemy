package com.shopme.admin.setting.state;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shopme.admin.setting.country.CountryRepository;
import com.shopme.common.entity.Country;
import com.shopme.common.entity.State;

@SpringBootTest
@AutoConfigureMockMvc
public class StateRestControllerTests {

	@Autowired
	private CountryRepository repo;

	@Autowired
	private StateRepository stateRepo;

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	@WithMockUser(username = "mockuser@gmail.com", password = "mockuser", roles = "ADMIN")
	public void testListByCountry() throws Exception {
		Integer countryId = 14;
		String url = "/states/list_by_country/" + countryId;

		MvcResult result = mockMvc.perform(get(url)).andExpect(status().isOk()).andDo(print()).andReturn();

		String jsonResponse = result.getResponse().getContentAsString();
		System.out.println(jsonResponse);

		State[] countries = objectMapper.readValue(jsonResponse, State[].class);

		assertThat(countries).hasSizeGreaterThan(1);
	}

	@Test
	@WithMockUser(username = "mockuser@gmail.com", password = "mockuser", roles = "ADMIN")
	public void testSaveCountry() throws JsonProcessingException, Exception {
		String url = "/states/save";
		Integer countryId = 14;
		String stateName = "TestState";
		Country country = repo.findById(countryId).get();
		
		State state = new State(stateName, country);

		MvcResult result = mockMvc
				.perform(post(url).contentType("application/json").content(objectMapper.writeValueAsString(state))
						.with(csrf())) // in case post method
				.andDo(print()).andExpect(status().isOk()).andReturn();

		String response = result.getResponse().getContentAsString();
		Integer stateId = Integer.parseInt(response);

		Optional<State> findById = stateRepo.findById(stateId);
		assertThat(findById.isPresent());
		State savedState = findById.get();

		assertThat(savedState.getName()).isEqualTo(stateName);

	}

}
