 
package com.example.shenzhoushihao;              //声明包语句

//引入相关类
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Message;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/* 
 * 游戏失败界面
 *
 */

public class FailView extends SurfaceView implements SurfaceHolder.Callback   
{
	PlaneActivity activity;                      //PlaneActivity的引用
	private TutorialThread thread;               //刷帧的线程
	Bitmap fialBackground;                       //背景
	Bitmap goon;                                 //重新游戏按钮
	Bitmap exit2;                                //退出按钮
	Paint paint;                                 //画笔
	
	
	public FailView(PlaneActivity activity)      //构造器 
	{
		super(activity);
		this.activity = activity;                //得到activity的引用
        getHolder().addCallback(this);
        this.thread = new TutorialThread(getHolder(), this);
        initBitmap();                            //初始化图片资源 
	}
	
	
	public void initBitmap()                     //初始化图片资源的方法
	{
		paint = new Paint();
		fialBackground = BitmapFactory.decodeResource(getResources(), R.drawable.fialbackground);
		goon = BitmapFactory.decodeResource(getResources(), R.drawable.goon);
		exit2 = BitmapFactory.decodeResource(getResources(), R.drawable.exit2);
	}
	
	
	public void onDraw(Canvas canvas)            //绘制方法
	{
		//画的内容是z轴的，后画的会覆盖前面画的
		canvas.drawBitmap(fialBackground, 0, 0, paint);
		canvas.drawBitmap(goon, 0, 10, paint);
		canvas.drawBitmap(exit2, 230, 209, paint);
		
		canvas.drawRect(0, 0, 320, 10, paint);   //绘制上下的黑框
		canvas.drawRect(0, 230, 320, 240, paint);
	}
	
	
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) 
	{}
	
	
	public void surfaceCreated(SurfaceHolder holder)                             //创建时被调用  
	{
        this.thread.setFlag(true);               //设置线程标志位
        this.thread.start();                     //启动线程
	}
	
	
	public void surfaceDestroyed(SurfaceHolder holder)                           //摧毁时被调用 
	{
        boolean retry = true;                    //循环标志位
        thread.setFlag(false);                   //设置循环标志位
        while (retry) 
        {
            try 
            {
                thread.join();                   //等待线程结束
                retry = false;
            } 
            catch (InterruptedException e)       //不断地循环，直到刷帧线程结束
            {}
        }
	}
	
	
	@Override
	public boolean onTouchEvent(MotionEvent event)                               //屏幕监听
	{
		if(event.getAction() == MotionEvent.ACTION_DOWN)
		{
			if(event.getX() > 0 && event.getX() < 0+goon.getWidth()
					&& event.getY() > 10 && event.getY() < 10+goon.getHeight())  //点击了重新游戏按钮
			{
				Message msg1 = activity.myHandler.obtainMessage(2);
				activity.myHandler.sendMessage(msg1);                            //向主activity发送Handler消息			
			}
			else if(event.getX() > 230 && event.getX() < 230+exit2.getWidth()
					&& event.getY() > 209 && event.getY() < 209+exit2.getHeight()) //点击了退出游戏按钮
			{
				System.exit(0);                                                  //直接退出
			}
		}
		return super.onTouchEvent(event);
	}
	
	
	
	class TutorialThread extends Thread          //刷帧线程
	{
		private int span = 500;                  //睡眠的毫秒数 
		private SurfaceHolder surfaceHolder;
		private FailView fialView;
		private boolean flag = false;
		
		
        public TutorialThread(SurfaceHolder surfaceHolder, FailView fialView)    //构造器
        {
            this.surfaceHolder = surfaceHolder;
            this.fialView = fialView;
        }
        
        
        public void setFlag(boolean flag)        //设置标志位
        {
        	this.flag = flag;
        }
        
        
		@Override
		public void run()                        //重写的run方法
		{
			Canvas c;
            while (this.flag) 
            {
                c = null;
                try                              // 锁定整个画布
                {
                    c = this.surfaceHolder.lockCanvas(null);                     
                    synchronized (this.surfaceHolder) 
                    {
                    	fialView.onDraw(c);      //调用绘制方法
                    }
                }
                finally 
                {
                    if (c != null)               //更新屏幕显示内容
                    {
                    	this.surfaceHolder.unlockCanvasAndPost(c);
                    }
                }
                try
                {
                	Thread.sleep(span);          //睡眠指定毫秒数
                }
                catch(Exception e)               //捕获异常信息
                {
                	e.printStackTrace();         //打印异常信息
                }
            }
		}
	}
}