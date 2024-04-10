package partie2.io;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

//Represente les informations d'une reference
public record ReferenceInfo(String className, List<String> primitives, Map<String, ScriptInfo> scripts) implements Serializable {

}
