package ui.portfolio;

import java.util.GregorianCalendar;

import stock.data.Stock;
/**
 * 
 * A class that stores the information for each individual investment in {@link Portfolio}
 *
 */
public class PortfolioDataPoint implements Comparable<PortfolioDataPoint> {
	private GregorianCalendar date;
	private Stock stock;
	private int quantity;
	private double price;

	/**
	 * @param date
	 * @param stock
	 * @param quantity
	 * @param price
	 */
	public PortfolioDataPoint(GregorianCalendar date, Stock stock, int quantity, double price) {
		this.date = date;
		this.stock = stock;
		this.quantity = quantity;
		this.price = price;
	}

	/**
	 * @return the date
	 */
	public GregorianCalendar getDate() {
		return date;
	}

	/**
	 * @return the stock
	 */
	public Stock getStock() {
		return stock;
	}

	/**
	 * @return the quantity
	 */
	public int getQuantity() {
		return quantity;
	}

	/**
	 * @return the price
	 */
	public double getPrice() {
		return price;
	}

	@Override
	public int compareTo(PortfolioDataPoint o) {
		return date.compareTo(o.date);
	}

	/**
	 * @param date the date to set
	 */
	public void setDate(GregorianCalendar date) {
		this.date = date;
	}

	/**
	 * @param stock the stock to set
	 */
	public void setStock(Stock stock) {
		this.stock = stock;
	}

	/**
	 * @param quantity the quantity to set
	 */
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	/**
	 * @param price the price to set
	 */
	public void setPrice(double price) {
		this.price = price;
	}

}
