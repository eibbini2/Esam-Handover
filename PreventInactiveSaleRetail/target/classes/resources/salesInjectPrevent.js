$( document ).ready(function() {

		let barcodeText;

		$('#dialogPayment').dialog();
		$('#dialogPayment').dialog('close');

		function checkItem(item) {
			console.log('first',item);
		return new Promise((resolve,reject)=>{
		      var rowData = {
		            'entity': 'material',
		            'material': item
		        };




		        $.ajax({
		            url: 'RetrieveEntitiesServlet',
		            data: rowData,
		            type: 'POST',
		            dataType: 'json',
		            success: function(data) {
	                    console.log(data);
									if (data.isInactive) {
									 showErrorDialog('Inactive Item');
									 playErrorBeep();
									 resolve();
								 }
		                    if (data.status == 'success') {
													if(Number(data.grossprice) == 0) {
														showErrorDialog('Zero Price Item');
														playErrorBeep();
														reject();
													} else {
													reject()
												}
		                        if (data['intercept'] == true) {
		                            return;
		                        }

		                        $('#notificationArea').errorStyle('', '');
		                        resultValue = data.statusflag;
		                        if (resultValue == 'true') {

		                        }
		                    } else if (data.status == 'error') {
	                            resolve(item);
	                           }
		            },
		              error: function(xhr, status, error) {
											console.log(error);
											reject(error);
		                }
		              });
		});
		    }

				function checkBarcode(barcode) {
					return new Promise((resolve,reject)=>{



									$.ajax({
											url: 'SearchServlet',
											data: JSON.stringify({entity: 'material', method: 'advanced', gtin: barcode, method: "advanced"}),
											type: 'POST',
											dataType: 'json',
											success: function(data) {
												if (data.isInactive) {
												 showErrorDialog('Inactive Item');
												 playErrorBeep();
												 reject();
											 }
															if (data.status == 'success') {
																console.log(data);
																resolve(data)
																	if (data['intercept'] == true) {
																			return;
																	}

																	$('#notificationArea').errorStyle('', '');
																	resultValue = data.statusflag;
																	if (resultValue == 'true') {

																	}
															}
											},
												error: function(xhr, status, error) {
														console.log(error);
														reject(error);
												}
												});
					});
				}


				$('#materialSearchInput').on({
		        keyup: function(e) {
		        let	itemInfoText = $('#itemInfoArea').text();
		        	if (e.keyCode !== 13) {
		        		barcodeText = $(this).val();
		        	}
		        	if (e.keyCode === 13) {
		        		$('#itemInfoText').promise().done(function(){
									console.log('barcode', barcodeText, $('#materialSearchInput').val());
										return checkItem(barcodeText || $('#materialSearchInput').val()).then(item=>{
	                                        return barcodeText;

									})
									.then(result=>{
	                                    console.log(result);
										return checkBarcode2(result)
									})
									.then(item=>{
										if(Number(item.rows[0].price) == 0) {
											showErrorDialog('Zero Price Item');
											playErrorBeep();
											return;
										}
									})
									.catch(err=>{
										console.log(err)
									})
				});
	    }
	   }
	  })



				function checkBarcode2(barcode) {
					return new Promise((resolve,reject)=>{


	console.log(barcode);
									$.ajax({
											url: 'SearchServlet',
											data: {
	    "entity": "material",
	    "method": "advanced",
	    "receiptscreenrequest": false,
	    "description": "",
	    "shortText": "",
	    "materialID": "",
	    "foreignName": "",
	    "isActive": true,
	    "isBatchNumber": false,
	    "isSerialNumber": false,
	    "gtin": barcode,
	    "prodCatID": "",
	    "refundAllowed": false,
	    "scaleCode": "",
	    "alternateId": "",
	    "articleType": "",
	    "udfString1": "",
	    "udfString2": "",
	    "udfStringXL1": "",
	    "udfStringXL2": "",
	    "udfStringXL3": "",
	    "udfStringXL4": "",
	    "udfDouble1": "",
	    "udfDouble2": "",
	    "_search": false,
	    "nd": 1680043497862,
	    "rows": 10,
	    "page": "1",
	    "sidx": "",
	    "sord": ""
	},
											type: 'POST',
											dataType: 'json',
											success: function(data) {
												if (Number(data.records) && data.rows[0].isInactive) {
												 showErrorDialog('Inactive Item');
												 playErrorBeep();
												 resolve();
											 }
															if (data.status == 'success') {
																if(!Number(data.records)) {
																	showErrorDialog("No Item found")
																	reject()
																}else {
																resolve(data)
															}
																	if (data['intercept'] == true) {
																			return;
																	}

																	$('#notificationArea').errorStyle('', '');
																	resultValue = data.statusflag;
																	if (resultValue == 'true') {

																	}
															}
											},
												error: function(xhr, status, error) {
														console.log(error);
														reject(error);
												}
												});
					});
				}
});
