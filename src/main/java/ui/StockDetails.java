package ui;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import handlers.StockDataHandler;
import javafx.stage.Stage;
import stock.data.Stock;
import ui.market.LineGraph;
/**
 * 
 * The class for the UI elements of the page containing detailed information on each stock
 *
 */
public class StockDetails extends UserInterface {

	private StockDataHandler stockHandler;

	private HashMap<Stock, LineGraph> graphs;

	public StockDetails(Stage stage, StockDataHandler stockHandler) throws FileNotFoundException, IOException {
		super(stage);

		this.stockHandler = stockHandler;

		generateGraphs();
	}

	@Override
	protected void draw() {

	}

	private void generateGraphs() {
		graphs = new HashMap<Stock, LineGraph>();
		List<Stock> stocks = stockHandler.getStocks().toList();
		for (Stock stock : stocks) {
			LineGraph graph = new LineGraph(stage, stock);

			graphs.put(stock, graph);
		}
	}

	public void setCurrent(Stock stock) {
		root.getChildren().clear();
		LineGraph g = graphs.get(stockHandler.find(stock.getSymbol()));
		add(g.getRoot());
	}

}
