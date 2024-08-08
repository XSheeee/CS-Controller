package io.github.xsheeee.cs_controller;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.List;

import io.github.xsheeee.cs_controller.Tools.Values;

public class AppConfigActivity extends AppCompatActivity {
    private ArrayAdapter<CharSequence> adapter;
    private Spinner spinner;
    private int ii;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_config);

        // 初始化 backButton
        TextView back = findViewById(R.id.backButton2);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // 返回上一个活动
            }
        });

        // 初始化 Spinner
        spinner = findViewById(R.id.spinner);

        Intent intent = getIntent();
        String aName = intent.getStringExtra("aName");
        String pName = intent.getStringExtra("pName");

        // 创建 ArrayAdapter
        adapter = ArrayAdapter.createFromResource(
                this,
                R.array.modes,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // 设置 Adapter 到 Spinner
        spinner.setAdapter(adapter);
        Values.updateLists();

        // 默认显示
        for (int i = 0; i < Values.lists.size(); i++) { // 使用 '<' 而不是 '<='
            if (Values.lists.get(i).contains(pName)) {
                ii = i;
                break;
            }
        }

        // 设置默认的 Spinner 项
        setDefaultSpinnerItem(ii);

        // 设置 Spinner 的 OnItemSelectedListener
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // 如果已经在当前列表中，则直接返回
                if (Values.lists.get(position).contains(pName)) {
                    return;
                }

                // 从旧列表移除并添加到新列表
                for (int i = 0; i < Values.lists.size(); i++) {
                    if (Values.lists.get(i).contains(pName)) {
                        Values.lists.get(i).remove(pName);
                        break;
                    }
                }

                Values.lists.get(position).add(pName);
                Values.toUpdateLists();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // 什么都不做
            }
        });
    }

    private void setDefaultSpinnerItem(int defaultItem) {
        spinner.setSelection(defaultItem); // 设置默认的 Spinner 项
    }
}