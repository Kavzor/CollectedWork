package com.kavzor.data.currency;

import java.text.DecimalFormat;


public class CurrencyContext {

    private DecimalFormat decimalFormat;

    private String[] mRates;
    private String[] mCurrencies;

    private CurrencyContext(DecimalFormat decimalFormat, String[] mRates, String[] mCurrencies) {
        this.decimalFormat = decimalFormat;
        this.mRates = mRates;
        this.mCurrencies = mCurrencies;
    }

    public DecimalFormat getDecimalFormat() {
        return decimalFormat;
    }


    public String[] getRates() {
        return mRates;
    }

    public String[] getCurrencies() {
        return mCurrencies;
    }

    public static CurrencyContext prepare(ContextConfig config) {
        CurrencyContextBuilder builder = new CurrencyContextBuilder();
        config.config(builder);
        return builder.build();
    }

    public static class CurrencyContextBuilder {
        private DecimalFormat nestedDecimalFormat;
        private String[] nestedRates;
        private String[] nestedCurrencies;

        public CurrencyContextBuilder decimalFormat(DecimalFormat format) {
            this.nestedDecimalFormat = format;
            return this;
        }

        public CurrencyContextBuilder rates(String[] rates){
            nestedRates = rates;
            return this;
        }

        public CurrencyContextBuilder currencies(String[] currencies){
            nestedCurrencies = currencies;
            return this;
        }

        public CurrencyContext build() {
            return new CurrencyContext(nestedDecimalFormat, nestedRates, nestedCurrencies);
        }
    }
}
