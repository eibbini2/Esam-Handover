
$( document ).ready(function() {
$('#cashDeskClosingBtn').replaceWith(function () {
    return $('<li id="cashDeskClosingBtn" class="function dataLossCheck"><a>اقفال نهاية اليوم</a></li>', {
        html: $(this).html()
    });
});


$('#cashDeskClosingBtn').on('click', document, function() {
     getParkedReceipts()
        .then(async parkedReceipts => {
            if (parkedReceipts.length) {
                if (confirm('يوجد فواتير معلقة , لا يمكنك تطبيق اقفال نهاية اليوم. هل ترغب بإلغاء جميع الفواتير تلقائيا؟')) {
			for (let i = 0; i < parkedReceipts.length; i++) {
                    await cancelParkedReceipt(parkedReceipts[i].receiptKey)
                    }
        	return prepareDrawerDetails(true, "cashdeskclosing.jsp");
                } else {
                    return;
                }
            } else {
                       	return prepareDrawerDetails(true, "cashdeskclosing.jsp");

            }
        })


});


function getParkedReceipts() {
    return new Promise((resolve, reject) => {
        let basicOptions = {
            url: 'ReceiptController?action=getParkedReceipts',
            dataType: 'json',
            contentType: 'application/json; charset=utf-8',
            type: 'POST',
            cache: false,
            timeout: 120000
        };


        $.ajax(basicOptions).done((data) => {
            if (data.status === 'success') {
                let payload = data.payload;
                resolve(payload);
            }
        }).fail(() => {
            reject(data.description);
        });
    })
}

function cancelParkedReceipt(receiptKey) {
    return new Promise((resolve, reject) => {

        var cancelReceiptAction = new BasicReceiptActionCommand('voidReceipt');

        cancelReceiptAction.execute({
            'receiptKey': receiptKey
        }, function(success) {

            if (success < 0) {
                reject(receiptKey);
            } else {

                clearFormData(false, 'cancel');
                setFocusForPage();
                setNewCCOState(STATE_READY_FOR_RECEIPT);
                prepareForNewReceipt();
                resolve(receiptKey);
            }

            window.setTimeout(function() {
                executeGetParkedReceipts();
            }, 1000);
        });
    });
}
});
