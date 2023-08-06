package abs.cco.plugins.helpers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sap.scco.util.logging.Logger;

import abs.cco.plugins.Tables.PromotionDayDAO;
import abs.cco.plugins.Tables.PromotionElementDAO;
import abs.cco.plugins.Tables.PromotionSchemeDAO;
import abs.cco.plugins.models.PromotionDaysModel;
import abs.cco.plugins.models.PromotionElementModel;
import abs.cco.plugins.models.PromotionSchemeModel;

public final class SyncHelper {
	private static final Logger logger = Logger.getLogger(SyncHelper.class);
	
	public static void CreatePromotionsTablesOnCcoDatabase() {
		PromotionSchemeDAO.getInstance().setupTable();
		PromotionElementDAO.getInstance().setupTable();
		PromotionDayDAO.getInstance().setupTable();
	}
	private static String convertStreamToString(InputStream is) {

		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();

		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();

	}
	public static void LoadPromotionsData(String PromotionPortalUrl, String CCOID) {
		logger.info("LoadPromotionsData => --------------------------------------------------- " + PromotionPortalUrl );
		if (CCOID == "" || PromotionPortalUrl == "") {
			GlobalVariables.ActivePromotions = GenerateActivePromotions();
			return;
		}
		// --------------------------------------------------------------------
		try {
			// Create a neat value object to hold the URL
			URL url = new URL(PromotionPortalUrl + "?cco=" + CCOID);
			// Open a connection(?) on the URL(??) and cast the response(???)
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			// Now it's "open", we can set the request method, headers etc.
			connection.setRequestProperty("accept", "application/json");
			connection.connect();
			if (connection.getResponseCode() == 201 || connection.getResponseCode() == 200) {
				// This line makes the request
				InputStream responseStream = connection.getInputStream();
				String jsonReply = convertStreamToString(responseStream);
				// Manually converting the response body InputStream to APOD using Jackson
				ObjectMapper mapper = new ObjectMapper();
				// load active promotions from portal
				List<PromotionSchemeModel> result = mapper.readValue(jsonReply, new TypeReference<List<PromotionSchemeModel>>() {});
				// delete all promotions from CCO database
				PromotionSchemeDAO.getInstance().DeleteAll();
				PromotionElementDAO.getInstance().DeleteAll();
				PromotionDayDAO.getInstance().DeleteAll();
				// now save the promotions comes from portal to cco database
				for (PromotionSchemeModel p : result) {
					PromotionSchemeDAO.getInstance().save(p);
					for (PromotionElementModel e : p.getPromotionElements()) {
						PromotionElementDAO.getInstance().save(e);
					}
					for (PromotionDaysModel d : p.getPromotionDays()) {
						PromotionDayDAO.getInstance().save(d);
					}
				}
			}
		}
		catch (Exception e) {
			logger.info("Error in loading promotions =>  " + e.getMessage());
		} 
		finally 
		{
			GlobalVariables.ActivePromotions = GenerateActivePromotions();
		}

	}
	
	private static PromotionSchemeModel CreatePromotion(PromotionSchemeModel p, int promotionIdCount)
	{
		PromotionSchemeModel pPromotion = new PromotionSchemeModel();
		pPromotion.setCustomerGroupIDs(p.getCustomerGroupIDs());
		pPromotion.setCustomerIDs(p.getCustomerIDs());
		pPromotion.setPromotionID(promotionIdCount);
		pPromotion.setCustomerLevelTypeId(p.getCustomerLevelTypeId());
		pPromotion.setDescription(p.getDescription());
		pPromotion.setDiscountTypeID(p.getDiscountTypeID());
		pPromotion.setIncentiveID(p.getIncentiveID());
		pPromotion.setPOSLevelTypeId(p.getPOSLevelTypeId());
		pPromotion.setPOSs(p.getPOSs());
		pPromotion.setPromotionAppliedID(p.getPromotionAppliedID());
		pPromotion.setPromotionDays(p.getPromotionDays());
		pPromotion.setSchemeBaseType(p.getSchemeBaseType());
		pPromotion.setStoreID(p.getStoreID());
		pPromotion.setPromotionElements(new ArrayList<PromotionElementModel>());
		return pPromotion;
	}
	
