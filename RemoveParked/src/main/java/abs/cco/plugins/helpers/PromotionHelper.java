package abs.cco.plugins.helpers;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import abs.cco.plugins.models.*;

//firstBigDecimal.compareTo(secondBigDecimal) < 0 // "<"
//firstBigDecimal.compareTo(secondBigDecimal) > 0 // ">"    
//firstBigDecimal.compareTo(secondBigDecimal) == 0 // "=="  
//firstBigDecimal.compareTo(secondBigDecimal) >= 0 // ">="   
public class PromotionHelper {
	private AppliedPromotionElementModel focElement;
	private final boolean scannedFocFlag = true; // Defining FOC item ,scanned = 1 , given by customer care = 2
	private List<SalesItemEntityModel> salesItems;
	private List<AppliedPromotionModel> Promotions;
	private String customerGroupCode;
	private String customerCode;
	private boolean promoChanged = false;

	public PromotionHelper(List<SalesItemEntityModel> _salesItems, String _customerGroupCode, String _customerCode) {
		this.Promotions = new ArrayList<AppliedPromotionModel>();
		this.salesItems = _salesItems;
		this.customerGroupCode = _customerGroupCode;
		this.customerCode = _customerCode;
	}

	public final List<AppliedPromotionModel> RecalculatePromotion() {
		ArrayList<SalesItemEntityModel> tmpdetailsItems = new ArrayList<SalesItemEntityModel>();
		this.salesItems.sort(Comparator.comparing(SalesItemEntityModel::getAddedDateTime).reversed());
		this.salesItems.forEach(x -> {
			tmpdetailsItems.add(x);
		});
		this.Promotions.clear();
		this.salesItems.clear();
		int detailsCount = tmpdetailsItems.size();
		
		for (int i = detailsCount - 1; i >= 0; i--) {
			this.salesItems.add(0, tmpdetailsItems.get(i));
			ApplyByLastScannedItem();
		}
		return this.Promotions.stream().filter(x -> x.getDiscountAmount().compareTo(new BigDecimal(0)) > 0).collect(Collectors.toList());
	}

	private void ApplyByLastScannedItem() {
		if (this.salesItems.isEmpty()) {
			return;
		}
		SalesItemEntityModel lastItem = this.salesItems.get(0);
		List<Integer> itemPromotions = GlobalVariables.getPromotionElements().stream().filter(x -> x.getItemNumbers().contains(lastItem.getItemNumber())).map(x -> x.getPromotionID()).collect(Collectors.toList());
		if (itemPromotions.isEmpty()) {
			return;
		}
		if (itemPromotions.size() > 1) // if the item has more than one promotion then we must take the latest one and
										// ignore the older promotions
		{
			int latestPromotionId = Collections.max(itemPromotions);
			itemPromotions.clear();
			itemPromotions.add(latestPromotionId);
		}
		List<PromotionSchemeModel> promotionSchemes = GlobalVariables.ActivePromotions.stream().filter(x -> itemPromotions.contains(x.getPromotionID()) && (x.getCustomerLevelTypeId() == 1 || (x.getCustomerLevelTypeId() == 2 && x.getCustomerIDsList().contains(this.customerCode)) || (x.getCustomerLevelTypeId() == 3 && x.getCustomerGroupIDsList().contains(this.customerGroupCode))))
				.collect(Collectors.toList());
		if (promotionSchemes.isEmpty()) {
			return;
		}
		for (PromotionSchemeModel promotion : promotionSchemes) {
			if (!IsPromotionActive(promotion)) {
				continue;
			}

			ArrayList<PromotionLineItemModel> promotionLIModel = new ArrayList<PromotionLineItemModel>();
			for (SalesItemEntityModel li : this.salesItems) {
				promotionLIModel.add(new PromotionLineItemModel(li));
			}

			ArrayList<AppliedPromotionElementModel> promElements = new ArrayList<AppliedPromotionElementModel>();

			for (PromotionElementModel element : promotion.getPromotionElements()) {
				AppliedPromotionElementModel model = new AppliedPromotionElementModel(element);
				SalesItemEntityModel relatedItemFromReceipt = this.salesItems.stream().filter(x -> x.getItemNumber() == element.getItemLevelReference()).findFirst().orElse(null);

				if (relatedItemFromReceipt != null) {
					model.setPrice(relatedItemFromReceipt.getUnitPrice());
				}
				promElements.add(model);
			}

			List<AppliedPromotionElementModel> focs = promElements.stream().filter(x -> x.getIsFOCRecord()).collect(Collectors.toList());
			if (focs.isEmpty()) {
				this.focElement = null;
			} else if (focs.size() == 1) {
				this.focElement = focs.get(0);
			} else {
				int focsCountInReceiptItems = 0;
				List<String> receiptItemIDs = this.salesItems.stream().map(SalesItemEntityModel::getItemNumber).collect(Collectors.toList());
				for (AppliedPromotionElementModel foc : focs) {
					if (receiptItemIDs.contains(foc.getItemLevelReference())) {
						focsCountInReceiptItems++;
					}
				}
				if (focsCountInReceiptItems <= 1) {
					this.focElement = focs.stream().findFirst().orElse(null);
				} else {
					// get the lowest price of FOCs
					BigDecimal minPriceOfFOCs = Collections.min(focs, Comparator.comparing(s -> s.getPrice())).getPrice();
					this.focElement = focs.stream().filter(x -> minPriceOfFOCs.equals(x.getPrice())).findFirst().orElse(null);
				}
			}
			if (promotion.getSchemeBaseType() == 1) // VALUE:
			{
				promotion.setIsSatisfied(IsValueSatisfied(promotionLIModel, promotion, promElements));
			} else // Quantity:
			{
				promotion.setIsSatisfied(IsQuantitySatisfied(promotionLIModel, promotion, promElements));
			}

			if (promotion.getIsSatisfied()) {
				this.promoChanged = false;
				ApplyPromotion(promotion, promElements, promotionLIModel);
				if (this.promoChanged) {
					UpdateUIPromotionRecord(promotionLIModel, promotion, promElements);
				}
			} else {
				this.Promotions.removeIf(x -> x.getPromotionID() == promotion.getPromotionID());
			}
		}
	}

