package partie2.io;

import java.io.Serializable;

public record State(Request request, Response response) implements Serializable {

}