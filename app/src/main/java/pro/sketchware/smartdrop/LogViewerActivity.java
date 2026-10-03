package pro.sketchware.smartdrop;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import androidx.core.content.FileProvider;

import com.besome.sketch.lib.base.BaseAppCompatActivity;
import com.google.android.material.tabs.TabLayout;

import java.io.File;

import pro.sketchware.R;
import pro.sketchware.databinding.ActivityLogViewerBinding;

/**
 * Экран просмотра журнала отладки.
 * Вкладки RU/EN, кнопки: Обновить, Копировать, Поделиться, Очистить.
 * Автообновление каждые 2 секунды.
 */
public class LogViewerActivity extends BaseAppCompatActivity {

    private ActivityLogViewerBinding binding;
    private boolean showRussian = true;
    private final Handler handler = new Handler(Looper.getMainLooper());
    private final Runnable autoRefresh = new Runnable() {
        @Override
        public void run() {
            loadLog();
            handler.postDelayed(this, 2000);
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLogViewerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        binding.toolbar.setNavigationOnClickListener(v -> finish());

        binding.tabs.addTab(binding.tabs.newTab().setText(R.string.log_viewer_tab_ru));
        binding.tabs.addTab(binding.tabs.newTab().setText(R.string.log_viewer_tab_en));
        binding.tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                showRussian = tab.getPosition() == 0;
                loadLog();
            }

            @Override public void onTabUnselected(TabLayout.Tab tab) {}
            @Override public void onTabReselected(TabLayout.Tab tab) {}
        });

        binding.btnRefresh.setOnClickListener(v -> loadLog());

        binding.btnCopy.setOnClickListener(v -> {
            String text = binding.logText.getText().toString();
            ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            if (cm != null) {
                cm.setPrimaryClip(ClipData.newPlainText("log", text));
                Toast.makeText(this, R.string.log_viewer_copied, Toast.LENGTH_SHORT).show();
            }
        });

        binding.btnShare.setOnClickListener(v -> {
            File f = DebugLogger.get(this).getOpenLogFile(showRussian);
            if (!f.exists()) {
                f = DebugLogger.get(this).getHiddenLogFile(showRussian);
            }
            if (f == null || !f.exists()) {
                Toast.makeText(this, R.string.log_viewer_empty, Toast.LENGTH_SHORT).show();
                return;
            }
            try {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_STREAM, FileProvider.getUriForFile(
                        this, getPackageName() + ".provider", f));
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                startActivity(Intent.createChooser(intent, getString(R.string.log_viewer_btn_share)));
            } catch (Exception e) {
                Toast.makeText(this, "Ошибка: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        binding.btnClear.setOnClickListener(v -> {
            DebugLogger.get(this).clearLogs();
            Toast.makeText(this, R.string.log_viewer_cleared, Toast.LENGTH_SHORT).show();
            handler.postDelayed(this::loadLog, 300);
        });

        loadLog();
    }

    private void loadLog() {
        String text = DebugLogger.get(this).readLog(showRussian);
        if (text.isEmpty()) {
            binding.logText.setText(R.string.log_viewer_empty);
        } else {
            binding.logText.setText(text);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        handler.post(autoRefresh);
    }

    @Override
    public void onPause() {
        super.onPause();
        handler.removeCallbacks(autoRefresh);
    }
}
