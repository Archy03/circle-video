$( document ).ready(function() {

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Show more medias

    $('#listRightMedias .h-media:lt(5)').show();
    var items =  $('#listRightMedias .h-media').length;
    var shown =  5;
    var loadmore = $('.loadmore');
    if(items <= shown) loadmore.hide();
    loadmore.click(function () {
        shown += 5;
        if(shown < items) {$('#listRightMedias .h-media:lt('+shown+')').show();}
        else {$('#listRightMedias .h-media:lt('+items+')').show();
            loadmore.hide();
        }
    });

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Logout

    $("#logoutLink").on("click", function (e) {
        e.preventDefault();
        document.logoutForm.submit();
    });

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // User form, cancel button

    $("#cancelButton").on("click", function() {
        history.back();
    });

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Dismiss alert message automatically

    setTimeout(function() {
        $("#success-alert").alert('close');
    }, 3500);

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // My medias sort

    var userMediaSortHeader = $('#userMediaSortHeader');
    userMediaSortHeader.on('click', 'a[data-action="userMediaSort"]', function (event) {
        event.preventDefault();
        var btn = $(this);
        $('#userMediaList').load(btn.attr('data-href') + " #userMediaList>*", "");
        var activeLink = userMediaSortHeader.find('a.active');
        activeLink.removeClass('active disabledLink');
        btn.addClass('active disabledLink');
    });

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Delete user confirm

    $(".deleteUser").click(function (e) {
        e.preventDefault();
        var link = $(this);
        var userFullName = link.attr("userFullName");
        $("#yesButton").attr("href", link.attr("href"));
        $("#confirmText").html("<p>Do you really want to delete <strong>" + userFullName + "</strong>?<br>If the User has any medias, comments, ratings, it will be also deleted!</br>This process cannot be undone!</p>");
        $("#confirmModal").modal();
    });

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Delete media confirm
    /* No event has been added to your element because events are added when the document is ready.
    You need to use .on() method of jQuery to prevent this. So the event is added to document instead of the element itself.
    *  */
    $(function(){
        $(document).on('click', 'a.mediaDelete', function (e) {
            e.preventDefault();
            var link = $(this);
            var title = link.attr("title");
            var userFullName = link.attr("userFullName");
            $("#yesButton").attr("href", link.attr("href"));
            $("#confirmText").html("<p>Do you really want to delete <strong>" + title + "</strong> media from <strong>" + userFullName + "</strong>?<br>All the media comments and ratings will be lost!</br>This process cannot be undone!</p>");
            $("#confirmModal").modal();
        })
    });

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // User profile picture upload check size and profile picture preview

    $("#fileImage").change(function () {
        var fileSize = this.files[0].size / 1024 / 1024;
        if(fileSize > 1) {
            this.setCustomValidity("Only image that is 1MB or less!!");
            this.reportValidity();
        } else {
            this.setCustomValidity("");
            showImageThumbnail(this);
        }
    });

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Show image thumbnail path

    function showImageThumbnail(fileInput) {
        var file = fileInput.files[0];
        var reader = new FileReader();
        reader.onload = function (e) {
            $("#thumbnail").attr("src", e.target.result);
        };

        reader.readAsDataURL(file);
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Header
    // Goto section
    $('[data-toggle="tooltip"]').tooltip();


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Header
    // Dropdown for "Pages" element on hover instead click.

	$('.pages').hover(function() { $(this).addClass('open'); }, function() { $(this).removeClass('open'); });


});

