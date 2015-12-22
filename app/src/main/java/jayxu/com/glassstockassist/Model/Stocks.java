package jayxu.com.glassstockassist.Model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Random;

/**
 * Created by Yuchen on 12/13/2015.
 */
public class Stocks implements Parcelable {
    private double mLastPrice;
    private String mName;
    private String mSymbol;
    private double mVolumn;
    private double mCurrentBid;
    private double mCurrentAsk;
    private int mAskSize;
    private int mBidSize;
    private double mMyTradePrice;
    private double mMyTradeShares;
    private double mPercentageChanges;
    private double mPriceChanges;
    private double mHigh;
    private double mLow;
    private String mExchangeMarket;

    public Stocks(){
        this.mLastPrice = 0;
        this.mName ="";
        this.mSymbol ="";
        this.mVolumn = 0;
        this.mCurrentBid = 0;
        this.mCurrentAsk = 0;
        this.mAskSize = 0;
        this.mBidSize = 0;
        this.mMyTradePrice = 0;
        this.mMyTradeShares = 0;
        this.mPercentageChanges = 0;
        this.mPriceChanges = 0;
        this.mHigh = 0;
        this.mLow = 0;
        this.mExchangeMarket="";
    }

    public Stocks(double mLastPrice, String mName, String mSymbol, double mVolumn, double mCurrentBid, double mCurrentAsk, int mAskSize, int mBidSize, double mMyTradePrice, double mMyTradeShares, double mPercentageChanges, double mPriceChanges, double mHigh, double mLow, String Exchange) {
        this.mLastPrice = mLastPrice;
        this.mName = mName;
        this.mSymbol = mSymbol;
        this.mVolumn = mVolumn;
        this.mCurrentBid = mCurrentBid;
        this.mCurrentAsk = mCurrentAsk;
        this.mAskSize = mAskSize;
        this.mBidSize = mBidSize;
        this.mMyTradePrice = mMyTradePrice;
        this.mMyTradeShares = mMyTradeShares;
        this.mPercentageChanges = mPercentageChanges;
        this.mPriceChanges = mPriceChanges;
        this.mHigh = mHigh;
        this.mLow = mLow;
        this.mExchangeMarket=Exchange;
    }

    public Stocks(String Symbol, double price){
        Random random=new Random();
        if (Symbol==null){
            String alphabet="ABCDEFGHIJKMLNOPQRSTOVWXYZ";
            String symbol="";
            for(int i=0; i<4; i++){
                symbol=symbol+alphabet.charAt(random.nextInt(alphabet.length()-1));
            }
            this.mSymbol=symbol;
        }else{
            this.mSymbol=Symbol;
        }
        if(price== StockConstants.KEY_GENERATE_RANDOM_STOCK){

            this.mLastPrice=random.nextDouble()*100;
            //Create a random String Symbol
        }else{
            this.mLastPrice=price;
        }
    }

    public Stocks(String Name, String Symbol, String exchange){
        this.mLastPrice = StockConstants.PRICE_NOT_SET;
        this.mName =Name;
        this.mSymbol =Symbol;
        this.mExchangeMarket=exchange;
    }

    public double SetAndGetNewRandomStockPrice(){
        Random random=new Random();
        this.setmLastPrice(getmLastPrice()+(random.nextDouble()*4)-2);
        return getmLastPrice();
    }

    public double getmLastPrice() {
        return mLastPrice;
    }

    public void setmLastPrice(double mLastPrice) {
        this.mLastPrice = mLastPrice;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getmSymbol() {
        return mSymbol;
    }

    public void setmSymbol(String mSymbol) {
        this.mSymbol = mSymbol;
    }

    public double getmVolumn() {
        return mVolumn;
    }

    public void setmVolumn(double mVolumn) {
        this.mVolumn = mVolumn;
    }

    public double getmCurrentBid() {
        return mCurrentBid;
    }

    public void setmCurrentBid(double mCurrentBid) {
        this.mCurrentBid = mCurrentBid;
    }

    public double getmCurrentAsk() {
        return mCurrentAsk;
    }

    public void setmCurrentAsk(double mCurrentAsk) {
        this.mCurrentAsk = mCurrentAsk;
    }

    public int getmAskSize() {
        return mAskSize;
    }

    public void setmAskSize(int mAskSize) {
        this.mAskSize = mAskSize;
    }

    public int getmBidSize() {
        return mBidSize;
    }

    public void setmBidSize(int mBidSize) {
        this.mBidSize = mBidSize;
    }

    public double getmMyTradePrice() {
        return mMyTradePrice;
    }

    public void setmMyTradePrice(double mMyTradePrice) {
        this.mMyTradePrice = mMyTradePrice;
    }

    public double getmMyTradeShares() {
        return mMyTradeShares;
    }

    public void setmMyTradeShares(double mMyTradeShares) {
        this.mMyTradeShares = mMyTradeShares;
    }

    public double getmPercentageChanges() {
        return mPercentageChanges;
    }

    public void setmPercentageChanges(double mPercentageChanges) {
        this.mPercentageChanges = mPercentageChanges;
    }

    public double getmPriceChanges() {
        return mPriceChanges;
    }

    public void setmPriceChanges(double mPriceChanges) {
        this.mPriceChanges = mPriceChanges;
    }

    public double getmHigh() {
        return mHigh;
    }

    public void setmHigh(double mHigh) {
        this.mHigh = mHigh;
    }

    public double getmLow() {
        return mLow;
    }

    public void setmLow(double mLow) {
        this.mLow = mLow;
    }

    public String getmExchangeMarket() {
        return mExchangeMarket;
    }

    public void setmExchangeMarket(String mExchangeMarket) {
        this.mExchangeMarket = mExchangeMarket;
    }

    @Override
    public String toString() {
        return "The stock name is:"+this.mName
                +", symbol: "+this.mSymbol
                +", price: "+this.mLastPrice;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mName);
        dest.writeString(this.mSymbol);
        dest.writeString(this.mExchangeMarket);

        dest.writeDouble(this.mLastPrice);
        dest.writeDouble(this.mVolumn);
        dest.writeDouble(this.mCurrentBid);
        dest.writeDouble(this.mCurrentAsk);
        dest.writeDouble(this.mMyTradePrice);
        dest.writeDouble(this.mMyTradeShares);
        dest.writeDouble(this.mPercentageChanges);
        dest.writeDouble(this.mPriceChanges);
        dest.writeDouble(this.mHigh);
        dest.writeDouble(this.mLow);

        dest.writeInt(this.mAskSize);
        dest.writeInt(this.mBidSize);
    }

    public static final Creator<Stocks> CREATOR=new Creator<Stocks>() {
        @Override
        public Stocks createFromParcel(Parcel source) {
            return new Stocks(source);
        }

        @Override
        public Stocks[] newArray(int size) {
            return new Stocks[size];
        }
    };
    // "De-parcel object
    public Stocks(Parcel in){

        this.mName =in.readString() ;
        this.mSymbol = in.readString();
        this.mExchangeMarket=in.readString();

        this.mLastPrice =in.readDouble() ;
        this.mVolumn = in.readDouble();
        this.mCurrentBid = in.readDouble();
        this.mCurrentAsk = in.readDouble();
        this.mMyTradePrice = in.readDouble();
        this.mMyTradeShares = in.readDouble();
        this.mPercentageChanges = in.readDouble();
        this.mPriceChanges = in.readDouble();
        this.mHigh = in.readDouble();
        this.mLow = in.readDouble();

        this.mAskSize = in.readInt();
        this.mBidSize = in.readInt();
    }
}
