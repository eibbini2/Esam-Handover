//SchemeBaseTypes
//types.Add(new { Value = 1, Text = LangService.IsArabic ? "كمية" : "Quantity" });
//types.Add(new { Value = 2, Text = LangService.IsArabic ? "قيمة" : "Value" });

//POSs Levels
//types.Add(new { Value = 1, Text = LangService.IsArabic ? "كل الكاشات" : "All POSs" });
//types.Add(new { Value = 2, Text = LangService.IsArabic ? "كاش محدد" : "Specific POS" });

//SelectCustomerLevels
//types.Add(new { Value = 1, Text = LangService.IsArabic ? "كل العملاء" : "All Customers" });
//types.Add(new { Value = 2, Text = LangService.IsArabic ? "عميل محدد" : "Specific Customer" });
//types.Add(new { Value = 3, Text = LangService.IsArabic ? "مجموعة عملاء محدده" : "Specific Customer Group" });

//DiscountTypes
//types.Add(new { Value = 1, Text = LangService.IsArabic ? "ثابت" : "Fixed" });
//types.Add(new { Value = 2, Text = LangService.IsArabic ? "مجموع" : "Total" });
//types.Add(new { Value = 3, Text = LangService.IsArabic ? "تجاوز الحد" : "Excess" });

//PromotionApplieds
//types.Add(new { Value = 1, Text = LangService.IsArabic ? "مرة" : "Once" });
//types.Add(new { Value = 2, Text = LangService.IsArabic ? "عدة مرات" : "Multiple Time" });

//Incentives
//types.Add(new { Value = 1, Text = LangService.IsArabic ? "نسبة" : "Percentage" });
//types.Add(new { Value = 2, Text = LangService.IsArabic ? "خصم ثابت" : "Fixed Discount" });
//types.Add(new { Value = 3, Text = LangService.IsArabic ? "مادة مجانية" : "FOC" });
//types.Add(new { Value = 4, Text = LangService.IsArabic ? "قيمة ثابتة" : "Fixed value" });

package abs.cco.plugins.models;

import java.util.*;

import abs.cco.plugins.helpers.GlobalHelper;

public class PromotionSchemeModel {
//	public PromotionSchemeModel() {
//		this.setPromotionDays(new List<PromotionDaysModel>());
//		this.setPromotionElements(new List<PromotionElementModel>());
//	}

	public final List<String> getCustomerIDsList()
	{
		return GlobalHelper.isNullOrEmpty(this.getCustomerIDs()) ? new ArrayList<String>() :Arrays.asList(this.getCustomerIDs().split("[,]"));
	}
	public final List<String> getCustomerGroupIDsList()
	{
		return GlobalHelper.isNullOrEmpty(this.getCustomerGroupIDs()) ? new ArrayList<String>() :Arrays.asList(this.getCustomerGroupIDs().split("[,]"));
	}
	
	private int PromotionID;

	public final int getPromotionID() {
		return PromotionID;
	}

	public final void setPromotionID(int value) {
		PromotionID = value;
	}

	private int SchemeBaseType;

	public final int getSchemeBaseType() {
		return SchemeBaseType;
	}

	public final void setSchemeBaseType(int value) {
		SchemeBaseType = value;
	}

	private int CustomerLevelTypeId;

	public final int getCustomerLevelTypeId() {
		return CustomerLevelTypeId;
	}

	public final void setCustomerLevelTypeId(int value) {
		CustomerLevelTypeId = value;
	}

	private int POSLevelTypeId;

	public final int getPOSLevelTypeId() {
		return POSLevelTypeId;
	}

	public final void setPOSLevelTypeId(int value) {
		POSLevelTypeId = value;
	}

	private String POSs;

	public final String getPOSs() {
		return POSs;
	}

	public final void setPOSs(String value) {
		POSs = value;
	}

	private String StoreID;

	public final String getStoreID() {
		return StoreID;
	}

	public final void setStoreID(String value) {
		StoreID = value;
	}

	private String CustomerGroupIDs;

	public final String getCustomerGroupIDs() {
		return CustomerGroupIDs;
	}

	public final void setCustomerGroupIDs(String value) {
		CustomerGroupIDs = value;
	}

	private String CustomerIDs;

	public final String getCustomerIDs() {
		return CustomerIDs;
	}

	public final void setCustomerIDs(String value) {
		CustomerIDs = value;
	}

	private int PromotionAppliedID;

	public final int getPromotionAppliedID() {
		return PromotionAppliedID;
	}

	public final void setPromotionAppliedID(int value) {
		PromotionAppliedID = value;
	}

	private int DiscountTypeID;

	public final int getDiscountTypeID() {
		return DiscountTypeID;
	}

	public final void setDiscountTypeID(int value) {
		DiscountTypeID = value;
	}

	private int IncentiveID;

	public final int getIncentiveID() {
		return IncentiveID;
	}

	public final void setIncentiveID(int value) {
		IncentiveID = value;
	}

	private String Description;

	public final String getDescription() {
		return Description;
	}

	public final void setDescription(String value) {
		Description = value;
	}

	private List<PromotionDaysModel> PromotionDays;

	public final List<PromotionDaysModel> getPromotionDays() {
		return PromotionDays;
	}

	public final void setPromotionDays(List<PromotionDaysModel> value) {
		PromotionDays = value;
	}

	private List<PromotionElementModel> PromotionElements;

	public final List<PromotionElementModel> getPromotionElements() {
		return PromotionElements;
	}

	public final void setPromotionElements(List<PromotionElementModel> value) {
		PromotionElements = value;
	}
	private boolean IsSatisfied;
	public final boolean getIsSatisfied()
	{
		return IsSatisfied;
	}
	public final void setIsSatisfied(boolean value)
	{
		IsSatisfied = value;
	}
}
