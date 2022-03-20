var buttonLoad;
var dropDownCountry;
var buttonAddCountry;
var buttonUpdateCountry;
var buttonDeleteCountry;
var labelCountryName;
var fieldCountryName;
var fieldCountryCode;

$(document).ready(function() {
	buttonLoad = $("#buttonLoadCountries");
	dropDownCountry = $("#dropDownCountries");
	buttonAddCountry = $("#buttonAddCountry");
	buttonUpdateCountry = $("#buttonUpdateCountry");
	buttonDeleteCountry = $("#buttonDeleteCountry");
	labelCountryName = $("#labelCountryName");
	fieldCountryName = $("#fieldCountryName");
	fieldCountryCode = $("#fieldCountryCode");

	buttonLoad.click(function() {
		loadCountries();
	});

	dropDownCountry.on("change", function() {
		changeFormStateToSelectedCountry();
	});

	//insert new country function
	buttonAddCountry.click(function() {
		if (buttonAddCountry.val() == "Add") {
			addCountry();
		} else {
			changeFormStateToNew();
		}
	})

	//update country function
	buttonUpdateCountry.click(function() {
		updateCountry();
	});

	buttonDeleteCountry.click(function() {
		deleteCountry();
	})
});

function loadCountries() {
	url = contextPath + "countries/list";
	$.get(url, function(responseJson) {
		dropDownCountry.empty();

		$.each(responseJson, function(index, country) {
			optionValue = country.id + "-" + country.code;
			$("<option>").val(optionValue).text(country.name).appendTo(dropDownCountry);
		});
	}).done(function() {
		buttonLoad.val("refresh");
		showToastMessage("Loaded All Countries");
	}).fail(function() {
		showToastMessage("Couldn't connect server");
	});
}

function deleteCountry() {
	optionValue = dropDownCountry.val();
	countryId = optionValue.split("-")[0];
	url = contextPath + "countries/delete/" + countryId;

	$.get(url, function() {
		$("#dropDownCountries option[value='" + optionValue + "']").remove();
		changeFormStateToNew();
	}).done(function() {
		showToastMessage("the Country has been deleted");
	}).fail(function() {
		showToastMessage("Couldn't connect server");
	});
}


function updateCountry() {
	url = contextPath + "countries/save";
	countryName = fieldCountryName.val();
	countryCode = fieldCountryCode.val();

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
		showToastMessage("The Country has been udpated: " + countryName);

		changeFormStateToNew();
	}).fail(function() {
		showToastMessage("Couldn't connect server");
	});
}

function addCountry() {
	url = contextPath + "countries/save";
	countryName = fieldCountryName.val();
	countryCode = fieldCountryCode.val();
	jsonData = { name: countryName, code: countryCode };
	$.ajax({
		type: 'POST',
		url: url,
		beforeSend: function(xhr) {
			xhr.setRequestHeader(csrfHeaderName, csrfValue);
		},
		data: JSON.stringify(jsonData),
		contentType: 'application/json'
	}).done(function(countryId) {
		selectedNewlyAddedCountry(countryId, countryCode, countryName);
		showToastMessage("The New Country has been added: " + countryName);
	}).fail(function() {
		showToastMessage("Couldn't connect server");
	});
}

function selectedNewlyAddedCountry(countryId, countryCode, countryName) {
	optionValue = countryName + "-" + countryCode;
	$("<option>").val(optionValue).text(countryName).appendTo(dropDownCountry);

	$("#dropDownCountries option[value='" + optionValue + "']").prop("selected", true);

	fieldCountryName.val("").focus();
	fieldCountryCode.val("");
}

function changeFormStateToNew() {
	buttonAddCountry.val("Add");
	labelCountryName.text("Country Name:");
	buttonUpdateCountry.prop("disabled", true);
	buttonDeleteCountry.prop("disabled", true);

	fieldCountryName.val("").focus();
	fieldCountryCode.val("");

}

function changeFormStateToSelectedCountry() {
	buttonAddCountry.prop("value", "New");//prop -> attr : perform correctly.
	buttonUpdateCountry.prop("disabled", false);
	buttonDeleteCountry.prop("disabled", false);

	labelCountryName.text("selected Country:");

	selectedCountryName = $("#dropDownCountries option:selected").text();
	fieldCountryName.val(selectedCountryName);

	countryCode = dropDownCountry.val().split("-")[1];
	fieldCountryCode.val(countryCode);
};

function showToastMessage(message) {
	$("#toastMessageCountry").text(message);
	$(".toast").toast('show');
}
