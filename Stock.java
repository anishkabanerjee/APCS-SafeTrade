import java.util.*;
import java.lang.reflect.*;
import java.text.DecimalFormat;

/**
 * Represents a stock in the SafeTrade project
 */
public class Stock
{

    /**
     * DecimalFormat object for formatting prices.
     */
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


    /**
     * Returns a quote string for this stock. The quote includes: the company
     * name for this stock; the stock symbol; last sale price; the lowest and
     * highest day prices; the lowest price in a sell order (or "market") and
     * the number of shares in it (or "none" if there are no sell orders); the
     * highest price in a buy order (or "market") and the number of shares in
     * it (or "none" if there are no buy orders).
     *
     * @return the quote for this stock
     */
    public String getQuote()
    {
        String quote = "";
        quote += companyName + " (" + stockSymbol + ")\n";
        quote += "Price: " + lastPrice + " hi: " + hiPrice + " lo: " + loPrice
               + " vol: " + volume;
        return quote;
    }

    /**
     * Places a trading order for this stock.Adds the order to the appropriate '
     * priority queue depending on whether this is a buy or sell order.
     * Notifies the trader who placed the order that the order has been placed,
     * by sending a message to that trader.
     *
     * @param order - a trading order to be placed.
     */
    public void placeOrder(TradeOrder order)
    {
        String msg = "New order: ";
        if (order.isBuy())
        {
            buyOrders.add(order);
            msg += "Buy";
        }
        else
        {
            sellOrders.add(order);
            msg += "Sell";
        }

        msg += " " + stockSymbol + " (" + companyName + ")\n";
        msg += volume + " shares at ";
        if (order.isMarket())
        {
            msg += "market";
        }
        else
        {
            msg += money.format(order.getPrice());
        }

        order.getTrader().recieveMessage(msg);
        executeOrders();
    }

