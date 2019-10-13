$(function() {
  $('#file_upload_modal').modal({
    backdrop : 'static',
    keyboard : false
  });

  $("#dropz_div").dropzone({
    url : "/workflow/pd/deploy",
    method : "post",
    maxFiles : 1,
    maxFilesize : 1024,
    acceptedFiles : ".zip,.bar,.bpmn",
    autoProcessQueue : false,// 关闭自动上传
    paramName : "file",
    addRemoveLinks : true,
    dictDefaultMessage : '拖动文件至此或者点击上传',
    dictMaxFilesExceeded : "您最多只能上传1个文件！",
    dictResponseError : '文件上传失败!',
    dictInvalidFileType : "文件类型只能是*.jpg,*.gif,*.png,*.jpeg。",
    dictFallbackMessage : "浏览器不受支持",
    dictFileTooBig : "文件过大上传文件最大支持.",
    dictRemoveFile : "删除",
    dictCancelUpload : "取消",
    init : function() {
      var myDropzone = this;
      var submitButton = $("#file_upload_submit_btn");
      var cancelButton = $("#file_upload_cancel_btn");
      this.on("addedfile", function(file) {
        // 上传文件时触发的事件
      });
      this.on("success", function(file, data) {
        // 上传成功触发的事件
        console.log(data);
        var callback = $("#file_upload_modal").data("callback");
        if (typeof callback == 'function') {
          callback();
        }
      });
      this.on("error", function(file, data) {
        // 上传失败触发的事件
      });
      this.on("removedfile", function(file) {
        // 删除文件时触发的方法
      });
      submitButton.on('click', function() {
        // 点击上传文件
        myDropzone.processQueue();
      });
      cancelButton.on('click', function() {
        // 取消上传
        myDropzone.removeAllFiles();
      });
    }
  });

  $('#file_upload_modal').on('hidden.bs.modal', function() {
    $(this).parent().remove();
    $("input[type=file][class=dz-hidden-input]").remove();
  });
});
