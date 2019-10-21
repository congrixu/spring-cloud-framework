$(function() {
  $("#process_def_config_table tbody td[edit=userIds]").click(function() {
    var bpmnType = $(this).attr("bpmnType");
    if ('UserTask' != bpmnType) {
      alertMsg("只有用户任务才可以配置用户信息！");
      return false;
    }

    var el = $(this);
    var clickEvent = function() {
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
      });
    }

    var userIds = el.html();
    createInputDom(el, userIds, clickEvent);
  });

  $("#process_def_config_table tbody td[edit=groupIds]").click(function() {
    var bpmnType = $(this).attr("bpmnType");
    if ('UserTask' != bpmnType) {
      alertMsg("只有用户任务才可以配置用户组信息！");
      return false;
    }

    var el = $(this);
    var clickEvent = function() {
      load("/workflow/group/to-choose", {}, null, function() {
        $("#choose_group_modal").data("callback", function(groupIds, groupNames) {
          // console.log(userIds)
          if (groupIds) {
            var groupIdStr = groupIds.join(",");
            var groupNameStr = groupNames.join(",");
            groupIdStr = groupIdStr + ",";
            groupNameStr = groupNameStr + ",";

            $(el).html(groupIdStr);
            $(el).next().html(groupNameStr);
          }
        });
      });
    }
    var groupIds = el.html();
    createInputDom(el, groupIds, clickEvent);
  });

  function createInputDom(el, initVal, clickEvent) {
    var inputArea = [];
    inputArea.push('<input type="text" class="form-control" value="' + initVal + '" >');

    var inputDom = $(inputArea.join(""));
    inputDom.click(function(event) {
      event.stopPropagation(); // 阻止事件冒泡
      clickEvent();
    });
    inputDom.blur(function() {
      var val = $(this).val();
      var tdDom = $(this).parent();
      $(tdDom).html(val);
    });
    $(el).html(inputDom);

    inputDom.focus();
  }

  $("#submit_pro_def_config_btn").click(function() {

    var ugConfig = [];
    var trs = $("#process_def_config_table tbody tr");
    $.each(trs, function(i, tr) {
      var tds = $(tr).find("td");
      var bpmnId = $(tds[0]).html();
      var userIds = $(tds[3]).html();
      var userNames = $(tds[4]).html();
      var groupIds = $(tds[5]).html();
      var groupNames = $(tds[6]).html();
      var config = {};
      config.bpmnId = bpmnId;
      config.userIds = userIds;
      config.userNames = userNames;
      config.groupIds = groupIds;
      config.groupNames = groupNames;

      ugConfig.push.push(config);
    });

    var param = {};
    param.processDefinitionId = $("#process_definition_id").val();
    param.ugConfigJson = JSON.stringify(ugConfig);//转换JSON字符串
    ajax("post", "save-config-usergroup", param, function(data) {
      if (data) {
        alertMsg("配置成功！");
      } else {
        alertMsg("配置失败！");
      }
    });
  });

})
