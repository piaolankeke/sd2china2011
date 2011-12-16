package net.csdn.client.sd.utils;

import java.util.List;

import net.csdn.client.sd.R;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;

public class UIUtils {

	public static boolean hasInternet(Context ctx) {

		NetworkInfo info = (NetworkInfo) ((ConnectivityManager) ctx
				.getSystemService(Context.CONNECTIVITY_SERVICE))
				.getActiveNetworkInfo();

		if (info == null || !info.isConnected()) {
			return false;
		}
		if (info.isRoaming()) {
			// here is the roaming option you can change it if you want to
			// disable internet while roaming, just return false
			return false;
		}
		return true;
	}

	public static boolean checkGoogleMap(Context ctx) {
		boolean isInstallGMap = false;
		List<PackageInfo> packs = ctx.getPackageManager().getInstalledPackages(
				0);
		for (int i = 0; i < packs.size(); i++) {
			PackageInfo p = packs.get(i);
			if (p.versionName == null) { // system packages
				continue;
			}
			if ("com.google.android.apps.maps".equals(p.packageName)) {
				isInstallGMap = true;
				break;
			}
		}
		return isInstallGMap;
	}

}
