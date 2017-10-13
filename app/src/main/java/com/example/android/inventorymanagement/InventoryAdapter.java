package com.example.android.inventorymanagement;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by pankhuriagarwal on 12/2/16.
 */

public class InventoryAdapter extends ArrayAdapter<String> {

    public InventoryAdapter(Context context, ArrayList<String> list) {
        super(context, 0, list);
    }

    @Override
    public View getView(int position, View convertView, final ViewGroup parent) {

        // Check if an existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);
        }
        final String currentItem = getItem(position);

        TextView setTextItem = (TextView) listItemView.findViewById(R.id.text);
        setTextItem.setText(currentItem);
        Button buyButton = (Button) listItemView.findViewById(R.id.buy);
        buyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final InventoryDbHelper db = new InventoryDbHelper(getContext());
                int pos = currentItem.indexOf("\nQuantity");
                final String productName = currentItem.substring(0, pos);
                final Cursor cur = db.getData(productName);
                if (cur.moveToFirst()) {
                    int quantity = cur.getInt(cur.getColumnIndex(InventoryContract.InventoryEntry.COLUMN_QUANTITY));
                    if (quantity > 0) {
                        db.updateData(productName, quantity, -1);
                        Toast.makeText(getContext(), "Refresh!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), "Stock Up Now!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        return listItemView;
    }
}
