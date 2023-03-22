package handlers;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

import application.Util;
import btree.BinarySearchTree;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import stock.comparators.SortGain;
import stock.comparators.SortLoss;
import stock.comparators.SortSymbol;
import stock.comparators.SortVolume;
import stock.data.Stock;
import stock.data.StockDataPoint;
/**
 * 
 * Handles and retrieves all data regarding stocks and the heat map positions.
 *
 */
public class StockDataHandler {

	private static File[] files;

	private BinarySearchTree<Stock> stocks;
	private BinarySearchTree<Stock> gainers;
	private BinarySearchTree<Stock> losers;
	private BinarySearchTree<Stock> active;

	private HashMap<String, Rectangle> heatMapPositions;
	private HashMap<String, Stock> lookup;
	private HashMap<String, List<Stock>> stockSectors;

	private HashMap<String, List<Stock>> remainingStocksForEachSector;
	/**
	 * 
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public StockDataHandler() throws FileNotFoundException, IOException {

		loadStocks();
		loadSectors();
		readHeatMapPositions();
		createTrees();

	}

	private void createTrees() {
		stocks = new BinarySearchTree<Stock>(new SortSymbol());
		gainers = new BinarySearchTree<Stock>(new SortGain());
		losers = new BinarySearchTree<Stock>(new SortLoss());
		active = new BinarySearchTree<Stock>(new SortVolume());

		for (Stock stock : lookup.values()) {
			stocks.add(stock);

			if (stock.getSymbol().contentEquals("INDEXSP:.INX"))
				continue;

			gainers.add(stock);
			losers.add(stock);
			active.add(stock);
		}
	}

	private void loadSectors() {
		stockSectors = new HashMap<>();

		for (Stock stock : lookup.values()) {
			String sector = stock.getIndustry();
			if (!stockSectors.containsKey(sector)) {
				List<Stock> list = new ArrayList<Stock>();
				list.add(stock);
				stockSectors.put(sector, list);
			} else
				stockSectors.get(sector).add(stock);
		}
	}
	/**
	 * Finds the associated Stock with the given symbol
	 * @param symbol stock symbol
	 * @return the Stock instance with the given symbol
	 */
	public Stock find(String symbol) {
		return lookup.get(symbol);
	}
	/**
	 * Searches for the stock with an identical name to the input. If not found, it then searches for the first stock that contains the input.
	 * @param name name or substring of the Stock
	 * @return the Stock instance found with the given input
	 */
	public Stock searchName(String name) {
		List<Stock> stockList = stocks.toList();

		for (Stock stock : stockList)
			if (stock.getName().toLowerCase().contentEquals(name.toLowerCase()))
				return stock;

		for (Stock stock : stockList)
			if (stock.getName().toLowerCase().contains(name.toLowerCase()))
				return stock;

		return null;
	}
	/**
	 * Searches if a stock exists with input symbol
	 * @param symbol the stock symbol
	 * @return true if the stock exists otherwise false
	 */
	public boolean contains(String symbol) {
		return lookup.containsKey(symbol);
	}
	/**
	 * 
	 * @return the HashMap containing all the heat map positions and strings of each rectangle
	 */
	public HashMap<String, Rectangle> getHeatMapPositions() {
		return heatMapPositions;
	}

	private File downloadFile(String realFileId, String path) {
		
		File dir = new File("resources/");
		dir.mkdir();
		File file = new File(path);
		
		try {
			InputStream in = new URL("https://docs.google.com/spreadsheets/d/" + realFileId + "/export?format=csv")
					.openStream();
			OutputStream outputStream = new ByteArrayOutputStream();

			in.transferTo(outputStream);

			// Cast to be able to retrieve data as byte array
			ByteArrayOutputStream data = (ByteArrayOutputStream) outputStream;

			FileOutputStream fop = new FileOutputStream(file);

			if (!file.exists()) {
				file.createNewFile();
			}

			fop.write(data.toByteArray());

			// Close file output stream to prevent leak
			fop.close();

			// return the CSV file
		} catch (MalformedURLException e) {
			System.err.println("Bad URL");
			System.err.println("Unable to retrieve sheets");
			e.printStackTrace();
			System.exit(-1);
		} catch (IOException e) {
			System.err.println("Unable to write to file");
			e.printStackTrace();
			System.exit(-1);
		}

		return file;
	}

