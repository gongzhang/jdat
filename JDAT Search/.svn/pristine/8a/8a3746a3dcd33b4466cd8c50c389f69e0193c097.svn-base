package cn.edu.buaa.sei.jdat.search;

import java.util.ArrayList;
import cn.edu.buaa.sei.jdat.model.Element;

public class IndexItem extends ArrayList<Element> {
	
	private static final long serialVersionUID = 3137631288081447156L;

	public IndexItem() {
		super(1);
	}
	
	public String getName() {
		if (size() == 0)
			return null;
		else
			return get(0).getName().toLowerCase();
	}
	
	public String getFullName() {
		if (size() == 0)
			return null;
		else
			return get(0).getSimpleSignature().toLowerCase();
	}
	
}
