$(function() {
	$( document ).ready(function() {



		$("<style>")
		.prop("type", "text/css")
		.html("\
		.loader {\
		color: #ffffff;\
		font-size: 20px;\
		margin: 100px auto;\
		width: 1em;\
		height: 1em;\
		border-radius: 50%;\
		position: relative;\
		text-indent: -9999em;\
		-webkit-animation: load4 1.3s infinite linear;\
		animation: load4 1.3s infinite linear;\
		-webkit-transform: translateZ(0);\
		-ms-transform: translateZ(0);\
		transform: translateZ(0);\
		}\
		@-webkit-keyframes load4 {\
		0%,\
		100% {\
		box-shadow: 0 -3em 0 0.2em, 2em -2em 0 0em, 3em 0 0 -1em, 2em 2em 0 -1em, 0 3em 0 -1em, -2em 2em 0 -1em, -3em 0 0 -1em, -2em -2em 0 0;\
		}\
		12.5% {\
		box-shadow: 0 -3em 0 0, 2em -2em 0 0.2em, 3em 0 0 0, 2em 2em 0 -1em, 0 3em 0 -1em, -2em 2em 0 -1em, -3em 0 0 -1em, -2em -2em 0 -1em;\
		}\
		25% {\
		box-shadow: 0 -3em 0 -0.5em, 2em -2em 0 0, 3em 0 0 0.2em, 2em 2em 0 0, 0 3em 0 -1em, -2em 2em 0 -1em, -3em 0 0 -1em, -2em -2em 0 -1em;\
		}\
		37.5% {\
		box-shadow: 0 -3em 0 -1em, 2em -2em 0 -1em, 3em 0em 0 0, 2em 2em 0 0.2em, 0 3em 0 0em, -2em 2em 0 -1em, -3em 0em 0 -1em, -2em -2em 0 -1em;\
		}\
		50% {\
		box-shadow: 0 -3em 0 -1em, 2em -2em 0 -1em, 3em 0 0 -1em, 2em 2em 0 0em, 0 3em 0 0.2em, -2em 2em 0 0, -3em 0em 0 -1em, -2em -2em 0 -1em;\
		}\
		62.5% {\
		box-shadow: 0 -3em 0 -1em, 2em -2em 0 -1em, 3em 0 0 -1em, 2em 2em 0 -1em, 0 3em 0 0, -2em 2em 0 0.2em, -3em 0 0 0, -2em -2em 0 -1em;\
		}\
		75% {\
		box-shadow: 0em -3em 0 -1em, 2em -2em 0 -1em, 3em 0em 0 -1em, 2em 2em 0 -1em, 0 3em 0 -1em, -2em 2em 0 0, -3em 0em 0 0.2em, -2em -2em 0 0;\
		}\
		87.5% {\
		box-shadow: 0em -3em 0 0, 2em -2em 0 -1em, 3em 0 0 -1em, 2em 2em 0 -1em, 0 3em 0 -1em, -2em 2em 0 0, -3em 0em 0 0, -2em -2em 0 0.2em;\
		}\
		}\
		@keyframes load4 {\
		0%,\
		100% {\
		box-shadow: 0 -3em 0 0.2em, 2em -2em 0 0em, 3em 0 0 -1em, 2em 2em 0 -1em, 0 3em 0 -1em, -2em 2em 0 -1em, -3em 0 0 -1em, -2em -2em 0 0;\
		}\
		12.5% {\
		box-shadow: 0 -3em 0 0, 2em -2em 0 0.2em, 3em 0 0 0, 2em 2em 0 -1em, 0 3em 0 -1em, -2em 2em 0 -1em, -3em 0 0 -1em, -2em -2em 0 -1em;\
		}\
		25% {\
		box-shadow: 0 -3em 0 -0.5em, 2em -2em 0 0, 3em 0 0 0.2em, 2em 2em 0 0, 0 3em 0 -1em, -2em 2em 0 -1em, -3em 0 0 -1em, -2em -2em 0 -1em;\
		}\
		37.5% {\
		box-shadow: 0 -3em 0 -1em, 2em -2em 0 -1em, 3em 0em 0 0, 2em 2em 0 0.2em, 0 3em 0 0em, -2em 2em 0 -1em, -3em 0em 0 -1em, -2em -2em 0 -1em;\
		}\
		50% {\
		box-shadow: 0 -3em 0 -1em, 2em -2em 0 -1em, 3em 0 0 -1em, 2em 2em 0 0em, 0 3em 0 0.2em, -2em 2em 0 0, -3em 0em 0 -1em, -2em -2em 0 -1em;\
		}\
		62.5% {\
		box-shadow: 0 -3em 0 -1em, 2em -2em 0 -1em, 3em 0 0 -1em, 2em 2em 0 -1em, 0 3em 0 0, -2em 2em 0 0.2em, -3em 0 0 0, -2em -2em 0 -1em;\
		}\
		75% {\
		box-shadow: 0em -3em 0 -1em, 2em -2em 0 -1em, 3em 0em 0 -1em, 2em 2em 0 -1em, 0 3em 0 -1em, -2em 2em 0 0, -3em 0em 0 0.2em, -2em -2em 0 0;\
		}\
		87.5% {\
		box-shadow: 0em -3em 0 0, 2em -2em 0 -1em, 3em 0 0 -1em, 2em 2em 0 -1em, 0 3em 0 -1em, -2em 2em 0 0, -3em 0em 0 0, -2em -2em 0 0.2em;\
		}\
		}\
		.loader{\
		position: fixed;\
		left: 0px;\
		top: 0px;\
		width: 100%;\
		height: 100%;\
		z-index: 9999;\
		background: url('https://thumbs.gfycat.com/RelievedSilentArcticwolf-small.gif')\
		            50% 50% no-repeat rgb(255,255,255);\
		}	\
		").appendTo("head");

		$('#mainView > div > div.fastSelectionAreaContainer').attr('style',"height:500px !important; width:1600px !important");
		$('#mainView > div > div.fastSelectionAreaContainer > div.fastSelectionAreaMain').attr('style',"height:500px !important");
		$('#mainView > div > div.fastSelectionAreaContainer > div.fastSelectionAreaMain > div').attr('style',"height:500px !important");
		$('#mainView > div > div.fastSelectionAreaContainer > div.fastSelectionAreaMain > div > div.content').attr('style',"height:500px !important");
		$('#mainView > div > div.fastSelectionAreaContainer > div.fastSelectionAreaMain > div > div.content > div.contentGrid').attr('style',"height:500px !important");
		$('#fastSelGridRow_0').attr('style',"height:500px !important");
	setTimeout(function(){
	$('#mainView > div > div.fastSelectionAreaContainer > div.fastSelectionAreaMain > div > div.title').attr('style',"height:500px !important");
	$('#mainView > div > div.fastSelectionAreaContainer > div.fastSelectionAreaMain > div > div.content > div.quickNav').attr('style',"height:500px !important");
	$('#mainView > div > div.fastSelectionAreaContainer > div.fastSelectionAreaMain > div > div.card > div.content > div.quickNav').attr('style',"height:500px !important");
	},300);


		function getCustomer1(customerCode) {
			 $.ajax({
					 url: 'SearchServlet',
					 data: {
							 entity: 'customer',
							 method: 'basic',
							 searchInput: customerCode,
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
					 $('#customerIDInput').val(customer.customerID);
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


		var callCustomerCards = function (params) {
	console.log(params)
		var url = (typeof(params) === 'string' ? params : params.url),
				data = params.data || {},
				type = params.type || params.method || 'GET',
				call;
	if (type === 'GET') {
		myData = $.param({});
	call = $.getJSON(url, myData);
	}
	else {
		call = $.ajax({
			url: url,
			contentType: 'application/json',
			dataType: 'json',
			data: JSON.stringify({"customerCard":data}),
			type: type,
			beforeSend: function() {

			},
		});
	}
	call
	.success(function (res) {
	console.log(res)
	getCustomer1(res.customer)
	 $( ".loader" ).remove();
	})
	.error(function (err) {
		console.log(err);
		$( ".loader" ).remove();
	});
};

$(document).on('keyup', '#customerIDInput', function(e) {
	var customerInput = $('#customerIDInput').val();
	if ( customerInput.length >= 15 ) {
    callCustomerCards({url:'http://localhost:4000/getCustomerCards', type: 'post',  data: customerInput.slice(1,-1)});
	}
});

$('#salesDetails').append('<div class="optionItem"><button id="calculateServiceCharge">Service Charge</button></div>');

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

$('#salesDetailsMain > div > div > div').hide();
	});
});
