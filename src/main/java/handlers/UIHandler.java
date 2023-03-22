package handlers;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;

import javafx.scene.Group;
import javafx.stage.Stage;
import stock.data.Stock;
import ui.Discover;
import ui.HeatMap;
import ui.NavBar;
import ui.SideMenu;
import ui.StockDetails;
import ui.UserInterface;
import ui.Watchlist;
import ui.market.MarketTrends;
import ui.portfolio.Portfolio;
/**
 * 
 * Handles all the pages of the program and transitions between different menus
 *
 */
public class UIHandler {
	private static Group root = new Group();;

	private static HashMap<Class<? extends UserInterface>, Boolean> menuHandler = new HashMap<>();;
	private static HashMap<Class<? extends UserInterface>, UserInterface> instances = new HashMap<>();
	/**
	 * 
	 * @param stage
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public UIHandler(Stage stage) throws FileNotFoundException, IOException {

		StockDataHandler handler = new StockDataHandler();

		UserInterface stockDetails = new StockDetails(stage, handler);
		UserInterface navBar = new NavBar(stage, handler);
		UserInterface heatMap = new HeatMap(stage, handler);
		UserInterface sideMenu = new SideMenu(stage);
		UserInterface marketTrends = new MarketTrends(stage, handler);
		UserInterface portfolio = new Portfolio(stage, handler);
		UserInterface watchlist = new Watchlist(stage, handler);
		UserInterface discover = new Discover(stage, handler);

		menuHandler.put(StockDetails.class, false);
		menuHandler.put(NavBar.class, true);
		menuHandler.put(HeatMap.class, true);
		menuHandler.put(SideMenu.class, false);
		menuHandler.put(MarketTrends.class, false);
		menuHandler.put(Portfolio.class, false);
		menuHandler.put(Watchlist.class, false);
		menuHandler.put(Discover.class, false);

		instances.put(StockDetails.class, stockDetails);
		instances.put(NavBar.class, navBar);
		instances.put(HeatMap.class, heatMap);
		instances.put(SideMenu.class, sideMenu);
		instances.put(MarketTrends.class, marketTrends);
		instances.put(Portfolio.class, portfolio);
		instances.put(Watchlist.class, watchlist);
		instances.put(Discover.class, discover);

		checkVisability();
	}

	private static void checkVisability() {
		for (Class<? extends UserInterface> ui : menuHandler.keySet()) {
			if (menuHandler.get(ui)) {
				if (!root.getChildren().contains(instances.get(ui).getRoot()))
					root.getChildren().add(instances.get(ui).getRoot());
			} else
				root.getChildren().remove(instances.get(ui).getRoot());
		}
	}

	/**
	 * Method to switch the visibility of a UI element
	 * @param ui the UserInterface to switch visibility
	 */
	public static void switchVisabile(Class<? extends UserInterface> ui) {
		menuHandler.put(ui, !menuHandler.get(ui));
		checkVisability();
	}
	/**
	 * Switches the page to the desired menu
	 * @param ui the UserInterface to switch to
	 */
	public static void switchPage(Class<? extends UserInterface> ui) {
		for (Class<? extends UserInterface> menu : menuHandler.keySet()) {
			if (!menu.equals(NavBar.class))
				menuHandler.put(menu, false);
		}

		menuHandler.put(ui, true);

		checkVisability();
	}
	/**
	 * Adds the stock to the watchlist
	 * @param stock
	 */
	public static void addWatchlistStock(Stock stock) {
		((Watchlist) instances.get(Watchlist.class)).addStock(stock);
	}

	/**
	 * Switches the current page to the stock details menu
	 * @param stock
	 */
	public static void switchToStock(Stock stock) {
		((StockDetails) instances.get(StockDetails.class)).setCurrent(stock);

		switchPage(StockDetails.class);
	}
	/**
	 * 
	 * @return root node 
	 */
	public Group getRoot() {
		return root;
	}
}
