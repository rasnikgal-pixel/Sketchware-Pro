package pro.sketchware.activities.usercode;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.besome.sketch.lib.base.BaseAppCompatActivity;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import mod.hey.studios.code.SrcCodeEditor;
import pro.sketchware.R;
import pro.sketchware.activities.soraeditor.SoraEditorActivity;
import pro.sketchware.utility.SketchwareUtil;

/**
 * Экран "Мой код" — управление пользовательскими Java-классами.
 */
public class UserCodeActivity extends BaseAppCompatActivity {

    private RecyclerView list;
    private TextView emptyText;
    private UserAdapter adapter;
    private UserCodeManager manager;
    private String scId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        enableEdgeToEdgeNoContrast();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_code);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.user_code_title);
            getSupportActionBar().setSubtitle(R.string.user_code_subtitle);
        }

        scId = getIntent().getStringExtra("sc_id");
        if (scId == null) {
            finish();
            return;
        }

        manager = new UserCodeManager(scId);

        list = findViewById(R.id.user_code_list);
        emptyText = findViewById(R.id.user_code_empty);
        list.setLayoutManager(new LinearLayoutManager(this));

        adapter = new UserAdapter();
        list.setAdapter(adapter);

        FloatingActionButton fab = findViewById(R.id.fab_create);
        fab.setOnClickListener(v -> showCreateDialog());

        refresh();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(Menu.NONE, 1, Menu.NONE, R.string.user_code_show_path)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        if (item.getItemId() == 1) {
            String path = manager.getUserDir();
            new MaterialAlertDialogBuilder(this)
                    .setTitle(R.string.user_code_show_path)
                    .setMessage(path == null ? "N/A" : path)
                    .setPositiveButton(android.R.string.ok, null)
                    .show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void refresh() {
        List<File> files = manager.listFiles();
        adapter.setFiles(files);
        emptyText.setVisibility(files.isEmpty() ? View.VISIBLE : View.GONE);
        list.setVisibility(files.isEmpty() ? View.GONE : View.VISIBLE);
    }

    private void showCreateDialog() {
        View v = getLayoutInflater().inflate(R.layout.dialog_user_code_create, null);
        android.widget.EditText etName = v.findViewById(R.id.et_class_name);
        android.widget.RadioGroup rg = v.findViewById(R.id.rg_template);
        rg.check(R.id.rb_empty_class);

        new MaterialAlertDialogBuilder(this)
                .setTitle(R.string.user_code_create)
                .setView(v)
                .setPositiveButton(android.R.string.ok, (d, w) -> {
                    String name = UserCodeManager.capitalize(etName.getText().toString().trim());
                    if (!UserCodeManager.isValidClassName(name)) {
                        SketchwareUtil.toast(getString(R.string.user_code_invalid_name));
                        return;
                    }
                    if (manager.fileExists(name)) {
                        SketchwareUtil.toast(getString(R.string.user_code_already_exists));
                        return;
                    }
                    int template = 0;
                    int checked = rg.getCheckedRadioButtonId();
                    if (checked == R.id.rb_utils) template = 1;
                    else if (checked == R.id.rb_custom_view) template = 2;
                    else if (checked == R.id.rb_interface) template = 3;

                    if (manager.createFile(name, template)) {
                        SketchwareUtil.toast(getString(R.string.user_code_created));
                        refresh();
                        File created = new File(manager.getUserDir(), name + ".java");
                        if (created.exists()) {
                            openInSrcEditor(created);
                        }
                    }
                })
                .setNegativeButton(R.string.common_word_cancel, null)
                .show();
    }

    private void showFileMenu(File file) {
        String className = file.getName().replace(".java", "");
        new MaterialAlertDialogBuilder(this)
                .setTitle(className)
                .setItems(new String[]{
                        getString(R.string.user_code_open_sora),
                        getString(R.string.user_code_open_src),
                        getString(R.string.user_code_rename),
                        getString(R.string.user_code_delete)
                }, (d, which) -> {
                    switch (which) {
                        case 0 -> openInSora(file);
                        case 1 -> openInSrcEditor(file);
                        case 2 -> showRenameDialog(className);
                        case 3 -> showDeleteDialog(className);
                    }
                })
                .setNegativeButton(R.string.common_word_cancel, null)
                .show();
    }

    private void openInSora(File file) {
        // Открыть SoraEditorActivity — а она должна открыть конкретный файл.
        // Пока — через SrcCodeEditor.
        openInSrcEditor(file);
    }

    private void openInSrcEditor(File file) {
        Intent intent = new Intent(this, SrcCodeEditor.class);
        intent.putExtra("title", file.getName());
        intent.putExtra("sc_id", scId);
        intent.putExtra("content", file.getAbsolutePath());
        startActivity(intent);
    }

    private void showRenameDialog(String oldName) {
        View v = getLayoutInflater().inflate(R.layout.dialog_user_code_rename, null);
        android.widget.EditText et = v.findViewById(R.id.et_new_name);
        et.setText(oldName);
        et.setSelection(oldName.length());

        new MaterialAlertDialogBuilder(this)
                .setTitle(R.string.user_code_rename)
                .setView(v)
                .setPositiveButton(android.R.string.ok, (d, w) -> {
                    String newName = UserCodeManager.capitalize(et.getText().toString().trim());
                    if (!UserCodeManager.isValidClassName(newName)) {
                        SketchwareUtil.toast(getString(R.string.user_code_invalid_name));
                        return;
                    }
                    if (manager.fileExists(newName)) {
                        SketchwareUtil.toast(getString(R.string.user_code_already_exists));
                        return;
                    }
                    if (manager.renameFile(oldName, newName)) {
                        refresh();
                    }
                })
                .setNegativeButton(R.string.common_word_cancel, null)
                .show();
    }

    private void showDeleteDialog(String className) {
        new MaterialAlertDialogBuilder(this)
                .setTitle(R.string.user_code_delete)
                .setMessage(R.string.user_code_delete_confirm)
                .setPositiveButton(R.string.user_code_delete, (d, w) -> {
                    if (manager.deleteFile(className)) {
                        refresh();
                    }
                })
                .setNegativeButton(R.string.common_word_cancel, null)
                .show();
    }

    private class UserAdapter extends RecyclerView.Adapter<UserAdapter.VH> {
        private final List<File> files = new ArrayList<>();

        void setFiles(List<File> newFiles) {
            files.clear();
            files.addAll(newFiles);
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_user_code, parent, false);
            return new VH(v);
        }

        @Override
        public void onBindViewHolder(@NonNull VH holder, int position) {
            File f = files.get(position);
            holder.name.setText(f.getName());
            holder.itemView.setOnClickListener(v -> openInSrcEditor(f));
            holder.itemView.setOnLongClickListener(v -> {
                showFileMenu(f);
                return true;
            });
        }

        @Override
        public int getItemCount() {
            return files.size();
        }

        class VH extends RecyclerView.ViewHolder {
            final TextView name;
            VH(View v) {
                super(v);
                name = v.findViewById(R.id.tv_file_name);
            }
        }
    }
}
