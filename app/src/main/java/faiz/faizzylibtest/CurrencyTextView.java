package faiz.faizzylibtest;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.TextView;

import java.text.NumberFormat;
import java.util.Currency;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


@SuppressLint("AppCompatCustomView")
public class CurrencyTextView extends TextView {
    private String locale = "India";
    //    private int decimalPlace = 2;
    private boolean grouping = false;
    private boolean showSymbol = false;
    private String symbol;
    private Locale localeNew;
    private NumberFormat fmt;
    private CharSequence text;
    private BufferType bufferType;
    private int fractionDigit = 2;

    public CurrencyTextView(Context context) {
        this(context, null);


    }

    public CurrencyTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public CurrencyTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);

    }


    private void init(AttributeSet attrs) {
        if (attrs != null) {
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.CurrencyTextView);
            locale = a.getString(R.styleable.CurrencyTextView_countryName) != null && !TextUtils.isEmpty(a.getString(R.styleable.CurrencyTextView_countryName)) ?
                    a.getString(R.styleable.CurrencyTextView_countryName) : "India";
//            decimalPlace = a.getInt(R.styleable.CurrencyTextView_decimal_place, 2);
            grouping = a.getBoolean(R.styleable.CurrencyTextView_grouping_currency, false);
            showSymbol = a.getBoolean(R.styleable.CurrencyTextView_showCurrencySymbol, false);
            a.recycle();
            initAttribut();
        }
        setText();
    }

    private void setText() {

        super.setText(getDisplayableText(), bufferType);

    }

    private CharSequence getDisplayableText() {
        return getFormatText(text);
    }

    private void initAttribut() {
        if (locale != null && !locale.isEmpty()) {
            localeNew = new Locale("", setCountry(getLocale()));
            Currency currency = Currency.getInstance(localeNew);
            symbol = currency.getSymbol();
            fmt = NumberFormat.getCurrencyInstance(localeNew);
        }

    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        if (TextUtils.isEmpty(text)) {
            return;
        }
        this.text = text;
        bufferType = type;
        setText();
    }


    public void setLocale(String CountryName) {
        this.locale = CountryName;
        initAttribut();
        setText();
    }


    public String setCountry(String country) {
        Map<String, String> countries = new HashMap<>();
        for (String iso : Locale.getISOCountries()) {
            System.out.println(iso);
            Locale l = new Locale("", iso);
            countries.put(l.getDisplayCountry(), iso);
        }


        System.out.println(countries.get("Switzerland"));
        System.out.println(countries.get("Andorra"));
        System.out.println(countries.get("Japan"));
        System.out.println(countries.get("India"));


        return countries.get(country);
    }

    public String getLocale() {
        return locale;
    }


    private String getFormatText(CharSequence charSequence) {
        String formatedString = "";
        try {

            if (!TextUtils.isEmpty(charSequence)) {
                if (fmt != null) {

                    fmt.setGroupingUsed(grouping);
                    fmt.setMaximumFractionDigits(fractionDigit);
                    fmt.setMinimumFractionDigits(fractionDigit);
                    formatedString = fmt.format(Double.parseDouble(charSequence.toString()));

                    if (!showSymbol) {
                        formatedString = formatedString.substring(1, formatedString.length());
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        return formatedString;
    }

    public void setGrouping(boolean grouping) {
        this.grouping = grouping;
        setText();
    }

    public void setShowSymbol(boolean showSymbol) {
        this.showSymbol = showSymbol;
        setText();
    }

    public void setFractionDigit(int fractionDigit) {
        this.fractionDigit = fractionDigit;
        setText();
    }
}

