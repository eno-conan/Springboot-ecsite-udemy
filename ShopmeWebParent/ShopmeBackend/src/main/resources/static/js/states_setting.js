var buttonLoadCountriesForStates;
var dropDownCountriesForStates;
var buttonAddState;
var buttonUpdateState;
var buttonDeleteState;
var labelStateName;
var fieldStateName;

$(document).ready(function() {
	buttonLoadCountriesForStates = $("#buttonLoadCountriesForStates");
	dropDownCountriesForStates = $("#dropDownCountriesForStates");
	dropDownStatesRelatedCountry = $("#dropDownStatesRelatedCountry");

	labelCountryNameForStates = $("#labelCountryNameForStates");
	labelStateName = $("#labelStateName");
	fieldStateName = $("#fieldStateName");

	buttonAddState = $("#buttonAddState");
	buttonUpdateState = $("#buttonUpdateState");
	buttonDeleteState = $("#buttonDeleteState");

	buttonLoadCountriesForStates.click(function() {
		loadCountriesForStates();
	});

	dropDownCountriesForStates.on("change", function() {
		loadStatesSelectedCountry();
	});

	//insert new country function
	buttonAddState.click(function() {
		if (buttonAddState.val() == "Add") {
			addState();
		} else {
			changeFormStateToNew();
		}
	})
	/*
	
		//update State function
		buttonUpdateState.click(function() {
			updateState();
		});
	
		buttonDeleteState.click(function() {
			deleteState();
		})*/
});

function loadCountriesForStates() {
	url = contextPath + "countries/list";
	$.get(url, function(responseJson) {
		dropDownCountriesForStates.empty();

		$.each(responseJson, function(index, country) {
			$("<option>").val(country.id).text(country.name).appendTo(dropDownCountriesForStates);
		});
	}).done(function() {
		buttonLoadCountriesForStates.val("refresh");
		showToastMessageForStates("Loaded All Countries");
	}).fail(function() {
		showToastMessageForStates("Couldn't connect server");
	});
}

function loadStatesSelectedCountry() {
	selectedCountryId = $("#dropDownCountriesForStates option:selected");
	url = contextPath + "states/list_by_country/" + selectedCountryId.val();
	$.get(url, function(responseJson) {
		dropDownStatesRelatedCountry.empty();

		$.each(responseJson, function(index, state) {
			$("<option>").val(state.id).text(state.name).appendTo(dropDownStatesRelatedCountry);
		});
	}).done(function() {
		changeFormStateToNew();
		showToastMessageForStates("Loaded All States");
	}).fail(function() {
		showToastMessageForStates("Couldn't connect server");
	});
}

function addState() {
	url = contextPath + "states/save";
	selectedCountry = $("#dropDownCountriesForStates option:selected");
	countryId = selectedCountry.val();
	countryName = selectedCountry.text();
	stateName = fieldStateName.val();
	
	jsonData = { name: stateName, country: { id: countryId, name: countryName }};
	$.ajax({
		type: 'POST',
		url: url,
		beforeSend: function(xhr) {
			xhr.setRequestHeader(csrfHeaderName, csrfValue);
		},
		data: JSON.stringify(jsonData),
		contentType: 'application/json'
	}).done(function(stateId) {
		selectedNewlyAddedState(stateId, stateName);
		showToastMessageForStates("The New State has been added: " + stateName);
	}).fail(function() {
		showToastMessageForStates("Couldn't connect server");
	});
}

function deleteState() {
	optionValue = dropDownCountry.val();
	countryId = optionValue.split("-")[0];
	url = contextPath + "countries/delete/" + countryId;

	$.get(url, function() {
		$("#dropDownCountries option[value='" + optionValue + "']").remove();
		changeFormStateToNew();
	}).done(function() {
		showToastMessageForStates("the Country has been deleted");
	}).fail(function() {
		showToastMessageForStates("Couldn't connect server");
	});
}


function updateState() {
	url = contextPath + "countries/save";
	countryName = fieldCountryName.val();

	countryId = dropDownCountry.val().split("-")[0];

	jsonData = { id: countryId, name: countryName, code: countryCode };
	
	$.ajax({
		type: 'POST',
		url: url,
		beforeSend: function(xhr) {
			xhr.setRequestHeader(csrfHeaderName, csrfValue);
		},
		data: JSON.stringify(jsonData),
		contentType: 'application/json'
	}).done(function(countryId) {
		$("#dropDownCountries option:selected").val(countryId + "-" + countryCode);
		$("#dropDownCountries option:selected").text(countryName);
		showToastMessageForStates("The Country has been udpated: " + countryName);

		changeFormStateToNew();
	}).fail(function() {
		showToastMessage("Couldn't connect server");
	});
}


function selectedNewlyAddedState(stateId, stateName) {
	$("<option>").val(stateId).text(stateName).appendTo(dropDownStatesRelatedCountry);

	$("#dropDownStatesRelatedCountry option[value='" + stateId + "']").prop("selected", true);

	fieldStateName.val("").focus();
}

function changeFormStateToNew() {
	buttonAddState.val("Add");
	labelStateName.text("Country Name:");
	buttonUpdateState.prop("disabled", true);
	buttonDeleteState.prop("disabled", true);

	fieldStateName.val("").focus();

}

function showToastMessageForStates(message) {
	$("#toastMessageForState").text(message);
	$(".toast").toast('show');
}
