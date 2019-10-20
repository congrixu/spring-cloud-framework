$(function() {
  $("#process_def_config_table tbody td[edit=userIds]").click(function() {
    var bpmnType = $(this).attr("bpmnType");
    if ('UserTask' != bpmnType) {
      alertMsg("只有用户任务才可以配置用户信息！");
      return false;
    }

    var userIds = $(this).html();
    createInputDom($(this), userIds);
  });

  function createInputDom(el, val) {
    var inputArea = [];
    inputArea.push('<input type="text" class="form-control" value="' + val + '" >');

    var inputDom = $(inputArea.join(""));
    inputDom.click(function(event) {
      event.stopPropagation(); // 阻止事件冒泡
      load("/workflow/user/to-choose", {}, null, function() {
        $("#choose_user_modal").data("callback", function(userIds, userNames) {
          // console.log(userIds)
          if (userIds) {
            var userIdStr = userIds.join(",");
            var userNameStr = userNames.join(",");
            userIdStr = userIdStr + ",";
            userNameStr = userNameStr + ",";
            
            $(el).html(userIdStr);
            $(el).next().html(userNameStr)
          }
        });
      })
    });
    inputDom.blur(function() {
      var val = $(this).val();
      var tdDom = $(this).parent();
      $(tdDom).html(val);
    });
    $(el).html(inputDom);

    inputDom.focus();
  }
})
