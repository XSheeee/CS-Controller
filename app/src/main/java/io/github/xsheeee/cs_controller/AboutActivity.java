package io.github.xsheeee.cs_controller;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.widget.TextView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ClickableSpan;
import android.view.View;
import android.text.method.LinkMovementMethod;

public class AboutActivity extends AppCompatActivity {

    private static final int[] TEXT_VIEW_IDS = {
        R.id.view_source_code,
        R.id.xshe_github,
        R.id.cs_source_code,
        R.id.mowei_github
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        // 初始化 Toolbar
        Toolbar toolbar = findViewById(R.id.backButton3);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.outline_arrow_back_24);
        toolbar.setNavigationOnClickListener(v -> finish());

        setupClickableLinks();
    }

    private void setupClickableLinks() {
        for (int i = 0; i < TEXT_VIEW_IDS.length; i++) {
            TextView textView = findViewById(TEXT_VIEW_IDS[i]);
            SpannableString spannableString = new SpannableString(getTextForIndex(i));

            String clickableText = getClickableTextForIndex(i);
            String url = getUrlForIndex(i);

            if (clickableText != null && url != null) {
                addClickablePart(spannableString, clickableText, url);
            }

            textView.setText(spannableString);
            textView.setMovementMethod(LinkMovementMethod.getInstance());
        }
    }

    private String getTextForIndex(int index) {
        switch (index) {
            case 0: return getString(R.string.view_source_code);
            case 1: return getString(R.string.xshe_github);
            case 2: return getString(R.string.cs_source_code);
            case 3: return getString(R.string.mowei_github);
            default: return "";
        }
    }

    private String getClickableTextForIndex(int index) {
        switch (index) {
            case 0: return "GitHub";
            case 1: return "GitHub";
            case 2: return "GitHub";
            case 3: return "GitHub";
            default: return null;
        }
    }

    private String getUrlForIndex(int index) {
        switch (index) {
            case 0: return getString(R.string.url_xshe_source);
            case 1: return getString(R.string.url_xshe_github);
            case 2: return getString(R.string.url_mowei_source);
            case 3: return getString(R.string.url_mowei_github);
            default: return null;
        }
    }

    // 添加点击链接
    private void addClickablePart(SpannableString spannableString, String clickableText, String url) {
        int start = spannableString.toString().indexOf(clickableText);
        int end = start + clickableText.length();

        if (start >= 0) {
            ClickableSpan clickableSpan = new ClickableSpan() {
                @Override
                public void onClick(View widget) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(url));
                    startActivity(intent);
                }
            };
            spannableString.setSpan(clickableSpan, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
    }
}