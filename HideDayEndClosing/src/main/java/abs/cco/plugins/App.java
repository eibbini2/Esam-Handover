package abs.cco.plugins;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.*;

import com.sap.scco.ap.plugin.BasePlugin;
import com.sap.scco.ap.plugin.annotation.ui.JSInject;
import com.sap.scco.util.exception.WrongUsageException;

import org.apache.commons.io.IOUtils;

public class App extends BasePlugin {


	@Override
	public void startup() {

		super.startup();
	}

	@Override
	public String getId() {
		return "Hide & Prevent DayEndClosing Details Plugin New";
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
	
	@Override
	public Map<String, String> getPluginPropertyConfig() {
		Map<String, String> pr = new HashMap<String, String>();
		return pr;
	}
	
	


	@JSInject(targetScreen = "cashdeskclosing")
	public InputStream[] injectJS() {
		return new InputStream[] {this.getClass().getResourceAsStream("/resources/salesInject.js")};
	}
	

	@JSInject(targetScreen = "sales")
	public InputStream[] injectJS1() {
		return new InputStream[] {this.getClass().getResourceAsStream("/resources/salesInject1.js")};
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
