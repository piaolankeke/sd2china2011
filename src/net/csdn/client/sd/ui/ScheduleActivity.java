package net.csdn.client.sd.ui;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import net.csdn.client.sd.R;
import net.csdn.client.sd.provider.RefreshTask;
import net.csdn.client.sd.utils.FeedParser;
import net.csdn.client.sd.utils.UIUtils;
import android.app.ListActivity;
import android.content.Context;
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

public class ScheduleActivity extends CommonActivity implements OnItemClickListener {
	private static final String TAG = "ScheduleActivity";
//	TextView nav_title;
//	Button nav_refresh;
	private ListView list;
	private JSONObject schedules;
	private JSONArray arrayJson;
	private ArrayList<JSONObject> ret = new ArrayList();
	FeedParser feedParser;
	RefreshTask refreshTask;

	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.list_content);

		feedParser = new FeedParser(this);
		
		initHeader(R.string.nav_title_schedule);
//		nav_title = (TextView) findViewById(R.id.navigation_text);
		nav_title.setText(R.string.nav_title_schedule);
//
//		nav_refresh = (Button) findViewById(R.id.navigation_right_bt);
//		nav_refresh.setOnClickListener(reflashClickListener);
		
		list = getListView();
		
		try {
			schedules = feedParser.readAssetsJSON("schedule.json");
			arrayJson = schedules.getJSONArray("schedule");
			for (int i = 0; i < arrayJson.length(); i++) {
				ret.add(arrayJson.getJSONObject(i));
			}

			Log.e(TAG, ret.get(0).getString("title"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		ItemAdapter adapter = new ItemAdapter(this, R.layout.event_items, ret);
		list.setAdapter(adapter);
		list.setOnItemClickListener(this);
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
		refreshTask.execute(this);
	}
*/
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub

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

	public final class ViewHolder {
		public TextView month;
		public TextView day;
		public TextView title;
	}

	class ItemAdapter extends ArrayAdapter<JSONObject> {
		int resource;
		List items;
		LayoutInflater vi;
//		LinearLayout todoView;

		public ItemAdapter(Context _context, int _resource,
				List<JSONObject> _items) {
			super(_context, _resource, _items);
			resource = _resource;
			items = _items;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				String inflater = Context.LAYOUT_INFLATER_SERVICE;
				vi = (LayoutInflater) getContext().getSystemService(inflater);
				convertView = vi.inflate(resource, null);
				holder = new ViewHolder();
				holder.month = (TextView) convertView.findViewById(R.id.month);
				holder.day = (TextView) convertView.findViewById(R.id.day);
				holder.title = (TextView) convertView.findViewById(R.id.event_title);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			
			JSONObject item = (JSONObject) items.get(position);
			String smonth = "";
			String sday = "";
			String title = "";
			try {
				smonth = item.getString("month");
				sday = item.getString("day");
				title = item.getString("title");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			
			holder.month.setText(smonth);
			holder.day.setText(sday);
			holder.title.setText(title);

			return convertView;

		}
	}

}
