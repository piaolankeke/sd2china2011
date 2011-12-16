package net.csdn.client.sd.provider;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

import net.csdn.client.sd.Const;
import org.apache.http.util.EncodingUtils;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

public class FeedParser {
	private static final String TAG = "FeedParser";
	public static Context mContext;

	public FeedParser(Context ctx) {
		mContext = ctx;
	}

	public String getDataFromServer() {
		String str = "";
		BaseAuthenicationHttpClient bh = new BaseAuthenicationHttpClient();
		str = bh.doGet(Const.FEEDURL);

		return str;
	}
	
	public void initData(String filename) {
		File file = new File(mContext.getFilesDir(), filename);
		if(!file.exists()){
			initDataFromAssets();
		}
	}

	public void initDataFromAssets() {
		InputStream in;
		byte[] buffer;
		FileOutputStream fos;
		File file;
		String folderName = "data";
		try {
			String[] fileNames = mContext.getAssets().list(folderName);
			Log.e("initDataFromAssets", "=" + folderName);
			for (int i = 0; i < fileNames.length; i++) {
				file = new File(mContext.getFilesDir(), fileNames[i]);
				Log.e("initDataFromAssets", "=" + file.getPath());
				fos = mContext.openFileOutput(file.getName(),
						Context.MODE_PRIVATE);

				in = mContext.getAssets().open(folderName + "/" + fileNames[i]);
				buffer = new byte[1024];
				int len;
				while ((len = in.read(buffer)) != -1) {
					buffer = EncodingUtils.getString(buffer, "utf-8")
							.getBytes();
					fos.write(buffer, 0, len);
				}

				fos.flush();
				in.close();
				fos.close();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void updataData(String data, String filename) {
		FileOutputStream out;
		try {
			File file = new File(mContext.getFilesDir(), filename + ".json");
			out = mContext.openFileOutput(file.getName(), Context.MODE_PRIVATE);
			Log.e(TAG,data);
			data = "{" + filename + ":" + data + "}";
			out.write(data.getBytes("utf-8"));
			out.flush();
			out.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public JSONObject readAssetsJSON(String filename) {
		JSONObject ret = new JSONObject();
		InputStream in = null;
		String folderName = "data";
		try {
			File file = new File(mContext.getFilesDir(), filename);
			Log.e("readAssetsJSON", "=" + file.exists());
			if (file.exists()) {
				in = mContext.openFileInput(file.getName());
			} else {
				in = mContext.getAssets().open(folderName + "/" + filename);
			}

			BufferedReader bufferedReader = new BufferedReader(
					new InputStreamReader(in, "UTF-8"));
			StringBuffer buffer = new StringBuffer();
			String line = bufferedReader.readLine();
			while (line != null) {
				buffer.append(line);
				line = bufferedReader.readLine();
			}
			bufferedReader.close();
			in.close();

			ret = new JSONObject(buffer.toString());

		} catch (Exception e) {
			e.printStackTrace();
		}
		return ret;
	}
}
