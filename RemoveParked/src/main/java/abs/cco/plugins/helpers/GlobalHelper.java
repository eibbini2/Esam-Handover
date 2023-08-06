package abs.cco.plugins.helpers;

import java.util.HashMap;
import java.util.Map;

import com.sap.scco.ap.pos.dao.CDBSession;
import com.sap.scco.ap.pos.dao.CDBSessionFactory;
import com.sap.scco.ap.pos.entity.ReceiptEntity;
import com.sap.scco.ap.pos.service.CalculationPosService;
import com.sap.scco.ap.pos.service.ReceiptPosService;
import com.sap.scco.ap.pos.service.ServiceFactory;
import com.sap.scco.ap.pos.util.ui.BroadcasterHolder;
import com.sap.scco.env.UIEventDispatcher;
import com.sap.scco.util.logging.Logger;

import abs.cco.plugins.App;
import net.sf.json.JSONObject;

public final class GlobalHelper {
	private static final Logger logger = Logger.getLogger(App.class);
	
	
	public static boolean isNullOrEmpty(String param) { 
	    return param == null || param.trim().length() == 0;
	}
	
	public static void showMessageToUi(String msg, String type) {
		Map<String, String> dialogOptions = new HashMap<String, String>();
		dialogOptions.put("message", msg);
		dialogOptions.put("id", App.class.getSimpleName());
		dialogOptions.put("type", type);
		dialogOptions.put("maxLifeTime", "30");
		UIEventDispatcher.INSTANCE.dispatchAction("SHOW_MESSAGE_DIALOG", null, dialogOptions);
	}
	
	public static void showfieldToUi(String msg, String type) {
		JSONObject dialogOptions = new JSONObject();
		dialogOptions.put("message", "Please insert some value here:");
		dialogOptions.put("id", "DIALOG_CONFIG");
		dialogOptions.put("type", "info");
		dialogOptions.put("input", "true");

		JSONObject btnOkConf = new JSONObject();
		btnOkConf.put("type", "good");
		btnOkConf.put("id", "DIALOG_CONFIG_BTN_OK");
		btnOkConf.put("text", "OK");
		btnOkConf.put("default", "true");

		JSONObject btnCancelConf = new JSONObject();
		btnCancelConf.put("type", "bad");
		btnCancelConf.put("id", "DIALOG_CONFIG_BTN_CAN");
		btnCancelConf.put("text", "Abort");

		dialogOptions.put("buttons", new JSONObject[] {btnOkConf, btnCancelConf});
		UIEventDispatcher.INSTANCE.dispatchAction("SHOW_MESSAGE_DIALOG", null, dialogOptions);
	}
//https://answers.sap.com/questions/13187468/sap-cco-plugin-cross-out-article-from-sales-screen.html
// Setting the status manually does not suffice.
//You also need to tell CCO to recalculate the receipt (because sums etc. change when you cancel an item) and also to refresh the new state of the receipt in the ui.
	
	
	
	
//	Please check if the receipt posted is exactly what you want, because you did not update the receipt.
//	Maybe the change is only visible in the ui and the receipt in the backend and the receipt shown in the ui are out of sync.
//	When using retail ui the equivalent to the ReceiptPosService is the ReceiptManager. Both should have a updateReceipt method iirc.
//
//	setUnitPriceChanged only needs to be set, if you changed the unit price. But this should do no harm.
	
	public static void RecalculateTheReceipt(ReceiptEntity receipt) {
		CDBSession cdbSession = CDBSessionFactory.instance.createSession();
		// get the calculation service (only available in quickservice ui)
		CalculationPosService calculationPosService = ServiceFactory.INSTANCE.getOrCreateServiceInstance(CalculationPosService.class, cdbSession);
		// get the ReceiptPosService
		ReceiptPosService receiptPosService = ServiceFactory.INSTANCE.getOrCreateServiceInstance(ReceiptPosService.class, cdbSession);
		// after all your manipulation on the receipt, recalculate and refresh it
		logger.info("Recalculation receipt.");
		calculationPosService.recalculateReceipt(receipt);
		logger.info("Updating receipt.");
		receiptPosService.updateReceipt(receipt, true);
		BroadcasterHolder.INSTANCE.getBroadcaster().broadcastPluginEventForPath("RECEIPT_REFRESH", null);
	}
}