	private boolean IsPromotionActive(PromotionSchemeModel promotion) {
		boolean active = false;
		int dayofWeek = LocalDateTime.now().getDayOfWeek().getValue();
		PromotionDaysModel promDay = promotion.getPromotionDays().stream().filter(x -> x.getWeekDayID() == dayofWeek).findFirst().orElse(null);
		if (promDay == null) {
			return false;
		}
		LocalTime  start = LocalTime.MIN;
		LocalTime  end = LocalTime.MAX;

		start = LocalTime.parse(promDay.getStartTime());

		if (promDay.getEndTime() != null || !promDay.getEndTime().equals("")) {
			end = LocalTime.parse(promDay.getEndTime());
			if (dayofWeek == promDay.getWeekDayID() && LocalTime.now().compareTo(start) >= 0 && LocalTime.now().compareTo(end) <= 0) {
				active = true;
			}
		} else {
			if (dayofWeek == promDay.getWeekDayID() && LocalTime.now().compareTo(start) >= 0) {
				active = true;
			}
		}
		return active;
	}

	private boolean IsValueSatisfied(List<PromotionLineItemModel> lineItems, PromotionSchemeModel scheme, List<AppliedPromotionElementModel> promElements) {
		boolean isSatisfied = false;
		List<AppliedPromotionElementModel> elements = promElements.stream().filter(x -> !x.getIsFOCRecord()).collect(Collectors.toList());
		for (AppliedPromotionElementModel element : elements) {
			element.setCurrentQuantity(new BigDecimal(lineItems.stream().filter(x -> element.getItemNumbers().contains(x.getItemNumber())).mapToDouble(x -> x.getLineQuantity().doubleValue()).sum()));
			BigDecimal availableQty = element.getCurrentQuantity();
			BigDecimal total = new BigDecimal(0);
			total = (BigDecimal) lineItems.stream().filter(x -> element.getItemNumbers().contains(x.getItemNumber())).mapToDouble(x -> x.getTotalBeforeTax().doubleValue());
			if ((scheme.getDiscountTypeID() != 3 && total.compareTo(element.getValuePoint()) >= 0) || (scheme.getDiscountTypeID() == 3 && total.compareTo(element.getValuePoint()) > 0)) {
				isSatisfied = true;
			} else {
				return false;
			}
		}

		return isSatisfied;
	}

	private boolean IsQuantitySatisfied(List<PromotionLineItemModel> lineItems, PromotionSchemeModel scheme, List<AppliedPromotionElementModel> promElements) {
		boolean isSatisfied = false;
		List<AppliedPromotionElementModel> elements = promElements.stream().filter(x -> !x.getIsFOCRecord()).collect(Collectors.toList());
		for (AppliedPromotionElementModel element : elements) {
			element.setCurrentQuantity(new BigDecimal(lineItems.stream().filter(x -> element.getItemNumbers().contains(x.getItemNumber())).mapToDouble(x -> x.getLineQuantity().doubleValue()).sum()));
			BigDecimal availableQty = element.getCurrentQuantity();
			if ((scheme.getDiscountTypeID() != 3 && availableQty.compareTo(element.getActualQuantity()) >= 0) || (scheme.getDiscountTypeID() == 3 && availableQty.compareTo(element.getActualQuantity()) > 0)) {
				isSatisfied = true;
			} else {
				return false;
			}
		}

		return isSatisfied;
	}

	private void ApplyPromotion(PromotionSchemeModel scheme, List<AppliedPromotionElementModel> promElements, List<PromotionLineItemModel> promotionLIModel) {
		int appliedCount = 0;
		if (scheme.getSchemeBaseType() == 1) {
			appliedCount = GetMinValuePoint(promElements, promotionLIModel);
		} else {
			appliedCount = promElements.stream().filter(x -> !x.getIsFOCRecord()).mapToInt(x -> x.getQtyTimes()).min().orElse(0);
		}
		BigDecimal discount = new BigDecimal(0);
		for (AppliedPromotionElementModel ele : promElements) {
			discount = new BigDecimal(0);
			discount = CalculateLIPromotion(scheme, ele, ele, promotionLIModel, appliedCount);

			ele.setPromotionDiscount(discount);
		}
	}

