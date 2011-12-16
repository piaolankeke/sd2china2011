package net.csdn.client.sd.ui;

import net.csdn.client.sd.R;
import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TabHost;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class MainActivity extends TabActivity implements
		OnCheckedChangeListener {
	/** Called when the activity is first created. */
	private RadioButton[] mRadioButtons;
	public static int mMode = 0;
	private TabHost mHost;
	private RadioGroup localRadioGroup;

	private Intent localIntent0,localIntent1,localIntent2,localIntent3,localIntent4;
	private String[] tabStr = {"home_tab","events_tab","forum_tab","weibo_tab","home_tab"};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tab);

		localIntent0 = new Intent(this, HomeActivity.class);
		localIntent1 = new Intent(this, ScheduleActivity.class);
		localIntent2 = new Intent(this, ForumActivity.class);
		localIntent3 = new Intent(this, WeiboActivity.class);
		localIntent4 = new Intent(this, HomeActivity.class);
		
		try {
			mHost = getTabHost();
			initRadios();
		} catch (Exception e) {
			e.printStackTrace();
		}
		mHost.addTab(mHost
				.newTabSpec(tabStr[0])
				.setIndicator(getText(R.string.tab_0),
						getResources().getDrawable(R.drawable.icon_0_n))
				.setContent(localIntent0));
		mHost.addTab(mHost
				.newTabSpec(tabStr[1])
				.setIndicator(getText(R.string.tab_1),
						getResources().getDrawable(R.drawable.icon_1_n))
				.setContent(localIntent1));
		mHost.addTab(mHost
				.newTabSpec(tabStr[2])
				.setIndicator(getText(R.string.tab_2),
						getResources().getDrawable(R.drawable.icon_2_n))
				.setContent(localIntent2));
		mHost.addTab(mHost
				.newTabSpec(tabStr[3])
				.setIndicator(getText(R.string.tab_3),
						getResources().getDrawable(R.drawable.icon_3_n))
				.setContent(localIntent3));
		mHost.addTab(mHost
				.newTabSpec(tabStr[4])
				.setIndicator(getText(R.string.tab_4),
						getResources().getDrawable(R.drawable.icon_4_n))
				.setContent(localIntent4));
		mHost.setCurrentTabByTag(tabStr[0]);
	}

	protected void onResume() {
		super.onResume();
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		// TODO Auto-generated method stub
		if (isChecked) {
			if (buttonView == this.mRadioButtons[0]) {
				mHost.setCurrentTabByTag("home_tab");
			}
			if (buttonView == this.mRadioButtons[1]) {
				mHost.setCurrentTabByTag("events_tab");
			}
			if (buttonView == this.mRadioButtons[2]) {
				mHost.setCurrentTabByTag("forum_tab");
			}
			if (buttonView == this.mRadioButtons[3]) {
				mHost.setCurrentTabByTag("weibo_tab");
			}
			if (buttonView == this.mRadioButtons[4]) {
				mHost.setCurrentTabByTag("more_tab");
			}
		}
	}

	private void initRadios() {
		localRadioGroup = (RadioGroup) findViewById(R.id.main_radio);
		RadioButton[] arrayOfRadioButton1 = new RadioButton[5];

		int i = 0;
		while (i < 5) {
			String str = "radio_button" + i;
			RadioButton localRadioButton = (RadioButton) localRadioGroup
					.findViewWithTag(str);
			if (i == 0) {
				localRadioButton.setChecked(true);
			}
			arrayOfRadioButton1[i] = localRadioButton;
			arrayOfRadioButton1[i].setOnCheckedChangeListener(this);
			i += 1;
		}
		this.mRadioButtons = arrayOfRadioButton1;
	}
}