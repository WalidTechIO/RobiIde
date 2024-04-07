# Telecharger JavaFX
- [Lien](https://gluonhq.com/products/javafx/)

# Telecharger fasterxml.jackson

- [Lien](https://central.sonatype.com/namespace/com.fasterxml.jackson.core)

# Lancer le projet
- Creer un dossier lib à la racine du projet
- Creer dans ce dossier lib 2 dossiers: javafx et fasterxml
- Décompressez JavaFX SDK (Windows ou Linux) dans lib/javafx
- Mettez jackson-core.jar, jackson-anotation.jar et jackson-databind.jar dans lib/fasterxml
- Si vous êtes sur Linux, dans les scripts il faut remplacer les ; dans le flag -cp par des :
- Lancer le serveur et le client avec les scripts présents dans le dossier resources

# Contenu du projet

## Serveur

- Un serveur d'interpretation ROBI
	- Mode Socket TCP
		- Mode un client a la fois
		- Mode multi-client
		- Rendu graphique effectuer cote client
		- Recois des requetes sous forme de chaine JSON renvoie les reponses en chaine JSON egalement
	- Mode Endpoint HTTP
		- /render
			- Effectue un rendu cote serveur !
			- Ne supporte pas le mode Step by Step
			- Genere un script de setTimeout s'executant au bon moment
			- Chaque setTimeout change la source d'une balise img d'id renderer recuperer depuis le DOM
			- La source de l'image est l'image encodée en base64
			- Image new peut prendre en argument une image au format base 64 plutot qu'un chemin
			- La reponse est une page HTML contenant le script et une balise img d'id renderer
			
		- /world
			- Cet endpoint permet de developper des clients fonctionnant en mode Step by Step
			- Renvoie en JSON une liste de reponses
			- Ces reponses sont associées a des delai correspondant au temps apres lequel le programme a 'rendu' ce monde
			- Chaque reponse est composée:
				- D'un feedback
				- D'un monde courant (Arbre d'objects graphique serveur)
				- De la derniere expression appelee
				- De l'environnement (references, primitives, scripts)
		
## Clients
- 3 Clients pour le serveur
	- JAVA:
		- Gere le mode Step by Step
		- Permet de consulter l'environnement(references, scripts et primitives des references)
		- Permet de consulter la pile des appels de S-expr
		- Permet d'exporter la derniere requete/response au format JSON et de les consulter dans une UI
		- Permet de sauvegarder et charger un script robi
		- Lors du rendu les images sont récupérés sur la machine cliente et non le serveur
	- Web Simple:
		- Pas de mode etape par etape
		- Rendu cote serveur
		- Pour transmettre les images au serveur un systeme de template et de pre-compilation du programme ROBI a ete mis en place
	- Web React:
		- Comme le Web Simple mais encapsuler dans une app react (evolutif)