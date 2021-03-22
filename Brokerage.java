import java.lang.reflect.*;
import java.util.*;

/**
 * Represents a brokerage.
 */
public class Brokerage implements Login
{
    private Map<String, Trader> traders;
    private Set<Trader> loggedTraders;
    private StockExchange exchange;


    /**
     * Constructs new brokerage affiliated with a given stock exchange.
     * Initializes the map of traders to an empty map (a TreeMap), keyed by
     * trader's name; initializes the set of active (logged-in) traders to an
     * empty set (a TreeSet).
     *
     * @param exchange - exchange a stock exchange
     */
    public Brokerage(StockExchange exchange)
    {
        traders = new TreeMap<String, Trader>();
        loggedTraders = new TreeSet<Trader>();
    }


    public int addUser(String name, String password)
    {
        if (name.length() < 4 || name.length() > 10)
        {
            return -1;
        }
        else if (password.length() < 2 || password.length() > 10)
        {
            return -2;
        }
        else if (traders.containsKey(name))
        {
            return -3;
        }

        Trader trader = new Trader(this, name, password);
        traders.put(name, trader);
        return 0;
    }

    public int login(String name, String password)
    {
        if (!traders.containsKey(name))
        {
            return -1;
        }
        else if (!traders.get(name).password().equals(password))
        {
            return -2;
        }
        else if (loggedTraders.contains(traders.get(name)))
        {
            return -3;
        }

        loggedTraders.add(traders.get(name));
        traders.get(name).openWindow();
        return 0;
    }

    /**
     * Removes a specified trader from the set of logged-in traders. The trader
     * may be assumed to logged in already.
     *
     * @param trader the trader that logs out
     */
    public void logout(Trader trader)
    {
        loggedTraders.remove(trader);
    }

    /**
     * Requests a quote for a given stock from the stock exchange and passes it
     * along to the trader by calling trader's receiveMessage method. .
     *
     * @param symbol - the stock symbol
     * @param trader - the trader who requested a quote
     */
    public void getQuote(String symbol, Trader trader)
    {
        trader.recieveMessage(exchange.getQuote(symbol));
    }

    /**
     * Places an order at the stock exchange.
     *
     * @param order - an order to be placed at the stock exchange.
     */
    public void placeOrder(TradeOrder order)
    {
        exchange.placeOrder(order);
    }

    /**
     * Get the current value of traders.
     *
     * @return the value of trader
     */
    protected Map<String, Trader> getTraders()
    {
        return traders;
    }

    /**
     * Get the current value of loggedTraders.
     *
     * @return the value of loggedTraders
     */
    protected Set<Trader> getLoggedTraders()
    {
        return loggedTraders;
    }

    /**
     * Get the current value of exchange.
     *
     * @return the value of exchange
     */
    protected StockExchange getExchange()
    {
        return exchange;
    }

    /**
     * <p>
     * A generic toString implementation that uses reflection to print names and
     * values of all fields <em>declared in this class</em>. Note that
     * superclass fields are left out of this implementation.
     * </p>
     *
     * @return a string representation of this Brokerage.
     */
    public String toString()
    {
        String str = this.getClass().getName() + "[";
        String separator = "";

        Field[] fields = this.getClass().getDeclaredFields();

        for (Field field : fields)
        {
            try
            {
                str += separator + field.getType().getName() + " "
                    + field.getName() + ":" + field.get( this );
            }
            catch (IllegalAccessException ex)
            {
                System.out.println(ex);
            }

            separator = ", ";
        }

        return str + "]";
    }
}
