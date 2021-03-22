import java.util.*;
import java.lang.reflect.*;
import java.text.DecimalFormat;

/**
 * Represents a stock in the SafeTrade project
 */
public class Stock
{

    public static DecimalFormat money = new DecimalFormat("0.00");

    private String stockSymbol;
    private String companyName;
    private double loPrice, hiPrice, lastPrice;
    private int volume;
    private PriorityQueue<TradeOrder> buyOrders, sellOrders;


    /**
     * Constructs a new stock with a given symbol, company name, and starting
     * price. Sets low price, high price,and last price to the same opening
     * price.Sets "day" volume to zero.Initializes a priority queue for sell
     * orders to an empty PriorityQueue with a PriceComparator configured for
     * comparing orders in ascending order;initializes a priority queue for buy
     * orders to an empty PriorityQueue with a PriceComparatorconfigured for
     * comparing orders in descending order.
     *
     * @param symbol - the stock symbol
     * @param name - full company name
     * @param price - opening price for this stock
     */
    public Stock(String symbol, String name, double price)
    {
        stockSymbol = symbol;
        companyName = name;
        loPrice = price;
        hiPrice = price;
        lastPrice = price;
        volume = 0;

        PriceComparator ascending = new PriceComparator();
        buyOrders = new PriorityQueue<TradeOrder>(ascending);

        PriceComparator descending = new PriceComparator();
        sellOrders = new PriorityQueue<TradeOrder>(descending);
    }


    Price: 10.00  hi: 10.00  lo: 10.00  vol: 0
    Ask: 12.75 size: 300  Bid: 12.00 size: 500

    public String getQuote()
    {
        String quote = "";
        quote += companyName + " (" + stockSymbol + ")\n";
        quote += "Price: " + lastPrice + " hi: " + hiPrice + " lo: " + loPrice
               + " Ask: "
    }

    public void placeOrder(TradeOrder order)
    {

    }

    protected void executeOrders()
    {

    }


    protected String getStockSymbol()
    {
        return stockSymbol;
    }

    protected String getCompanyName()
    {
        return companyName;
    }

    protected double getLoPrice()
    {
        return loPrice;
    }

    protected double getHiPrice()
    {
        return hiPrice;
    }

    protected double getLastPrice()
    {
        return lastPrice;
    }

    protected int getVolume()
    {
        return volume;
    }

    protected PriorityQueue<TradeOrder> getBuyOrders()
    {
        return buyOrders;
    }

    protected PriorityQueue<TradeOrder> getSellOrders()
    {
        return sellOrders;
    }

    /**
     * <p>
     * A generic toString implementation that uses reflection to print names and
     * values of all fields <em>declared in this class</em>. Note that
     * superclass fields are left out of this implementation.
     * </p>
     *
     * @return a string representation of this Stock.
     */
    public String toString()
    {
        String str = this.getClass().getName() + "[";
        String separator = "";

        Field[] fields = this.getClass().getDeclaredFields();

        for ( Field field : fields )
        {
            try
            {
                str += separator + field.getType().getName() + " "
                    + field.getName() + ":" + field.get( this );
            }
            catch ( IllegalAccessException ex )
            {
                System.out.println( ex );
            }

            separator = ", ";
        }

        return str + "]";
    }
}
