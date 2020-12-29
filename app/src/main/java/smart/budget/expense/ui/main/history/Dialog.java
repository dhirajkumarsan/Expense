package smart.budget.expense.ui.main.history;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.SearchView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import smart.budget.expense.R;

public class Dialog extends AppCompatDialogFragment {
    private SearchView searchView;
    private String username;
    @Override
    public android.app.Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.search_dialog,null);
        searchView = view.findViewById(R.id.search);
        SharedPreferences sharedpreferences = getActivity().getSharedPreferences("preferences", Context.MODE_PRIVATE);
        username=sharedpreferences.getString("username","");
        searchView.setQuery(username,true);
        searchView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

            }
        });
        builder.setView(view)
                .setTitle("Search")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SharedPreferences sharedpreferences = getActivity().getSharedPreferences("preferences", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putString("username", searchView.getQuery().toString());
                        editor.commit();
                        getActivity().finish();
                        startActivity(getActivity().getIntent());
                    }
                });
        return  builder.create();

    }
}