	private void retrieveFiles() {
		files = new File[Util.STOCK_DATA_FILE_IDS.length];

		for (int i = 0; i < files.length; i++) {
			files[i] = downloadFile(Util.STOCK_DATA_FILE_IDS[i], Util.RAW_STOCK_DATA_PATH + "stockdata" + i + ".csv");
		}
	}

	private void loadStocks() throws IOException, FileNotFoundException {
		lookup = new HashMap<>();
		retrieveFiles();

		BufferedReader br = new BufferedReader(new FileReader(files[0]));
		StringTokenizer st = null;

		for (int i = 0; i < 504; i++) {
			while (st == null || !st.hasMoreTokens())
				st = new StringTokenizer(br.readLine().trim(), ",\n");

			Stock s = new Stock(st.nextToken(), st.nextToken(), st.nextToken(), st.nextToken(), st.nextToken(),
					st.nextToken(), st.nextToken(), st.nextToken(), st.nextToken(), st.nextToken(), st.nextToken(),
					st.nextToken(), st.nextToken(), st.nextToken(), st.nextToken(), st.nextToken(), st.nextToken(),
					st.nextToken());

			lookup.put(s.getSymbol(), s);
		}

		ArrayList<StockDataPoint> historicalData = new ArrayList<>();

		while (br.ready()) {
			while (st == null || !st.hasMoreTokens())
				st = new StringTokenizer(br.readLine().trim(), ",\n");

			String test = st.nextToken();

			if (test.contentEquals("Date"))
				st = null;
			else {
				StockDataPoint dataPoint = new StockDataPoint(test, st.nextToken(), st.nextToken(), st.nextToken(),
						st.nextToken(), st.nextToken());

				historicalData.add(dataPoint);
			}
		}

		lookup.get("INDEXSP:.INX").setHistoricalData(historicalData);

		for (int stockDataSheet = 1; stockDataSheet <= 10; stockDataSheet++) {
			br = new BufferedReader(new FileReader(files[stockDataSheet]));

			LinkedList<String> stockList = new LinkedList<>();

			int max = (stockDataSheet == 10) ? 53 : 50;

			for (int i = 0; i < max; i++) {
				while (st == null || !st.hasMoreTokens())
					st = new StringTokenizer(br.readLine().trim(), ",\n");

				if (!st.hasMoreTokens())
					break;

				stockList.add(st.nextToken());
			}

			for (String stock : stockList) {
				historicalData = new ArrayList<>();

				for (int i = 0; i < 280; i++) {
					if (!br.ready())
						break;

					if (st == null || !st.hasMoreTokens())
						st = new StringTokenizer(br.readLine().trim(), ",\n");

					if (st == null || !st.hasMoreTokens())
						continue;

					String test = st.nextToken();

					if (test.contentEquals("#N/A"))
						break;

					if (test.contentEquals("Date"))
						st = null;
					else {
						StockDataPoint dataPoint = null;
						try {
							dataPoint = new StockDataPoint(test, st.nextToken(), st.nextToken(), st.nextToken(),
									st.nextToken(), st.nextToken());
						} catch (Exception e) {
							e.printStackTrace();
						}

						historicalData.add(dataPoint);
					}
				}

				lookup.get(stock).setHistoricalData(historicalData);
			}
		}

		br.close();
	}

