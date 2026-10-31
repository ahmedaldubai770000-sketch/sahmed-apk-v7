
package com.sahmed.privatechat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.media.projection.MediaProjectionManager;
import android.os.Bundle;
import android.webkit.JavascriptInterface;
import android.webkit.PermissionRequest;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import com.getcapacitor.BridgeActivity;

public class MainActivity extends BridgeActivity {
    private static final int REQUEST_MEDIA_PROJECTION = 1001;
    private MediaProjectionManager projectionManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        projectionManager = (MediaProjectionManager) getSystemService(MEDIA_PROJECTION_SERVICE);

        try {
            WebView webView = getBridge().getWebView();
            webView.getSettings().setMediaPlaybackRequiresUserGesture(false);
            webView.getSettings().setJavaScriptEnabled(true);
            webView.addJavascriptInterface(new AndroidBridge(), "Android");
            
            webView.setWebChromeClient(new WebChromeClient() {
                @Override
                public void onPermissionRequest(PermissionRequest request) {
                    request.grant(request.getResources());
                }
            });
        } catch (Exception e) {}
    }

    public class AndroidBridge {
        @JavascriptInterface
        public void startScreenCapture() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Intent captureIntent = projectionManager.createScreenCaptureIntent();
                    startActivityForResult(captureIntent, REQUEST_MEDIA_PROJECTION);
                }
            });
        }
        
        @JavascriptInterface
        public boolean isNative(){
            return true;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_MEDIA_PROJECTION) {
            if (resultCode == Activity.RESULT_OK) {
                try {
                    getBridge().getWebView().evaluateJavascript(
                        "if(window.onScreenShareGranted) window.onScreenShareGranted(); console.log('Screen share granted');", null
                    );
                    // Start foreground service notification
                    Intent serviceIntent = new Intent(this, ScreenCaptureService.class);
                    serviceIntent.putExtra("resultCode", resultCode);
                    serviceIntent.putExtra("data", data);
                    startForegroundService(serviceIntent);
                } catch (Exception e) {}
            } else {
                try {
                    getBridge().getWebView().evaluateJavascript(
                        "alert('تم رفض مشاركة الشاشة');", null
                    );
                } catch (Exception e) {}
            }
        }
    }
}
