package io.github.xsheeee.cs_controller;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import io.github.xsheeee.cs_controller.Tools.Tools;
import io.github.xsheeee.cs_controller.Tools.Values;

public class MainActivity extends AppCompatActivity {
    private Tools tools;
    private TextView configTextView;
    private AutoCompleteTextView menu;
    private TextView versionTextView;
    private TextView processStatusTextView; 
    private MaterialCardView processStatusCard;
    private MaterialCardView rootWarningCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tools = new Tools(getApplicationContext());

        configTextView = findViewById(R.id.config_text_view);
        versionTextView = findViewById(R.id.version_text_view);
        processStatusTextView = findViewById(R.id.process_status_text_view);
        processStatusCard = findViewById(R.id.process_status_card);
        rootWarningCard = findViewById(R.id.root_warning_card);

        TextInputLayout textInputLayout = findViewById(R.id.menu);
        menu = (AutoCompleteTextView) textInputLayout.getEditText();

        setupDropdownMenu();
        updateConfigTextView();
        updateVersionTextView();
        updateProcessStatusTextView();
        checkRootStatus();

        AppCompatImageView logo = findViewById(R.id.main_logo);
        logo.setOnClickListener(v -> {
            Intent intent = new Intent(this, AboutActivity.class);
            startActivity(intent);
        });

        MaterialCardView appListConfigButton = findViewById(R.id.go_app_list);
        appListConfigButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, AppListActivity.class);
            startActivity(intent);
        });

        MaterialCardView goLogButton = findViewById(R.id.go_log);
        goLogButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, LogActivity.class);
            startActivity(intent);
        });

        MaterialCardView goSettingsButton = findViewById(R.id.go_settings);
        goSettingsButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
        });
    }

    private void setupDropdownMenu() {
        String[] items = getResources().getStringArray(R.array.select_mode);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, items);
        menu.setAdapter(adapter);

        menu.setOnItemClickListener((parent, view, position, id) -> {
            int mode = position + 1;
            changeModeInMainActivity(mode);
        });
    }

    private void changeModeInMainActivity(int mode) {
        tools.changeMode(mode);
        updateConfigTextView();
    }

    private void updateConfigTextView() {
        String fileContent = tools.readFileWithShell(Values.CSConfigPath);
        if (fileContent != null) {
            String modeString = getString(R.string.now_mode);
            configTextView.setText(modeString + fileContent);
        } else {
            configTextView.setText(R.string.read_mode_error);
        }
    }

    private void updateVersionTextView() {
        String version = tools.getVersionFromModuleProp();
        if (version != null) {
            String versionString = getString(R.string.cs_version);
            versionTextView.setText(versionString + version);
        } else {
            versionTextView.setText(R.string.read_version_error);
        }
    }

    private void updateProcessStatusTextView() {
        String statusPrefix = getString(R.string.cs_work);
        if (tools.isProcessRunning(Values.csProcess)) {
            processStatusTextView.setText(statusPrefix + getString(R.string.cs_work_true));
        } else {
            processStatusTextView.setText(statusPrefix + getString(R.string.cs_work_false));
            TypedValue typedValue = new TypedValue();
            getTheme().resolveAttribute(R.attr.colorErrorContainer, typedValue, true);
            int color = typedValue.data;
            processStatusCard.setCardBackgroundColor(color);
        }
        processStatusTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
    }

    private void checkRootStatus() {
        if (tools.getSU()) {
            rootWarningCard.setVisibility(MaterialCardView.GONE);
        } else {
            rootWarningCard.setVisibility(MaterialCardView.VISIBLE);
        }
    }
}