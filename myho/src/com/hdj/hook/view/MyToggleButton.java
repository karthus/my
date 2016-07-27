package com.hdj.hook.view;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class MyToggleButton extends View{

	private ToggleState toggleState = ToggleState.Open;//���ص�״̬
	private Bitmap slideBg;
	private Bitmap switchBg;
	private boolean isSliding = false;
	
	private int currentX;//��ǰ������x����

	/**
	 * ������viewֻ���ڲ����ļ���ʹ�ã�ֻ��Ҫ��д������췽��
	 * @param context
	 * @param attrs
	 */
	public MyToggleButton(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	/**
	 * ������ view��Ҫ��java�����ж�̬new�������ߵ���������췽��
	 * @param context
	 */
	public MyToggleButton(Context context) {
		super(context);
	}
	
	public enum ToggleState{
		Open,Close
	}

	/**
	 * ���û�����ı���ͼƬ
	 * @param slideButtonBackground
	 */
	public void setSlideBackgroudResource(int slideButtonBackground) {
		slideBg = BitmapFactory.decodeResource(getResources(), slideButtonBackground);
	}

	/**
	 * ���û������صı���ͼƬ
	 * @param switchBackground
	 */
	public void setSwitchBackgroudResource(int switchBackground) {
		switchBg = BitmapFactory.decodeResource(getResources(), switchBackground);
	}

	/**
	 * ���ÿ��ص�״̬
	 * @param open
	 */
	public void setToggleState(ToggleState state) {
		toggleState = state;
	}
	
	/**
	 * ���õ�ǰ�ؼ���ʾ����Ļ�ϵĿ��
	 */
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		setMeasuredDimension(switchBg.getWidth(), switchBg.getHeight());
	}
	
	/**
	 * �����Լ���ʾ����Ļʱ������
	 */
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		//1.���Ʊ���ͼƬ
		//left: ͼƬ����ߵ�x����
		//top: ͼƬ������y����
		canvas.drawBitmap(switchBg, 0, 0, null);
		
		//2.���ƻ������ͼƬ
		if(isSliding){
			int left = currentX - slideBg.getWidth()/2;
			if(left<0)left = 0;
			if(left>(switchBg.getWidth() - slideBg.getWidth())){
				left = switchBg.getWidth() - slideBg.getWidth();
			}
			canvas.drawBitmap(slideBg, left, 0, null);
		}else {
			//��ʱ̧�𣬸���stateȥ���ƻ������λ��
			if(toggleState==ToggleState.Open){
				canvas.drawBitmap(slideBg, switchBg.getWidth()-slideBg.getWidth(), 0, null);
			}else {
				canvas.drawBitmap(slideBg, 0, 0, null);
			}
		}
		
	}
	
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		currentX = (int) event.getX();
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			isSliding = true;
			break;
		case MotionEvent.ACTION_MOVE:
			break;
		case MotionEvent.ACTION_UP:
			isSliding = false;
			
			int centerX = switchBg.getWidth()/2;
			if(currentX>centerX){
				//open
				if(toggleState!=ToggleState.Open){
					toggleState = ToggleState.Open;
					if(listner!=null){
						listner.onToggleStateChange(toggleState);
					}
				}
			}else {
				//close
				if(toggleState!=ToggleState.Close){
					toggleState = ToggleState.Close;
					if(listner!=null){
						listner.onToggleStateChange(toggleState);
					}
				}
			}
			break;
		}
		invalidate();
		return true;
	}

	private OnToggleStateChangeListner listner;
	public void setOnToggleStateChangeListener(OnToggleStateChangeListner listner){
		this.listner = listner;
	}
	public interface OnToggleStateChangeListner{
		void onToggleStateChange(ToggleState state);
	}
}
