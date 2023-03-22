package stock.comparators;

import stock.data.Stock;
/**
 * 
 * Comparator for {@link Stock} using {@link Stock#getMarketCap()}
 *
 */
public class SortMarketCap extends StockComparator {

	@Override
	public int compare(Stock o1, Stock o2) {
		return Long.compare(o2.getMarketCap(), o1.getMarketCap());
	}

}