	private static List<PromotionSchemeModel> GenerateActivePromotions()
	{
		logger.info("GenerateActivePromotions =>  " );
		List<PromotionSchemeModel> data = PromotionSchemeDAO.getInstance().findAll();
		for (PromotionSchemeModel p : data) {
			p.setPromotionElements(PromotionElementDAO.getInstance().findAll(p.getPromotionID()));
			p.setPromotionDays(PromotionDayDAO.getInstance().findAll(p.getPromotionID()));
		}
		int promotionIdCount = 1;
		List<PromotionSchemeModel> promotions = new ArrayList<PromotionSchemeModel>();
		for (PromotionSchemeModel p : data)
		{
			List<PromotionElementModel> elements = p.getPromotionElements().stream().filter(x -> x.getQtyPoint() != null || x.getValuePoint() != null).collect(Collectors.toList());
			PromotionElementModel foc = p.getPromotionElements().stream().filter(x -> x.getQtyPoint() == null && x.getValuePoint() == null).findFirst().orElse(null);
			List<PromotionElementModel> focs = new ArrayList<PromotionElementModel>();
			if (foc != null)
			{
				List<String> itemIDs = Arrays.asList(foc.getItemLevelReference().split("[,]"));
				for (String itemID : itemIDs)
				{
					PromotionElementModel pElement = new PromotionElementModel();
					pElement.setIncentiveValue(foc.getIncentiveValue());
					pElement.setItemLevelReference(itemID);
					pElement.setItemLevelTypeId(foc.getItemLevelTypeId());
					pElement.setQtyPoint(foc.getQtyPoint());
					pElement.setValuePoint(foc.getValuePoint());
					pElement.setPromotionID (promotionIdCount);
					pElement.setItemUOMID(foc.getItemUOMID());
					pElement.setPromotionElementID(foc.getPromotionElementID());
					focs.add(pElement);
				}
			}
			
			if (elements.size() == 1)
			{
				PromotionElementModel element = elements.get(0);
				List<String> itemIDs = Arrays.asList(element.getItemLevelReference().split("[,]"));
				for (String itemID : itemIDs)
				{
					PromotionSchemeModel pPromotion = CreatePromotion(p, promotionIdCount);
					PromotionElementModel pElement = new PromotionElementModel();
					pElement.setIncentiveValue(element.getIncentiveValue());
					pElement.setItemLevelReference(itemID);;
					pElement.setItemLevelTypeId(element.getItemLevelTypeId());
					pElement.setQtyPoint(element.getQtyPoint()); 
					pElement.setValuePoint(element.getValuePoint());
					pElement.setPromotionID(promotionIdCount);
					pElement.setItemUOMID(element.getItemUOMID());
					pElement.setPromotionElementID(element.getPromotionElementID());
					pPromotion.getPromotionElements().add(pElement);
					if (focs != null && !focs.isEmpty())
					{
						pPromotion.getPromotionElements().addAll(focs);
					}
					promotions.add(pPromotion);
					promotionIdCount++;
				}
			}
			else
			{
				PromotionSchemeModel pPromotion = CreatePromotion(p, promotionIdCount);
				for (PromotionElementModel element : elements)
				{
					element.setPromotionID(pPromotion.getPromotionID());
					pPromotion.getPromotionElements().add(element);
				}
				if (focs != null && !focs.isEmpty())
				{
					focs.forEach(x -> x.setPromotionID(pPromotion.getPromotionID()));
					pPromotion.getPromotionElements().addAll(focs);
				}
				promotions.add(pPromotion);
				promotionIdCount++;
			}
		}
		return promotions;
	}
}
