import java.lang.reflect.*;
import java.util.*;

/**
 * Represents a stock trader.
 */
public class Trader implements Comparable<Trader>
{
    private Brokerage brokerage;
    private String screenName, password;
    private TraderWindow myWindow;
    private Queue<String> mailbox;

    /**
     * Constructs a new trader, affiliated with a given brockerage, with a given
     * screen name and password.
     *
     * @param brokerage - the brokerage for this trader
     * @param name - user name
     * @param pswd - password
     */
    public Trader(Brokerage brokerage, String name, String pswd)
    {
        this.brokerage = brokerage;
        screenName = name;
        password = pswd;
    }


    /**
     * Returns the screen name for this trader.
     *
     * @return the screen name for this trader
     */
    public String getName()
    {
        return screenName;
    }

    /**
     * Returns the password for this trader.
     *
     * @return the password for this trader
     */
    public String password()
    {
        return password;
    }

    public int compareTo(Trader other)
    {
        return screenName.compareTo(other.screenName);
    }

    public boolean equals(Object other)
    {
        if (other instanceof Trader)
        {
            if (((Trader) other).screenName.equals(screenName))
            {
                return true;
            }
            return false;
        }
        throw new ClassCastException();
    }

    /**
     * Creates a new TraderWindow for this trader and saves a reference to it in
     * myWindow. Removes and displays all the messages, if any, from this
     * trader's mailbox by calling myWindow.showMessage (msg) for each message.
     */
    public void openWindow()
    {
        myWindow = new TraderWindow(this);
        while (mailbox.size() != 0)
        {
            myWindow.showMessage(mailbox.remove());
        }
    }

    /**
     * Return true if this trader has any messages in its mailbox.
     *
     * @return true if this trader has messages; false otherwise.
     */
    public boolean hasMessages()
    {
        return mailbox.size() != 0;
    }

    /**
     * Adds msg to this trader's mailbox and displays all messages. If this
     * trader is logged in (myWindow is not null)removes and shows all the
     * messages in the mailbox by calling myWindow.showMessage(msg) for each msg
     * in the mailbox.
     *
     * @param msg - a message to be added to this trader's mailbox
     */
    public void receiveMessage(String msg)
    {
        mailbox.add(msg);
        if (myWindow != null)
        {
            myWindow.showMessage(mailbox.remove());
        }
    }

    /**
     * Requests a quote for a given stock symbol from the brokerage by calling
     * brokerage's getQuote.
     *
     * @param symbol - a stock symbol for which a quote is requested
     */
    public void getQuote(String symbol)
    {
        brokerage.getQuote(symbol, this);
    }

    /**
     * Places a given order with the brokerage by callingbrokerage's placeOrder.
     *
     * @param order a trading order to be placed
     */
    public void placeOrder(TradeOrder order)
    {
        brokerage.placeOrder(order);
    }

    /**
     * Logs out this trader. Calls brokerage's logoutfor this trader. Sets
     * myWindow to null(this method is called from a TraderWindow's window
     * listener when the "close window" button is clicked).
     */
    public void quit()
    {
        brokerage.logout(this);
        myWindow = null;
    }

    /**
     * Get the current value of mailbox
     *
     * @return the value of mailbox for this object
     */
    protected Queue<String> mailbox()
    {
        return mailbox;
    }



    /**
     * <p>
     * A generic toString implementation that uses reflection to print names and
     * values of all fields <em>declared in this class</em>. Note that
     * superclass fields are left out of this implementation.
     * </p>
     *
     * @return a string representation of this Trader.
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
                if ( field.getType().getName().equals( "Brokerage" ) )
                    str += separator + field.getType().getName() + " "
                        + field.getName();
                else
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
