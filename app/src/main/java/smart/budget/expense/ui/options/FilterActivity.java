package smart.budget.expense.ui.options;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.net.Uri;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Arrays;

import smart.budget.expense.R;
import smart.budget.expense.ui.main.history.WalletEntriesRecyclerViewAdapter;

public class FilterActivity extends AppCompatActivity {
    RecyclerView recycler_View;

    DatabaseReference myRef ;
ArrayList<String> usersl =new ArrayList<>();
ListView listView;
ArrayAdapter<String>array;
    private RecyclerView historyRecyclerView;
    private WalletEntriesRecyclerViewAdapter historyRecyclerViewAdapter;
    private TextView dividerTextView;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
   // FirebaseAuth.getInstance().getCurrentUser().getUid();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter2);
       // dividerTextView = view.findViewById(R.id.divider_textview);
      //  dividerTextView.setText("Last 100 entries:");
        historyRecyclerView = findViewById(R.id.history_recycler_view);
        historyRecyclerView.setLayoutManager(new LinearLayoutManager(FilterActivity.this));
        historyRecyclerViewAdapter = new WalletEntriesRecyclerViewAdapter(FilterActivity.this, firebaseAuth.getCurrentUser().getUid());
        historyRecyclerView.setAdapter(historyRecyclerViewAdapter);
        historyRecyclerViewAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                historyRecyclerView.smoothScrollToPosition(0);
            }
        });
        /*listView=findViewById(R.id.list);
        array=new ArrayAdapter<String>(FilterActivity.this, android.R.layout.simple_list_item_1,usersl);
        listView.setAdapter(array);
    /*    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // User is signed in
            String name=user.getEmail();
            Toast.makeText(FilterActivity.this, ""+name, Toast.LENGTH_SHORT).show();
        } else {
            // No user is signed in
        }
    //    recycler_View=(RecyclerView)findViewById(R.id.history_recycler_view);
//        recycler_View.setLayoutManager(new LinearLayoutManager(this));
    /*    GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(FilterActivity.this);
        if (acct != null) {
            String personName = acct.getDisplayName();
            String personGivenName = acct.getGivenName();
            String personFamilyName = acct.getFamilyName();
            String personEmail = acct.getEmail();
            String personId = acct.getId();
            Uri personPhoto = acct.getPhotoUrl();
            Toast.makeText(FilterActivity.this, ""+personEmail+""+personName+""+personId, Toast.LENGTH_SHORT).show();
        }*/
        // Start listing users from the beginning, 1000 at a time
       /* GetUsersResult result = FirebaseAuth.getInstance().getUsersAsync(Arrays.asList(
                new UidIdentifier("uid1"),
                new ProviderIdentifier("google.com", "google_uid4"))).get();

        System.out.println("Successfully fetched user data:");
        for (UserRecord user : result.getUsers()) {
            System.out.println(user.getUid());
        }

        System.out.println("Unable to find users corresponding to these identifiers:");
        for (UserIdentifier uid : result.getNotFound()) {
            System.out.println(uid);
        }
        }*/
       /* myRef= FirebaseDatabase .getInstance().getReference().child("users");
        // Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
             //   Iterable<DataSnapshot> list=dataSnapshot.getChildren();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String s1=snapshot.getKey().toString();

                    usersl.add(s1);
                   // Toast.makeText(FilterActivity.this, ""+s1, Toast.LENGTH_SHORT).show();
                }

                array.notifyDataSetChanged();
              //  Log.d(TAG, "Value is: " + value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
               // Log.w(TAG, "Failed to read value.", error.toException());
            }
        });*/
    }

}