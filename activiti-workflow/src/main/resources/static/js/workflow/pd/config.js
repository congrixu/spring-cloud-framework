$(function() {
  $('#pd_config_tab a').click(function(e) {
    e.preventDefault();
    $(this).tab('show');
    var name = $(this).attr("name");
    if (name == 'configNode') {
      loadPD();
    } else {
      configForm();
    }
  });

  function loadPD() {
    var url = "/workflow/pd/find-bpmn";
    loadData(url);
  }

  function loadForm() {
    var url = "";
    loadData(url);
  }

  function loadData(url) {
    var processDefinitionId = $("#process_definition_id").val();
    load(url, {
      processDefinitionId : processDefinitionId
    }, $("#pd_config_tab_content"), null);
  }

  loadPD();// 初始化流程定义配置

});
