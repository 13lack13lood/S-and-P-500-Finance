package stock.comparators;

import stock.data.Stock;
/**
 * 
 * Comparator for {@link Stock} using {@link Stock#getChangePercentage()} from largest to smallest.
 * This is the inverse of {@link SortLoss}
 *
 */
public class SortGain extends StockComparator {

	@Override
	public int compare(Stock o1, Stock o2) {
		return Double.compare(o2.getChangePercentage(), o1.getChangePercentage());
	}

}
