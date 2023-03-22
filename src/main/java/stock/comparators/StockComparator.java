package stock.comparators;

import java.util.Comparator;

import stock.data.Stock;
/**
 * 
 * A StockComparator implements {@link Comparator} for {@link Stock}
 *
 */
public abstract class StockComparator implements Comparator<Stock> {
	public abstract int compare(Stock o1, Stock o2);
}
