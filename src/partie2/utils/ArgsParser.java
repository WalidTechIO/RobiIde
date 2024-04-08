package partie2.utils;

import java.util.HashMap;
import java.util.Map;

public class ArgsParser {
	
	private Map<Character, Object> parsedFlags = new HashMap<>();
	private Map<Character, Character> recognizedFlags = new HashMap<>();
	
	public ArgsParser(String flagDescriptor) {
		if(flagDescriptor.length() < 2 || flagDescriptor.length() % 2 == 1) throw new IllegalArgumentException("Flag Descriptor must have even number of chars");
		for(int i = 0; i<flagDescriptor.length(); i+=2) {
			recognizedFlags.put(flagDescriptor.charAt(i), flagDescriptor.charAt(i+1));
		}
	}
	
	public ArgsParser parse(String args[]) {
		for(int i=0; i<args.length; i+=2) {
			if(args[i].startsWith("-") && args[i].length() == 2 && recognizedFlags.keySet().contains(args[i].charAt(1))) {
				switch(recognizedFlags.get(args[i].charAt(1))) {
				case 'n': 
					parsedFlags.put(args[i].charAt(1), new Object());
					i--;
					break;
				case 'b': 
					if(args[i+1].equals("true")) parsedFlags.put(args[i].charAt(1), (Boolean)true);
					if(args[i+1].equals("false")) parsedFlags.put(args[i].charAt(1), (Boolean)false);
					break;
				case 'i':
					try {
						parsedFlags.put(args[i].charAt(1), Integer.parseInt(args[i+1]));
					} catch(NumberFormatException ignored) {}
					break;
				}
			}
		}
		return this;
	}
		
	public boolean hasParsed(Character flag) {
		return parsedFlags.containsKey(flag);
	}
	
	public Object get(Character flag) {
		return parsedFlags.get(flag);
	}

}
