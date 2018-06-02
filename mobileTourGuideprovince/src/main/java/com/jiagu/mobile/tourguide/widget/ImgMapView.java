package com.jiagu.mobile.tourguide.widget;

import java.math.BigDecimal;
import java.util.ArrayList;

import com.jiagu.mobile.tourguide.R;
import com.jiagu.mobile.tourguide.utils.FileTools;
import com.jiagu.mobile.tourguide.utils.ImageTools;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.Paint.Align;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.FloatMath;
import android.view.MotionEvent;
import android.widget.ImageView;


/** 
* @ClassName: ImgMapView 
* @Description: 
* @author zz
* @date 2015年1月15日 下午12:36:18 
* @version 1
* @company 西安甲骨企业文化传播有限公司  
*/ 
public class ImgMapView extends ImageView {

	private static final int MARKAWIDTH = 63; // marka点的宽度
	private static final int MARKAHEIGHT = 71; // marka点的高度
	private static final int MARKAPADDING = 8; // marka的padding值

	private PointF startPoint = new PointF();
	private PointF startMarkaPoint = new PointF();

	private int bmpWidth;
	private int bmpHeight;
	private double zoomWidth;

	private int mode = 0;//用于标记模式
	private static final int DRAG = 1;// 拖动
	private static final int ZOOM = 2;// 放大
	private float startDis = 0;
	private double startMarkaDis = 0;

	 /** 两个手指的中间点 */  
    private PointF midPoint; 

	private boolean isChange = false; //用于标记图片是否变化
	private float dx, dy; // x轴 和 y轴移动距离

	private Matrix currentMaritx = new Matrix();
	private static Matrix matrix = new Matrix();

	private static ArrayList<Coordinate> Coordinates = new ArrayList<ImgMapView.Coordinate>();
	
	private double xOffset = 1;
	private double yOffset = 1;

	public void setOffset(double xOffset, double yOffset) {
		this.xOffset = xOffset;
		this.yOffset = yOffset;
	}

	private Bitmap marka;
	private Bitmap click;
	private Paint markaPaint;
	private boolean markaClickable = false;
	private Handler mHandler;

	private OnMarkaClickListener mMarkaClickListener;
	
	
	private ArrayList<MotionEvent> mEventList = null;//触屏事件记录列表
	
	
	boolean isSetImage = false;

	public ImgMapView(Context context) {
		super(context);
		this.setClickable(true);

		mHandler = new Handler();
		markaPaint = new Paint(Paint.DITHER_FLAG);
		marka = BitmapFactory.decodeResource(getResources(), R.drawable.mark);
		click = BitmapFactory.decodeResource(getResources(), R.drawable.activemark);
		
		mEventList = new ArrayList<MotionEvent>();
	}

	public ImgMapView(Context context, AttributeSet attrs) {
		super(context, attrs, 0);
		this.setClickable(true);

		mHandler = new Handler();
		markaPaint = new Paint(Paint.DITHER_FLAG);
		marka = BitmapFactory.decodeResource(getResources(), R.drawable.mark);
		click = BitmapFactory.decodeResource(getResources(), R.drawable.activemark);
		
		mEventList = new ArrayList<MotionEvent>();
	}
	
	public void clearUnbindCoordinates(){
		for (int i = 0; i < Coordinates.size(); i++) {
			Coordinate c = Coordinates.get(i);
			if (c.isBind == false) {
				Coordinates.remove(i);
				i--;
			}
		}
		invalidate();
	}

	public void deleteCoordinateById(String id) {
		for (int i = 0; i < Coordinates.size(); i++) {
			Coordinate c = Coordinates.get(i);
			if (id.equals(c.id)) {
				Coordinates.remove(i);		
				invalidate();
				break;
			}
		}
	}

	public void deleteCoordinateByXY(float x, float y) {
		for (int i = 0; i < Coordinates.size(); i++) {
			Coordinate c = Coordinates.get(i);
			if (c.imgX == x && c.imgY == y) {
				Coordinates.remove(i);
				invalidate();
				break;
			}
		}
	}
	
