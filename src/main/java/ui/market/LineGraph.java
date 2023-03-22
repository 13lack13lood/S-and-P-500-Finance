package ui.market;

import java.math.BigDecimal;
import java.math.MathContext;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map.Entry;

import application.Util;
import javafx.event.EventHandler;
import javafx.geometry.VPos;
import javafx.scene.Group;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import stock.comparators.SortDataPointPrice;
import stock.data.Stock;
import stock.data.StockDataPoint;
import ui.UserInterface;
/**
 * 
 * A class that generates a line graph given the historical data
 *
 */
public class LineGraph extends UserInterface {

	private static enum Time {
		WEEK, MONTH, QUARTER_YEAR, HALF_YEAR, YEAR_TO_DATE, YEAR
	}

	private static Time timeFrame = Time.WEEK;

	private HashMap<Time, ArrayList<StockDataPoint>> dataPerTimeFrame;

	private Stock stock;
	private HashMap<Text, Line> gridlines;

	private Text change;
	private Text price;

	private Rectangle graphBase;
	private Rectangle titleBase;

	private Group top;
	private Group graph;
	private Group dataPointInfo;

	private double gridStartX;
	private double scaleBottomValue;
	private double scaleBottomPos;
	private double gridPixels;
	private double scaleDifference;
	private double gridLineWidth;
	/**
	 * 
	 * @param stage
	 * @param stock
	 */
	public LineGraph(Stage stage, Stock stock) {
		super(stage);
		this.stock = stock;

		findPoints();

		draw();
	}

	@Override
	protected void draw() {
		graphBase = new Rectangle(0.03 * Util.getScreenWidth(), 0.23 * Util.getScreenHeight(),
				0.5 * Util.getScreenWidth(), 0.72 * Util.getScreenHeight());
		graphBase.setFill(Util.PRIMARY_COLOR);
		graphBase.setArcHeight(20);
		graphBase.setArcWidth(20);

		DropShadow shadow = new DropShadow();
		shadow.setOffsetX(2);
		shadow.setOffsetY(4);

		graphBase.setEffect(shadow);

		add(graphBase);

		createTop();
		createGraph();
		createButtons();
		createInfo();
	}

	private void findPoints() {
		dataPerTimeFrame = new HashMap<>();

		ArrayList<StockDataPoint> data = stock.getHistoricalData();

		GregorianCalendar today = new GregorianCalendar();

		ArrayList<StockDataPoint> copy = new ArrayList<>();

		for (int i = data.size() - 1; i > data.size() - 1 - 5; i--) {
			copy.add(data.get(i));
		}

		Collections.reverse(copy);

		dataPerTimeFrame.put(Time.WEEK, copy);

		copy = new ArrayList<>();

		copy.add(data.get(data.size() - 1));

		for (int i = data.size() - 2; i > data.size() - 1 - 22; i--) {
			StockDataPoint point = data.get(i);

			if (point.getDate().get(GregorianCalendar.DAY_OF_MONTH) == today.get(GregorianCalendar.DAY_OF_MONTH))
				break;

			copy.add(point);
		}

		Collections.reverse(copy);

		dataPerTimeFrame.put(Time.MONTH, copy);

		copy = new ArrayList<>();

		for (int i = data.size() - 1; i > data.size() - 1 - data.size() / 4.0; i--) {
			copy.add(data.get(i));
		}

		Collections.reverse(copy);

		dataPerTimeFrame.put(Time.QUARTER_YEAR, copy);

		copy = new ArrayList<>();

		for (int i = data.size() - 1; i > data.size() - 1 - data.size() / 2.0; i--) {

			copy.add(data.get(i));
		}

		Collections.reverse(copy);

		dataPerTimeFrame.put(Time.HALF_YEAR, copy);

		copy = new ArrayList<>();

		for (int i = data.size() - 1; i >= 0; i--) {
			if (data.get(i).getDate().get(GregorianCalendar.YEAR) == today.get(GregorianCalendar.YEAR)) {
				copy.add(data.get(i));
			} else
				break;
		}

		Collections.reverse(copy);

		dataPerTimeFrame.put(Time.YEAR_TO_DATE, copy);

		copy = new ArrayList<>();

		for (StockDataPoint point : data)
			copy.add(point);

		dataPerTimeFrame.put(Time.YEAR, copy);
	}

