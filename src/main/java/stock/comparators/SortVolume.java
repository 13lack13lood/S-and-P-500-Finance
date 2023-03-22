package stock.comparators;

import stock.data.Stock;
/**
 * 
 * Comparator for {@link Stock} using {@link Stock#getVolume()} sorted from largest to smallest
 *
 */
public class SortVolume extends StockComparator {

	@Override
	public int compare(Stock o1, Stock o2) {
		return Double.compare(o2.getVolume(), o1.getVolume());
	}

}
