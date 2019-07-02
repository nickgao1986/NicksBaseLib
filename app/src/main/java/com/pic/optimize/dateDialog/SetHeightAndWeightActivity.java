package com.pic.optimize.dateDialog;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.fourmob.datetimepicker.date.NumberPicker;
import com.fourmob.datetimepicker.date.TextPickerDialogUtil;
import com.pic.optimize.R;

import java.util.Locale;

public class SetHeightAndWeightActivity extends Activity {

    private TextView set_height;
    private TextView set_weight;
    private int mHeight = 175;
    private int mWeight = 70;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pedo_height_and_weight_layout);
        TextView title = (TextView) findViewById(R.id.height);
        title.setTypeface(Typeface.createFromAsset(this.getAssets(), "fonts/PingFang-SC-Semibold.ttf"));
//
        TextView title1 = (TextView) findViewById(R.id.weight);
        title1.setTypeface(Typeface.createFromAsset(this.getAssets(), "fonts/PingFang-SC-Semibold.ttf"));

        set_height = (TextView)findViewById(R.id.set_height);
        set_weight = (TextView)findViewById(R.id.set_weight);
        set_height.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSetHeightDialog();
            }
        });
        set_weight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSetWeightDialog();
            }
        });
//
//        TextView gender1 = (TextView)findViewById(R.id.gender1);
//        gender1.setSelected(true);
    }

    private void showSetHeightDialog() {
        TextPickerDialogUtil heightPickDialog = new TextPickerDialogUtil(this);
        heightPickDialog.addItems(getItem(216, "", "cm", 57));
        heightPickDialog.setListener(new TextPickerDialogUtil.OnSelectChangeListener() {
            @Override
            public void onDateChange(Integer[] value, NumberPicker changePicker) {
            }

            @Override
            public void onConfirm(Integer[] value) {
                mHeight = value[0] + 57;
                set_height.setText(String.format(Locale.getDefault(), "%dcm", mHeight));
            }
        });

        heightPickDialog.pickers.get(0).setValue(mHeight - 57);
        heightPickDialog.show(getString(R.string.set_height));
    }

    private void showSetWeightDialog() {
        TextPickerDialogUtil weightPickDialog = new TextPickerDialogUtil(this);
        weightPickDialog.addItems(getItem(146, "", " kg", 5));
        weightPickDialog.setListener(new TextPickerDialogUtil.OnSelectChangeListener() {
            @Override
            public void onDateChange(Integer[] value, NumberPicker changePicker) {
            }

            @Override
            public void onConfirm(Integer[] value) {
                mWeight = value[0] + 5;
                set_weight.setText(String.format(Locale.getDefault(), "%dkg", mWeight));
            }
        });

        weightPickDialog.pickers.get(0).setValue(mWeight - 5);
        weightPickDialog.show(getString(R.string.set_weight));
    }

    private String[] getItem(int length, String startTag, String endTag, int start) {
        String[] item = new String[length];
        for (int i = start; i < length + start; i++) {
            item[i - start] = startTag + i + endTag;
        }
        return item;
    }
}
