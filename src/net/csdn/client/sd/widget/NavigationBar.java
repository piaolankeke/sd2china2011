package net.csdn.client.sd.widget;

import org.json.JSONObject;

import net.csdn.client.sd.provider.BaseAuthenicationHttpClient;
import net.csdn.client.sd.ui.ForumActivity;
import net.csdn.client.sd.R;
import net.csdn.client.sd.utils.UIUtils;

import android.content.Context;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class NavigationBar extends LinearLayout {
	private static final String TAG = "NavigationBar";
	private TextView nav_title;
	private Button nav_btn;
	Context cxt;

	public NavigationBar(Context paramContext) {
		super(paramContext);
	}

	public NavigationBar(Context paramContext, AttributeSet paramAttributeSet) {
		super(paramContext, paramAttributeSet);
		this.cxt = paramContext;
		setOrientation(LinearLayout.VERTICAL);
		LayoutInflater.from(this.cxt).inflate(R.layout.navigation_bar, this, true); 
	}
	
}
