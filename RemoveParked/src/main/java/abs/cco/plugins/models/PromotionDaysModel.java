//WeekDays
//types.Add(new { Value = 1, Text = LangService.IsArabic ? "السبت" : "Saturday" });
//types.Add(new { Value = 2, Text = LangService.IsArabic ? "الاحد" : "Sunday" });
//types.Add(new { Value = 3, Text = LangService.IsArabic ? "الاثنين" : "Monday" });
//types.Add(new { Value = 4, Text = LangService.IsArabic ? "الثلاثاء" : "Tuesday" });
//types.Add(new { Value = 5, Text = LangService.IsArabic ? "الاربعاء" : "Wednesday" });
//types.Add(new { Value = 6, Text = LangService.IsArabic ? "الخميس" : "Thursday" });
//types.Add(new { Value = 7, Text = LangService.IsArabic ? "الجمعه" : "Friday" });

package abs.cco.plugins.models;

public class PromotionDaysModel
{
	private int PromotionDayID;
	public final int getPromotionDayID()
	{
		return PromotionDayID;
	}
	public final void setPromotionDayID(int value)
	{
		PromotionDayID = value;
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
	private int WeekDayID;
	public final int getWeekDayID()
	{
		return WeekDayID;
	}
	public final void setWeekDayID(int value)
	{
		WeekDayID = value;
	}
	private String StartTime;
	public final String getStartTime()
	{
		return StartTime;
	}
	public final void setStartTime(String value)
	{
		StartTime = value;
	}
	private String EndTime;
	public final String getEndTime()
	{
		return EndTime;
	}
	public final void setEndTime(String value)
	{
		EndTime = value;
	}
}