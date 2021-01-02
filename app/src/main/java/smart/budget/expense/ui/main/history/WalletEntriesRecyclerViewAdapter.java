package smart.budget.expense.ui.main.history;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;

import androidx.constraintlayout.solver.widgets.Rectangle;
import androidx.fragment.app.FragmentActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.Environment;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.collection.LLRBNode;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import smart.budget.expense.R;
import smart.budget.expense.firebase.FirebaseElement;
import smart.budget.expense.firebase.FirebaseObserver;
import smart.budget.expense.firebase.ListDataSet;
import smart.budget.expense.firebase.models.Wallet;
import smart.budget.expense.firebase.viewmodel_factories.UserProfileViewModelFactory;
import smart.budget.expense.firebase.viewmodel_factories.WalletEntriesHistoryViewModelFactory;
import smart.budget.expense.firebase.models.User;
import smart.budget.expense.firebase.models.WalletEntry;
import smart.budget.expense.util.CategoriesHelper;
import smart.budget.expense.models.Category;
import smart.budget.expense.ui.main.history.edit_entry.EditWalletEntryActivity;
import smart.budget.expense.util.CurrencyHelper;

import static android.content.Context.MODE_PRIVATE;

public class WalletEntriesRecyclerViewAdapter extends RecyclerView.Adapter<WalletEntryHolder>  {

    private final String uid;
    private final FragmentActivity fragmentActivity;
    private ListDataSet<WalletEntry> walletEntries;
    private ListDataSet<WalletEntry> walletstoreEntries;
Context context;

    ViewGroup store;
    WalletEntryHolder holder_store;
    int position_store;
    int k = 0;
    int a=0;
    boolean added;
String username = "";

    private String cityi;
    private User user;
    private boolean firstUserSync = false,sync=false;

    String listText="";
    int i=0;


    public WalletEntriesRecyclerViewAdapter(FragmentActivity fragmentActivity, String uid) {

        System.out.println("changed");

        firstUserSync=false;
        if(walletstoreEntries!=null){
            walletstoreEntries.clear();
            System.out.println("wallet store cleared");
        }
        if(walletEntries!=null){
            walletEntries.clear();
            System.out.println("wallet cleared");


        }

        this.fragmentActivity = fragmentActivity;

        this.uid = uid;
        UserProfileViewModelFactory.getModel(uid, fragmentActivity).observe(fragmentActivity, new FirebaseObserver<FirebaseElement<User>>() {
            @Override
            public void onChanged(FirebaseElement<User> element) {
                System.out.println("changed");
                if (!element.hasNoError()) return;
                WalletEntriesRecyclerViewAdapter.this.user = element.getElement();
                if (!firstUserSync) {
                    System.out.println("getting wallet entries");
                    WalletEntriesHistoryViewModelFactory.getModel(uid, fragmentActivity).observe(fragmentActivity, new FirebaseObserver<FirebaseElement<ListDataSet<WalletEntry>>>() {
                        @Override
                        public void onChanged(FirebaseElement<ListDataSet<WalletEntry>> element) {
                            if (element.hasNoError()) {
                                System.out.println("putting wallet entries");
                                SharedPreferences sharedpreferences = fragmentActivity.getSharedPreferences("preferences", MODE_PRIVATE);
                                added = sharedpreferences.getBoolean("added",false);
                                System.out.println(element.getElement().getList());
if(!added){
    walletEntries = element.getElement();
    walletstoreEntries = element.getElement();
    //  System.out.println("changed");
    // checkCity();
   // generateText(walletEntries.getList());
    element.getElement().notifyRecycler(WalletEntriesRecyclerViewAdapter.this);

}

                            }
                        }
                    });
                }


                notifyDataSetChanged();
                firstUserSync = true;
            }
        });

    }


    public String sendListText(){
        return listText;
    }