	private int GetMinValuePoint(List<AppliedPromotionElementModel> elements, List<PromotionLineItemModel> lineItems) {
		int minQty = 0;
		int valueCount = 0;
		elements = elements.stream().filter(x -> !x.getIsFOCRecord()).collect(Collectors.toList());
		BigDecimal price = new BigDecimal(0);
		BigDecimal currentQty = new BigDecimal(0);
		List<AppliedPromotionElementModel> elementsWithoutFOCs = elements.stream().filter(x -> x.getValuePoint() != null || x.getQtyPoint() != null).collect(Collectors.toList());
		for (AppliedPromotionElementModel tmpElem : elementsWithoutFOCs) {
			PromotionLineItemModel lineitem = lineItems.stream().filter(x -> tmpElem.getItemNumbers().contains(x.getItemNumber())).findFirst().orElse(null);
			if (lineitem != null) {
				currentQty = tmpElem.getCurrentQuantity();
				price = lineitem.getUomPrice();
				if (price.compareTo(new BigDecimal(0)) > 0) {
					minQty = (currentQty.divide(new BigDecimal((Math.ceil((tmpElem.getValuePoint().divide(price).doubleValue())))))).intValue();
					if (minQty < valueCount || valueCount == 0) {
						valueCount = minQty;
					}
				}
			}
		}
		return valueCount;
	}

	private BigDecimal GetFOCQTYAgg(PromotionSchemeModel scheme, List<PromotionLineItemModel> lines, List<AppliedPromotionElementModel> elements) {
		if (scheme.getIncentiveID() != 3) {
			return new BigDecimal(0);
		}
		PromotionLineItemModel line = lines.stream().filter(x -> x.getIsPromoted() == false && this.focElement.getItemNumbers().contains(x.getItemNumber())).findFirst().orElse(null);
		if (this.scannedFocFlag) {
			if (line == null) {
				return new BigDecimal(0);
			}
			BigDecimal totalQty = line.getLineQuantity();
			totalQty = new BigDecimal(lines.stream().filter(x -> x.getItemNumber() == line.getItemNumber() && !x.getIsPromoted()).mapToDouble(x -> x.getLineQuantity().doubleValue()).sum());
			AppliedPromotionElementModel element = elements.stream().filter(x -> x.getItemNumbers().contains(line.getItemNumber()) && (x.getQtyPoint() != null || x.getValuePoint() != null)).findFirst().orElse(null);
			AppliedPromotionElementModel FOCelement = elements.stream().filter(x -> x.getQtyPoint() == null && x.getValuePoint() == null).findFirst().orElse(null);

			int incentiveValue = FOCelement.getIncentiveValue().intValue();
			BigDecimal value = new BigDecimal(0);
			int valueCount = 0; // = elements.Min(x => x.ValueTimes);
			if (scheme.getSchemeBaseType() == 1) {
				valueCount = GetMinValuePoint(elements, lines);
			}
			List<AppliedPromotionElementModel> nonFocElements = elements.stream().filter(x -> !x.getIsFOCRecord()).collect(Collectors.toList());

			int qtyCount = nonFocElements.stream().mapToInt(x -> x.getQtyTimes()).min().orElse(0);
			int appliedCount = 0;
			int qtyPoint = 0;

			if (scheme.getSchemeBaseType() == 1) // value
			{
				if (element != null) {
					qtyPoint = (int) Math.ceil((element.getValuePoint().divide(line.getUomPrice())).doubleValue());
				}
				value = totalQty.subtract(new BigDecimal((qtyPoint * valueCount)));
				appliedCount = valueCount;
			} else {
				if (element != null) {
					qtyPoint = element.getQtyPoint().intValue();
				}
				value = totalQty.subtract(new BigDecimal((qtyPoint * qtyCount)));
				appliedCount = qtyCount;
			}
			if (scheme.getPromotionAppliedID() == 1 || scheme.getDiscountTypeID() == 3) // Once or excess
			{
				if (totalQty.subtract(new BigDecimal(incentiveValue)).compareTo(new BigDecimal(qtyPoint)) > 0) {
					return new BigDecimal(incentiveValue);
				} else {
					if (totalQty.subtract(new BigDecimal(qtyPoint)).compareTo(new BigDecimal(0)) > 0) {
						return totalQty.subtract(new BigDecimal(qtyPoint));
					} else {
						return new BigDecimal(0);
					}
				}
			} else {
				BigDecimal Qty = totalQty;
				int QtyValueCount = qtyCount;
				if (scheme.getSchemeBaseType() == 1) {
					QtyValueCount = valueCount;
				}
				// //Check if the FOC Contained also in the promotion itself
				if (element != null && FOCelement.getItemNumbers().contains(line.getItemNumber())) {
					int applied = 0;
				} else {
					if ((totalQty.subtract(new BigDecimal((incentiveValue * appliedCount))).compareTo(new BigDecimal(0)) > 0)) {
						return new BigDecimal(incentiveValue * appliedCount);
					} else {
						return totalQty;
					}
				}
			}
		} else {
			return this.focElement.getFOCQuantity();
		}
		return new BigDecimal(0);
	}

