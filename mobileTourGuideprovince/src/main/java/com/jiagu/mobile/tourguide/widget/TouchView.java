package com.jiagu.mobile.tourguide.widget;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.jiagu.mobile.tourguide.R;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Environment;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout.LayoutParams;

public class TouchView extends View {
	private Bitmap mBitmap;
	private Bitmap bitmap;
	private Canvas mCanvas;
	private Path mPath;
	private DisplayMetrics dm;
	private Paint mBitmapPaint;// �����Ļ���
	private Paint mPaint;// ��ʵ�Ļ���
	private float mX, mY;// ��ʱ������

	private boolean isPaint = false;
	private LayoutParams lp;

	// ����Path·���ļ���,��List������ģ��ջ�����ں��˲���
	private static List<DrawPath> savePath;

	// ����Path·���ļ���,��List������ģ��ջ,����ǰ������
	private static List<DrawPath> canclePath;

	// ��¼Path·���Ķ���
	private DrawPath dp;

	private int screenWidth, screenHeight;// ��Ļ�L��

	private class DrawPath {
		public Path path;// ·��
		public Paint paint;// ����
	}

	// ������ɫ
	// public static int color = Color.RED;
	public static int color = Color.parseColor("#fe0000");
	public static int srokeWidth = 3;

	/**
	 * �õ�����
	 * 
	 * @return
	 */
	public Paint getPaint() {
		return mPaint;
	}

	/**
	 * ���û���
	 * 
	 * @param mPaint
	 */
	public void setPaint(Paint mPaint) {
		this.mPaint = mPaint;
	}

	private void init(int w, int h) {

		lp = new LayoutParams(w,h);
		this.setLayoutParams(lp);
		
		screenWidth = w;
		screenHeight = h;
		mBitmap = Bitmap.createBitmap(screenWidth, screenHeight,
				Bitmap.Config.ARGB_8888);
		// ����һ��һ�λ��Ƴ�����ͼ��
		mCanvas = new Canvas(mBitmap);
		mBitmapPaint = new Paint(Paint.DITHER_FLAG);
		mCanvas.drawBitmap(bitmap, 0, 0, mBitmapPaint);
		mPaint = new Paint();
		mPaint.setAntiAlias(true);
		mPaint.setStyle(Paint.Style.STROKE);
		mPaint.setStrokeJoin(Paint.Join.ROUND);// �������Ե
		mPaint.setStrokeCap(Paint.Cap.ROUND);// ��״
		mPaint.setStrokeWidth(srokeWidth);// ���ʿ��
		mPaint.setColor(color);
		savePath = new ArrayList<DrawPath>();
		canclePath = new ArrayList<DrawPath>();
	}

	private void initPaint() {
		Log.i("�߳�", "-------->>");
		mPaint = new Paint();
		mPaint.setAntiAlias(true);
		mPaint.setStyle(Paint.Style.STROKE);
		mPaint.setStrokeJoin(Paint.Join.ROUND);// �������Ե
		mPaint.setStrokeCap(Paint.Cap.ROUND);// ��״
		mPaint.setStrokeWidth(srokeWidth);// ���ʿ��
		mPaint.setColor(color);
	}

	public void setPaintColor(int color) {
		this.color = color;
		if (mPaint != null)
			mPaint.setColor(color);
	}

	public void setPaintStrokeWidth(int strokeWidth) {
		srokeWidth = strokeWidth;
		if (mPaint != null)
			mPaint.setStrokeWidth(srokeWidth);
	}

	public void setBitmap(Bitmap bitmap) {
		this.bitmap = bitmap;
		init(bitmap.getWidth(), bitmap.getHeight());
	}

