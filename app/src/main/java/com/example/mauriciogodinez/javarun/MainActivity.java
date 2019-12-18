package com.example.mauriciogodinez.javarun;

import android.annotation.TargetApi;
import android.content.Intent;
import android.icu.text.NumberFormat;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView quantityTextView;

    private CheckBox whippedCheckBox;
    private CheckBox chocolateCheckBox;
    private EditText nameEditText;

    private Integer quantity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        quantityTextView = findViewById(R.id.quantity_text_view);
        whippedCheckBox = findViewById(R.id.whipped_check_box);
        chocolateCheckBox = findViewById(R.id.chocolate_check_box);
        nameEditText = findViewById(R.id.name_field);

        Button moreButton = findViewById(R.id.more_button);
        Button lessButton = findViewById(R.id.less_button);
        Button orderButton = findViewById(R.id.order_button);

        moreButton.setOnClickListener(this);
        lessButton.setOnClickListener(this);
        orderButton.setOnClickListener(this);

        quantity = Integer.parseInt(quantityTextView.getText().toString());
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        switch (id){
            case R.id.more_button:
                more();
                break;
            case R.id.less_button:
                less();
                break;
            case R.id.order_button:
                order();
                break;
            default:
                break;
        }
    }

    private void more(){
        if (quantity == 100){
            Toast.makeText(this, getString(R.string.toast_plus), Toast.LENGTH_SHORT).show();
            return;
        }else{
            quantity++;
        }
        displayQuantity(quantity);
    }

    private void less(){
        if (quantity == 1){
            Toast.makeText(this, getString(R.string.toast_less), Toast.LENGTH_SHORT).show();
            return;
        }else{
            quantity--;
        }
        displayQuantity(quantity);
    }

    private void displayQuantity(Integer q){
        quantityTextView.setText(q.toString());
    }

    private void order(){
        String name_text = nameEditText.getText().toString();

        boolean hasWhippedCream = whippedCheckBox.isChecked();
        boolean hasChocolate = chocolateCheckBox.isChecked();

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

    private int calculatePrice(boolean addWhippedCream, boolean addChocolate){
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
    private String createOrderSummary(String name, int price,
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