	private PromotionLineItemModel UpdateUIPromotionRecord(List<PromotionLineItemModel> lines, PromotionSchemeModel scheme, List<AppliedPromotionElementModel> elements) {
		AppliedPromotionModel index = this.Promotions.stream().filter(x -> x.getPromotionID() == scheme.getPromotionID()).findFirst().orElse(null);
		BigDecimal actualFOC = new BigDecimal(0);
		BigDecimal focQty = new BigDecimal(this.Promotions.stream().filter(x -> x.getPromotionID() == scheme.getPromotionID()).mapToDouble(x -> x.getFOCPoints().doubleValue()).sum());

		BigDecimal quantity = new BigDecimal(elements.stream().mapToDouble(x -> x.getCurrentQuantity().doubleValue()).sum());

		boolean deleted = false;
		boolean promotionsChanged = false;
		if (focElement != null) {
			actualFOC = GetFOCQTYAgg(scheme, lines, elements);
		}

		PromotionLineItemModel promotionRecord = new PromotionLineItemModel();

		if (index != null) // update the promotion record:
		{
			List<AppliedPromotionModel> lastPromotedItem = this.Promotions.stream().filter(x -> x.getPromotionID() == scheme.getPromotionID()).collect(Collectors.toList());

			if (scheme.getIncentiveID() == 3) // FOC
			{
				if (actualFOC.compareTo(focQty) != 0) // promotion is changed:
				{
					if (actualFOC.compareTo(focQty) < 0) {
						deleted = true;
					}
					promotionsChanged = true;
				}
			} else {
				BigDecimal promoQty = new BigDecimal(lastPromotedItem.stream().mapToDouble(x -> x.getPromotionQuantity().doubleValue()).sum());
				if (quantity.compareTo(promoQty) != 0) // promotion is changed:
				{
					if (quantity.compareTo(promoQty) < 0) {
						deleted = true;
					}
					promotionsChanged = true;
				}
			}

			if (promotionsChanged) {
				int promotionCount = 1;
				if (focElement != null) {
					promotionCount = (focElement.getFOCQuantity().subtract(focQty)).intValue() / focElement.getFOCQuantity().intValue();
				}

				boolean updateRecord = false;
				if (!lastPromotedItem.isEmpty()) {
					updateRecord = true;
				}
				if ((updateRecord)) {
					if (scheme.getIncentiveID() != 3) {
						CreateSalesTransactionPromotion(scheme, actualFOC, elements, lines, true);
					} else {
						CreateSalesTransactionPromotion(scheme, actualFOC, elements, lines, true);
					}
				}
				if (!updateRecord && !deleted) {
					CreateSalesTransactionPromotion(scheme, actualFOC, elements, lines, false);
				}
			}
		} else // create new promotion record:
		{
			CreateSalesTransactionPromotion(scheme, actualFOC, elements, lines, false);
		}
		return promotionRecord;
	}

	private void CreateSalesTransactionPromotion(PromotionSchemeModel scheme, BigDecimal actualFOCQty, List<AppliedPromotionElementModel> elements, List<PromotionLineItemModel> lines, boolean isUpdate) {
		AppliedPromotionModel prom;
		ArrayList<AppliedPromotionElementModel> promotionElements = new ArrayList<AppliedPromotionElementModel>();
		for (AppliedPromotionElementModel ele : elements) {
			promotionElements.add(ele);
		}
		switch (scheme.getIncentiveID()) {
		case 3: // FOC
		case 5: // Nominal Points
		{
			for (AppliedPromotionElementModel ele : promotionElements) {
				List<AppliedPromotionModel> oldPromotions = this.Promotions.stream().filter(x -> ele.getItemNumbers().contains(x.getItemNumber())).collect(Collectors.toList());
				PromotionLineItemModel line1 = lines.stream().filter(x -> ele.getItemNumbers().contains(x.getItemNumber())).findFirst().orElse(null);
				if (line1 != null) {
					SalesItemEntityModel line = this.salesItems.stream().filter(x -> x.getItemNumber() == line1.getItemNumber()).findFirst().orElse(null);
					if (line == null) {
						prom = new AppliedPromotionModel(scheme, new SalesItemEntityModel());
					} else {
						prom = new AppliedPromotionModel(scheme, line);
					}
					prom.setItemNumber(line1.getItemNumber());
					prom.setDiscountAmount(new BigDecimal(0));
					if (focElement != null) {
						if (this.focElement.getItemNumbers().contains(line1.getItemNumber())) {
							prom.setFOCPoints(actualFOCQty);
							prom.setDiscountAmount(this.focElement.getPrice().multiply(prom.getFOCPoints()));
							if (!isUpdate) {
								this.Promotions.add(0, prom);
							} else {
								
								AppliedPromotionModel m = this.Promotions.stream().filter(x-> x.getPromotionID() == ele.getPromotionID() && ele.getItemNumbers().contains(x.getItemNumber())).findFirst().orElse(null);
								int index = -1;	if(m != null) index = this.Promotions.indexOf(m);
								
								if (index != -1) {
									this.Promotions.set(index, prom);
								} else {
									this.Promotions.add(0, prom);
								}
							}
						}
					}
				}
			}
			break;
		}
		case 1: // Percentage
		case 2: // Fixed Discount
		case 4: // Fixed Value
		{
			for (AppliedPromotionElementModel ele : promotionElements) {
				List<AppliedPromotionModel> oldPromotions = this.Promotions.stream().filter(x -> ele.getItemNumbers().contains(x.getItemNumber())).collect(Collectors.toList());
				PromotionLineItemModel line1 = lines.stream().filter(x -> ele.getItemNumbers().contains(x.getItemNumber())).findFirst().orElse(null);
				if (line1 != null) {
					List<SalesItemEntityModel> lineItems = this.salesItems.stream().filter(x -> x.getItemNumber() == line1.getItemNumber()).collect(Collectors.toList());
					if (lineItems.size() > 0) {
						SalesItemEntityModel line = lineItems.get(0);
						prom = new AppliedPromotionModel(scheme, line);
						prom.setItemNumber(line1.getItemNumber());
						if (scheme.getSchemeBaseType() == 2) // QUANTITY
						{
							if (scheme.getDiscountTypeID() == 3) {
								prom.setAppliedQuantity((ele.getCurrentQuantity().subtract(ele.getActualQuantity())).subtract(new BigDecimal(oldPromotions.stream().mapToDouble(x -> x.getAppliedQuantity().doubleValue()).sum())));
							} else {
								prom.setAppliedQuantity(ele.getAppliedQuantity());
							}
						} else // VALUE
						{
							prom.setAppliedValue(ele.getAppliedValue());
						}
						prom.setDiscountAmount(ele.getPromotionDiscount());
						// prom.FixedValue = scheme.incentiveID == (int)IncentiveTypesEnum.FixedValue ?
						// ele.IncentiveValue : null;
						prom.setPromotionQuantity(ele.getCurrentQuantity());
						if (!isUpdate) {
							this.Promotions.add(0, prom);
						} else {

							AppliedPromotionModel m = this.Promotions.stream().filter(x-> x.getPromotionID() == ele.getPromotionID() && ele.getItemNumbers().contains(x.getItemNumber())).findFirst().orElse(null);
							int index = -1;	if(m != null) index = this.Promotions.indexOf(m);
							
							if (index != -1) {
								this.Promotions.set(index, prom);
							} else {
								this.Promotions.add(0, prom);
							}
						}
					}
				}
			}
			break;
		}
		}
	}

