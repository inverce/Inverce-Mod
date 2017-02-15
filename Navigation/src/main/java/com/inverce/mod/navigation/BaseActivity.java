//package com.pelvifly.training.core;
//
//import android.content.Context;
//import android.content.Intent;
//import android.content.res.Configuration;
//import android.os.Bundle;
//import android.support.annotation.IdRes;
//import android.support.annotation.NonNull;
//import android.support.v4.app.Fragment;
//import android.support.v4.app.FragmentManager;
//import android.support.v4.app.FragmentTransaction;
//import android.support.v7.app.AppCompatActivity;
//import android.view.View;
//
//import com.pelvifly.app.R;
//import com.pelvifly.common.events.Event;
//import com.pelvifly.training.interfaces.ui.ActivityStream;
//import com.pelvifly.training.utils.SharedUtils;
//import com.pelvifly.training.utils.Tools;
//
//import java.io.Serializable;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Locale;
//
//import static com.pelvifly.training.Cfg.Enable.MultiLang;
//import static com.pelvifly.training.utils.SharedUtils.KEY_LANGUAGE;
//
//public abstract class BaseActivity extends AppCompatActivity implements ActivityStream {
//    protected final static String SERIALIZED_DATA = "serialized_param";
//    protected boolean blockBackPress;
//
//    protected
//    @IdRes
//    int getDefaultFragmentContainer() {
//        return R.id.content_container;
//    }
//
//
//    public void changeFragment(Fragment fragment) {
//        changeFragment(fragment, false);
//    }
//
//    public void changeFragment(Fragment fragment, @IdRes int res) {
//        changeFragment(fragment, false);
//    }
//
//    public synchronized void changeFragment(Fragment fragment, boolean addToBackStack) {
//        changeFragment(fragment, getDefaultFragmentContainer(), addToBackStack);
//    }
//
//    public synchronized void changeFragment(Fragment fragment, @IdRes int res, boolean addToBackStack) {
//        FragmentManager manager = getSupportFragmentManager();
//        FragmentTransaction transaction = manager.beginTransaction();
//        transaction.setCustomAnimations(R.anim.frag_animation_in, R.anim.frag_animation_out);
//        transaction.replace(res, fragment);
//        if (addToBackStack) {
//            transaction.addToBackStack(null);
//        }
//        transaction.commit();
//    }
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        List<Fragment> fragments = getSupportFragmentManager().getFragments();
//        if (fragments != null) {
//            for (Fragment fragment : fragments) {
//                if (fragment != null) {
//                    fragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
//                }
//            }
//        }
//    }
//    protected void setLanguage() {
//        if (SharedUtils.getValue(KEY_LANGUAGE) != null && MultiLang) {
//            setLocale(SharedUtils.getValue(KEY_LANGUAGE));
//        } else {
//            setLocale("pl");
//        }
//    }
//
//
//    public void setLocale(String lang) {
//        if (Tools.getContext() != null) {
//            Locale locale = new Locale(lang);
//            Locale.setDefault(locale);
//            Configuration config = new Configuration();
//            config.setLocale(locale);
//            Tools.getContext().getResources().updateConfiguration(config, null);
//            onConfigurationChanged(config);
//        }
//    }
//
//    /**************************************************************************/
//    /*************************** Arguments helpers ****************************/
//    /**************************************************************************/
//    private Bundle getArguments() {
//        if (getIntent() != null) {
//            if (getIntent().getExtras() != null) {
//                return getIntent().getExtras();
//            }
//        }
//        return new Bundle();
//    }
//
//    public int getArgumentInt(String name, int fallback) {
//        return getArguments().getInt(name, fallback);
//    }
//
//    public String getArgumentString(String name, String fallback) {
//        return getArguments().getString(name, fallback);
//    }
//
//    @SuppressWarnings("unchecked")
//    public <T extends Serializable> T getArgumentSerializable(String name) {
//        return (T) getArguments().getSerializable(name);
//    }
//
//    // Fragments utils
//
//
//    protected <T extends Fragment> T findFirstFragment(Class<T> fragmentClass) {
//        FragmentManager fragmentManager = getSupportFragmentManager();
//
//        ArrayList<Fragment> fragments = new ArrayList<>(fragmentManager.getFragments());
//        for (Fragment f : fragments) {
//            if (fragmentClass.isInstance(f)) {
//                return (T) f;
//            }
//        }
//        return null;
//    }
//
//    // Lifecycle
//
//    @Override
//    protected void onStop() {
//        super.onStop();
//        Event.Bus.unregister(ActivityStream.class, this);
//    }
//
//    @Override
//    protected void onStart() {
//        super.onStart();
//        Event.Bus.register(ActivityStream.class, this);
//    }
//
//
//    // Activity utils
//
//    public static <T extends BaseActivity> void navigate(Class<T> clazz, Context context) {
//        Intent intent = new Intent(context, clazz);
//        context.startActivity(intent);
//    }
//
//    public <T extends BaseActivity> void navigate(Class<T> clazz) {
//        Intent intent = new Intent(this, clazz);
//        startActivity(intent);
//    }
//
//    public <T extends BaseActivity> void navigate(Class<T> clazz, Serializable serializable) {
//        Intent intent = new Intent(this, clazz);
//        intent.putExtra(SERIALIZED_DATA, serializable);
//        startActivity(intent);
//    }
//
//    // Base methods
//    @Override
//    public void onBackPressed() {
//        if (!blockBackPress) {
//            super.onBackPressed();
//        }
//    }
//
//    @Override
//    public BaseActivity setBlockBackPress(boolean blockBackPress) {
//        this.blockBackPress = blockBackPress;
//        return this;
//    }
//
//    @Override
//    public void goBack() {
//        this.onBackPressed();
//    }
//
//
//    // Progress util
//    public View getProgress() {
//        return null;
//    }
//}
