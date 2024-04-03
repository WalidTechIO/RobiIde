package partie2.server;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class Environment {
	
	private final Map<String, Reference> references = new HashMap<>();
	
	public Environment() {
		
	}
	
	public void addReference(String name, Reference reference) {
		references.put(name, reference);
	}
	
	public String getNameByReference(Reference reference) {
		for(Entry<String, Reference> entry : references.entrySet()) {
			if(entry.getValue().equals(reference)) return entry.getKey();
		}
		return null;
	}
	
	public Reference getReferenceByName(String name) {
		return references.get(name);
	}
	
	public Reference deleteReference(String name) {
		return references.remove(name);
	}

}
