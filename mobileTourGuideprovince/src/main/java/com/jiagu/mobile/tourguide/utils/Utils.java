package com.jiagu.mobile.tourguide.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;

public class Utils {
	public static final boolean DEBUG = true;
	public static boolean isNullOrEmpty(String text) {
		if (text == null)
			return true;
		if (text.length() <= 0)
			return true;

		return false;
	}

	public static boolean isMobileNO(String mobiles) {
		Pattern p = Pattern
				.compile("^((13[0-9])|(15[^4,\\D])|(18[0-9]))\\d{8}$");
		Matcher m = p.matcher(mobiles);
		return m.matches();
	}

	public static String getVersionName(Context cotenxt) {
		try {
			PackageManager packageManager = cotenxt.getPackageManager();
			PackageInfo packInfo = packageManager.getPackageInfo(
					cotenxt.getPackageName(), 0);
			String version = packInfo.versionName;
			return version;
		} catch (NameNotFoundException e) {
			return "";
		}
	}
}
