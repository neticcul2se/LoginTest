package pae.com.wa.logintest;

import android.app.Activity;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends Activity {
    String android_id="";
Button login;
    EditText tuser,tpassword;
String user,password,encrypt;
    String resultkey = null;
    String resultlogin= null;
    String Jkey=null;
    JSONObject json = null;
    String key = "";
    TextView h;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (android.os.Build.VERSION.SDK_INT > 9) {

            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

            StrictMode.setThreadPolicy(policy);

        }
        login=(Button)findViewById(R.id.btnlog);
        tuser=(EditText)findViewById(R.id.username);
        tpassword=(EditText)findViewById(R.id.password);
h=(TextView)findViewById(R.id.head);
        //TelephonyManager tm = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
        android_id = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        //android_id = tm.getDeviceId();


        final String urlall = getUrl(android_id);

        resultkey = getHttpGet(urlall);


        JSONObject Jserver = null;
        try {
            Jserver = new JSONObject(resultkey);
            Jkey = Jserver.getString("key");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                user = tuser.getText().toString();
                password = tpassword.getText().toString();
                String sum = Jkey + password;
                encrypt = md5.md5gen(sum);
                h.setText(encrypt);

                final String urlLog = "http://128.199.230.75/authen.php";
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("code", "09"));
                params.add(new BasicNameValuePair("id", android_id));
                params.add(new BasicNameValuePair("username", user));
                params.add(new BasicNameValuePair("password", encrypt));
                params.add(new BasicNameValuePair("other", ""));

                resultlogin = getHttpPost(urlLog, params);
                h.setText(resultlogin);
            }
        });




    }







    public static String getUrl(String android_id)
    {// connect to map web service
        StringBuffer urlString = new StringBuffer();
        urlString.append("http://128.199.230.75/genkey.php?code=");
        urlString.append("06");
        urlString.append("&id=");
        urlString.append(android_id.toString());

        return urlString.toString();
    }


    public String getHttpGet(String url) {
        StringBuilder str = new StringBuilder();
        HttpClient client = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(url);

        try {
            HttpResponse response = client.execute(httpGet);
            StatusLine statusLine = response.getStatusLine();
            int statusCode = statusLine.getStatusCode();

            if (statusCode == 200) { // Status OK
                HttpEntity entity = response.getEntity();
                InputStream content = entity.getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(content));
                String line;
                while ((line = reader.readLine()) != null) {
                    str.append(line);
                }
            } else {
                Log.e("Log", "Failed to download result..");
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return str.toString();
    }


    public String getHttpPost(String url,List<NameValuePair> data) {
        StringBuilder str = new StringBuilder();
        HttpClient client = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(url);

        try {
            /*List<NameValuePair> params = new ArrayList<NameValuePair>();

            params.add(new BasicNameValuePair("strA","pae"));
            params.add(new BasicNameValuePair("strB", "paee"));

            httpGet.setEntity(new UrlEncodedFormEntity(params));*/

            httpPost.setEntity(new UrlEncodedFormEntity(data));
            HttpResponse response = client.execute(httpPost);
            StatusLine statusLine = response.getStatusLine();
            int statusCode = statusLine.getStatusCode();

            if (statusCode == 200) { // Status OK
                HttpEntity entity = response.getEntity();
                InputStream content = entity.getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(content));
                String line;
                while ((line = reader.readLine()) != null) {
                    str.append(line);
                }
            } else {
                Log.e("Log", "Failed to download result..");
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return str.toString();
    }


}
