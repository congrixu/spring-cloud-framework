function initPageOption(paginationEl, opts) {
  var options = {};
  options.bootstrapMajorVersion = 3;
  options.currentPage = '1';// 当前页
  options.totalPages = '0';// 总页数
  options.useBootstrapTooltip = true;
  options.size = "normal";
  options.alignment = "left";
  options.itemTexts = function(type, page, current) {
    switch (type) {
    case "first":
      return "首页";
    case "prev":
      return "上一页";
    case "next":
      return "下一页";
    case "last":
      return "尾页";
    case "page":
      return page;
    }
  };
  options.tooltipTitles = function(type, page, current) {
    switch (type) {
    case "first":
      return "首页 <i class='icon-fast-backward icon-white'></i>";
    case "prev":
      return "上一页 <i class='icon-backward icon-white'></i>";
    case "next":
      return "下一页 <i class='icon-forward icon-white'></i>";
    case "last":
      return "尾页 <i class='icon-fast-forward icon-white'></i>";
    case "page":
      return "第 " + page + " 页<i class='icon-file icon-white'></i>";
    }
  };
  options.bootstrapTooltipOptions = {
    html : true,
    placement : 'bottom'
  };

  if (opts) {
    for ( var key in opts) {
      options[key] = opts[key];
    }
  }

  if (!opts || !opts["currentPage"] || !opts["totalPage"]) {
    var currentPage = paginationEl.attr("currentPage");
    var totalPage = paginationEl.attr("totalPage");
    options.currentPage = currentPage;
    options.totalPages = totalPage;
  }

  return options;
}

window.ajaxMainContentPage = function(paginationEl, url, params, loadToEl, opts) {

  var options = initPageOption(paginationEl, opts);

  options.onPageClicked = function(event, originalEvent, type, page) { // 异步局部加载
    params = params || {};
    if (typeof params == 'function') {
      params = params();
    }

    params.pageNumber = page;
    load(url, params, loadToEl, null);
  }

  // 加载分页控件
  if (options.totalPages > 0) {
    $(paginationEl).bootstrapPaginator(options);
  }
}
