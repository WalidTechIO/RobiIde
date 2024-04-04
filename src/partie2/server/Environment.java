package partie2.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
	
	@Override
	public String toString() {
		String res = "";
		
		for(Entry<String, Reference> entry : references.entrySet()) {
			String className = entry.getValue().getRef().getClass().getName();
			if(className.equals("java.lang.Class")) {
				Class<?> classe = (Class<?>) entry.getValue().getRef();
				className = "The class " + classe.getCanonicalName();
			} else {
				className = "An instance of " + className;
			}
			res += entry.getKey() + " is referencing: " + className + "\n";
		}
		
		return res;
	}

	public void clear(String prefix) {
		List<String> toDelete = new ArrayList<>();
		references.forEach((s, r) -> {
			if(s.startsWith(prefix) && !s.equals("space")) toDelete.add(s);
		});
		toDelete.forEach((s) -> references.remove(s));
	}
	
	public Map<String, List<String>> scriptsMap() {
		Map<String, List<String>> map = new HashMap<>();
		references.forEach((s,r) -> {
			map.put(s, r.availableScript());
		});
		return map;
	}

}
