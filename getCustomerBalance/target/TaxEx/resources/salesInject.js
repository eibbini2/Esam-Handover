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
      

$('#salesDetails').append('<div class="optionItem"><button id="ChangeTaxS0">TAX Exempt - S0</button></div>');

var sales;
function ChangeTax(taxCode) {
sales = $('#reciept_DataGrid').jqGrid('getGridParam', 'data');
//$( "body" ).append( "<div id='load1' class='loader'></div>" );
sales.forEach((sale, i)=>{ 

if (sale.material && sale.state != "3") {
//console.log(sale.pos, i, taxCode);
changeRow(sale.pos, 900, taxCode)

}
})

}
var counter = 0;
var firstItemNewPrice;
function changeRow(pos, timeout, taxCode) {
setTimeout(function() {
$(`#${pos} td:nth-child(1)`).click();
},pos * timeout * 0.91);
setTimeout(function() {

var precent;
var newPrice;
var unitPrice = $(`#${pos}`).closest('tr').find('td:eq(6)').text();
unitPrice = unitPrice.replace(/,/g, '');

if ($(`#taxTypeCodeSelectBox`).val() == "S16") {
percent = 1.16;
}

if ($(`#taxTypeCodeSelectBox`).val() == "S10") {
percent = 1.10;
}

if ($(`#taxTypeCodeSelectBox`).val() == "S8") {
percent = 1.08;
}

if ($(`#taxTypeCodeSelectBox`).val() == "S4") {
percent = 1.04;
}

if ($(`#taxTypeCodeSelectBox`).val() == "S2") {
percent = 1.02;
}

if ($(`#taxTypeCodeSelectBox`).val() == "S0") {
percent = 1 - 0;
}

if ($(`#taxTypeCodeSelectBox`).val() == "X0") {
percent = 1 - 0;
}

newPrice = unitPrice / percent;
newPrice = newPrice.toFixed(3);

$(`#taxTypeCodeSelectBox
option[value=${taxCode}]`).prop('selected', true);
//$('#dialogMaterialCreateOrChangeParent > div.ui-dialog-buttonpane.ui-widget-content.ui-helper-clearfix > div.ui-dialog-buttonset > button:nth-child(1) > span').click();
$("#dialogMaterialCreateOrChangeParent > div.ui-dialog-buttonpane.ui-widget-content.ui-helper-clearfix > div.ui-dialog-buttonset > button:nth-child(1)").click();


counter = counter + 1;
if (counter == 1) {
firstItemNewPrice = newPrice;
 setTimeout(function(){changePrice(pos, timeout, firstItemNewPrice)},(pos* timeout) + 152)
}
 changePrice(pos, timeout, newPrice);
},(pos* timeout) + 150)
}

function changePrice(pos, timeout, newPrice) {
setTimeout(function() { 
$(`#${pos}`).closest('tr').find('td:eq(6)').click();
console.log(pos,timeout, newPrice);
console.log('aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa');
$(`#${pos}`).closest('tr').find('td:eq(6) input').val(newPrice);
},(pos* timeout) + 150)
setTimeout(function(){
$(`#${pos}`).closest('tr').find('td:eq(6) input').val(newPrice);
$(`#${pos} td:nth-child(2)`).click();
setTimeout(function(){
$('#superUser').val('Admin');
$('#superPassword').val('abs@1234');
$('.button.first').click();},50);

if (sales.length-1 == pos){
 $( ".loader" ).remove();
}

},(pos* timeout) + 350)
}

$(document).on('click', '#ChangeTaxS0', function(){ChangeTax('S0')});
	});
	});
