package application;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import handlers.UIHandler;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
/**
 * This is the class that is executed for the program to run
 */
public class Main extends Application {

	@Override
	public void start(Stage stage) {
		try {
			Util.setStage(stage);
			
			System.out.println("Loading");
			
			
			UIHandler uiHandler = new UIHandler(stage);

			Scene scene = new Scene(uiHandler.getRoot(), Util.BACKGROUND_COLOR);
			scene.getStylesheets().add("style.css");

			stage.setScene(scene);
			stage.show();

		} catch (Exception e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}
