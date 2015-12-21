package jayxu.com.glassstockassist.Model;

/**
 * Created by Yuchen on 12/20/2015.
 */
public class StockConstants {
    public static final String KEY_APICALLBACKNAME="findSymbolByName";
    public static final String StockPriceAPIURL="http://dev.markitondemand.com/MODApis/Api/v2/Quote/jsonp?symbol=SYMBOSTRING&callback="+KEY_APICALLBACKNAME;
    public static final String StockSymbolLookupAPIURL="http://dev.markitondemand.com/MODApis/Api/v2/Lookup/jsonp?input=SEARCHSTRING&callback="+KEY_APICALLBACKNAME;
    //Below are used to replace the API query with valid data
    public static final String KEY_SEARCHSTRING="SEARCHSTRING";
    public static final String KEY_SYMBOLSTRING="SYMBOSTRING";

//Below determines what is the max number of stocks to be displayed
    public static final int MAX_NUM_STOCKS=4;

//Below are keys used to indicate whether to initiliaize the remoteview or just update the Prices;
    public static final int KEY_UPDATE_PRICE=1;
    public static final int KEY_USING_NEW_STOCKS =0;
    public static final int KEY_REMOVE_ITEM =2 ;
    public static final int KEY_ADD_STOCK = 3;

    //Below are indices used to placed menu items
    public static final int INDEX_ADD_MENU =0 ;
    public static final int INDEX_DELETE_MENU =1 ;

    //below is the timer for update rate, units are milliseconds
    public static final long UPDATE_RATE = 5000;

    //below is used to indicate that the price is not initialized yet
    public static final double PRICE_NOT_SET=-999;
    //Below is used to indicate to randomly generate a stock price for you
    public static final double KEY_GENERATE_RANDOM_STOCK = -1;
    //Below are used to referrence to the JSON data we got back from API
    public static final String JSON_KEY_LASTPRICE = "LastPrice";
    public static final String JSON_KEY_NAME = "Name";
    public static final String JSON_KEY_SYMBOL = "Symbol";
    public static final String JSON_KEY_CHANGE = "Change";
    public static final String JSON_KEY_CHANGE_PERCENT="ChangePercent";
    public static final String JSON_KEY_VOLUME="Volume";
    public static final String JSON_KEY_HIGH="High";
    public static final String JSON_KEY_LOW="Low";


}
