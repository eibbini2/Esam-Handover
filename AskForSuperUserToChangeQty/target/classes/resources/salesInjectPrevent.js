$(function() {
	$( document ).ready(function() {
		 $('#dialogPayment').dialog();
		 $('#dialogPayment').dialog('close');
		var sUser;
		var sPassword;
        var allowed = false;
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


	 $(document).on("click",'td[aria-describedby*="reciept_DataGrid_quantity"]', function(e){
			    console.log('check allowed', allowed);
    if (!allowed) {
    askforTheSuperUser();
    } else {
        console.log('should show', $(this).parent());
        e.preventDefault();
        e.stopPropagation();
        return;
       // displayQuantityChangePopup($(this).parent());
    }
});

	 $(document).on("change",'#reciept_DataGrid', function(e){
         console.log('yosef');
        e.preventDefault();
        e.stopPropagation();
        return;
});


disableChangeQty();

function allowChangeQty1() {
    let val = $('input[name="quantity"]').val();
    EDIT_QUANTITY = val == '' ? DEFAULT_QUANTITY : val;
                    var cl = $('#reciept_DataGrid #' + getMainInputRowId() + '_quantity');
                    setTimeout(function() {
                        cl.select();
                    }, 100);
                    _CURRENT_EDITED_CELL = { 'name': name, rowid: getMainInputRowId() };

        $(cl).prop('disabled', false);
        setTimeout(function(){
                    displayQuantityChangePopup(cl);
            },200);

         setTimeout(function(){
             allowed = false;
         },1000);
}

function allowChangeQty() {
    allowed = true;
    console.log('test');
       $(`#${getMainInputRowId()} td:nth-child(5)`).click();
    setTimeout(function(){
  displayQuantityChangePopup( $(`#${getMainInputRowId()} td:nth-child(5)`));
    },200);

    return false;


   // $(this)[0].onclick = null;
 //   $(this).unbind('click');
	var link = document.querySelectorAll('td[aria-describedby*="reciept_DataGrid_quantity"]');

     setTimeout(function(){
  //       $(`#${getMainInputRowId()} td:nth-child(5)`).click();
         allowed = false;

                          },1000);

}

function askforTheSuperUser(){
	$('#superUser').focus();
    askForSuperUser("Change Quantity Requires Authorization",
								function (u,p) {
									var receiptGrid = $('#mainView #reciept_DataGrid');
									var salesItemKey = receiptGrid.jqGrid('getCell', getMainInputRowId(), 'salesItemKey');
                console.log('ffff',u,p);
								 sUser = u;
								 sPassword = p;
								var sIsLowLevel = '';
                                var payload = {receiptKey: $('#receiptEntityKey').val(), superUser: sUser, superPassword: sPassword, lowLevel: sIsLowLevel, salesItemKey: salesItemKey};

								if(isBarcodeLogin('Admin')) {
									var login = processBarcodeLogin('Admin');
									sUser = login[0] || sUser ;
									sPassword = login[1] || sPassword;
								}
		$.ajax({
						    "url": "ReceiptController?action=addOrUpdateSalesItem",
						    "dataType": "json",
						    "contentType": "application/json; charset=utf-8",
						    "type": "POST",
						    "cache": false,
						    "data": JSON.stringify(payload)
						}).success(function(res) {
							if(!sPassword){
								askforTheSuperUser();
								return;
							}
					$.ajax({
						    "url": "BaseSettingsController?action=GetUsers&bpm.pltype=json",
						    "dataType": "json",
						    "contentType": "application/json; charset=utf-8",
						    "type": "POST",
						    "cache": false,
						    "timeout": 120000,
						    "data": JSON.stringify({"userKey": currentUserKey})
						}).success(function(res) {
							let usersAllowedToChangeQty = [];
							res.users.forEach(user=>  user.permissions.find(row=>row.protectedResourceId == "ChangeQtyRetail") != undefined ? usersAllowedToChangeQty.push(user) : '' )
							let currentUser = usersAllowedToChangeQty.find(row=> row.userName == sUser);
							if(!currentUser){
								askforTheSuperUser();
								return;
							}
							$.ajax({
							"url": "BaseSettingsController?action=GetUser&bpm.pltype=json",
							"dataType": "json",
							"contentType": "application/json; charset=utf-8",
							"type": "POST",
							"cache": false,
							"timeout": 120000,
							"data": JSON.stringify({"userKey": currentUser.key})
						}).success(function(res1) {
								var changeQty = res1.user.permissions.find(row=>row.protectedResourceId == "ChangeQtyRetail");
								if(changeQty.allowed) {
								hideMessageDialog('SUPER_USER_DIALOG');
								allowChangeQty1();
                                    return;
								} else {
									disableChangeQty();
									askforTheSuperUser();
								}
								}).error(function(res) {
								 disableChangeQty();
								 askforTheSuperUser();
								 console.log(res.status)
							});
				})

       }).error(function(res) {
    askforTheSuperUser();
});

},
							function () {
                                try{
								// reset everything
								this.onRequestError(xhr, xhr.status, error);
								cb(BasicAjaxJSONCommand.STATE_CANCEL_SU_DIALOG, null,
										{"code":"-1","description": "status: " + status + ", " +
												"error: " + error, "status": status, "error": error});
                                    } catch(e){
    disableChangeQty();
}
								})
}

$.ajax({
    "url": "BaseSettingsController?action=GetUser&bpm.pltype=json",
    "dataType": "json",
    "contentType": "application/json; charset=utf-8",
    "type": "POST",
    "cache": false,
    "timeout": 120000,
     "data": JSON.stringify({"userKey": currentUserKey})
}).success(function(res) {
	res.user.role == 'Cashier' ? $('#configurationDialogBtn').hide() : '';
}).error(function(res) {

 console.log(res.status)
});

function disableChangeQty() {

	 	  $(document).on("click",'td[aria-describedby*="reciept_DataGrid_quantity"]', function(e){
                               var cl = $('#reciept_DataGrid #' + getMainInputRowId() + '_quantity');
         //$(`#${getMainInputRowId()} td:nth-child(2)`).click();
           $(cl).prop('disabled', true);
          hideQuantityChangePopup();
          allowed = false;
	   });
              hideQuantityChangePopup();

}
		//
    //         $('#inputArea .salesItemQuantityChangeContainer .incButton').on('mousedown', function() {
    //     increaseQuantity(getMainInputRowId());
    // });
		//
    // $('#inputArea .salesItemQuantityChangeContainer .decButton').on('mousedown', function() {
    //     decreaseQuantity(getMainInputRowId());
    // });
	});
	});
