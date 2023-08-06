package abs.changeQty.plugins;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.*;

import com.sap.scco.ap.plugin.BasePlugin;
import com.sap.scco.ap.plugin.annotation.ui.JSInject;
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
		return "Authorization Required to Change Item Quantity";
	}

	@Override
	public String getName() {
		// return this.getId();
		return "Authorization Required to Change Item Quantity";
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
