package net.csdn.client.sd.ui;

import java.text.SimpleDateFormat;

import org.json.JSONObject;

import net.csdn.client.sd.Const;
import net.csdn.client.sd.R;
import net.csdn.client.sd.utils.FeedParser;
import android.app.ListActivity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class CommonActivity extends ListActivity {
	private static final String TAG = "CommonActivity";
	protected TextView nav_title;
	protected Button nav_refresh;
	protected View refreshProgress;
	protected Boolean is_loading = false;
	protected FeedParser feedParser;
	protected RefreshTask refreshTask;

	public int getLocalVersion() {
		SharedPreferences prefs = getSharedPreferences("version_sync",
				Context.MODE_PRIVATE);
		final int localVersion = prefs.getInt("local_version", 0);
		return localVersion;
	}

	public int getServerVersion() {
		feedParser = new FeedParser(CommonActivity.this);
		int version_code = feedParser.getVersionFromServer();
		return version_code;
	}

	public void updateLocalVersion() {
		int version_code = getServerVersion();
		SharedPreferences prefs = getSharedPreferences("version_sync",
				Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putInt("local_version", version_code);
		editor.commit();
	}

	public void initHeader(int navTitleSchedule) {
		nav_title = (TextView) findViewById(R.id.navigation_text);
		nav_refresh = (Button) findViewById(R.id.navigation_right_bt);
		refreshProgress = findViewById(R.id.title_refresh_progress);

//		nav_title.setText(R.string.nav_title_forum);
		nav_refresh.setOnClickListener(reflashClickListener);
		refreshProgress.setVisibility(View.GONE);
	}

	protected OnClickListener reflashClickListener = new OnClickListener() {
		public void onClick(View v) {
			updataData();
		}
	};

	private void updateRefreshStatus() {
		nav_refresh.setVisibility(is_loading ? View.GONE : View.VISIBLE);
		refreshProgress.setVisibility(is_loading ? View.VISIBLE : View.GONE);
	}

	protected void updataData() {
		refreshTask = new RefreshTask();
		refreshTask.execute("");
	}

	class RefreshTask extends AsyncTask<String, Void, Boolean> {

		@Override
		protected Boolean doInBackground(String... params) {
			// TODO Auto-generated method stub
			int local_version = getLocalVersion();
			int remote_version = getServerVersion();
			if ( local_version < remote_version) {
				feedParser = new FeedParser(CommonActivity.this);
				String data = feedParser.getDataFromServer();
				try {
					JSONObject obj = new JSONObject(data);
					for (int i = 0; i < Const.DATATAG.length; i++) {
						feedParser.updataData(obj.getString(Const.DATATAG[i]),
								Const.DATATAG[i]);
					}
				} catch (Exception ex) {

				}
			} 
			return null;
		}

		@Override
		protected void onPreExecute() {
			Log.e(TAG, "开始更新....");
			is_loading = true;
			updateRefreshStatus();
			super.onPreExecute();
		}

		@Override
		protected void onPostExecute(Boolean result) {
			Log.e(TAG, "更新完毕....");
			is_loading = false;
			updateLocalVersion();
			updateRefreshStatus();
			Toast.makeText(CommonActivity.this, "更新完毕",
					Toast.LENGTH_LONG).show();
			super.onPostExecute(result);
		}

		@Override
		protected void onProgressUpdate(Void... values) {
			Log.e(TAG, "正在更新....");
			is_loading = true;
			updateRefreshStatus();
			super.onProgressUpdate(values);
		}

		@Override
		protected void onCancelled() {
			is_loading = false;
			updateRefreshStatus();
			super.onCancelled();
		}
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		
	}

	@Override
	protected void onPause() {
		if (refreshTask != null && refreshTask.getStatus() != AsyncTask.Status.FINISHED) 
		{
			refreshTask.cancel(true);
		}
		
		super.onStop();
	}		

	protected interface Prefs {
		String PREFS_NAME = "version_sync";
		String LOCAL_VERSION = "local_version";
	}
}
