$(document).ready(function() {

  ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  // Rating and Comment
  // Rating

  var container = $("#voteSection");
  container.on('click', 'a[data-action="vote"]', function(event){
    event.preventDefault();
    var btn = $(this);
    btn.addClass('voted');
    var voteType = btn.attr('data-vote-type');

    $.ajax({
      type: 'post',
      url: btn.attr('data-href'),
      success: function (data) {
        if (data === 'ok') {
          var valLikeElement = btn.siblings('.rating-value-like');
          var valLike = parseInt(valLikeElement.html());
          var valDislikeElement = btn.siblings('.rating-value-dislike');
          var valLDislike = parseInt(valDislikeElement.html());
          btn.parent().find('a[data-action="vote"]').attr('data-action', '');
          if (voteType === 'like') {
            valLike++;
            valLikeElement.html(valLike);
          } else {
            valLDislike++;
            valDislikeElement.html(valLDislike);
          }
          if (valLike === 0 || valLike === 1) {
            valLikeElement.removeClass('rating-value-no rating-value-positive');
            valLikeElement.addClass(valLike === 0 ? 'rating-value-no' : 'rating-value-positive');
          }
          if (valLDislike === 0 || valLDislike === 1) {
            valDislikeElement.removeClass('rating-value-no rating-value-negative');
            valDislikeElement.addClass(valLDislike === 0 ? 'rating-value-no' : 'rating-value-negative');
          }
          container.load(location.href + " #voteSection>*","");
        }
        else {
          btn.removeClass('voted');
        }
      },
      error: function () {
        alert('Voting failed. Try reloading page.');
      }
    });
  });

  // New Comment

  var commentForm = $('#commentForm');
  var comment_header = $("#comment_header");
  var commentCount = $("#commentCount");
  commentForm.on('submit', function(event) {
    event.preventDefault();
    $.ajax({
      type: 'post',
      url: commentForm.attr('action'),
      data: commentForm.serialize(),
      success: function (result) {
        if (result === 'ok') {
          $('html,body').animate({scrollTop: comment_header.offset().top}, 'slow');
          commentCount.load(location.href + " #commentCount>*", "");
          $('#text').val('');
            comment_header.load(location.href + " #comment_header>*", "");
          setTimeout(function() {
            $('#commentList .cl-comment:first').addClass('newComment');
          }, 100);
          setTimeout(function() {
            $('#commentList .cl-comment:first').removeClass('newComment');
          }, 3000);
        }
      }
    });
  });

  // Sort Comment

  comment_header.on('click', 'a[data-action="commentSort"]', function (event) {
    event.preventDefault();
    var btn = $(this);
    btn.addClass("active");
    btn.addClass("disabledLink");
    var commentSortType = btn.attr('data-comment-sort-type');
    $.ajax({
      url: btn.attr('data-href'),
      success: function () {
        $('#commentList').load(this.url + " #commentList>*", "");
        var btnNewest = $('#newest');
        var btnOldest = $('#oldest');
        if(commentSortType === 'oldest') {
          btnNewest.removeClass('active');
          btnNewest.removeClass('disabledLink');
        } else {
          btnOldest.removeClass('active');
          btnOldest.removeClass('disabledLink');
        }
      }
    });
  });

  // Delete Comment

  comment_header.on("click", 'a[data-action="delete-comment"]', function (event) {
    event.preventDefault();
    var btn = $(this);
    $.ajax({
      url: btn.attr('data-href'),
      success: function (result) {
        if(result === "ok") {
          comment_header.load(location.href + " #comment_header>*", "");
          commentCount.load(location.href + " #commentCount>*", "");
        }
      }
    });
  });
});

