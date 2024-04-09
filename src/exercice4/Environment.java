package exercice4;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Environment {
	
	private final Map<String, Reference> references = new HashMap<>();
	
	public Environment() {
		
	}
	
	public void addReference(String name, Reference reference) {
		references.put(name, reference);
	}
	
	public Reference getReferenceByName(String name) {
		return references.get(name);
	}
	
	public void clear(String prefix) {
		List<String> toDelete = new ArrayList<>();
		references.forEach((s, r) -> {
			if(s.startsWith(prefix) && !s.equals("space")) toDelete.add(s);
		});
		toDelete.forEach((s) -> references.remove(s));
	}

}
