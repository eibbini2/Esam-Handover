Plugin.MyPlugin = class MyPlugin {
  constructor(pluginService, eventBus, c) {
	  console.log(pluginService, eventBus)
    this.pluginService = pluginService;
    this.eventBus = eventBus;
    this.c = c;
    this.init();
  }

  init() {
	 console.log(this);
    this.eventBus.subscribe({
      'handleEvent': (event) => { 
                this.receiptStore = this.pluginService.getContextInstance('ReceiptStore');
                this.userStore = this.pluginService.getContextInstance('UserStore');
                this.translationStore = this.pluginService.getContextInstance('TranslationStore');
                this.user = this.userStore.getUser();
                
		  if(event.type == "NEW_SALES_ORDER"){
			                        if (this.receiptStore.receiptModel === null || (typeof this.receiptStore.receiptModel != "undefined" && this.receiptStore.receiptModel.status == 2) || (typeof this.receiptStore.receiptModel != "undefined" && !this.receiptStore.receiptModel.salesItems.length) && this.user.id !== null && !this.active) {

                            
if (confirm("There is a new Sales Order! Do you want to open it?") == true) {
	this.newPayload = [];
	event.payload.forEach(salesOrder => {
	salesOrder.businessTransactionDate = salesOrder.docDate.time;
	salesOrder.id = salesOrder.salesBusinessObjectId;
	this.newPayload.push(salesOrder);
	});
	this.eventBus.subscribers[9].functionByIdStore[1530].salesBusinessObjectService.eventBus.subscribers[0].functionSelectionService.functionStore.FETCH_SALES_ORDER.salesBusinessObjectService.openSalesBusinessObjectTablePopup(this.newPayload ,'SALESORDER');
	//this.eventBus.subscribers[9].functionByIdStore[1530].salesBusinessObjectService.eventBus.subscribers[0].functionSelectionService.functionStore.FETCH_SALES_ORDER.salesBusinessObjectService.openFetchSalesBusinessObjectPopup('SALESORDER');
     //    this.eventBus.subscribers[9].functionByIdStore[1530].salesBusinessObjectService.eventBus.subscribers[0].functionSelectionService.functionStore.FETCH_SALES_ORDER.salesBusinessObjectService.openFetchSalesBusinessObjectPopup()
     this.active = false;

	} else {
	this.active = false;
	}
		}
		  }
		  
		  }
     });
	 }

 };
return Plugin;

