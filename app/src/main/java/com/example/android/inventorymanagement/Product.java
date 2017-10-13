package com.example.android.inventorymanagement;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Product extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        final InventoryDbHelper db = new InventoryDbHelper(this);
        Intent getItemNumber = getIntent();
        String productName = getItemNumber.getExtras().getString("itemNumber");
        int position = productName.indexOf("\nQuantity");
        final String subProductName = productName.substring(0, position);

        final Cursor cursor = db.getData(subProductName);

        if (cursor.moveToFirst()) {

            // Set Item Name
            TextView name = (TextView) findViewById(R.id.item_name);
            name.setText(subProductName);

            // Set Quantity
            int quantity = cursor.getInt(cursor.getColumnIndex(InventoryContract.InventoryEntry.COLUMN_QUANTITY));
            TextView tQuantity = (TextView) findViewById(R.id.quantity);
            tQuantity.setText("" + quantity);

            // Set Price
            int price = cursor.getInt(cursor.getColumnIndex(InventoryContract.InventoryEntry.COLUMN_PRICE));
            TextView Price = (TextView) findViewById(R.id.price);
            Price.setText("$" + price);

        }

        // Increase quantity by 1
        Button buttonReceive = (Button) findViewById(R.id.Get);
        buttonReceive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cursor.moveToFirst()) {
                    int quantity = cursor.getInt(cursor.getColumnIndex(InventoryContract.InventoryEntry.COLUMN_QUANTITY));
                    db.updateData(subProductName, quantity, 1);
                    quantity = cursor.getInt(cursor.getColumnIndex(InventoryContract.InventoryEntry.COLUMN_QUANTITY));
                    TextView mQuantity = (TextView) findViewById(R.id.quantity);
                    mQuantity.setText("" + quantity);
                    Toast.makeText(Product.this, "Refresh!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Decrease quantity by 1
        Button buttonTrack = (Button) findViewById(R.id.Buy);
        buttonTrack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cursor.moveToFirst()) {
                    int quantity = cursor.getInt(cursor.getColumnIndex(InventoryContract.InventoryEntry.COLUMN_QUANTITY));
                    if (quantity > 0) {
                        db.updateData(subProductName, quantity, -1);
                        quantity = cursor.getInt(cursor.getColumnIndex(InventoryContract.InventoryEntry.COLUMN_QUANTITY));
                        TextView mQuantity = (TextView) findViewById(R.id.quantity);
                        mQuantity.setText("" + quantity);
                        Toast.makeText(Product.this, "Refresh!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(Product.this, "Stock Up Now!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });



        // Place Order
        Button placeOrder = (Button) findViewById(R.id.order);
        placeOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String productName = "";
                if (cursor.moveToFirst()) {
                    productName = cursor.getString(cursor.getColumnIndex(InventoryContract.InventoryEntry.COLUMN_PRODUCT_NAME));
                }
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("message/rfc822");
                intent.putExtra(Intent.EXTRA_TEXT, "Some More " + productName + " Needed");
                startActivity(Intent.createChooser(intent, "Send Email"));
            }
        });

        // delete one row
        Button delete1 = (Button) findViewById(R.id.delete_data);
        delete1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE:
                                if (db.deleteData(subProductName)) {
                                    Intent returnHome = new Intent(Product.this, MainActivity.class);
                                    startActivity(returnHome);
                                    Toast.makeText(Product.this, "Deleted!", Toast.LENGTH_SHORT).show();
                                }
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                break;
                        }
                    }
                };
                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(Product.this);
                alertBuilder.setMessage("Are you sure you want to delete?").setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();
            }
        });


        ImageView img = (ImageView) findViewById(R.id.imageView);
        byte[] image = cursor.getBlob(cursor.getColumnIndex(InventoryContract.InventoryEntry.COLUMN_IMAGE));
        Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);
        img.setImageBitmap(bitmap);
    }
}
