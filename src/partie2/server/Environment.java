package partie2.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import partie2.io.ReferenceInfo;

public class Environment {
	
	private final Map<String, Reference> references = new HashMap<>();
	
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

	public void clear(String prefix) {
		//If we delete as same time as we iterate this will throw a ConcurrentModificationException
		List<String> toDelete = new ArrayList<>();
		references.forEach((s, r) -> {
			if(s.startsWith(prefix) && !s.equals("space")) toDelete.add(s);
		});
		toDelete.forEach((s) -> references.remove(s));
	}
	
	public Map<String, ReferenceInfo> info() {
		Map<String, ReferenceInfo> map = new HashMap<>();
		references.forEach((n,r) -> map.put(n, r.info()));
		return map;
	}

}
