package pro.sketchware.activities.preview;

import android.os.Bundle;

import com.besome.sketch.lib.base.BaseAppCompatActivity;

import pro.sketchware.R;
import pro.sketchware.databinding.ActivityPreviewBinding;

/**
 * Полноэкранный предпросмотр текущего layout'а проекта.
 * Пока что — заглушка: показывает пустой экран с toolbar.
 * Реальный превью будет добавлен следующим шагом.
 */
public class PreviewActivity extends BaseAppCompatActivity {

    public static final String EXTRA_SC_ID = "sc_id";
    public static final String EXTRA_LAYOUT_FILE = "layout_file";

    private ActivityPreviewBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        enableEdgeToEdgeNoContrast();
        super.onCreate(savedInstanceState);
        binding = ActivityPreviewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.preview_activity_title);
        }
        binding.toolbar.setNavigationOnClickListener(v -> finish());

        // TODO: реальное наполнение — следующим шагом.
    }
}
