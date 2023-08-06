$( document ).ready(function() {
	var okIsVisible;
	var cashoutIsVisible;
	var okButtonClicked;
//			var url = $(location).attr("href").split('/').pop();
//			if (url == 'cashdeskclosing.jsp') {
//				$("#multicurrency_tabs").remove();
//				$("#multicurrency_tabs-main").remove();
//			}
//
//	        function hideColumn(headertext) {
//	            var t = $("th:contains(" + headertext + ")").index() + 1;
//	            $('td:nth-child('+t+'),th:nth-child('+t+')').hide();
//	        }
//
//	        var okIsVisible = $('#cashdeskclosingokBtn').is(":visible");
//
//	        if (okIsVisible) {
//		        $('#cashdeskclosingokBtn').click();
//		        setTimeout( function() {
//		        	  hideColumn('Expected amount');
//	        		  hideColumn('Cash balancing');
//	        		  hideColumn('المبلغ المتوقع');
//	        		  hideColumn('موازنة النقد');
//		        	}, 200);
//	        }
//
	$('#multicurrency_tabs').empty();
	$("#multicurrency_tabs-main").empty();

	function hideColumn(headertext) {
		var t = $("th:contains(" + headertext + ")").index() + 1;
		$('td:nth-child('+t+'),th:nth-child('+t+')').hide();
	}

	function hidejqCol(colName) {
		var grid = $("#cashdeskClosingCashout_DataGrid");
		var colsArray = [];
		colsArray.push(colName);
		grid.jqGrid('hideCol',colsArray);
	}

	function hideColumns() {
		setTimeout( function() {
		//	hideColumn('Expected amount');
		//	hideColumn('Cash balancing');
			hidejqCol("expectedAmount");
			hidejqCol("cashbalancingAmount");
			hideColumn('المبلغ المتوقع');
			hideColumn('موازنة النقد');

			//clickOk();
		 }, 200);
	 // $('#dialogdialogCashDeskClosingCashoutParent')("button:contains('OK')")
	}

	function isVisible(){
	if (!okButtonClicked) {
		$('#cashdeskclosingokBtn').click();
			hideColumns();
			okButtonClicked = true;
	}
	}

	function clickOk() {
		setTimeout( function() {
		$("#dialogdialogCashDeskClosingCashoutParent button span:contains('OK')").click();
			$("#dialogdialogCashDeskClosingCashoutParent button span:contains('موافق')").click();
		}, 100);
	}

//		$('#cashdeskclosingokBtn').bind('isVisible', isVisible);

//		$('#cashdeskclosingokBtn').show('slow', function(){
//		    $(this).trigger('isVisible');
//		});
	$(document).click(function(event){
	setTimeout( function() {
		okIsVisible = $('#cashdeskclosingokBtn').is(":visible");
		cashoutIsVisible = $('#dialogCashDeskClosingCashout').is(":visible");
		if (okIsVisible && !cashoutIsVisible) {
		 return isVisible();
		}
	}, 200);
	console.log(event.target);
	});
});
