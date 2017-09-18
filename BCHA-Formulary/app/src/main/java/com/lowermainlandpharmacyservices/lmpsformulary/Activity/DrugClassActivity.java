package com.lowermainlandpharmacyservices.lmpsformulary.Activity;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lowermainlandpharmacyservices.lmpsformulary.R;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DrugClassActivity extends Activity {
    @BindView(R.id.drugClassListView) ListView drugListView;
    public static final String DRUG_LIST_EXTRA = "drugListExtra";
    public static final String DRUG_LIST_HEADER = "drugListHeader";
    List<String> drugNames;
    String drugClassName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drug_class);
        ButterKnife.bind(this);

        String drugData = getIntent().getStringExtra(DRUG_LIST_EXTRA);
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<String>>() {}.getType();
        drugNames= gson.fromJson(drugData, type);

        drugClassName = getIntent().getStringExtra(DRUG_LIST_HEADER);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, drugNames);
        drugListView.setAdapter(adapter);
        drugListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String drugName = drugNames.get(i);
                Intent intent = new Intent();
                intent.putExtra(ResultsActivity.DRUG_INTENT, drugName);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.setTitle(drugClassName);
            actionBar.setDisplayShowHomeEnabled(false);
        }
    }
}
