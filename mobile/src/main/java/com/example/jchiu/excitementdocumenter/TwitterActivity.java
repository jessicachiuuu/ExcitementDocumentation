package com.example.jchiu.excitementdocumenter;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import com.twitter.sdk.android.tweetcomposer.TweetComposer;

import java.io.File;


public class TwitterActivity extends ActionBarActivity {

    private Uri fileUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_twitter);

        Intent i = getIntent();
        fileUri = (Uri) i.getExtras().get(Intent.EXTRA_STREAM);

        TweetComposer.Builder builder = new TweetComposer.Builder(this)
                .text("#cs160excited")
                .image(fileUri);
        builder.show();

//        File myImageFile = new File("/path/to/image");
//        Uri myImageUri = Uri.fromFile(myImageFile);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_twitter, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