	public static void deleteCoordinateByXYFromList(float x, float y){
		int iFlag = 0;
		if (Coordinates == null){
			FileTools.writeLog("scenic.txt", "Coordinates == null");
		}
		else{
			for (int i = 0; i < Coordinates.size(); i++) {
				Coordinate c = Coordinates.get(i);
				if (c.imgX == x && c.imgY == y) {
					Coordinates.remove(i);
					iFlag = 1;
					FileTools.writeLog("scenic.txt", "delete success from Coordinates !");
					break;
				}
			}
		}
		
		if (iFlag == 0){
			FileTools.writeLog("scenic.txt", "delete failure from Coordinates !");
		}
	}
	
	public static int getCoordinatesSize(){
		return Coordinates.size();
	}

	public void setCoordinateByXY(String id, float x, float y) {
		for (int i = 0; i < Coordinates.size(); i++) {
			Coordinate c = Coordinates.get(i);
			if (c.imgX == x && c.imgY == y) {
				Coordinates.get(i).id = id;
				break;
			}
		}
	}

	private void calcZoom(double scale) {
		float markaWidthOffset = (float) (MARKAWIDTH / 2);
		for (Coordinate c : Coordinates) {
			c.x = ((float) (c.x * scale));
			c.y = ((float) (c.y * scale));
			invalidate();
		}
	}
	
	private void calcZoom(double scale, PointF point){
		calcCoordinate(this);
	}
	
	public static void calcCoordinate(ImageView view){
		if (ImageStatus.getStatus(view) == true){
			for (Coordinate c : Coordinates) {
				BigDecimal imgX = new BigDecimal(String.valueOf(c.imgX));
				imgX = imgX.multiply(ImageStatus.zoom);
				
				BigDecimal imgY = new BigDecimal(String.valueOf(c.imgY));
				imgY = imgY.multiply(ImageStatus.zoom);
				
				c.x = ImageStatus.left.add(imgX).doubleValue();
				c.y = ImageStatus.top.add(imgY).doubleValue();
							
				view.invalidate();
			}
		}	
	}

	@Override
	public void setImageBitmap(Bitmap bm) {
		//FileTools.writeLog("realtime.txt", "before invoke setImageBitmap");
		
		matrix = new Matrix();
		Bitmap smallBitmap = ImageTools.zoomBitmap(bm,
				(int) (bm.getWidth() * xOffset),
				(int) (bm.getHeight() * yOffset));
		bmpHeight = smallBitmap.getHeight();
		bmpWidth = smallBitmap.getWidth();
		zoomWidth = bmpWidth;
		
		super.setImageBitmap(smallBitmap);
		
		//FileTools.writeLog("realtime.txt", "after invoke setImageBitmap");
		
		isSetImage = true;
		
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		this.setImageMatrix(matrix);
	
		for (Coordinate coordinates : Coordinates) {
			Matrix matrix = new Matrix();
			
			matrix.setTranslate((float)(coordinates.x - (coordinates.newBitmapW/ 2 )), (float)(coordinates.y- (coordinates.newBitmapH)));

			if (coordinates.isClick == true) {
				//canvas.drawBitmap(click, matrix, markaPaint);
				
				//canvas.drawBitmap(click, matrix, markaPaint);
				
				if (null == coordinates.clickbmp)
				{
					coordinates.clickbmp = createDrawable(coordinates.name, R.drawable.baiduactivemark,coordinates);
				}
				
				canvas.drawBitmap(coordinates.clickbmp, matrix, markaPaint);
			} 
			else {
				//canvas.drawBitmap(marka, matrix, markaPaint);
				
				//canvas.drawBitmap(marka, matrix, markaPaint);
				
				if (null == coordinates.bmp)
				{
					coordinates.bmp = createDrawable(coordinates.name, R.drawable.baidumark,coordinates);
				}
				
				canvas.drawBitmap(coordinates.bmp, matrix, markaPaint);
			}
		}
	}
	
