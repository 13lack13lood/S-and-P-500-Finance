package stock.comparators;

import stock.data.Stock;
/**
 * 
 * Comparator for {@link Stock} using {@link Stock#getSymbol()} sorted lexicographically
 *
 */
public class SortSymbol extends StockComparator {

	@Override
	public int compare(Stock o1, Stock o2) {
		return o1.getSymbol().compareTo(o2.getSymbol());
	}

}
