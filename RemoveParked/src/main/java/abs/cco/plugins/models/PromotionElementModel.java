package abs.cco.plugins.models;
import java.math.*;
import java.util.Arrays;
import java.util.List;

public class PromotionElementModel
{
	private int PromotionElementID;
	public final int getPromotionElementID()
	{
		return PromotionElementID;
	}
	public final void setPromotionElementID(int value)
	{
		PromotionElementID = value;
	}
	private int PromotionID;
	public final int getPromotionID()
	{
		return PromotionID;
	}
	public final void setPromotionID(int value)
	{
		PromotionID = value;
	}
	private int ItemLevelTypeId;
	public final int getItemLevelTypeId()
	{
		return ItemLevelTypeId;
	}
	public final void setItemLevelTypeId(int value)
	{
		ItemLevelTypeId = value;
	}
	private String ItemLevelReference;
	public final String getItemLevelReference()
	{
		return ItemLevelReference;
	}
	public final void setItemLevelReference(String value)
	{
		ItemLevelReference = value;
	}
	private String ItemUOMID;
	public final String getItemUOMID()
	{
		return ItemUOMID;
	}
	public final void setItemUOMID(String value)
	{
		ItemUOMID = value;
	}
	private BigDecimal QtyPoint = null;
	public final BigDecimal getQtyPoint()
	{
		return QtyPoint;
	}
	public final void setQtyPoint(BigDecimal value)
	{
		QtyPoint = value;
	}
	private BigDecimal ValuePoint = null;
	public final BigDecimal getValuePoint()
	{
		return ValuePoint;
	}
	public final void setValuePoint(BigDecimal value)
	{
		ValuePoint = value;
	}
	private BigDecimal IncentiveValue = null;
	public final BigDecimal getIncentiveValue()
	{
		return IncentiveValue;
	}
	public final void setIncentiveValue(BigDecimal value)
	{
		IncentiveValue = value;
	}
	public final List<String> getItemNumbers()
	{
		return Arrays.asList(this.getItemLevelReference().split("[,]"));
	}
}