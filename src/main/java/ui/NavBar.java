package ui;

import application.Util;
import handlers.StockDataHandler;
import handlers.UIHandler;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
/**
 * 
 * The class for the UI elements of the navigation bar on the top of the program 
 *
 */
public class NavBar extends UserInterface {

	private Rectangle topBar, searchBar;
	private Text title;
	private Button menuButton;
	private ImageView menuIcon, searchIcon;
	private TextField searchBarField;

	private StockDataHandler stockHandler;
	/**
	 * 
	 * @param stage
	 * @param handler
	 */
	public NavBar(Stage stage, StockDataHandler handler) {
		super(stage);
		this.stockHandler = handler;
		draw();
	}

	@Override
	protected void draw() {
		createTopBar();
		createSearchBar();
		createTitle();
		createMenuIcon();
		createButton();
		createSearchIcon();
		createSearchTextField();

		add(topBar, searchBar, title, menuIcon, searchIcon, searchBarField, menuButton);
	}

	private void createTopBar() {
		topBar = new Rectangle();
		topBar.setX(0);
		topBar.setY(0);
		topBar.setWidth(stage.getWidth());
		topBar.setHeight(60.0 / 720 * stage.getHeight());
		topBar.setFill(Util.PRIMARY_COLOR);

		DropShadow topBarShadow = new DropShadow();
		topBarShadow.setOffsetY(4);

		topBar.setEffect(topBarShadow);
	}

	private void createSearchBar() {
		searchBar = new Rectangle();
		searchBar.setX(0.45 * stage.getWidth());
		searchBar.setWidth(0.5 * stage.getWidth());
		searchBar.setHeight(0.7 * topBar.getHeight());
		searchBar.setY((topBar.getHeight() - searchBar.getHeight()) / 2.0);
		searchBar.setFill(Util.SECONDARY_COLOR);
		searchBar.setArcHeight(searchBar.getHeight());
		searchBar.setArcWidth(searchBar.getHeight());

		DropShadow searchBarShadow = new DropShadow();
		searchBarShadow.setOffsetY(2);
		searchBarShadow.setRadius(8);

		searchBar.setEffect(searchBarShadow);
	}

	private void createTitle() {
		title = new Text();
		title.setText("S&P 500 Finance");
		title.setX(0.07 * stage.getWidth());
		title.setFont(Font.font("Open Sans", Math.round(topBar.getHeight() * 0.7 / 1.3333)));
		title.setY(topBar.getHeight() * 0.7);
		title.setFill(Color.WHITE);
	}

	private void createMenuIcon() {
		menuIcon = new ImageView(new Image("menu.png"));
		menuIcon.setX(0.01 * stage.getWidth());
		menuIcon.setFitHeight(topBar.getHeight() * 0.6);
		menuIcon.setFitWidth(menuIcon.getFitHeight());
		menuIcon.setY((topBar.getHeight() - menuIcon.getFitHeight()) / 2.0);
	}

	private void createButton() {
		menuButton = new Button();
		menuButton.setId("menuButton");
		menuButton.setTranslateX(menuIcon.getX());
		menuButton.setTranslateY(menuIcon.getY());
		menuButton.setMinWidth(menuIcon.getFitWidth());
		menuButton.setMinHeight(menuIcon.getFitHeight());
		menuButton.setOnMouseClicked(event -> {
			UIHandler.switchVisabile(SideMenu.class);
		});
	}

	private void createSearchIcon() {
		searchIcon = new ImageView(new Image("search.png"));
		searchIcon.setX(searchBar.getX() + searchBar.getHeight() / 3.0);
		searchIcon.setFitHeight(topBar.getHeight() * 0.6);
		searchIcon.setFitWidth(searchIcon.getFitHeight());
		searchIcon.setY((topBar.getHeight() - searchIcon.getFitHeight()) / 2.0);
	}

	private void createSearchTextField() {
		searchBarField = new TextField();
		searchBarField.setPromptText("Search for stocks");
		searchBarField.setAlignment(Pos.CENTER_LEFT);
		searchBarField.setMinWidth(searchBar.getWidth() * 0.88);
		searchBarField.setMaxWidth(searchBar.getWidth() * 0.88);
		searchBarField.setMaxHeight(searchBar.getWidth() * 0.8);
		searchBarField.setFont(Font.font("Open Sans", Math.round(searchBar.getHeight() * 0.7 / 1.3333)));
		searchBarField.setTranslateX(searchIcon.getX() + 0.05 * searchBar.getWidth());
		searchBarField.setTranslateY(searchBar.getY() - (searchIcon.getY() - searchBar.getY()));
		searchIcon.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				String stock = searchBarField.getText().trim();

				if (stock.isEmpty())
					return;

				if (stockHandler.contains(stock.toUpperCase()))
					UIHandler.switchToStock(stockHandler.find(stock.toUpperCase()));
				else if (stockHandler.searchName(stock) != null)
					UIHandler.switchToStock(stockHandler.searchName(stock));

			}
		});
	}
}