	private Bitmap createDrawable(String title, int resourceid,Coordinate coordinates) {  		
		DisplayMetrics dm = this.getResources().getDisplayMetrics();
		float defaultfontsize = 12;
		float fontsize = defaultfontsize*dm.density;
		
		Paint textPaint = new Paint(Paint.ANTI_ALIAS_FLAG  | Paint.DEV_KERN_TEXT_FLAG); 
		textPaint.setTextSize(fontsize);
		textPaint.setTypeface(Typeface.DEFAULT_BOLD); // 采用默认的宽度  
		textPaint.setColor(Color.WHITE); 
		textPaint.setTextAlign(Align.LEFT);
		
		float titlewidth = textPaint.measureText(title);
				
		Bitmap bmp  = BitmapFactory.decodeResource(getResources(), resourceid);
		Matrix matrix = new Matrix();   //矩阵，用于图片比例缩放  
		matrix.postScale((float)(titlewidth*2)/bmp.getWidth(), (float)1);    //设置高宽比例（三维矩阵） 
		Bitmap newBmp = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrix, true);
		
		Canvas canvas = new Canvas(newBmp); 
	
		Paint paint = new Paint(); // 建立画笔  
        paint.setDither(true);  
        paint.setFilterBitmap(true);  
        
        Rect src = new Rect(0, 0, newBmp.getWidth(), newBmp.getHeight());  
        Rect dst = new Rect(0, 0, newBmp.getWidth(), newBmp.getHeight());  
        
        canvas.drawBitmap(newBmp, src, dst, paint);
        
        canvas.drawText(title, newBmp.getWidth()/2-titlewidth/2, newBmp.getHeight()/2, textPaint); 
      
