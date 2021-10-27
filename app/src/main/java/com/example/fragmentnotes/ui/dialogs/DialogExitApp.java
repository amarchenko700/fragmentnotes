package com.example.fragmentnotes.ui.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.example.fragmentnotes.R;
import com.example.fragmentnotes.ui.MainActivity;
import com.google.android.material.snackbar.Snackbar;

public class DialogExitApp extends DialogFragment {
    private View view;
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return new AlertDialog.Builder(getContext())
                .setTitle(R.string.exitTitle)
                .setIcon(R.drawable.ic_exit)
                .setMessage(R.string.messageToExit)
                .setPositiveButton(R.string.textYes, (dialog, which) -> exitFromApp())
                .setNegativeButton(R.string.textNo, ((dialog, which) -> showSnakBar(R.string.appContinues)))
                .create();
    }

    private void showSnakBar(int appContinues) {
        Snackbar.make(view, appContinues, Snackbar.LENGTH_LONG).show();
    }

    private void exitFromApp() {
        Toast.makeText(getContext(), R.string.textAppClosed, Toast.LENGTH_SHORT).show();
        getActivity().finish();
    }

    public void setView(View view) {
        this.view = view;
    }
}
