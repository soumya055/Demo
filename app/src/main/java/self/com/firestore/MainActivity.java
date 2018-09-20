package self.com.firestore;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

public class MainActivity extends AppCompatActivity {

    public static final String QUOTE_KEY = "quote";
    public static final String AUTHOR_KEY = "author";

    private DocumentReference reference = FirebaseFirestore.getInstance().
            document("sampleData/inspiration");

    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv = findViewById(R.id.tv);
    }

    @Override
    protected void onStart() {
        super.onStart();
        reference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {

                if (documentSnapshot.exists()){
                    String quote = documentSnapshot.getString(QUOTE_KEY);
                    String author = documentSnapshot.getString(AUTHOR_KEY);
                    tv.setText("\"" + quote + "\" --" + author );
                }else{
                    Log.w("InspiringQuote","Got an exception!" + e);
                }
            }
        });
    }

    public void saveData(View view) {
        EditText quoteView = findViewById(R.id.quote);
        EditText authorView = findViewById(R.id.author);

        String quote = quoteView.getText().toString();
        String author = authorView.getText().toString();

        if (quote.isEmpty() || author.isEmpty()) {
            return;
        }

        Map<String, Object> dataToSave = new HashMap<>();
        dataToSave.put(QUOTE_KEY, quote);
        dataToSave.put(AUTHOR_KEY, author);
        reference.set(dataToSave).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()) {
                    Toast.makeText(MainActivity.this, "Hooray! Document has been saved!", Toast.LENGTH_SHORT).show();
                    System.out.println("Hooray!, Document has been saved!");
                } else {
                    Toast.makeText(MainActivity.this, " OOps,there was a problem!", Toast.LENGTH_SHORT).show();
                    System.out.println("OOps, Sorry, there was a problem!" + "\n" + task.getException());
                }
            }
        });
    }

    public void fetchData(View view) {
        reference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    String quote = documentSnapshot.getString(QUOTE_KEY);
                    String author = documentSnapshot.getString(AUTHOR_KEY);
                    System.out.println("asdasjkdabfjdkdfjkds"+ quote + "2:; " + author);
                    tv.setText("\"" + quote + "\" --" + author );
                }
            }
        });
    }
}
