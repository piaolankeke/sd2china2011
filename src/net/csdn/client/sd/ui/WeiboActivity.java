package net.csdn.client.sd.ui;

import java.util.ArrayList;
import java.util.List;

import net.csdn.client.sd.R;
import net.csdn.client.sd.provider.RefreshTask;
import net.csdn.client.sd.utils.FeedParser;
import net.csdn.client.sd.utils.UIUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
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

public class WeiboActivity extends ListActivity implements OnItemClickListener {
	private static final String TAG = "WeiboActivity";
	TextView nav_title;
	Button nav_refresh;
	private JSONObject weibo;
	private JSONArray arrayJson;
	private ArrayList<JSONObject> ret = new ArrayList();
	private ListView list;
	FeedParser feedParser;
	RefreshTask refreshTask;

	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.list_content);
		
		feedParser = new FeedParser(this);
		
		nav_title = (TextView) findViewById(R.id.navigation_text);
		nav_refresh = (Button) findViewById(R.id.navigation_right_bt);
		list = getListView();
		nav_title.setText(R.string.nav_title_weibo);
		nav_refresh.setOnClickListener(reflashClickListener);
		
		try {
			weibo = feedParser.readAssetsJSON("weibo.json");
			arrayJson = weibo.getJSONArray("weibo");
			for(int i = 0; i < arrayJson.length(); i++ ){
				ret.add(arrayJson.getJSONObject(i));
			}
			
		Log.e(TAG,ret.get(0).getString("title"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		ItemAdapter adapter = new ItemAdapter(this,R.layout.weibo_items,R.id.weibo_title,ret);
		list.setAdapter(adapter);
		list.setOnItemClickListener(this);
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
		// TODO Auto-generated method stub
		Intent viewMessage = new Intent(this, WeiboContentActivity.class);
		Bundle bundle = new Bundle();
		JSONObject item = (JSONObject)ret.get(position);
		try {
			bundle.putString("link",item.getString("link"));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		viewMessage.putExtras(bundle);
		this.startActivity(viewMessage);
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
	
	class ItemAdapter extends ArrayAdapter<JSONObject> {
		private RotateAnimation rotate=null;
		int resource;
		int textviewResource;
		List items;

	    public ItemAdapter(Context _context,int _resource,int _textviewResource, List<JSONObject> _items) {
	         super(_context, _resource, _textviewResource, _items);
	         resource = _resource;
	         textviewResource = _textviewResource;
	         items = _items;
	    }

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
            LinearLayout todoView;
            JSONObject item = (JSONObject) items.get(position);
            String title = "" ;
			try {
				title = item.getString("title");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            if (convertView == null){
                  todoView = new LinearLayout(getContext());
                  String inflater = Context.LAYOUT_INFLATER_SERVICE;
                  LayoutInflater vi;
                  vi = (LayoutInflater)getContext().getSystemService(inflater);
                  vi.inflate(resource, todoView, true);
            }
            else{
                  todoView = (LinearLayout) convertView;
            }
            TextView item_title = (TextView)todoView.findViewById(textviewResource);
            item_title.setText(title);
            
            return todoView;

		}
	}


}
