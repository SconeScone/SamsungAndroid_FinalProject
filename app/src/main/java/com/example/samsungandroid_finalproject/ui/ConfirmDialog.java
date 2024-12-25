package com.example.samsungandroid_finalproject.ui;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.example.samsungandroid_finalproject.R;

public class ConfirmDialog extends DialogFragment {
    public static final String REQUEST_KEY = "ConfirmDialog.REQUEST_KEY";
    public static final String RESULT_KEY = "ConfirmDialog.RESULT_KEY";

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        builder.setTitle(R.string.dialog_deletion_title)
                .setMessage(R.string.dialog_deletion_message)
                .setPositiveButton(R.string.delete_plant, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Bundle result = new Bundle();
                        result.putBoolean(RESULT_KEY, true);
                        getParentFragmentManager().setFragmentResult(REQUEST_KEY, result);
                        dialog.dismiss();
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        return builder.create();
    }
}
