/*
 ******************************************************************************************
 *
 *    Part of the master's thesis
 *    Topic: "Supporting the development of fine motor skills in children using IT tools"
 *
 *    FRIENDLY LETTERS created by Mikolaj Szotowicz : https://github.com/szotowicz
 *
 ****************************************************************************************
 */
package com.pg.mikszo.friendlyletters.activity;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.window.OnBackInvokedDispatcher;

import com.pg.mikszo.friendlyletters.R;

public abstract class BaseActivity extends Activity {

    protected abstract void loadOnCreateView();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        loadOnCreateView();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            getOnBackInvokedDispatcher().registerOnBackInvokedCallback(
                    OnBackInvokedDispatcher.PRIORITY_DEFAULT, this::handleBackNavigation);
        }
    }

    // Predictive back (Android 13+) no longer calls onBackPressed(), so this method is the
    // single place subclasses override for back handling; it is reached via the
    // OnBackInvokedCallback above on API 33+, and via onBackPressed() below on older devices.
    @Override
    public final void onBackPressed() {
        handleBackNavigation();
    }

    protected void handleBackNavigation() {
        super.onBackPressed();
    }

}