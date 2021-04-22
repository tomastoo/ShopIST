package pt.ulisboa.tecnico.cmov.shopist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import util.main.MainAdapter;
import util.main.SharedClass;

public class MainActivity extends AppCompatActivity implements ExpandableListView.OnChildClickListener {

    public static final String EXTRA_MESSAGE = "pt.ulisboa.tecnico.cmov.shopist.MESSAGE";
    // Main Activity Options
    ExpandableListView expandableListView;
    List<String> listGroup;
    HashMap<String, List<String>> listItem;
    MainAdapter adapter;
    private SharedClass sharedClass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ((SharedClass)getApplicationContext()).instanceDb();
        expandableListView = findViewById(R.id.expand_activities_button);
        listGroup = new ArrayList<>();
        listItem = new HashMap<>();
        adapter = new MainAdapter(this, listGroup, listItem);
        expandableListView.setAdapter(adapter);
        expandableListView.setOnChildClickListener(this);
        initListData();
    }

    private void initListData() {
        listGroup.add(getString(R.string.group1));
        listGroup.add(getString(R.string.group2));
        listGroup.add(getString(R.string.group3));

        String[] array;
        List<String> list1 = new ArrayList<>();
        array = getResources().getStringArray(R.array.group1);
        for(String item : array){
            list1.add(item);
        }

        List<String> list2 = new ArrayList<>();
        array = getResources().getStringArray(R.array.group2);
        for(String item : array){
            list2.add(item);
        }

        List<String> list3 = new ArrayList<>();
        array = getResources().getStringArray(R.array.group3);
        for(String item : array){
            list3.add(item);
        }

        listItem.put(listGroup.get(0), list1);
        listItem.put(listGroup.get(1), list2);
        listItem.put(listGroup.get(2), list3);
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
        String item = (String) parent.getExpandableListAdapter().getChild(groupPosition, childPosition);
        /*Toast.makeText(getApplicationContext(), item, Toast.LENGTH_SHORT).show();*/

        String group = (String) parent.getExpandableListAdapter().getGroup(groupPosition);
        //Toast.makeText(getApplicationContext(), item, Toast.LENGTH_SHORT).show();

        switch (group){
            case "Menu":
                Toast.makeText(getApplicationContext(), "A1", Toast.LENGTH_SHORT).show();
                break;
            case "Pantry Lists":
                Toast.makeText(getApplicationContext(), "A2", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, PantryList.class);
                intent.putExtra(EXTRA_MESSAGE, item);
                startActivity(intent);
                break;
            case "Shopping Lists":
                Toast.makeText(getApplicationContext(), "A3", Toast.LENGTH_SHORT).show();
                break;
        }
        return false;
    }
}

