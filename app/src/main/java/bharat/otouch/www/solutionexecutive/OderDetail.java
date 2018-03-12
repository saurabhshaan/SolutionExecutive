package bharat.otouch.www.solutionexecutive;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by shaan on 14/07/2017.
 */

public class OderDetail extends AppCompatActivity implements View.OnClickListener {
String email,fcm_token;
    TextView dname, dmobile, demail, dquery;
    EditText editText;
    Button btn;
    String string;

    int quan = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.orderdetail);

        dname = (TextView) findViewById(R.id.nametext);
        dmobile = (TextView) findViewById(R.id.mobiletext);
        demail = (TextView) findViewById(R.id.emailtext);
        dquery = (TextView) findViewById(R.id.querytext);

        //code to send data to the client
        editText = (EditText) findViewById(R.id.texttosend);
        btn = (Button) findViewById(R.id.btntextsend);
        btn.setOnClickListener(this);
        dmobile.setText("Mobile : " + getIntent().getStringExtra("mobile"));
        demail.setText("Email : " + getIntent().getStringExtra("email"));
        dquery.setText("Query : " + getIntent().getStringExtra("query"));
        email=getIntent().getStringExtra("email");
        fcm_token = getIntent().getStringExtra("fcmtoken");


    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btntextsend) {
            detail();
        }
    }

    private void detail() {
        if (editText.getText().toString().length() == 0) {
            editText.setError("Replay.. can not be blanked");
            return;
        }
        else {
         //   Toast.makeText(this, "Fields Validated", Toast.LENGTH_SHORT).show();
        }
        string = editText.getText().toString().trim();
      //  Toast.makeText(this, "result" + string, Toast.LENGTH_SHORT).show();
        if (isOnline()) {
            String method = "register";
            Toast.makeText(this, "connection is ok", Toast.LENGTH_SHORT).show();
            Log.d("TAG", "bt start");
            BackgroundTaskOder backgroundTastOder = new BackgroundTaskOder(this);
            backgroundTastOder.execute(method, string,email,fcm_token);
            Log.d("TAG", "bt end");

        }
        else {
            Toast.makeText(OderDetail.this, "Connection is Offline", Toast.LENGTH_SHORT).show();
        }
    }
    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        return info != null && info.isConnectedOrConnecting();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}