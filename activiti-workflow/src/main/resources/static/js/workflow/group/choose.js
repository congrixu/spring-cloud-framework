$(function() {
  $('#choose_group_modal').modal({
    backdrop : 'static',
    keyboard : false
  });

  load("/workflow/group/choose", {}, $("#show_choose_group_div"), null);

  $("#choose_group_submit_btn").click(function() {
    var sels = $("input[name=chooseGroup]:checked", "#choose_group_table");
    if (sels && sels.length > 0) {
      var groupIds = [];
      var groupNames = [];
      $.each(sels, function(i, obj) {
        var groupId = $(this).val();
        var groupName = $(this).attr("groupName");
        groupIds.push(groupId);
        groupNames.push(groupName);
      });

      var callback = $("#choose_group_modal").data("callback");
      if (typeof callback == 'function') {
        if ("${mutl}") {
          callback(groupIds, groupNames);
        } else {
          callback(groupId, groupName);
        }
      }
      $("#choose_group_cancel_btn").click();
    } else {
      alertMsg("请选择您要设置的用户组");
    }
  });

  $('#choose_group_modal').on('hidden.bs.modal', function(e) {
    $("#choose_group_modal").parent().remove();
  })
});
