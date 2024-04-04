package partie2.io;

import java.io.Serializable;

public record Response(String feedback, String image, String expr, String environment) implements Serializable {

}
