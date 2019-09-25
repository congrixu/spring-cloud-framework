$(function () {
    'use strict'

    $('[data-toggle="tooltip"]').tooltip();
    
    $(".sidebar-menu").tree();
    $(".sidebar-menu a[url]").click(function(){
    	console.log($(this));
    	var url = $(this).attr("url");
    	load (url, null, $("#main_content"), null);
    });
})
