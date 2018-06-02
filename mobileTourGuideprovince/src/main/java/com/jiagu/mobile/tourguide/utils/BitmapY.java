package com.jiagu.mobile.tourguide.utils;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;

/**
 * @author Administrator
 *
 */
public class BitmapY {
	// ����Բ��ͼƬ
	public static Bitmap GetRoundedCornerBitmap(Bitmap bitmap) {
		try {
			Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
					bitmap.getHeight(), Config.ARGB_8888);
			Canvas canvas = new Canvas(output);
			final Paint paint = new Paint();
			final Rect rect = new Rect(0, 0, bitmap.getWidth(),
					bitmap.getHeight());
			final RectF rectF = new RectF(new Rect(0, 0, bitmap.getWidth(),
					bitmap.getHeight()));
			final float roundPx = 30;
			paint.setAntiAlias(true);
			canvas.drawARGB(0, 0, 0, 0);
			paint.setColor(Color.BLACK);
			canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
			paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));

			final Rect src = new Rect(0, 0, bitmap.getWidth(),
					bitmap.getHeight());

			canvas.drawBitmap(bitmap, src, rect, paint);
			return output;
		} catch (Exception e) {
			return bitmap;
		}
	}

	/*
	 * ����Բ��ͼƬ
	 */
	public static Bitmap makeRoundCorner(Bitmap bitmap) {
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		int left = 0, top = 0, right = width, bottom = height;
		float roundPx = height / 2;
		if (width > height) {
			left = (width - height) / 2;
			top = 0;
			right = left + height;
			bottom = height;
		} else if (height > width) {
			left = 0;
			top = (height - width) / 2;
			right = width;
			bottom = top + width;
			roundPx = width / 2;
		}
		Log.i("111", "ps:" + left + ", " + top + ", " + right + ", " + bottom);

		Bitmap output = Bitmap.createBitmap(width, height,
				Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(output);
		int color = 0xff424242;
		Paint paint = new Paint();
		Rect rect = new Rect(left, top, right, bottom);
		RectF rectF = new RectF(rect);

		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
		paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);
		return output;
	}

	/*
	 * �����ٽ����£� ����ͼƬ�ǳ����Σ�����ͼƬ�� ���� �϶�����һ��ҪС����һ�ߣ�Ҫ���������ξ�����С��һ��Ϊ��׼���������ж�λ��һ�ߵ���ʾ��Χ
	 * ����Բ�ǵİ뾶���������ο��һ�룬�ù� CSS �ľ�֪������Բ�ܷ��� border-radius��Ϊ 50% �Ϳ����ˣ�����һ������
	 * android �� UI ����̫������ ���λ���Բ�ǵĴ��룺
	 */

	public static Bitmap makeRoundCorner(Bitmap bitmap, int px) {
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		Bitmap output = Bitmap.createBitmap(width, height,
				Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(output);
		int color = 0xff424242;
		Paint paint = new Paint();
		Rect rect = new Rect(0, 0, width, height);
		RectF rectF = new RectF(rect);
		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, px, px, paint);
		paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);
		return output;
	}
}
