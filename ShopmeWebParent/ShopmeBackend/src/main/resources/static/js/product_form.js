var extraImagesCount = 0;

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

	$("input[name='extraImage']").each(function(index) {
		extraImagesCount++;
		$(this).change(function() {
			showExtraImageThumbnail(this, index)
		});
	});

	$("#extraImage1").change(function() {
		if (!checkFileSize(this)) {
			return;
		}
		showExtraImageThumbnail(this);
	});

});

function showExtraImageThumbnail(fileInput, index) {
	var file = fileInput.files[0];
	var reader = new FileReader();
	reader.onload = function(e) {
		$("#extraThumbnail" + index).attr("src", e.target.result);
	}

	reader.readAsDataURL(file);

	if (index >= extraImagesCount - 1) {
		addNextExtraImageSection(index + 1);
	}
};

function addNextExtraImageSection(index) {
	console.log(index);
	htmlExtraImage = `<div class="col border m-3 p-2" id="divExtraImage${index}">
				<div id="extraImageHeader${index}">
					<label>Extra Image# ${index + 1}</label>
				</div>
				<div class="m-2">
					<img id="extraThumbnail${index}" src="${defaultImageThumbnailSrc}"
						alt="Extra Image#${index + 1} Preview" class="img-fluid" />
				</div>
				<div>
					<input type="file" name="extraImage"
					onChange="showExtraImageThumbnail(this,${index})"
						accept="image/jpeg,image/png" />
				</div>
			</div>
		`;

	htmlLinkRemove = `
		<a class="ftn fas fa-times-circle fa-2x icon-dark float-right" 
		href="javascript:removeExtraImage(${index - 1})"
		title="remove this image"
		></a>
	`;

	$("#divProductImages").append(htmlExtraImage);

	$("#extraImageHeader" + (index - 1)).append(htmlLinkRemove);

	extraImagesCount++;
}

function removeExtraImage(index) {
	$("#divExtraImage" + index).remove();
}


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