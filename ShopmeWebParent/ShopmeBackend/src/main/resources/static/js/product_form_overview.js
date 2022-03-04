dropdownBrands = $("#brand");
dropdownCategories = $("#category");

/* read this after reading html DOM */
/*document.addEventListener("DOMContentLoaded", function() {*/
$(document).ready(function() {

	$("#shortDescription").richText();/* only this??? */
	$("#fullDescription").richText();/* only this??? */

	dropdownBrands.change(function() {
		dropdownCategories.empty();
		getCategories();
	});
	
	getCategories();

});

function getCategories() {
	brandId = dropdownBrands.val();
	url = brandModuleURL + "/" + brandId + "/categories";

	$.get(url, function(responseJson) {
		$.each(responseJson, function(index, category) {
			/* index:start zero */
			$("<option>").val(category.id).text(category.name)
				.appendTo(dropdownCategories);
		})
	})
}

function checkUnique(form) {

	productId = $("#id").val();
	productName = $("#name").val();
	csrfValue = $("input[name='_csrf']").val();
	params = {
		id: productId,
		name: productName,
		_csrf: csrfValue
	};

	/*url = moduleURL + "/check_unique";*/ //apply
	/*url = `[[@{/products/check_unique}]]`;*/ //not apply...
	/*url = "[[@{/products/check_unique}]]";*/ //not apply...
	$.post(
		checkUniqueURL,
		params,
		function(response) {
			console.log(response);
			if (response == "OK") {
				form.submit();
			} else if (response == "Duplicated") {
				showWarningModal("CategoryName is Duplicated! Input value is "
					+ productName);
			} else {
				showErrorModal("Unknown Error Happened");
			}
		}).fail(function() {
			showErrorModal("Error", "Could not connect server");
		});
	return false;

}