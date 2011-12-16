package net.csdn.client.sd.ui;

import net.csdn.client.sd.Const;
import net.csdn.client.sd.R;
import net.csdn.client.sd.provider.RefreshTask;
import net.csdn.client.sd.ui.CommonActivity.Prefs;
import net.csdn.client.sd.utils.FeedParser;
import net.csdn.client.sd.utils.UIUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class HomeActivity extends Activity {
	private static final String TAG = "HomeActivity";

	private Button btn_calling, btn_map, btn_share;
	private JSONObject home;
	FeedParser feedParser;
	RefreshTask refreshTask;
	private double latitude = 0.0;
	private double longitude = 0.0;
	int local_version,remote_version;
	SharedPreferences prefs;

	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.home);

		feedParser = new FeedParser(this);
		feedParser.initData("home.json");

		try {
			home = feedParser.readAssetsJSON("home.json");
			home = home.getJSONObject("home");
			Log.e(TAG, home.getString("phone"));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		btn_calling = (Button) findViewById(R.id.contact_us);
		btn_map = (Button) findViewById(R.id.location);
		btn_share = (Button) findViewById(R.id.recommend);

		btn_calling.setOnClickListener(callingClickListener);
		btn_map.setOnClickListener(mapClickListener);
		btn_share.setOnClickListener(shareClickListener);

		if (UIUtils.hasInternet(this)) {
			prefs = getSharedPreferences("version_sync",
	                Context.MODE_PRIVATE);
			local_version = prefs.getInt("local_version", 0);
			remote_version = feedParser.getVersionFromServer();
			Log.e(TAG,"local_version="+local_version+",remote_version="+remote_version);
			if(remote_version > local_version ){
				HomeRefreshTask homeRefreshTask = new HomeRefreshTask();
				homeRefreshTask.execute("");
			}
		}

	}

	private OnClickListener callingClickListener = new OnClickListener() {
		public void onClick(View v) {
			// Perform action on click
			try {
				Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"
						+ home.getString("phone")));

				HomeActivity.this.startActivity(intent);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	};

	private OnClickListener mapClickListener = new OnClickListener() {
		public void onClick(View v) {
			// Perform action on click
			try {
				Log.e(TAG, home.getString("location"));
				if (UIUtils.checkGoogleMap(HomeActivity.this)) {
//					Intent it = new Intent(Intent.ACTION_VIEW,
//							Uri.parse("geo:0,0?q=" + home.getString("location")
//									+ "&hl=zh&z=15"));
					set_location();
					String uri = "http://maps.google.com/maps?f=d&saddr=" + latitude + "," + longitude 
                    + "&daddr=" + home.getString("location") + "&hl=en";
					Intent it = new Intent(Intent.ACTION_VIEW,Uri.parse(uri));
					startActivity(it);
				} else {
					Intent it = new Intent(
							Intent.ACTION_VIEW,
							Uri.parse("http://ditu.google.cn/maps?hl=zh&mrt=loc&q="
									+ home.getString("location")));
					startActivity(it);
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	};

	private OnClickListener shareClickListener = new OnClickListener() {
		public void onClick(View v) {
			// Perform action on click
			Intent intent = new Intent(Intent.ACTION_SEND);
			intent.setType("text/plain");

			try {
				JSONObject share_home = new JSONObject(home.getString("share"));
				Log.e(TAG,
						share_home.getString("subject")
								+ share_home.getString("content"));
				intent.putExtra(
						Intent.EXTRA_TEXT,
						share_home.getString("subject") + "  "
								+ share_home.getString("content") + "  "
								+ share_home.getString("url"));
				intent = Intent.createChooser(intent,
						share_home.getString("subject"));
				startActivity(intent);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	};

	private class HomeRefreshTask extends AsyncTask<String, Void, Boolean> {

		@Override
		protected Boolean doInBackground(String... params) {
			// TODO Auto-generated method stub
			feedParser = new FeedParser(HomeActivity.this);
			String data = feedParser.getDataFromServer();
			try {
				JSONObject obj = new JSONObject(data);
				for (int i = 0; i < Const.DATATAG.length; i++) {
					feedParser.updataData(obj.getString(Const.DATATAG[i]),
							Const.DATATAG[i]);
				}
			} catch (Exception ex) {

			}
			return null;
		}

		@Override
		protected void onPreExecute() {
			Log.e(TAG, "开始更新....");
			super.onPreExecute();
		}

		@Override
		protected void onPostExecute(Boolean result) {
			Log.e(TAG, "更新完毕....");
			prefs = getSharedPreferences("version_sync",
					Context.MODE_PRIVATE);
			SharedPreferences.Editor editor = prefs.edit();
			editor.putInt("local_version", remote_version);
			editor.commit();
			super.onPostExecute(result);
		}

		@Override
		protected void onProgressUpdate(Void... values) {
			Log.e(TAG, "正在更新....");
			super.onProgressUpdate(values);
		}

		@Override
		protected void onCancelled() {
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
	
	private void set_location() {
		LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			Location location = locationManager
					.getLastKnownLocation(LocationManager.GPS_PROVIDER);
			if (location != null) {
				latitude = location.getLatitude();
				longitude = location.getLongitude();
			}
		} else {
			LocationListener locationListener = new LocationListener() {
				@Override
				public void onLocationChanged(Location location) {
				}

				@Override
				public void onProviderDisabled(String provider) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void onProviderEnabled(String provider) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void onStatusChanged(String provider, int status,
						Bundle extras) {
					// TODO Auto-generated method stub
					
				}
			};
			locationManager
					.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
							1000, 0, locationListener);
			Location location = locationManager
					.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
			if (location != null) {
				latitude = location.getLatitude(); // 经度
				longitude = location.getLongitude(); // 纬度
			}
		}
	}
}
