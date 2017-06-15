package com.awesomeproject;

/**
 * Created by mprabakaran on 6/14/17.
 */
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.widget.Toast;

import com.facebook.react.ReactInstanceManager;
import com.facebook.react.ReactRootView;
import com.facebook.react.common.LifecycleState;
import com.facebook.react.modules.core.DefaultHardwareBackBtnHandler;
import com.facebook.react.shell.MainReactPackage;
import android.util.Log;

public class RNActivity extends AppCompatActivity implements DefaultHardwareBackBtnHandler {
    private static final int OVERLAY_PERMISSION_REQ_CODE=100;
    private static final String TAG = "Muru React Native";
    private ReactRootView mReactRootView;
    private ReactInstanceManager mReactInstanceManager;


    public static Intent createLaunchIntent(Context context)
    {
        Log.d(TAG, "Create Launch Intent");
        return new Intent(context, RNActivity.class);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "On Create");

        mReactRootView=new ReactRootView(this);
        mReactInstanceManager=ReactInstanceManager.builder()
                .setApplication(getApplication())
                .setJSBundleFile("assets://main.jsbundle")
                //.setJSBundleFile("./node_modules/@fdx/widgets/native/android/dist/main.jsbundle")
                //.setBundleAssetName("index.android.bundle")
                .setJSMainModuleName("index.android")
                .addPackage(new MainReactPackage())
                .setUseDeveloperSupport(BuildConfig.DEBUG)
                .setInitialLifecycleState(LifecycleState.RESUMED)
                .build();
        mReactRootView.startReactApplication(mReactInstanceManager, "AwesomeProject", null);

        setContentView(mReactRootView);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode==OVERLAY_PERMISSION_REQ_CODE)
        {
            if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.M)
            {
                if (!Settings.canDrawOverlays(this))
                {
                    Toast.makeText(this, "Overlat permission deined", Toast.LENGTH_LONG);
                }
            }
        }
    }


    @Override
    public void invokeDefaultOnBackPressed()
    {
        super.onBackPressed();
    }


    @Override
    public void onBackPressed()
    {
        if (mReactInstanceManager!=null)
        {
            mReactInstanceManager.onBackPressed();
        }
        else
        {
            super.onBackPressed();
        }
    }


    @Override
    protected void onPause()
    {
        super.onPause();

        if (mReactInstanceManager!=null)
        {
            mReactInstanceManager.onHostPause();
        }
    }


    @Override
    protected void onResume()
    {
        super.onResume();

        if (mReactInstanceManager!=null)
        {
            mReactInstanceManager.onHostResume(this, this);
        }
    }


    @Override
    protected void onDestroy()
    {
        super.onDestroy();

        if (mReactInstanceManager!=null)
        {
            mReactInstanceManager.onHostDestroy();
        }
    }


    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event)
    {
        if (keyCode==KeyEvent.KEYCODE_MENU && mReactInstanceManager!=null)
        {
            mReactInstanceManager.showDevOptionsDialog();
            return true;
        }
        return super.onKeyUp(keyCode, event);
    }
}
