package com.example.mauriciogodinez.javarun;

import android.annotation.TargetApi;
import android.content.Intent;
import android.icu.text.NumberFormat;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.ShareCompat;
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

    private Integer quantity = 2;

    public static final String QUANTITY_EXTRA = "quantity";

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
        Button webpageButton = findViewById(R.id.website_button);
        Button locationButton = findViewById(R.id.location_button);
        Button shareButton = findViewById(R.id.share_button);

        moreButton.setOnClickListener(this);
        lessButton.setOnClickListener(this);
        orderButton.setOnClickListener(this);
        webpageButton.setOnClickListener(this);
        locationButton.setOnClickListener(this);
        shareButton.setOnClickListener(this);

        quantity = Integer.parseInt(quantityTextView.getText().toString());

        if(savedInstanceState != null){
            if(savedInstanceState.containsKey(QUANTITY_EXTRA)){
                quantity = savedInstanceState.getInt(QUANTITY_EXTRA);
                displayQuantity(quantity);
            }
        }
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
            case R.id.website_button:
                String urlAsString = "http://www.udacity.com";
                openWebPage(urlAsString);
                break;
            case R.id.location_button:
                String addressString = "1600 Amphitheatre Parkway, CA";
                showMap(addressString);
                break;
            case R.id.share_button:
                String textTobeShare = "I want to share this";
                shareText(textTobeShare);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if(quantity > 0)
            outState.putInt(QUANTITY_EXTRA, quantity);
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

        int price = calculatePrice(hasWhippedCream, hasChocolate, quantity);

        String priceMessage = createOrderSummary(name_text, hasWhippedCream, hasChocolate,
                quantity, price);

        Intent intent = new Intent(Intent.ACTION_SENDTO);

        intent.setData(Uri.parse("mailto:"));
        intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.order_summary_message, name_text));
        intent.putExtra(Intent.EXTRA_TEXT, priceMessage);

        startMyActivities(intent);
    }

    private int calculatePrice(boolean addWhippedCream, boolean addChocolate, int quantity){
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
    private String createOrderSummary(String name, boolean addWhippedCream,
                                      boolean addChocolate, int quantity, int price){
        String priceMessage = getString(R.string.order_summary_name, name);

        priceMessage += "\n" + getString(R.string.order_summary_w_cream, addWhippedCream);
        priceMessage += "\n" + getString(R.string.order_summary_chocolate, addChocolate);
        priceMessage += "\n" + getString(R.string.order_summary_quantity, quantity);
        priceMessage += "\n" + getString(R.string.order_summary_price,
                NumberFormat.getCurrencyInstance().format(price));
        priceMessage += "\n" + getString(R.string.thank_you);

        return priceMessage;
    }

    private void startMyActivities(Intent intent){
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    private void openWebPage(String url) {
        Uri webpage = Uri.parse(url);

        Intent intent = new Intent(Intent.ACTION_VIEW, webpage);

        startMyActivities(intent);
    }

    private void showMap(String addressString) {
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("geo")
                .path("0,0")
                .appendQueryParameter("q", addressString);

        Uri addressUri = builder.build();

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(addressUri);

        startMyActivities(intent);
    }

    private void shareText(String text){
        String mimeType = "text/plain";
        String titleForWindow = "This is it";

        ShareCompat.IntentBuilder
                .from(this)
                .setChooserTitle(titleForWindow)
                .setType(mimeType)
                .setText(text)
                .startChooser();
    }
}