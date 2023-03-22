package ui;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import application.Util;
import handlers.StockDataHandler;
import handlers.UIHandler;
import javafx.event.EventHandler;
import javafx.geometry.VPos;
import javafx.scene.Group;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextBoundsType;
import javafx.stage.Stage;
import stock.comparators.SortMarketCap;
import stock.data.Stock;
/**
 * 
 * The class for the Home menu that creates all the UI elements of the page
 *
 */
public class HeatMap extends UserInterface {

	private StockDataHandler stockLookup;
	private HashMap<String, Rectangle> heatMapPos;

	private Group hover;
	/**
	 * 
	 * @param stage
	 * @param stockHandler
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public HeatMap(Stage stage, StockDataHandler stockHandler) throws FileNotFoundException, IOException {
		super(stage);

		stockLookup = stockHandler;
		heatMapPos = stockHandler.getHeatMapPositions();

		draw();
	}

	@Override
	protected void draw() {
		DecimalFormat format = new DecimalFormat("0.00");

		for (Entry<String, Rectangle> entry : heatMapPos.entrySet()) {

			Rectangle rect = entry.getValue();

			if (entry.getKey().contains("last")) {
				rect.setOnMouseEntered(lastHover(entry.getKey().replaceAll("last", "").trim()));
				rect.setOnMouseExited(lastExit());
				add(rect);

				continue;
			}

			Text name = new Text();

			name.setBoundsType(TextBoundsType.VISUAL);

			name.setFill(Color.WHITE);
			name.setTextAlignment(TextAlignment.CENTER);

			if (stockLookup.contains(entry.getKey())) {
				rect.setOnMouseClicked(event -> UIHandler.switchToStock(stockLookup.find(entry.getKey())));

				add(rect);

				name.setText(entry.getKey() + "\n"
						+ format.format(stockLookup.find(entry.getKey()).getChangePercentage()) + "%");
				name.setFont(
						Font.font("Open Sans", Math.round(Math.min(rect.getWidth(), rect.getHeight()) * 0.35 / 1.333)));

				name.setTextOrigin(VPos.CENTER);
				name.setX(rect.getX() + rect.getWidth() / 2 - name.getLayoutBounds().getWidth() / 2);
				name.setY(rect.getY() + rect.getHeight() / 2);

				name.setOnMouseClicked(event -> UIHandler.switchToStock(stockLookup.find(entry.getKey())));

			} else {
				name.setText(entry.getKey());
				name.setFont(Font.font("Open Sans", Math.round(rect.getHeight() * 0.7)));

				name.setX(rect.getX() + 3);
				name.setTextOrigin(VPos.CENTER);
				name.setY(rect.getY() + rect.getHeight() / 2);
			}

			add(name);
		}
	}

	private EventHandler<MouseEvent> lastHover(String sector) {

		EventHandler<MouseEvent> handler = new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {

				double width = 250 * (Util.getScreenWidth() / 1280.0);
				double height = 300 * (Util.getScreenHeight() / 720.0);

				double x = event.getX() + 0.02 * stage.getWidth();
				double y = event.getY();

				if (x + width >= stage.getWidth()) {
					x = event.getX() - width - 0.02 * stage.getWidth();
				}

				if (y + height >= stage.getHeight()) {
					y = event.getY() - height - 0.02 * stage.getHeight();
				}

				hover = new Group();

				Rectangle base = new Rectangle(x, y, width, height);
				base.setFill(Util.BACKGROUND_COLOR);

				double arcRadius = 20.0 / 720 * Util.getScreenHeight();

				base.setArcHeight(arcRadius);
				base.setArcWidth(arcRadius);

				DropShadow baseShadow = new DropShadow();
				baseShadow.setOffsetX(4);
				baseShadow.setOffsetY(4);
				baseShadow.setRadius(4);

				base.setEffect(baseShadow);
				hover.getChildren().add(base);

				double barHeight = (height - arcRadius) / 7.0;

				List<Stock> remainingStocks = stockLookup.getRemainingStock(sector);

				remainingStocks.sort(new SortMarketCap());

				int stockIndex = 0;

				for (double pillY = arcRadius / 2.0 + base.getY(); pillY < base.getY()
						+ 7.0 * barHeight; pillY += barHeight) {
					Stock stock = remainingStocks.get(stockIndex);

					Rectangle pill = new Rectangle(arcRadius / 2.0 + base.getX(), pillY, 0.32 * width,
							barHeight * 0.88);
					pill.setFill(stock.getChangeColor());
					pill.setArcHeight(pill.getHeight());
					pill.setArcWidth(pill.getHeight());
					hover.getChildren().add(pill);

					Text name = new Text(stock.getSymbol());
					name.setTextAlignment(TextAlignment.CENTER);
					name.setFont(Font.font("Open Sans", Math.round(pill.getHeight() * 0.85 / 1.333)));
					name.setFill(Color.WHITE);
					name.setTextOrigin(VPos.CENTER);
					name.setX(pill.getX() + pill.getWidth() / 2 - name.getLayoutBounds().getWidth() / 2);
					name.setY(pill.getY() + pill.getHeight() / 2);

					hover.getChildren().add(name);

					DecimalFormat format = new DecimalFormat("0.00");

					Text price = new Text("$" + format.format(stock.getPrice()));
					price.setFont(Font.font("Open Sans", Math.round(pill.getHeight() * 0.78 / 1.333)));
					price.setFill(Color.WHITE);
					price.setX(base.getX() + 0.40 * base.getWidth());
					price.setTextOrigin(VPos.CENTER);
					price.setY(name.getY());
					price.setFill(Color.WHITE);

					hover.getChildren().add(price);

					String percentChangeString = "";

					if (stock.getChange() >= 0)
						percentChangeString = "+" + format.format(stock.getChangePercentage()) + "%";
					else
						percentChangeString = format.format(stock.getChangePercentage()) + "%";
					
					Text change = new Text(percentChangeString);
					change.setFont(Font.font("Open Sans", Math.round(pill.getHeight() * 0.78 / 1.333)));

					if (stock.getChangeColor().equals(Util.SECONDARY_COLOR))
						change.setFill(Color.WHITE);
					else
						change.setFill(stock.getChangeColor());

					change.setTextOrigin(VPos.CENTER);
					change.setY(name.getY());
					change.setX(base.getX() + base.getWidth() - change.getLayoutBounds().getWidth() - arcRadius / 2.0);

					hover.getChildren().add(change);

					stockIndex++;
				}

				add(hover);

			}

		};

		return handler;

	}

	private EventHandler<MouseEvent> lastExit() {

		EventHandler<MouseEvent> handler = new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				root.getChildren().remove(hover);
				hover = null;
			}

		};

		return handler;

	}
}
