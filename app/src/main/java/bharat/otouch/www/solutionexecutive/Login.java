package bharat.otouch.www.solutionexecutive;

import android.content.Context;
import android.content.Intent;
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
 * Created by shaan on 07/07/2017.
 */

public class Login extends AppCompatActivity implements View.OnClickListener {

    private TextView textView;
    private EditText mloginname;
    private EditText mloginemail;
    private EditText mloginpass;
    private Button mbutton;
    String login_name,login_email,login_pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        textView = (TextView) findViewById(R.id.create_account);
        textView.setOnClickListener(this);

        mloginname = (EditText) findViewById(R.id.login_name);
        mloginemail = (EditText) findViewById(R.id.login_email);
        mloginpass = (EditText) findViewById(R.id.login_password);
        mbutton = (Button) findViewById(R.id.loginbutton);

        mbutton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        if (v.getId()== R.id.create_account)
        {
          //  Toast.makeText(this, "create account clicked", Toast.LENGTH_SHORT).show();
            Intent create = new Intent(this,OpenAccount.class);
            startActivity(create);
        }
        if (v.getId()== R.id.loginbutton)
        {
            Intent log = new Intent(this,MainActivity.class);
            startActivity(log);
            login();
        }

    }

    private void login() {
        if (mloginname.getText().toString().length() == 0) {
            mloginname.setError("Name can not be blanked");
            return;
        } else if (mloginemail.getText().toString().length() == 0) {
            mloginemail.setError("Fill mobile number");
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(mloginemail.getText().toString()).matches()) {//Validation for Invalid Email Address
            mloginemail.setError("Invalid Email");
            return;
        } else if(mloginpass.getText().toString().length()==0) {
            mloginpass.setError("Invalid Password");
        }
        else {
            Toast.makeText(this, "All Fields Validated", Toast.LENGTH_SHORT).show();
        }

//validation END
        login_name = mloginname.getText().toString().trim();
        login_email = mloginemail.getText().toString().trim();
        login_pass = mloginpass.getText().toString().trim();
        Toast.makeText(this, "result" + mloginname + " " + mloginemail + " " + mloginpass, Toast.LENGTH_SHORT).show();
        if (isOnline()) {
            String method = "register";
            Toast.makeText(this, "connection is ok", Toast.LENGTH_SHORT).show();
            Log.d("TAG", "bt start");
            BackgroundLoginTask backgroundloginTask = new BackgroundLoginTask(this);
            backgroundloginTask.execute(method, login_name, login_email, login_pass);
            Log.d("TAG", "bt end");

        }
        else {
            Toast.makeText(Login.this, "Connection is Offline", Toast.LENGTH_SHORT).show();
        }
    }

    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        return info != null && info.isConnectedOrConnecting();
    }

}