    private void generateText(List<WalletEntry> walletEntryList) {
        if(walletEntries!=null){
            if(walletEntries.getList().size()>0) {
               

                for(int z=0;z<walletEntryList.size();z++){

                    System.out.println("size is"+walletEntryList.size());
                    WalletEntry w = walletEntryList.get(z);
                     a=w.duration;

                    long s = w.balanceDifference;

                    String state = " ";
                    if (s < 0)
                        state = "withdraw";
                    else
                        state = "deposited";



                    listText = listText + "Name: " + w.name
                            + "\nCity: " + w.village
                            + "\nAmount: " + CurrencyHelper.formatCurrency(user.currency, w.balanceDifference)
                            + "\nState: " + state
                            + "\n\n";
                }





            }
        }


    }


   /* private void checkCity() {
        if (!city.equals("default")) {
            int i = 0;
            for (WalletEntry w : walletEntries.getList()) {
                String s="City is : "+city
                        +"\n Village is : "+w.village;
                //TODO Extract the Complete list and compare to the walletEntries.village field and use the updated list in recycler view
                //System.out.println("city................." + city + "..............village is........" + w.village);
                if (!city.equals(w.city)) {
                    //walletEntries.getList().remove(i);
                }
                i++;
            }
        }
    }*/

    @Override
    public WalletEntryHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        store = parent;
        System.out.println("onCreateViewHolder");
        LayoutInflater inflater = LayoutInflater.from(fragmentActivity);
        View view = inflater.inflate(R.layout.history_listview_row, parent, false);


