package id.odt.museek.config;

import android.app.Application;
import androidx.multidex.MultiDex;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.twitter.sdk.android.core.TwitterAuthConfig;

/**
 * Created by arrival on 4/18/17.
 */

public class config extends Application {

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "8DgCR0CipvDPBMnxSPWmNVUYy";
    private static final String TWITTER_SECRET = "9DYW8MHRvarAowtemSiY9JOUaCC5HzoQWEcvTVXlcNFAzBfJF1";

    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    public void onCreate() {
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        super.onCreate();
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        MultiDex.install(this);
    }
}
