package abs.cco.plugins;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.IOUtils;

import com.sap.scco.ap.plugin.BasePlugin;
import com.sap.scco.ap.plugin.annotation.PluginAt;
import com.sap.scco.ap.plugin.annotation.PluginAt.POSITION;
import com.sap.scco.ap.pos.dao.IReceiptManager;
import com.sap.scco.ap.pos.entity.BusinessPartnerEntity;
import com.sap.scco.env.UIEventDispatcher;
import com.sap.scco.util.exception.WrongUsageException;
import com.sap.scco.util.logging.Logger;

import abs.cco.plugins.helpers.GlobalHelper;

public class App extends BasePlugin {
	private static final Logger logger = Logger.getLogger(App.class);

	@Override
	public void startup() {

		super.startup();
	}

	@Override
	public String getId() {
		return "Unit Code Plugin";
	}

	@Override
	public String getName() {
		return this.getId();
	}

	@Override
	public String getVersion() {

		return getClass().getPackage().getImplementationVersion();
	}

	@Override
	public boolean persistPropertiesToDB() {
		// TODO Auto-generated method stub
		return true;
	}

	
	public static void showMessageToUi(String msg, String type) {
		Map<String, String> dialogOptions = new HashMap<String, String>();
		dialogOptions.put("message", msg);
		dialogOptions.put("id", App.class.getSimpleName());
		dialogOptions.put("type", type);
		dialogOptions.put("maxLifeTime", "30");
		UIEventDispatcher.INSTANCE.dispatchAction("SHOW_MESSAGE_DIALOG", null, dialogOptions);
	}
	
	
	@PluginAt(pluginClass = IReceiptManager.class, method = "setBusinessPartner", where = POSITION.AFTER)
	public Object checkForBalance(Object proxy, Object[] args, Object ret, StackTraceElement caller) {
		
		logger.info("I'm after aftersetBusinessPartnerOtherModes-------------RETAIL-------------------------------" + args[1]);
		if (args[1] != null) {
		GlobalHelper.showfieldToUi("TEST POPUP", "info");
		 BusinessPartnerEntity customer = (BusinessPartnerEntity) args[1];
		 String payload =  new JSONObject().put("cardCode", customer.getId()).toString();
		  showMessageToUi("Please wait...", "info");

		  logger.info("------try------------------------------- : " +  customer.getId().toString());	

        try {
      	  URL url = new URL("http://int01-c01.b1pro.com:8080/B1iXcellerator/exec/ipo/vP.001sap0000.in_HCSX/com.sap.b1i.vplatform.runtime/INB_HT_CALL_SYNC_XPT/INB_HT_CALL_SYNC_XPT.ipo/proc/getBalance");
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);
            OutputStream os = conn.getOutputStream();
            os.write(payload.getBytes());
            os.flush();
            BufferedReader br = null;
            if (conn.getResponseCode() == 200) {
          	br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
              StringBuilder errorBuilder = new StringBuilder();
    		  showMessageToUi(br.toString(), "info");
              String error = errorBuilder.toString();
              showMessageToUi(error, "info");
              logger.info("conn.getErrorStream():" + error);
            } 
            conn.disconnect();
           // GlobalHelper.hideMessage("info");
          } catch (Exception ex) {
            showMessageToUi(ex.getMessage(), "info");
          }
        return ret;

		}
		return ret;
	}


	public void extractResource(String resName, String path, boolean overwrite) throws WrongUsageException {
		InputStream oIn = this.getClass().getResourceAsStream(resName);
		if (null != oIn) {
			try {
				String outName = resName.substring(resName.lastIndexOf("/"));
				String fileName = path + File.separator + outName;
				File oFl = new File(fileName);
				if (!oFl.exists() || overwrite) {
					OutputStream oOut = new FileOutputStream(oFl);
					IOUtils.copy(oIn, oOut);
					oOut.close();
				}
				oIn.close();
			} catch (IOException oX) {
				throw new WrongUsageException("Error extracting: " + oX.getMessage());
			}
		} else {
			throw new WrongUsageException("Could not find resource '" + resName + "'.");
		}
	}

}
