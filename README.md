# CurrencyTextView
currency is a library to set amount in currency format

xml code...

 <faiz.faizzylibtest.CurrencyTextView
 
        android:id="@+id/tv_currency"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:gravity="center"
        android:inputType="numberDecimal"
        android:text="45555455.556355"
        android:textColor="@color/colorAccent"
        android:textSize="18sp"
        android:layout_centerInParent="true"
        />


Java code..

 tvCurrency= (CurrencyTextView) findViewById(R.id.tv_currency);
 
        tvCurrency.setText("15058901.0799962");
        tvCurrency.setFractionDigit(2);
        tvCurrency.setGrouping(true);
        tvCurrency.setShowSymbol(true);
        tvCurrency.setLocale("Algeria");
