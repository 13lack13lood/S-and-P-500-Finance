package ui;

import application.Util;
import handlers.UIHandler;
import javafx.geometry.VPos;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import ui.market.MarketTrends;
import ui.portfolio.Portfolio;
/**
 * 
 * The class for the UI elements of the pop up side menu of the program
 *
 */
public class SideMenu extends UserInterface {

	private Rectangle base, home, market, portfolio, watchlist, discover;
	private ImageView menuIcon, homeIcon, marketIcon, portfolioIcon, watchListIcon, discoverIcon;
	private Text homeText, marketText, portfolioText, watchlistText, discoverText;
	private Button menuButton;
	/**
	 * 
	 * @param stage
	 */
	public SideMenu(Stage stage) {
		super(stage);

		draw();
	}

	@Override
	protected void draw() {
		createBase();
		createHome();
		createMarket();
		createPortfolio();
		createWatchlist();
		createDiscover();
	}

	private void createBase() {
		base = new Rectangle(0, 0, 0.2 * stage.getWidth(), stage.getHeight());
		base.setFill(Util.PRIMARY_COLOR);

		DropShadow shadow = new DropShadow();
		shadow.setOffsetX(4);

		base.setEffect(shadow);

		base.setOnMouseEntered(null);

		menuIcon = new ImageView(new Image("menu.png"));
		menuIcon.setX(0.01 * stage.getWidth());
		menuIcon.setFitHeight(60.0 / 720 * stage.getHeight() * 0.6);
		menuIcon.setFitWidth(menuIcon.getFitHeight());
		menuIcon.setY((60.0 / 720 * stage.getHeight() - menuIcon.getFitHeight()) / 2.0);

		menuButton = new Button();
		menuButton.setId("menuButton");
		menuButton.setTranslateX(menuIcon.getX());
		menuButton.setTranslateY(menuIcon.getY());
		menuButton.setMinWidth(menuIcon.getFitWidth());
		menuButton.setMinHeight(menuIcon.getFitHeight());
		menuButton.setOnMouseClicked(event -> UIHandler.switchVisabile(SideMenu.class));

		add(base, menuIcon, menuButton);
	}

	private void createHome() {
		Group container = new Group();

		home = new Rectangle(-0.1 * Util.getScreenHeight(), 0.1 * Util.getScreenHeight(), 1.25 * base.getWidth(),
				0.08 * Util.getScreenHeight());
		home.setArcHeight(home.getHeight());
		home.setArcWidth(home.getHeight());
		home.setFill(Color.TRANSPARENT);

		homeIcon = new ImageView(new Image("home.png"));
		homeIcon.setX(menuIcon.getX());
		homeIcon.setFitHeight(menuIcon.getFitHeight());
		homeIcon.setFitWidth(menuIcon.getFitHeight());
		homeIcon.setY(home.getY() + home.getHeight() / 2.0 - homeIcon.getFitHeight() / 2.0);

		homeText = new Text("Home");
		homeText.setFill(Color.WHITE);
		homeText.setFont(Font.font("Open Sans", Math.round(0.5 * home.getHeight() / 1.333)));
		homeText.setX(homeIcon.getX() + homeIcon.getFitWidth() * 4 / 2.0);
		homeText.setTextOrigin(VPos.CENTER);
		homeText.setY(home.getY() + home.getHeight() / 2.0);

		container.getChildren().addAll(home, homeIcon, homeText);
		container.setOnMouseEntered(event -> home.setFill(Util.ACCENT_COLOR));
		container.setOnMouseExited(event -> home.setFill(Color.TRANSPARENT));
		container.setOnMouseClicked(event -> UIHandler.switchPage(HeatMap.class));

		add(container);

	}

	private void createMarket() {
		Group container = new Group();

		market = new Rectangle(home.getX(), home.getY() + home.getHeight(), home.getWidth(), home.getHeight());
		market.setArcHeight(market.getHeight());
		market.setArcWidth(market.getHeight());
		market.setFill(Color.TRANSPARENT);

		marketIcon = new ImageView(new Image("market.png"));
		marketIcon.setX(menuIcon.getX());
		marketIcon.setFitHeight(menuIcon.getFitHeight());
		marketIcon.setFitWidth(menuIcon.getFitHeight());
		marketIcon.setY(market.getY() + market.getHeight() / 2.0 - marketIcon.getFitHeight() / 2.0);

		marketText = new Text("Market Trends");
		marketText.setFill(Color.WHITE);
		marketText.setFont(Font.font("Open Sans", Math.round(0.5 * home.getHeight() / 1.333)));
		marketText.setX(marketIcon.getX() + marketIcon.getFitWidth() * 4 / 2.0);
		marketText.setTextOrigin(VPos.CENTER);
		marketText.setY(market.getY() + market.getHeight() / 2.0);

		container.getChildren().addAll(market, marketIcon, marketText);
		container.setOnMouseEntered(event -> market.setFill(Util.ACCENT_COLOR));
		container.setOnMouseExited(event -> market.setFill(Color.TRANSPARENT));
		container.setOnMouseClicked(event -> UIHandler.switchPage(MarketTrends.class));

		add(container);

	}

