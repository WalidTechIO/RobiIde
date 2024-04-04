package partie2.utils;

import java.util.ArrayList;
import java.util.List;

import stree.parser.SNode;

public class NodeUtils {
	
	public static String nodeToFormattedString(SNode node, String indentationChars) {
		return stringToFormattedString(nodeToString(node), indentationChars);
	}
	
	public static String nodeToFormattedString(SNode node) {
		return nodeToFormattedString(node, "  ");
	}
	
	public static String stringToFormattedString(String base) {
		return stringToFormattedString(base, "  ");
	}
	
	public static String stringToFormattedString(String base, String indentationChars) {
		int indentLevel = 0;
		String finalExpr = "";
		
		for(int i=0; i<base.length(); i++) {
			Character c = base.charAt(i);
			if(c.equals('(')) {
				finalExpr += "\n";
				for(int j=0; j<indentLevel; j++) finalExpr += indentationChars;
				finalExpr += "(";
				indentLevel++;
				continue;
			}
			if(c.equals(')')) {
				indentLevel--;
				//finalExpr += "\n";
				for(int j=0; j<indentLevel; j++) finalExpr += indentationChars;
				finalExpr += ")\n";
				continue;
			}
			finalExpr += c;
		}
		indentLevel = 0;
		String lines[] = finalExpr.split("\n");
		List<String> newLines = new ArrayList<>();
		for(String line : lines) {
			if(line.contains("(")) indentLevel++;
			if(line.contains(")")) indentLevel--;
			if(!line.contains("(") && !line.contains(")")) {
				String newLine = "";
				for(int j=0; j<indentLevel; j++) newLine += indentationChars;
				newLines.add(newLine + line);
			} else {
				newLines.add(line);
			}
		}
		
		return newLines.stream()
				.reduce((a,s) -> a+=s+"\n")
				.get();
	}
	
	public static String nodeToString(SNode node) {
		if(node.isLeaf()) {
			return node.contents() + " ";
		} else {
			String res = "( ";
			for(SNode children : node.children()) res += nodeToString(children);
			return res + ") ";
		}
	}

}
