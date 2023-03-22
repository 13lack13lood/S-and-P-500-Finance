package stock.data;

import java.util.GregorianCalendar;

import application.Util;
/**
 * 
 * A class storing the information for each individual historical data point for a particular stock retrieved through {@link handlers.StockDataHandler StockDataHandler}
 *
 */
public class StockDataPoint {
	private double open;
	private double high;
	private double low;
	private double close;
	private long volume;
	private GregorianCalendar date;
	/**
	 * 
	 * @param date
	 * @param open
	 * @param high
	 * @param low
	 * @param close
	 * @param volume
	 */
	public StockDataPoint(String date, String open, String high, String low, String close, String volume) {
		String[] splitDate = date.substring(0, date.indexOf(' ')).split("/");
		this.date = new GregorianCalendar(Integer.parseInt(splitDate[2]), Integer.parseInt(splitDate[0]) - 1,
				Integer.parseInt(splitDate[1]));
		this.open = Util.checkDouble(open);
		this.high = Util.checkDouble(high);
		this.low = Util.checkDouble(low);
		this.close = Util.checkDouble(close);
		this.volume = Util.checkLong(volume);
	}

	/**
	 * @return the open
	 */
	public double getOpen() {
		return open;
	}

	/**
	 * @return the high
	 */
	public double getHigh() {
		return high;
	}

	/**
	 * @return the low
	 */
	public double getLow() {
		return low;
	}

	/**
	 * @return the close
	 */
	public double getClose() {
		return close;
	}

	/**
	 * @return the volume
	 */
	public long getVolume() {
		return volume;
	}

	/**
	 * @return the date
	 */
	public GregorianCalendar getDate() {
		return date;
	}

	@Override
	public String toString() {
		return "StockDataPoint [open=" + open + ", high=" + high + ", low=" + low + ", close=" + close + ", volume="
				+ volume + ", date=" + date + "]";
	}

}
