"use strict";

var mediaInput = document.getElementById('media_file');

mediaInput.onchange = function (e) {
	var fullPath = mediaInput.value;
	if (fullPath) {
		var startIndex = (fullPath.indexOf('\\') >= 0 ? fullPath.lastIndexOf('\\') : fullPath.lastIndexOf('/'));
		var filename = fullPath.substring(startIndex);
		if (filename.indexOf('\\') === 0 || filename.indexOf('/') === 0) {
			filename = filename.substring(1);
		}
		jQuery('#media-label-text').text(filename);
	}
};

var thumbnailInput = document.getElementById('thumbnail_file');

if(thumbnailInput != null) {
	thumbnailInput.onchange = function (e) {
		var fullPath = thumbnailInput.value;
		if (fullPath) {
			var startIndex = (fullPath.indexOf('\\') >= 0 ? fullPath.lastIndexOf('\\') : fullPath.lastIndexOf('/'));
			var filename = fullPath.substring(startIndex);
			if (filename.indexOf('\\') === 0 || filename.indexOf('/') === 0) {
				filename = filename.substring(1);
			}
			jQuery('#thumbnail-label-text').text(filename);
		}
	};
}


////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// Media upload check size and thumbnail

$('#media_file').change(function () {
	var mediaCategory = $(this).attr('data-media-category');
	var fileSize = this.files[0].size / 1024 / 1024;
	if(mediaCategory === 'video') {
		if(fileSize > 250) {
			$("#media-label-text").text("Only video that is 250MB or less!");
			this.setCustomValidity("Only video that is 250MB or less!");
			$("#media_file_label").addClass('big_file_error');
			this.reportValidity();
		} else {
			this.setCustomValidity("");
			$("#media_file_label").removeClass('big_file_error');
		}
	}
	if(mediaCategory === 'presentation') {
		if(fileSize > 5) {
			$("#media-label-text").text("Only presentation that is 5MB or less!");
			this.setCustomValidity("Only presentation that is 5MB or less!");
			$("#media_file_label").addClass('big_file_error');
			this.reportValidity();
		} else {
			this.setCustomValidity("");
			$("#media_file_label").removeClass('big_file_error');
		}
	}
	if(mediaCategory === 'picture') {
		if(fileSize > 2) {
			$("#media-label-text").text("Only picture that is 2MB or less!");
			this.setCustomValidity("Only picture that is 2MB or less!");
			$("#media_file_label").addClass('big_file_error');
			this.reportValidity();
		} else {
			this.setCustomValidity("");
			$("#media_file_label").removeClass('big_file_error');
		}
	}
});

$('#thumbnail_file').change(function () {
	var fileSize = this.files[0].size / 1024 / 1024;
	if(fileSize > 1) {
		$("#thumbnail-label-text").text("Only thumbnail image that is 1MB or less!");
		this.setCustomValidity("Only thumbnail image that is 1MB or less!");
		$("#thumbnail_file_label").addClass('big_file_error');
		this.reportValidity();
	} else {
		this.setCustomValidity("");
		$("#thumbnail_file_label").removeClass('big_file_error');
	}
});

