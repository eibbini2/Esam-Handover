package abs.card.plugins;

import java.io.BufferedReader;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.sap.scco.ap.plugin.BasePlugin;
import com.sap.scco.ap.plugin.BreakExecutionException;
import com.sap.scco.ap.plugin.annotation.PluginAt;
import com.sap.scco.ap.plugin.annotation.PluginAt.POSITION;
import com.sap.scco.ap.pos.dao.IReceiptManager;
import com.sap.scco.ap.pos.dto.SalesBusinessObjectDTO;
import com.sap.scco.ap.pos.entity.BusinessPartnerEntity;
import com.sap.scco.env.UIEventDispatcher;
import com.sap.scco.util.CLogUtils;
import com.sap.scco.util.exception.WrongUsageException;
import com.sap.scco.util.logging.Logger;
import com.sap.scco.ap.pos.service.PrintReceiptPosService;
import com.sap.scco.ap.pos.service.ReceiptPosService;
import com.sap.scco.ap.pos.util.ui.BroadcasterHolder;
import com.sap.scco.ap.pos.util.ui.UIEventChannelListener;
import com.sap.scco.ap.plugin.annotation.PluginAt;
import com.sap.scco.ap.pos.entity.ReceiptEntity;
import com.sap.scco.ap.pos.i14y.b1.SalesBusinessObjectSearchParameter;
import com.sap.scco.ap.pos.i14y.b1.wrapper.BusinessOneCallService;
import com.sap.scco.ap.plugin.annotation.ListenToExit;
import com.sap.scco.ap.plugin.annotation.ui.JSInject;
import java.io.InputStream;
import com.sap.scco.ap.pos.entity.ReceiptEntity;
import com.sap.scco.ap.pos.entity.AdditionalFieldEntity;
import generated.GenericValues;
import generated.PostInvoiceType;
import com.sap.scco.ap.pos.entity.PaymentItemEntity;
import com.sap.scco.ap.pos.i14y.b1.SalesBusinessObjectSearchParameter;

import abs.card.plugins.handler.BackendEventHandler;



public class App extends BasePlugin {
    AdditionalFieldEntity addFldItem1;
    AdditionalFieldEntity addFldItem2;

	private static final Logger logger = Logger.getLogger(App.class);


    public App() {

    }

	@Override
	public void startup() {
		onReceiptPrinted();
		 super.startup();
	}

	@Override
	public String getId() {
		return "Marouf Sales order Plugin";
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



		@JSInject(targetScreen = "sales")
	    public InputStream[] injectJS() {
	        return new InputStream[] { this.getClass().getResourceAsStream("/resources/nugi.js") };
	    }

	    @JSInject(targetScreen = "NGUI")
	    public InputStream[] jsInject() {
	        return new InputStream[]{this.getClass().getResourceAsStream("/resources/nugi.js")};
	    }


	    public void onReceiptPrinted() {
	        System.out.println("onReceiptPrinted");
			logger.info("here11");
	        	Runnable helloRunnable = new Runnable() {
	        		@Override
					public void run() {
	    	        try {
	    				logger.info("here12");
	    				 	SalesBusinessObjectSearchParameter parameter = new SalesBusinessObjectSearchParameter("", null, "SALESORDER", 1, null);   // params
		    				List<SalesBusinessObjectDTO> listOfOpenSalesOrders = BusinessOneCallService.INSTANCE.fetchSalesBusinessObjectsForBusinessPartner(parameter, (String)null); //call B1IF fetch Data
		    		//		logger.info(listOfOpenSalesOrders.toString());

	        			Map<String, String> oJSON = new HashMap<>();
	        		//	Map<String, String> oJSONEventPayload = new HashMap<>();

	        		//	String[][] array = new String[1][];


	        	        oJSON.put("type", "FETCH_SALES_BUSINESS_OBJECT");
	        	        oJSON.put("eventId", "");
	        	        oJSON.put("eventName", "SALESORDER");
	        	        oJSON.put("numberOfBusinessObjectsToFetch", "10");

	        	        
                   //   BroadcasterHolder.INSTANCE.getBroadcaster().broadcastPluginEventForPath("FETCH_SALES_ORDER", oJSON);
                        BroadcasterHolder.INSTANCE.getBroadcaster().broadcastPluginEventForPath("NEW_SALES_ORDER", listOfOpenSalesOrders);
                        
                        

	    	        }
	    	        catch(Exception e) {	        	        
	    				logger.info("error: " + e);

	    	        }
	        	    }
	        	};

	        	ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
	        	executor.scheduleAtFixedRate(helloRunnable, 0, 90, TimeUnit.SECONDS);   // Timer to send sales orders array every 90 seconds

	    }

}