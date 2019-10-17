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
		if (!options.showSkipPage) {
			return false;
		}

		var currentPage = paginationEl.attr("currentPage");
		var totalPage = paginationEl.attr("totalPage");
		var skipPage = [];
		skipPage.push('<span style="margin-right: 0px;width: 240px;" class="col-sm-5">');
		skipPage.push(' 总共  ' + totalPage + ' 页，当前第');
		skipPage
				.push('<input type="text" value="'
						+ currentPage
						+ '" class="form-control" style="width: 40px; margin-left: 5px; margin-right: 5px;padding-left: 1px; padding-right: 1px; display: inline;">');
		skipPage.push("页  </span>");
		$(skipPage.join("")).insertBefore($("li:first", $(paginationEl)));
		$("input", $(paginationEl)).keypress(function(event) {
			var keyCode = event.keyCode;
			if (keyCode == 13) {
				var page = $("input", $(paginationEl)).val();
				if (isNaN(page) || parseInt(page) != page) {
					alert("请输入正确的数值页码！")
					return false;
				}

				if (!page || parseInt(page) <= 0) {
					page = 1;
				} else if (parseInt(page) > parseInt(totalPage)) {
					page = totalPage;
				}
				$("input", $(paginationEl)).val(page);
				// 回车
				params = params || {};
				if (typeof params == 'function') {
					params = params();
				}
				params.pageNumber = page;
				load(url, params, loadToEl, null);
			}
		});
	}
}
