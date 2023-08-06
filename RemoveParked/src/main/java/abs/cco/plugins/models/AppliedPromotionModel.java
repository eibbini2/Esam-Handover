package abs.cco.plugins.models;
import java.math.*;

public class AppliedPromotionModel
{
	public AppliedPromotionModel(PromotionSchemeModel scheme, SalesItemEntityModel line)
	{
		this.setPromotionID(scheme.getPromotionID());
		this.setDiscription(scheme.getDescription());
		this.setLineTotal(line.getLineTotal());
		this.setLineQuantity(line.getQuantity());
	}

	private BigDecimal LineTotal = new BigDecimal(0);
	public final BigDecimal getLineTotal()
	{
		return LineTotal;
	}
	public final void setLineTotal(BigDecimal value)
	{
		LineTotal = value;
	}
	private int PromotionID;
	public final int getPromotionID()
	{
		return PromotionID;
	}
	private void setPromotionID(int value)
	{
		PromotionID = value;
	}
	private String Discription;
	public final String getDiscription()
	{
		return Discription;
	}
	private void setDiscription(String value)
	{
		Discription = value;
	}
	private BigDecimal FOCPoints = new BigDecimal(0);
	public final BigDecimal getFOCPoints()
	{
		return FOCPoints;
	}
	public final void setFOCPoints(BigDecimal value)
	{
		FOCPoints = value;
	}
	private BigDecimal AppliedQuantity = null;
	public final BigDecimal getAppliedQuantity()
	{
		return AppliedQuantity;
	}
	public final void setAppliedQuantity(BigDecimal value)
	{
		AppliedQuantity = value;
	}
	private BigDecimal AppliedValue = null;
	public final BigDecimal getAppliedValue()
	{
		return AppliedValue;
	}
	public final void setAppliedValue(BigDecimal value)
	{
		AppliedValue = value;
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
	private BigDecimal DiscountAmount = new BigDecimal(0);
	public final BigDecimal getDiscountAmount()
	{
		return DiscountAmount;
	}
	public final void setDiscountAmount(BigDecimal value)
	{
		DiscountAmount = value;
	}
	private BigDecimal PromotionQuantity = new BigDecimal(0);
	public final BigDecimal getPromotionQuantity()
	{
		return PromotionQuantity;
	}
	public final void setPromotionQuantity(BigDecimal value)
	{
		PromotionQuantity = value;
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
	public final BigDecimal getDiscountRounded()
	{
		return getDiscountAmount().setScale(2, RoundingMode.CEILING);
	}
	public final BigDecimal getUILineNetTotal()
	{
		return getDiscountRounded().multiply(new BigDecimal(-1));
	}
}