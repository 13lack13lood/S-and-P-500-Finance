package ui;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.stage.Stage;
/**
 * 
 * A user interface must be able to draw and display something on the user's screen.
 * All classes that display any user interface elements must implement this class
 *
 */
public abstract class UserInterface {
	protected Stage stage;
	protected Group root;
	/**
	 * 
	 * @param stage
	 */
	public UserInterface(Stage stage) {
		this.stage = stage;
		root = new Group();

	}

	protected abstract void draw();

	protected void add(Node... nodes) {
		for (Node node : nodes)
			root.getChildren().add(node);
	}

	/**
	 * @return the root
	 */
	public Group getRoot() {
		return root;
	}

}
