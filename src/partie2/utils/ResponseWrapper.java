package partie2.utils;

import java.io.Serializable;

import partie2.io.Response;

public record ResponseWrapper(Response resp, int delay) implements Serializable {

}
