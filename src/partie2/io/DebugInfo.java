package partie2.io;

import java.io.Serializable;
import java.util.Map;

//Information de debuggage transmise au client
public record DebugInfo(String expr, Map<String, ReferenceInfo> env) implements Serializable {

}
