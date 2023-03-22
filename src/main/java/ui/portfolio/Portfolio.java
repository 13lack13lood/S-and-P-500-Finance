package ui.portfolio;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Comparator;
import java.util.GregorianCalendar;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

import application.Util;
import handlers.StockDataHandler;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.VPos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import stock.data.Stock;
import ui.UserInterface;
/**
 * 
 * The class for the Portfolio menu that creates all the UI elements of the page
 *
 */
public class Portfolio extends UserInterface {

	private StockDataHandler stockHandler;

	private LinkedHashMap<Stock, List<PortfolioDataPoint>> stocks;

	private Pane pane;

	private Group overlayMenu;

	private double rectWidth = 0.8695 * Util.getScreenWidth();
	private double rectX = Util.getScreenWidth() / 2.0 - rectWidth / 2.0;
	private double rectY = 0.44 * Util.getScreenHeight();
	/**
	 * 
	 * @param stage
	 * @param stockHandler
	 */
	public Portfolio(Stage stage, StockDataHandler stockHandler) {
		super(stage);

		this.stockHandler = stockHandler;

		stocks = new LinkedHashMap<>();
		pane = new Pane();

		draw();
	}

	@Override
	protected void draw() {
		readPortfolioData();
		generatePortfolio();

		createInfo();

		pane.addEventHandler(ScrollEvent.SCROLL, getPaneScroll());
		pane.requestFocus();

		Rectangle r = new Rectangle(1.1 * rectWidth, Util.getScreenHeight() - 0.27 * Util.getScreenHeight());
		r.setLayoutX(rectX);
		r.setLayoutY(rectY);

		pane.setClip(r);

		add(pane);
	}

	private void createInfo() {
		Group info = new Group();

		DropShadow shadow = new DropShadow();
		shadow.setOffsetX(3);
		shadow.setOffsetY(6);

		Text summary = new Text("Summary");
		summary.setX(0.07578 * Util.getScreenWidth());
		summary.setY(0.2 * Util.getScreenHeight());
		summary.setFont(Font.font("Open Sans", 0.061111 * Util.getScreenHeight() / 1.333));
		summary.setFill(Color.WHITE);

		info.getChildren().add(summary);

		Line summaryLine = new Line(summary.getX() - 0.05 * summary.getLayoutBounds().getWidth(),
				summary.getY() + 0.3 * summary.getLayoutBounds().getHeight(),
				summary.getX() + 1.05 * summary.getLayoutBounds().getWidth(),
				summary.getY() + 0.3 * summary.getLayoutBounds().getHeight());
		summaryLine.setStroke(Util.ACCENT_COLOR);
		summaryLine.setStrokeWidth(0.00277777 * Util.getScreenHeight());

		info.getChildren().add(summaryLine);

		Rectangle investmentNumberBase = new Rectangle(0.21718 * Util.getScreenWidth(),
				0.12083 * Util.getScreenHeight(), 0.14765 * Util.getScreenWidth(), 0.23888 * Util.getScreenHeight());
		investmentNumberBase.setFill(Util.PRIMARY_COLOR);
		investmentNumberBase.setArcHeight(20.0 / 720 * Util.getScreenHeight());
		investmentNumberBase.setArcWidth(20.0 / 720 * Util.getScreenHeight());
		investmentNumberBase.setEffect(shadow);

		Text investments = new Text("Investments");
		investments.setFont(Font.font("Open Sans", 0.04583 * Util.getScreenHeight() / 1.333));
		investments.setX(investmentNumberBase.getX() + 0.5 * investmentNumberBase.getWidth()
				- 0.5 * investments.getLayoutBounds().getWidth());
		investments.setY(investmentNumberBase.getY() + 0.25 * investmentNumberBase.getHeight());
		investments.setFill(Color.WHITE);

		Text investmentNumber = new Text(Integer.toString(stocks.size()));
		investmentNumber.setFont(Font.font("Open Sans", 0.075 * Util.getScreenHeight() / 1.333));
		investmentNumber.setX(investmentNumberBase.getX() + 0.5 * investmentNumberBase.getWidth()
				- 0.5 * investmentNumber.getLayoutBounds().getWidth());
		investmentNumber.setY(investmentNumberBase.getY() + 0.75 * investmentNumberBase.getHeight());
		investmentNumber.setFill(Color.WHITE);

		info.getChildren().addAll(investmentNumberBase, investmentNumber, investments);

		Rectangle investmentValueBase = new Rectangle(0.4 * Util.getScreenWidth(), 0.12083 * Util.getScreenHeight(),
				0.14765 * Util.getScreenWidth(), 0.23888 * Util.getScreenHeight());
		investmentValueBase.setFill(Util.PRIMARY_COLOR);
		investmentValueBase.setArcHeight(20.0 / 720 * Util.getScreenHeight());
		investmentValueBase.setArcWidth(20.0 / 720 * Util.getScreenHeight());
		investmentValueBase.setEffect(shadow);

		Text totalValue = new Text("Total Value");
		totalValue.setFont(Font.font("Open Sans", 0.04583 * Util.getScreenHeight() / 1.333));
		totalValue.setX(investmentValueBase.getX() + 0.5 * investmentValueBase.getWidth()
				- 0.5 * totalValue.getLayoutBounds().getWidth());
		totalValue.setY(investmentValueBase.getY() + 0.25 * investmentValueBase.getHeight());
		totalValue.setFill(Color.WHITE);

		Text totalValueNumber = new Text(calculateTotalValue());
		totalValueNumber.setFont(Font.font("Open Sans", 0.04 * Util.getScreenHeight() / 1.333));
		totalValueNumber.setX(investmentValueBase.getX() + 0.5 * investmentValueBase.getWidth()
				- 0.5 * totalValueNumber.getLayoutBounds().getWidth());
		totalValueNumber.setY(investmentValueBase.getY() + 0.75 * investmentValueBase.getHeight());
		totalValueNumber.setFill(Color.WHITE);

		info.getChildren().addAll(investmentValueBase, totalValueNumber, totalValue);

		Rectangle gainInfoBase = new Rectangle(0.5648 * Util.getScreenWidth(), 0.12083 * Util.getScreenHeight(),
				0.35703 * Util.getScreenWidth(), 0.23888 * Util.getScreenHeight());
		gainInfoBase.setFill(Util.PRIMARY_COLOR);
		gainInfoBase.setArcHeight(20.0 / 720 * Util.getScreenHeight());
		gainInfoBase.setArcWidth(20.0 / 720 * Util.getScreenHeight());
		gainInfoBase.setEffect(shadow);

		Text dayGainText = new Text("Day Gain");
		dayGainText.setFont(Font.font("Open Sans", 0.04583 * Util.getScreenHeight() / 1.333));
		dayGainText.setX(gainInfoBase.getX() + 0.0306 * gainInfoBase.getWidth());
		dayGainText.setY(investmentValueBase.getY() + 0.25 * investmentValueBase.getHeight());
		dayGainText.setFill(Color.WHITE);

		Rectangle dayGainBase = new Rectangle(dayGainText.getX(), gainInfoBase.getY() + 0.33 * gainInfoBase.getHeight(),
				0.42669 * gainInfoBase.getWidth(), 0.54651 * gainInfoBase.getHeight());
		dayGainBase.setFill(calculateDayGainColor());
		dayGainBase.setArcHeight(20.0 / 720 * Util.getScreenHeight());
		dayGainBase.setArcWidth(20.0 / 720 * Util.getScreenHeight());
		dayGainBase.setEffect(shadow);

		Text dayGainNumber = new Text(calculateDayGainValue());
		dayGainNumber.setFont(Font.font("Open Sans", 0.04583 * Util.getScreenHeight() / 1.333));
		dayGainNumber.setX(dayGainBase.getX() + 0.02 * dayGainBase.getWidth());
		dayGainNumber.setY(investmentValueBase.getY() + 0.52 * investmentValueBase.getHeight());
		dayGainNumber.setFill(Color.WHITE);

		Text dayGainPercent = new Text(calculateDayGainPercent());
		dayGainPercent.setFont(Font.font("Open Sans", 0.04583 * Util.getScreenHeight() / 1.333));
		dayGainPercent.setX(dayGainBase.getX() + 0.02 * dayGainBase.getWidth());
		dayGainPercent.setY(investmentValueBase.getY() + 0.77 * investmentValueBase.getHeight());
		dayGainPercent.setFill(Color.WHITE);

		Text totalGainText = new Text("Total Gain");
		totalGainText.setFont(Font.font("Open Sans", 0.04583 * Util.getScreenHeight() / 1.333));
		totalGainText.setX(gainInfoBase.getX() + 0.5317 * gainInfoBase.getWidth());
		totalGainText.setY(investmentValueBase.getY() + 0.25 * investmentValueBase.getHeight());
		totalGainText.setFill(Color.WHITE);

		Rectangle totalGainBase = new Rectangle(totalGainText.getX(),
				gainInfoBase.getY() + 0.33 * gainInfoBase.getHeight(), 0.42669 * gainInfoBase.getWidth(),
				0.54651 * gainInfoBase.getHeight());
		totalGainBase.setFill(calculateTotalGainColor());
		totalGainBase.setArcHeight(20.0 / 720 * Util.getScreenHeight());
		totalGainBase.setArcWidth(20.0 / 720 * Util.getScreenHeight());
		totalGainBase.setEffect(shadow);

		Text totalGainNumber = new Text(calculateTotalGainValue());
		totalGainNumber.setFont(Font.font("Open Sans", 0.04583 * Util.getScreenHeight() / 1.333));
		totalGainNumber.setX(totalGainBase.getX() + 0.02 * totalGainBase.getWidth());
		totalGainNumber.setY(investmentValueBase.getY() + 0.52 * investmentValueBase.getHeight());
		totalGainNumber.setFill(Color.WHITE);

		Text totalGainPercent = new Text(calculateTotalGainPercent());
		totalGainPercent.setFont(Font.font("Open Sans", 0.04583 * Util.getScreenHeight() / 1.333));
		totalGainPercent.setX(totalGainBase.getX() + 0.02 * totalGainBase.getWidth());
		totalGainPercent.setY(investmentValueBase.getY() + 0.77 * investmentValueBase.getHeight());
		totalGainPercent.setFill(Color.WHITE);

		info.getChildren().addAll(gainInfoBase, dayGainBase, dayGainText, dayGainNumber, dayGainPercent, totalGainText,
				totalGainBase, totalGainNumber, totalGainPercent);

		add(info);
	}