	private void createButtons() {
		Group buttons = new Group();

		Text week = new Text("5D");
		week.setFill(Color.WHITE);
		week.setFont(Font.font("Open Sans", Math.round(0.06 * graphBase.getHeight() / 1.333)));
		week.setX(graphBase.getX() + 0.06 * graphBase.getWidth());
		week.setY(graphBase.getY() + 0.2 * graphBase.getHeight());

		Line highlight = new Line(week.getX(), week.getY() + 0.01 * graphBase.getHeight(),
				week.getX() + week.getLayoutBounds().getWidth(), week.getY() + 0.01 * graphBase.getHeight());
		highlight.setStroke(Util.ACCENT_COLOR);
		highlight.setStrokeWidth(3.0 / 720 * Util.getScreenHeight());

		week.setOnMouseClicked(timeFrameClick(week, Time.WEEK, highlight));

		buttons.getChildren().addAll(week, highlight);

		Text month = new Text("1M");
		month.setFill(Color.WHITE);
		month.setFont(week.getFont());
		month.setX(week.getX() + week.getLayoutBounds().getWidth() + 0.03 * graphBase.getWidth());
		month.setY(week.getY());
		month.setOnMouseClicked(timeFrameClick(month, Time.MONTH, highlight));

		buttons.getChildren().add(month);

		Line seperateWeekMonth = new Line((week.getX() + week.getLayoutBounds().getWidth() + month.getX()) / 2.0,
				month.getLayoutBounds().getCenterY() - month.getLayoutBounds().getWidth() / 2.0,
				(week.getX() + week.getLayoutBounds().getWidth() + month.getX()) / 2.0,
				month.getLayoutBounds().getCenterY() + month.getLayoutBounds().getWidth() / 2.0);
		seperateWeekMonth.setStroke(Color.WHITE);
		seperateWeekMonth.setStrokeWidth(1);

		buttons.getChildren().add(seperateWeekMonth);

		Text quarter = new Text("3M");
		quarter.setFill(Color.WHITE);
		quarter.setFont(week.getFont());
		quarter.setX(month.getX() + month.getLayoutBounds().getWidth() + 0.03 * graphBase.getWidth());
		quarter.setY(week.getY());
		quarter.setOnMouseClicked(timeFrameClick(quarter, Time.QUARTER_YEAR, highlight));

		buttons.getChildren().add(quarter);

		Line seperateMonthQuarter = new Line((month.getX() + month.getLayoutBounds().getWidth() + quarter.getX()) / 2.0,
				seperateWeekMonth.getStartY(),
				(month.getX() + month.getLayoutBounds().getWidth() + quarter.getX()) / 2.0,
				seperateWeekMonth.getEndY());
		seperateMonthQuarter.setStroke(Color.WHITE);
		seperateMonthQuarter.setStrokeWidth(1);

		buttons.getChildren().add(seperateMonthQuarter);

		Text half = new Text("6M");
		half.setFill(Color.WHITE);
		half.setFont(week.getFont());
		half.setX(quarter.getX() + quarter.getLayoutBounds().getWidth() + 0.03 * graphBase.getWidth());
		half.setY(week.getY());
		half.setOnMouseClicked(timeFrameClick(half, Time.HALF_YEAR, highlight));

		buttons.getChildren().add(half);

		Line seperateQuarterHalf = new Line((quarter.getX() + quarter.getLayoutBounds().getWidth() + half.getX()) / 2.0,
				seperateWeekMonth.getStartY(),
				(quarter.getX() + quarter.getLayoutBounds().getWidth() + half.getX()) / 2.0,
				seperateWeekMonth.getEndY());
		seperateQuarterHalf.setStroke(Color.WHITE);
		seperateQuarterHalf.setStrokeWidth(1);

		buttons.getChildren().add(seperateQuarterHalf);

		Text ytd = new Text("YTD");
		ytd.setFill(Color.WHITE);
		ytd.setFont(week.getFont());
		ytd.setX(half.getX() + half.getLayoutBounds().getWidth() + 0.03 * graphBase.getWidth());
		ytd.setY(week.getY());
		ytd.setOnMouseClicked(timeFrameClick(ytd, Time.YEAR_TO_DATE, highlight));

		buttons.getChildren().add(ytd);

		Line seperateHalfYTD = new Line((half.getX() + half.getLayoutBounds().getWidth() + ytd.getX()) / 2.0,
				seperateWeekMonth.getStartY(), (half.getX() + half.getLayoutBounds().getWidth() + ytd.getX()) / 2.0,
				seperateWeekMonth.getEndY());
		seperateHalfYTD.setStroke(Color.WHITE);
		seperateHalfYTD.setStrokeWidth(1);

		buttons.getChildren().add(seperateHalfYTD);

		Text year = new Text("1Y");
		year.setFill(Color.WHITE);
		year.setFont(week.getFont());
		year.setX(ytd.getX() + ytd.getLayoutBounds().getWidth() + 0.03 * graphBase.getWidth());
		year.setY(week.getY());
		year.setOnMouseClicked(timeFrameClick(year, Time.YEAR, highlight));

		buttons.getChildren().add(year);

		Line seperateYTDYear = new Line((ytd.getX() + ytd.getLayoutBounds().getWidth() + year.getX()) / 2.0,
				seperateWeekMonth.getStartY(), (ytd.getX() + ytd.getLayoutBounds().getWidth() + year.getX()) / 2.0,
				seperateWeekMonth.getEndY());
		seperateYTDYear.setStroke(Color.WHITE);
		seperateYTDYear.setStrokeWidth(1);

		buttons.getChildren().add(seperateYTDYear);

		add(buttons);
	}

	private EventHandler<MouseEvent> timeFrameClick(Text button, Time newTimeFrame, Line line) {
		EventHandler<MouseEvent> handler = new EventHandler<>() {

			@Override
			public void handle(MouseEvent event) {
				line.setStartX(button.getX());
				line.setEndX(button.getX() + button.getLayoutBounds().getWidth());
				timeFrame = newTimeFrame;

				createChangeText();

				root.getChildren().remove(graph);

				createGraph();
			}

		};

		return handler;
	}

