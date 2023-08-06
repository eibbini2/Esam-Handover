package abs.cco.plugins.models;
import java.time.*;
import java.math.*;

public class SalesItemEntityModel
{
	private LocalDateTime AddedDateTime = LocalDateTime.MIN;
	public final LocalDateTime getAddedDateTime()
	{
		return AddedDateTime;
	}
	public final void setAddedDateTime(LocalDateTime value)
	{
		AddedDateTime = value;
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
	private BigDecimal unitPrice = new BigDecimal(0);
	public final BigDecimal getUnitPrice()
	{
		return unitPrice;
	}
	public final void setUnitPrice(BigDecimal value)
	{
		unitPrice = value;
	}
	private BigDecimal quantity = new BigDecimal(0);
	public final BigDecimal getQuantity()
	{
		return quantity;
	}
	public final void setQuantity(BigDecimal value)
	{
		quantity = value;
	}

	private BigDecimal lineTotal = new BigDecimal(0);
	public final BigDecimal getLineTotal()
	{
		return lineTotal;
	}
	public final void setLineTotal(BigDecimal value)
	{
		lineTotal = value;
	}
}