	private BigDecimal CalculateLIPromotion(PromotionSchemeModel scheme, AppliedPromotionElementModel element, AppliedPromotionElementModel parentItem, List<PromotionLineItemModel> promotionLIModel, int appliedCount) {
		BigDecimal discount = new BigDecimal(0);
		PromotionLineItemModel currentItem = promotionLIModel.stream().filter(x -> element.getItemNumbers().contains(x.getItemNumber())).findFirst().orElse(null);
		if (currentItem != null) {
			element.setPrice(currentItem.getUomPrice());
			element.setCurrentQuantity(new BigDecimal(promotionLIModel.stream().filter(x -> element.getItemNumbers().contains(x.getItemNumber())).mapToDouble(x -> x.getLineQuantity().doubleValue()).sum()));
			element.setPromotionDiscount(new BigDecimal(this.Promotions.stream().filter(x -> element.getItemNumbers().contains(x.getItemNumber())).mapToDouble(x -> x.getDiscountAmount().doubleValue()).sum()));
			discount = discount.add(ApplyPromotionSchemeOnElement(scheme, element, appliedCount, false, parentItem));

			PromotionLineItemModel m = promotionLIModel.stream().filter(x-> element.getItemNumbers().contains(x.getItemNumber())).findFirst().orElse(null);
			if (m != null) {
				boolean fromdelete = false;
				List<AppliedPromotionModel> promotionItem = this.Promotions.stream().filter(x -> x.getPromotionID() == scheme.getPromotionID()).collect(Collectors.toList());

				BigDecimal oldDiscount = new BigDecimal(0);
				if (promotionItem != null && fromdelete) {
					oldDiscount = (new BigDecimal(promotionItem.stream().mapToDouble(x -> x.getDiscountAmount().doubleValue()).sum())).multiply(new BigDecimal(-1));
				} else {
					oldDiscount = element.getPromotionDiscount();
				}
				element.setPromotionDiscount(discount);
				if (!discount.equals(oldDiscount)) {
					this.promoChanged = true;
				}
			}
			if (scheme.getIncentiveID() == 3) {
				this.promoChanged = true;
			}
		}
		return discount;
	}

	private BigDecimal ApplyPromotionSchemeOnElement(PromotionSchemeModel scheme, AppliedPromotionElementModel element, int appliedCount, boolean isPlu, AppliedPromotionElementModel parentItem) {
		BigDecimal discount = new BigDecimal(0);
		BigDecimal price = element.getPrice();
		BigDecimal quantity = element.getCurrentQuantity();
		switch (scheme.getPromotionAppliedID()) {
		case 1: // once
		{
			switch (scheme.getSchemeBaseType()) {
			case 1: // Value
			{
				discount = GetIncentiveTotal_Value(scheme, element, 1, isPlu, parentItem);
				break;
			}
			case 2: // Quantity
			{
				discount = GetIncentiveTotal_Quantity(scheme, price, quantity, element, 1, parentItem);
				break;
			}
			}
			break;
		}
		case 2: // Immediately
		case 3: // InvoiceTotal
		{
			switch (scheme.getSchemeBaseType()) {
			case 1: // Value
			{
				discount = GetIncentiveTotal_Value(scheme, element, appliedCount, isPlu, parentItem);
				break;
			}
			case 2: // Quantity
			{
				discount = GetIncentiveTotal_Quantity(scheme, price, quantity, element, appliedCount, parentItem);
				break;
			}
			}
			break;
		}
		}

		return discount;
	}