	private void createGraph() {
		graph = new Group();

		calcGridLines();

		for (Entry<Text, Line> entry : gridlines.entrySet()) {
			graph.getChildren().addAll(entry.getKey(), entry.getValue());
		}

		ArrayList<StockDataPoint> points = dataPerTimeFrame.get(timeFrame);

		ArrayList<Double> pointXCoords = new ArrayList<>();

		double increment = gridLineWidth / (points.size() - 1);

		for (double i = 0, x = gridStartX; i < points.size() - 1; i++, x += increment) {
			double leftY = scaleBottomPos
					- (points.get((int) i).getClose() - scaleBottomValue) / scaleDifference * gridPixels;
			double rightY = scaleBottomPos
					- (points.get((int) i + 1).getClose() - scaleBottomValue) / scaleDifference * gridPixels;

			Line line = new Line(x, leftY, x + increment, rightY);
			line.setStrokeWidth(1.0 / 720.0 * Util.getScreenHeight());
			line.setStroke(Util.ACCENT_COLOR);

			graph.getChildren().add(line);

			pointXCoords.add(x);
		}

		createStockDataPointInfo(dataPerTimeFrame.get(timeFrame).get(dataPerTimeFrame.get(timeFrame).size() - 1));

		pointXCoords.add(pointXCoords.get(pointXCoords.size() - 1) + increment);

		graph.setOnMouseMoved(graphHover(pointXCoords));

		root.getChildren().add(graph);
	}

	private void calcGridLines() {
		gridlines = new HashMap<>();

		ArrayList<StockDataPoint> points = dataPerTimeFrame.get(timeFrame);

		ArrayList<StockDataPoint> copy = new ArrayList<StockDataPoint>();
		for (StockDataPoint point : points)
			copy.add(point);

		copy.sort(new SortDataPointPrice());

		double low = Math.floor(copy.get(0).getClose());

		BigDecimal round = new BigDecimal(low);
		round = round.round(new MathContext(2));
		low = round.doubleValue();
		low -= Math.pow(10, round.toPlainString().length() - 2);

		double high = Math.ceil(copy.get(copy.size() - 1).getClose());

		round = new BigDecimal(high);
		round = round.round(new MathContext(2));
		high = round.doubleValue();
		high += Math.pow(10, (round.toPlainString().length() - 2));

		double difference = high - low;

		double widest = Double.MIN_VALUE;

		ArrayList<Text> list = new ArrayList<>();

		DecimalFormat format = new DecimalFormat("0.00");

		for (double y = high, totalPixels = 0; y >= low; y -= (high - low) / 5.0, totalPixels += 45.0 / 720
				* Util.getScreenHeight()) {
			Text value = new Text("$" + format.format(y));
			value.setFont(Font.font("Open Sans", Math.round(0.04 * graphBase.getHeight() / 1.333)));
			value.setX(graphBase.getX() + 0.02 * graphBase.getWidth());
			value.setTextOrigin(VPos.CENTER);
			value.setY(graphBase.getY() + 0.27 * graphBase.getHeight() + totalPixels);
			value.setFill(Color.WHITE);

			widest = Math.max(widest, value.getX() + value.getLayoutBounds().getWidth() + 0.02 * graphBase.getWidth());

			list.add(value);
		}

		double lineLength = 0;
		double lowest = Double.MIN_VALUE;

		for (Text value : list) {
			Line gridline = new Line(widest, value.getY(), graphBase.getX() + 0.98 * graphBase.getWidth(),
					value.getY());
			gridline.setStrokeWidth(1.0 / 720.0 * Util.getScreenHeight());
			gridline.setStroke(Color.WHITE);

			gridlines.put(value, gridline);

			lineLength = gridline.getEndX() - gridline.getStartX();
			lowest = Math.max(value.getY(), lowest);
		}

		gridLineWidth = lineLength;
		scaleBottomValue = low;
		gridPixels = 5 * 45.0 / 720 * Util.getScreenHeight();
		gridStartX = widest;
		scaleDifference = difference;
		scaleBottomPos = lowest;
	}

