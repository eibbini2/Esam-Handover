$(function() {
	$( document ).ready(function() {

function addSelectToDiv(){
	if($('#DIALOG_CONFIG').is(':visible') && !$('#UnitID1').length ){
	$('.promptField').attr('list' , 'UnitID1');
 	$('#DIALOG_CONFIG > .inner > .message').after(`<datalist id="UnitID1"></datalist>`);

var receiptAdditionalFieldsGrid = new AdditionalFieldsGrid($('#additionalFieldsContainer'), 'com.sap.scco.ap.pos.entity.ReceiptEntity', { editable: false, 'width': '620' });
    receiptAdditionalFieldsGrid.init();

  // $('#DIALOG_CONFIG input').hide();
   receiptAdditionalFieldsGrid.config.U_Unit.groupValues.forEach(row=>{$("#UnitID1").append(new Option(row.value, row.value))});
   
	}
}

/*
$(document).on('change','#UnitID',function(){
  $('#DIALOG_CONFIG input').val($('#UnitID').val());  
})
*/

setInterval(addSelectToDiv,500)

	});
});