$(function() {
  $('#choose_user_table').DataTable({
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
    var userId = $("#user_id", "#choose_user_search_form").val();
    var userName = $("#user_name", "#choose_user_search_form").val();
    if (userId) {
      param.userId = userId;
    }
    if (userName) {
      param.userName = userName;
    }
    return param;
  };

  var showDiv = $("#show_choose_user_div");

  ajaxPage($("#choose_user_pagination"), "/workflow/user/choose", buildSearchParam, showDiv);

  $("#search_btn").click(function() {
    var param = buildSearchParam();
    load(url, param, showDiv, null);
  });
});
