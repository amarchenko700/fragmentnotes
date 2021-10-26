package com.example.fragmentnotes.ui.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.example.fragmentnotes.R;

public class DialogExitApp extends DialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return new AlertDialog.Builder(getContext())
                .setTitle(R.string.exitTitle)
                .setIcon(R.drawable.ic_exit)
                .setMessage(R.string.messageToExit)
                .setPositiveButton(R.string.textYes, (dialog, which) -> exitFromApp())
                .setNegativeButton(R.string.textNo, ((dialog, which) -> showToast(getContext(), R.string.appContinues)))
                .create();
    }

    private void exitFromApp() {
        showToast(getContext(), R.string.textAppClosed);
        getActivity().finish();
    }

    private void showToast(Context context, int msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }
}
