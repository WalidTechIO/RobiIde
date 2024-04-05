package partie2.io;

import java.io.Serializable;

import partie2.io.graphics.GWorld;

public record Response(String feedback, GWorld world, DebugInfo info) implements Serializable {

}
