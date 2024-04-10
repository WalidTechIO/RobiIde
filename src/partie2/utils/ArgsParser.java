package partie2.utils;

import java.util.HashMap;
import java.util.Map;

public class ArgsParser {
	
	private Map<Character, Object> parsedFlags = new HashMap<>();
	private Map<Character, Character> recognizedFlags = new HashMap<>();
	
	/**
	 * Constructeur
	 * @param flagDescriptor chaine de caractere au format x1y1x2y2 ou les x sont des caracteres 
	 * representant un drapeau et les y le caracte representant la donnee associée au drapeau
	 */
	public ArgsParser(String flagDescriptor) {
		if(flagDescriptor.length() < 2 || flagDescriptor.length() % 2 == 1) throw new IllegalArgumentException("Flag Descriptor must have even number of chars");
		for(int i = 0; i<flagDescriptor.length(); i+=2) {
			recognizedFlags.put(flagDescriptor.charAt(i), flagDescriptor.charAt(i+1));
		}
	}
	
	/**
	 * Parse les arguments selon la chaine fournie au constructeur
	 * @param args
	 */
	public ArgsParser parse(String args[]) {
		for(int i=0; i<args.length; i+=2) {
			if(args[i].startsWith("-") && args[i].length() == 2 && recognizedFlags.keySet().contains(args[i].charAt(1))) {
				switch(recognizedFlags.get(args[i].charAt(1))) {
				case 'n': 
					parsedFlags.put(args[i].charAt(1), null);
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
	
	/**
	 * Indique si un drapeau a ete parsé
	 */
	public boolean hasParsed(Character flag) {
		return parsedFlags.containsKey(flag);
	}
	
	/**
	 * Renvoie la valeur associée au drapeau si elle existe
	 */
	public Object get(Character flag) {
		return parsedFlags.get(flag);
	}

}
