package net.csdn.client.sd.widget;

import net.csdn.client.sd.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

public class NavigationLayout extends LinearLayout {
	private View a;

	public NavigationLayout(Context paramContext) {
		super(paramContext, null);
	}

	public NavigationLayout(Context cxt, AttributeSet paramAttributeSet) {
		super(cxt, paramAttributeSet);
		setOrientation(1);
		View localView1 = View.inflate(cxt, R.layout.navigation_bar, null);
		this.a = localView1;
		View localView2 = this.a;
		addView(localView2);
	}
}
