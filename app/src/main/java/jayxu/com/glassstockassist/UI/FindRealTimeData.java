package jayxu.com.glassstockassist.UI;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Entity;
import android.os.AsyncTask;
import android.util.Log;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;

import jayxu.com.glassstockassist.Model.StockConstants;
import jayxu.com.glassstockassist.Model.Stocks;

/**
 * Created by Yuchen on 12/20/2015.
 * This class is used by other classes to make API calls and grab real time stock price data
 */
public class FindRealTimeData {
    protected static ArrayList<Stocks> SearchResults = new ArrayList<>();
    protected static Stocks stock = new Stocks();

    private static final String TAG = FindRealTimeData.class.getSimpleName();

    private static OkHttpClient client = new OkHttpClient();


    public static ArrayList<Stocks> findSymbolByName(String StockName) {
        //clear the current result list for a new search

        SearchResults.clear();
//        re-do the search for the new StockName

        HttpResponse response = null;
        HttpClient client = new DefaultHttpClient();
        HttpGet request = new HttpGet();
        HttpEntity entity = null;
        try {
            //create the URI to inquery about the Stock Symbol based off of the stockname input
            request.setURI(new URI(StockConstants.StockSymbolLookupAPIURL.replace(StockConstants.KEY_SEARCHSTRING, StockName)));
            response = client.execute(request);
            entity = response.getEntity();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {

            String result = EntityUtils.toString(entity);
            //formating the result into JSON array:
            result = result.replace(StockConstants.KEY_APICALLBACKNAME + "(", "");
            result = result.replace(")", "");

            Log.d(TAG, "Symbol response was :" + result);
            JSONArray jsonArray = new JSONArray(result);
            //Iterating through the list and grab the data
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                // return an arraylist of stock objs containing name/symbol and exchange market
                Stocks stock = new Stocks(obj.getString("Name"), obj.getString("Symbol"), obj.getString("Exchange"));
                Log.d("~~~~~~~", "Adding new stock :" + stock.toString());
                SearchResults.add(stock);
            }
        } catch (JSONException e) {
            Log.d(TAG, "Caught JSONException:" + e);
        } catch (IOException e) {
            Log.d(TAG, "Caught IOException:" + e);
        }


        return SearchResults;
    }

    public static Stocks findPriceBySymbol(String Symbol) {
        HttpResponse response = null;
        HttpClient client = new DefaultHttpClient();
        HttpGet request = new HttpGet();
        HttpEntity entity = null;
        try {

            request.setURI(new URI(StockConstants.StockPriceAPIURL.replace(StockConstants.KEY_SYMBOLSTRING, Symbol)));
            response = client.execute(request);
            entity = response.getEntity();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {

            String result = EntityUtils.toString(entity);
            Log.d(TAG, "Price response was :" + result);
            //formatting resulting Json
            result = result.replace(StockConstants.KEY_APICALLBACKNAME + "(", "");
            result = result.replace(")", "");
            JSONObject obj = new JSONObject(result);
            Log.d(TAG, "The last price was :" + obj.getDouble(StockConstants.JSON_KEY_LASTPRICE));
            //modifying the stock obj to return with all the data we got back from the JSON API
            stock.setmLastPrice(obj.getDouble(StockConstants.JSON_KEY_LASTPRICE));
            stock.setmName(obj.getString(StockConstants.JSON_KEY_NAME));
            stock.setmSymbol(obj.getString(StockConstants.JSON_KEY_SYMBOL));
            stock.setmPriceChanges(obj.getDouble(StockConstants.JSON_KEY_CHANGE));
            stock.setmPercentageChanges(obj.getDouble(StockConstants.JSON_KEY_CHANGE_PERCENT));
            stock.setmVolumn(obj.getDouble(StockConstants.JSON_KEY_VOLUME));
            stock.setmHigh(obj.getDouble(StockConstants.JSON_KEY_HIGH));
            stock.setmLow(obj.getDouble(StockConstants.JSON_KEY_LOW));

        } catch (JSONException e) {
            Log.d(TAG, "Caught JSONException:" + e);
        } catch (IOException e) {
            Log.d(TAG, "Caught IOException: " + e);
        }


        return stock;
    }


}
