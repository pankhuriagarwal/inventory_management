package com.example.android.inventorymanagement;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;

public class AddItem extends AppCompatActivity {

    byte[] images;
    private int REQUEST_IMAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);
        final InventoryDbHelper db = new InventoryDbHelper(AddItem.this);
        Button add = (Button) findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText ProductName = (EditText) findViewById(R.id.product_name);
                EditText Quantity = (EditText) findViewById(R.id.quantity);
                EditText Price = (EditText) findViewById(R.id.price);
                String productName = ProductName.getText().toString();
                // Check if name has been set
                if (productName.length() == 0) {
                    Toast.makeText(AddItem.this, "Please Add a Product Name.", Toast.LENGTH_SHORT).show();
                    return;
                }
                // Check if quantity has been set
                String mQuantity = Quantity.getText().toString();
                if (mQuantity.length() == 0) {
                    Toast.makeText(AddItem.this, "Please Add quantity.", Toast.LENGTH_SHORT).show();
                    return;
                }
                int quantity = Integer.parseInt(mQuantity);
                String mPrice = Price.getText().toString();

                // Check if price has been set
                if (mPrice.length() == 0) {
                    Toast.makeText(AddItem.this, "Please Add price.", Toast.LENGTH_SHORT).show();
                    return;
                }
                int price = Integer.parseInt(mPrice);
                // Check if image has been set
                if (images == null) {
                    Toast.makeText(AddItem.this, "Please Add an image.", Toast.LENGTH_SHORT).show();
                    return;
                }
                db.insertData(productName, quantity, price, images);
                Intent mainIntent = new Intent(AddItem.this, MainActivity.class);
                startActivity(mainIntent);
                String message = "Product Added!";
                Toast.makeText(AddItem.this, message, Toast.LENGTH_SHORT).show();
            }
        });

        // Capture Image
        Button addImage = (Button) findViewById(R.id.addImage);
        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE);
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestImage, int resultCode, Intent data) {

        if (requestImage == REQUEST_IMAGE && resultCode == RESULT_OK && data != null) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");

            ImageView image = (ImageView) findViewById(R.id.Image);
            image.setImageBitmap(imageBitmap);


            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            imageBitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
            images = stream.toByteArray();
        }
    }
}
