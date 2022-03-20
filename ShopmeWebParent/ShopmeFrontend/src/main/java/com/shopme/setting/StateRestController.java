package com.shopme.setting;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.shopme.common.entity.Country;
import com.shopme.common.entity.State;
import com.shopme.common.entity.StateDTO;

@RestController
public class StateRestController {

	@Autowired
	private StateRepository stateRepo;

	@GetMapping("/settings/list_states_by_country/{id}")
	public List<StateDTO> listByCountry(@PathVariable("id") Integer countryId) {
		List<State> listStates = stateRepo.findAllByCountryOrderByNameAsc(new Country(countryId));
		List<StateDTO> result = new ArrayList<StateDTO>();

		for (State state : listStates) {
			result.add(new StateDTO(state.getId(), state.getName()));
		}

		return result;
	}

	@PostMapping("/states/save")
	public String save(@RequestBody State state) {
		State savedState = stateRepo.save(state);
		return String.valueOf(savedState.getId());
	}

	@GetMapping("/states/delete/{id}")
	public void delete(@PathVariable("id") Integer id) {
		stateRepo.deleteById(id);
	}

}
