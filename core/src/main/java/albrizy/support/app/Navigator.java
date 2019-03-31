package albrizy.support.app;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

@SuppressWarnings("WeakerAccess")
public class Navigator {

    private static final String EXTRA_POSITION = "EXTRA_POSITION";

    public interface Adapter {
        Fragment onCreateFragment(int position);
        String getTag(int position);
        int getCount();
    }

    @NonNull
    private final Adapter adapter;
    private final FragmentManager manager;

    @IdRes
    private final int containerId;
    private int defaultPosition;
    private int currentPosition;

    public Navigator(FragmentManager manager, @NonNull Adapter adapter, @IdRes int idRes) {
        this.manager = manager;
        this.adapter = adapter;
        this.containerId = idRes;
    }

    public void onCreate(@Nullable Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            currentPosition = savedInstanceState.getInt(EXTRA_POSITION, defaultPosition);
        }
    }

    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(EXTRA_POSITION, currentPosition);
    }

    public void setDefaultPosition(int defaultPosition) {
        this.defaultPosition = defaultPosition;
    }

    public int getCurrentPosition() {
        return currentPosition;
    }

    public Fragment getCurrentFragment() {
        return getFragment(currentPosition);
    }

    public Fragment getFragment(int position) {
        return manager.findFragmentByTag(adapter.getTag(position));
    }

    public void showFragment(int position) {
        this.showFragment(position, false);
    }

    public void showFragment(int position, boolean reset) {
        this.showFragment(position, reset, false);
    }

    public void showFragment(int position, boolean reset, boolean allowStateLoss) {
        final FragmentTransaction ft = manager.beginTransaction();
        for(int i = 0; i < adapter.getCount(); i++) {
            if (position == i) {
                if (reset) {
                    removeFragment(ft, position);
                    addFragment(ft, position);
                } else {
                    showFragment(ft, position);
                }
            } else {
                hideFragment(ft, position);
            }
        }
        commitTransaction(ft, allowStateLoss);
        currentPosition = position;
    }

    private void showFragment(FragmentTransaction ft, int position) {
        String tag = adapter.getTag(position);
        Fragment fragment = manager.findFragmentByTag(tag);
        if (fragment == null) {
            addFragment(ft, position);
        } else {
            ft.show(fragment);
        }
    }

    private void hideFragment(FragmentTransaction ft, int position) {
        String tag = adapter.getTag(position);
        Fragment fragment = manager.findFragmentByTag(tag);
        if (fragment != null) {
            ft.hide(fragment);
        }
    }

    private void addFragment(FragmentTransaction ft, int position) {
        String tag = adapter.getTag(position);
        Fragment fragment = adapter.onCreateFragment(position);
        ft.add(containerId, fragment, tag);
    }

    private void removeFragment(FragmentTransaction ft, int position) {
        String tag = adapter.getTag(position);
        Fragment fragment = manager.findFragmentByTag(tag);
        if (fragment != null) {
            ft.remove(fragment);
        }
    }

    private void commitTransaction(FragmentTransaction ft, boolean allowStateLoss) {
        if (allowStateLoss) {
            ft.commitAllowingStateLoss();
        } else {
            ft.commit();
        }
    }
}
