package partie2.io;

import java.io.Serializable;

//Etat d'un client (derniere requete et reponse)
public record State(Request request, Response response) implements Serializable {

}