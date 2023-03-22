package stock.comparators;

import stock.data.Stock;
/**
 * 
 * Comparator for {@link Stock} using {@link Stock#getChangePercentage())} from smallest to largest.
 * This is the inverse of the {@link SortLoss} comparator.
 *
 */
public class SortLoss extends StockComparator {

	@Override
	public int compare(Stock o1, Stock o2) {
		return Double.compare(o1.getChangePercentage(), o2.getChangePercentage());
	}

}