	public TouchView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		dm = new DisplayMetrics();
		((Activity) context).getWindowManager().getDefaultDisplay()
				.getMetrics(dm);
		if (bitmap != null) {
			bitmap.recycle();
			mBitmap.recycle();
			bitmap = null;
			mBitmap = null;
		}
		Resources resources = getResources();
		bitmap = BitmapFactory.decodeResource(resources,
				R.drawable.touming_icon);
		init(bitmap.getWidth(), bitmap.getHeight());
	}

	public TouchView(Context context, AttributeSet attrs) {
		super(context, attrs);
		dm = new DisplayMetrics();
		((Activity) context).getWindowManager().getDefaultDisplay()
				.getMetrics(dm);
		if (bitmap != null) {
			bitmap.recycle();
			mBitmap.recycle();
			bitmap = null;
			mBitmap = null;
		}
		Resources resources = getResources();
		bitmap = BitmapFactory.decodeResource(resources,
				R.drawable.touming_icon);
		init(bitmap.getWidth(), bitmap.getHeight());
	}

	public TouchView(Context context, int w, int h) {
		super(context);
		dm = new DisplayMetrics();
		((Activity) context).getWindowManager().getDefaultDisplay()
				.getMetrics(dm);
		if (bitmap != null) {
			bitmap.recycle();
			mBitmap.recycle();
			bitmap = null;
			mBitmap = null;
		}
		Resources resources = getResources();
		bitmap = BitmapFactory.decodeResource(resources,
				R.drawable.touming_icon);
		init(bitmap.getWidth(), bitmap.getHeight());
	}

	@Override
	public void onDraw(Canvas canvas) {
		try {
			// ������ɫ��������ɫӦ����
			// canvas.drawColor(color);
			// ��ǰ���Ѿ���������ʾ����
			canvas.drawBitmap(bitmap, 0, 0, mBitmapPaint);
			canvas.drawBitmap(mBitmap, 0, 0, mBitmapPaint);
			if (mPath != null) {
				// ʵʱ����ʾ
				canvas.drawPath(mPath, mPaint);
			}
		} catch (Exception e) {
			System.out
					.println("TouchView  -> onDraw() Canvas: trying to use a recycled bitmap");
		}
	}

	/**
	 * �����ĺ���˼����ǽ�������գ� ������������Path·�����һ���Ƴ����� ���½�·�����ڻ������档
	 */
	public int undo() {
		Log.i("�߳�", "________---");
		fn(bitmap.getWidth(), bitmap.getHeight());
		// ��ջ������������ͼƬ�б����Ļ�����ʹ����������³�ʼ���ķ������ø÷����Ὣ������յ���
		if (savePath != null && savePath.size() > 0) {
			// �Ƴ����һ��path,�൱�ڳ�ջ����
			savePath.remove(savePath.size() - 1);

			Iterator<DrawPath> iter = savePath.iterator(); // �ظ�����
			while (iter.hasNext()) {
				DrawPath drawPath = iter.next();
				mCanvas.drawPath(drawPath.path, drawPath.paint);
			}
			invalidate();// ˢ��
		} else {
			return -1;
		}
		return savePath.size();
	}

	/**
	 * �����ĺ���˼����ǽ�������·�����浽����һ����������(ջ)�� Ȼ���redo�ļ�������ȡ����˶��� ���ڻ������漴�ɡ�
	 */
	public int redo() {
		// ��������㶮�˵Ļ����Ǿ����������ɡ�
		if (canclePath.size() < 1)
			return canclePath.size();
		mBitmap = Bitmap.createBitmap(screenWidth, screenHeight,
				Bitmap.Config.ARGB_8888);
		mCanvas.setBitmap(mBitmap);// �������û������൱����ջ���
		// ��ջ������������ͼƬ�б����Ļ�����ʹ����������³�ʼ���ķ������ø÷����Ὣ������յ���
		if (canclePath != null && canclePath.size() > 0) {
			// �Ƴ����һ��path,�൱�ڳ�ջ����
			DrawPath dPath = canclePath.get(canclePath.size() - 1);
			savePath.add(dPath);
			canclePath.remove(canclePath.size() - 1);

			Iterator<DrawPath> iter = savePath.iterator();
			while (iter.hasNext()) {
				DrawPath drawPath = iter.next();
				mCanvas.drawPath(drawPath.path, drawPath.paint);
			}
			invalidate();// ˢ��
		}
		return canclePath.size();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (isPaint) {
			float x = event.getX();
			float y = event.getY();

			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:

				initPaint();
				// ������һ������
				canclePath = new ArrayList<DrawPath>();
				// ÿ��down��ȥ����newһ��Path
				mPath = new Path();
				// ÿһ�μ�¼��·�������ǲ�һ����
				dp = new DrawPath();
				touch_start(x, y);
				invalidate();
				break;
			case MotionEvent.ACTION_MOVE:
				touch_move(x, y);
				invalidate();
				break;
			case MotionEvent.ACTION_UP:
				touch_up();
				invalidate();
				break;
			}
		}
		return true;
	}

	public void setIsPaint(boolean is) {
		isPaint = is;
	}

	private void touch_start(float x, float y) {
		mPath.moveTo(x, y);
		mX = x;
		mY = y;
	}

	private void touch_move(float x, float y) {
		float dx = Math.abs(x - mX);
		float dy = Math.abs(mY - y);
		if (dx >= 4 || dy >= 4) {
			// ��x1,y1��x2,y2��һ�����������ߣ���ƽ��(ֱ����mPath.lineToҲ�ǿ��Ե�)
			mPath.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
			mX = x;
			mY = y;
		}
	}

	private void touch_up() {
		mPath.lineTo(mX, mY);
		mCanvas.drawPath(mPath, mPaint);
		// ��һ��������·����������(�൱����ջ����)
		dp.path = mPath;
		dp.paint = mPaint;
		savePath.add(dp);
		mPath = null;// �����ÿ�
		// mPaint=null;
	}

	public String saveImage(String filename) {
		String ph = Environment.getExternalStorageDirectory().getPath() + "/DCIM/TouristDIY/";
		File folder = new File(ph);
		try {
			if(!folder.exists()){
				folder.mkdirs();
			}
			String filePath = ph + filename + ".jpg";
			FileOutputStream out = new FileOutputStream(filePath);
			mBitmap.compress(CompressFormat.JPEG, 100, out);
			out.flush();
			out.close();
			return filePath;
		} catch (Exception e) {
		}
		return "";
	}

	Bitmap bm1;
	Bitmap bp;
	Bitmap bitmap2 = null;

	public Bitmap BitmapAmplification(String path) {
		if (bitmap2 != null) {
			bitmap2.recycle();
			bitmap2 = null;
			if (bitmap != null) {
				bitmap = null;
			}
			if (bm1 != null) {
				bm1 = null;
			}

			if (bp != null) {
				bp = null;
			}
		}
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		bm1 = BitmapFactory.decodeFile(path, options);
		int scu = (int) options.outWidth / 550;
		if (scu <= 0) {// С����
			scu = 1;
		}
		options.inSampleSize = scu;
		Log.v("nnn", "scu" + scu);
		options.inJustDecodeBounds = false;
		bitmap2 = BitmapFactory.decodeFile(path, options);
		if (bitmap2 != null) {
			int w = bitmap2.getWidth(); // �õ�ͼƬ�Ŀ��
			int h = bitmap2.getHeight();// �õ�ͼƬ�ĸ߶�
			Log.v("nnn", "��" + w + "��" + h);
			float ww = ((float) dm.widthPixels) / w;
			float hh = ((float) dm.heightPixels) / h;
			Matrix matrix = new Matrix();
			matrix.postScale(ww, hh);
			Log.v("nnn", "����www");
			Bitmap bp = Bitmap.createBitmap(bitmap2, 0, 0, w, h, matrix, true);
			if (!bitmap2.isRecycled() && bitmap2 != null) {
				bitmap2.recycle();
				bitmap2 = null;
				Log.v("nnn", "����");
				System.gc();
			}
			return bp;
		}
		return null;
	}

	public void fn(int w, int h) {
		screenWidth = w;
		screenHeight = h;
		mBitmap = Bitmap.createBitmap(screenWidth, screenHeight,
				Bitmap.Config.ARGB_8888);
		// ����һ��һ�λ��Ƴ�����ͼ��
		mCanvas = new Canvas(mBitmap);
		mBitmapPaint = new Paint(Paint.DITHER_FLAG);
		mCanvas.drawBitmap(bitmap, 0, 0, mBitmapPaint);
		mPaint = new Paint();
		mPaint.setAntiAlias(true);
		mPaint.setStyle(Paint.Style.STROKE);
		mPaint.setStrokeJoin(Paint.Join.ROUND);// �������Ե
		mPaint.setStrokeCap(Paint.Cap.ROUND);// ��״
		mPaint.setStrokeWidth(srokeWidth);// ���ʿ��
		mPaint.setColor(color);
	}

	int k = 7;

	public void UPdataColor(int i) {
		if (i == 1) {
			if (mPaint == null) {
				initPaint();
			}

		} else if (i == 2) {

		} else if (i == 3) {

		} else if (i == 4) {
			k = k + 3;
			mPaint.setStrokeWidth(k);
		}
	}

}
