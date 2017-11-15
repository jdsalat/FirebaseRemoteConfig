package `in`.blogspot.thejavedsalat.remoteconfigdemo

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.ImageView
import android.widget.Toast
import com.google.android.gms.tasks.Task
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import com.squareup.picasso.Picasso

class MainActivity : AppCompatActivity() {
    lateinit var mFirebaseRemoteConfig: FirebaseRemoteConfig
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance()

        val firbaseRemoteConfigSetting: FirebaseRemoteConfigSettings = FirebaseRemoteConfigSettings.Builder()
                .setDeveloperModeEnabled(true).build()
        mFirebaseRemoteConfig.setConfigSettings(firbaseRemoteConfigSetting)
        mFirebaseRemoteConfig.setDefaults(R.xml.default_values)

        fetchConfig()
    }

    private fun fetchConfig() {
        var cacheDurtion: Long = 10
        if (mFirebaseRemoteConfig.info.configSettings.isDeveloperModeEnabled) {
            cacheDurtion = 0
        }

        mFirebaseRemoteConfig.fetch(cacheDurtion).addOnCompleteListener { task: Task<Void> ->
            if (task.isComplete) {
                Toast.makeText(this, "Fetch Succeeded",
                        Toast.LENGTH_SHORT).show();
                mFirebaseRemoteConfig.activateFetched()
                displayImage()
            } else {
                Toast.makeText(this, "Fetch Failed",
                        Toast.LENGTH_SHORT).show();
            }

        }

    }


    private fun displayImage() {
        val image_url = mFirebaseRemoteConfig.getString("image_url")
        Picasso.with(this).setLoggingEnabled(true)
        Picasso.with(this).load(image_url).resize(300,300).into(findViewById<ImageView>(R.id.imageView));
    }

    override fun onResume() {
        super.onResume()
        fetchConfig()
       // displayImage()
    }

}
