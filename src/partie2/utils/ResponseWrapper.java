package partie2.utils;

import java.io.Serializable;

import partie2.io.Response;

//Wrap interpreter response and a delay
public record ResponseWrapper(Response resp, int delay) implements Serializable {

}
