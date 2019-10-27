$(function() {

  $('#oper_button_modal').modal({
    backdrop : 'static',
    keyboard : false
  });

  $('#oper_button_modal').on('hidden.bs.modal', function() {
    $('#oper_button_modal').parent().remove();
  });
  $("#oper_button_table tbody td[edit]").click(function() {
    var el = $(this);
    var val = el.html();
    createInputDom(el, val);
  });

  function createInputDom(el, initVal, clickEvent) {
    var inputArea = [];
    inputArea.push('<input type="text" class="form-control" value="' + initVal + '" >');

    var inputDom = $(inputArea.join(""));
    inputDom.click(function(event) {
      event.stopPropagation(); // 阻止事件冒泡
      if (typeof clickEvent == 'function') {
        clickEvent();
      }
    });
    inputDom.blur(function() {
      var val = $(this).val();
      var tdDom = $(this).parent();
      $(tdDom).html(val);
    });
    $(el).html(inputDom);

    inputDom.focus();
  }

  $("#add_dom_line").click(function() {
    var dom = [];
    dom.push("<tr>");
    dom.push("<td><a class='fa fa-minus-square' name='subA'></a></td>");
    dom.push("<td edit='operTitle'></td>");
    dom.push("<td edit='operValue'></td>");
    dom.push("</tr>");
    var domTr = $(dom.join(""));
    domTr.find("a").click(function() {
      subDomLine($(this));
    });
    domTr.find("[edit='operTitle'],[edit='operValue']").click(function() {
      var el = $(this);
      var val = el.html();
      createInputDom(el, val);
    });
    $("#oper_button_table tbody").append(domTr);
  });

  $("a[name=subA]").click(function() {
    subDomLine($(this));
  });

  function subDomLine(el) {
    $(el).parent().parent().remove();
  }

  $("#oper_button_submit_btn").click(function() {
    var trs = $("#oper_button_table tbody tr");
    if (trs && trs.length > 0) {
      var obs = [];
      var bpmnId = $("#oper_btn_bpmn_id").val();
      var processDefinitionId = $("#oper_btn_process_definition_id").val();
      $.each(trs, function(i, obj) {
        var tds = $(obj).find("td");
        var operTitle = $(tds[1]).html();
        var operValue = $(tds[2]).html();
        if (!operTitle || !operValue) {
          return true;// continue
        }
        var operBtn = {};
        operBtn.bpmnId = bpmnId;
        operBtn.processDefinitionId = processDefinitionId;
        operBtn.operTitle = operTitle;
        operBtn.operValue = operValue;
        obs.push(operBtn);
      });
      var param = {};
      param.bpmnId = bpmnId;
      param.processDefinitionId = processDefinitionId;
      param.operBtns = JSON.stringify(obs);
      ajax("post", "/workflow/pd/save-oper-button", param, function(data) {
        if (data) {
          alertMsg("配置成功！");
        } else {
          alertMsg("配置失败！");
        }
      });
    }
  });

});
