package leoalvesmachado.com.fuelguide;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static String DATA = "APP_DATA";
    private EditText gasolinePrice;
    private EditText ethanolPrice;
    private EditText gasolineCons;
    private EditText ethanolCons;
    private TextView result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences sharedPreferences = getSharedPreferences(DATA, MODE_PRIVATE);

        gasolinePrice = (EditText) findViewById(R.id.valorGasolina);
        ethanolPrice = (EditText) findViewById(R.id.valorAlcool);
        gasolineCons = (EditText) findViewById(R.id.consumoGasolina);
        ethanolCons =  (EditText) findViewById(R.id.consumoAlcool);
        result = (TextView) findViewById(R.id.resultText);

        if (sharedPreferences.contains("gasolinePrice")) {
            gasolinePrice.setText(sharedPreferences.getString("gasolinePrice", ""));
        }
        if (sharedPreferences.contains("ethanolPrice")) {
            ethanolPrice.setText(sharedPreferences.getString("ethanolPrice", ""));
        }
        if (sharedPreferences.contains("gasolineCons")) {
            gasolineCons.setText(sharedPreferences.getString("gasolineCons", ""));
        }
        if (sharedPreferences.contains("ethanolCons")) {
            ethanolCons.setText(sharedPreferences.getString("ethanolCons", ""));
        }
        if (sharedPreferences.contains("result")) {
            result.setText(sharedPreferences.getString("result", result.getText().toString()));
        }
        Button calculate = (Button) findViewById(R.id.calculate);
        calculate.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View view) {
                double gp = convertInsertedTextToDouble(gasolinePrice);
                double gc = convertInsertedTextToDouble(gasolineCons);
                double ep = convertInsertedTextToDouble(ethanolPrice);
                double ec = convertInsertedTextToDouble(ethanolCons);
                if (gp <= 0 || gc <= 0 || ep <= 0 || ec <= 0) {
                    result.setText(R.string.invalid_data_entered);
                    return;
                }
                double gasolineValue = gp / gc;
                double ethanolValue = ep / ec;
                if (gasolineValue > ethanolValue) {
                    result.setText(R.string.ethanol_better);
                } else {
                    result.setText(R.string.gasoline_better);
                }
                saveData();
            }
        });
    }

    private void saveData() {
        SharedPreferences sharedPreferences = getSharedPreferences(DATA, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString("gasolinePrice", gasolinePrice.getText().toString());
        editor.putString("gasolineCons", gasolineCons.getText().toString());
        editor.putString("ethanolPrice", ethanolPrice.getText().toString());
        editor.putString("ethanolCons", ethanolCons.getText().toString());
        editor.putString("result", result.getText().toString());

        editor.commit();
    }

    private double convertInsertedTextToDouble(EditText field) {
        double result;
        try {
            result = Double.parseDouble(field.getText().toString());
        } catch (NumberFormatException e) {
            try {
                result = Double.parseDouble(field.getHint().toString()); //user didn't enter any text, using hint
            } catch (NumberFormatException e2) {
                result = -1; //Invalid data entered
            }
        }
        return result;
    }

    private void goToNewIntent(Class cls) {
        Intent intent = new Intent(MainActivity.this, cls);
        startActivity(intent);
    }

    @Override
    protected void onPause() {
        saveData();
        super.onPause();
    }
}
