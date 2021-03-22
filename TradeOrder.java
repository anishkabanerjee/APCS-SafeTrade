import java.lang.reflect.*;

/**
 * Represents a buy or sell order for trading a given number of shares of a
 * specified stock.
 */
public class TradeOrder
{
    private Trader trader;
    private String symbol;
    private boolean buyOrder;
    private boolean marketOrder;
    private int numShares;
    private double price;

    // TODO complete class

    //
    // The following are for test purposes only
    //
    /**
     * <p>
     * A generic toString implementation that uses reflection to print names and
     * values of all fields <em>declared in this class</em>. Note that
     * superclass fields are left out of this implementation.
     * </p>
     * 
     * @return a string representation of this TradeOrder.
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

    /**
     * Get the current value of trader.
     * @return The value of trader for this object.
     */
    public Trader getTrader()
    {
        return trader;
    }

    /**
     * Set the value of trader for this object.
     * @param trader The new value for trader.
     */
    public void setTrader(Trader trader)
    {
        this.trader = trader;
    }

    /**
     * Get the current value of symbol.
     * @return The value of symbol for this object.
     */
    public String getSymbol()
    {
        return symbol;
    }

    /**
     * Set the value of symbol for this object.
     * @param symbol The new value for symbol.
     */
    public void setSymbol(String symbol)
    {
        this.symbol = symbol;
    }

    /**
     * Get the current value of buyOrder.
     * @return The value of buyOrder for this object.
     */
    public boolean isBuyOrder()
    {
        return buyOrder;
    }

    /**
     * Set the value of buyOrder for this object.
     * @param buyOrder The new value for buyOrder.
     */
    public void setBuyOrder(boolean buyOrder)
    {
        this.buyOrder = buyOrder;
    }

    /**
     * Get the current value of marketOrder.
     * @return The value of marketOrder for this object.
     */
    public boolean isMarketOrder()
    {
        return marketOrder;
    }

    /**
     * Set the value of marketOrder for this object.
     * @param marketOrder The new value for marketOrder.
     */
    public void setMarketOrder(boolean marketOrder)
    {
        this.marketOrder = marketOrder;
    }

    /**
     * Get the current value of numShares.
     * @return The value of numShares for this object.
     */
    public int getNumShares()
    {
        return numShares;
    }

    /**
     * Set the value of numShares for this object.
     * @param numShares The new value for numShares.
     */
    public void setNumShares(int numShares)
    {
        this.numShares = numShares;
    }

    /**
     * Get the current value of price.
     * @return The value of price for this object.
     */
    public double getPrice()
    {
        return price;
    }

    /**
     * Set the value of price for this object.
     * @param price The new value for price.
     */
    public void setPrice(double price)
    {
        this.price = price;
    }
}
