package ui;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.List;
import java.util.StringTokenizer;

import application.Util;
import btree.BinarySearchTree;
import handlers.StockDataHandler;
import handlers.UIHandler;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.VPos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import stock.comparators.SortSymbol;
import stock.data.Stock;
/**
 * 
 * The class for the Watchlist menu that creates all the UI elements of the page
 *
 */
public class Watchlist extends UserInterface {

	private StockDataHandler stockHandler;

	private BinarySearchTree<Stock> stocks;

	private Pane pane;

	private double rectHeight = 0.09 * Util.getScreenHeight();
	private double rectWidth = 0.72 * Util.getScreenWidth();
	private double rectX = Util.getScreenWidth() / 2.0 - rectWidth / 2.0;
	private double rectY = 0.12 * Util.getScreenHeight();
	/**
	 * 
	 * @param stage
	 * @param stockHandler
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public Watchlist(Stage stage, StockDataHandler stockHandler) throws FileNotFoundException, IOException {
		super(stage);

		this.stockHandler = stockHandler;

		stocks = new BinarySearchTree<Stock>(new SortSymbol());

		pane = new Pane();

		draw();
	}

	@Override
	protected void draw() {
		readWatchListData();

		generateStocks();

		pane.addEventHandler(ScrollEvent.SCROLL, getPaneScroll());
		pane.requestFocus();

		Rectangle r = new Rectangle(1.1 * rectWidth, Util.getScreenHeight() - 0.17 * Util.getScreenHeight());
		r.setLayoutX(rectX);
		r.setLayoutY(rectY);

		pane.setClip(r);

		add(pane);
	}

	private void generateStocks() {
		List<Stock> stockList = stocks.toList();

		double y = rectY;

		DecimalFormat format = new DecimalFormat("0.00");

		for (int stockListIndex = 0; stockListIndex < stockList.size(); y += 1.1 * rectHeight, stockListIndex++) {
			Stock stock = stockList.get(stockListIndex);

			Rectangle rect = new Rectangle(rectX, y, rectWidth, rectHeight);
			rect.setFill(Util.PRIMARY_COLOR);
			rect.setArcHeight(20 / 720.0 * Util.getScreenHeight());
			rect.setArcWidth(20 / 720.0 * Util.getScreenHeight());

			DropShadow shadow = new DropShadow();
			shadow.setOffsetX(3);
			shadow.setOffsetY(5);

			rect.setEffect(shadow);

			Text symbol = new Text(stock.getSymbol());
			symbol.setFont(Font.font("Open Sans", 0.7 * rect.getHeight() / 1.333));
			symbol.setX(rectX + 0.02 * rectWidth);
			symbol.setTextOrigin(VPos.CENTER);
			symbol.setY(rect.getY() + rectHeight / 2.0);
			symbol.setFill(Color.WHITE);

			Text name = new Text(stock.getName());
			name.setFont(Font.font("Open Sans", 0.35 * rect.getHeight() / 1.333));
			name.setX(0.405 * rectWidth);
			name.setTextOrigin(VPos.CENTER);
			name.setY(rect.getY() + rectHeight / 2.0);
			name.setFill(Color.WHITE);

			Text price = new Text("$" + Double.toString(stock.getPrice()));
			price.setFont(name.getFont());
			price.setX(rectX + 0.63 * rectWidth);
			price.setTextOrigin(VPos.CENTER);
			price.setY(name.getY());
			price.setFill(Color.WHITE);

			String changeString = "";

			if (stock.getChange() >= 0)
				changeString = "+$" + format.format(stock.getChange());
			else
				changeString = "-$" + format.format(stock.getChange()).substring(1);

			Text change = new Text(changeString);
			change.setFont(price.getFont());
			change.setX(rectX + 0.73 * rectWidth);
			change.setTextOrigin(VPos.CENTER);
			change.setY(price.getY());
			change.setFill(Color.WHITE);

			String percentChangeString = "";

			if (stock.getChange() >= 0)
				percentChangeString = "+" + format.format(stock.getChangePercentage()) + "%";
			else
				percentChangeString = format.format(stock.getChangePercentage()) + "%";

			Text percentChange = new Text(percentChangeString);
			percentChange.setFont(price.getFont());
			percentChange.setX(rectX + 0.83 * rectWidth);
			percentChange.setTextOrigin(VPos.CENTER);
			percentChange.setY(price.getY());
			percentChange.setFill(Color.WHITE);

			Rectangle percentChangeRect = new Rectangle(
					percentChange.getX() + 0.5 * percentChange.getLayoutBounds().getWidth() - 0.04 * rect.getWidth(),
					percentChange.getY() - 0.67 * percentChange.getLayoutBounds().getHeight(), 0.08 * rect.getWidth(),
					1.34 * percentChange.getLayoutBounds().getHeight());

			percentChangeRect.setFill(stock.getChangeColor());
			percentChangeRect.setArcHeight(percentChangeRect.getHeight());
			percentChangeRect.setArcWidth(percentChangeRect.getHeight());

			Group clickArea = new Group(symbol, name, price, change, percentChangeRect, percentChange);
			clickArea.setOnMouseClicked(event -> UIHandler.switchToStock(stock));

			Group delete = new Group();

			ImageView deleteIcon = new ImageView(new Image("x.png"));
			deleteIcon.setX(rectX + rectWidth - 0.8 * rectHeight);
			deleteIcon.setFitWidth(0.55 * rectHeight);
			deleteIcon.setFitHeight(deleteIcon.getFitWidth());
			deleteIcon.setY(y + 0.5 * rectHeight - 0.5 * deleteIcon.getFitHeight());

			Circle deleteCircle = new Circle(deleteIcon.getX() + 0.5 * deleteIcon.getFitWidth(),
					deleteIcon.getY() + 0.5 * deleteIcon.getFitHeight(), 0.5 * deleteIcon.getFitWidth());
			deleteCircle.setFill(Util.ACCENT_COLOR);

			delete.getChildren().addAll(deleteCircle, deleteIcon);
			delete.setOnMouseClicked(new EventHandler<MouseEvent>() {

				@Override
				public void handle(MouseEvent event) {
					stocks.delete(stock);

					refresh();
				}

			});

			pane.getChildren().addAll(rect, clickArea, delete);
		}

	}

	private EventHandler<ScrollEvent> getPaneScroll() {
		EventHandler<ScrollEvent> handler = new EventHandler<ScrollEvent>() {

			@Override
			public void handle(ScrollEvent event) {
				ObservableList<Node> objects = pane.getChildren();

				Rectangle first = (Rectangle) objects.get(0);
				Rectangle last = (Rectangle) objects.get(objects.size() - 3);

				if (event.getDeltaY() > 0 && first.getY() + first.getTranslateY() < rectY) {
					for (Node e : objects) {
						e.setTranslateY(e.getTranslateY() + event.getDeltaY());
					}
				}

				if (event.getDeltaY() < 0
						&& last.getY() + last.getTranslateY() + last.getHeight() >= 0.93 * Util.getScreenHeight()) {
					for (Node e : objects) {
						e.setTranslateY(e.getTranslateY() + event.getDeltaY());
					}
				}
			}

		};

		return handler;
	}

	private void readWatchListData() {
		try {
			BufferedReader br = null;
			
			File file = new File(Util.RAW_WATCHLIST_DATA_PATH);

			if(file.exists()) {
				br = new BufferedReader(new FileReader(file));
			} else {
				br = new BufferedReader(new InputStreamReader(this.getClass().getResourceAsStream("/watchlist.properties")));
			}
						
			StringTokenizer st = null;

			while (br.ready()) {
				while (st == null || !st.hasMoreTokens())
					st = new StringTokenizer(br.readLine().trim());

				Stock stock = stockHandler.find(st.nextToken());

				stocks.add(stock);
			}
			
			writeData();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void writeData() {
		try {
			File file = new File(Util.RAW_WATCHLIST_DATA_PATH);
			FileWriter fw = new FileWriter(file, false);
			for (Stock stock : stocks.toList()) {
				fw.write(stock.getSymbol() + "\n");
			}

			fw.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void refresh() {
		writeData();

		stocks = new BinarySearchTree<Stock>(new SortSymbol());
		pane.getChildren().clear();
		root.getChildren().remove(pane);

		root.getChildren().clear();

		draw();
	}
	/**
	 * Adds a stock to the watchlist and refreshes the page so that the stock is visually added
	 * @param stock
	 */
	public void addStock(Stock stock) {
		stocks.add(stock);
		refresh();
	}
}
