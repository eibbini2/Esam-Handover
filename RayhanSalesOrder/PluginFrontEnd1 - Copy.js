	var salesOrders;
	var salesOrdersArray = [];
	var salesOrdersArray1 = [];
	
$(function() {
	$( document ).ready(function() {
		
	
	var newSalesOrder;
	var prevSalesOrder;
	var ordersHTML;
	var currentReceiptID = $('#receiptIdItem').text();

	
setInterval(function(){salesOrders = searchSalesOrder({"entity":"invoice","method":"basic","businessObjectType":"SALESORDER","numberOfBusinessObjectsToFetch":10,"rows":10})},90000);


function searchSalesOrder(searchParameter){
	currentReceiptID = $('#receiptIdItem').text();
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
			result = data;
			if(!prevSalesOrder) {
				prevSalesOrder = data.rows[0].salesBusinessObjectId;
			}
			
			//console.log('preLoadSalesOrder',preLoadSalesOrder);
			salesOrdersArray = salesOrdersArray.filter((v, i, a) => a.indexOf(v) === i);
			ordersHTML = '';
			data.rows.forEach(row=>{
			preLoadSalesOrder({"id": row.salesBusinessObjectId ,"typeSel":"SALESORDER","series":"265","action":"fetchInvoice","numberOfBusinessObjectsToFetch":10, "customerName":data.rows[0].customerName});		
			})
			newSalesOrder = data.rows[0].salesBusinessObjectId;
			
			if(prevSalesOrder != newSalesOrder) {
			// loadSalesOrder({"id": newSalesOrder ,"typeSel":"SALESORDER","series":"265","action":"fetchInvoice","numberOfBusinessObjectsToFetch":10});	
			prevSalesOrder = newSalesOrder;
			}
			
			if (salesOrdersArray1.length) {
				$('<div />').html(ordersHTML).dialog();
			}
			
		},
		error: function(xhr, status, error){
			result.description = error;
		}
	});
	
	return result;
}

function preLoadSalesOrder(searchParameter){
	var result = {
					status:"error"
				 };
	$.ajax({
		url: 'InvoiceBackendFetchServlet',
		data: searchParameter,
		type: 'GET',
		dataType: 'json',
		async: false,
		success: function(data){
		if(data["receipt"] != null){
			dynamicUpdateReceipt({},'',false);
			var id = searchParameter.id;
			salesOrdersArray.push({[id]: searchParameter})
			salesOrdersArray1.push({[id]: data["receipt"]})
			ordersHTML += `<a href="#" onclick="loadClickedOrder(${searchParameter.id})">Doc Num: ${searchParameter.id} , Customer Name: ${searchParameter.customerName}</a><br/>`
		}							
		},
		error: function(xhr, status, error){
			hideMessageDialog("INVOICE_LOAD");
			//Fallback to default handling
			material_create_changeDialogOKButtonProxied();
		}
	});
	
	return result;
}


function loadSalesOrder(searchParameter){
	var result = {
					status:"error"
				 };
	$.ajax({
		url: 'InvoiceBackendFetchServlet',
		data: searchParameter,
		type: 'GET',
		dataType: 'json',
		async: false,
		success: function(data){
		result = data;								
		loadThisOrder(data, searchParameter)			
		},
		error: function(xhr, status, error){
			hideMessageDialog("INVOICE_LOAD");
			//Fallback to default handling
			material_create_changeDialogOKButtonProxied();
		}
	});
	
	return result;
}


function loadSalesOrderandPark(searchParameter){
	var result = {
					status:"error"
				 };
	$.ajax({
		url: 'InvoiceBackendFetchServlet',
		data: searchParameter,
		type: 'GET',
		dataType: 'json',
		async: false,
		success: function(data){
			result = data;
						
			showMessageDialog(INVOICE_LOADING, {"type": "loading", "dialogId": "INVOICE_LOAD"});
			dynamicUpdateReceipt(data["receipt"],'',false);
			hideMessageDialog("INVOICE_LOAD");	
  
			
		},
		error: function(xhr, status, error){
			hideMessageDialog("INVOICE_LOAD");
			//Fallback to default handling
			material_create_changeDialogOKButtonProxied();
		}
	});
	
	return result;
}

//loadSalesOrder({"id":salesBusinessObjectId ,"typeSel":"SALESORDER","series":"265","action":"fetchInvoice","numberOfBusinessObjectsToFetch":10})

//receiptDataGridIsEmpty()  true/false

function parkReceipt(){
   
   var param = {
        'entity': 'parkReceipt',
        'receiptKey': $('#receiptEntityKey').val()
    };
	
  $.ajax({
        url: 'ReceiptManagerServlet',
        data: param,
        type: 'POST',
        dataType: 'json',
        async: false,
        success: function(data) {
            $('#mainView #controlArea').find('input, textarea, button, select').removeAttr('disabled');
            if (null != data.cashDrawer) {
                $('#notificationArea').notificationStyle('Info', data.cashDrawer);
            }

            if (data.status == 'error') {
                $('#notificationArea').errorStyle('', data.description);
            } else {
                if (isAutomaticLockOrLogoutEnabled === true) {
                    LOGGED_OUT_ON_SERVER = true;
                }

                if (data.description && '' != data.description) {
                    $('#notificationArea').notificationStyle('', data.description);
                }
                $('#notificationArea').errorStyle('', '');
                //Clear form data for next customer
                window.setTimeout(function() {
                    clearFormData();
                    prepareForNewReceipt();
                }, 500);
                window.setTimeout(function() {
                    setFocusForPage();
                    if (isAutomaticLockOrLogoutEnabled === true && LOGGED_OUT_ON_SERVER) {
                        return;
                    }
                    setNewCCOState(STATE_READY_FOR_RECEIPT);
                }, 510);
                window.setTimeout(function() {
                    executeGetParkedReceipts();
                }, 520);
            }
        },
        error: function(xhr, status, error) {
            $('#mainView #controlArea').find('input, textarea, button, select').removeAttr('disabled');
            $('#notificationArea').errorStyle('', error);
        }
    });
}



function loadThisOrder(data, searchParameter){
					if(data["exception"] != null){
						//if(	$('#receiptIdItem').text() != '-'){
						//	executeResumeReceipt(currentReceiptID);
						//}
				//	var errorMsg = getErrorMessage(data, businessObjectType);
					//showMessageDialog(errorMsg, {"type": "error", "dialogId": "INVOICE_LOAD"});
					return;
				} else if(data["status"] == "error"){
					//showMessageDialog(data["description"], {"type": "error", "dialogId": "INVOICE_LOAD"});
					//Fallback to default handling
						//if(	$('#receiptIdItem').text() != '-'){
						//	executeResumeReceipt(currentReceiptID);
						//}
					//material_create_changeDialogOKButtonProxied();
					return;
				}
				
				if(data["receipt"] != null){
				if (confirm('There is a new sales order. Do you want to open it?')) {
				if (!receiptDataGridIsEmpty() && searchParameter) {
					parkReceipt();
					setTimeout(function(){loadSalesOrderandPark(searchParameter)},3000);						
				} else {
					showMessageDialog(INVOICE_LOADING, {"type": "loading", "dialogId": "INVOICE_LOAD"});
					dynamicUpdateReceipt(data["receipt"]);
					hideMessageDialog("INVOICE_LOAD");
				}
				}
				salesOrdersArray = salesOrdersArray.filter(arrayItem => arrayItem !== searchParameter.id);
				}
	}
	

	});	
});

function loadClickedOrder(id){

var searchParameter = Object.values(salesOrdersArray.find(x => Object.keys(x) == id));
var data = Object.values(salesOrdersArray1.find(x => Object.keys(x) == id));

				if (!receiptDataGridIsEmpty() && searchParameter) {
					parkReceipt();
					setTimeout(function(){loadSalesOrderandPark(searchParameter)},3000);						
				} else {
					showMessageDialog(INVOICE_LOADING, {"type": "loading", "dialogId": "INVOICE_LOAD"});
					dynamicUpdateReceipt(data);
					hideMessageDialog("INVOICE_LOAD");
				}

	}	
