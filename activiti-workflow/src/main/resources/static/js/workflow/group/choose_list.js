$(function() {
  $('#choose_group_table').DataTable({
    lengthChange : false,
    searching : false,
    ordering : false,
    info : false,
    paging : false,
    serverSide : false,// 服务器模式
    autoWidth : true
  });

  var buildSearchParam = function() {
    var param = {};
    var groupId = $("#group_id", "#choose_group_search_form").val();
    var groupName = $("#group_name", "#choose_group_search_form").val();
    if (groupId) {
      param.groupId = groupId;
    }
    if (groupName) {
      param.groupName = groupName;
    }
    return param;
  };

  var showDiv = $("#show_choose_group_div");

  ajaxPage($("#choose_group_pagination"), "/workflow/group/choose", buildSearchParam, showDiv);

  $("#search_btn").click(function() {
    var param = buildSearchParam();
    load(url, param, showDiv, null);
  });
});