	private void readPortfolioData() {
		try {
			BufferedReader br = null;
			
			File file = new File(Util.RAW_PORTFOLIO_DATA_PATH);
			if(file.exists()) {
				br = new BufferedReader(new FileReader(file));
			} else {
				br = new BufferedReader(new InputStreamReader(this.getClass().getResourceAsStream("/portfolio.properties")));
			}
			
			StringTokenizer st = null;

			while (br.ready()) {
				while (st == null || !st.hasMoreTokens())
					st = new StringTokenizer(br.readLine().trim(), ";");

				Stock stock = stockHandler.find(st.nextToken());
				GregorianCalendar date = new GregorianCalendar(Integer.parseInt(st.nextToken()),
						Integer.parseInt(st.nextToken()), Integer.parseInt(st.nextToken()));
				double price = Double.parseDouble(st.nextToken());
				int quantity = Integer.parseInt(st.nextToken());

				PortfolioDataPoint dataPoint = new PortfolioDataPoint(date, stock, quantity, price);

				if (!stocks.containsKey(stock))
					stocks.put(stock, new LinkedList<PortfolioDataPoint>());

				stocks.get(stock).add(dataPoint);
			}
			
			writeData();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void generatePortfolio() {
		double y = rectY;

		DecimalFormat format = new DecimalFormat("0.00");

		Text symbolHeading = new Text("Symbol");
		symbolHeading.setX(0.0773 * Util.getScreenWidth());
		symbolHeading.setY(0.429166 * Util.getScreenHeight());
		symbolHeading.setFont(Font.font("Open Sans", 0.0375 * Util.getScreenHeight() / 1.333));
		symbolHeading.setFill(Color.WHITE);

		Text priceHeading = new Text("Price");
		priceHeading.setX(0.3 * Util.getScreenWidth());
		priceHeading.setY(0.429166 * Util.getScreenHeight());
		priceHeading.setFont(Font.font("Open Sans", 0.0375 * Util.getScreenHeight() / 1.333));
		priceHeading.setFill(Color.WHITE);

		Text quantityHeading = new Text("Quantity");
		quantityHeading.setX(0.4656 * Util.getScreenWidth());
		quantityHeading.setY(0.429166 * Util.getScreenHeight());
		quantityHeading.setFont(Font.font("Open Sans", 0.0375 * Util.getScreenHeight() / 1.333));
		quantityHeading.setFill(Color.WHITE);

		Text dayGainHeading = new Text("Day Gain");
		dayGainHeading.setX(0.63203 * Util.getScreenWidth());
		dayGainHeading.setY(0.429166 * Util.getScreenHeight());
		dayGainHeading.setFont(Font.font("Open Sans", 0.0375 * Util.getScreenHeight() / 1.333));
		dayGainHeading.setFill(Color.WHITE);

		Text valueHeading = new Text("Value");
		valueHeading.setX(0.80859 * Util.getScreenWidth());
		valueHeading.setY(0.429166 * Util.getScreenHeight());
		valueHeading.setFont(Font.font("Open Sans", 0.0375 * Util.getScreenHeight() / 1.333));
		valueHeading.setFill(Color.WHITE);

		Group addInvestment = new Group();

		ImageView addInvestmentIcon = new ImageView(new Image("add.png"));
		addInvestmentIcon.setX(0.89609 * Util.getScreenWidth());
		addInvestmentIcon.setFitWidth(0.040277777 * Util.getScreenHeight());
		addInvestmentIcon.setFitHeight(addInvestmentIcon.getFitWidth());
		addInvestmentIcon.setY(valueHeading.getY() - valueHeading.getLayoutBounds().getHeight());

		Circle addInvestmentCircle = new Circle(addInvestmentIcon.getX() + 0.5 * addInvestmentIcon.getFitWidth(),
				addInvestmentIcon.getY() + 0.5 * addInvestmentIcon.getFitHeight(),
				0.5 * addInvestmentIcon.getFitWidth());
		addInvestmentCircle.setFill(Util.ACCENT_COLOR);

		addInvestment.getChildren().addAll(addInvestmentCircle, addInvestmentIcon);
		addInvestment.setOnMouseClicked(addInvestment());

		add(symbolHeading, priceHeading, quantityHeading, dayGainHeading, valueHeading, addInvestment);

		for (Stock stock : stocks.keySet()) {
			double baseRectHeight = 0.1222 * Util.getScreenHeight();
			double addedHeight = 0.0611111 * Util.getScreenHeight();
			double rectHeight = baseRectHeight + addedHeight * stocks.get(stock).size();

			Rectangle rect = new Rectangle(rectX, y, rectWidth, rectHeight);
			rect.setFill(Util.PRIMARY_COLOR);
			rect.setArcHeight(20 / 720.0 * Util.getScreenHeight());
			rect.setArcWidth(20 / 720.0 * Util.getScreenHeight());

			DropShadow shadow = new DropShadow();
			shadow.setOffsetX(3);
			shadow.setOffsetY(5);

			rect.setEffect(shadow);

			Group group = new Group();

			Text symbol = new Text(stock.getSymbol());
			symbol.setFont(Font.font("Open Sans", 0.38636 * baseRectHeight / 1.333));
			symbol.setX(rectX + 0.01437 * rectWidth);
			symbol.setY(y + baseRectHeight * 0.48);
			symbol.setFill(Color.WHITE);

			Text purchaseDate = new Text("Purchase Date");
			purchaseDate.setFont(Font.font("Open Sans", 0.2514 * baseRectHeight / 1.333));
			purchaseDate.setX(symbol.getX());
			purchaseDate.setY(y + baseRectHeight * 0.86);
			purchaseDate.setFill(Color.WHITE);

			Line line = new Line(symbol.getX(), y + 0.9772 * baseRectHeight, symbol.getX() + 0.90835 * rectWidth,
					y + 0.9772 * baseRectHeight);
			line.setStrokeWidth(0.00277 * Util.getScreenHeight());
			line.setStroke(Util.ACCENT_COLOR);

			Text currentPrice = new Text("$" + format.format(stock.getPrice()));
			currentPrice.setFont(Font.font("Open Sans", 0.306818 * baseRectHeight / 1.333));
			currentPrice.setX(priceHeading.getX() + priceHeading.getLayoutBounds().getWidth()
					- currentPrice.getLayoutBounds().getWidth());
			currentPrice.setY(y + baseRectHeight * 0.45);
			currentPrice.setFill(Color.WHITE);

			Text purchasePrice = new Text("Purchase Price");
			purchasePrice.setFont(Font.font("Open Sans", 0.2514 * baseRectHeight / 1.333));
			purchasePrice.setX(priceHeading.getX() + priceHeading.getLayoutBounds().getWidth()
					- purchasePrice.getLayoutBounds().getWidth());
			purchasePrice.setY(y + baseRectHeight * 0.86);
			purchasePrice.setFill(Color.WHITE);

			Text purchaseQuantity = new Text("Quantity");
			purchaseQuantity.setFont(Font.font("Open Sans", 0.2514 * baseRectHeight / 1.333));
			purchaseQuantity.setX(quantityHeading.getX() + quantityHeading.getLayoutBounds().getWidth()
					- purchaseQuantity.getLayoutBounds().getWidth());
			purchaseQuantity.setY(y + baseRectHeight * 0.86);
			purchaseQuantity.setFill(Color.WHITE);

			ImageView addIcon = new ImageView(new Image("add.png"));
			addIcon.setX(0.892969 * Util.getScreenWidth());
			addIcon.setFitWidth(0.05 * Util.getScreenHeight());
			addIcon.setFitHeight(addIcon.getFitWidth());
			addIcon.setY(currentPrice.getY() - currentPrice.getLayoutBounds().getHeight());
			addIcon.setOnMouseClicked(addPortfolio(stock));

			group.getChildren().addAll(symbol, purchaseDate, purchasePrice, purchaseQuantity, currentPrice, line,
					addIcon);

			pane.getChildren().addAll(rect, group);

			List<PortfolioDataPoint> dataPoints = stocks.get(stock);
			dataPoints.sort(Comparator.naturalOrder());

			int totalQuantity = 0;

			for (int i = 0; i < dataPoints.size(); i++) {
				GregorianCalendar date = dataPoints.get(i).getDate();

				Text dateText = new Text(Util.MONTHS_ABREVIATED[date.get(Calendar.MONTH)] + " "
						+ date.get(Calendar.DAY_OF_MONTH) + ", " + date.get(Calendar.YEAR));
				dateText.setFont(currentPrice.getFont());
				dateText.setX(purchaseDate.getX());
				dateText.setY(y + baseRectHeight + i * addedHeight + 0.69 * addedHeight);
				dateText.setFill(Color.WHITE);

				Text price = new Text("$" + format.format(dataPoints.get(i).getPrice()));
				price.setFont(currentPrice.getFont());
				price.setX(priceHeading.getX() + priceHeading.getLayoutBounds().getWidth()
						- price.getLayoutBounds().getWidth());
				price.setY(dateText.getY());
				price.setFill(Color.WHITE);

				totalQuantity += dataPoints.get(i).getQuantity();

				Text quantity = new Text(Integer.toString(dataPoints.get(i).getQuantity()));
				quantity.setFont(currentPrice.getFont());
				quantity.setX(quantityHeading.getX() + quantityHeading.getLayoutBounds().getWidth()
						- quantity.getLayoutBounds().getWidth());
				quantity.setY(dateText.getY());
				quantity.setFill(Color.WHITE);

				double gainValue = dataPoints.get(i).getQuantity() * (stock.getPrice() - dataPoints.get(i).getPrice());

				Text gain = new Text(
						(gainValue >= 0) ? "+$" + format.format(gainValue) : "-$" + format.format(Math.abs(gainValue)));
				gain.setFont(currentPrice.getFont());
				gain.setX(rectX + 0.59748 * rectWidth);
				gain.setY(dateText.getY());
				gain.setFill(Color.WHITE);

				Rectangle changeRect = new Rectangle(rectX + 0.71428 * rectWidth,
						gain.getY() - 0.9 * gain.getLayoutBounds().getHeight(), 0.10 * rectWidth,
						1.2 * gain.getLayoutBounds().getHeight());
				changeRect.setFill(calculateColor(gainValue / dataPoints.get(i).getPrice()));
				changeRect.setEffect(shadow);
				changeRect.setArcHeight(changeRect.getHeight());
				changeRect.setArcWidth(changeRect.getHeight());

				Text gainPercent = new Text(
						(gainValue / (dataPoints.get(i).getPrice() * dataPoints.get(i).getQuantity() * 100.0) >= 0)
								? "+" + format.format(gainValue
										/ (dataPoints.get(i).getPrice() * dataPoints.get(i).getQuantity()) * 100.0)
										+ "%"
								: format.format(gainValue
										/ (dataPoints.get(i).getPrice() * dataPoints.get(i).getQuantity()) * 100.0)
										+ "%");
				gainPercent.setFont(currentPrice.getFont());
				gainPercent.setX(
						changeRect.getX() + changeRect.getWidth() / 2 - gainPercent.getLayoutBounds().getWidth() / 2);
				gainPercent.setTextOrigin(VPos.CENTER);
				gainPercent.setY(changeRect.getY() + changeRect.getHeight() / 2);
				gainPercent.setFill(Color.WHITE);

				Text value = new Text("$" + format.format(dataPoints.get(i).getQuantity() * stock.getPrice()));
				value.setFont(currentPrice.getFont());
				value.setX(valueHeading.getX() + valueHeading.getLayoutBounds().getWidth() / 2
						- value.getLayoutBounds().getWidth() / 2);
				value.setY(dateText.getY());
				value.setFill(Color.WHITE);

				ImageView changeIcon = new ImageView(new Image("more.png"));
				changeIcon.setX(0.892969 * Util.getScreenWidth());
				changeIcon.setFitWidth(0.05 * Util.getScreenHeight());
				changeIcon.setFitHeight(changeIcon.getFitWidth());
				changeIcon.setY(value.getY() - value.getLayoutBounds().getHeight());
				changeIcon.setOnMouseClicked(changePortfolio(stock, dataPoints.get(i)));

				group.getChildren().addAll(dateText, quantity, price, gain, changeRect, gainPercent, value, changeIcon);
			}

			Text totalQuantityText = new Text(Integer.toString(totalQuantity));
			totalQuantityText.setFont(Font.font("Open Sans", 0.306818 * baseRectHeight / 1.333));
			totalQuantityText.setX(quantityHeading.getX() + quantityHeading.getLayoutBounds().getWidth()
					- totalQuantityText.getLayoutBounds().getWidth());
			totalQuantityText.setY(y + baseRectHeight * 0.45);
			totalQuantityText.setFill(Color.WHITE);

			Text dayGain = new Text(
					(stock.getChange() * totalQuantity >= 0) ? "+$" + format.format(stock.getChange() * totalQuantity)
							: "-$" + format.format(Math.abs(stock.getChange() * totalQuantity)));
			dayGain.setFont(currentPrice.getFont());
			dayGain.setX(rectX + 0.59748 * rectWidth);
			dayGain.setY(totalQuantityText.getY());
			dayGain.setFill(Color.WHITE);

			Rectangle changeRect = new Rectangle(rectX + 0.71428 * rectWidth,
					dayGain.getY() - 0.9 * dayGain.getLayoutBounds().getHeight(), 0.084456 * rectWidth,
					1.2 * dayGain.getLayoutBounds().getHeight());
			changeRect.setFill(calculateColor(stock.getChangePercentage()));
			changeRect.setEffect(shadow);
			changeRect.setArcHeight(changeRect.getHeight());
			changeRect.setArcWidth(changeRect.getHeight());

			Text gainPercent = new Text(
					stock.getChangePercentage() >= 0 ? "+" + format.format(stock.getChangePercentage()) + "%"
							: format.format(stock.getChangePercentage()) + "%");
			gainPercent.setFont(currentPrice.getFont());
			gainPercent
					.setX(changeRect.getX() + changeRect.getWidth() / 2 - gainPercent.getLayoutBounds().getWidth() / 2);
			gainPercent.setTextOrigin(VPos.CENTER);
			gainPercent.setY(changeRect.getY() + changeRect.getHeight() / 2);
			gainPercent.setFill(Color.WHITE);

			Text totalGainText = new Text("Total Gain");
			totalGainText.setFont(Font.font("Open Sans", 0.2514 * baseRectHeight / 1.333));
			totalGainText.setX(dayGainHeading.getX() + dayGainHeading.getLayoutBounds().getWidth() / 2
					- totalGainText.getLayoutBounds().getWidth() / 2);
			totalGainText.setY(y + baseRectHeight * 0.86);
			totalGainText.setFill(Color.WHITE);

			Text valueText = new Text("Value");
			valueText.setFont(Font.font("Open Sans", 0.15 * baseRectHeight / 1.333));
			valueText.setX(valueHeading.getX() + valueHeading.getLayoutBounds().getWidth() / 2
					- valueText.getLayoutBounds().getWidth() / 2);
			valueText.setY(y + baseRectHeight * 0.86);
			valueText.setFill(Color.WHITE);

			Text totalValue = new Text("$" + format.format(stock.getPrice() * totalQuantity));
			totalValue.setFont(Font.font("Open Sans", 0.306818 * baseRectHeight / 1.333));
			totalValue.setX(valueHeading.getX() + valueHeading.getLayoutBounds().getWidth() / 2
					- totalValue.getLayoutBounds().getWidth() / 2);
			totalValue.setY(y + baseRectHeight * 0.45);
			totalValue.setFill(Color.WHITE);

			group.getChildren().addAll(dayGain, changeRect, gainPercent, valueText, totalGainText, totalQuantityText,
					totalValue);

			y += 1.04 * rectHeight;
		}
	}

	private String calculateTotalValue() {
		DecimalFormat format = new DecimalFormat("0.00");

		double value = 0;

		for (Stock stock : stocks.keySet()) {
			for (PortfolioDataPoint data : stocks.get(stock)) {
				value += data.getQuantity() * stock.getPrice();
			}
		}

		return "$" + format.format(value);
	}

	private String calculateDayGainValue() {
		DecimalFormat format = new DecimalFormat("0.00");

		double value = 0;

		for (Stock stock : stocks.keySet()) {
			for (PortfolioDataPoint data : stocks.get(stock)) {
				value += data.getQuantity() * stock.getChange();
			}
		}

		String string = format.format(value);

		return (value >= 0) ? "+$" + string : "-$" + format.format(Math.abs(value));
	}

	private String calculateDayGainPercent() {
		DecimalFormat format = new DecimalFormat("0.00");

		double current = 0;
		double original = 0;

		for (Stock stock : stocks.keySet()) {
			for (PortfolioDataPoint data : stocks.get(stock)) {
				current += data.getQuantity() * stock.getPrice();
				original += data.getQuantity() * stock.getPriceOpen();
			}
		}

		double percent = (current - original) / original * 100;

		String string = format.format(percent);

		return (percent >= 0) ? "+" + string + "%" : string + "%";
	}

	private Color calculateDayGainColor() {

		double current = 0;
		double original = 0;

		for (Stock stock : stocks.keySet()) {
			for (PortfolioDataPoint data : stocks.get(stock)) {
				current += data.getQuantity() * stock.getPrice();
				original += data.getQuantity() * stock.getPriceOpen();
			}
		}

		double percent = (current - original) / original * 100;

		return calculateColor(percent);

	}

	private String calculateTotalGainValue() {
		DecimalFormat format = new DecimalFormat("0.00");

		double value = 0;

		for (Stock stock : stocks.keySet()) {
			for (PortfolioDataPoint data : stocks.get(stock)) {
				value += data.getQuantity() * (stock.getPrice() - data.getPrice());
			}
		}

		String string = format.format(value);

		return (value >= 0) ? "+$" + string : "-$" + format.format(Math.abs(value));
	}

	private String calculateTotalGainPercent() {
		DecimalFormat format = new DecimalFormat("0.00");

		double current = 0;
		double original = 0;

		for (Stock stock : stocks.keySet()) {
			for (PortfolioDataPoint data : stocks.get(stock)) {
				current += data.getQuantity() * stock.getPrice();
				original += data.getQuantity() * data.getPrice();
			}
		}

		double percent = (current - original) / original * 100;

		String string = format.format(percent);

		return (percent >= 0) ? "+" + string + "%" : string + "%";
	}

	private Color calculateTotalGainColor() {
		double current = 0;
		double original = 0;

		for (Stock stock : stocks.keySet()) {
			for (PortfolioDataPoint data : stocks.get(stock)) {
				current += data.getQuantity() * stock.getPrice();
				original += data.getQuantity() * data.getPrice();
			}
		}

		double percent = (current - original) / original * 100;

		return calculateColor(percent);
	}

	private Color calculateColor(double value) {
		if (value >= 2)
			return Util.BIG_GAIN_COLOR;

		if (value >= 1)
			return Util.MEDIUM_GAIN_COLOR;

		if (value >= 0.2)
			return Util.SMALL_GAIN_COLOR;

		if (value > -0.2)
			return Util.SECONDARY_COLOR;

		if (value > -1)
			return Util.SMALL_LOSS_COLOR;

		if (value > -2)
			return Util.MEDIUM_LOSS_COLOR;

		return Util.BIG_LOSS_COLOR;
	}

	private EventHandler<ScrollEvent> getPaneScroll() {
		EventHandler<ScrollEvent> handler = new EventHandler<ScrollEvent>() {

			@Override
			public void handle(ScrollEvent event) {
				ObservableList<Node> objects = pane.getChildren();

				Rectangle first = (Rectangle) objects.get(0);
				Rectangle last = (Rectangle) objects.get(objects.size() - 2);

				if (event.getDeltaY() > 0 && first.getY() + first.getTranslateY() < rectY) {
					for (Node e : objects) {
						e.setTranslateY(e.getTranslateY() + event.getDeltaY());
					}
				}

				if (event.getDeltaY() < 0
						&& last.getY() + last.getTranslateY() + last.getHeight() >= 0.95 * Util.getScreenHeight()) {
					for (Node e : objects) {
						e.setTranslateY(e.getTranslateY() + event.getDeltaY());
					}
				}
			}

		};

		return handler;
	}

	private EventHandler<MouseEvent> changePortfolio(Stock stock, PortfolioDataPoint data) {
		EventHandler<MouseEvent> handler = new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				overlayMenu = new Group();

				Rectangle base = new Rectangle(1.0 / 3 * Util.getScreenWidth(), 1.0 / 3 * Util.getScreenHeight(),
						1.0 / 3 * Util.getScreenWidth(), 1.0 / 3 * Util.getScreenHeight());
				base.setFill(Util.SECONDARY_COLOR);

				double arcRadius = 20.0 / 720 * Util.getScreenHeight();

				base.setArcHeight(arcRadius);
				base.setArcWidth(arcRadius);

				DropShadow baseShadow = new DropShadow();
				baseShadow.setOffsetX(4);
				baseShadow.setOffsetY(4);
				baseShadow.setRadius(4);

				base.setEffect(baseShadow);

				Text dateText = new Text("Date");
				dateText.setFont(Font.font("Open Sans", 20.0 / 720 * Util.getScreenHeight()));
				dateText.setX(base.getX() + 0.07 * base.getWidth());
				dateText.setY(base.getY() + 0.2 * base.getHeight());
				dateText.setFill(Color.WHITE);

				TextField month = new TextField();
				month.setPromptText("MM");
				month.setFont(Font.font("Open Sans", 20.0 / 720 * Util.getScreenHeight()));
				month.setMinWidth(0.0506 * Util.getScreenWidth());
				month.setMaxWidth(0.0506 * Util.getScreenWidth());
				month.setTranslateX(dateText.getX() + dateText.getLayoutBounds().getWidth() + 0.2 * base.getWidth());
				month.setTranslateY(dateText.getY() - dateText.getLayoutBounds().getHeight());

				TextField day = new TextField();
				day.setPromptText("DD");
				day.setFont(Font.font("Open Sans", 20.0 / 720 * Util.getScreenHeight()));
				day.setMinWidth(0.0506 * Util.getScreenWidth());
				day.setMaxWidth(0.0506 * Util.getScreenWidth());
				day.setTranslateX(month.getTranslateX() + month.getLayoutBounds().getWidth() + 0.1 * base.getWidth());
				day.setTranslateY(month.getTranslateY());

				TextField year = new TextField();
				year.setPromptText("YYYY");
				year.setFont(Font.font("Open Sans", 20.0 / 720 * Util.getScreenHeight()));
				year.setMinWidth(0.0606 * Util.getScreenWidth());
				year.setMaxWidth(0.0606 * Util.getScreenWidth());
				year.setTranslateX(day.getTranslateX() + day.getLayoutBounds().getWidth() + 0.1 * base.getWidth());
				year.setTranslateY(month.getTranslateY());

				Text priceText = new Text("Price");
				priceText.setFont(Font.font("Open Sans", 20.0 / 720 * Util.getScreenHeight()));
				priceText.setX(dateText.getX());
				priceText.setY(dateText.getY() + dateText.getLayoutBounds().getHeight() + 0.2 * base.getHeight());
				priceText.setFill(Color.WHITE);

				TextField price = new TextField();
				price.setPromptText("$0.00");
				price.setFont(Font.font("Open Sans", 20.0 / 720 * Util.getScreenHeight()));
				price.setMinWidth(0.0706 * Util.getScreenWidth());
				price.setMaxWidth(0.0706 * Util.getScreenWidth());
				price.setTranslateX(month.getTranslateX());
				price.setTranslateY(priceText.getY() - priceText.getLayoutBounds().getHeight());

				Text quantityText = new Text("Quantity");
				quantityText.setFont(Font.font("Open Sans", 20.0 / 720 * Util.getScreenHeight()));
				quantityText.setX(dateText.getX());
				quantityText.setY(priceText.getY() + priceText.getLayoutBounds().getHeight() + 0.2 * base.getHeight());
				quantityText.setFill(Color.WHITE);

				TextField quantity = new TextField();
				quantity.setPromptText("00");
				quantity.setFont(Font.font("Open Sans", 20.0 / 720 * Util.getScreenHeight()));
				quantity.setMinWidth(0.0506 * Util.getScreenWidth());
				quantity.setMaxWidth(0.0506 * Util.getScreenWidth());
				quantity.setTranslateX(month.getTranslateX());
				quantity.setTranslateY(quantityText.getY() - quantityText.getLayoutBounds().getHeight());

				Group cancelButton = new Group();

				Rectangle cancel = new Rectangle(base.getX() + 0.7 * base.getWidth(), price.getTranslateY(),
						0.25 * base.getWidth(), 0.12 * base.getHeight());
				cancel.setFill(Util.BIG_LOSS_COLOR);
				cancel.setEffect(baseShadow);
				cancel.setArcHeight(cancel.getHeight());
				cancel.setArcWidth(cancel.getHeight());

				Text cancelText = new Text("Cancel");
				cancelText.setFont(Font.font("Open Sans", 0.6 * cancel.getHeight() / 1.333));
				cancelText.setFill(Color.WHITE);
				cancelText
						.setX(cancel.getX() + 0.5 * cancel.getWidth() - 0.5 * cancelText.getLayoutBounds().getWidth());
				cancelText.setTextOrigin(VPos.CENTER);
				cancelText.setY(cancel.getY() + 0.5 * cancel.getHeight());

				cancelButton.getChildren().addAll(cancel, cancelText);
				cancelButton.setOnMouseClicked(new EventHandler<MouseEvent>() {

					@Override
					public void handle(MouseEvent event) {
						root.getChildren().remove(overlayMenu);
					}

				});

				Group addButton = new Group();

				Rectangle add = new Rectangle(base.getX() + 0.7 * base.getWidth(), quantity.getTranslateY(),
						0.25 * base.getWidth(), 0.12 * base.getHeight());
				add.setFill(Util.BIG_GAIN_COLOR);
				add.setEffect(baseShadow);
				add.setArcHeight(add.getHeight());
				add.setArcWidth(add.getHeight());

				Text addText = new Text("Save");
				addText.setFont(Font.font("Open Sans", 0.6 * add.getHeight() / 1.333));
				addText.setFill(Color.WHITE);
				addText.setX(add.getX() + 0.5 * add.getWidth() - 0.5 * addText.getLayoutBounds().getWidth());
				addText.setTextOrigin(VPos.CENTER);
				addText.setY(add.getY() + 0.5 * add.getHeight());

				addButton.getChildren().addAll(add, addText);
				addButton.setOnMouseClicked(new EventHandler<MouseEvent>() {

					@Override
					public void handle(MouseEvent event) {
						GregorianCalendar date = new GregorianCalendar();

						String monthValue = month.getText().trim().replaceAll("[^0-9]", "");
						String dayValue = day.getText().trim().replaceAll("[^0-9]", "");
						String yearValue = year.getText().trim().replaceAll("[^0-9]", "");
						if (monthValue.length() > 0 && dayValue.length() > 0 && yearValue.length() > 0) {
							try {
								date.set(Calendar.MONTH, Integer.parseInt(monthValue) - 1);
								date.set(Calendar.DAY_OF_MONTH, Integer.parseInt(dayValue));
								date.set(Calendar.YEAR, Integer.parseInt(yearValue));

								data.setDate(date);
							} catch (Exception e) {
								date = null;
							}
						}

						double originalPrice = data.getPrice();

						try {

							data.setPrice(Double.parseDouble(price.getText().trim().replaceAll("[^0-9]", "")));
						} catch (Exception e) {
							data.setPrice(originalPrice);
						}

						int originalQuantity = data.getQuantity();

						try {

							data.setQuantity(Integer.parseInt(quantity.getText().trim().replaceAll("[^0-9]", "")));
						} catch (Exception e) {
							data.setQuantity(originalQuantity);
						}

						refresh();
					}

				});

				Group deleteButton = new Group();

				Rectangle delete = new Rectangle(base.getX() + 0.7 * base.getWidth(),
						(cancel.getY() + cancel.getHeight() + add.getY()) / 2.0 - 0.06 * base.getHeight(),
						0.25 * base.getWidth(), 0.12 * base.getHeight());
				delete.setFill(Util.BIG_LOSS_COLOR);
				delete.setEffect(baseShadow);
				delete.setArcHeight(delete.getHeight());
				delete.setArcWidth(delete.getHeight());

				Text deleteText = new Text("Delete");
				deleteText.setFont(Font.font("Open Sans", 0.6 * delete.getHeight() / 1.333));
				deleteText.setFill(Color.WHITE);
				deleteText
						.setX(delete.getX() + 0.5 * delete.getWidth() - 0.5 * deleteText.getLayoutBounds().getWidth());
				deleteText.setTextOrigin(VPos.CENTER);
				deleteText.setY(delete.getY() + 0.5 * delete.getHeight());

				deleteButton.getChildren().addAll(delete, deleteText);
				deleteButton.setOnMouseClicked(new EventHandler<MouseEvent>() {

					@Override
					public void handle(MouseEvent event) {
						stocks.get(stock).remove(data);
						refresh();
					}

				});

				overlayMenu.getChildren().addAll(base, dateText, month, day, year, quantityText, quantity, priceText,
						price, cancelButton, addButton, deleteButton);

				add(overlayMenu);
			}

		};

		return handler;
	}

