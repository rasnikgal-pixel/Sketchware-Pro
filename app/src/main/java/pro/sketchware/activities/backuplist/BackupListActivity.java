package pro.sketchware.activities.backuplist;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.besome.sketch.lib.base.BaseAppCompatActivity;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import mod.hey.studios.project.backup.BackupFactory;
import pro.sketchware.R;
import pro.sketchware.utility.FileUtil;

/**
 * Список всех .swb-резервных копий в папке BackupFactory.getBackupDir().
 * Поддерживает: Восстановить, Поделиться, Переименовать, Удалить.
 */
public class BackupListActivity extends BaseAppCompatActivity {

    private RecyclerView list;
    private View emptyView;
    private Adapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        enableEdgeToEdgeNoContrast();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_backup_list);

        Toolbar tb = findViewById(R.id.toolbar);
        setSupportActionBar(tb);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.backup_list_title);
        }
        tb.setNavigationOnClickListener(v -> finish());

        list = findViewById(R.id.backup_list);
        emptyView = findViewById(R.id.empty_view);
        list.setLayoutManager(new LinearLayoutManager(this));
        adapter = new Adapter();
        list.setAdapter(adapter);

        loadBackups();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(Menu.NONE, 1, Menu.NONE, R.string.backup_list_refresh)
                .setIcon(R.drawable.ic_mtrl_refresh)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        menu.add(Menu.NONE, 2, Menu.NONE, R.string.backup_list_open_folder)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == 1) { loadBackups(); return true; }
        if (id == 2) { openFolder(); return true; }
        return super.onOptionsItemSelected(item);
    }

    /** Обходит .sketchware/backups рекурсивно и собирает все .swb. */
    private void loadBackups() {
        List<File> files = new ArrayList<>();
        File root = new File(BackupFactory.getBackupDir());
        collectSwb(root, files);
        // свежие сверху
        Collections.sort(files, (a, b) -> Long.compare(b.lastModified(), a.lastModified()));

        adapter.setItems(files);
        boolean isEmpty = files.isEmpty();
        emptyView.setVisibility(isEmpty ? View.VISIBLE : View.GONE);
        list.setVisibility(isEmpty ? View.GONE : View.VISIBLE);
    }

    private void collectSwb(File dir, List<File> out) {
        if (dir == null || !dir.exists()) return;
        File[] children = dir.listFiles();
        if (children == null) return;
        for (File f : children) {
            if (f.isDirectory()) {
                collectSwb(f, out);
            } else if (f.getName().toLowerCase(Locale.ROOT).endsWith("." + BackupFactory.EXTENSION)) {
                out.add(f);
            }
        }
    }

    private void openFolder() {
        try {
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setDataAndType(Uri.parse("file://" + BackupFactory.getBackupDir()), "resource/folder");
            startActivity(i);
        } catch (Throwable t) {
            Toast.makeText(this, BackupFactory.getBackupDir(), Toast.LENGTH_LONG).show();
        }
    }

    private void showActions(final File file) {
        String[] actions = new String[]{
                getString(R.string.backup_list_action_restore),
                getString(R.string.backup_list_action_share),
                getString(R.string.backup_list_action_rename),
                getString(R.string.backup_list_action_delete)
        };
        new MaterialAlertDialogBuilder(this)
                .setTitle(file.getName())
                .setItems(actions, (d, which) -> {
                    switch (which) {
                        case 0: confirmRestore(file); break;
                        case 1: share(file); break;
                        case 2: showRenameDialog(file); break;
                        case 3: confirmDelete(file); break;
                    }
                })
                .setNegativeButton(R.string.common_word_cancel, null)
                .show();
    }

    private void confirmRestore(final File file) {
        new MaterialAlertDialogBuilder(this)
                .setTitle(R.string.backup_list_action_restore)
                .setMessage(R.string.backup_list_restore_confirm)
                .setPositiveButton(R.string.backup_list_action_restore, (d, w) -> doRestore(file))
                .setNegativeButton(R.string.common_word_cancel, null)
                .show();
    }

    private void doRestore(final File file) {
        final AlertDialog progress = new MaterialAlertDialogBuilder(this)
                .setMessage(R.string.backup_list_restore_in_progress)
                .setCancelable(false)
                .create();
        progress.show();

        new Thread(() -> {
            String errorMsg = null;
            try {
                BackupFactory bf = new BackupFactory(a.a.a.lC.b());
                bf.setBackupLocalLibs(false);
                bf.restore(file);
                if (!bf.isRestoreSuccess()) errorMsg = bf.getError();
            } catch (Throwable t) {
                errorMsg = String.valueOf(t.getMessage());
            }
            final String err = errorMsg;
            runOnUiThread(() -> {
                try { progress.dismiss(); } catch (Throwable ignored) {}
                if (err == null) {
                    Toast.makeText(this, R.string.backup_list_restore_success, Toast.LENGTH_SHORT).show();
                    // Закрыть все активити над MainActivity и вернуться с обновлением списка
                    try {
                        android.content.Intent i = new android.content.Intent(this,
                                pro.sketchware.activities.main.activities.MainActivity.class);
                        i.setFlags(android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP
                                | android.content.Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        i.putExtra("refresh_projects", true);
                        startActivity(i);
                    } catch (Throwable ignored) {}
                    finish();
                } else {
                    Toast.makeText(this, getString(R.string.backup_list_restore_error, err), Toast.LENGTH_LONG).show();
                }
            });
        }, "backup-restore-thread").start();
    }

    private void share(File file) {
        try {
            Uri uri = FileProvider.getUriForFile(this,
                    getPackageName() + ".provider", file);
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("application/octet-stream");
            i.putExtra(Intent.EXTRA_STREAM, uri);
            i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivity(Intent.createChooser(i, getString(R.string.backup_list_action_share)));
        } catch (Throwable t) {
            Toast.makeText(this, String.valueOf(t.getMessage()), Toast.LENGTH_LONG).show();
        }
    }

    private void showRenameDialog(final File file) {
        final android.widget.EditText input = new android.widget.EditText(this);
        String current = file.getName();
        if (current.endsWith(".swb")) current = current.substring(0, current.length() - 4);
        input.setText(current);
        input.setSelection(current.length());
        int pad = (int) (16 * getResources().getDisplayMetrics().density);
        android.widget.FrameLayout wrap = new android.widget.FrameLayout(this);
        wrap.setPadding(pad, pad / 2, pad, 0);
        wrap.addView(input, new android.widget.FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));

        new MaterialAlertDialogBuilder(this)
                .setTitle(R.string.backup_list_rename_title)
                .setView(wrap)
                .setPositiveButton(android.R.string.ok, (d, w) -> {
                    String name = input.getText().toString().trim();
                    if (name.isEmpty()) return;
                    if (!name.toLowerCase(Locale.ROOT).endsWith(".swb")) name += ".swb";
                    File dest = new File(file.getParentFile(), name);
                    if (file.renameTo(dest)) loadBackups();
                })
                .setNegativeButton(R.string.common_word_cancel, null)
                .show();
    }

    private void confirmDelete(final File file) {
        new MaterialAlertDialogBuilder(this)
                .setTitle(R.string.backup_list_action_delete)
                .setMessage(R.string.backup_list_delete_confirm)
                .setPositiveButton(R.string.backup_list_action_delete, (d, w) -> {
                    if (file.delete()) {
                        Toast.makeText(this, R.string.backup_list_deleted, Toast.LENGTH_SHORT).show();
                        loadBackups();
                    }
                })
                .setNegativeButton(R.string.common_word_cancel, null)
                .show();
    }

    private String formatSize(long bytes) {
        if (bytes < 1024) return bytes + " B";
        if (bytes < 1024 * 1024) return String.format(Locale.ROOT, "%.1f KB", bytes / 1024.0);
        return String.format(Locale.ROOT, "%.2f MB", bytes / (1024.0 * 1024.0));
    }

    private String formatDate(long millis) {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(new Date(millis));
    }

    private class Adapter extends RecyclerView.Adapter<Adapter.VH> {
        private List<File> items = new ArrayList<>();

        void setItems(List<File> newItems) {
            this.items = newItems;
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(android.R.layout.simple_list_item_2, parent, false);
            return new VH(v);
        }

        @Override
        public void onBindViewHolder(@NonNull VH holder, int position) {
            File f = items.get(position);
            String name = f.getName();
            if (name.toLowerCase(Locale.ROOT).endsWith(".swb")) {
                name = name.substring(0, name.length() - 4);
            }
            holder.title.setText(name);
            holder.subtitle.setText(formatSize(f.length()) + "  •  " + formatDate(f.lastModified()));
            holder.itemView.setOnClickListener(v -> showActions(f));
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        class VH extends RecyclerView.ViewHolder {
            final TextView title;
            final TextView subtitle;
            VH(@NonNull View v) {
                super(v);
                title = v.findViewById(android.R.id.text1);
                subtitle = v.findViewById(android.R.id.text2);
            }
        }
    }
}
