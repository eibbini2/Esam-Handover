package abs.cco.plugins;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;

import com.sap.scco.ap.configuration.ConfigurationHelper;
import com.sap.scco.ap.plugin.BasePlugin;
import com.sap.scco.ap.plugin.annotation.ListenToExit;
import com.sap.scco.ap.plugin.annotation.PluginAt;
import com.sap.scco.ap.plugin.annotation.PluginAt.POSITION;
import com.sap.scco.ap.plugin.annotation.Schedulable;
import com.sap.scco.ap.plugin.annotation.ui.CSSInject;
import com.sap.scco.ap.plugin.annotation.ui.JSInject;
import com.sap.scco.ap.pos.dao.AdditionalFieldCheckResult;
import com.sap.scco.ap.pos.dao.BarcodeManager;
import com.sap.scco.ap.pos.dao.CDBSession;
import com.sap.scco.ap.pos.dao.CDBSessionFactory;
import com.sap.scco.ap.pos.dao.IBarcodeManager;
import com.sap.scco.ap.pos.dao.IReceiptManager;
import com.sap.scco.ap.pos.dao.ReceiptManager;
import com.sap.scco.ap.pos.dao.SynchStatusManager;
import com.sap.scco.ap.pos.dto.AdditionalFieldDTO;
import com.sap.scco.ap.pos.dto.ReceiptDTO;
import com.sap.scco.ap.pos.dto.SalesItemDTO;
import com.sap.scco.ap.pos.entity.AdditionalFieldContainer;
import com.sap.scco.ap.pos.entity.AdditionalFieldEntity;
import com.sap.scco.ap.pos.entity.BarcodeEntity;
import com.sap.scco.ap.pos.entity.BusinessPartnerEntity;
import com.sap.scco.ap.pos.entity.MaterialEntity;
import com.sap.scco.ap.pos.entity.ReceiptEntity;
import com.sap.scco.ap.pos.entity.SalesItemEntity;
import com.sap.scco.ap.pos.entity.SalesItemEntity.AdditionalFieldKeys;
import com.sap.scco.ap.pos.entity.SalesItemMetaDataEntity;
import com.sap.scco.ap.pos.entity.UserEntity;
import com.sap.scco.ap.pos.i14y.SalesBusinessObjectByIdFetchMediator;
import com.sap.scco.ap.pos.i14y.b1.SalesBusinessObjectFetchService;
import com.sap.scco.ap.pos.i14y.b1.getinvoicebyid.SalesBusinessObjectToReceiptHandler;
import com.sap.scco.ap.pos.i14y.b1.getinvoicebyid.SalesOrderToSalesItemsHandler;
import com.sap.scco.ap.pos.i14y.b1.wrapper.handlers.SalesBusinessObjectByIdFetchHandler;
import com.sap.scco.ap.pos.i14y.util.context.I14YContext;
import com.sap.scco.ap.pos.job.PluginJob;

import com.sap.scco.ap.pos.service.MaterialPosService;
import com.sap.scco.ap.pos.service.PrintReceiptPosService;
import com.sap.scco.ap.pos.service.ReceiptPosService;
import com.sap.scco.ap.pos.util.TriggerParameter;
import com.sap.scco.cs.utilities.ReceiptHelper;
import com.sap.scco.util.exception.WrongUsageException;
import com.sap.scco.util.logging.Logger;

import abs.cco.plugins.helpers.*;
import abs.cco.plugins.models.SalesItemEntityModel;
import generated.GenericValues;
import generated.PostInvoiceType;

import org.apache.commons.io.IOUtils;

public class App extends BasePlugin {
	AdditionalFieldEntity addFldItem1 = new AdditionalFieldEntity();

	private static final Logger logger = Logger.getLogger(App.class);

	@Override
	public void startup() {

		super.startup();
	}

	@Override
	public String getId() {
		return "Cost Center Plugin";
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
		pr.put("Cost2", "String");
		pr.put("Cost3", "String");
		pr.put("Cost4", "String");
		return pr;
	}
	
	


	//@JSInject(targetScreen = "sales")
	//public InputStream[] injectJS() {
	//	return new InputStream[] {this.getClass().getResourceAsStream("/resources/salesInject.js")};
	//}
	
	@ListenToExit(exitName="genericButtonCallback")
	public void handleDialogInput(Object caller, Object[] args) {
		logger.info("I'm genericButtonCallback  - retail mode --------------------------------------------" + "Essam" + (String) args[2] + args[0] + args[1]);
	//String inputVal= (String) args[2];

	//addFldItem1.setFieldName("UnitNo");
	//addFldItem1.setValue(inputVal.toString());
	//logger.info("	addFldItem - retail mode --------------------------------------------" + "Essam" + inputVal);
	//ReceiptEntity receipt = (ReceiptEntity) args[0];
	//receipt.addAdditionalField(addFldItem);
	}
	

	
	@PluginAt(pluginClass = ReceiptPosService.class, method = "updateSalesItem", where = POSITION.AFTER)
	public void afterupdateSalesItemOtherModes(Object proxy, Object[] args, Object ret, StackTraceElement caller) {
		logger.info("I'm after updateSalesItem  - other modes --------------------------------------------");
		//SalesItemHelper.applyPromotionsOnReceipt((ReceiptEntity) args[0]);
	}
	
	@PluginAt(pluginClass = ReceiptPosService.class, method = "addSalesItem", where = POSITION.AFTER)
	public void afterAddSalesItemOtherModes(Object proxy, Object[] args, Object ret, StackTraceElement caller) {
		logger.info("I'm after addSalesItem  - other modes --------------------------------------------");
		//SalesItemHelper.applyPromotionsOnReceipt((ReceiptEntity) args[0]);
	}
	

