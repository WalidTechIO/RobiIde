package partie2.utils;

import javafx.scene.Scene;

public record SceneWrapper<T>(Scene scene, T controller) {

}
