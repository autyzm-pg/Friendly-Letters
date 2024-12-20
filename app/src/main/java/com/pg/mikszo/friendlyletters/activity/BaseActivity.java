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
import android.os.Bundle;

import com.pg.mikszo.friendlyletters.R;

public abstract class BaseActivity extends Activity {

    protected abstract void loadOnCreateView();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        loadOnCreateView();
    }

}