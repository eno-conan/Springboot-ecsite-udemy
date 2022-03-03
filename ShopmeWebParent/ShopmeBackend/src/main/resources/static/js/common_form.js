$(document).ready(function() {
	$("#buttonCancel").on("click", function() {
		window.location = moduleURL;
	});

	$("#fileImage").change(function() {
		filesize = this.files[0].size;

		if (filesize > (MAX_FILE_SIZE)) {
			this.setCustomValidity("less than 1kB");
			this.reportValidity();
		} else {
			this.setCustomValidity("");
			showImageThumbnail(this);
		}
	});
});

function showImageThumbnail(fileInput) {
	var file = fileInput.files[0];
	var reader = new FileReader();
	reader.onload = function(e) {
		$("#thumbnail").attr("src", e.target.result);
	}

	reader.readAsDataURL(file);
}

function showModalDialog(title, message) {
	$("#modalTitle").text(title);
	$("#modalBody").text(message);
	$("#modalDialog").modal();
}

function showErrorModal(message) {
	showModalDialog("Error", message);
}

function showWarningModal(message) {
	showModalDialog("Warning", message);
}