package abs.cco.plugins.helpers;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.sap.scco.ap.pos.entity.ReceiptEntity;
import com.sap.scco.ap.pos.entity.SalesItemEntity;

import abs.cco.plugins.models.*;

public final class SalesItemHelper {

	public static void applyPromotionsOnReceipt(ReceiptEntity receipt) {
		if (GlobalVariables.ActivePromotions == null || GlobalVariables.ActivePromotions.size() == 0) {
			// return;
		}
		List<SalesItemEntity> salesItems = receipt.getSalesItems();
		if (salesItems == null || salesItems.size() == 0) {
			//return;
		}
		String customerCode = receipt.getBupaExternalId();
		String customerGroupCode = "";
		if (receipt.getBusinessPartner() != null) {
			customerCode = receipt.getBusinessPartner().getExternalId();
			customerGroupCode = receipt.getBusinessPartner().getCustomerGroupCode();
		}
		if (customerCode == null)
			customerCode = "";
		if (customerGroupCode == null)
			customerGroupCode = "";
		List<SalesItemEntityModel> targetItems = new ArrayList<SalesItemEntityModel>();
		for (int i = 0; i < salesItems.size(); i++) {
			SalesItemEntity item = (salesItems.get(i));
			if (item.getStatus().equals("1") && (item.getQuantityTypeCode().equals(null) || item.getMaterial().getDefaultQuantityTypeCode().equals(null) || item.getQuantityTypeCode().equals(item.getMaterial().getDefaultQuantityTypeCode()))) {
				SalesItemEntityModel targetItem = new SalesItemEntityModel();
				targetItem.setAddedDateTime(LocalDateTime.now());
				targetItem.setItemNumber(item.getMaterial().getExternalID());
				targetItem.setLineTotal(item.getPaymentNetAmount());
				targetItem.setQuantity(item.getQuantity());
				targetItem.setUnitPrice(item.getUnitNetAmount());
				targetItems.add(targetItem);
			}
		}
		if (targetItems.size() == 0) {
			//return;
		}
		// ------------------------------------------------------------------------------------------------------
		PromotionHelper PromotionHelper = new PromotionHelper(targetItems, customerGroupCode, customerCode);
		List<AppliedPromotionModel> satisfiedPromotions = PromotionHelper.RecalculatePromotion();
		if (satisfiedPromotions.size() == 0) {
			//return;
		}
		// ------------------------------------------------------------------------------------------------------
		for (SalesItemEntity salesItem : receipt.getSalesItems()) {
			//SalesItemEntity matchItem = salesItems.stream().filter(x -> x.getMaterial().getExternalID() == x.getMaterial().getExternalID()).findFirst().orElse(null);
			//if (matchItem != null) {
			salesItem.setDiscountAmount(new BigDecimal(1));
			salesItem.setItemDiscountChanged(true);
			salesItem.setNote("discount amount" + new BigDecimal(1));
			//salesItem.setMarkChanged(true);
			//}
		}
		// ------------------------------------------------------------------------------------------------------
		//GlobalHelper.RecalculateTheReceipt(receipt);
		//System.out.println(receipt);
	}
}
