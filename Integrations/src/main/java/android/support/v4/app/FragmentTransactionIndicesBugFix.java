package android.support.v4.app;

import java.util.Collections;

public class FragmentTransactionIndicesBugFix {
    public static void reorderIndices(FragmentManager fragmentManager) {
        if (!(fragmentManager instanceof FragmentManagerImpl))
            return;
        FragmentManagerImpl fragmentManagerImpl = (FragmentManagerImpl) fragmentManager;
        if (fragmentManagerImpl.mAvailIndices != null)
            Collections.sort(fragmentManagerImpl.mAvailIndices, Collections.reverseOrder());
    }
}