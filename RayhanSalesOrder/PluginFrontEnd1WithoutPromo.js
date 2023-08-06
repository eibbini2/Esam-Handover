
$(function() {
	$( document ).ready(function() {

	$('#salesDetails').append('<div class="optionItem"><button id="calculateServiceCharge">Service Charge</button></div>');
		


$(document).on('keyup', '#customerIDInput', function(e) {
 var customerInput = $('#customerIDInput').val();
if ( customerInput.length >= 15 ) {
var aylaCustomerID= customerInput.substr(2, 6); 
aylaCustomerID = "C".toUpperCase() +"u"+aylaCustomerID;
 $('#customerIDInput').val(aylaCustomerID);
 getCustomer1();
}
});

 function getCustomer1() {
        $.ajax({
            url: 'SearchServlet',
            data: {
                entity: 'customer',
                method: 'basic',
                searchInput: $('#customerIDInput').val(),
                _search: false,
                rows: 1,
                page: 1
            },
            type: 'POST',
            dataType: 'JSON',
            success: function(data) {
                if (data && data.rows && data.rows.length > 0) {
                    var customer = data.rows[0];
                    if (customer.statusvalue !== '2') {
                        showCustomerNotActivePopup();
                        return;
                    } else {
                        executeRetrieveCustomer();
                        return;
                    }
                }
                checkLoyaltyScanAccount($('#customerIDInput').val()).catch((err) => {
                    if (data.records === 0) {
                        showError(customerNotActiveTranslations['NO_ID_AVAILABLE']);
                    } else {
                        showError(err);
                    }
                }).then((loyaltyInfo) => {
                    playAttentionBeep();
                    setInBackendCall(false);
                });
            },
            error: function(xhr, status, error) {
                showError(error);
            }
        });
    }
	
	var serviceCharge = 0;
var serviceChargePercent = 0.10;
var serviceChargeExists = false;
function calculateServiceCharge() {
serviceCharge = 0;
sales = $('#reciept_DataGrid').jqGrid('getGridParam', 'data');
console.log(sales);
sales.forEach((sale, i)=>{ 
if (sale.material && sale.state != '3') {
if(sale.material == "SC0000001"){
serviceChargeExists = true;
} else {
serviceChargeExists = false;
}
var taxRate = 0;
if (sale.taxTypeCode == "S16") {
taxRate = 1.16;
}

if (sale.taxTypeCode == "S10") {
taxRate = 1.10;
}

if (sale.taxTypeCode == "S7") {
taxRate = 1.07;
}

if (sale.taxTypeCode == "S8") {
taxRate = 1.08;
}

if (sale.taxTypeCode == "S4") {
taxRate = 1.04;
}

if (sale.taxTypeCode == "S2") {
taxRate = 1.02;
}

if (sale.taxTypeCode == "S0") {
taxRate = 1;
}

if (sale.taxTypeCode == "X0") {
taxRate = 1;
}


if (sale.taxTypeCode == "SN") {
taxRate = 1;
}

console.log(serviceCharge,parseFloat(sale.unitPrice), parseFloat(sale.quantity),  taxRate, serviceChargePercent)
serviceCharge = serviceCharge + (parseFloat(sale.unitPrice) * parseFloat(sale.quantity) /  taxRate) * serviceChargePercent;
}
})
if (serviceChargeExists) {
return;
}
serviceCharge = serviceCharge + serviceCharge * 0.07;
console.log("ServiceCharge",serviceCharge.toFixed(2))
var item = {id:'SC0000001', material: 'SC0000001', unitPriceOrigin: serviceCharge.toFixed(2)  ,grossValue: serviceCharge.toFixed(2), unitPriceChanged: false, manualPriceChange: true};
	queryForSalesItem(item);
setTimeout(function(){
	$(`#${sales.length-1}`).closest('tr').find('td:eq(6) input').val(serviceCharge.toFixed(2));
	$(`#${sales.length-1} td:nth-child(2)`).click();
}, 1000)

}

$(document).on('click', '#calculateServiceCharge', function(){calculateServiceCharge()});

});
});