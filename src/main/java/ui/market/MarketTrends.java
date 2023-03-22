package ui.market;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;

import application.Util;
import handlers.StockDataHandler;
import javafx.event.EventHandler;
import javafx.geometry.VPos;
import javafx.scene.Group;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import ui.UserInterface;
/**
 * 
 * The class for the Market Trends menu that creates all the UI elements of the page
 *
 */
public class MarketTrends extends UserInterface {

	private StockDataHandler stockLookup;

	private enum Button {
		INDEX, ACTIVE, GAINERS, LOSERS
	}

	private Button currentSelected;

	private HashMap<Button, UserInterface> menuHandler;
	/**
	 * 
	 * @param stage
	 * @param stockHandler
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public MarketTrends(Stage stage, StockDataHandler stockHandler) throws FileNotFoundException, IOException {
		super(stage);
		stockLookup = stockHandler;

		menuHandler = new HashMap<>();
		currentSelected = Button.INDEX;

		draw();
	}

	@Override
	protected void draw() {
		addButtons();

		LineGraph graph = new LineGraph(stage, stockLookup.find("INDEXSP:.INX"));
		MarketStockList active = new MarketStockList(stockLookup.getActive(), stage);
		MarketStockList gainers = new MarketStockList(stockLookup.getGainers(), stage);
		MarketStockList losers = new MarketStockList(stockLookup.getLosers(), stage);

		menuHandler.put(Button.INDEX, graph);
		menuHandler.put(Button.ACTIVE, active);
		menuHandler.put(Button.GAINERS, gainers);
		menuHandler.put(Button.LOSERS, losers);

		add(graph.getRoot());

	}

	private void addButtons() {
		Group buttons = new Group();

		DropShadow shadow = new DropShadow();
		shadow.setOffsetX(6);
		shadow.setOffsetY(3);

		Group indexButton = new Group();

		Rectangle indexBase = new Rectangle();
		indexBase.setX(Util.getScreenWidth() * 0.23125);
		indexBase.setY(Util.getScreenHeight() * 0.125);
		indexBase.setHeight(Util.getScreenHeight() * 0.054166);
		indexBase.setArcHeight(indexBase.getHeight());
		indexBase.setArcWidth(indexBase.getHeight());
		indexBase.setFill(Util.PRIMARY_COLOR);
		indexBase.setEffect(shadow);

		Text index = new Text("S&P 500 Index");
		index.setFont(Font.font("Open Sans", indexBase.getHeight() * 0.63 / 1.333));
		index.setFill(Color.WHITE);

		ImageView indexImage = new ImageView(new Image("index.png"));
		indexImage.setFitHeight(index.getLayoutBounds().getHeight());
		indexImage.setFitWidth(index.getLayoutBounds().getHeight());
		indexImage.setX(indexBase.getX() + 0.4 * indexBase.getHeight());
		indexImage.setY(indexBase.getY() + (indexBase.getHeight() - indexImage.getFitWidth()) / 2.0);

		index.setX(indexImage.getX() + 1.25 * indexImage.getFitWidth());
		index.setTextOrigin(VPos.CENTER);
		index.setY(indexBase.getY() + indexBase.getHeight() / 2.0);

		indexBase.setWidth(index.getLayoutBounds().getWidth() + indexImage.getFitWidth() + indexBase.getArcWidth());

		indexButton.setOnMouseClicked(getButtonClickAction(Button.INDEX));
		indexButton.getChildren().addAll(indexBase, indexImage, index);

		Group activeButton = new Group();

		Rectangle activeBase = new Rectangle();
		activeBase.setX(Util.getScreenWidth() * 0.4023);
		activeBase.setY(indexBase.getY());
		activeBase.setHeight(indexBase.getHeight());
		activeBase.setArcHeight(activeBase.getHeight());
		activeBase.setArcWidth(activeBase.getHeight());
		activeBase.setFill(Util.PRIMARY_COLOR);
		activeBase.setEffect(shadow);

		Text active = new Text("Most Active");
		active.setFont(Font.font("Open Sans", activeBase.getHeight() * 0.63 / 1.333));
		active.setFill(Color.WHITE);

		ImageView activeImage = new ImageView(new Image("active.png"));
		activeImage.setFitHeight(active.getLayoutBounds().getHeight());
		activeImage.setFitWidth(active.getLayoutBounds().getHeight());
		activeImage.setX(activeBase.getX() + 0.4 * activeBase.getHeight());
		activeImage.setY(activeBase.getY() + (activeBase.getHeight() - activeImage.getFitWidth()) / 2.0);

		active.setX(activeImage.getX() + 1.25 * activeImage.getFitWidth());
		active.setTextOrigin(VPos.CENTER);
		active.setY(activeBase.getY() + activeBase.getHeight() / 2.0);

		activeBase.setWidth(active.getLayoutBounds().getWidth() + activeImage.getFitWidth() + activeBase.getArcWidth());

		activeButton.setOnMouseClicked(getButtonClickAction(Button.ACTIVE));
		activeButton.getChildren().addAll(activeBase, activeImage, active);

		Group gainersButton = new Group();

		Rectangle gainersBase = new Rectangle();
		gainersBase.setX(Util.getScreenWidth() * 0.55078);
		gainersBase.setY(indexBase.getY());
		gainersBase.setHeight(indexBase.getHeight());
		gainersBase.setArcHeight(gainersBase.getHeight());
		gainersBase.setArcWidth(gainersBase.getHeight());
		gainersBase.setFill(Util.PRIMARY_COLOR);
		gainersBase.setEffect(shadow);

		Text gainers = new Text("Gainers");
		gainers.setFont(Font.font("Open Sans", gainersBase.getHeight() * 0.63 / 1.333));
		gainers.setFill(Color.WHITE);

		ImageView gainersImage = new ImageView(new Image("home.png"));
		gainersImage.setFitHeight(gainers.getLayoutBounds().getHeight());
		gainersImage.setFitWidth(gainers.getLayoutBounds().getHeight());
		gainersImage.setX(gainersBase.getX() + 0.4 * gainersBase.getHeight());
		gainersImage.setY(gainersBase.getY() + (gainersBase.getHeight() - gainersImage.getFitWidth()) / 2.0);

		gainers.setX(gainersImage.getX() + 1.25 * gainersImage.getFitWidth());
		gainers.setTextOrigin(VPos.CENTER);
		gainers.setY(gainersBase.getY() + gainersBase.getHeight() / 2.0);

		gainersBase.setWidth(
				gainers.getLayoutBounds().getWidth() + gainersImage.getFitWidth() + gainersBase.getArcWidth());

		gainersButton.setOnMouseClicked(getButtonClickAction(Button.GAINERS));
		gainersButton.getChildren().addAll(gainersBase, gainersImage, gainers);

		Group losersButton = new Group();

		Rectangle losersBase = new Rectangle();
		losersBase.setX(Util.getScreenWidth() * 0.6734);
		losersBase.setY(indexBase.getY());
		losersBase.setHeight(indexBase.getHeight());
		losersBase.setArcHeight(losersBase.getHeight());
		losersBase.setArcWidth(losersBase.getHeight());
		losersBase.setFill(Util.PRIMARY_COLOR);
		losersBase.setEffect(shadow);

		Text losers = new Text("Losers");
		losers.setFont(Font.font("Open Sans", losersBase.getHeight() * 0.63 / 1.333));
		losers.setFill(Color.WHITE);

		ImageView losersImage = new ImageView(new Image("losers.png"));
		losersImage.setFitHeight(losers.getLayoutBounds().getHeight());
		losersImage.setFitWidth(losers.getLayoutBounds().getHeight());
		losersImage.setX(losersBase.getX() + 0.4 * losersBase.getHeight());
		losersImage.setY(losersBase.getY() + (losersBase.getHeight() - losersImage.getFitWidth()) / 2.0);

		losers.setX(losersImage.getX() + 1.25 * losersImage.getFitWidth());
		losers.setTextOrigin(VPos.CENTER);
		losers.setY(losersBase.getY() + losersBase.getHeight() / 2.0);

		losersBase.setWidth(losers.getLayoutBounds().getWidth() + losersImage.getFitWidth() + losersBase.getArcWidth());

		losersButton.setOnMouseClicked(getButtonClickAction(Button.LOSERS));
		losersButton.getChildren().addAll(losersBase, losersImage, losers);

		buttons.getChildren().addAll(indexButton, activeButton, gainersButton, losersButton);

		add(buttons);
	}

	private void checkVisability() {
		for (Button button : menuHandler.keySet())
			root.getChildren().remove(menuHandler.get(button).getRoot());

		root.getChildren().add(menuHandler.get(currentSelected).getRoot());
	}

	private EventHandler<MouseEvent> getButtonClickAction(Button button) {
		EventHandler<MouseEvent> handler = new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				currentSelected = button;
				checkVisability();
			}

		};

		return handler;
	}
}
