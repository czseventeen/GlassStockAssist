package jayxu.com.glassstockassist.Model;

import java.util.Random;

/**
 * Created by Yuchen on 12/13/2015.
 */
public class Stocks {
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
    }

    public Stocks(double mLastPrice, String mName, String mSymbol, double mVolumn, double mCurrentBid, double mCurrentAsk, int mAskSize, int mBidSize, double mMyTradePrice, double mMyTradeShares, double mPercentageChanges, double mPriceChanges, double mHigh, double mLow) {
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
    }

    public Stocks(int i){
        if(i==-1){
            Random random=new Random();
            String alphabet="ABCDEFGHIJKMLNOPQRSTOVWXYZ";
            String symbol="";
            this.mLastPrice=random.nextDouble()*100;
            //Create a random String Symbol
            for(i=0; i<4; i++){
                symbol=symbol+alphabet.charAt(random.nextInt(alphabet.length()-1));
            }
            this.mSymbol=symbol;
        }
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
}