	private void readHeatMapPositions() throws IOException {
		heatMapPositions = new HashMap<>();

		BufferedReader br = new BufferedReader(new InputStreamReader(this.getClass().getResourceAsStream(Util.RAW_HEATMAP_POSITIONS_PATH)));

		String currentSector = "";
		HashSet<String> heatMapStocks = new HashSet<>();

		while (br.ready()) {
			String name = br.readLine().trim();
			double height = Integer.parseInt(br.readLine().trim().replaceAll("[^0-9]", "")) / 720.0
					* (Util.getScreenHeight() - 20);
			double width = Integer.parseInt(br.readLine().trim().replaceAll("[^0-9]", "")) / 1280.0
					* Util.getScreenWidth();
			double left = Integer.parseInt(br.readLine().trim().replaceAll("[^0-9]", "")) / 1280.0
					* Util.getScreenWidth();
			double top = Integer.parseInt(br.readLine().trim().replaceAll("[^0-9]", "")) / 720.0
					* (Util.getScreenHeight() - 20);

			Rectangle rectangle = new Rectangle();
			rectangle.setX(left);
			rectangle.setY(top);
			rectangle.setWidth(width);
			rectangle.setHeight(height);
			rectangle.setStroke(Util.BACKGROUND_COLOR);
			rectangle.setStrokeWidth(0.5 / 720 * Util.getScreenHeight());

			if (name.contains("last")) {
				rectangle.setFill(calculateHeatMapRemainingColors(heatMapStocks, currentSector));
			} else if (!lookup.containsKey(name)) {
				currentSector = name;
				rectangle.setFill(Util.PRIMARY_COLOR);
			} else {
				rectangle.setFill(find(name).getChangeColor());
				heatMapStocks.add(name);
			}

			heatMapPositions.put(name, rectangle);
		}

		br.close();
	}

	private Color calculateHeatMapRemainingColors(HashSet<String> heatMapStocks, String sector) {
		if (remainingStocksForEachSector == null)
			remainingStocksForEachSector = new HashMap<>();

		long totalMarketCap = 0;

		if (!remainingStocksForEachSector.containsKey(sector)) {
			LinkedList<Stock> remainingStocks = new LinkedList<Stock>();

			for (Stock stock : stockSectors.get(sector)) {
				if (!heatMapStocks.contains(stock.getSymbol())) {
					totalMarketCap += stock.getMarketCap();
					remainingStocks.addLast(stock);
				}
			}

			remainingStocksForEachSector.put(sector, remainingStocks);
		}

		List<Stock> remainingStocks = remainingStocksForEachSector.get(sector);

		double changePercentage = 0;

		for (Stock stock : remainingStocks) {
			changePercentage += stock.getChangePercentage() * ((double) stock.getMarketCap() / totalMarketCap);
		}

		if (changePercentage >= 2)
			return Util.BIG_GAIN_COLOR;

		if (changePercentage >= 1)
			return Util.MEDIUM_GAIN_COLOR;

		if (changePercentage >= 0.2)
			return Util.SMALL_GAIN_COLOR;

		if (changePercentage > -0.2)
			return Util.SECONDARY_COLOR;

		if (changePercentage > -1)
			return Util.SMALL_LOSS_COLOR;

		if (changePercentage > -2)
			return Util.MEDIUM_LOSS_COLOR;

		return Util.BIG_LOSS_COLOR;
	}

	/**
	 * 
	 * @param sector
	 * @return remaining stocks for given sector
	 */
	public List<Stock> getRemainingStock(String sector) {
		return remainingStocksForEachSector.get(sector);
	}

	/**
	 * @return the stocks
	 */
	public BinarySearchTree<Stock> getStocks() {
		return stocks;
	}

	/**
	 * @return the gainers
	 */
	public BinarySearchTree<Stock> getGainers() {
		return gainers;
	}

	/**
	 * @return the losers
	 */
	public BinarySearchTree<Stock> getLosers() {
		return losers;
	}

	/**
	 * @return the active
	 */
	public BinarySearchTree<Stock> getActive() {
		return active;
	}

}
