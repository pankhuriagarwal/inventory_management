package com.example.android.inventorymanagement;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final InventoryDbHelper db = new InventoryDbHelper(this);

        final ListView listView = (ListView) findViewById(R.id.list);
        listView.setEmptyView(findViewById(R.id.empty_text));
        ArrayList<String> list = db.getCompleteData();
        InventoryAdapter arrayAdapter = new InventoryAdapter(
                MainActivity.this, list);
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(MainActivity.this, Product.class);
                String itemSelected = ((TextView) view.findViewById(R.id.text)).getText().toString();
                intent.putExtra("itemNumber", itemSelected);
                startActivity(intent);
            }
        });

        Button add = (Button) findViewById(R.id.addButton);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addIntent = new Intent(MainActivity.this, AddItem.class);
                startActivity(addIntent);
            }
        });

        // Refresh List
        Button refresh = (Button) findViewById(R.id.refreshButton);
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final ListView listView = (ListView) findViewById(R.id.list);
                listView.setEmptyView(findViewById(R.id.empty_text));
                ArrayList<String> list = db.getCompleteData();
                InventoryAdapter mAdapter = new InventoryAdapter(MainActivity.this, list);
                listView.setAdapter(mAdapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Intent intent = new Intent(MainActivity.this, Product.class);
                        String itemSelected = ((TextView) view.findViewById(R.id.text)).getText().toString();
                        intent.putExtra("itemNumber", itemSelected);
                        startActivity(intent);
                    }
                });
            }
        });

        // Delete Everything
        Button delete = (Button) findViewById(R.id.deleteButton);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.deleteEveryEntry();
                Toast.makeText(MainActivity.this, "Refresh list", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
