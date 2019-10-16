$(function() {

  $('#process_def_table').DataTable({
    lengthChange : false,
    searching : false,
    ordering : false,
    info : false,
    paging : false,
    serverSide : false,// 服务器模式
    autoWidth : true
  });

  var buildSearchParam = function() {
    var search = {};
    var defKey = $("#def_key").val();
    if (defKey) {
      search.key = defKey;
    }
    return search;
  };

  ajaxMainContentPage($("#process_def_pagination"), "/workflow/pd/find", buildSearchParam, $("#main_content"));

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

  $("#scan_def_xml_btn").click(function() {
    var $selDom = $("input[type=radio][name=selRadio]:checked");
    var val = $selDom.val();
    if (val) {
      var deploymentId = $selDom.attr("deployId");
      var resourceName = $selDom.attr("resourceName");
      var url = "/workflow/pd/load-rsource?deploymentId=" + deploymentId + "&resourceName=" + resourceName;
      openDialog(url);
    }
  });

  function openDialog(url) {
    url = url + "&noButton=true";
    window.showModalDialog(url, '详细信息',
        'dialogWidth:1000px;dialogHeight:600px;resizable:yes;minimize:yes;maximize:yes;resizable=yes;status=no');
  }

});
