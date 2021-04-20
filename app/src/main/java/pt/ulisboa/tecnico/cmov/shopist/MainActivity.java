package pt.ulisboa.tecnico.cmov.shopist;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ExpandableListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    // Main Activity Options
    ExpandableListView expandableListView;
    List<String> listGroup;
    HashMap<String, List<String>> listItem;
    MainAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        expandableListView = findViewById(R.id.expand_activities_button);
        listGroup = new ArrayList<>();
        listItem = new HashMap<>();
        adapter = new MainAdapter(this, listGroup, listItem);
        expandableListView.setAdapter(adapter);
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
}