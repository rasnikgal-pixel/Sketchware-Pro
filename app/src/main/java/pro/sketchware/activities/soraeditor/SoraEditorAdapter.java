package pro.sketchware.activities.soraeditor;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Адаптер ViewPager2 для редактора SORA.
 * Использует стабильные ID для сохранения фрагментов при переключении вкладок.
 */
public class SoraEditorAdapter extends FragmentStateAdapter {

    private final List<String> filePaths = new ArrayList<>();
    private SoraEditorFragment.OnModifiedListener modifiedListener;

    public SoraEditorAdapter(@NonNull FragmentActivity activity) {
        super(activity);
    }

    public void setOnModifiedListener(SoraEditorFragment.OnModifiedListener listener) {
        this.modifiedListener = listener;
    }

    public void setFiles(List<String> paths) {
        filePaths.clear();
        if (paths != null) filePaths.addAll(paths);
        notifyDataSetChanged();
    }

    public void addFile(String path) {
        if (path == null || filePaths.contains(path)) return;
        filePaths.add(path);
        notifyItemInserted(filePaths.size() - 1);
    }

    public void removeFileAt(int index) {
        if (index < 0 || index >= filePaths.size()) return;
        filePaths.remove(index);
        notifyItemRemoved(index);
        notifyItemRangeChanged(index, filePaths.size() - index);
    }

    public String getFilePath(int index) {
        if (index < 0 || index >= filePaths.size()) return null;
        return filePaths.get(index);
    }

    public List<String> getFilePaths() {
        return new ArrayList<>(filePaths);
    }

    public int getIndexForPath(String path) {
        return filePaths.indexOf(path);
    }

    @Override
    public int getItemCount() {
        return filePaths.size();
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        String path = filePaths.get(position);
        SoraEditorFragment fragment = SoraEditorFragment.newInstance(path);
        fragment.setOnModifiedListener(modifiedListener);
        return fragment;
    }

    @Override
    public long getItemId(int position) {
        if (position < 0 || position >= filePaths.size()) return RecyclerView_NO_ID;
        // Стабильный ID на основе пути файла
        return filePaths.get(position).hashCode();
    }

    @Override
    public boolean containsItem(long itemId) {
        for (String path : filePaths) {
            if (path.hashCode() == itemId) return true;
        }
        return false;
    }

    private static final long RecyclerView_NO_ID = -1L;
}
