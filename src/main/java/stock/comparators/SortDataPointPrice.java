package stock.comparators;

import java.util.Comparator;

import stock.data.StockDataPoint;
/**
 * 
 * Comparator for {@link StockDataPoint} using {@link StockDataPoint#getClose()} from smallest to largest
 *
 */
public class SortDataPointPrice implements Comparator<StockDataPoint> {

	@Override
	public int compare(StockDataPoint o1, StockDataPoint o2) {
		return Double.compare(o1.getClose(), o2.getClose());
	}

}
