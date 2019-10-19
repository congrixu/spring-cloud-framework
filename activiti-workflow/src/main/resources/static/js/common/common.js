window.onerror = function() {
  if (typeof console == "object" && typeof console.log == "function") {
    console.log(arguments);
  }
}
window.log = function() {
  if (typeof console == "object" && typeof console.log == "function") {
    console.log(arguments);
  }
}

window.openLoading = function() {

}

window.closeLoading = function() {

}

window.ajax = function(method, url, data, callback, options) {

  options = options || {};

  options.type = method.toUpperCase();
  options.url = ctxPath + url;
  options.data = data;
  options.contentType = "application/x-www-form-urlencoded; charset=utf-8";
  options.dataType = "json";
  options.success = callback;
  options.complete = function(xhr, textStatus) {
    closeLoading()
    if (!textStatus == 'success') {
      return;
    }
  }
  openLoading();
  $.ajax(options);
};

window.load = function(url, data, toEl, callback) {
  if (!toEl) {
    toEl = $("<div></div>")
    $(document.body).append(toEl);
  }

  $(toEl).load(ctxPath + url, data, function(resp, textStatus, xhr) {
    if (textStatus == 'success') {
      if ((typeof callback) == "function") {
        callback();
      }
    } else {
      $(toEl).html(resp);
    }
  });
};

window.loadTable = function(url, data) {
  openLoading();

  var param = data;
  if (typeof data == 'function') {
    param = data();
  }
  load(url, param, $("#main_content"), function() {
    closeLoading();
  });
}

/**
 * 替换字符
 */
String.prototype.replaceAll = function(s1, s2) {
  return this.replace(new RegExp(s1, "gm"), s2);
}

/**
 * 是否以指定字符结束
 * 
 */
String.prototype.endWith = function(str) {
  if (str == null || str == "" || this.length == 0 || str.length > this.length)
    return false;
  if (this.substring(this.length - str.length) == str)
    return true;
  else
    return false;
  return true;
}

/**
 * 是否以指定字符开始
 * 
 */
String.prototype.startWith = function(str) {
  if (str == null || str == "" || this.length == 0 || str.length > this.length)
    return false;
  if (this.substr(0, str.length) == str)
    return true;
  else
    return false;
  return true;
}

window.alertMsg = function(msg, title) {
  var t = title || "";
  alert(msg);
}

window.confirmMsg = function(msg, title, callback) {

}
