package partie2.server;

import partie2.io.Response;

//Interface permettant d'utiliser le polymorphisme pour lier l'interpreter au serveur http ou tcp
//bien que leur fonctionnement soit totalement different
public interface canSendResponse {
	
	void sendResponse(Response reponse);

}
