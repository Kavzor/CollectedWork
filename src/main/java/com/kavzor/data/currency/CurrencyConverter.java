package com.kavzor.data.currency;


import java.util.Arrays;


public class CurrencyConverter {

    private CurrencyContext context;
    private String from;
    private String to;
    private String value;
    private StateEvaluator empty;
    private TextEvaluator error;
    private TextEvaluator success;

    private static final double INVALID_FORMAT = -1;

    private CurrencyConverter(CurrencyContext context, String from, String to, String value,
                              StateEvaluator empty, TextEvaluator error, TextEvaluator success) {
        this.context = context;
        this.from = from;
        this.to = to;
        this.value = value;
        this.empty = empty;
        this.error = error;
        this.success = success;

        this.evaluate();
    }

    private void evaluate() {
        if (value.isEmpty()) {
            empty.evaluate();
        } else {
            double numericValue = toDouble(this.value);
            if (numericValue == INVALID_FORMAT) {
                error.evaluate("Cannot convert non-numeric values");
            } else {
                double result = calculate(numericValue, getRate(from), getRate(to));
                String formattedResult = context.getDecimalFormat().format(result);
                success.evaluate(formattedResult);
            }
        }
    }

    private double calculate(double value, double fromRate, double toRate) {
        return fromRate == 0 || toRate == 0 ? 0 : value * (fromRate / toRate);
    }

    private double toDouble(String value) {
        try {
            return Double.parseDouble(value);
        }
        catch(NumberFormatException exception) {
            return INVALID_FORMAT;
        }
    }

    private Double getRate(String name){
        String[] rates = context.getRates();
        String[] currencies = context.getCurrencies();
        int index = getIndex(currencies, name);

        return Double.valueOf(rates[index]);
    }

    private int getIndex(Object[] arr, Object obj) {
        return Arrays.asList(arr).indexOf(obj);
    }

    public static void convert(Convertor convertor) {
        CurrencyConverterBuilder builder = new CurrencyConverterBuilder();
        convertor.convert(builder);
        builder.evaluate();
    }

    public static class CurrencyConverterBuilder {
        private CurrencyContext nestedContext;
        private String nestedFrom;
        private String nestedTo;
        private String nestedValue;
        private StateEvaluator nestedEmpty;
        private TextEvaluator nestedError;
        private TextEvaluator nestedSuccess;

        public CurrencyConverterBuilder context(CurrencyContext context) {
            this.nestedContext = context;
            return this;
        }

        public CurrencyConverterBuilder from(String from) {
            this.nestedFrom = from;
            return this;
        }

        public CurrencyConverterBuilder to(String to) {
            this.nestedTo = to;
            return this;
        }

        public CurrencyConverterBuilder value(String value) {
            this.nestedValue = value;
            return this;
        }

        public CurrencyConverterBuilder onEmpty(StateEvaluator empty) {
            this.nestedEmpty = empty;
            return this;
        }

        public CurrencyConverterBuilder onError(TextEvaluator error) {
            this.nestedError = error;
            return this;
        }

        public CurrencyConverterBuilder onSuccess(TextEvaluator success) {
            this.nestedSuccess = success;
            return this;
        }

        private void evaluate() {
            new CurrencyConverter(nestedContext, nestedFrom, nestedTo,
                    nestedValue, nestedEmpty, nestedError, nestedSuccess);
        }
    }
}