	private EventHandler<MouseEvent> graphHover(ArrayList<Double> xCoords) {
		EventHandler<MouseEvent> handler = new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				double x = event.getX();

				int index = Collections.binarySearch(xCoords, x);

				if (index < 0) {
					index = (index + 1) * -1;

					if (index < xCoords.size() - 1 && index > 0) {
						double higher = xCoords.get(index);
						double lower = xCoords.get(index - 1);

						index = (higher - x < x - lower) ? index : index - 1;
					}
				}

				createStockDataPointInfo(dataPerTimeFrame.get(timeFrame).get(index));
			}

		};

		return handler;
	}

	private void createStockDataPointInfo(StockDataPoint data) {
		root.getChildren().remove(dataPointInfo);

		dataPointInfo = new Group();

		DropShadow shadow = new DropShadow();
		shadow.setOffsetX(4);
		shadow.setOffsetY(4);

		Rectangle dateBase = new Rectangle(gridStartX, scaleBottomPos + 0.06 * graphBase.getHeight(),
				0.27 * graphBase.getWidth(), 0.07 * graphBase.getHeight());
		dateBase.setFill(Util.ACCENT_COLOR);
		dateBase.setEffect(shadow);
		dateBase.setArcHeight(dateBase.getHeight());
		dateBase.setArcWidth(dateBase.getHeight());

		Text date = new Text(Util.MONTHS[data.getDate().get(Calendar.MONTH)] + " "
				+ data.getDate().get(Calendar.DAY_OF_MONTH) + ", " + data.getDate().get(Calendar.YEAR));
		date.setFont(Font.font("Open Sans", dateBase.getHeight() * 0.63 / 1.333));
		date.setX(dateBase.getX() + dateBase.getWidth() / 2 - date.getLayoutBounds().getWidth() / 2);
		date.setTextOrigin(VPos.CENTER);
		date.setY(dateBase.getY() + dateBase.getHeight() / 2);
		date.setFill(Color.WHITE);

		dataPointInfo.getChildren().addAll(dateBase, date);

		Rectangle volumeBase = new Rectangle(dateBase.getX(),
				dateBase.getY() + dateBase.getHeight() + 0.02 * graphBase.getHeight(), dateBase.getWidth(),
				dateBase.getHeight());
		volumeBase.setFill(Util.SECONDARY_COLOR);
		volumeBase.setEffect(shadow);
		volumeBase.setArcHeight(volumeBase.getHeight());
		volumeBase.setArcWidth(volumeBase.getHeight());

		Text volume = new Text("Volume: " + ((stock.getVolumeAvg() == Long.MIN_VALUE || stock.getVolumeAvg() == 0) ? "-"
				: Long.toString(stock.getVolumeAvg())));
		volume.setFont(Font.font("Open Sans", volumeBase.getHeight() * 0.62 / 1.333));
		volume.setX(volumeBase.getX() + volumeBase.getWidth() / 2 - volume.getLayoutBounds().getWidth() / 2);
		volume.setTextOrigin(VPos.CENTER);
		volume.setY(volumeBase.getY() + volumeBase.getHeight() / 2);
		volume.setFill(Color.WHITE);

		dataPointInfo.getChildren().addAll(volumeBase, volume);

		Rectangle openBase = new Rectangle(graphBase.getX() + 0.45 * graphBase.getWidth(), dateBase.getY(),
				0.23 * graphBase.getWidth(), dateBase.getHeight());
		openBase.setFill(Util.SECONDARY_COLOR);
		openBase.setEffect(shadow);
		openBase.setArcHeight(openBase.getHeight());
		openBase.setArcWidth(openBase.getHeight());

		Text open = new Text("Open: $" + checkData(data.getOpen()));
		open.setFont(Font.font("Open Sans", openBase.getHeight() * 0.62 / 1.333));
		open.setX(openBase.getX() + openBase.getWidth() / 2 - open.getLayoutBounds().getWidth() / 2);
		open.setTextOrigin(VPos.CENTER);
		open.setY(openBase.getY() + openBase.getHeight() / 2);
		open.setFill(Color.WHITE);

		dataPointInfo.getChildren().addAll(openBase, open);

		Rectangle closeBase = new Rectangle(graphBase.getX() + 0.45 * graphBase.getWidth(), volumeBase.getY(),
				0.23 * graphBase.getWidth(), dateBase.getHeight());
		closeBase.setFill(Util.SECONDARY_COLOR);
		closeBase.setEffect(shadow);
		closeBase.setArcHeight(closeBase.getHeight());
		closeBase.setArcWidth(closeBase.getHeight());

		Text close = new Text("Close: $" + checkData(data.getClose()));
		close.setFont(Font.font("Open Sans", closeBase.getHeight() * 0.62 / 1.333));
		close.setX(closeBase.getX() + closeBase.getWidth() / 2 - close.getLayoutBounds().getWidth() / 2);
		close.setTextOrigin(VPos.CENTER);
		close.setY(closeBase.getY() + closeBase.getHeight() / 2);
		close.setFill(Color.WHITE);

		dataPointInfo.getChildren().addAll(closeBase, close);

		Rectangle highBase = new Rectangle(graphBase.getX() + 0.72 * graphBase.getWidth(), dateBase.getY(),
				0.23 * graphBase.getWidth(), dateBase.getHeight());
		highBase.setFill(Util.SECONDARY_COLOR);
		highBase.setEffect(shadow);
		highBase.setArcHeight(highBase.getHeight());
		highBase.setArcWidth(highBase.getHeight());

		Text high = new Text("High: $" + checkData(data.getHigh()));
		high.setFont(Font.font("Open Sans", highBase.getHeight() * 0.62 / 1.333));
		high.setX(highBase.getX() + highBase.getWidth() / 2 - high.getLayoutBounds().getWidth() / 2);
		high.setTextOrigin(VPos.CENTER);
		high.setY(highBase.getY() + highBase.getHeight() / 2);
		high.setFill(Color.WHITE);

		dataPointInfo.getChildren().addAll(highBase, high);

		Rectangle lowBase = new Rectangle(graphBase.getX() + 0.72 * graphBase.getWidth(), volumeBase.getY(),
				0.23 * graphBase.getWidth(), dateBase.getHeight());
		lowBase.setFill(Util.SECONDARY_COLOR);
		lowBase.setEffect(shadow);
		lowBase.setArcHeight(lowBase.getHeight());
		lowBase.setArcWidth(lowBase.getHeight());

		Text low = new Text("Low: $" + checkData(data.getLow()));
		low.setFont(Font.font("Open Sans", lowBase.getHeight() * 0.62 / 1.333));
		low.setX(lowBase.getX() + lowBase.getWidth() / 2 - low.getLayoutBounds().getWidth() / 2);
		low.setTextOrigin(VPos.CENTER);
		low.setY(lowBase.getY() + lowBase.getHeight() / 2);
		low.setFill(Color.WHITE);

		dataPointInfo.getChildren().addAll(lowBase, low);

		add(dataPointInfo);
	}

	private void createInfo() {
		Group info = new Group();

		GregorianCalendar today = new GregorianCalendar();

		DropShadow shadow = new DropShadow();
		shadow.setOffsetX(4);
		shadow.setOffsetY(4);

		Rectangle dateBase = new Rectangle(0.65 * Util.getScreenWidth(), graphBase.getY(), 0.5 * graphBase.getWidth(),
				0.07 * graphBase.getHeight());
		dateBase.setFill(Util.ACCENT_COLOR);
		dateBase.setEffect(shadow);
		dateBase.setArcHeight(dateBase.getHeight());
		dateBase.setArcWidth(dateBase.getHeight());

		Text date = new Text(Util.MONTHS[today.get(Calendar.MONTH)] + " " + today.get(Calendar.DAY_OF_MONTH) + ", "
				+ today.get(Calendar.YEAR));
		date.setFont(Font.font("Open Sans", dateBase.getHeight() * 0.63 / 1.333));
		date.setX(dateBase.getX() + dateBase.getWidth() / 2 - date.getLayoutBounds().getWidth() / 2);
		date.setTextOrigin(VPos.CENTER);
		date.setY(dateBase.getY() + dateBase.getHeight() / 2);
		date.setFill(Color.WHITE);

		info.getChildren().addAll(dateBase, date);

		Rectangle priceBase = new Rectangle(graphBase.getX() + graphBase.getWidth() + 0.03 * Util.getScreenWidth(),
				graphBase.getY() + dateBase.getHeight() + 0.03 * Util.getScreenHeight(), 0.12 * Util.getScreenWidth(),
				0.13 * Util.getScreenHeight());
		priceBase.setFill(Util.PRIMARY_COLOR);
		priceBase.setEffect(shadow);
		priceBase.setArcHeight(10);
		priceBase.setArcWidth(10);

		Text priceName = new Text("Price");
		priceName.setFont(Font.font("Open Sans", priceBase.getHeight() * 0.28 / 1.333));
		priceName.setX(priceBase.getX() + priceBase.getWidth() * 0.07);
		priceName.setY(priceBase.getY() + priceBase.getHeight() * 0.3);
		priceName.setFill(Color.WHITE);

		Text price = new Text("$" + checkData(stock.getPrice()));
		price.setFont(Font.font("Open Sans", priceBase.getHeight() * 0.35 / 1.333));
		price.setX(priceName.getX());
		price.setY(priceBase.getY() + priceBase.getHeight() * 0.75);
		price.setFill(Color.WHITE);

		info.getChildren().addAll(priceBase, priceName, price);

		Rectangle openBase = new Rectangle(priceBase.getX() + priceBase.getWidth() + 0.03 * Util.getScreenWidth(),
				graphBase.getY() + dateBase.getHeight() + 0.03 * Util.getScreenHeight(), 0.12 * Util.getScreenWidth(),
				0.13 * Util.getScreenHeight());
		openBase.setFill(Util.PRIMARY_COLOR);
		openBase.setEffect(shadow);
		openBase.setArcHeight(10);
		openBase.setArcWidth(10);

		Text openName = new Text("Open");
		openName.setFont(Font.font("Open Sans", openBase.getHeight() * 0.28 / 1.333));
		openName.setX(openBase.getX() + openBase.getWidth() * 0.07);
		openName.setY(openBase.getY() + openBase.getHeight() * 0.3);
		openName.setFill(Color.WHITE);

		Text open = new Text("$" + checkData(stock.getPriceOpen()));
		open.setFont(Font.font("Open Sans", openBase.getHeight() * 0.35 / 1.333));
		open.setX(openName.getX());
		open.setY(openBase.getY() + openBase.getHeight() * 0.75);
		open.setFill(Color.WHITE);

		info.getChildren().addAll(openBase, openName, open);

		Rectangle changeBase = new Rectangle(openBase.getX() + openBase.getWidth() + 0.03 * Util.getScreenWidth(),
				graphBase.getY() + dateBase.getHeight() + 0.03 * Util.getScreenHeight(), 0.12 * Util.getScreenWidth(),
				0.13 * Util.getScreenHeight());
		changeBase.setFill(Util.PRIMARY_COLOR);
		changeBase.setEffect(shadow);
		changeBase.setArcHeight(10);
		changeBase.setArcWidth(10);

		Text changeName = new Text("Change");
		changeName.setFont(Font.font("Open Sans", changeBase.getHeight() * 0.28 / 1.333));
		changeName.setX(changeBase.getX() + changeBase.getWidth() * 0.07);
		changeName.setY(changeBase.getY() + changeBase.getHeight() * 0.3);
		changeName.setFill(Color.WHITE);

		Text change = new Text(checkData(stock.getChangePercentage()) + "%");
		change.setFont(Font.font("Open Sans", changeBase.getHeight() * 0.35 / 1.333));
		change.setX(changeName.getX());
		change.setY(changeBase.getY() + changeBase.getHeight() * 0.75);
		change.setFill(Color.WHITE);

		info.getChildren().addAll(changeBase, changeName, change);

		Rectangle highBase = new Rectangle(priceBase.getX(),
				priceBase.getY() + priceBase.getHeight() + 0.03 * Util.getScreenHeight(), 0.12 * Util.getScreenWidth(),
				0.13 * Util.getScreenHeight());
		highBase.setFill(Util.PRIMARY_COLOR);
		highBase.setEffect(shadow);
		highBase.setArcHeight(10);
		highBase.setArcWidth(10);

		Text highName = new Text("High");
		highName.setFont(Font.font("Open Sans", highBase.getHeight() * 0.28 / 1.333));
		highName.setX(highBase.getX() + highBase.getWidth() * 0.07);
		highName.setY(highBase.getY() + highBase.getHeight() * 0.3);
		highName.setFill(Color.WHITE);

		Text high = new Text("$" + checkData(stock.getPriceHigh()));
		high.setFont(Font.font("Open Sans", highBase.getHeight() * 0.35 / 1.333));
		high.setX(highName.getX());
		high.setY(highBase.getY() + highBase.getHeight() * 0.75);
		high.setFill(Color.WHITE);

		info.getChildren().addAll(highBase, highName, high);

		Rectangle lowBase = new Rectangle(openBase.getX(), highBase.getY(), 0.12 * Util.getScreenWidth(),
				0.13 * Util.getScreenHeight());
		lowBase.setFill(Util.PRIMARY_COLOR);
		lowBase.setEffect(shadow);
		lowBase.setArcHeight(10);
		lowBase.setArcWidth(10);

		Text lowName = new Text("Low");
		lowName.setFont(Font.font("Open Sans", lowBase.getHeight() * 0.28 / 1.333));
		lowName.setX(lowBase.getX() + lowBase.getWidth() * 0.07);
		lowName.setY(lowBase.getY() + lowBase.getHeight() * 0.3);
		lowName.setFill(Color.WHITE);

		Text low = new Text("$" + checkData(stock.getPriceLow()));
		low.setFont(Font.font("Open Sans", lowBase.getHeight() * 0.35 / 1.333));
		low.setX(lowName.getX());
		low.setY(lowBase.getY() + lowBase.getHeight() * 0.75);
		low.setFill(Color.WHITE);

		info.getChildren().addAll(lowBase, lowName, low);

		Rectangle volumeBase = new Rectangle(changeBase.getX(), highBase.getY(), 0.12 * Util.getScreenWidth(),
				0.13 * Util.getScreenHeight());
		volumeBase.setFill(Util.PRIMARY_COLOR);
		volumeBase.setEffect(shadow);
		volumeBase.setArcHeight(10);
		volumeBase.setArcWidth(10);

		Text volumeName = new Text("Volume");
		volumeName.setFont(Font.font("Open Sans", volumeBase.getHeight() * 0.28 / 1.333));
		volumeName.setX(volumeBase.getX() + volumeBase.getWidth() * 0.07);
		volumeName.setY(volumeBase.getY() + volumeBase.getHeight() * 0.3);
		volumeName.setFill(Color.WHITE);

		Text volume = new Text((stock.getVolume() == Long.MIN_VALUE || stock.getVolume() == 0) ? "-"
				: Long.toString(stock.getVolume()));
		volume.setFont(Font.font("Open Sans", volumeBase.getHeight() * 0.35 / 1.333));
		volume.setX(volumeName.getX());
		volume.setY(volumeBase.getY() + volumeBase.getHeight() * 0.75);
		volume.setFill(Color.WHITE);

		info.getChildren().addAll(volumeBase, volumeName, volume);

		Rectangle high52Base = new Rectangle(priceBase.getX(),
				highBase.getY() + highBase.getHeight() + 0.03 * Util.getScreenHeight(), 0.12 * Util.getScreenWidth(),
				0.13 * Util.getScreenHeight());
		high52Base.setFill(Util.PRIMARY_COLOR);
		high52Base.setEffect(shadow);
		high52Base.setArcHeight(10);
		high52Base.setArcWidth(10);

		Text high52Name = new Text("52w High");
		high52Name.setFont(Font.font("Open Sans", high52Base.getHeight() * 0.28 / 1.333));
		high52Name.setX(high52Base.getX() + high52Base.getWidth() * 0.07);
		high52Name.setY(high52Base.getY() + high52Base.getHeight() * 0.3);
		high52Name.setFill(Color.WHITE);

		Text high52 = new Text("$" + checkData(stock.getHigh52()));
		high52.setFont(Font.font("Open Sans", high52Base.getHeight() * 0.35 / 1.333));
		high52.setX(high52Name.getX());
		high52.setY(high52Base.getY() + high52Base.getHeight() * 0.75);
		high52.setFill(Color.WHITE);

		info.getChildren().addAll(high52Base, high52Name, high52);

		Rectangle low52Base = new Rectangle(openBase.getX(), high52Base.getY(), 0.12 * Util.getScreenWidth(),
				0.13 * Util.getScreenHeight());
		low52Base.setFill(Util.PRIMARY_COLOR);
		low52Base.setEffect(shadow);
		low52Base.setArcHeight(10);
		low52Base.setArcWidth(10);

		Text low52Name = new Text("52w Low");
		low52Name.setFont(Font.font("Open Sans", low52Base.getHeight() * 0.28 / 1.333));
		low52Name.setX(low52Base.getX() + low52Base.getWidth() * 0.07);
		low52Name.setY(low52Base.getY() + low52Base.getHeight() * 0.3);
		low52Name.setFill(Color.WHITE);

		Text low52 = new Text("$" + checkData(stock.getLow52()));
		low52.setFont(Font.font("Open Sans", low52Base.getHeight() * 0.35 / 1.333));
		low52.setX(low52Name.getX());
		low52.setY(low52Base.getY() + low52Base.getHeight() * 0.75);
		low52.setFill(Color.WHITE);

		info.getChildren().addAll(low52Base, low52Name, low52);

		Rectangle volumeavgBase = new Rectangle(changeBase.getX(), high52Base.getY(), 0.12 * Util.getScreenWidth(),
				0.13 * Util.getScreenHeight());
		volumeavgBase.setFill(Util.PRIMARY_COLOR);
		volumeavgBase.setEffect(shadow);
		volumeavgBase.setArcHeight(10);
		volumeavgBase.setArcWidth(10);

		Text volumeavgName = new Text("Volume Avg");
		volumeavgName.setFont(Font.font("Open Sans", volumeavgBase.getHeight() * 0.28 / 1.333));
		volumeavgName.setX(volumeavgBase.getX() + volumeavgBase.getWidth() * 0.07);
		volumeavgName.setY(volumeavgBase.getY() + volumeavgBase.getHeight() * 0.3);
		volumeavgName.setFill(Color.WHITE);

		Text volumeavg = new Text((stock.getVolumeAvg() == Long.MIN_VALUE || stock.getVolumeAvg() == 0) ? "-"
				: Long.toString(stock.getVolumeAvg()));
		volumeavg.setFont(Font.font("Open Sans", volumeavgBase.getHeight() * 0.35 / 1.333));
		volumeavg.setX(volumeavgName.getX());
		volumeavg.setY(volumeavgBase.getY() + volumeavgBase.getHeight() * 0.75);
		volumeavg.setFill(Color.WHITE);

		info.getChildren().addAll(volumeavgBase, volumeavgName, volumeavg);

		Rectangle epBase = new Rectangle(priceBase.getX(),
				high52Base.getY() + high52Base.getHeight() + 0.03 * Util.getScreenHeight(),
				0.12 * Util.getScreenWidth(), 0.13 * Util.getScreenHeight());
		epBase.setFill(Util.PRIMARY_COLOR);
		epBase.setEffect(shadow);
		epBase.setArcHeight(10);
		epBase.setArcWidth(10);

		Text epName = new Text("EPS");
		epName.setFont(Font.font("Open Sans", epBase.getHeight() * 0.28 / 1.333));
		epName.setX(epBase.getX() + epBase.getWidth() * 0.07);
		epName.setY(epBase.getY() + epBase.getHeight() * 0.3);
		epName.setFill(Color.WHITE);

		Text ep = new Text("$" + checkData(stock.getEps()));
		ep.setFont(Font.font("Open Sans", epBase.getHeight() * 0.35 / 1.333));
		ep.setX(epName.getX());
		ep.setY(epBase.getY() + epBase.getHeight() * 0.75);
		ep.setFill(Color.WHITE);

		info.getChildren().addAll(epBase, epName, ep);

		Rectangle peBase = new Rectangle(openBase.getX(), epBase.getY(), 0.12 * Util.getScreenWidth(),
				0.13 * Util.getScreenHeight());
		peBase.setFill(Util.PRIMARY_COLOR);
		peBase.setEffect(shadow);
		peBase.setArcHeight(10);
		peBase.setArcWidth(10);

		Text peName = new Text("PE Ratio");
		peName.setFont(Font.font("Open Sans", peBase.getHeight() * 0.28 / 1.333));
		peName.setX(peBase.getX() + peBase.getWidth() * 0.07);
		peName.setY(peBase.getY() + peBase.getHeight() * 0.3);
		peName.setFill(Color.WHITE);

		Text pe = new Text(checkData(stock.getPe()));
		pe.setFont(Font.font("Open Sans", peBase.getHeight() * 0.35 / 1.333));
		pe.setX(peName.getX());
		pe.setY(peBase.getY() + peBase.getHeight() * 0.75);
		pe.setFill(Color.WHITE);

		info.getChildren().addAll(peBase, peName, pe);

		Rectangle marketCapBase = new Rectangle(changeBase.getX(), epBase.getY(), 0.12 * Util.getScreenWidth(),
				0.13 * Util.getScreenHeight());
		marketCapBase.setFill(Util.PRIMARY_COLOR);
		marketCapBase.setEffect(shadow);
		marketCapBase.setArcHeight(10);
		marketCapBase.setArcWidth(10);

		Text marketCapName = new Text("Mkt Cap");
		marketCapName.setFont(Font.font("Open Sans", marketCapBase.getHeight() * 0.28 / 1.333));
		marketCapName.setX(marketCapBase.getX() + marketCapBase.getWidth() * 0.07);
		marketCapName.setY(marketCapBase.getY() + marketCapBase.getHeight() * 0.3);
		marketCapName.setFill(Color.WHITE);

		Text marketCap = new Text(checkMarketCap(stock.getMarketCap()));
		marketCap.setFont(Font.font("Open Sans", marketCapBase.getHeight() * 0.35 / 1.333));
		marketCap.setX(marketCapName.getX());
		marketCap.setY(marketCapBase.getY() + marketCapBase.getHeight() * 0.75);
		marketCap.setFill(Color.WHITE);

		info.getChildren().addAll(marketCapBase, marketCapName, marketCap);

		add(info);
	}

	private String checkMarketCap(long cap) {
		if (cap == Long.MIN_VALUE)
			return "-";
		long marketCap = cap;

		String string = Long.toString(marketCap);

		int sigFigs = (string.length() - 1) % 3;

		int power = string.length() - sigFigs - 1;

		String output = string.substring(0, sigFigs + 1) + "." + string.substring(sigFigs + 1, sigFigs + 3);

		switch (power) {
		case 3:
			return output + "K";
		case 6:
			return output + "M";
		case 9:
			return output + "B";
		case 12:
			return output + "T";
		default:
			return output;

		}
	}

	private String checkData(double data) {
		DecimalFormat format = new DecimalFormat("0.00");

		return (data == Integer.MIN_VALUE || data == Long.MIN_VALUE || data == Double.MIN_VALUE || data == 0) ? "-"
				: format.format(data);
	}

	private void createTop() {
		titleBase = new Rectangle(graphBase.getX(), graphBase.getY() + graphBase.getArcHeight(),
				0.5 * graphBase.getWidth(), 0.08 * graphBase.getHeight());
		titleBase.setArcWidth(titleBase.getHeight());
		titleBase.setArcHeight(titleBase.getHeight());
		titleBase.setFill(Util.ACCENT_COLOR);

		Rectangle cover = new Rectangle(titleBase.getX(), titleBase.getY(), titleBase.getHeight(),
				titleBase.getHeight());
		cover.setFill(Util.ACCENT_COLOR);

		String nameText = stock.getName();

		Text name = new Text(nameText);

		name.setFont(Font.font("Open Sans", Math.round(0.6 * titleBase.getHeight() / 1.333)));

		name.setX(titleBase.getX() + 0.05 * titleBase.getWidth());
		name.setTextOrigin(VPos.CENTER);
		name.setY(titleBase.getY() + titleBase.getHeight() / 2.0);
		name.setFill(Color.WHITE);

		if (name.getLayoutBounds().getWidth() > 0.4 * graphBase.getWidth()) {
			String[] words = nameText.split(" ");
			int chars = 0;
			int wordNumber = 0;

			for (String word : words) {
				chars += word.length();
				wordNumber++;
				if (chars >= 0.4 * nameText.length())
					break;
			}

			nameText = "";

			for (int i = 0; i < wordNumber; i++) {
				nameText += words[i] + " ";
			}

			nameText += "\n";

			for (int i = wordNumber; i < words.length; i++) {
				nameText += words[i] + " ";
			}

			nameText = nameText.trim();
			name.setText(nameText);
			name.setFont(Font.font("Open Sans", Math.round(0.28 * titleBase.getHeight() / 1.333)));

		}

		Text ticker = new Text(stock.getSymbol());
		ticker.setX(name.getX() + name.getLayoutBounds().getWidth() + 0.05 * titleBase.getWidth());
		ticker.setFont(Font.font("Open Sans", Math.round(0.4 * titleBase.getHeight() / 1.333)));
		ticker.setTextOrigin(VPos.CENTER);
		ticker.setY(name.getY());
		ticker.setFill(Color.WHITE);

		Group stockName = new Group();
		stockName.getChildren().add(name);
		stockName.getChildren().add(ticker);

		titleBase.setWidth(stockName.getLayoutBounds().getWidth() + titleBase.getArcHeight());

		top = new Group();

		top.getChildren().add(titleBase);
		top.getChildren().add(cover);

		top.getChildren().add(stockName);

		DecimalFormat format = new DecimalFormat("0.00");

		price = new Text("$" + format.format(stock.getPrice()));
		price.setFont(Font.font("Open Sans", Math.round(1.25 * titleBase.getHeight() / 1.333)));
		price.setFill(Color.WHITE);
		price.setX(graphBase.getX() + graphBase.getWidth() - 0.025 * graphBase.getWidth()
				- price.getLayoutBounds().getWidth());
		price.setTextOrigin(VPos.CENTER);
		price.setY(name.getY());

		createChangeText();

		top.getChildren().add(price);

		root.getChildren().add(top);
	}

	private void createChangeText() {
		DecimalFormat format = new DecimalFormat("0.00");

		top.getChildren().remove(change);

		ArrayList<StockDataPoint> points = dataPerTimeFrame.get(timeFrame);

		String changeText = "";
		double absChange = (points.get(points.size() - 1).getClose() - points.get(0).getClose());
		changeText = format.format(absChange);
		if (absChange >= 0)
			changeText = "+$" + changeText;
		else
			changeText = "-$" + changeText.substring(1);

		changeText += " (" + format.format(absChange / points.get(0).getClose() * 100) + "%) " + changeText(timeFrame);

		change = new Text(changeText);
		change.setFill((absChange >= 0) ? Util.BIG_GAIN_COLOR : Util.BIG_LOSS_COLOR);
		change.setFont(Font.font("Open Sans", Math.round(0.5 * titleBase.getHeight() / 1.333)));
		change.setX(graphBase.getX() + graphBase.getWidth() - 0.025 * graphBase.getWidth()
				- change.getLayoutBounds().getWidth());
		change.setY(price.getY() + price.getLayoutBounds().getHeight() / 2.0 + change.getLayoutBounds().getHeight());

		top.getChildren().add(change);

	}

	private String changeText(Time time) {
		switch (time) {
		case WEEK:
			return "past 5 days";
		case MONTH:
			return "past month";
		case QUARTER_YEAR:
			return "past 3 months";
		case HALF_YEAR:
			return "past 6 months";
		case YEAR_TO_DATE:
			return "year to date";
		default:
			return "past year";

		}
	}

}
