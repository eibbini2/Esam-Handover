
var database = 'CustomerCheckout';


var checkInconsistentReceipts = function (date) {
    return `Select a."POSKey",
a."CashDeskID",
a."ExternalID",
a."CreationDateTime",
b."ErrorMessage",
a."PayDocType"
from "${database}"."PointOfSaleReceipt" a
Inner Join "${database}"."ErrorLog" b
ON b."ReceiptKey" = a."POSKey" and a."ReleaseStatusCode" = 'INC'`
};

var countInconsistentReceipts = function (date) {
    return `select distinct
Count(b."ErrorMessage") as "NumberofReceipts",b."ErrorMessage"
from "CustomerCheckout"."PointOfSaleReceipt" a
Inner Join "CustomerCheckout"."ErrorLog" b
ON b."ReceiptKey" = a."POSKey" and a."ReleaseStatusCode" = 'INC' and a."PayDocType" = 'rCustomer'
Group By b."ErrorMessage"`
};

var checkLatestReceipt = function () {
    return `Select MAX(a."CreationDateTime") as "CreationDateTime"
from "${database}"."PointOfSaleReceipt" a
--Inner Join "${database}"."ErrorLog" b
--ON b."ReceiptKey" = a."POSKey"`
};

var checkLatestReceiptForAllPos = function () {
    return `Select distinct MAX(a."CreationDateTime") as "CreationDateTime", a."CashDeskID"
from "${database}"."PointOfSaleReceipt" a
Group By "CashDeskID"
Order By MAX(a."CreationDateTime")`
};

module.exports = {checkInconsistentReceipts, checkLatestReceipt, countInconsistentReceipts, checkLatestReceiptForAllPos};
