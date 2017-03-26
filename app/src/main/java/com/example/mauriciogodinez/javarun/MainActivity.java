package com.example.mauriciogodinez.javarun;

import android.annotation.TargetApi;
import android.content.Intent;
import android.icu.text.NumberFormat;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    TextView quantity_text;

    CheckBox checkW;
    CheckBox checkC;

    EditText name;
    Integer quantity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        quantity_text = (TextView) findViewById(R.id.quantity_text_view);

        checkW = (CheckBox) findViewById(R.id.whipped_check_box);
        checkC = (CheckBox) findViewById(R.id.chocolate_check_box);

        name = (EditText) findViewById(R.id.name_field);
        quantity = Integer.parseInt(quantity_text.getText().toString());

    }

    public void more(View view){
        if (quantity == 100){
            Toast.makeText(this, getString(R.string.toast_plus), Toast.LENGTH_SHORT).show();
            return;
        }else{
            quantity++;
        }
        displayQuantity(quantity);
    }

    public void less(View view){
        if (quantity == 1){
            Toast.makeText(this, getString(R.string.toast_less), Toast.LENGTH_SHORT).show();
            return;
        }else{
            quantity--;
        }
        displayQuantity(quantity);
    }

    public void displayQuantity(Integer q){
        quantity_text.setText(q.toString());
    }

    public void order(View view){
        String name_text = name.getText().toString();
        boolean hasWhippedCream = checkW.isChecked();
        boolean hasChocolate = checkC.isChecked();

        int price = calculatePrice(hasWhippedCream, hasChocolate);
        String priceMessage = createOrderSummary(name_text, price, hasWhippedCream, hasChocolate);
//        Intent i = new Intent(Intent.ACTION_VIEW);
//        i.setData(Uri.parse("geo:47.6, -122.3"));
        Intent i = new Intent(Intent.ACTION_SENDTO);

        i.setData(Uri.parse("mailto:"));

//        i.putExtra(Intent.EXTRA_EMAIL, address);
        i.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.order_summary_message, name_text));
        i.putExtra(Intent.EXTRA_TEXT, priceMessage);

        if(i.resolveActivity(getPackageManager()) != null){
            startActivity(i);
        }
    }

    protected int calculatePrice(boolean addWhippedCream, boolean addChocolate){
        int basePrice = 5;

        if(addWhippedCream){
            basePrice += 1;
        }
        if(addChocolate){
            basePrice += 2;
        }

        return quantity * basePrice;
    }

    @TargetApi(Build.VERSION_CODES.N)
    protected String createOrderSummary(String name, int price,
                                        boolean addWhippedCream, boolean addChocolate){
        String priceMessage = getString(R.string.order_summary_name, name);

        priceMessage += "\n" + getString(R.string.order_summary_w_cream, addWhippedCream);
        priceMessage += "\n" + getString(R.string.order_summary_chocolate, addChocolate);
        priceMessage += "\n" + getString(R.string.order_summary_quantity, quantity);
        priceMessage += "\n" + getString(R.string.order_summary_price,
                NumberFormat.getCurrencyInstance().format(price));
        priceMessage += "\n" + getString(R.string.thank_you);

        return priceMessage;
    }
}
