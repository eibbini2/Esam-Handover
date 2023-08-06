package abs.cco.plugins;

import abs.cco.plugins.helpers.GlobalHelper;
import com.sap.scco.ap.plugin.BasePlugin;
import com.sap.scco.ap.plugin.annotation.ListenToExit;
import com.sap.scco.ap.plugin.annotation.PluginAt;
import com.sap.scco.ap.plugin.annotation.ui.JSInject;
import com.sap.scco.ap.pos.dao.IReceiptManager;
import com.sap.scco.ap.pos.entity.AdditionalFieldEntity;
import com.sap.scco.ap.pos.entity.ReceiptEntity;
import com.sap.scco.ap.pos.service.ReceiptPosService;
import com.sap.scco.util.exception.WrongUsageException;
import com.sap.scco.util.logging.Logger;
import generated.GenericValues;
import generated.PostInvoiceType;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.commons.io.IOUtils;

public class App extends BasePlugin {
  AdditionalFieldEntity addFldItem1 = new AdditionalFieldEntity();
  
  private static final Logger logger = Logger.getLogger(App.class);
  
  public void startup() {
    super.startup();
  }
  
  public String getId() {
    return "Unit Code Plugin";
  }
  
  public String getName() {
    return getId();
  }
  
  public String getVersion() {
    return getClass().getPackage().getImplementationVersion();
  }
  
  public boolean persistPropertiesToDB() {
    return true;
  }
  
  @JSInject(targetScreen = "sales")
  public InputStream[] injectJS() {
    return new InputStream[] { getClass().getResourceAsStream("/resources/salesInject.js") };
  }
  
  @ListenToExit(exitName = "genericButtonCallback")
  public void handleDialogInput(Object caller, Object[] args) {
    logger.info("I'm genericButtonCallback  - retail mode --------------------------------------------Essam" + (String)args[2] + args[0] + args[1]);
    String inputVal = (String)args[2];
    this.addFldItem1.setFieldName("UnitNo");
    this.addFldItem1.setValue(inputVal.toString());
    logger.info("\taddFldItem - retail mode --------------------------------------------Essam" + inputVal);
  }
  
  @PluginAt(pluginClass = ReceiptPosService.class, method = "setBusinessPartner", where = PluginAt.POSITION.AFTER)
  public Object aftersetBusinessPartnerOtherModes(Object proxy, Object[] args, Object ret, StackTraceElement caller) {
    logger.info("I'm after aftersetBusinessPartnerOtherModes--------------------------------------------");
    ReceiptEntity receipt = (ReceiptEntity)args[0];
    receipt.addAdditionalField(this.addFldItem1);
    receipt.setSoftwareVersion(this.addFldItem1.getValue().toString());
    receipt.setComment(this.addFldItem1.getValue().toString());
    return ret;
  }
  
  @PluginAt(pluginClass = IReceiptManager.class, method = "setBusinessPartner", where = PluginAt.POSITION.AFTER)
  public Object checkForLoyaltyFactor(Object proxy, Object[] args, Object ret, StackTraceElement caller) {
    logger.info("I'm after aftersetBusinessPartnerOtherModes-------------RETAIL-------------------------------");
    if (args[1] != null) {
      GlobalHelper.showfieldToUi("TEST POPUP", "info");
      try {
        Thread.sleep(4000L);
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
      } 
      ReceiptEntity receipt = (ReceiptEntity)args[0];
      logger.info("************************: " + this.addFldItem1);
      if (this.addFldItem1.getValue() != null) {
        receipt.addAdditionalField(this.addFldItem1);
        AdditionalFieldEntity additionalFieldEntity = new AdditionalFieldEntity();
      } 
    } 
    return ret;
  }
  
  @ListenToExit(exitName = "BusinessOneServiceWrapper.beforePostInvoiceRequest")
  public void enlargeB1iMessage(Object caller, Object[] args) {
    PostInvoiceType request = (PostInvoiceType)args[1];
    logger.info("************************: beforePostInvoiceRequest " + request.getSale().getDocuments().toString());
    GenericValues.KeyValPair keyValPair = new GenericValues.KeyValPair();
    keyValPair.setKey("UnitNo");
    keyValPair.setValue(this.addFldItem1.getValue());
    request.getSale().getDocuments().setGenericValues(new GenericValues());
    logger.info("************************: beforePostInvoiceRequest1 " + request.getSale().getDocuments().getGenericValues());
    request.getSale().getDocuments().getGenericValues().getKeyValPair().add(keyValPair);
    args[1] = request;
  }
  
  public void extractResource(String resName, String path, boolean overwrite) throws WrongUsageException {
    InputStream oIn = getClass().getResourceAsStream(resName);
    if (oIn != null) {
      try {
        String outName = resName.substring(resName.lastIndexOf("/"));
        String fileName = String.valueOf(path) + File.separator + outName;
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
