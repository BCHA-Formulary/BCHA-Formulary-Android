package com.lowermainlandpharmacyservices.lmpsformulary.Activity;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lowermainlandpharmacyservices.lmpsformulary.Model.Refactored.DrugBase;
import com.lowermainlandpharmacyservices.lmpsformulary.Model.Refactored.ExcludedDrug;
import com.lowermainlandpharmacyservices.lmpsformulary.Model.Refactored.FormularyDrug;
import com.lowermainlandpharmacyservices.lmpsformulary.Model.Refactored.NameType;
import com.lowermainlandpharmacyservices.lmpsformulary.Model.Refactored.RestrictedDrug;
import com.lowermainlandpharmacyservices.lmpsformulary.Model.Refactored.Status;
import com.lowermainlandpharmacyservices.lmpsformulary.R;
import com.lowermainlandpharmacyservices.lmpsformulary.Utilities.SqlHelper;

import java.util.ArrayList;
import java.util.Collections;
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
    List<TextView> mDrugClassTextViews = new ArrayList<>();

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
    }

    @Override
    public void onStart() {
        super.onStart();
        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.show();
            actionBar.setDisplayShowHomeEnabled(false);
        }
    }

    private void loadFormularyDrug(FormularyDrug drug) {
        formularyStrengthsLayout.setVisibility(View.VISIBLE);
        nonFormularyLayout.setVisibility(View.GONE);

        if (resultDrug.nameType == NameType.BRAND) {
            altNamesTitle.setText("Generic Names:");
        } else {
            altNamesTitle.setText("Brand Names:");
        }

        //try to sort the alt names
        try {
            drug.alternateNames.removeAll(Collections.singleton(null));
            Collections.sort(drug.alternateNames);
        } catch (Exception e) {
            Log.e(TAG, "Could not sort: " + e.getMessage());
        }
        StringBuilder altNameString = new StringBuilder();
        for (String altNames : drug.alternateNames) {
            if (altNames != null)
                altNameString.append("• " + altNames + "\n");
        }
        altNameList.setText(altNameString.toString());

        drugStatus.setText(Status.FORMULARY.name());
        drugStatus.setTextColor(Color.parseColor("#000000"));
        StringBuilder strengthsString = new StringBuilder();

        //try to sort the strengths
        try {
            drug.strengths.removeAll(Collections.singleton(null));
            Collections.sort(drug.strengths);
        } catch (Exception e) {
            Log.e(TAG, "Could not sort: " + e.getMessage());
        }

        for (String strength: drug.strengths) {
            if (strength == null)
                Log.d(TAG, "null string");
            else
                strengthsString.append("• " + strength + "\n");
        }
        formulary_strength.setText(strengthsString.toString());

        //try to sort the classes
        try {
            drug.drugClass.removeAll(Collections.singleton(null));
            Collections.sort(drug.drugClass);
        } catch (Exception e) {
            Log.e(TAG, "Could not sort: " + e.getMessage());
        }

        for (final String drugClass : drug.drugClass) {
            if (drugClass != null) {
                TextView v = new TextView(this);
                String drugClassText = "• " + drugClass;
                v.setMovementMethod(LinkMovementMethod.getInstance());
                v.setText(drugClassText, TextView.BufferType.SPANNABLE);
                Spannable mySpannable = (Spannable) v.getText();
                ClickableSpan clickableSpan = new ClickableSpan() {
                    @Override
                    public void onClick(View view) {
                        Log.d(TAG, drugClass);
                        drugClassSearch(drugClass);
                    }
                };
                mySpannable.setSpan(clickableSpan, 2, drugClassText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                drugClassLayout.addView(v);
                mDrugClassTextViews.add(v);
            }
        }
    }

    private void loadNonFormularyDrug(ExcludedDrug drug) {
        formularyStrengthsLayout.setVisibility(View.GONE);
        nonFormularyLayout.setVisibility(View.VISIBLE);

        if (resultDrug.nameType == NameType.BRAND) {
            altNamesTitle.setText("Generic Names:");
        } else {
            altNamesTitle.setText("Brand Names:");
        }

        //try to sort the alt names
        try {
            drug.alternateNames.removeAll(Collections.singleton(null));
            Collections.sort(drug.alternateNames);
        } catch (Exception e) {
            Log.e(TAG, "Could not sort: " + e.getMessage());
        }
        StringBuilder altNameString = new StringBuilder();
        for (String altNames : drug.alternateNames) {
            if (altNames == null)
                Log.d(TAG, "Null alt names");
            else
                altNameString.append("• " + altNames + "\n");
        }
        altNameList.setText(altNameString.toString());

        drugStatus.setText(Status.EXCLUDED.name());
        drugStatus.setTextColor(Color.parseColor("#CC0000"));

        restriction_criteria.setText(drug.criteria);

        //try to sort the drugClass
        try {
            drug.drugClass.removeAll(Collections.singleton(null));
            Collections.sort(drug.drugClass);
        } catch (Exception e) {
            Log.e(TAG, "Could not sort: " + e.getMessage());
        }
        for (final String drugClass : drug.drugClass) {
            if (drugClass != null) {
                TextView v = new TextView(this);
                String drugClassText = "• " + drugClass;
                v.setMovementMethod(LinkMovementMethod.getInstance());
                v.setText(drugClassText, TextView.BufferType.SPANNABLE);
                Spannable mySpannable = (Spannable) v.getText();
                ClickableSpan clickableSpan = new ClickableSpan() {
                    @Override
                    public void onClick(View view) {
                        Log.d(TAG, drugClass);
                        drugClassSearch(drugClass);
                    }
                };
                mySpannable.setSpan(clickableSpan, 2, drugClassText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                drugClassLayout.addView(v);
                mDrugClassTextViews.add(v);
            }
        }
    }

    private void loadNonFormularyDrug(RestrictedDrug drug) {
        formularyStrengthsLayout.setVisibility(View.GONE);
        nonFormularyLayout.setVisibility(View.VISIBLE);

        if (resultDrug.nameType == NameType.BRAND) {
            altNamesTitle.setText("Generic Names:");
        } else {
            altNamesTitle.setText("Brand Names:");
        }

        //try to sort the drugClass
        try {
            drug.alternateNames.removeAll(Collections.singleton(null));
            Collections.sort(drug.alternateNames);
        } catch (Exception e) {
            Log.e(TAG, "Could not sort: " + e.getMessage());
        }
        StringBuilder altNameString = new StringBuilder();
        for (String altNames : drug.alternateNames) {
            if (altNames == null)
                Log.d(TAG, "null alt name");
            else
                altNameString.append("• " + altNames + "\n");
        }
        altNameList.setText(altNameString.toString());

        drugStatus.setText(Status.RESTRICTED.name());
        drugStatus.setTextColor(Color.parseColor("#CC0000"));

        restriction_criteria.setText(drug.criteria);

        //try to sort the drugClass
        try {
            drug.drugClass.removeAll(Collections.singleton(null));
            Collections.sort(drug.drugClass);
        } catch (Exception e) {
            Log.e(TAG, "Could not sort: " + e.getMessage());
        }
        for (final String drugClass : drug.drugClass) {
            if (drugClass != null) {
                TextView v = new TextView(this);
                String drugClassText = "• " + drugClass;
                v.setMovementMethod(LinkMovementMethod.getInstance());
                v.setText(drugClassText, TextView.BufferType.SPANNABLE);
                Spannable mySpannable = (Spannable) v.getText();
                ClickableSpan clickableSpan = new ClickableSpan() {
                    @Override
                    public void onClick(View view) {
                        Log.d(TAG, drugClass);
                        drugClassSearch(drugClass);
                    }
                };
                mySpannable.setSpan(clickableSpan, 2, drugClassText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                drugClassLayout.addView(v);
                mDrugClassTextViews.add(v);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SELECT_DRUG_REQUEST) {
            if (resultCode == RESULT_OK) {
                String drugResult = data.getStringExtra(DRUG_INTENT);
                if (!drugResult.isEmpty()) {
                    resultDrug = mSqlHelper.queryDrug(drugResult);
                    getActionBar().setTitle(resultDrug.primaryName);

                    if (mDrugClassTextViews != null && mDrugClassTextViews.size() > 0) {
                        for (TextView v : mDrugClassTextViews) {
                            drugClassLayout.removeView(v);
                        }
                        mDrugClassTextViews.clear();
                    }
                    
                    if (resultDrug.status == Status.FORMULARY) {
                        loadFormularyDrug((FormularyDrug) resultDrug);
                    } else if (resultDrug.status == Status.EXCLUDED) {
                        loadNonFormularyDrug((ExcludedDrug) resultDrug);
                    } else {
                        loadNonFormularyDrug((RestrictedDrug) resultDrug);
                    }
                }
            }
        }
    }

    private void initializeDrugJson(String drugJson) {
        Gson gson = new Gson();
        resultDrug = gson.fromJson(drugJson, DrugBase.class);
        getActionBar().setTitle(resultDrug.primaryName);

        if (mDrugClassTextViews != null && mDrugClassTextViews.size() > 0) {
            for (TextView v : mDrugClassTextViews) {
                drugClassLayout.removeView(v);
            }
            mDrugClassTextViews.clear();
        }
        if (resultDrug.status == Status.FORMULARY) {
            FormularyDrug drug = gson.fromJson(drugJson, FormularyDrug.class);
            loadFormularyDrug(drug);
        } else if (resultDrug.status == Status.EXCLUDED) {
            ExcludedDrug drug = gson.fromJson(drugJson, ExcludedDrug.class);
            loadNonFormularyDrug(drug);
        } else {
            RestrictedDrug drug = gson.fromJson(drugJson, RestrictedDrug.class);
            loadNonFormularyDrug(drug);
        }
    }

    private void drugClassSearch(String drugClass) {
        List<String> drugList = mSqlHelper.getDrugNamesFromClass(drugClass);
        if (drugList.size() > 0) {
            Intent intent = new Intent(this, DrugClassActivity.class);
            Gson gson = new Gson();
            String drugString = gson.toJson(drugList);
            intent.putExtra(DRUG_LIST_EXTRA, drugString);
            intent.putExtra(DrugClassActivity.DRUG_LIST_HEADER, drugClass);
            startActivityForResult(intent, SELECT_DRUG_REQUEST);
        } else {
            //TODO no results - "No other drugs with that drug class"
        }
    }
}
