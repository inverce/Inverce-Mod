package com.inverce.mod.navigation.requests;

import android.os.Bundle;
import android.support.annotation.CheckResult;

import com.inverce.mod.navigation.interfaces.NavigationHandler;
import com.inverce.mod.core.interfaces.functions.Use;
import com.inverce.mod.core.utilities.SubBundleBuilder;

import java.io.Serializable;

@SuppressWarnings("unchecked")
public class NavigationRequest<TYPE extends NavigationRequest<TYPE>> implements Serializable {
    protected Bundle extras = new Bundle();
    private static NavigationHandler handler;

    /**
     * Finalize request, this will NOT schedule it to for execution, as such request can be modified or passed
     */
    @CheckResult
    public NavigationRequest build() {
        return this;
    }

    /**
     * Finalize request by scheduling it to for execution
     */
    public void make() {
        handler.handleRequest(this);
    }

    //<editor-fold desc="Edit data">
    @CheckResult
    public SubBundleBuilder<TYPE> setExtras() {
        return new SubBundleBuilder<>((TYPE) this, extras);
    }

    @CheckResult
    public TYPE setExtras(Use<Bundle> setup) {
        setup.use(extras);
        return (TYPE) this;
    }

    @CheckResult
    public TYPE setExtras(Bundle copyFrom) {
        if (copyFrom != null) {
            extras.putAll(copyFrom);
        }
        return (TYPE) this;
    }
    //</editor-fold>

    public static class Handler {
        public static void set(NavigationHandler handler) {
            NavigationRequest.handler = handler;
        }
    }
}
