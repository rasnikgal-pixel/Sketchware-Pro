package mod.jbk.editor.manage.library;


import pro.sketchware.R;
import android.content.Context;

import androidx.annotation.Nullable;

import com.besome.sketch.beans.ProjectLibraryBean;
import com.besome.sketch.editor.manage.library.LibraryItemView;

import java.util.List;

import mod.jbk.build.BuiltInLibraries;

public class ExcludeBuiltInLibrariesLibraryItemView extends LibraryItemView {
    private final String sc_id;

    public ExcludeBuiltInLibrariesLibraryItemView(Context context, String sc_id) {
        super(context);
        this.sc_id = sc_id;
    }

    @Override
    public void setData(@Nullable ProjectLibraryBean projectLibraryBean) {
        boolean excludingEnabled = ExcludeBuiltInLibrariesActivity.isExcludingEnabled(sc_id);
        List<BuiltInLibraries.BuiltInLibrary> excludedLibraries = ExcludeBuiltInLibrariesActivity.getExcludedLibraries(sc_id);
        icon.setImageResource(ExcludeBuiltInLibrariesActivity.getItemIcon());
        title.setText(ExcludeBuiltInLibrariesActivity.getItemTitle());
        description.setText(
                !excludingEnabled ? ExcludeBuiltInLibrariesActivity.getDefaultItemDescription()
                        : String.format(ExcludeBuiltInLibrariesActivity.getSelectedLibrariesItemDescription(),
                        excludedLibraries.size(), BuiltInLibraries.KNOWN_BUILT_IN_LIBRARIES.length)
        );
        enabled.setText(excludingEnabled ? getString(R.string.auto_exclude_built_in_libraries_library_item_view_001) : getString(R.string.auto_exclude_built_in_libraries_library_item_view_002));
        enabled.setSelected(excludingEnabled);
    }
}
