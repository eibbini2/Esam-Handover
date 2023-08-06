package abs.taxExempt.plugins;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.*;

import com.sap.scco.ap.plugin.BasePlugin;
import com.sap.scco.ap.plugin.annotation.PluginAt;
import com.sap.scco.ap.plugin.annotation.PluginAt.POSITION;
import com.sap.scco.ap.plugin.annotation.ui.JSInject;
import com.sap.scco.ap.pos.dao.IReceiptManager;
import com.sap.scco.ap.pos.entity.SalesItemEntity;
import com.sap.scco.ap.pos.util.ui.BroadcasterHolder;
import com.sap.scco.env.UIEventDispatcher;
import com.sap.scco.util.exception.WrongUsageException;
import com.sap.scco.util.logging.Logger;

import org.apache.commons.io.IOUtils;

public class App extends BasePlugin {

	private static final Logger logger = Logger.getLogger(App.class);
	
	@Override
	public void startup() {

		super.startup();
	}

	@Override
	public String getId() {
		return "Prevent Sales of a Zero Item Price";
	}

	@Override
	public String getName() {
		// return this.getId();
		return "Prevent Sales of a Zero Item Price";
	}

	@Override
	public String getVersion() {

		//return getClass().getPackage().getImplementationVersion();
		return "1.0.0";
	}
	

	@Override
	public boolean persistPropertiesToDB() {
		// TODO Auto-generated method stub
		return true;
	}
	
	@Override
	public Map<String, String> getPluginPropertyConfig() {
		Map<String, String> pr = new HashMap<String, String>();
		return pr;
	}
	
	@JSInject(targetScreen = "sales")
	public InputStream[] injectJS1() {
		return new InputStream[] {this.getClass().getResourceAsStream("/resources/salesInjectPrevent.js")};
	}
	
	public static void showMessageToUi(String msg, String type) {
		Map<String, String> dialogOptions = new HashMap<String, String>();
		dialogOptions.put("message", msg);
		dialogOptions.put("id", App.class.getSimpleName());
		dialogOptions.put("type", type);
		dialogOptions.put("maxLifeTime", "30");
		UIEventDispatcher.INSTANCE.dispatchAction("SHOW_MESSAGE_DIALOG", null, dialogOptions);
	}
	
	@PluginAt(pluginClass = IReceiptManager.class, method = "addSalesItems", where = POSITION.BEFORE)
	public void printItem( Object proxy,  Object[] args, StackTraceElement caller) {
	    SalesItemEntity S = (SalesItemEntity) args[1];
	      logger.info("------SALESITEM------------------------------- : " + S.toString());
	      Object zero = 0;
	      if(S.getUnitNetAmount().toBigInteger().equals(zero)) {
	    	  showMessageToUi("Item with no Price 0","Retail");
	      }
	     
	      BroadcasterHolder.INSTANCE.getBroadcaster().broadcastPluginEventForPath("RECEIPT_REFRESH", null);
	}
	
	
		public void extractResource(String resName, String path, boolean overwrite) throws WrongUsageException {
			  logger.info("------Error------------------------------- : " + path);	
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
