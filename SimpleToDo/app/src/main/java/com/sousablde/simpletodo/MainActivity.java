package com.sousablde.simpletodo;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    //I will now start the implementation of the edit activity action
    //this is part of the stretch goal of this assignment

    //a numeric code to identify the edit activity
    public final static int EDIT_REQUEST_CODE = 20;
    //keys used for passing data between activities
    public final static String ITEM_TEXT = "itemText";
    public final static String ITEM_POSITION = "itemPosition";

    //adding objects that will be associated with any instance of main activity
    //android is the one that manages the creation and destruction of this instance

    //starting with the model
    ArrayList<String> items;

    //declaring the adapter, intermediary object that wires model (arraylist) to the view
    ArrayAdapter<String> itemsAdapter;

    //to have an instance of the ListView itself we will have a listview object
    ListView lvItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //now to initialize the created objects
        //items = new ArrayList<>();
        readItems();
        itemsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items);

        //now to resolve the instance that already exists for listView
        //the instructional video suggests to cast findViewById to ListView
        //but that is now considered redundant
        //code suggested: vItems = (ListView) findViewById(R.id.lvItems);
        //my code:
        lvItems = findViewById(R.id.lvItems);

        //now to wire the adapter to the listview
        lvItems.setAdapter(itemsAdapter);

        //mock data
        //items.add("Be better");
        //items.add("Do better");

        setupListViewListener();
    }

    //creating a method to handle the add button
    public void onAddItem(View v) {
        EditText etNewItem = findViewById(R.id.etNewName);
        String itemText = etNewItem.getText().toString();
        itemsAdapter.add(itemText);
        etNewItem.setText("");
        writeItems();
        Toast.makeText(getApplicationContext(), "Item Added to list", Toast.LENGTH_SHORT).show();
    }

    //adding the remove method, press and hold action will remove
    private void setupListViewListener() {
        Log.i("MainActivity", "Setting up listener on list view");
        lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i("MainActivity", "Item removed from list: " + position);
                items.remove(position);
                itemsAdapter.notifyDataSetChanged();
                writeItems();
                return true;
            }
        });

        //set up item listener for edit (regular click)
        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                //create new activity
                //pass data being edited
                //display activity of the user
                Intent i = new Intent(MainActivity.this, EditItemActivity.class);
                i.putExtra(ITEM_TEXT, items.get(position));
                //passing the position to insure editing happens in selected item line
                i.putExtra(ITEM_POSITION, position);
                //display the activity
                startActivityForResult(i, EDIT_REQUEST_CODE);
            }
        });
    }

    //handle results from edit activity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //if the edit completed ok
        if (resultCode == RESULT_OK && requestCode == EDIT_REQUEST_CODE) {
            //extract updated item text from result intent extras
            String updatedItem = data.getExtras().getString(ITEM_TEXT);
            //extract original positions= of edited items
            int position = data.getExtras().getInt(ITEM_POSITION);
            //update the model with the new item text at the edit position
            items.set(position, updatedItem);
            //notify the adapter that the model changed
            itemsAdapter.notifyDataSetChanged();
            //persist the changed model
            writeItems();
            //using a toast like before to notify the user that the operation was successfully concluded
            Toast.makeText(this, "Item updated successfully", Toast.LENGTH_SHORT).show();

        }
    }

    private File getDataFile() {
        return new File(getFilesDir(), "todo.txt");
    }

    private void readItems() {
        try {
            items = new ArrayList<>(FileUtils.readLines(getDataFile(), Charset.defaultCharset()));
        } catch (IOException e) {
            Log.e("MainActivity", "Error reading file", e);
            items = new ArrayList<>();
        }
    }

    private void writeItems() {
        try {
            FileUtils.writeLines(getDataFile(), items);
        } catch (IOException e) {
            Log.e("MainActivity", "Error writing file", e);
        }

    }
}
