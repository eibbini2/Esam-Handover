package abs.card.plugins;

import com.sap.scco.ap.plugin.BasePlugin;
import com.sap.scco.ap.plugin.annotation.ListenToExit;
import com.sap.scco.ap.plugin.annotation.ui.JSInject;
import com.sap.scco.ap.pos.entity.AdditionalFieldEntity;
import com.sap.scco.env.UIEventDispatcher;
import com.sap.scco.util.exception.WrongUsageException;
import com.sap.scco.util.logging.Logger;
import generated.GenericValues;
import generated.PostInvoiceType;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.io.IOUtils;

public class App extends BasePlugin {
  AdditionalFieldEntity addFldItem1;
  
  AdditionalFieldEntity addFldItem2;
  
  private static final Logger logger = Logger.getLogger(App.class);
  
  public App() {
    this.addFldItem1 = new AdditionalFieldEntity();
    this.addFldItem2 = new AdditionalFieldEntity();
    this.addFldItem1.setFieldName("MachineId");
    this.addFldItem2.setFieldName("CardNumber");
  }
  
  public void startup() {
    super.startup();
  }
  
  public String getId() {
    return "Card Number & Machine Id Plugin";
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
  
  public static void showMessageToUi(String msg, String type) {
    Map<String, String> dialogOptions = new HashMap<>();
    dialogOptions.put("message", msg);
    dialogOptions.put("id", "AddetionalCardFields");
    dialogOptions.put("type", type);
    dialogOptions.put("maxLifeTime", "30");
    UIEventDispatcher.INSTANCE.dispatchAction("SHOW_MESSAGE_DIALOG", null, dialogOptions);
  }
  
  @JSInject(targetScreen = "sales")
  public InputStream[] injectJS() {
    return new InputStream[] { getClass().getResourceAsStream("/resources/salesInject.js") };
  }
  
  @ListenToExit(exitName = "BusinessOneServiceWrapper.beforePostInvoiceRequest")
  public void enlargeB1iMessage(Object caller, Object[] args) {
    PostInvoiceType request = (PostInvoiceType)args[1];
    logger.info("************************: beforePostInvoiceRequest " + ((PostInvoiceType.Sale.DocumentLines.Row)request.getSale().getDocumentLines().getRow().get(0)).getFreeText().toString() + "this.addFldItem1.getValue(): " + this.addFldItem1.getValue());
    String ExtraInfo = ((PostInvoiceType.Sale.DocumentLines.Row)request.getSale().getDocumentLines().getRow().get(0)).getFreeText().toString();
    ExtraInfo = ExtraInfo.substring(5);
    String[] extraInfoObject = ExtraInfo.split("\\|");
    logger.info("************************: beforePostInvoiceRequest " + extraInfoObject.toString() + "this.addFldItem1.getValue(): " + this.addFldItem1.getValue());
    GenericValues.KeyValPair keyValPair = new GenericValues.KeyValPair();
    GenericValues.KeyValPair keyValPair1 = new GenericValues.KeyValPair();
    keyValPair.setKey("U_MachineId");
    keyValPair1.setKey("CreditCardNumber");
    keyValPair.setValue(extraInfoObject[1]);
    keyValPair1.setValue(extraInfoObject[0]);
    if (extraInfoObject.length > 1) {
      request.getSale().getDocuments().setGenericValues(new GenericValues());
      request.getSale().getPaymentList().setGenericValues(new GenericValues());
      request.getSale().getPaymentList().getGenericValues().getKeyValPair().add(keyValPair);
      request.getSale().getPaymentList().getGenericValues().getKeyValPair().add(keyValPair1);
      ((PostInvoiceType.Sale.PaymentList.Details)request.getSale().getPaymentList().getDetails().get(0)).getPaymentsCreditCards().setGenericValues(new GenericValues());
      ((PostInvoiceType.Sale.PaymentList.Details)request.getSale().getPaymentList().getDetails().get(0)).getPaymentsCreditCards().getGenericValues().getKeyValPair().add(keyValPair);
      ((PostInvoiceType.Sale.PaymentList.Details)request.getSale().getPaymentList().getDetails().get(0)).getPaymentsCreditCards().getGenericValues().getKeyValPair().add(keyValPair1);
      request.getSale().getDocuments().getGenericValues().getKeyValPair().add(keyValPair);
      request.getSale().getDocuments().getGenericValues().getKeyValPair().add(keyValPair1);
    } 
    logger.info("************************: beforePostInvoiceRequest1 " + request.getSale().getDocuments().getGenericValues());
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
