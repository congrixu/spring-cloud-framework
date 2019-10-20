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
    var param = {};
    var defKey = $("#def_key").val();
    if (defKey) {
      param['search.pdKey'] = defKey;
    }
    return param;
  };

  ajaxPage($("#process_def_pagination"), "/workflow/pd/find", buildSearchParam, $("#main_content"));

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
  $("#scan_def_img_btn").click(function() {
    var $selDom = $("input[type=radio][name=selRadio]:checked");
    var val = $selDom.val();
    if (val) {
      var processDefinitionId = $selDom.attr("processDefinitionId");
      var url = "/workflow/pd/load-rsource-img?processDefinitionId=" + processDefinitionId;
      openDialog(url);
    }
  });

  $("#config_def_btn").click(function() {
    var $selDom = $("input[type=radio][name=selRadio]:checked");
    var val = $selDom.val();
    if (val) {
      var processDefinitionId = $selDom.attr("processDefinitionId");
      loadTable("/workflow/pd/to-config", {
        processDefinitionId : processDefinitionId
      });
    }
  });

  function openDialog(url) {
    url = url + "&noButton=true";
    window.open(url, '详细信息',
        'dialogWidth:1000px;dialogHeight:600px;resizable:yes;minimize:yes;maximize:yes;resizable=yes;status=no');
  }

});