	private void createPortfolio() {
		Group container = new Group();

		portfolio = new Rectangle(home.getX(), market.getY() + market.getHeight(), home.getWidth(), home.getHeight());
		portfolio.setArcHeight(portfolio.getHeight());
		portfolio.setArcWidth(portfolio.getHeight());
		portfolio.setFill(Color.TRANSPARENT);

		portfolioIcon = new ImageView(new Image("portfolio.png"));
		portfolioIcon.setX(menuIcon.getX());
		portfolioIcon.setFitHeight(menuIcon.getFitHeight());
		portfolioIcon.setFitWidth(menuIcon.getFitHeight());
		portfolioIcon.setY(portfolio.getY() + portfolio.getHeight() / 2.0 - portfolioIcon.getFitHeight() / 2.0);

		portfolioText = new Text("Portfolio");
		portfolioText.setFill(Color.WHITE);
		portfolioText.setFont(Font.font("Open Sans", Math.round(0.5 * home.getHeight() / 1.333)));
		portfolioText.setX(portfolioIcon.getX() + portfolioIcon.getFitWidth() * 4 / 2.0);
		portfolioText.setTextOrigin(VPos.CENTER);
		portfolioText.setY(portfolio.getY() + portfolio.getHeight() / 2.0);

		container.getChildren().addAll(portfolio, portfolioIcon, portfolioText);
		container.setOnMouseEntered(event -> portfolio.setFill(Util.ACCENT_COLOR));
		container.setOnMouseExited(event -> portfolio.setFill(Color.TRANSPARENT));
		container.setOnMouseClicked(event -> UIHandler.switchPage(Portfolio.class));

		add(container);
	}

	private void createWatchlist() {
		Group container = new Group();

		watchlist = new Rectangle(home.getX(), portfolio.getY() + portfolio.getHeight(), home.getWidth(),
				home.getHeight());
		watchlist.setArcHeight(watchlist.getHeight());
		watchlist.setArcWidth(watchlist.getHeight());
		watchlist.setFill(Color.TRANSPARENT);

		watchListIcon = new ImageView(new Image("watchlist.png"));
		watchListIcon.setX(menuIcon.getX());
		watchListIcon.setFitHeight(menuIcon.getFitHeight());
		watchListIcon.setFitWidth(menuIcon.getFitHeight());
		watchListIcon.setY(watchlist.getY() + watchlist.getHeight() / 2.0 - watchListIcon.getFitHeight() / 2.0);

		watchlistText = new Text("Watchlist");
		watchlistText.setFill(Color.WHITE);
		watchlistText.setFont(Font.font("Open Sans", Math.round(0.5 * home.getHeight() / 1.333)));
		watchlistText.setX(watchListIcon.getX() + watchListIcon.getFitWidth() * 4 / 2.0);
		watchlistText.setTextOrigin(VPos.CENTER);
		watchlistText.setY(watchlist.getY() + watchlist.getHeight() / 2.0);

		container.getChildren().addAll(watchlist, watchListIcon, watchlistText);
		container.setOnMouseEntered(event -> watchlist.setFill(Util.ACCENT_COLOR));
		container.setOnMouseExited(event -> watchlist.setFill(Color.TRANSPARENT));
		container.setOnMouseClicked(event -> UIHandler.switchPage(Watchlist.class));

		add(container);
	}

	private void createDiscover() {
		Group container = new Group();

		discover = new Rectangle(home.getX(), watchlist.getY() + watchlist.getHeight(), home.getWidth(),
				home.getHeight());
		discover.setArcHeight(discover.getHeight());
		discover.setArcWidth(discover.getHeight());
		discover.setFill(Color.TRANSPARENT);

		discoverIcon = new ImageView(new Image("discover.png"));
		discoverIcon.setX(menuIcon.getX());
		discoverIcon.setFitHeight(menuIcon.getFitHeight());
		discoverIcon.setFitWidth(menuIcon.getFitHeight());
		discoverIcon.setY(discover.getY() + discover.getHeight() / 2.0 - discoverIcon.getFitHeight() / 2.0);

		discoverText = new Text("Discover");
		discoverText.setFill(Color.WHITE);
		discoverText.setFont(Font.font("Open Sans", Math.round(0.5 * home.getHeight() / 1.333)));
		discoverText.setX(discoverIcon.getX() + discoverIcon.getFitWidth() * 4 / 2.0);
		discoverText.setTextOrigin(VPos.CENTER);
		discoverText.setY(discover.getY() + discover.getHeight() / 2.0);

		container.getChildren().addAll(discover, discoverIcon, discoverText);
		container.setOnMouseEntered(event -> discover.setFill(Util.ACCENT_COLOR));
		container.setOnMouseExited(event -> discover.setFill(Color.TRANSPARENT));
		container.setOnMouseClicked(event -> UIHandler.switchPage(Discover.class));

		add(container);
	}

}
