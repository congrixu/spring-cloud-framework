$(function() {
  $('#bind_form_table').DataTable({
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
    var defKey = $("#def_key").val();
    if (defKey) {
      param['pdKey'] = defKey;
    }
    return param;
  };

  ajaxPage($("#bind_form_pagination"), "/workflow/pd/query-bind-form", buildSearchParam, $("#main_content"));

  $("#search_btn").click(function() {
    loadTable("/workflow/pd/query-bind-form", buildSearchParam);
  });

  $("#add_bind_form_btn").click(function() {
    load("/workflow/pd/bind-form", null, null, function() {
      $("#bind_form_modal").data("callback", function() {
        $("#search_btn").click();
      });
    });
  });
  $("#del_bind_form_btn").click(function() {
    var selRadio = $("input[name=selRadio]:checked", "#bind_form_table");
    if (!selRadio || selRadio.length <= 0) {
      return false;
    }

    confirmMsg("您确认要删除当前表单表吗？", "硬认删除", function() {
      var id = $(selRadio).val();
      ajax("post", "/workflow/pd/delete-bind-form/" + id, null, function(data) {
        if (data === true) {
          $("#search_btn").click();
        } else {
          alertMsg("删除失败！");
        }
      });
    });
  });

});
