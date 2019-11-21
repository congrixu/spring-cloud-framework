$(function() {

  $('#process_pi_table').DataTable({
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
    var applUserName = $("#appl_user_name").val();
    if (defKey) {
      param['search.applUserName'] = applUserName;
    }
    return param;
  };

  ajaxPage($("#process_pi_pagination"), "/workflow/pd/query", buildSearchParam, $("#main_content"));

  $("#search_btn").click(function() {
    loadTable("/workflow/pi/query", buildSearchParam);
  });

});
