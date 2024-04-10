package partie2.io;

import java.io.Serializable;

//Information d'un script
public record ScriptInfo(Integer nbParams, String expr, String proto) implements Serializable {

}
