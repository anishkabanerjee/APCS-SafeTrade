/**
 * A price comparator for trade orders.
 */
public class PriceComparator implements java.util.Comparator<TradeOrder>
{

    private boolean isAscending;


    /**
     * Constructs a price comparator that compares two orders in ascending
     * order. Sets the private boolean ascending flag to true.
     */
    public PriceComparator()
    {
        isAscending = true;
    }

    /**
     * Constructs a price comparator that compares two orders in ascending or
     * descending order. The order of comparison depends on the value of a given
     * parameter. Sets the private boolean ascending flag to asc.
     *
     * @param asc if true, make an ascending comparator; otherwise make a
     * descending comparator.
     */
    public PriceComparator(boolean asc)
    {
        isAscending = asc;
    }


    public int compare(TradeOrder order1, TradeOrder order2)
    {
        if (order1.isMarketOrder() && order2.isMarketOrder())
        {
            return 0;
        }
        else if (order1.isMarketOrder() && !order2.isMarketOrder())
        {
            return -1;
        }
        else if(!order1.isMarketOrder() && order2.isMarketOrder())
        {
            return 1;
        }

        if (isAscending)
        {
            return (int) (order1.getPrice() - order2.getPrice() * 100);
        }

        return (int) (order2.getPrice() - order1.getPrice());
    }

}
