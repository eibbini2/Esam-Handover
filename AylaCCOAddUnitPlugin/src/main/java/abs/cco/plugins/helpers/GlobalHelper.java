package abs.cco.plugins.helpers;

import java.util.HashMap;
import java.util.Map;

import com.sap.scco.env.UIEventDispatcher;

import abs.cco.plugins.App;
import net.sf.json.JSONObject;

public final class GlobalHelper {
		
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

}
