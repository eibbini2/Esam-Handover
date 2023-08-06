package abs.cco.plugins.helpers;
import java.util.ArrayList;
import java.util.List;
import abs.cco.plugins.models.PromotionElementModel;
import abs.cco.plugins.models.PromotionSchemeModel;

public class GlobalVariables {

	public static List<PromotionSchemeModel> ActivePromotions;

	public static List<PromotionElementModel> getPromotionElements() {
		List<PromotionElementModel> elements = new ArrayList<PromotionElementModel>();
		if (ActivePromotions == null || ActivePromotions.isEmpty())
			return elements;
		for (PromotionSchemeModel p : ActivePromotions) {
			elements.addAll(p.getPromotionElements());
		}
		return elements;
	}

}