	private BigDecimal GetIncentiveTotal_Quantity(PromotionSchemeModel scheme, BigDecimal price, BigDecimal quantity, AppliedPromotionElementModel element, int appliedCount, AppliedPromotionElementModel parentItem) {
		BigDecimal totalDiscount = new BigDecimal(0);
		BigDecimal totalAmount = new BigDecimal(0);
		BigDecimal totalAppliedQty = new BigDecimal(0);
		BigDecimal qtyPoints = scheme.getIncentiveID() == 3 ? new BigDecimal(0) : parentItem.getQtyPoint();
		BigDecimal incentiveValue = parentItem.getIncentiveValue() == null ? new BigDecimal(0) : (BigDecimal) parentItem.getIncentiveValue();
		int discountType = scheme.getDiscountTypeID();
		if (parentItem.getAppliedQuantity() == null) {
			parentItem.setAppliedQuantity(new BigDecimal(0));
		}
		totalAppliedQty = qtyPoints.multiply(new BigDecimal(appliedCount)).subtract(parentItem.getAppliedQuantity());
		BigDecimal appliedQuantity = element.getCurrentQuantity().compareTo(totalAppliedQty) < 0 ? element.getCurrentQuantity() : totalAppliedQty;
		BigDecimal reminder = quantity.subtract(appliedQuantity);
		totalAmount = totalAmount.add(element.getCurrentTotal());
		switch (scheme.getIncentiveID()) {
		case 1: // Percentage
		{
			BigDecimal newPrice = price.subtract(price.multiply(incentiveValue));
			switch (discountType) {
			case 1: // Fixed
			{
				totalDiscount = newPrice.multiply(appliedQuantity.add(price.multiply(reminder)));
				element.setAppliedQuantity(appliedQuantity);
				break;
			}
			case 2: // Total //Same as Once
			{
				totalDiscount = newPrice.multiply(quantity);
				element.setAppliedQuantity(quantity);
				break;
			}
			case 3: // Excess //Same as Once
			{
				if (parentItem.getAppliedQuantity().add(quantity).compareTo(qtyPoints) < 0) {
					totalDiscount = totalAmount;
					element.setAppliedQuantity(quantity);
				} else {
					// totalDiscount = price * qtyPoints + newPrice * (quantity - qtyPoints);
					// element.AppliedQuantity = (quantity - qtyPoints);
					BigDecimal remainingPoints = new BigDecimal(0.00);
					if (parentItem.getAppliedQuantity().compareTo(qtyPoints) < 0) {
						remainingPoints = qtyPoints.subtract(parentItem.getAppliedQuantity());
					}
					// else remainingPoints = 0;
					remainingPoints = remainingPoints.compareTo(new BigDecimal(0)) < 0 ? new BigDecimal(0) : remainingPoints;
					totalDiscount = price.multiply(remainingPoints).add(newPrice.multiply(quantity.subtract(remainingPoints)));
					element.setAppliedQuantity(appliedQuantity);
				}
				break;
			}
			}
			break;
		}
		case 2: // Fixed Discount
		{
			switch (discountType) {
			case 1: // Fixed
			{
				totalDiscount = price.subtract(incentiveValue).multiply(appliedQuantity).add(price.multiply(reminder));
				element.setAppliedQuantity(appliedQuantity);
				break;
			}
			case 2: // Total //Same as Once
			{
				totalDiscount = price.subtract(incentiveValue).multiply(quantity);
				element.setAppliedQuantity(quantity);
				break;
			}
			case 3: // Excess //Same as Once
			{
				// totalDiscount = price * qtyPoints + (price - incentiveValue) * (quantity -
				// qtyPoints);
				// element.AppliedQuantity = (quantity - qtyPoints);
				BigDecimal newPrice = price.subtract(incentiveValue);
				if (parentItem.getAppliedQuantity().add(quantity).compareTo(qtyPoints) < 0) {
					totalDiscount = totalAmount;
					element.setAppliedQuantity(quantity);
				} else {
					BigDecimal remainingPoints = new BigDecimal(0.00);
					if (parentItem.getAppliedQuantity().compareTo(qtyPoints) < 0) {
						remainingPoints = qtyPoints.subtract(parentItem.getAppliedQuantity());
					}
					// else remainingPoints = 0;
					remainingPoints = remainingPoints.compareTo(new BigDecimal(0)) < 0 ? new BigDecimal(0) : remainingPoints;
					totalDiscount = price.multiply(remainingPoints).add(newPrice.multiply(quantity.subtract(remainingPoints)));
					element.setAppliedQuantity(appliedQuantity);
				}
				break;
			}
			}
			break;
		}
		case 3: // FOC
		{
			switch (discountType) {
			case 1: // Fixed
			case 2: // Total //Same as Fixed
			{
				// if(scannedFocFlag)
				this.focElement.setFOCQuantity(this.focElement.getIncentiveValue().multiply(new BigDecimal(appliedCount)));
				element.setAppliedQuantity(qtyPoints.multiply(new BigDecimal(appliedCount)));
				totalDiscount = price.multiply(quantity);
				break;
			}
			case 3: // Excess // Same as Once
			{
				// if(scannedFocFlag)
				this.focElement.setFOCQuantity(this.focElement.getIncentiveValue());
				element.setAppliedQuantity(qtyPoints);
				totalDiscount = price.multiply(quantity);
				break;
			}
			}
			break;
		}
		case 4: // Fixed value
		{
			SalesItemEntityModel model = this.salesItems.stream().filter(x -> element.getItemNumbers().contains(x.getItemNumber())).findFirst().orElse(null);
			BigDecimal incentivePrice = model.getUnitPrice();
			switch (discountType) {
			case 1: // Fixed
			{
				// totalDiscount = incentivePrice * appliedQuantity + price * reminder;
				totalDiscount = (incentivePrice.multiply(appliedQuantity)).add(price.multiply(reminder));
				element.setAppliedQuantity(appliedQuantity);
				break;
			}
			case 2: // Total // Same as Once
			{
				totalDiscount = incentivePrice.multiply(quantity);
				element.setAppliedQuantity(quantity);
				break;
			}
			case 3: // Excess // Same as Once
			{
				if (parentItem.getAppliedQuantity().add(quantity).compareTo(qtyPoints) < 0) {
					totalDiscount = totalAmount;
					element.setAppliedQuantity(quantity);
				} else {
					BigDecimal remainingPoints = new BigDecimal(0.00);
					if (parentItem.getAppliedQuantity().compareTo(qtyPoints) < 0) {
						remainingPoints = qtyPoints.subtract(parentItem.getAppliedQuantity());
					}
					remainingPoints = remainingPoints.compareTo(new BigDecimal(0.00)) < 0 ? new BigDecimal(0.00) : remainingPoints;
					totalDiscount = price.multiply(remainingPoints).add(incentivePrice.multiply(quantity.subtract(remainingPoints)));
					element.setAppliedQuantity(appliedQuantity);
				}
				break;
			}
			}
			break;
		}
		}
		return totalAmount.subtract(totalDiscount);
	}

