package net.csdn.client.sd.ui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.csdn.client.sd.R;
import net.csdn.client.sd.provider.RefreshTask;
import net.csdn.client.sd.ui.ScheduleActivity.ItemAdapter;
import net.csdn.client.sd.ui.ScheduleActivity.ViewHolder;
import net.csdn.client.sd.utils.Base64;
import net.csdn.client.sd.utils.FeedParser;
import net.csdn.client.sd.utils.UIUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class LecturerActivity extends ListActivity {
	private static final String TAG = "LecturerActivity";
	TextView nav_title;
	Button nav_refresh;
	private ListView list;
	private Intent intent;
	private String typeid;
	private JSONObject lecturers;
	private JSONArray arrayJson;
	private ArrayList<JSONObject> ret = new ArrayList();
	FeedParser feedParser;
	RefreshTask refreshTask;

	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.list_content);

		feedParser = new FeedParser(this);

		nav_title = (TextView) findViewById(R.id.navigation_text);
		nav_refresh = (Button) findViewById(R.id.navigation_right_bt);
		nav_title.setText(R.string.nav_title_lecturer);

		intent = this.getIntent();
		typeid = intent.getStringExtra("typeid");
		nav_refresh.setOnClickListener(reflashClickListener);

		list = getListView();
		try {
			lecturers = feedParser.readAssetsJSON("lecturer.json");
			arrayJson = lecturers.getJSONObject("lecturer")
					.getJSONArray(typeid);
			for (int i = 0; i < arrayJson.length(); i++) {
				ret.add(arrayJson.getJSONObject(i));
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		ItemAdapter adapter = new ItemAdapter(this, R.layout.lecturer_items,
				ret);
		list.setAdapter(adapter);
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

	public final class ViewHolder {
		public ImageView avatar;
		public TextView name;
		public TextView title;
		public TextView message;
	}

	class ItemAdapter extends ArrayAdapter<JSONObject> {
		int resource;
		List items;
		LayoutInflater vi;

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
				holder.avatar = (ImageView) convertView
						.findViewById(R.id.lecturer_avatar);
				holder.name = (TextView) convertView
						.findViewById(R.id.lecturer_name);
				holder.title = (TextView) convertView
						.findViewById(R.id.lecturer_title);
				holder.message = (TextView) convertView
						.findViewById(R.id.lecturer_message);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			JSONObject item = (JSONObject) items.get(position);
			String sname = "";
			String stitle = "";
			String smessage = "";
			String savatar = "";
			try {
				sname = item.getString("name");
				stitle = item.getString("title");
				smessage = item.getString("description");
				savatar = item.getString("avatar");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			byte[] decodedString = null;
			try {
				decodedString = Base64.decode(savatar);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Bitmap bmp = BitmapFactory.decodeByteArray(decodedString, 0,
					decodedString.length);
			holder.avatar.setImageBitmap(bmp);

			holder.name.setText(sname);
			holder.title.setText(stitle);
			holder.message.setText(smessage);

			return convertView;

		}
	}

}
