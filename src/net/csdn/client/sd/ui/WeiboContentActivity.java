package net.csdn.client.sd.ui;

import net.csdn.client.sd.R;
import net.csdn.client.sd.provider.RefreshTask;
import net.csdn.client.sd.utils.UIUtils;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class WeiboContentActivity extends Activity {
	private static final String TAG = "WeiboContentActivity";
	TextView nav_title;
	Button nav_refresh;
	private Intent intent;
	private String link;
	private WebView mWebView;
	RefreshTask refreshTask;

	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.web);
		nav_title = (TextView) findViewById(R.id.navigation_text);
		nav_refresh = (Button) findViewById(R.id.navigation_right_bt);
		nav_title.setText(R.string.nav_title_weibo);
		nav_refresh.setOnClickListener(reflashClickListener);

		intent = this.getIntent();
		link = intent.getStringExtra("link");
		Log.e(TAG, link);
		mWebView = (WebView) findViewById(R.id.mWebView);
		mWebView.getSettings().setJavaScriptEnabled(true);
		mWebView.setWebViewClient(new WebViewClient() {
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;
			}
		});
		mWebView.loadUrl(link);
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebView.canGoBack()) {
			mWebView.goBack();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	private OnClickListener reflashClickListener = new OnClickListener() {
		public void onClick(View v) {
			updataData();
		}
	};

	private void updataData() {
		if (!UIUtils.hasInternet(this)) {
			Toast.makeText(this, "更新需要您开启网络", Toast.LENGTH_LONG).show();
			return;
		}
		refreshTask = new RefreshTask();
		refreshTask.execute(this);
	}
}
