$(function() {
  $('#choose_user_modal').modal({
    backdrop : 'static',
    keyboard : false
  });

  load("/workflow/user/choose", {}, $("#show_choose_user_div"), null);

  $("#choose_user_submit_btn").click(function() {
    var sels = $("input[name=chooseUser]:checked", "#choose_user_table");
    if (sels && sels.length > 0) {
      var userIds = [];
      var userNames = [];
      $.each(sels, function(i, obj) {
        var userId = $(this).val();
        var userName = $(this).attr("userName");
        userIds.push(userId);
        userNames.push(userName);
      });

      var callback = $("#choose_user_modal").data("callback");
      if (typeof callback == 'function') {
        if ("${mutl}") {
          callback(userIds, userNames);
        } else {
          callback(userId, userName);
        }
      }
      $("#choose_user_cancel_btn").click();
    } else {
      alertMsg("请选择您要设置的用户");
    }
  });

  $('#choose_user_modal').on('hidden.bs.modal', function(e) {
    $("#choose_user_modal").parent().remove();
  })
});
