package bharat.otouch.www.solutionexecutive;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by shaan on 12/07/2017.
 */

public class BackgroundLoginTask extends AsyncTask<String,Void,String> {
    Context ctx;
    public BackgroundLoginTask(Context ctx)
    {
        this.ctx=ctx;

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();


    }

    @Override
    protected String doInBackground(String... params) {
        String reg_url="http://172.28.172.2/problemsolver/loginattempt.php";
        Log.d("TAG", "step1");

        String method=params[0];
        if (method.equals("login"))
        {
            String Executive_name=params[1];
            String Executive_email=params[2];
            String Executive_pass=params[3];
            Log.d("TAG", Executive_name+" "+Executive_email+" "+Executive_pass);
            try {
                URL url=new URL(reg_url);
                HttpURLConnection httpURLConnection= (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                Log.d("TAG", "open url connnection ");
                OutputStream os=httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter=new BufferedWriter(new OutputStreamWriter(os,"UTF-8"));
                Log.d("TAG","buffered writer");
                //encode data before send it
                //no space permiteted in equals sign
                String OpnAcc= URLEncoder.encode("Name","UTF-8")+"="+ URLEncoder.encode(Executive_name,"UTF-8")+"&"+
                        URLEncoder.encode("Email","UTF-8")+"="+ URLEncoder.encode(Executive_email,"UTF-8")+"&"+
                        URLEncoder.encode("Password","UTF-8")+"="+ URLEncoder.encode(Executive_pass,"UTF-8");

                Log.d("TAG", "data parameter set ");
                bufferedWriter.write(OpnAcc);
                bufferedWriter.flush();
                bufferedWriter.close();
                Log.d("TAG", "buffer writer close");
                os.close();
                //get response from server
                InputStream is=httpURLConnection.getInputStream();
                Log.d("TAG", "debug");
                is.close();
                return "Login.....success";

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return null;
    }
    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        Log.d("TAG", "loged in");
        Toast.makeText(ctx, "Welcome", Toast.LENGTH_SHORT).show();
    }

}