    /**
     * Executes as many pending orders as possible.
           1. Examines the top sell order and the top buy order in the
              respective priority queues.
               i. If both are limit orders and the buy order price is greater or
                  equal to the sell order price, executes the order (or a part
                  of it) at the sell order price.
               ii. If one order is limit and the other is market, executes the
                   order (or a part of it) at the limit order price.
               iii. If both orders are market, executes the order (or a part of
                    it) at the last sale price.
           2. Figures out how many shares can be traded, which is the smallest
              of the numbers of shares in the two orders.
           3. Subtracts the traded number of shares from each order;Removes each
              of the orders with 0 remaining shares from the respective queue.
           4. Updates the day's low price, high price, and volume.
           5. Sends a message to each of the two traders involved in the
              transaction. For example: You bought: 150 GGGL at 38.00 at 5700.00
              Note: The dollar amounts should be formatted to two decimal places
              (eg. 12.40, not 12.4)
           6. Repeats steps 1-5 for as long as possible, that is as long as
           there is any movement in the buy / sell order queues. (The process
           gets stuck when the top buy order and sell order are both limit
           orders and the ask price is higher than the bid price.)
     */
    protected void executeOrders()
    {
        int numSharesSold = 0;
        double priceSoldAt = 0;
        TradeOrder topSellOrder = sellOrders.peek();
        TradeOrder topBuyOrder = buyOrders.peek();
        if(topSellOrder != null && topBuyOrder != null)
        {
            if (!topBuyOrder.isMarket() && !topSellOrder.isMarket())
            {
                if(topBuyOrder.getPrice() >= topSellOrder.getPrice())
                {
                    if(topSellOrder.getShares() > topSellOrder.getShares())
                    {
                        topSellOrder.subtractShares(topBuyOrder.getShares());
                        numSharesSold = topBuyOrder.getShares();
                        priceSoldAt = topSellOrder.getPrice();
                        buyOrders.remove();
                    }
                    else if(topSellOrder.getShares() < topBuyOrder.getShares())
                    {
                        topBuyOrder.subtractShares(topSellOrder.getShares());

                        numSharesSold = topSellOrder.getShares();
                        priceSoldAt = topSellOrder.getPrice();
                        sellOrders.remove();
                    }
                    else if(topSellOrder.getShares() == topBuyOrder.getShares())
                    {
                        numSharesSold = topBuyOrder.getShares();
                        priceSoldAt = topSellOrder.getPrice();
                        sellOrders.remove();
                        buyOrders.remove();
                    }
                }
            }
            else if (!topBuyOrder.isMarket() && topSellOrder.isMarket())
            {
                if (topSellOrder.getShares() > topBuyOrder.getShares())
                {
                    topSellOrder.subtractShares(topBuyOrder.getShares());
                    numSharesSold = topBuyOrder.getShares();
                    priceSoldAt = topBuyOrder.getPrice();
                    buyOrders.remove();
                }
                else if (topSellOrder.getShares() < topBuyOrder.getShares())
                {
                    topBuyOrder.subtractShares(topSellOrder.getShares());
                    numSharesSold = topSellOrder.getShares();
                    priceSoldAt = topBuyOrder.getPrice();
                    sellOrders.remove();
                }
                else if (topSellOrder.getShares() == topBuyOrder.getShares())
                {
                    numSharesSold = topBuyOrder.getShares();
                    priceSoldAt = topBuyOrder.getPrice();
                    sellOrders.remove();
                    buyOrders.remove();
                }
            }
            else if (topBuyOrder.isMarket() && !topSellOrder.isMarket())
            {
                if(topSellOrder.getShares() > topBuyOrder.getShares())
                {
                    topSellOrder.subtractShares(topBuyOrder.getShares());
                    numSharesSold = topBuyOrder.getShares();
                    priceSoldAt = topSellOrder.getPrice();
                    buyOrders.remove();
                }
                else if (topSellOrder.getShares() < topBuyOrder.getShares())
                {
                    topBuyOrder.subtractShares(topSellOrder.getShares());
                    numSharesSold = topSellOrder.getShares();
                    priceSoldAt = topSellOrder.getPrice();
                    sellOrders.remove();
                }
                else if (topSellOrder.getShares() == topBuyOrder.getShares())
                {
                    numSharesSold = topBuyOrder.getShares();
                    priceSoldAt = topSellOrder.getPrice();
                    sellOrders.remove();
                    buyOrders.remove();
                }
            }
            else
            {
                if (topSellOrder.getShares() > topBuyOrder.getShares())
                {
                    topSellOrder.subtractShares(topBuyOrder.getShares());
                    numSharesSold = topBuyOrder.getShares();
                    priceSoldAt = lastPrice;
                    buyOrders.remove();
                }
                else if (topSellOrder.getShares() < topBuyOrder.getShares())
                {
                    topBuyOrder.subtractShares(topSellOrder.getShares());
                    numSharesSold = topSellOrder.getShares();
                    priceSoldAt = lastPrice;
                    sellOrders.remove();
                }
                else if (topSellOrder.getShares() == topBuyOrder.getShares())
                {
                    numSharesSold = topBuyOrder.getShares();
                    priceSoldAt = lastPrice;
                    sellOrders.remove();
                    buyOrders.remove();
                }
            }

            String msg = numSharesSold + " share(s) of " + stockSymbol +
                money.format(priceSoldAt);
            topBuyOrder.getTrader().recieveMessage("You just bought " + msg);
            topSellOrder.getTrader().recieveMessage("You just sold " + msg);
        }

        if (priceSoldAt >= hiPrice)
        {
          hiPrice = priceSoldAt;
        }
        else
        {
          loPrice = priceSoldAt;
        }

        volume += numSharesSold;
        if (priceSoldAt != 0)
        {
            lastPrice = priceSoldAt;
        }
        if (numSharesSold != 0)
        {
            executeOrders();
        }
    }

    /**
     * Returns the stock symbol for this stock.
     *
     * @return the stock symbol for this stock
     */
    protected String getStockSymbol()
    {
        return stockSymbol;
    }

    /**
     * Returns the company name for this stock.
     *
     * @return the company name for this stock
     */
    protected String getCompanyName()
    {
        return companyName;
    }

    /**
     * Returns the loPrice for this stock.
     *
     * @return the loPrice for this stock
     */
    protected double getLoPrice()
    {
        return loPrice;
    }

    /**
     * Returns the hiPrice for this stock.
     *
     * @return the hiPrice for this stock
     */
    protected double getHiPrice()
    {
        return hiPrice;
    }

    /**
     * Returns the lastPrice for this stock.
     *
     * @return the lastPrice for this stock
     */
    protected double getLastPrice()
    {
        return lastPrice;
    }

    /**
     * Returns the volume for this stock.
     *
     * @return the volume for this stock
     */
    protected int getVolume()
    {
        return volume;
    }

    /**
     * Returns the buyOrders for this stock.
     *
     * @return the buyOrders for this stock
     */
    protected PriorityQueue<TradeOrder> getBuyOrders()
    {
        return buyOrders;
    }

    /**
     * Returns the sellOrders for this stock.
     *
     * @return the sellOrders for this stock
     */
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
