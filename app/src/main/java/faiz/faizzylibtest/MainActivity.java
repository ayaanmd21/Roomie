package faiz.faizzylibtest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    CurrencyTextView tvCurrency;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvCurrency= (CurrencyTextView) findViewById(R.id.tv_currency);
        tvCurrency.setText("15058901.0799962");
        tvCurrency.setFractionDigit(2);
        tvCurrency.setGrouping(true);
        tvCurrency.setShowSymbol(true);
        tvCurrency.setLocale("Algeria");
    }
}
