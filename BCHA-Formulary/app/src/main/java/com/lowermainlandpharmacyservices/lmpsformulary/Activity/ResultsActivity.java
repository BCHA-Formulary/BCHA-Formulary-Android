package com.lowermainlandpharmacyservices.lmpsformulary.Activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lowermainlandpharmacyservices.lmpsformulary.Model.Refactored.DrugBase;
import com.lowermainlandpharmacyservices.lmpsformulary.Model.Refactored.FormularyDrug;
import com.lowermainlandpharmacyservices.lmpsformulary.Model.Refactored.NameType;
import com.lowermainlandpharmacyservices.lmpsformulary.Model.Refactored.Status;
import com.lowermainlandpharmacyservices.lmpsformulary.R;
import com.lowermainlandpharmacyservices.lmpsformulary.Utilities.SqlHelper;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.content.ContentValues.TAG;
import static com.lowermainlandpharmacyservices.lmpsformulary.Activity.DrugClassActivity.DRUG_LIST_EXTRA;

public class ResultsActivity extends Activity {

    public static final String DRUG_INTENT = "DRUG_EXTRA";
    public static final int SELECT_DRUG_REQUEST = 1;
    DrugBase resultDrug;
    private SqlHelper mSqlHelper;

    @BindView(R.id.altNameTitle) TextView altNamesTitle;
    @BindView(R.id.altNameList) TextView altNameList;
    @BindView(R.id.drugStatus) TextView drugStatus;
    @BindView(R.id.formularyStrengthsLayout) LinearLayout formularyStrengthsLayout;
    @BindView(R.id.formulary_strength) TextView formulary_strength;
    @BindView(R.id.nonFormularyLayout) LinearLayout nonFormularyLayout;
    @BindView(R.id.restriction_criteria) TextView restriction_criteria;
    @BindView(R.id.drugClassLayout) LinearLayout drugClassLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        ButterKnife.bind(this);

        mSqlHelper = new SqlHelper(this);
        String drugData = getIntent().getStringExtra(DRUG_INTENT);
        initializeDrugJson(drugData);
//        Gson gson = new Gson();
//        resultDrug = gson.fromJson(drugData, DrugBase.class);
//        if (resultDrug.status == Status.FORMULARY) {
//            FormularyDrug drug = gson.fromJson(drugData, FormularyDrug.class);
//            loadFormularyDrug(drug);
//        } else if (resultDrug.status == Status.EXCLUDED) {
//
//        } else {
//
//        }
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    private void loadFormularyDrug(FormularyDrug drug) {
        formularyStrengthsLayout.setVisibility(View.VISIBLE);
        nonFormularyLayout.setVisibility(View.GONE);

        if (resultDrug.nameType == NameType.GENERIC) {
            altNamesTitle.setText("Generic Names:");
        } else {
            altNamesTitle.setText("Brand Names:");
        }
        StringBuilder altNameString = new StringBuilder();
        for (String altNames : drug.alternateNames) {
            altNameString.append("- " + altNames + "\n");
        }
        altNameList.setText(altNameString.toString());

        switch (drug.status) {
            case FORMULARY:
                drugStatus.setText(Status.FORMULARY.name());
                drugStatus.setTextColor(Color.parseColor("#000000"));
                break;
            case EXCLUDED:
                drugStatus.setText(Status.EXCLUDED.name());
                drugStatus.setTextColor(Color.parseColor("#CC0000"));
                break;
            case RESTRICTED:
                drugStatus.setText(Status.RESTRICTED.name());
                drugStatus.setTextColor(Color.parseColor("#CC0000"));
                break;
        }

        StringBuilder strengthsString = new StringBuilder();
        for (String strength: drug.strengths) {
            strengthsString.append("- " + strength + "\n");
        }
        formulary_strength.setText(strengthsString.toString());

        for (final String drugClass : drug.drugClass) {
            TextView v = new TextView(this);
            SpannableString content = new SpannableString(drugClass);
            content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
            v.setText(content);
            LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            llp.setMargins(15, 0, 0, 20); //left, top, right, bottom
            v.setLayoutParams(llp);
            drugClassLayout.addView(v);
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, drugClass);
                    drugClassSearch(drugClass);
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SELECT_DRUG_REQUEST) {
            if (resultCode == RESULT_OK) {
                String drugResult = data.getStringExtra(DRUG_INTENT);
                if (!drugResult.isEmpty()) {
                    DrugBase drug = mSqlHelper.queryDrug(drugResult);
                    if (drug.status == Status.FORMULARY) {
                        loadFormularyDrug((FormularyDrug) drug);
                    }
                }
            }
        }
    }

    private void initializeDrugJson(String drugJson) {
        Gson gson = new Gson();
        resultDrug = gson.fromJson(drugJson, DrugBase.class);
        if (resultDrug.status == Status.FORMULARY) {
            FormularyDrug drug = gson.fromJson(drugJson, FormularyDrug.class);
            loadFormularyDrug(drug);
        } else if (resultDrug.status == Status.EXCLUDED) {

        } else {

        }
    }

    private void drugClassSearch(String drugClass) {
        List<String> drugList = mSqlHelper.getDrugNamesFromClass(drugClass);
        if (drugList.size() > 0) {
            Intent intent = new Intent(this, DrugClassActivity.class);
            Gson gson = new Gson();
            String drugString = gson.toJson(drugList);
            intent.putExtra(DRUG_LIST_EXTRA, drugString);
            startActivityForResult(intent, SELECT_DRUG_REQUEST);
        } else {
            //TODO no results - "No other drugs with that drug class"
        }
    }
}
