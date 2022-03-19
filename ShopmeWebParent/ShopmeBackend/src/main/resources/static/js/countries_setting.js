var buttonLoad
var dropDownCountry

$(document).ready(function() {
	buttonLoad = $("#buttonLoadCountries");
	dropDownCountry = $("#dropDownCountries");

	buttonLoad.click(function() {
		loadCountries();
	});
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
	}).fail(function(){
		showToastMessage("Couldn't connect server");
	});
}

function showToastMessage(message) {
	$("#toastMessage").text(message);
	$(".toast").toast('show');
}