	private BigDecimal GetIncentiveTotal_Value(PromotionSchemeModel scheme, AppliedPromotionElementModel element, int appliedCount, boolean isPlu, AppliedPromotionElementModel parentItem) {
		BigDecimal totalDiscount = new BigDecimal(0);
		BigDecimal valuePoint = scheme.getIncentiveID() == 3 ? new BigDecimal(0) : (BigDecimal) parentItem.getValuePoint();
		BigDecimal incentiveValue = new BigDecimal(0);
		if (parentItem.getIncentiveValue() != null) {
			incentiveValue = (BigDecimal) parentItem.getIncentiveValue();
		}
		int discountType = scheme.getDiscountTypeID();
		BigDecimal price = element.getPrice();
		BigDecimal quantity = element.getCurrentQuantity();
		BigDecimal totalAmount = price.multiply(quantity);
		parentItem.setTotalValue(parentItem.getTotalValue().add(totalAmount));

		BigDecimal applicableValue = new BigDecimal(0);
		if (parentItem.getAppliedValue() == null) {
			parentItem.setAppliedValue(new BigDecimal(0));
		}

		BigDecimal totalAppliedValue = valuePoint.multiply(new BigDecimal(appliedCount)).subtract(parentItem.getAppliedValue());
		applicableValue = element.getCurrentTotal().compareTo(totalAppliedValue) < 0 ? element.getCurrentTotal() : totalAppliedValue;

		BigDecimal triggerQuantity = new BigDecimal(0);
		if (price.compareTo(new BigDecimal(0)) > 0) {
			if (isPlu) {
				triggerQuantity = valuePoint.divide(price).setScale(3, RoundingMode.CEILING);
			} else {
				triggerQuantity = new BigDecimal((int) (Math.ceil(valuePoint.divide(price).doubleValue())));
			}
		}

		switch (scheme.getIncentiveID()) {
		case 1: // Percentage
		{
			switch (discountType) {
			case 1: // Fixed
			{
				// 120 – (100*0.1)
				totalDiscount = totalAmount.subtract(applicableValue.multiply(incentiveValue));
				element.setAppliedValue(applicableValue);
				break;
			}
			case 2: // Total // Same as Once
			{
				totalDiscount = totalAmount.subtract(totalAmount.multiply(incentiveValue));
				element.setAppliedValue(totalAmount);
				break;
			}
			case 3: // Excess //Same as Once
			{
				if (parentItem.getAppliedValue().add(element.getCurrentTotal()).compareTo(valuePoint) < 0) {
					totalDiscount = element.getCurrentTotal();
					element.setAppliedValue(totalDiscount);
				} else {
					if (!parentItem.getAppliedValue().equals(new BigDecimal(0))) {
						valuePoint = valuePoint.subtract((BigDecimal) parentItem.getAppliedValue());
						valuePoint = valuePoint.compareTo(new BigDecimal(0)) < 0 ? new BigDecimal(0) : valuePoint;
					}
					// totalDiscount = totalAmount - ((parentItem.TotalValue - valuePoint -
					// (decimal)parentItem.AppliedValue) * incentiveValue);
					totalDiscount = totalAmount.subtract(((parentItem.getTotalValue().subtract(valuePoint).subtract(parentItem.getAppliedValue())).multiply(incentiveValue)));
					element.setAppliedValue(totalAmount); // (parentItem.TotalValue - valuePoint -
															// parentItem.AppliedValue);
				}

				break;
			}
			}
			break;
		}
		case 2: // Fixed Discount
		{
			switch (discountType) {
			case 1: // Fixed
			{
				// 10pc*5 + 2pc*10
				// totalDiscount = (triggerQuantity * appliedCount * (price - incentiveValue)) +
				// (quantity - (triggerQuantity * appliedCount)) * price;
				totalDiscount = (triggerQuantity.multiply(new BigDecimal(appliedCount)).multiply(price.subtract(incentiveValue))).add(quantity.subtract(triggerQuantity.multiply(new BigDecimal(appliedCount))).multiply(price));
				element.setAppliedValue(valuePoint.multiply(new BigDecimal(appliedCount)));
				break;
			}
			case 2: // Total //Same as Once
			{
				totalDiscount = quantity.multiply(price.subtract(incentiveValue));
				element.setAppliedValue(totalAmount);
				break;
			}
			case 3: // Excess //Same as Once
			{
				if (parentItem.getAppliedQuantity().add(element.getCurrentTotal()).compareTo(valuePoint) < 0) {
					totalDiscount = element.getCurrentTotal();
					element.setAppliedValue(totalDiscount);
				} else {
					BigDecimal remainingPoints = new BigDecimal(0.00);
					if (parentItem.getAppliedValue().compareTo(valuePoint) < 0) {
						remainingPoints = valuePoint.subtract((BigDecimal) parentItem.getAppliedValue());
						remainingPoints = new BigDecimal((int) (Math.ceil(remainingPoints.divide(price).doubleValue())));
					}
					remainingPoints = remainingPoints.compareTo(new BigDecimal(0)) < 0 ? new BigDecimal(0) : remainingPoints;
					totalDiscount = remainingPoints.multiply(price).add(quantity.subtract(remainingPoints).multiply(price.subtract(incentiveValue)));
					element.setAppliedValue(totalAmount);
				}
				break;
			}
			}
			break;
		}
		case 3: // FOC
		{
			switch (discountType) {
			case 1: // Fixed
			case 2: // Total // Same as Fixed
			{
				// 12*10 + 4 Each B
				this.focElement.setFOCQuantity(this.focElement.getIncentiveValue().multiply(new BigDecimal(appliedCount)));
				element.setAppliedValue(valuePoint.multiply(new BigDecimal(appliedCount)));
				totalDiscount = totalAmount;
				break;
			}
			case 3: // Excess // Same as Once
			{
				this.focElement.setFOCQuantity(this.focElement.getIncentiveValue());
				element.setAppliedValue(valuePoint);
				totalDiscount = totalAmount;
				break;
			}
			}
			break;
		}
		case 4: // Fixed value
		{
			SalesItemEntityModel model = this.salesItems.stream().filter(x -> element.getItemNumbers().contains(x.getItemNumber())).findFirst().orElse(null);
			BigDecimal incentivePrice = model.getUnitPrice();
			switch (discountType) {
			case 1: // Fixed
			{
				// 10pc*7 + 2pc*10
				totalDiscount = (triggerQuantity.multiply(new BigDecimal(appliedCount)).multiply(incentivePrice)).add(quantity.subtract(triggerQuantity.multiply(new BigDecimal(appliedCount))).multiply(price));
				element.setAppliedValue(valuePoint.multiply(new BigDecimal(appliedCount)));
				break;
			}
			case 2: // Total // Same as Once
			{
				// 12pc*7
				totalDiscount = incentivePrice.multiply(quantity);
				element.setAppliedValue(totalAmount);
				break;
			}
			case 3: // Excess // Same as Once
			{
				if (parentItem.getAppliedValue().add(element.getCurrentTotal()).compareTo(valuePoint) < 0) {
					totalDiscount = element.getCurrentTotal();
					element.setAppliedValue(totalDiscount);
				} else {
					BigDecimal remainingPoints = new BigDecimal(0.00);
					if (parentItem.getAppliedValue().compareTo(valuePoint) < 0) {
						remainingPoints = valuePoint.subtract(parentItem.getAppliedValue());
						remainingPoints = new BigDecimal((int) (Math.ceil(remainingPoints.divide(price).doubleValue())));
					}
					remainingPoints = remainingPoints.compareTo(new BigDecimal(0.00)) < 0 ? new BigDecimal(0.00) : remainingPoints;
					totalDiscount = remainingPoints.multiply(price).add(quantity.subtract(remainingPoints).multiply(incentiveValue));
					element.setAppliedValue(totalAmount);
				}
				break;
			}
			}
			break;
		}
		}
		return totalAmount.subtract(totalDiscount);
	}
}