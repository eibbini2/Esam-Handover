$(function() {
	$( document ).ready(function() {


function addExtraFields() {
let creditNumber = $('#cardNumberInput').val();
let machineNumber = $('#machineNumberInput').val();
let cardRowID = 1;
$('#ui-id-16').text('Credit Card Extra Info');

setTimeout(function(){
$('#articleNoteField').val(`${creditNumber}|` + `${machineNumber}|` + `${cardRowID}`);
$('#dialogMaterialCreateOrChangeParent > div.ui-dialog-buttonpane.ui-widget-content.ui-helper-clearfix > div.ui-dialog-buttonset > button:nth-child(1)').click();
},400);
setTimeout(function(){
    $('#ui-id-16').text('Create/Change article');
},3000);
}
$(document).on('click','#paymentOptionECBtn', function() {

    setTimeout(function(){ // to make sure that the credit card payment container has been showed and loaded 
        if(!$('#cardNumberInput').length)  // Here wer add Cardnumber input & Card Machine Id input
        $('#paymentInputs1').after(`<div><label for="cardNumberInput" class="left">Card Number <input type="text" maxlength="16" name="cardNumberInput" id="cardNumberInput" class="text ui-widget-content ui-corner-all" autocomplete="off"></label><label for="machineNumberInput" class="left">Machine Number <input type="text" maxlength="15" name="machineNumberInput" id="machineNumberInput" class="text ui-widget-content ui-corner-all" autocomplete="off"></label></div>`);
        $('#cardNumberInput').val('');
        $('#machineNumberInput').val('');
				$('#dialogPaymentParent > div.ui-dialog-buttonpane.ui-widget-content.ui-helper-clearfix > div.ui-dialog-buttonset > button:nth-child(1)').hide();
				$('#cardNumberInput').show();
				$('#machineNumberInput').show();
    },300);

})

$(document).on('click','#paymentOptionCashBtn', function() {

    setTimeout(function(){
        if($('#cardNumberInput').length) {
        $('#cardNumberInput').val('');
        $('#machineNumberInput').val('');
				$('#cardNumberInput').hide();
        $('#machineNumberInput').hide();
			}
    },300);

})
$(document).on('click','#dialogPaymentParent > div.ui-dialog-buttonpane.ui-widget-content.ui-helper-clearfix > div.ui-dialog-buttonset > button:nth-child(2)', function() {

    setTimeout(function(){
			if(!isEmptyReceipt())
      $(`#${1} td:nth-child(1)`).click();  // I chose first sales item , and click on number 1 to show create/dialog
        addExtraFields();
    },50);

})

// $(document).on('click','#dialogPaymentParent > div.ui-dialog-buttonpane.ui-widget-content.ui-helper-clearfix > div.ui-dialog-buttonset > button:nth-child(1)', function() {
//
//     setTimeout(function(){
// 			if(!isEmptyReceipt()) {
//       $(`#${1} td:nth-child(1)`).click();
//         addExtraFields();
// 				if(!$('#dialogPayment').is(":visible"))
// 				$('#okBtn').click();
// 			}
//     },50);
//
// })


	});
});
