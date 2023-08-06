package abs.cco.plugins.models;

import java.util.*;
import java.math.*;

public class AppliedPromotionElementModel {
	public AppliedPromotionElementModel() {
	}

	public AppliedPromotionElementModel(PromotionElementModel entity) {
		this.setPromotionID(entity.getPromotionID());
		this.setItemLevelReference(entity.getItemLevelReference());
		this.setIncentiveValue(entity.getIncentiveValue());
		this.setQtyPoint(entity.getQtyPoint());
		this.setValuePoint(entity.getValuePoint());
	}

	private int PromotionID;

	public final int getPromotionID() {
		return PromotionID;
	}

	public final void setPromotionID(int value) {
		PromotionID = value;
	}

	private String ItemLevelReference;

	public final String getItemLevelReference() {
		return ItemLevelReference;
	}

	public final void setItemLevelReference(String value) {
		ItemLevelReference = value;
	}

	public final List<String> getItemNumbers() {
		return Arrays.asList(this.getItemLevelReference().split("[,]"));
	}

	private BigDecimal QtyPoint = null;

	public final BigDecimal getQtyPoint() {
		return QtyPoint;
	}

	public final void setQtyPoint(BigDecimal value) {
		QtyPoint = value;
	}

	private BigDecimal ValuePoint = null;

	public final BigDecimal getValuePoint() {
		return ValuePoint;
	}

	public final void setValuePoint(BigDecimal value) {
		ValuePoint = value;
	}

	private BigDecimal IncentiveValue = null;

	public final BigDecimal getIncentiveValue() {
		return IncentiveValue;
	}

	public final void setIncentiveValue(BigDecimal value) {
		IncentiveValue = value;
	}

	private BigDecimal TotalValue = new BigDecimal(0);

	public final BigDecimal getTotalValue() {
		return TotalValue;
	}

	public final void setTotalValue(BigDecimal value) {
		TotalValue = value;
	}

	private BigDecimal CurrentQuantity = new BigDecimal(0);

	public final BigDecimal getCurrentQuantity() {
		return CurrentQuantity;
	}

	public final void setCurrentQuantity(BigDecimal value) {
		CurrentQuantity = value;
	}

	public final BigDecimal getCurrentTotal() {
		return getCurrentQuantity().multiply(getPrice());
	}

	public final BigDecimal getActualQuantity() {
		if (getQtyPoint() != null) {
			return (BigDecimal) getQtyPoint();
		} else {
			return new BigDecimal(0);
		}
	}

	private BigDecimal Price = new BigDecimal(0);

	public final BigDecimal getPrice() {
		return Price;
	}

	public final void setPrice(BigDecimal value) {
		Price = value;
	}

	private BigDecimal PromotionDiscount = new BigDecimal(0);

	public final BigDecimal getPromotionDiscount() {
		return PromotionDiscount;
	}

	public final void setPromotionDiscount(BigDecimal value) {
		PromotionDiscount = value;
	}

	public final int getQtyTimes() {
		return (getActualQuantity().compareTo(new BigDecimal(0)) > 0 ? getCurrentQuantity().divide(getActualQuantity()) : 0).intValue();
	}

	public final boolean getIsFOCRecord() {
		if (getQtyPoint() == null && getValuePoint() == null) {
			return true;
		} else {
			return false;
		}
	}

	private BigDecimal AppliedQuantity = null;

	public final BigDecimal getAppliedQuantity() {
		return AppliedQuantity;
	}

	public final void setAppliedQuantity(BigDecimal value) {
		AppliedQuantity = value;
	}

	private BigDecimal AppliedValue = null;

	public final BigDecimal getAppliedValue() {
		return AppliedValue;
	}

	public final void setAppliedValue(BigDecimal value) {
		AppliedValue = value;
	}

	private BigDecimal FOCQuantity = new BigDecimal(0);

	public final BigDecimal getFOCQuantity() {
		return FOCQuantity;
	}

	public final void setFOCQuantity(BigDecimal value) {
		FOCQuantity = value;
	}
}