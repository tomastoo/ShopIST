package pt.ulisboa.tecnico.cmov.shopist.Pantry;

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

import pt.ulisboa.tecnico.cmov.shopist.QRCodeScanner;
import pt.ulisboa.tecnico.cmov.shopist.R;
import util.db.queryInterfaces.PantryItem;

public class DialogRemoveItem extends AppCompatDialogFragment{
    private PantryItem pantryItem;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_dialog_remove, null);

        builder.setView(view).setTitle("Confirm item removal")
                .setNegativeButton("no", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setNeutralButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ((PantryList)getActivity()).deleteItemDialog(pantryItem);
                    }
                });

        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    public void setPantryItem(PantryItem pantryItem) {
        this.pantryItem = pantryItem;
    }
}
