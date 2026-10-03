package pro.sketchware.smartdrop;

import android.content.Context;
import android.text.InputType;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.besome.sketch.beans.ComponentBean;

import java.util.List;

import pro.sketchware.R;

/**
 * Универсальный диалог SmartDrop. Работает в трёх режимах в зависимости от количества компонентов:
 *
 *  0 компонентов  → CREATE_ONLY  (только создать, EditText с предзаполненным именем)
 *  1 компонент    → CONFIRM_ONE  (подключить к нему / создать новый)
 *  2+ компонентов → CHOOSE       (RadioGroup со списком + "Создать новый")
 *
 * Результат отдаётся через Callback.
 */
public class SmartDropDialog {

    public interface Callback {
        /** Подключиться к существующему компоненту. */
        void onAttachExisting(ComponentBean existing);

        /** Создать новый компонент с указанным именем. */
        void onCreateNew(String newName);

        /** Пользователь отменил (или закрыл) диалог. */
        void onCancelled();
    }

    private final Context context;
    private final int componentType;
    private final List<ComponentBean> existingComponents;
    private final String suggestedNewName;
    private final Callback callback;

    public SmartDropDialog(Context context,
                           int componentType,
                           List<ComponentBean> existingComponents,
                           String suggestedNewName,
                           Callback callback) {
        this.context = context;
        this.componentType = componentType;
        this.existingComponents = existingComponents;
        this.suggestedNewName = suggestedNewName;
        this.callback = callback;
    }

    public void show() {
        if (existingComponents == null || existingComponents.isEmpty()) {
            showCreateOnly();
        } else if (existingComponents.size() == 1) {
            showConfirmOne();
        } else {
            showChoose();
        }
    }

    // ─────────────────────────────────────────────────────────────
    // Режим 1: компонентов нет — только создание
    // ─────────────────────────────────────────────────────────────
    private void showCreateOnly() {
        String typeName = ComponentBean.getComponentName(context, componentType);
        String title = context.getString(R.string.smartdrop_dialog_create_title)
                .replace("%s", typeName);

        LinearLayout root = new LinearLayout(context);
        root.setOrientation(LinearLayout.VERTICAL);
        int pad = dp(20);
        root.setPadding(pad, dp(8), pad, dp(8));

        TextView hint = new TextView(context);
        hint.setText(R.string.smartdrop_new_name);
        root.addView(hint);

        EditText input = new EditText(context);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        input.setText(suggestedNewName);
        root.addView(input);

        AlertDialog dialog = new AlertDialog.Builder(context)
                .setTitle(title)
                .setView(root)
                .setPositiveButton(R.string.smartdrop_btn_create, (d, w) -> {
                    String name = input.getText().toString().trim();
                    if (name.isEmpty()) name = suggestedNewName;
                    callback.onCreateNew(name);
                })
                .setNegativeButton(R.string.cancel,
                        (d, w) -> callback.onCancelled())
                .setOnCancelListener(d -> callback.onCancelled())
                .create();

        dialog.show();
    }

    // ─────────────────────────────────────────────────────────────
    // Режим 2: один компонент — подключиться к нему или создать новый
    // ─────────────────────────────────────────────────────────────
    private void showConfirmOne() {
        ComponentBean existing = existingComponents.get(0);
        String typeName = ComponentBean.getComponentName(context, componentType);
        String title = context.getString(R.string.smartdrop_dialog_title)
                .replace("%s", typeName);
        String message = context.getString(R.string.smartdrop_connect_to)
                .replace("%s", existing.componentId);

        new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(R.string.smartdrop_btn_connect,
                        (d, w) -> callback.onAttachExisting(existing))
                .setNeutralButton(R.string.smartdrop_btn_new,
                        (d, w) -> showCreateOnly())
                .setNegativeButton(R.string.cancel,
                        (d, w) -> callback.onCancelled())
                .setOnCancelListener(d -> callback.onCancelled())
                .show();
    }

    // ─────────────────────────────────────────────────────────────
    // Режим 3: 2+ компонентов — RadioGroup со списком + создание
    // ─────────────────────────────────────────────────────────────
    private void showChoose() {
        String typeName = ComponentBean.getComponentName(context, componentType);
        String title = context.getString(R.string.smartdrop_dialog_title)
                .replace("%s", typeName);

        LinearLayout root = new LinearLayout(context);
        root.setOrientation(LinearLayout.VERTICAL);
        int pad = dp(20);
        root.setPadding(pad, dp(8), pad, dp(8));

        RadioGroup group = new RadioGroup(context);
        group.setOrientation(RadioGroup.VERTICAL);

        for (int i = 0; i < existingComponents.size(); i++) {
            ComponentBean c = existingComponents.get(i);
            RadioButton rb = new RadioButton(context);
            rb.setId(ViewGroup.generateViewId());
            rb.setText(c.componentId);
            rb.setTag(c);
            group.addView(rb);
            if (i == 0) rb.setChecked(true);
        }

        RadioButton rbNew = new RadioButton(context);
        rbNew.setId(ViewGroup.generateViewId());
        rbNew.setText(R.string.smartdrop_create_new);
        group.addView(rbNew);

        root.addView(group);

        TextView label = new TextView(context);
        label.setText(R.string.smartdrop_new_name);
        label.setPadding(0, dp(12), 0, dp(4));
        label.setVisibility(View.GONE);

        EditText input = new EditText(context);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        input.setText(suggestedNewName);
        input.setVisibility(View.GONE);

        root.addView(label);
        root.addView(input);

        group.setOnCheckedChangeListener((g, checkedId) -> {
            boolean isNew = checkedId == rbNew.getId();
            int vis = isNew ? View.VISIBLE : View.GONE;
            label.setVisibility(vis);
            input.setVisibility(vis);
            if (isNew) input.requestFocus();
        });

        new AlertDialog.Builder(context)
                .setTitle(title)
                .setView(root)
                .setPositiveButton(android.R.string.ok, (d, w) -> {
                    int checkedId = group.getCheckedRadioButtonId();
                    if (checkedId == rbNew.getId()) {
                        String name = input.getText().toString().trim();
                        if (name.isEmpty()) name = suggestedNewName;
                        callback.onCreateNew(name);
                    } else {
                        RadioButton rb = group.findViewById(checkedId);
                        if (rb != null && rb.getTag() instanceof ComponentBean cb) {
                            callback.onAttachExisting(cb);
                        } else {
                            callback.onCancelled();
                        }
                    }
                })
                .setNegativeButton(R.string.cancel,
                        (d, w) -> callback.onCancelled())
                .setOnCancelListener(d -> callback.onCancelled())
                .show();
    }

    private int dp(int v) {
        return Math.round(v * context.getResources().getDisplayMetrics().density);
    }
}
