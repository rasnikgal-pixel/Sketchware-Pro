package pro.sketchware.activities.soraeditor;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Адаптер ViewPager2 для редактора SORA.
 * Динамически создаёт фрагменты по списку открытых файлов.
 */
public class SoraEditorAdapter extends FragmentStateAdapter {

    private final List<String> filePaths = new ArrayList<>();
    private SoraEditorFragment.OnModifiedListener modifiedListener;
    private final List<SoraEditorFragment> fragments = new ArrayList<>();

    public SoraEditorAdapter(@NonNull FragmentActivity activity) {
        super(activity);
    }

    public void setOnModifiedListener(SoraEditorFragment.OnModifiedListener listener) {
        this.modifiedListener = listener;
    }

    public void setFiles(List<String> paths) {
        filePaths.clear();
        fragments.clear();
        if (paths != null) filePaths.addAll(paths);
        notifyDataSetChanged();
    }

    public void addFile(String path) {
        if (path == null || filePaths.contains(path)) return;
        filePaths.add(path);
        fragments.add(null);
        notifyItemInserted(filePaths.size() - 1);
    }

    public void removeFileAt(int index) {
        if (index < 0 || index >= filePaths.size()) return;
        filePaths.remove(index);
        if (index < fragments.size()) fragments.remove(index);
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

    public SoraEditorFragment getFragmentAt(int index) {
        if (index < 0 || index >= fragments.size()) return null;
        return fragments.get(index);
    }

    public int getIndexForPath(String path) {
        return filePaths.indexOf(path);
    }

    public int getCount() {
        return filePaths.size();
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
        while (fragments.size() <= position) fragments.add(null);
        fragments.set(position, fragment);
        return fragment;
    }

    @Override
    public void onBindViewHolder(@NonNull androidx.viewpager2.widget.ViewPager2.ViewHolder holder,
                                 int position, @NonNull List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);
    }
}
