package evan.fullsail.wikiview;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import java.io.IOException;


public class MainActivity extends Activity {

    WebView webView;
    final String home = "http://en.m.wikipedia.org";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        WebViewClient webViewClient = new WebViewClient();
        webView = (WebView)findViewById(R.id.webView);
        webView.setWebViewClient(webViewClient);

        try {
            Intent intent = getIntent();
            Uri uri = intent.getData();
            if (uri != null) {
                Log.i("URI Data", uri.toString());
                String url = uri.toString();
                if (url.contains(home)) {
                    webViewClient.shouldOverrideUrlLoading(webView, url);
                    webView.loadUrl(url);
                }
                else
                {
                    Toast.makeText(MainActivity.this, "Sorry could not find this Wikipedia Page", Toast.LENGTH_LONG).show();
                    webViewClient.shouldOverrideUrlLoading(webView, home);
                    webView.loadUrl(home);
                }
            }
            else
            {
                webViewClient.shouldOverrideUrlLoading(webView, home);
                webView.loadUrl(home);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_save)
        {
            SaveData saveData = new SaveData(webView.getUrl(), this);
            saveData.execute();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private class SaveData extends AsyncTask<Void, Void, Void>
    {
        String url;
        Context context;
        public SaveData(String url, Context context)
        {
            this.url = url;
            this.context = context;
        }

        @Override
        protected Void doInBackground(Void... voids)
        {
            try
            {
                DataManagement.SaveNewFavorite(url, context);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }

            return null;
        }
    }
}
