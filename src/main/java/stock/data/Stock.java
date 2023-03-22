package stock.data;

import java.util.ArrayList;

import application.Util;
import javafx.scene.paint.Color;

/**
 * A class sorting the information for each individual stock retrieved by {@link handlers.StockDataHandler StockDataHandler}
 *
 */
public class Stock {
	
	private String symbol;
	private String name;
	private String stockType;
	private String capType;
	private String industry;
	
	private double price;
	private double priceOpen;
	private double changePercentage;
	private double change;
	private double priceHigh;
	private double priceLow;
	private double pe;
	private double eps;
	private double high52;
	private double low52;
	
	private long marketCap;
	private long volume;
	private long volumeAvg;
	
	private ArrayList<StockDataPoint> historicalData;
	/**
	 * 
	 * @param symbol
	 * @param name
	 * @param stockType
	 * @param capType
	 * @param industry
	 * @param price
	 * @param priceOpen
	 * @param changePercentage
	 * @param change
	 * @param priceHigh
	 * @param priceLow
	 * @param pe
	 * @param eps
	 * @param high52
	 * @param low52
	 * @param marketCap
	 * @param volume
	 * @param volumeAvg
	 */
	public Stock(String symbol, String name, String stockType, String capType, String industry, String price,
			String priceOpen, String changePercentage, String change, String priceHigh, String priceLow, String pe,
			String eps, String high52, String low52, String marketCap, String volume, String volumeAvg) {
		this.symbol = symbol;
		this.name = name;
		this.stockType = stockType;
		this.capType = capType;
		this.industry = industry;
		this.price = Util.checkDouble(price);
		this.priceOpen = Util.checkDouble(priceOpen);
		this.changePercentage = Util.checkDouble(changePercentage);
		this.change = Util.checkDouble(change);
		this.priceHigh = Util.checkDouble(priceHigh);
		this.priceLow = Util.checkDouble(priceLow);
		this.pe = Util.checkDouble(pe);
		this.eps = Util.checkDouble(eps);
		this.high52 = Util.checkDouble(high52);
		this.low52 = Util.checkDouble(low52);
		this.marketCap = Util.checkLong(marketCap);
		this.volume = Util.checkLong(volume);
		this.volumeAvg = Util.checkLong(volumeAvg);
		historicalData = new ArrayList<>();
	}

	/**
	 * @return the symbol
	 */
	public String getSymbol() {
		return symbol;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the stockType
	 */
	public String getStockType() {
		return stockType;
	}

	/**
	 * @return the capType
	 */
	public String getCapType() {
		return capType;
	}

	/**
	 * @return the industry
	 */
	public String getIndustry() {
		return industry;
	}

	/**
	 * @return the price
	 */
	public double getPrice() {
		return price;
	}

	/**
	 * @return the priceOpen
	 */
	public double getPriceOpen() {
		return priceOpen;
	}

	/**
	 * @return the changePercentage
	 */
	public double getChangePercentage() {
		return changePercentage;
	}

	/**
	 * @return the change
	 */
	public double getChange() {
		return change;
	}

	/**
	 * @return the priceHigh
	 */
	public double getPriceHigh() {
		return priceHigh;
	}

	/**
	 * @return the priceLow
	 */
	public double getPriceLow() {
		return priceLow;
	}

	/**
	 * @return the pe
	 */
	public double getPe() {
		return pe;
	}

	/**
	 * @return the eps
	 */
	public double getEps() {
		return eps;
	}

	/**
	 * @return the high52
	 */
	public double getHigh52() {
		return high52;
	}

	/**
	 * @return the low52
	 */
	public double getLow52() {
		return low52;
	}

	/**
	 * @return the marketCap
	 */
	public long getMarketCap() {
		return marketCap;
	}

	/**
	 * @return the volume
	 */
	public long getVolume() {
		return volume;
	}

	/**
	 * @return the volumeAvg
	 */
	public long getVolumeAvg() {
		return volumeAvg;
	}

	/**
	 * @return the historicalData
	 */
	public ArrayList<StockDataPoint> getHistoricalData() {
		return historicalData;
	}

	/**
	 * @param historicalData the historicalData to set
	 */
	public void setHistoricalData(ArrayList<StockDataPoint> historicalData) {
		this.historicalData = historicalData;
	}
	/**
	 * 
	 * @return A color from {@link Util} based on the {@link Stock#getChangePercentage() getChangePercentage()}
	 */
	public Color getChangeColor() {
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

	@Override
	public String toString() {
		return "Stock [symbol=" + symbol + ", name=" + name + ", stockType=" + stockType + ", capType=" + capType
				+ ", industry=" + industry + ", price=" + price + ", priceOpen=" + priceOpen + ", changePercentage="
				+ changePercentage + ", priceHigh=" + priceHigh + ", priceLow=" + priceLow + ", pe=" + pe + ", eps="
				+ eps + ", high52=" + high52 + ", low52=" + low52 + ", marketCap=" + marketCap + ", volume=" + volume
				+ ", volumeAvg=" + volumeAvg + "]";
	}

	/**
	 * @param anObject
	 * @return
	 * @see java.lang.String#equals(java.lang.Object)
	 */
	public boolean equals(Object o) {
		return o instanceof Stock && ((Stock) o).name.equals(name);
	}

}
