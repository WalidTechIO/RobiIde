package partie2.io;

import java.io.Serializable;

public record Program(Mode mode, String contenu) implements Serializable {

	public static enum Mode implements Serializable {
		S_B_S, //Step by step
		DIRECT
	}
	
}
