package com.sousablde.simpletodo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import static com.sousablde.simpletodo.MainActivity.ITEM_POSITION;
import static com.sousablde.simpletodo.MainActivity.ITEM_TEXT;

public class EditItemActivity extends AppCompatActivity {
    //track the edit text
    EditText etItemText;
    //position of edited item in list
    int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);

        //resolve edit text from the layout
        etItemText = findViewById(R.id.etItemText);

        //set edit text value from intent extra
        etItemText.setText(getIntent().getStringExtra(ITEM_TEXT));

        //update position from intent extra
        position = getIntent().getIntExtra(ITEM_POSITION, 0);

        //update title bar of the activity
        getSupportActionBar().setTitle("Edit Item");

    }

    //handler for save button
    public void onSaveItem(View v) {
        //create a new intent to pass the result
        Intent i = new Intent();

        //pass updated item
        i.putExtra(ITEM_TEXT, etItemText.getText().toString());

        //pass the original position as extra
        i.putExtra(ITEM_POSITION, position);

        //set the intent as the result of the activity
        setResult(RESULT_OK, i);

        //close the activity and redirect to main
        finish();
    }
}
