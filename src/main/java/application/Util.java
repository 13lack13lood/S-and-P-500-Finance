package application;

import javafx.geometry.Rectangle2D;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;
/**
 * The Util class contains utility variables such as color of certain elements as well as the path for various resources.
 * It also contains utility methods that other classes can access and use. 
 */
public final class Util {
	
	public final static Color BACKGROUND_COLOR = Color.rgb(18, 18, 18);
	public final static Color PRIMARY_COLOR = Color.rgb(29, 29, 29);
	public final static Color SECONDARY_COLOR = Color.rgb(43, 43, 43);
	
	public final static Color ACCENT_COLOR = Color.web("#BB86FC");

	public final static Color BIG_GAIN_COLOR = Color.rgb(97, 168, 97);
	public final static Color MEDIUM_GAIN_COLOR = Color.rgb(74, 114, 74);
	public final static Color SMALL_GAIN_COLOR = Color.rgb(51, 73, 51);

	public final static Color SMALL_LOSS_COLOR = Color.rgb(96, 59, 57);
	public final static Color MEDIUM_LOSS_COLOR = Color.rgb(136, 71, 67);
	public final static Color BIG_LOSS_COLOR = Color.rgb(255, 105, 97);
	
	/**
	 * Path to access the RawStockData folder
	 */
	public final static String RAW_STOCK_DATA_PATH = "resources/";
	/**
	 * Path to access the file containing the stored portfolio information
	 */
	public final static String RAW_PORTFOLIO_DATA_PATH = "resources/portfolio.properties";
	/**
	 * Path to access the file containing the stored watchlist information
	 */
	public final static String RAW_WATCHLIST_DATA_PATH = "resources/watchlist.properties";
	/**
	 * Path to access the file containing the heat map positions of all the rectangles
	 */
	public final static String RAW_HEATMAP_POSITIONS_PATH = "/heatMapPos.properties";

	/**
	 * The IDs of the Google Drive files
	 */
	public final static String[] STOCK_DATA_FILE_IDS = { "1n8rHWxsb5waTFD4xqv92rOZBaeeMYJawuc0nywugz50",
			"13_tHIPmIbvztKjshwj0inMlzWd486h4Ol1ZqCXAv-rU", "1UBmY_O4irgMMedWgRTFp1BdT7to9wdWf2Qb2RKmTKsw",
			"1DsvYwd8sPx-rCpCkHqPujtXoxztGESg0e3n9KZYbv4s", "14NWDp4LvPa4ais2otRcfXllhFWAZn8GdB2VAaOZyk7A",
			"17x4jEW7lKdON81kKqrGsMyGnyaRIOg5YHhJmVqPF70Y", "1e0ohS4dzwaxFxpSWpOwOlLByOMIvklN7VohCiQ8oAas",
			"1YYOfv-vxlicVQA3LzoIr-u3rXNFqs0CgiJEGxJuL71s", "1jD4QiG3ulVlQRI2F1TcQ9IZZRf-C47VJUBjvcpu1JAk",
			"1O1IX_LmrACQTUSHXa1d6t6CbEYFlS2vvxWOzc3F5m1g", "1ZTJTLLE6y8DZ0lk3UkbGfzQBPRiDC-9dzv1MzCzrjLI" };

	/**
	 * Utility array to convert month number to full name
	 */
	public final static String MONTHS[] = { "January", "February", "March", "April", "May", "June", "July", "August",
			"September", "October", "November", "December" };
	/**
	 * Utility array to convert month number to abreviated name
	 */
	public final static String MONTHS_ABREVIATED[] = { "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct",
			"Nov", "Dec" };
	
	/**
	 * Utility method for Main class
	 * @param stage from Main class
	 */
	public static void setStage(Stage stage) {
		stage.setTitle("S&P 500 Finance");

		stage.setX(Util.SCREEN_BOUNDS.getMinX());
		stage.setY(Util.SCREEN_BOUNDS.getMinY());
		stage.setWidth(Util.getScreenWidth());
		stage.setHeight(Util.getScreenHeight());

		stage.setResizable(false);
	}
	
	/**
	 * Utility variable for the  bounds of the screen
	 */
	public final static Rectangle2D SCREEN_BOUNDS = Screen.getPrimary().getVisualBounds();

	/**
	 * Utility method for screen width
	 * @return the width of the screen
	 */
	public static Double getScreenWidth() {
		return SCREEN_BOUNDS.getWidth();
	}

	/**
	 * Utility method for screen height
	 * @return the height of the screen
	 */
	public static Double getScreenHeight() {
		return SCREEN_BOUNDS.getHeight();
	}

	/**
	 * Utility method to check if the string is a valid Double.
	 * @param n input string
	 * @return the Double value if the string is a valid Double, otherwise minimum value of Double
	 */
	public static Double checkDouble(String n) {
		if (n.contentEquals("#N/A"))
			return Double.MIN_VALUE;
		return Double.parseDouble(n);
	}

	/**
	 * Utility method to check if the string is a valid Integer.
	 * @param n input string
	 * @return the Integer value if the string is a valid Integer, otherwise minimum value of Integer
	 */
	public static Integer checkInt(String n) {
		if (n.contentEquals("#N/A"))
			return Integer.MIN_VALUE;
		return Integer.parseInt(n);
	}
	
	/**
	 * Utility method to check if the string is a valid Long.
	 * @param n input string
	 * @return the Long value if the string is a valid Long, otherwise minimum value of Long
	 */
	public static Long checkLong(String n) {
		if (n.contentEquals("#N/A"))
			return Long.MIN_VALUE;
		return Long.parseLong(n);
	}
}
