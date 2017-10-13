package com.example.android.inventorymanagement;

import android.provider.BaseColumns;

/**
 * Created by pankhuriagarwal on 12/1/16.
 */

public class InventoryContract {
    private InventoryContract() {
    }


    public static final class InventoryEntry implements BaseColumns {


        public final static String _ID = BaseColumns._ID;
        public final static String TABLE_NAME = "Inventory";

        public final static String COLUMN_PRODUCT_NAME = "Name";

        public static final String COLUMN_PRICE = "Price";

        public static final String COLUMN_QUANTITY = "Quantity";

        public static final String COLUMN_IMAGE = "Image";
    }
}
