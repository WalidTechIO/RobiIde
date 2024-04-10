package partie2.utils;

import javafx.scene.Scene;

//Wrap a javafx scene and it's controller
public record SceneWrapper<T>(Scene scene, T controller) {

}
