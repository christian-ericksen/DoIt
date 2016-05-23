package com.ericksen.christian.todolist;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Scanner;

public class SecondActivity extends AppCompatActivity {

    private ArrayList<String> arrayListToDoDetail;
    private ArrayAdapter<String> arrayAdapterToDoDetail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);


        arrayListToDoDetail = new ArrayList<>();
        arrayAdapterToDoDetail = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, arrayListToDoDetail);
        final ListView listView = (ListView) findViewById(R.id.listviewdetail);
        if (listView != null) {
            listView.setAdapter(arrayAdapterToDoDetail);

            registerForContextMenu(listView);

            try {
                Log.i("ON CREATE", "The onCreate has occurred");

                Scanner scanner = new Scanner(openFileInput("ToDo.txt"));

                while (scanner.hasNextLine()) {
                    String toDo = scanner.nextLine();
                    arrayAdapterToDoDetail.add(toDo);
                }
                scanner.close();
            } catch (Exception e) {
                Log.i("ON CREATE", e.getMessage());
            }

        }


        FloatingActionButton add = (FloatingActionButton) findViewById(R.id.addbuttondetail);
        assert add != null;
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText editTextToDoDetail = (EditText) findViewById(R.id.edittextdetail);
                String toDo = editTextToDoDetail.getText().toString().trim();
                editTextToDoDetail.setText("");


                if (toDo.isEmpty()) {
                    return;
                }

                arrayAdapterToDoDetail.add(toDo);


            }
        });

        Button back = (Button) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(SecondActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        if (v.getId() != R.id.listviewdetail) {
            return;
        }
        menu.setHeaderTitle("Delete Entry?");
        String[] options = {"Yes", "No"};

        for (String option : options) {
            menu.add(option);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int selectedIndex = info.position;

        if (item.getTitle().equals("Yes")) {
            arrayListToDoDetail.remove(selectedIndex);
            arrayAdapterToDoDetail.notifyDataSetChanged();
        }

        return true;
    }

//    @Override
//    public void onBackPressed() {
//        Intent intent = new Intent(SecondActivity.this, MainActivity.class);
//        startActivity(intent);
//    }

}