	@PluginAt(pluginClass = IReceiptManager.class, method = "addSalesItems", where = POSITION.AFTER)
	public Object afterAddSalesItemRetailModes(Object proxy, Object[] args, Object ret, StackTraceElement caller) {
		if (args.length == 2) {
			logger.info("I'm after addSalesItem - Promotion Plugin - retail mode --------------------------------------------");
			//SalesItemHelper.applyPromotionsOnReceipt((ReceiptEntity) args[0]);
		}
		return ret;
	}
	

	@PluginAt(pluginClass = IReceiptManager.class, method = "addSalesItems", where = POSITION.BEFORE)
	public void beforeAddSalesItemRetailModes(Object proxy, Object[] args, StackTraceElement callStack) {
		if (args.length == 2) {	
		logger.info("I'm before addSalesItem - Promotion Plugin - retail mode --------------------------------------------" + ((SalesItemEntity) args[1]).getMaterial().getUdfString1());
			//SalesItemHelper.applyPromotionsOnReceipt((ReceiptEntity) args[0]);
		}
	}

    @PluginAt(pluginClass = PrintReceiptPosService.class, method = "printReceipt", where = PluginAt.POSITION.AFTER)
    public Object onReceiptPrinted1(Object proxy, Object[] args, Object ret, StackTraceElement caller) {
        ReceiptEntity receipt = (ReceiptEntity) args[0];
        logger.info("I'm after printReceipt - Promotion Plugin - Other mode --------------------------------------------");
        if (null != receipt.getBusinessPartner()) {
          //  AdditionalInfoDto addInfo = AdditionalInfoDao.getInstance().findOne(receipt.getBusinessPartner().getExternalId());
        }
        return ret;
    }

    @PluginAt(pluginClass = IReceiptManager.class, method = "printReceipt", where = PluginAt.POSITION.BEFORE)
    public Object onReceiptPrinted(Object proxy, Object[] args, Object ret, StackTraceElement caller) {
        ReceiptEntity receipt = (ReceiptEntity) args[0];
        logger.info("I'm after printReceipt - Promotion Plugin - retail mode --------------------------------------------");
        if (null != receipt.getBusinessPartner()) {
          //  AdditionalInfoDto addInfo = AdditionalInfoDao.getInstance().findOne(receipt.getBusinessPartner().getExternalId());
        }
        return ret;
    }

	
	
		
	@ListenToExit(exitName="BusinessOneServiceWrapper.beforePostInvoiceRequest")
	public void enlargeB1iMessage(Object caller, Object[] args) {
		
		String CostCenter2 = this.getProperty("Cost2", "");
		String CostCenter3 = this.getProperty("Cost3", "");
		String CostCenter4 = this.getProperty("Cost4", "");
		
		PostInvoiceType request = (PostInvoiceType) args[1];
	

		ReceiptDTO request1 = (ReceiptDTO) args[0];
		List<SalesItemDTO> salesItems = request1.getSalesItems();
		for (int i = 0; i < salesItems.size(); i++) {
			SalesItemDTO item = (salesItems.get(i));
			GenericValues.KeyValPair keyValPair1 = new GenericValues.KeyValPair();
			GenericValues.KeyValPair keyValPair2 = new GenericValues.KeyValPair();
			GenericValues.KeyValPair keyValPair3 = new GenericValues.KeyValPair();
	
			
			keyValPair1.setKey("CostingCode2");
			
			
			keyValPair2.setKey("CostingCode3");

			
			keyValPair3.setKey("CostingCode4");

			

			try {
			keyValPair1.setValue(CostCenter2);
			keyValPair2.setValue(CostCenter3);
			keyValPair3.setValue(CostCenter4);
			logger.info("Tareq: " + i + "  " + keyValPair1.getValue()  + "  " + CostCenter3 + " " + keyValPair3);
			if (null == request.getSale().getDocumentLines().getRow().get(i).getGenericValues()) {
			request.getSale().getDocumentLines().getRow().get(i).setGenericValues(new GenericValues());
			}
			//request.getSale().getDocumentLines().getRow().get(i).setGenericValues(new GenericValues());
			request.getSale().getDocumentLines().getRow().get(i).getGenericValues().getKeyValPair().add(keyValPair1);
			request.getSale().getDocumentLines().getRow().get(i).getGenericValues().getKeyValPair().add(keyValPair2);
			request.getSale().getDocumentLines().getRow().get(i).getGenericValues().getKeyValPair().add(keyValPair3);
			}
			catch(Exception e) {
				logger.info("errooor" + e.toString());
			}
			logger.info("***********TAMRA*************: " + request.getSale().getDocumentLines().toString());
		}
		/*request1.getSalesItems().forEach((item)-> {
		
		
	
		request.getSale().getDocumentLines().getRow().get(0);
		request.getSale().getDocumentLines().getRow().forEach((row)->{
			if (!row.getItemCode().equals(item.getExternalID())) 
				continue;
			row.setGenericValues(new GenericValues());
			row.getGenericValues().getKeyValPair().add(keyValPair1);
			row.getGenericValues().getKeyValPair().add(keyValPair2);
			row.getGenericValues().getKeyValPair().add(keyValPair3);
			row.getGenericValues().getKeyValPair().add(keyValPair4);
			});
		});*/
		//logger.info("****	********************: beforePostInvoiceRequest " + request.getSale().getDocuments().toString());
		//keyValPair.equals(addFldItem1);
		//request.getSale().getDocuments().setGenericValues(new GenericValues());
		logger.info("************************: beforePostInvoiceRequest1 " + request.toString());
	    //request.getSale().getDocuments().getGenericValues().getKeyValPair().add(keyValPair4);
		args[1] = request;
	
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
