package abs.card.plugins.handler;

import com.sap.scco.ap.pos.util.ui.BroadcasterHolder;
import com.sap.scco.ap.pos.util.ui.UIEventChannelListener;

public class BackendEventHandler {

    // use the logger from cco

    private static BackendEventHandler instance;

    public static synchronized BackendEventHandler getInstance() {
        if (instance == null) {
            instance = new BackendEventHandler();
        }

        return instance;
    }

    private BackendEventHandler() {

    }

    public void registerEventHandler() {
    	return;
        //BroadcasterHolder.INSTANCE.addEventChannelListener(null {
//            @Override
//            public void handleEvent(String eventId, JSONObject payload) {
//                String customerId = "";
//                switch (eventId) {
//                    case "GET_ADDITIONAL_INFO_FOR_CUSTOMER":
//                        customerId = payload.getString("customerId");
//                   //     AdditionalInfoDto additionalInfoDto = AdditionalInfoDao.getInstance().findOne(customerId);
//                        JSONObject newPayload = new JSONObject();
//                        if (additionalInfoDto.getCustomerId() != null) {
//                            newPayload.put("customerId", additionalInfoDto.getCustomerId());
//                            newPayload.put("addFld1", additionalInfoDto.getAddFld1());
//                            newPayload.put("addFld2", additionalInfoDto.getAddFld2());
//                            newPayload.put("addFld3", additionalInfoDto.getAddFld3());
//                            newPayload.put("addFld4", additionalInfoDto.getAddFld4());
//                        }
//                        BroadcasterHolder.INSTANCE.getBroadcaster().broadcastPluginEventForPath("SHOW_ADD_FORM", newPayload);
//                        break;
//                    case "SAVE_ADDITIONAL_CUSTOMER_DATA":
//                        customerId = payload.getString("customerId");
//                        String addFld1 = payload.getString("addFld1");
//                        int addFld2 = payload.getInt("addFld2");
//                        Double addFld3 = payload.getDouble("addFld3");
//                        String addFld4 = payload.getString("addFld4");
//                        logger.info("Got data from ui");
//
//                        AdditionalInfoDto newDto = new AdditionalInfoDto(customerId, addFld1, addFld2, BigDecimal.valueOf(addFld3), addFld4);
//                        AdditionalInfoDao.getInstance().save(newDto);
//                        break;
//                }
            }
 //       });
    }

