package com.example.simpletodo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import org.apache.commons.io.FileUtils;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    List<String> listItems;
    Button addButton;
    EditText editTextItem;
    RecyclerView recyclerViewItems;
    ItemsAdapter itemsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addButton = findViewById(R.id.Add_Button);
        editTextItem = findViewById(R.id.Edit_Text_Item);
        recyclerViewItems = findViewById(R.id.Recycler_View_Items);

        loadItems();
        /*
        listItems = new ArrayList<>();
        listItems.add("Setup Android");
        listItems.add("Setup SimpleToDo App");
        listItems.add("Design SimpleToDo App");
        listItems.add("Render items for SimpleToDo App");
        listItems.add("Implement Add and Remove for SimpleToDo App");
        listItems.add("Submit SimpleToDo App");
         */

        ItemsAdapter.OnLongClickListener onLongClickListener = new ItemsAdapter.OnLongClickListener() {
            @Override
            public void onItemLongClicked(int position) {
                // Delete the item from the model
                listItems.remove(position);
                // Notify the adapter
                itemsAdapter.notifyItemRemoved(position);

                Toast.makeText(getApplicationContext(), "Item was removed!", Toast.LENGTH_SHORT).show();
                saveItems();
            }
        };

        itemsAdapter = new ItemsAdapter(listItems, onLongClickListener);
        recyclerViewItems.setAdapter(itemsAdapter);
        recyclerViewItems.setLayoutManager(new LinearLayoutManager(this));

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String todoItem = editTextItem.getText().toString();
                // Add item to the model
                listItems.add(todoItem);

                // Notify adapter that an item is inserted
                itemsAdapter.notifyItemInserted(listItems.size() - 1);
                editTextItem.setText("");

                Toast.makeText(getApplicationContext(), "Item was added!", Toast.LENGTH_SHORT).show();
                saveItems();
            }
        });
    }

    private File getDataFile() {
        return new File(getFilesDir(), "data.txt");
    }

    // This function will load items by reading every line of the datafile
    private void loadItems() {
        try {
            listItems = new ArrayList<>(FileUtils.readLines(getDataFile(), Charset.defaultCharset()));
        } catch (IOException e) {
            Log.e("MainActivity", "Error reading items", e);
            listItems = new ArrayList<>();
        }
    }

    // This function saves items by writing them to the datafile
    private void saveItems() {
        try {
            FileUtils.writeLines(getDataFile(), listItems);
        } catch (IOException e) {
            Log.e("MainActivity", "Error writing items", e);
        }
    }
}