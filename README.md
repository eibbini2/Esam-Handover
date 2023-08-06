# Esam-Handover

# getCustomerBalance
Java Only ( API Call to B1IF to get Customer Balance by CardCode and show it on the CCO UI when set Business Partner )
http://int01-c01.b1pro.com:8080/B1iXcellerator/exec/ipo/vP.001sap0000.in_HCSX/com.sap.b1i.vplatform.runtime/INB_HT_CALL_SYNC_XPT/INB_HT_CALL_SYNC_XPT.ipo/proc/getBalance

# capturingCreditCardDetails
B1IF is already included in the scenario and its simimlar to the extension receiptUDFV2 with small changes

# Rayhan Sales Order and Marouf Sales Order
Similar B1IF scenario injection ( **GetOpenInv4BP** ) and connected to SAP CCO B1IF scnario "sap.POS.**GetOpenInvoiceforBP_PostProcessing**"

# AylaCCOAddunitPlugin
B1IF: 192.168.202.122
CCO config ( Additional field) + CCO Java + Javascript plugin + B1IF scnario after invoice ( sap.POS.POSTInvoice_Documents) -> sap.CCO.ReceiptUDFV2
