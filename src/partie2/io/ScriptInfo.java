package partie2.io;

import java.io.Serializable;

public record ScriptInfo(Integer nbParams, String expr, String proto) implements Serializable {

}
