package net.csdn.client.sd.provider;

import net.csdn.client.sd.Const;
import net.csdn.client.sd.utils.FeedParser;

import org.json.JSONObject;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

public class RefreshTask extends AsyncTask<Context, Void, Boolean> {
	private static final String TAG = "RefreshTask";
	
	@Override
	protected Boolean doInBackground(Context... params) {
		FeedParser feedParser = new FeedParser(params[0]);
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
