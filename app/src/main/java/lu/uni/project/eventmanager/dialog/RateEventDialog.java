package lu.uni.project.eventmanager.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import lu.uni.project.eventmanager.R;
import lu.uni.project.eventmanager.activity.BottomNavigationActivity;
import lu.uni.project.eventmanager.pojo.Event;
import lu.uni.project.eventmanager.util.PreferenceKeys;
import lu.uni.project.eventmanager.util.SharedPreferencesHelper;

public class RateEventDialog extends Dialog implements
        android.view.View.OnClickListener {

    public Activity c;
    public Dialog d;
    public Button yes, no;
    public TextView title;
    public Event event;
    public RatingBar ratingBar;
    public DismisListner listner;

    public RateEventDialog(Activity a, Event event, DismisListner listner) {
        super(a);
        // TODO Auto-generated constructor stub
        this.c = a;
        this.event= event;
        this.listner= listner;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.layout_rate_dialog);
        yes = (Button) findViewById(R.id.btn_yes);
        no = (Button) findViewById(R.id.btn_no);
        title= findViewById(R.id.rateTitle);
        ratingBar= findViewById(R.id.ratingBar);
        title.setText("Rate "+event.getEventName());
        yes.setOnClickListener(this);
        no.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_yes:
                FirebaseAuth auth = FirebaseAuth.getInstance();
                FirebaseUser user = auth.getCurrentUser();
                String uid = "";
                if (user != null) {
                    uid = user.getUid();
                }
                DatabaseReference db= FirebaseDatabase.getInstance().getReference("rating");
                db.child(event.getEventId()).child(uid).setValue(ratingBar.getRating());
                listner.onDismiss(ratingBar.getRating());
                break;
            case R.id.btn_no:
                dismiss();
                break;
            default:
                break;
        }
        dismiss();
    }

    public interface DismisListner{
        public void onDismiss(float rating);
    }
}