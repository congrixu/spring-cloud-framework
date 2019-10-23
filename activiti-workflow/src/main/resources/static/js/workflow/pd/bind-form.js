$(function() {
  $('#bind_form_modal').modal({
    backdrop : 'static',
    keyboard : false
  });

  $('#bind_form_modal').on('hidden.bs.modal', function() {
    $('#bind_form_modal').parent().remove();
  });

  $("#bind_form_submit_btn").click(function() {
    var formName = $("#form_name").val();
    var pdKey = $("#pd_key").val();
    if (!formName || !pdKey) {
      return false;
    }
    var param = {};
    param.formName = formName;
    param.processDefinitionKey = pdKey;

    ajaxBody("post", "/workflow/pd/save-bind-form", param, function(data) {
      if (data === true) {
        var callback = $("#bind_form_modal").data("callback");
        if (typeof callback == 'function') {
          callback();
        }
        $("#bind_form_cancel_btn").click();
      } else {
        alertMsg("绑定失败！");
      }
    });
  });
});
