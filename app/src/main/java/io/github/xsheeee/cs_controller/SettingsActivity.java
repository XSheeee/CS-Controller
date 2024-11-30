package io.github.xsheeee.cs_controller;

import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.materialswitch.MaterialSwitch;
import io.github.xsheeee.cs_controller.Tools.Tools;
import java.util.HashMap;
import java.util.Map;
import androidx.appcompat.widget.Toolbar;
import io.github.xsheeee.cs_controller.Tools.Values;

public class SettingsActivity extends AppCompatActivity {
    private Tools tools;
    private Map<String, MaterialSwitch> switchMap = new HashMap<>();
    private static final String CONFIG_FILE_PATH = Values.csSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Toolbar toolbar = findViewById(R.id.backButton5);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.outline_arrow_back_24);
        toolbar.setNavigationOnClickListener(v -> finish());

        tools = new Tools(this);

        initSwitches();
        loadConfig();
        setupSwitchListeners();
        removeLeadingSpacesInFile();
    }

    private void initSwitches() {
        switchMap.put("Enable_Feas", findViewById(R.id.SwitchEnableFeas));
        switchMap.put("Disable_qcom_GpuBoost", findViewById(R.id.SwitchDisableQcomGpuBoost));
        switchMap.put("Core_allocation", findViewById(R.id.SwitchCoreAllocation));
        switchMap.put("Load_balancing", findViewById(R.id.SwitchLoadBalancing));
        switchMap.put("Disable_UFS_clock_gate", findViewById(R.id.SwitchDisableUfsClockGate));
        switchMap.put("TouchBoost", findViewById(R.id.SwitchTouchBoost));
        switchMap.put("CFS_Scheduler", findViewById(R.id.SwitchCfsScheduler));
        switchMap.put("Dynamic_Response", findViewById(R.id.SwitchDynamicResponse));
        switchMap.put("Adj_CpuIdle", findViewById(R.id.SwitchAdjCpuIdle));
        switchMap.put("New_Uclamp_Strategy", findViewById(R.id.SwitchNewUclampStrategy));
        switchMap.put("Disable_Detailed_Log", findViewById(R.id.SwitchDisableDetailedLog));
    }

    private void loadConfig() {
        String configContent = tools.readFileWithShell(CONFIG_FILE_PATH);
        if (configContent != null) {
            Map<String, Boolean> configMap = parseConfigContent(configContent);
            for (String key : switchMap.keySet()) {
                MaterialSwitch materialSwitch = switchMap.get(key);
                if (configMap.containsKey(key)) {
                    materialSwitch.setChecked(configMap.get(key));
                    materialSwitch.setEnabled(true);
                    materialSwitch.setAlpha(1.0f);
                } else {
                    materialSwitch.setEnabled(false);
                    materialSwitch.setAlpha(0.5f);
                }
            }
        } else {
            showToast(getString(R.string.read_mode_error));
        }
    }

    private Map<String, Boolean> parseConfigContent(String content) {
        Map<String, Boolean> configMap = new HashMap<>();
        String[] lines = content.split("\n");
        for (String line : lines) {
            line = line.trim();
            if (line.contains("=")) {
                String[] parts = line.split("=");
                String key = parts[0].trim();
                boolean value = parts[1].trim().equalsIgnoreCase("true");
                configMap.put(key, value);
            }
        }
        return configMap;
    }

    private void setupSwitchListeners() {
        for (String key : switchMap.keySet()) {
            MaterialSwitch materialSwitch = switchMap.get(key);
            materialSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
                String newValue = isChecked ? "true" : "false";
                tools.updateConfigEntry(CONFIG_FILE_PATH, key, newValue);
            });
        }
    }

    private void removeLeadingSpacesInFile() {
        String configContent = tools.readFileWithShell(CONFIG_FILE_PATH);
        if (configContent != null) {
            StringBuilder cleanedContent = new StringBuilder();
            String[] lines = configContent.split("\n");
            
            for (String line : lines) {
                String cleanedLine = line.replaceAll("^\\s+", "");
                cleanedContent.append(cleanedLine).append("\n");
            }
            
            tools.writeToFile(CONFIG_FILE_PATH, cleanedContent.toString());
        } else {
            showToast(getString(R.string.read_mode_error));
        }
    }

    private void showToast(String message) {
        Toast.makeText(SettingsActivity.this, message, Toast.LENGTH_SHORT).show();
    }
}