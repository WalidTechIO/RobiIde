package exercice4;

import java.util.HashMap;
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
	
	public Reference deleteReference(String name) {
		return references.remove(name);
	}

}
