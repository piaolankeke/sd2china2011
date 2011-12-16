package net.csdn.client.sd.ui;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import net.csdn.client.sd.Const;
import net.csdn.client.sd.R;
import net.csdn.client.sd.utils.BaseAuthenicationHttpClient;
import net.csdn.client.sd.utils.FeedParser;
import net.csdn.client.sd.utils.UIUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.animation.RotateAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class ForumActivity extends CommonActivity implements OnItemClickListener {
	private static final String TAG = "ForumActivity";
//	TextView nav_title;
//	Button nav_refresh;
	private JSONObject forums;
	private JSONArray arrayJson;
	private ArrayList<JSONObject> ret = new ArrayList();
	private ListView list;
	FeedParser feedParser;
	RefreshTask refreshTask;
//	View refreshProgress;

	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.list_content);

		feedParser = new FeedParser(this);
		initHeader(R.string.nav_title_forum);
//		nav_title = (TextView) findViewById(R.id.navigation_text);
//		nav_refresh = (Button) findViewById(R.id.navigation_right_bt);
		list = getListView();
		nav_title.setText(R.string.nav_title_forum);
//		nav_refresh.setOnClickListener(reflashClickListener);
//		refreshProgress = findViewById(R.id.title_refresh_progress);
//		refreshProgress.setVisibility(View.GONE);

		try {
			forums = feedParser.readAssetsJSON("forum.json");
			arrayJson = forums.getJSONArray("forum");
			for (int i = 0; i < arrayJson.length(); i++) {
				ret.add(arrayJson.getJSONObject(i));
			}

			Log.e(TAG, this.getFilesDir() + this.getPackageName());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		ItemAdapter adapter = new ItemAdapter(this, R.layout.forum_items,
				R.id.forum_title, ret);
		list.setAdapter(adapter);
		list.setOnItemClickListener(this);
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position,
			long arg3) {
		// TODO Auto-generated method stub
		Intent viewMessage = new Intent(this, LecturerActivity.class);
		Bundle bundle = new Bundle();
		JSONObject item = (JSONObject) ret.get(position);
		try {
			bundle.putString("typeid", item.getString("typeid"));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		viewMessage.putExtras(bundle);
		this.startActivity(viewMessage);
	}
/*
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
		refreshTask.execute("");
	}
*/
	class ItemAdapter extends ArrayAdapter<JSONObject> {
		private RotateAnimation rotate = null;
		int resource;
		int textviewResource;
		List items;

		public ItemAdapter(Context _context, int _resource,
				int _textviewResource, List<JSONObject> _items) {
			super(_context, _resource, _textviewResource, _items);
			resource = _resource;
			textviewResource = _textviewResource;
			items = _items;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			LinearLayout todoView;
			JSONObject item = (JSONObject) items.get(position);
			String title = "";
			try {
				title = item.getString("title");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (convertView == null) {
				todoView = new LinearLayout(getContext());
				String inflater = Context.LAYOUT_INFLATER_SERVICE;
				LayoutInflater vi;
				vi = (LayoutInflater) getContext().getSystemService(inflater);
				vi.inflate(resource, todoView, true);
			} else {
				todoView = (LinearLayout) convertView;
			}
			TextView item_title = (TextView) todoView
					.findViewById(textviewResource);
			item_title.setText(title);

			return todoView;

		}
	}
/*
	private class RefreshTask extends AsyncTask<String, Void, Boolean> {

		@Override
		protected Boolean doInBackground(String... params) {
			// TODO Auto-generated method stub
			FeedParser feedParser = new FeedParser(ForumActivity.this);
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
			nav_refresh.setVisibility(View.GONE);
			refreshProgress.setVisibility(View.VISIBLE);
			super.onPreExecute();
		}

		@Override
		protected void onPostExecute(Boolean result) {
			Log.e(TAG, "更新完毕....");
			nav_refresh.setVisibility(View.VISIBLE);
			refreshProgress.setVisibility(View.GONE);
			super.onPostExecute(result);
		}

		@Override
		protected void onProgressUpdate(Void... values) {
			Log.e(TAG, "正在更新....");
			nav_refresh.setVisibility(View.GONE);
			refreshProgress.setVisibility(View.VISIBLE);
			super.onProgressUpdate(values);
		}

		@Override
		protected void onCancelled() {
			nav_refresh.setVisibility(View.VISIBLE);
			refreshProgress.setVisibility(View.GONE);
			super.onCancelled();
		}
	}
	*/
}
