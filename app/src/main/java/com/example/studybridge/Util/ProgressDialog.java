package com.example.studybridge.Util;

import android.app.Dialog;
import android.content.Context;
import android.view.Window;
import android.view.animation.Animation;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;

import com.example.studybridge.R;

public class ProgressDialog extends Dialog {

    private ProgressBar progressBar;

    public ProgressDialog(@NonNull Context context) {
        super(context);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_loading);

        progressBar = (ProgressBar) findViewById(R.id.loading);
    }
}
