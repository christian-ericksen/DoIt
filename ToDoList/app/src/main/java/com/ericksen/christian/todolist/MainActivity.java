package com.ericksen.christian.todolist;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class MainActivity extends Activity {

    private ArrayList<String> arrayListToDo;
    private ArrayAdapter<String> arrayAdapterToDo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        arrayListToDo = new ArrayList<>();
        arrayAdapterToDo = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, arrayListToDo);
        final ListView listView = (ListView) findViewById(R.id.listview);
        if (listView != null) {
            listView.setAdapter(arrayAdapterToDo);

            registerForContextMenu(listView);

            try{
                Log.i("ON CREATE", "The onCreate has occurred");

               Scanner scanner = new Scanner(openFileInput("ToDo.txt"));

                while(scanner.hasNextLine()) {
                    String toDo = scanner.nextLine();
                    arrayAdapterToDo.add(toDo);
                }
                scanner.close();
            }catch(Exception e){
                Log.i("ON CREATE", e.getMessage());
            }

        }



        FloatingActionButton add = (FloatingActionButton) findViewById(R.id.addbutton);
        assert add != null;
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText editTextToDo = (EditText) findViewById(R.id.edittext);
                String toDo = editTextToDo.getText().toString().trim();
                editTextToDo.setText("");


                if (toDo.isEmpty()) {
                    return;
                }

                arrayAdapterToDo.add(toDo);


            }
        });

        assert listView != null;
        listView.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = (String) listView.getItemAtPosition(position);
                Intent intent = new Intent(MainActivity.this, SecondActivity.class);
                startActivity(intent);
            }
        });

    }





    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        if (v.getId() != R.id.listview) {
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
            arrayListToDo.remove(selectedIndex);
            arrayAdapterToDo.notifyDataSetChanged();
        }

        return true;
    }

    @Override
    public void onBackPressed() {
        try{
            Log.i("ON BACK PRESSED", "the on back event has occurred");

            PrintWriter pw = new PrintWriter(openFileOutput("ToDo.txt", Context.MODE_PRIVATE));

            for(String toDo : arrayListToDo){
                pw.println(toDo);
            }

            pw.close();

        }catch(Exception e){
            Log.i("ON BACK PRESSED", e.getMessage());
        }
    }


//    public void onItemClickListener(){
//        listView.setOnItemClickListener(new ListView.OnItemClickListener{
//            @Override
//                    public void onItemClick(View view){
//                Intent intent =new Intent(MainActivity.this, SecondActivity.class);
//                startActivity(intent);
//            }
//        });
//    }

}