       // view.setBackgroundResource(R.drawable.category_gaming);
        if(a==12){

        }
        return new WalletEntryHolder(view);
    }


    @Override
    public void onBindViewHolder(WalletEntryHolder holder, int position) {
        position_store=position;
        holder_store = holder;

        System.out.println("onBindViewHolder");
        String id = walletEntries.getIDList().get(position);
        WalletEntry walletEntry = walletEntries.getList().get(position);
           // System.out.println(walletEntry.city);
            Category category = CategoriesHelper.searchCategory(user, walletEntry.categoryID);
            holder.iconImageView.setImageResource(category.getIconResourceID());
            holder.iconImageView.setBackgroundTintList(ColorStateList.valueOf(category.getIconColor()));
            holder.categoryTextView.setText(category.getCategoryVisibleName(fragmentActivity));
            holder.nameTextView.setText(walletEntry.name);
         //   System.out.println(city);

            Date date = new Date(-walletEntry.timestamp);
            DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            holder.dateTextView.setText(dateFormat.format(date));
            holder.moneyTextView.setText(CurrencyHelper.formatCurrency(user.currency, walletEntry.balanceDifference));
            holder.moneyTextView.setTextColor(ContextCompat.getColor(fragmentActivity,
                    walletEntry.balanceDifference < 0 ? R.color.primary_text_expense : R.color.primary_text_income));
            int count=0;
          SharedPreferences sharedPreferences=fragmentActivity.getSharedPreferences(id,MODE_PRIVATE);
           count=sharedPreferences.getInt("counter",0);
       // Toast.makeText(fragmentActivity, ""+count, Toast.LENGTH_SHORT).show();
            if(count==1){
                holder.view.setBackgroundColor(Color.GREEN);
            }else if(count>=2){
                holder.view.setBackgroundColor(Color.RED);
        }else{
                holder.view.setBackgroundColor(Color.WHITE);
            }
            holder.view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                    createDeleteDialog(id, uid, walletEntry.balanceDifference, fragmentActivity, holder);


                    return false;
                }
            });

            holder.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(fragmentActivity, EditWalletEntryActivity.class);
                    intent.putExtra("wallet-entry-id", id);
                    intent.putExtra("date",holder.dateTextView.getText().toString());
                    fragmentActivity.startActivity(intent);

                }
            });



    }

    @Override
    public int getItemCount() {
        System.out.println("count");


        SharedPreferences sharedpreferences = fragmentActivity.getSharedPreferences("preferences", MODE_PRIVATE);
        cityi = sharedpreferences.getString("city","Everywhere");
        username=sharedpreferences.getString("username","");
//System.out.println("get count");
//System.out.println(cityi);
       // WalletEntriesHistoryViewModelFactory.getModel(uid, fragmentActivity).getStartDate();
     //   walletstoreEntries = walletEntries;


        if (walletEntries == null) return 0;
       // System.out.println("stored");

      //  System.out.println(walletstoreEntries.getList().size());
      //  System.out.println(walletEntries.getList().size());

        // System.out.println(walletEntries);
       // System.out.println(walletstoreEntries);
   // System.out.println("counts");
        if(cityi==null)
            return 0;
       else if (!cityi.equals("default")) {
          //  System.out.println(walletEntries.getList().size());

         /* for (int j=0;j<walletstoreEntries.getList().size();j++) {
            //   WalletEntry walletEntry = walletstoreEntries.getList().get(j);
              // System.out.println(walletEntry.city);


                  // System.out.println("j " + j+1);
                 // System.out.println("spinner "+city);
                //  System.out.println("database "+walletEntry.city);
                // TODO Extract the Complete list and compare to the walletEntries.village field and use the updated list in recycler view
                //System.out.println("city................." + city + "..............village is........" + w.village);
              if (!cityi.equals(walletstoreEntries.getList().get(j).city)) {
                    walletstoreEntries.getList().remove(j);
                    j--;
                //    notifyDataSetChanged();
                    //System.out.println(walletEntry.c);
                }

            } */

if(walletEntries.getList().size()==0){
    return 0;
}
if(cityi.equals("Everywhere")){
    listText =new String();
    generateText(walletstoreEntries.getList());
    return walletstoreEntries.getList().size();
}
else {


    k = 0;
    int j = 0;
    boolean flag = false;
    while (j < walletEntries.getList().size()) {
        flag = false;
        if ((!cityi.equals(walletEntries.getList().get(j).city)) || !(walletEntries.getList().get(j).name.toString().toLowerCase().trim().contains(username.toLowerCase().trim()))) {
            try {
                walletEntries.getList().remove(j);

                flag = true;
//            notifyItemRemoved(j);
            } catch (IndexOutOfBoundsException e) {
//            notifyDataSetChanged();
                e.printStackTrace();
            }

        }
        j++;
        if (flag == true) {
            j--;
            k++;
        }
    }




           /* for (int j=0;j<walletstoreEntries.getList().size()-k;j++) {
                //System.out.println(walletstoreEntries.getList().size());
               System.out.println("j value"+j);
                System.out.println(walletstoreEntries.getList().get(j).city);
                System.out.println(walletstoreEntries.getList());
                //TODO Extract the Complete list and compare to the walletEntries.village field and use the updated list in recycler view
                //System.out.println("city................." + city + "..............village is........" + w.village);
               if (!cityi.equals(walletstoreEntries.getList().get(j).city)) {

                   walletstoreEntries.getList().remove(j);
                   j--;
                   k++;
                }

            } */
    listText =new String();

    generateText(walletEntries.getList());
    return walletEntries.getList().size();
}


        }
        else{
           // System.out.println("default");

            return 0;

        }
    }

    private void createDeleteDialog(String id, String uid, long balanceDifference, Context context,WalletEntryHolder holder) {
        new AlertDialog.Builder(context)
                .setMessage("Do you want to delete?")
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        FirebaseDatabase.getInstance().getReference()
                                .child("wallet-entries").child(uid).child("default").child(id).removeValue();
                        user.wallet.sum -= balanceDifference;
                        UserProfileViewModelFactory.saveModel(uid, user);
                        SharedPreferences sharedPreferences=fragmentActivity.getSharedPreferences(id,MODE_PRIVATE);
                        SharedPreferences.Editor editor=sharedPreferences.edit();
                        editor.clear();
                        editor.apply();
                        dialog.dismiss();
                    }

                })

                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();

                    }
                })
                .setNeutralButton("go_last", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        int po= holder.getAdapterPosition();
                      //  int size=walletstoreEntries.getList().size();
                       // onBindViewHolder(holder_store,po+1);
                      notifyItemMoved(po,position_store);

                    }
                })
                .create().show();
    }

    public void setDateRange(Calendar calendarStart, Calendar calendarEnd) {
        WalletEntriesHistoryViewModelFactory.getModel(uid, fragmentActivity).setDateFilter(calendarStart, calendarEnd);
    }



}