	private EventHandler<MouseEvent> addPortfolio(Stock stock) {
		EventHandler<MouseEvent> handler = new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				overlayMenu = new Group();

				Rectangle base = new Rectangle(1.0 / 3 * Util.getScreenWidth(), 1.0 / 3 * Util.getScreenHeight(),
						1.0 / 3 * Util.getScreenWidth(), 1.0 / 3 * Util.getScreenHeight());
				base.setFill(Util.SECONDARY_COLOR);

				double arcRadius = 20.0 / 720 * Util.getScreenHeight();

				base.setArcHeight(arcRadius);
				base.setArcWidth(arcRadius);

				DropShadow baseShadow = new DropShadow();
				baseShadow.setOffsetX(4);
				baseShadow.setOffsetY(4);
				baseShadow.setRadius(4);

				base.setEffect(baseShadow);

				Text dateText = new Text("Date");
				dateText.setFont(Font.font("Open Sans", 20.0 / 720 * Util.getScreenHeight()));
				dateText.setX(base.getX() + 0.07 * base.getWidth());
				dateText.setY(base.getY() + 0.2 * base.getHeight());
				dateText.setFill(Color.WHITE);

				TextField month = new TextField();
				month.setPromptText("MM");
				month.setFont(Font.font("Open Sans", 20.0 / 720 * Util.getScreenHeight()));
				month.setMinWidth(0.0506 * Util.getScreenWidth());
				month.setMaxWidth(0.0506 * Util.getScreenWidth());
				month.setTranslateX(dateText.getX() + dateText.getLayoutBounds().getWidth() + 0.2 * base.getWidth());
				month.setTranslateY(dateText.getY() - dateText.getLayoutBounds().getHeight());

				TextField day = new TextField();
				day.setPromptText("DD");
				day.setFont(Font.font("Open Sans", 20.0 / 720 * Util.getScreenHeight()));
				day.setMinWidth(0.0506 * Util.getScreenWidth());
				day.setMaxWidth(0.0506 * Util.getScreenWidth());
				day.setTranslateX(month.getTranslateX() + month.getLayoutBounds().getWidth() + 0.1 * base.getWidth());
				day.setTranslateY(month.getTranslateY());

				TextField year = new TextField();
				year.setPromptText("YYYY");
				year.setFont(Font.font("Open Sans", 20.0 / 720 * Util.getScreenHeight()));
				year.setMinWidth(0.0606 * Util.getScreenWidth());
				year.setMaxWidth(0.0606 * Util.getScreenWidth());
				year.setTranslateX(day.getTranslateX() + day.getLayoutBounds().getWidth() + 0.1 * base.getWidth());
				year.setTranslateY(month.getTranslateY());

				Text priceText = new Text("Price");
				priceText.setFont(Font.font("Open Sans", 20.0 / 720 * Util.getScreenHeight()));
				priceText.setX(dateText.getX());
				priceText.setY(dateText.getY() + dateText.getLayoutBounds().getHeight() + 0.2 * base.getHeight());
				priceText.setFill(Color.WHITE);

				TextField price = new TextField();
				price.setPromptText("$0.00");
				price.setFont(Font.font("Open Sans", 20.0 / 720 * Util.getScreenHeight()));
				price.setMinWidth(0.0706 * Util.getScreenWidth());
				price.setMaxWidth(0.0706 * Util.getScreenWidth());
				price.setTranslateX(month.getTranslateX());
				price.setTranslateY(priceText.getY() - priceText.getLayoutBounds().getHeight());

				Text quantityText = new Text("Quantity");
				quantityText.setFont(Font.font("Open Sans", 20.0 / 720 * Util.getScreenHeight()));
				quantityText.setX(dateText.getX());
				quantityText.setY(priceText.getY() + priceText.getLayoutBounds().getHeight() + 0.2 * base.getHeight());
				quantityText.setFill(Color.WHITE);

				TextField quantity = new TextField();
				quantity.setPromptText("00");
				quantity.setFont(Font.font("Open Sans", 20.0 / 720 * Util.getScreenHeight()));
				quantity.setMinWidth(0.0506 * Util.getScreenWidth());
				quantity.setMaxWidth(0.0506 * Util.getScreenWidth());
				quantity.setTranslateX(month.getTranslateX());
				quantity.setTranslateY(quantityText.getY() - quantityText.getLayoutBounds().getHeight());

				Group cancelButton = new Group();

				Rectangle cancel = new Rectangle(base.getX() + 0.7 * base.getWidth(), price.getTranslateY(),
						0.25 * base.getWidth(), 0.12 * base.getHeight());
				cancel.setFill(Util.BIG_LOSS_COLOR);
				cancel.setEffect(baseShadow);
				cancel.setArcHeight(cancel.getHeight());
				cancel.setArcWidth(cancel.getHeight());

				Text cancelText = new Text("Cancel");
				cancelText.setFont(Font.font("Open Sans", 0.6 * cancel.getHeight() / 1.333));
				cancelText.setFill(Color.WHITE);
				cancelText
						.setX(cancel.getX() + 0.5 * cancel.getWidth() - 0.5 * cancelText.getLayoutBounds().getWidth());
				cancelText.setTextOrigin(VPos.CENTER);
				cancelText.setY(cancel.getY() + 0.5 * cancel.getHeight());

				cancelButton.getChildren().addAll(cancel, cancelText);
				cancelButton.setOnMouseClicked(new EventHandler<MouseEvent>() {

					@Override
					public void handle(MouseEvent event) {
						root.getChildren().remove(overlayMenu);
					}

				});

				Group addButton = new Group();

				Rectangle add = new Rectangle(base.getX() + 0.7 * base.getWidth(), quantity.getTranslateY(),
						0.25 * base.getWidth(), 0.12 * base.getHeight());
				add.setFill(Util.BIG_GAIN_COLOR);
				add.setEffect(baseShadow);
				add.setArcHeight(add.getHeight());
				add.setArcWidth(add.getHeight());

				Text addText = new Text("Save");
				addText.setFont(Font.font("Open Sans", 0.6 * add.getHeight() / 1.333));
				addText.setFill(Color.WHITE);
				addText.setX(add.getX() + 0.5 * add.getWidth() - 0.5 * addText.getLayoutBounds().getWidth());
				addText.setTextOrigin(VPos.CENTER);
				addText.setY(add.getY() + 0.5 * add.getHeight());

				addButton.getChildren().addAll(add, addText);
				addButton.setOnMouseClicked(new EventHandler<MouseEvent>() {

					@Override	
					public void handle(MouseEvent event) {
						PortfolioDataPoint data = new PortfolioDataPoint(new GregorianCalendar(), stock, 0, 0);

						GregorianCalendar date = new GregorianCalendar();

						String monthValue = month.getText().trim().replaceAll("[^0-9]", "");
						String dayValue = day.getText().trim().replaceAll("[^0-9]", "");
						String yearValue = year.getText().trim().replaceAll("[^0-9]", "");
						if (monthValue.length() > 0 && dayValue.length() > 0 && yearValue.length() > 0) {
							try {
								date.set(Calendar.MONTH, Integer.parseInt(monthValue) - 1);
								date.set(Calendar.DAY_OF_MONTH, Integer.parseInt(dayValue));
								date.set(Calendar.YEAR, Integer.parseInt(yearValue));

								data.setDate(date);
							} catch (Exception e) {
								date = null;
							}
						}

						try {

							data.setPrice(Double.parseDouble(price.getText().trim().replaceAll("[^0-9]", "")));
						} catch (Exception e) {
							data.setPrice(0);
						}

						try {

							data.setQuantity(Integer.parseInt(quantity.getText().trim().replaceAll("[^0-9]", "")));
						} catch (Exception e) {
							data.setQuantity(0);
						}

						stocks.get(stock).add(data);

						refresh();
					}

				});

				overlayMenu.getChildren().addAll(base, dateText, month, day, year, quantityText, quantity, priceText,
						price, cancelButton, addButton);

				add(overlayMenu);
			}

		};

		return handler;
	}

	private EventHandler<MouseEvent> addInvestment() {
		EventHandler<MouseEvent> handler = new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				overlayMenu = new Group();

				Rectangle base = new Rectangle(1.0 / 3 * Util.getScreenWidth(), 1.0 / 3 * Util.getScreenHeight(),
						1.0 / 3 * Util.getScreenWidth(), 1.0 / 7 * Util.getScreenHeight());
				base.setFill(Util.SECONDARY_COLOR);

				double arcRadius = 20.0 / 720 * Util.getScreenHeight();

				base.setArcHeight(arcRadius);
				base.setArcWidth(arcRadius);

				DropShadow baseShadow = new DropShadow();
				baseShadow.setOffsetX(4);
				baseShadow.setOffsetY(4);
				baseShadow.setRadius(4);

				base.setEffect(baseShadow);

				Text stockText = new Text("Stock");
				stockText.setFont(Font.font("Open Sans", 20.0 / 720 * Util.getScreenHeight()));
				stockText.setX(base.getX() + 0.07 * base.getWidth());
				stockText.setY(base.getY() + 0.4 * base.getHeight());
				stockText.setFill(Color.WHITE);

				TextField enteredStock = new TextField();
				enteredStock.setPromptText("INDEXSP:.INX");
				enteredStock.setFont(Font.font("Open Sans", 20.0 / 720 * Util.getScreenHeight()));
				enteredStock.setMinWidth(0.12 * Util.getScreenWidth());
				enteredStock.setMaxWidth(0.12 * Util.getScreenWidth());
				enteredStock.setTranslateX(
						stockText.getX() + stockText.getLayoutBounds().getWidth() + 0.2 * base.getWidth());
				enteredStock.setTranslateY(stockText.getY() - stockText.getLayoutBounds().getHeight());

				Group cancelButton = new Group();

				Rectangle cancel = new Rectangle(stockText.getX() + 0.15 * base.getWidth(),
						stockText.getY() + 0.23 * base.getHeight(), 0.25 * base.getWidth(), 0.3 * base.getHeight());
				cancel.setFill(Util.BIG_LOSS_COLOR);
				cancel.setEffect(baseShadow);
				cancel.setArcHeight(cancel.getHeight());
				cancel.setArcWidth(cancel.getHeight());

				Text cancelText = new Text("Cancel");
				cancelText.setFont(Font.font("Open Sans", 0.6 * cancel.getHeight() / 1.333));
				cancelText.setFill(Color.WHITE);
				cancelText
						.setX(cancel.getX() + 0.5 * cancel.getWidth() - 0.5 * cancelText.getLayoutBounds().getWidth());
				cancelText.setTextOrigin(VPos.CENTER);
				cancelText.setY(cancel.getY() + 0.5 * cancel.getHeight());

				cancelButton.getChildren().addAll(cancel, cancelText);
				cancelButton.setOnMouseClicked(new EventHandler<MouseEvent>() {

					@Override
					public void handle(MouseEvent event) {
						root.getChildren().remove(overlayMenu);
					}

				});

				Group addButton = new Group();

				Rectangle add = new Rectangle(cancel.getX() + cancel.getWidth() + 0.1 * base.getWidth(), cancel.getY(),
						0.25 * base.getWidth(), cancel.getHeight());
				add.setFill(Util.BIG_GAIN_COLOR);
				add.setEffect(baseShadow);
				add.setArcHeight(add.getHeight());
				add.setArcWidth(add.getHeight());

				Text addText = new Text("Save");
				addText.setFont(Font.font("Open Sans", 0.6 * add.getHeight() / 1.333));
				addText.setFill(Color.WHITE);
				addText.setX(add.getX() + 0.5 * add.getWidth() - 0.5 * addText.getLayoutBounds().getWidth());
				addText.setTextOrigin(VPos.CENTER);
				addText.setY(add.getY() + 0.5 * add.getHeight());

				addButton.getChildren().addAll(add, addText);
				addButton.setOnMouseClicked(new EventHandler<MouseEvent>() {

					@Override
					public void handle(MouseEvent event) {
						String stock = enteredStock.getText().trim();
						if (stockHandler.contains(stock.toUpperCase())
								&& !stocks.containsKey(stockHandler.find(stock.toUpperCase()))) {
							LinkedList<PortfolioDataPoint> points = new LinkedList<PortfolioDataPoint>();
							points.add(new PortfolioDataPoint(new GregorianCalendar(),
									stockHandler.find(stock.toUpperCase()), 0, 0));

							stocks.put(stockHandler.find(stock.toUpperCase()), points);
						}

						refresh();
					}
				});

				overlayMenu.getChildren().addAll(base, stockText, enteredStock, cancelButton, addButton);

				add(overlayMenu);
			}

		};

		return handler;
	}

	private void refresh() {
		writeData();

		stocks = new LinkedHashMap<>();
		pane.getChildren().clear();
		root.getChildren().remove(overlayMenu);
		root.getChildren().remove(pane);

		root.getChildren().clear();

		draw();
	}

	private void writeData() {
		try {
			FileWriter fw = new FileWriter(Util.RAW_PORTFOLIO_DATA_PATH, false);

			for (Stock stock : stocks.keySet()) {
				for (PortfolioDataPoint data : stocks.get(stock)) {
					fw.write(stock.getSymbol() + ";" + data.getDate().get(Calendar.YEAR) + ";"
							+ data.getDate().get(Calendar.MONTH) + ";" + data.getDate().get(Calendar.DAY_OF_MONTH) + ";"
							+ data.getPrice() + ";" + data.getQuantity() + "\n");
				}
			}

			fw.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
