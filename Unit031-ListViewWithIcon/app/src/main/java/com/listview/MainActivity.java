package com.listview;

import android.app.ListActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends ListActivity {

    private TextView mTxtResult;
    List<Map<String, Object>> mList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTxtResult = (TextView) findViewById(R.id.txtResult);

        mList = new ArrayList<Map<String,Object>>();
        String[] listFromResource = getResources().getStringArray(R.array.weekday);

        for (int i = 0; i < listFromResource.length; i++) {
            Map<String, Object> item = new HashMap<String, Object>();
            item.put("imgView", android.R.drawable.ic_menu_my_calendar);
            item.put("txtView", listFromResource[i]);
            mList.add(item);
        }

        SimpleAdapter adapter = new SimpleAdapter(this, mList,
                R.layout.list_item,
                new String[] { "imgView", "txtView" },
                new int[] { R.id.imgView ,R.id.txtView });

        setListAdapter(adapter);

        ListView listview = getListView();
        listview.setOnItemClickListener(listViewOnItemClick);
    }

    private AdapterView.OnItemClickListener listViewOnItemClick = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view,
                                int position, long id) {
            // 在TextView元件中顯示使用者點選的項目名稱。
            String s =((TextView)view.findViewById(R.id.txtView)).getText().toString();
            mTxtResult.setText(s);
        }
    };
}
