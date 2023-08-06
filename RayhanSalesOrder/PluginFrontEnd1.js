$(function() {
	$( document ).ready(function() {


function getSalesOrders(){
	if(!$('#dialogSearchParent').is(":visible")){
	if (navigator.onLine) {
  if(receiptDataGridIsEmpty()){
	//$('#dialogMaterialCreateOrChange').dialog();
	$('#1 td:first-child').click();
	setTimeout(function(){$('#salesBusinessObjectSelectBox').val('SALESORDER');
        	$("#dialogMaterialCreateOrChange .glAlias").hide();
        	$("#dialogMaterialCreateOrChange .taxRate").hide();
        	$("#dialogMaterialCreateOrChange .salesPerson").hide();
        	$("#dialogMaterialCreateOrChange .businessObjectCustomerId").show();
        	$("#dialogMaterialCreateOrChange .businessObjectSalesPerson").show();
        	$("#dialogMaterialCreateOrChange .lastNBusinessObject").show();
        	$("#dialogMaterialCreateOrChange .searchParameterDivider").show();
        	$("#dialogMaterialCreateOrChange .businessObjectId").show();
        	$("#dialogMaterialCreateOrChange .businessObjectId").focus();
        	$("#dialogMaterialCreateOrChange .businessObjectSeries").show();

        	setTextOfCustomerSearchButton();
        	setLabelOfBusinessObjectIdField('SALESORDER');

			$('.srchInvButton').click();

			     setTimeout(function() {
            jQuery('input[name=\'searchInput\']').focus();
        }, 150);
        setTimeout(function() {
            jQuery('input[name=\'searchInput\']').select();
        }, 150);
                         },200);

			//$('#dialogMaterialCreateOrChangeParent > div.ui-dialog-buttonpane.ui-widget-content.ui-helper-clearfix > div.ui-dialog-buttonset > button:nth-child(2) > span').click();
	}
} else {
  console.log('offline');
}
	}
}


setTimeout(setInterval(getSalesOrders,60000),60000);



setInterval(function(){salesOrders = searchSalesOrder({"entity":"invoice","method":"basic","businessObjectType":"SALESORDER","numberOfBusinessObjectsToFetch":10,"rows":10})},60000);


function searchSalesOrder(searchParameter){
	var result = {
					status:"error"
				 };
	$.ajax({
		url: 'SearchServlet',
		data: searchParameter,
		type: 'POST',
		dataType: 'json',
		async: false,
		success: function(data){

		},
		error: function(xhr, status, error){
			result.description = error;
		}
	});

	return result;
}


var tableRow;
var rowObject;
$(document).on("click", " #dialogSearch_dataGrid_invoice > tbody tr", function() {
   tableRow = $(this).index();
   rowObject = $('#dialogSearch_dataGrid_invoice').getRowData(tableRow);

   $("#dialogSearch_dataGrid_invoice > tbody tr").dblclick(function() {
   $('#SalesOrderNotes').remove();
       var SalesOrderComment = rowObject.comment;
$('#salesDetailsMain').before(`<div style="padding: 4px;
text-decoration: underline;
    max-width: 600px;
    float: left;
    width: 600px;
    direction: rtl;" id="SalesOrderNotes">Sales Order Comments: ${SalesOrderComment}<div>`);


	var checkOrder = setInterval(function(){
		if(!isEmptyReceipt()) {
		setTimeout(function(){openCreateOrChangeSalesItemDialog(1);},100);
		setTimeout(function(){$('#articleNoteField').val(SalesOrderComment);},200);
		setTimeout(function(){$('#dialogMaterialCreateOrChangeParent > div.ui-dialog-buttonpane.ui-widget-content.ui-helper-clearfix > div.ui-dialog-buttonset > button:nth-child(1) > span').click();},300)
		clearInterval(checkOrder);	
		}
	},400)
});
});

$(document).on('click', '#use', function(){
    $('#SalesOrderNotes').remove();
  //  var rowKey = $("#dialogSearch #dialogSearch_dataGrid_invoice").jqGrid('getGridParam','selrow') || tableRow;
    console.log('rowKey', tableRow)
  //  var rowObject = $('#dialogSearch_dataGrid_invoice').getRowData(tableRow);
     console.log('rowObject', rowObject);

    var SalesOrderComment = rowObject.comment;
$('#salesDetailsMain').before(`<div style="padding: 4px;
text-decoration: underline;
    max-width: 600px;
    float: left;
    width: 600px;
    direction: rtl;" id="SalesOrderNotes">Sales Order Comments: ${SalesOrderComment}<div>`);

	var checkOrder = setInterval(function(){
		if(!isEmptyReceipt()) {
		setTimeout(function(){openCreateOrChangeSalesItemDialog(1);},100);
		setTimeout(function(){$('#articleNoteField').val(SalesOrderComment);},200);
		setTimeout(function(){$('#dialogMaterialCreateOrChangeParent > div.ui-dialog-buttonpane.ui-widget-content.ui-helper-clearfix > div.ui-dialog-buttonset > button:nth-child(1) > span').click();},300)
		clearInterval(checkOrder);	
		}
	},400)
	


});

	});
});
