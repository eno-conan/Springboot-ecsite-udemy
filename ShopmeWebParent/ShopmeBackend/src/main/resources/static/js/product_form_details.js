function addNextDetailSection() {

	allDivDetails = $("[id^='divDetail']");
	divDetailsCount = allDivDetails.length;

	htmlDetailSection = `
		<div class="form-inline" id=divDetail${divDetailsCount}>
			<label class="m-3">Name</label> <input type="text"
				class="form-control w-25" name="detailNames" maxlength="255" /> 
				<label class="m-3">value</label> <input type="text"
				class="form-control w-25" name="detailValues" maxlength="255" />
		</div>`;

	$("#divProductDetails").append(htmlDetailSection);

	previousDivDetailSection = allDivDetails.last();
	previousDivDetailID = previousDivDetailSection.attr("id");

	htmlLinkRemove = `
		<a class="ftn fas fa-times-circle fa-2x icon-dark m-3"
		href="javascript:removeDetailSectionById('${previousDivDetailID}')"
		title="remove this detail"
		></a>
	`;

	//delte button add last part
	previousDivDetailSection.append(htmlLinkRemove);

	$("input[name='detailNames']").last().focus();

};

function removeDetailSectionById(id) {
	$("#" + id).remove();
}