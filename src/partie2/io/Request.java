package partie2.io;

import java.io.Serializable;

public record Request(RequestType type, Program program) implements Serializable {

	public static enum RequestType {
			EXE,
			PROG
	}
	
}
