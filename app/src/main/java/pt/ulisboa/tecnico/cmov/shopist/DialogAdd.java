package pt.ulisboa.tecnico.cmov.shopist;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

public class DialogAdd extends AppCompatDialogFragment{
    private final int REQUEST_CODE = 11;
    private String name;
    private EditText editTextListName;
    private DialogAddListener dialogAddListener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_dialog_add, null);

        builder.setView(view).setTitle("New List Name")
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        name = editTextListName.getText().toString();
                        if(name.length() > 0){
                            dialogAddListener.applyName(name);
                        }
                    }
                })
                .setNeutralButton("QR Code", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(getContext(), QRCodeScanner.class);
                        startActivity(intent);
                    }
                });
        editTextListName = view.findViewById(R.id.edit_list_name);

        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            dialogAddListener = (DialogAddListener) context;
        } catch (ClassCastException e) {
            throw  new ClassCastException(context.toString() + "must implement");
        }
    }



    public interface DialogAddListener{
        void applyName(String name);
    }
}
