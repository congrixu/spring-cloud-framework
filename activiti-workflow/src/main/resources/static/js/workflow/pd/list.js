$(function() {

  $('#process_def_table').DataTable({
    paging : true,
    lengthChange : false,
    searching : false,
    ordering : false,
    info : true,
    autoWidth : false,
    "bLengthChange" : false, // 开关，是否显示每页显示多少条数据的下拉框
    "aLengthMenu" : [ [ 5, 10, 25, -1 ], [ 5, 10, 25, "所有" ] ],// 设置每页显示数据条数的下拉选项
    'iDisplayLength' : 10, // 每页初始显示5条记录
    'bFilter' : false, // 是否使用内置的过滤功能（是否去掉搜索框）
    "bInfo" : true, // 开关，是否显示表格的一些信息(当前显示XX-XX条数据，共XX条)
    "bPaginate" : true, // 开关，是否显示分页器
    "bSort" : false, // 是否可排序 
    "oLanguage" : { // 语言转换
      "sInfo" : "显示第 _START_ 至 _END_ 项结果，共_TOTAL_ 项",
      "sLengthMenu" : "每页显示 _MENU_ 项结果",
      "oPaginate" : {
        "sFirst" : "首页",
        "sPrevious" : "前一页",
        "sNext" : "后一页",
        "sLast" : "尾页"
      }
    }
  });

  var buildSearchParam = function() {
  };

  $("#search_btn").click(function() {
    loadTable("/workflow/pd/find", buildSearchParam);
  });
  $("#deploy_btn").click(function() {
    load("/workflow/pd/uplod", null, null, function() {
      $("#file_upload_modal").data("callback", function() {
        $("#search_btn").click();
      });
    });
  });
});
