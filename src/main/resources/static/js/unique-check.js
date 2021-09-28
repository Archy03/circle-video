
function checkUserNameUnique(confirmUserName) {
  var url = "[[@{/users/check_username}]]";
  var userName = $("#userName").val();
  var userId = $("#id").val();
  var csrfValue = $("input[name='_csrf']").val();
  var params = {id: userId, userName: userName, _csrf: csrfValue};

  $.post(url, params, function (response) {
    if (response === "OK") {
      confirmUserName.setCustomValidity("");
    } else if (response === "Duplicated") {
      confirmUserName.setCustomValidity(
          "User " + userName + " already exists!");
    } else {
      showModalDialog("Error", "Unknown response from server");
    }
  }).fail(function () {
    showModalDialog("Error", "Could not connect to the server");
  });
  return false;
}

function checkEmailUnique(confirmEmail) {
  var url = "[[@{/users/check_email}]]";
  var email = $("#email").val();
  var userId = $("#id").val();
  var csrfValue = $("input[name='_csrf']").val();
  var params = {id: userId, email: email, _csrf: csrfValue};

  $.post(url, params, function (response) {
    if (response === "OK") {
      confirmEmail.setCustomValidity("");
    } else if (response === "Duplicated") {
      confirmEmail.setCustomValidity("Email " + email + " already exists!");
    } else {
      showModalDialog("Error", "Unknown response from server");
    }
  }).fail(function () {
    showModalDialog("Error", "Could not connect to the server");
  });
  return false;
}

function showModalDialog(title, message) {
  $("#modalTitle").text(title);
  $("#modalBody").text(message);
  $("#modalDialog").modal();
}
