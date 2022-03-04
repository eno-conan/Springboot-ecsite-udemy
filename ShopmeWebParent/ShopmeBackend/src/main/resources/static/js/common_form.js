$(document).ready(function() {
	$("#buttonCancel").on("click", function() {
		window.location = moduleURL;
	});

	$("#fileImage").change(function() {

		if (!checkFileSize(this)) {
			return;
		}
		showImageThumbnail(this);
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

/* add lesson-112*/
function checkFileSize(fileInput) {
	filesize = fileInput.files[0].size;

	if (filesize > (MAX_FILE_SIZE)) {
		fileInput.setCustomValidity("less than 1kB");
		fileInput.reportValidity();
		return false;
	} else {
		fileInput.setCustomValidity("");
		return true;
	}
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