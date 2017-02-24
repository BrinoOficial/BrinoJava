package cc.brino.Brpp.Syntax;

import java.util.Iterator;
import org.fife.ui.autocomplete.BasicCompletion;
import org.fife.ui.autocomplete.DefaultCompletionProvider;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import cc.brino.Brpp.Utils.JSONUtils;


public class BrinoCompletionProvider extends DefaultCompletionProvider {
	
	public BrinoCompletionProvider(){
		super();
		
		JSONArray Keywords = JSONUtils.getKeywords();
		@SuppressWarnings("unchecked")
		Iterator<JSONObject> iterator = Keywords.iterator();
		while (iterator.hasNext()) {
			JSONObject key = iterator.next();
			String arg = (String) key.get("highlight");
			addCompletion(new BasicCompletion(this, arg));
		}
	}
	
}
