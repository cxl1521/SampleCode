package com.recyclerview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        List<String> listStr = new ArrayList<>();
        for (int i = 0; i < 50; i++)
            listStr.add(new String("第" + String.valueOf(i+1) + "項"));

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        // 設定RecyclerView使用的LayoutManager，
        // LayoutManager決定項目的排列方式。
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//      recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
//      recyclerView.setLayoutManager(new StaggeredGridLayoutManager(3,
//                 StaggeredGridLayoutManager.VERTICAL));

        ItemAdapter itemAdapter = new ItemAdapter(listStr);
        recyclerView.setAdapter(itemAdapter);
    }
}
