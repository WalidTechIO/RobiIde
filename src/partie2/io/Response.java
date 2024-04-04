package partie2.io;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public record Response(String feedback, String image, String expr, String environment, Map<String, List<String>> scriptsMap) implements Serializable {

}
