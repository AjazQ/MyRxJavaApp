package com.aqsoft.myrxjavaapp;

import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.aqsoft.myrxjavaapp.nLevel.NLevelAdapter;
import com.aqsoft.myrxjavaapp.nLevel.NLevelItem;
import com.aqsoft.myrxjavaapp.nLevel.NLevelView;
import com.aqsoft.myrxjavaapp.nLevel.SomeObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    List<NLevelItem> list;
    ListView listView;
    ExpandableListView expListView;


    int[] colors = {Color.BLUE, Color.RED, Color.MAGENTA, Color.GRAY, Color.GREEN, Color.YELLOW};
    int[] icons = {android.R.drawable.btn_star, android.R.drawable.ic_btn_speak_now, android.R.drawable.btn_radio,android.R.drawable.ic_dialog_dialer,android.R.drawable.gallery_thumb,android.R.drawable.btn_plus,android.R.drawable.btn_minus};
    String jsonStringList = "[{\"title\":\"Root 1\",\"children\":[{\"title\":\"Child 11\",\"children\":[{\"title\":\"Extended Child 111\",\"children\":[{\"title\":\"Super Extended Child 1111\",\"children\":[]}]},{\"title\":\"Extended Child 112\",\"children\":[]},{\"title\":\"Extended Child 113\",\"children\":[]}]},{\"title\":\"Child 12\",\"children\":[{\"title\":\"Extended Child 121\",\"children\":[]},{\"title\":\"Extended Child 122\",\"children\":[]}]},{\"title\":\"Child 13\",\"children\":[]}]},{\"title\":\"Root 2\",\"children\":[{\"title\":\"Child 21\",\"children\":[{\"title\":\"Extended Child 211\",\"children\":[]},{\"title\":\"Extended Child 212\",\"children\":[]},{\"title\":\"Extended Child 213\",\"children\":[]}]},{\"title\":\"Child 22\",\"children\":[{\"title\":\"Extended Child 221\",\"children\":[]},{\"title\":\"Extended Child 222\",\"children\":[]}]},{\"title\":\"Child 23\",\"children\":[]}]},{\"title\":\"Root 3\",\"children\":[]}]";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //expListView = (ExpandableListView) findViewById(R.id.expList);
        //expListView.setAdapter();
        //String bigJsonArray = getJsonArray();
        //jsonStringList = bigJsonArray;
        NLevelExpandableListView();

    }

    private String getJsonArray(){

        JSONArray jsonArray = new JSONArray();
        try {


        for(int i=0;i<100; i++){
                JSONObject jo = new JSONObject();
                int pItemId = i+1;
                jo.put("title", "Parent Level " + pItemId);
            JSONArray children = getArray("1st level", 4);
            jo.put("children", children);
                jsonArray.put(jo);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return jsonArray.toString();

    }

    private static @NonNull JSONArray getArray(String title, int len) throws JSONException {
        JSONArray children = new JSONArray();
        for(int j=0;j<len;j++){
            JSONObject joChild = new JSONObject();
            int itemId= j+1;
            joChild.put("title", title + " " + itemId);
            if(len>0) {
                joChild.put("children", getArray("Children of " + title, len - 1));
            }
            children.put(joChild);
        }
        return children;
    }

    private void NLevelExpandableListView(){

        listView = (ListView) findViewById(R.id.listView1);
        list = new ArrayList<NLevelItem>();
        final LayoutInflater inflater = LayoutInflater.from(this);
        nestedLoop(jsonStringList, null, inflater, 0);

        NLevelAdapter adapter = new NLevelAdapter(list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                ((NLevelAdapter)listView.getAdapter()).toggle(arg2);
                ((NLevelAdapter)listView.getAdapter()).getFilter().filter();
            }
        });

    }

    private void nestedLoop(String levelList, NLevelItem nLevelItem, final LayoutInflater inflater, int level){

        try{

            JSONArray jsonArrayStringList = new JSONArray(levelList);
            int length = jsonArrayStringList.length();

            for (int i=0; i<length; i++){
                JSONObject itemObject = jsonArrayStringList.getJSONObject(i);
                int childrenSize = itemObject.getJSONArray("children").length();
                NLevelItem Parent = itemView(i, itemObject.getString("title"), nLevelItem, inflater, level, !(childrenSize>0));
                list.add(Parent);

                if(childrenSize>0){
                    nestedLoop(itemObject.getJSONArray("children").toString(), Parent, inflater, level+1);
                }
            }

        }catch (Exception e){

        }

    }

    private NLevelItem itemView(int itemRow, final String Title, NLevelItem nLevelItem, final LayoutInflater inflater, final int level, final boolean isLast){

        SomeObject listItemObject = new SomeObject(Title);
        listItemObject.setIcon(icons[level]);
        NLevelItem superChild = new NLevelItem(listItemObject, nLevelItem, new NLevelView() {
            @Override
            public View getView(NLevelItem item) {
                View view = inflater.inflate(R.layout.list_item, null);
                TextView tv = (TextView) view.findViewById(R.id.textView);
                ImageView imageView = (ImageView) view.findViewById(R.id.imageView);
                String name = (String) ((SomeObject) item.getWrappedObject()).getName();
                tv.setText(name);
                //tv.setBackgroundColor(colors[level]);
                int icon =((SomeObject)(item.getWrappedObject())).getIcon();
                if(icon!=0) {
                    imageView.setImageDrawable(getDrawable(icon));
                }
                ViewGroup.MarginLayoutParams mlp = (ViewGroup.MarginLayoutParams) imageView.getLayoutParams();
                mlp.setMargins(level*50, 5, 5, 5);

                if(isLast){
                    tv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText(MainActivity.this, "Clicked on: "+Title, Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                return view;
            }
        });

        return superChild;
    }

}