        canvas.save(Canvas.ALL_SAVE_FLAG);  
        canvas.restore();  
        coordinates.newBitmapW = newBmp.getWidth();
        coordinates.newBitmapH = newBmp.getHeight();
        return newBmp;
    }  

	private void followScroll(float x, float y) {
		for (Coordinate c : Coordinates) {
			c.x += x;
			c.y += y;
		}
		invalidate();
	};
	
	private void followScroll(){		
		BigDecimal startleft = new BigDecimal(String.valueOf(ImageStatus.left.doubleValue()));
		BigDecimal starttop = new BigDecimal(String.valueOf(ImageStatus.top.doubleValue()));
		
		ImageStatus.getStatus(this);
		
		BigDecimal endleft = new  BigDecimal(String.valueOf(ImageStatus.left.doubleValue()));
		BigDecimal endtop = new BigDecimal(String.valueOf(ImageStatus.top.doubleValue()));
		
		for (Coordinate c : Coordinates) {
			BigDecimal cx = new BigDecimal(String.valueOf(c.x));
			BigDecimal cy = new BigDecimal(String.valueOf(c.y));
			
			c.x = cx.add(endleft).subtract(startleft).doubleValue();
			c.y = cy.add(endtop).subtract(starttop).doubleValue();
		}
	}

	public void setMarkaClickable(boolean markaclickable) {
		this.markaClickable = markaclickable;
	}

	public void setCoordinate(ArrayList<Coordinate> Coordinates) {		
		if (ImgMapView.Coordinates.size() > 0){
			if (Coordinates.size()>0){
				ArrayList<Coordinate> bindCoordinates = new ArrayList<Coordinate>();
				
				for (int i=0; i<ImgMapView.Coordinates.size(); i++){
					for (int j=0; j<Coordinates.size(); j++){
						if (ImgMapView.Coordinates.get(i).imgX == Coordinates.get(j).imgX
								 && ImgMapView.Coordinates.get(i).imgY == Coordinates.get(j).imgY){
							ImgMapView.Coordinates.get(i).isBind = true;
							ImgMapView.Coordinates.get(i).id = Coordinates.get(j).id;
							bindCoordinates.add(ImgMapView.Coordinates.get(i));							
							ImgMapView.Coordinates.remove(i);
							Coordinates.remove(j);
							i--;
							break;
						}
					}
				}
				
				for (int i=0; i<ImgMapView.Coordinates.size(); i++){
					if (ImgMapView.Coordinates.get(i).isBind == true){
						ImgMapView.Coordinates.remove(i);
						i--;
					}
				}
				
				ImgMapView.Coordinates.addAll(bindCoordinates);
				
			}
		}
		else {
			ImgMapView.Coordinates.addAll(Coordinates);
		}
		invalidate();
	}
	
	public void clearCoordinates(){
		ImgMapView.Coordinates.clear();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		
		if (isSetImage == false){
			return true;
		}
		
		switch (event.getAction() & MotionEvent.ACTION_MASK) {
		case MotionEvent.ACTION_DOWN:
			MotionEvent ev = event.obtain(event);//记录手指按下时间
			mEventList.add(ev);
			mode = DRAG;
			isChange = false;
			currentMaritx.set(this.getImageMatrix()); // 记录ImageView当期的移动位置
			startPoint.set((float) (event.getX() * xOffset),(float) (event.getY() * yOffset));
			startMarkaPoint.set((float) (event.getX() * xOffset),(float) (event.getY() * yOffset));
			break;
		case MotionEvent.ACTION_UP:
			
			mMarkaClickListener.onPlayer();
			
			mode = 0;
			if (!isChange) {
				if (!markaClickable) {	
					//FileTools.writeLog("realtime.txt", "!markaClickable");
					
					if (mEventList!=null && mEventList.size()>0
							&& Math.abs(mEventList.get(0).getX()-event.getX())<2 
							&& Math.abs(mEventList.get(0).getY()-event.getY())<2
							&& event.getEventTime()-mEventList.get(0).getEventTime()>20){
					
						ImageStatus.getStatus(this);
					    
					    Coordinate c = new Coordinate(mEventList.get(0).getX(), mEventList.get(0).getY());
					   
						c.imgX = (float)((c.x - ImageStatus.left.doubleValue())/ImageStatus.zoom.doubleValue());
						c.imgY = (float)((c.y - ImageStatus.top.doubleValue())/ImageStatus.zoom.doubleValue());
													
						if ((c.imgX>0 && c.imgX*ImageStatus.zoom.doubleValue()<ImageStatus.width.doubleValue()) && (c.imgY>0&& c.imgY*ImageStatus.zoom.doubleValue()<ImageStatus.height.doubleValue())){
							Coordinates.add(c);
							invalidate();
						}
					}
				} 
				else {
					//FileTools.writeLog("realtime.txt", "markaClickable");
					
					//获取标记点
					final Coordinate coordinate = validateIsClickMarka(event.getX(), event.getY());
					invalidate();
					
					if (coordinate != null) {
						if (mMarkaClickListener != null) {
							mHandler.postDelayed(new Runnable() {
								@Override
								public void run() {
									mMarkaClickListener.onMarkaClick(coordinate);
								}
							}, 500);
						}
					}
				}
			}
			mEventList.clear();
			break;
		case MotionEvent.ACTION_MOVE:
			if (mode == DRAG) {
				//FileTools.writeLog("scenic.txt", "ACTION_MOVE mode == DRAG");
				
				float currentX = (float) (event.getX() * xOffset);
				float currentY = (float) (event.getY() * yOffset);
				dx = currentX - startPoint.x;//// x轴移动距离
				dy = currentY - startPoint.y;// y轴移动距离
				matrix.set(currentMaritx);//在当前的位置基础上移动
				matrix.postTranslate(dx, dy);

				//followScroll(event.getX()-startMarkaPoint.x, event.getY()-startMarkaPoint.y);
				followScroll();
				startMarkaPoint.set(currentX, currentY);
				if (dx > 5f || dy > 5f) {
					isChange = true;
				}
			} else if (mode == ZOOM) {//图片放大事件
				//FileTools.writeLog("scenic.txt", "ACTION_MOVE mode == ZOOM");
				
				isChange = true;
				float endDis = distance(event);// 结束距离
				if (endDis > 10f) {
					//FileTools.writeLog("scenic.txt", "endDis="+endDis+" startDis="+startDis);
					float scale = endDis / startDis;// 放大倍数
					
					if (ImageStatus.zoom.doubleValue() < 0.001 && ImageStatus.zoom.doubleValue()>0.001){
						FileTools.writeLog("scenic.txt", "before ImageStatus.zoom="+ImageStatus.zoom);
						ImageStatus.getStatus(this);
						FileTools.writeLog("scenic.txt", "after ImageStatus.zoom="+ImageStatus.zoom);
					}
					
					ImageStatus.getStatus(this);
					
					if ((scale * ImageStatus.zoom.doubleValue()>4) || (scale*ImageStatus.zoom.doubleValue()<0.25)){
						//FileTools.writeLog("scenic.txt", "return true");
						return true;
					}
					
					matrix.set(currentMaritx);
					if (scale*ImageStatus.zoom.doubleValue()>4){
						matrix.postScale(4/ImageStatus.zoom.floatValue(), 4/ImageStatus.zoom.floatValue(), midPoint.x,midPoint.y);	
					}
					else if (scale*ImageStatus.zoom.doubleValue()<0.25){
						matrix.postScale((float)(0.25/ImageStatus.zoom.doubleValue()), (float)(0.25/ImageStatus.zoom.doubleValue()), midPoint.x,midPoint.y);
					}
					else{
						matrix.postScale(scale, scale, midPoint.x,midPoint.y); 
					}
					
					calcZoom(ImageStatus.zoom.doubleValue(), midPoint);		
				}
			}
			break;
			// 有手指离开屏幕，但屏幕还有触点(手指)
		case MotionEvent.ACTION_POINTER_UP:
			mode = 0;
			
			
			
			break;
			// 当屏幕上已经有触点（手指）,再有一个手指压下屏幕
		case MotionEvent.ACTION_POINTER_DOWN:
			//FileTools.writeLog("scenic.txt", "ACTION_POINTER_DOWN");
			mode = ZOOM;
			startDis = distance(event);
			startMarkaDis = distance(event);
			
			midPoint = mid(event);  
			
			if (startDis > 10f) {// 避免手指上有两个茧
				currentMaritx.set(this.getImageMatrix());// 记录当前的缩放倍数
			}
			break;
		}
	
		this.setImageMatrix(matrix);
		return true;
	}

	//确认点击的标点是否为已点击的标点
	private Coordinate validateIsClickMarka(float eventX, float eventY) {
		Coordinate result = null;
		float markaWidthOffset = (float) (MARKAWIDTH / 2 * xOffset);
		float markaHeight = (float) (MARKAHEIGHT * xOffset);
		
		int nStart = 0;
		for (int i = 0; i < Coordinates.size(); i++) {
			Coordinate c = Coordinates.get(i);
//			float leftX = (float)((c.x - markaWidthOffset) - MARKAPADDING);
//			float rightX = (float)((c.x + markaWidthOffset) + MARKAPADDING);
//			float bottomY = (float)((c.y - markaHeight) - MARKAPADDING);
//			float topY = (float)(c.y + MARKAPADDING);
			
			float leftX = (float)((c.x - markaWidthOffset) - MARKAPADDING);
			float rightX = (float)((c.x + c.bmp.getWidth()) + MARKAPADDING);
			float bottomY = (float)((c.y - markaHeight) - MARKAPADDING);
			float topY = (float)(c.y + MARKAPADDING);
			
			if (eventX > leftX && eventX < rightX && eventY > bottomY&& eventY < topY) {
				c.isClick = true;
				nStart = i;
				result = c;
				break;
			} 
			else if (c.id == null || c.id.length()==0){
				c.isClick = false;
			}
		}
		
		for (int i=nStart+1; i<Coordinates.size(); i++){
			Coordinates.get(i).isClick = false;
		}
		
		return result;
	}

	public static class Coordinate {
		public String id;
		public float imgX;
		public float imgY;
		public double x;
		public double y;
		public boolean isClick = false;
		public boolean isBind = false;
		public String voiceURL;
		public String imageURL;
		public String name;
		public Bitmap bmp;
		public Bitmap clickbmp;
		public int newBitmapW;//记录新建bitmap的宽度
		public int newBitmapH;//记录新建bitmap的高度
		
		public Coordinate() {
			x = 0;
			y = 0;
			isClick = false;
			isBind = false;
			imgX = 0;
			imgY = 0;
			id = null;	
		}

		public Coordinate(float x, float y) {
			this.x = x;
			this.y = y;
			isClick = false;
			isBind = false;
			imgX = 0;
			imgY = 0;
			id = null;
		}
		
		public Coordinate(String id, float imgX, float imgY, boolean isClick, boolean isBind){
			this.id = new String(id);
			this.imgX = imgX;
			this.imgY = imgY;
			this.x = 0;
			this.y = 0;
			this.isClick = isClick;
			this.isBind = isBind;
		}
	}

	public void setOnMarkaClickListener(OnMarkaClickListener markaclick) {
		this.mMarkaClickListener = markaclick;
	}

	public interface OnMarkaClickListener {
		public void onMarkaClick(Coordinate coordinate);
		public void onDeleteAttract(String id);
		public void onPlayer();
	};

	/**
	 * 两点之间的距离
	 * 
	 * @param event
	 * @return
	 */
	private static float distance(MotionEvent event) {
		// 两根线的距离
		float dx = event.getX(1) - event.getX(0);
		float dy = event.getY(1) - event.getY(0);
		return FloatMath.sqrt(dx * dx + dy * dy);
	}
	
	/** 计算两个手指间的中间点 */  
    private PointF mid(MotionEvent event) {  
        float midX = (event.getX(1) + event.getX(0)) / 2;  
        float midY = (event.getY(1) + event.getY(0)) / 2;  
        return new PointF(midX, midY);  
    }  

    private static class ImageStatus{
    	public static BigDecimal left = new BigDecimal("0");
    	public static BigDecimal right = new BigDecimal("0");
    	public static BigDecimal top = new BigDecimal("0");
    	public static BigDecimal bottom = new BigDecimal("0");
    	public static BigDecimal zoom = new BigDecimal("1");
    	public static BigDecimal width = new BigDecimal("0");
    	public static BigDecimal height = new BigDecimal("0");	
    	public static BigDecimal imageWidth = new BigDecimal("0");
    	public static BigDecimal imageHeight = new BigDecimal("0");
    	public static boolean isChange;
    	
    	static boolean getStatus(ImageView view){
    		if (view != null && view.getDrawable() != null){
    				Rect rect = view.getDrawable().getBounds(); 
        	    	
        			float[] values = new float[9]; 
        			Matrix matrix = view.getImageMatrix(); 
        			matrix.getValues(values); 
        			
        			int[] location = new int[2];
        			view.getLocationOnScreen(location);
        			
        			imageWidth = new BigDecimal(String.valueOf(rect.width()));
        			imageHeight = new BigDecimal(String.valueOf(rect.height()));
        			
        			zoom = new BigDecimal(String.valueOf(values[0]));
        			
        			left = new BigDecimal(String.valueOf(values[2]));
        			top = new BigDecimal(String.valueOf(values[5]));
        			
        			width = imageWidth.multiply(zoom);
        			height = imageHeight.multiply(zoom);
        			
        			right = left.add(width);
        			bottom = top.add(height);
        			
        			return true;
    			}
    		return false;
    	}
    	
    	static void mapPoint(float[] dst, float[] src, ImageView view){
    		Matrix matrix = view.getImageMatrix(); 
    		
    		matrix.mapPoints(dst, src);
    	}
    }
}
