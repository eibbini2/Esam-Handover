package abs.cco.plugins.models;
import java.math.*;

public class PromotionLineItemModel
{
	public PromotionLineItemModel()
	{
	}

	public PromotionLineItemModel(SalesItemEntityModel model)
	{
		this.setTotalBeforeTax(model.getLineTotal());
		this.setItemNumber(model.getItemNumber());
		this.setLineQuantity(model.getQuantity());
		this.setUomPrice(model.getUnitPrice());
	}

	private BigDecimal LineQuantity = new BigDecimal(0);
	public final BigDecimal getLineQuantity()
	{
		return LineQuantity;
	}
	public final void setLineQuantity(BigDecimal value)
	{
		LineQuantity = value;
	}
	private BigDecimal UomPrice = new BigDecimal(0);
	public final BigDecimal getUomPrice()
	{
		return UomPrice;
	}
	public final void setUomPrice(BigDecimal value)
	{
		UomPrice = value;
	}
	private boolean IsPromoted;
	public final boolean getIsPromoted()
	{
		return IsPromoted;
	}
	public final void setIsPromoted(boolean value)
	{
		IsPromoted = value;
	}
	private BigDecimal TotalBeforeTax = new BigDecimal(0);
	public final BigDecimal getTotalBeforeTax()
	{
		return TotalBeforeTax;
	}
	public final void setTotalBeforeTax(BigDecimal value)
	{
		TotalBeforeTax = value;
	}
	private String ItemNumber;
	public final String getItemNumber()
	{
		return ItemNumber;
	}
	public final void setItemNumber(String value)
	{
		ItemNumber = value;
	}
}