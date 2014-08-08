 
package com.example.shenzhoushihao;                   //声明包语句

//引入相关类
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Message;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/*
 * 帮助界面
 *
 */

public class HelpView2 extends SurfaceView implements SurfaceHolder.Callback 
{
	PlaneActivity activity;                           //PlaneActivity的引用
	private TutorialThread thread;                    //刷帧的线程
	Paint paint;                                      //画笔
	Bitmap background;                                //背景
	Bitmap help3;                                     //写有游戏玩法的ipad屏幕
	Bitmap ok;                                        //确定按钮
	
	
	public HelpView2(PlaneActivity activity)          //构造器 
	{
		super(activity);
		this.activity = activity;                     //得到activity的引用
        getHolder().addCallback(this);
        this.thread = new TutorialThread(getHolder(), this);
        initBitmap();                                 //初始化图片资源
	}
	
	
	public void initBitmap()                          //初始化图片资源的方法
	{
		paint = new Paint();                          //生成画笔 
		background = BitmapFactory.decodeResource(getResources(), R.drawable.background);
		help3 = BitmapFactory.decodeResource(getResources(), R.drawable.help3);
		ok = BitmapFactory.decodeResource(getResources(), R.drawable.ok);
	}
	
	
	public void onDraw(Canvas canvas)                 //绘制方法
	{
		//画的内容是z轴的，后画的会覆盖前面画的
		canvas.drawColor(Color.BLACK);                //将屏幕刷成黑色 
		canvas.drawBitmap(background, 0,0, paint);
		canvas.drawBitmap(help3, 0, 10, paint);
		canvas.drawRect(0, 0, 320, 10, paint);        //绘制上下的黑框
		canvas.drawRect(0, 230, 320, 240, paint); 
		canvas.drawBitmap(ok, 256, 198, paint);  
    }
	
	
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) 
	{}
	
	
	public void surfaceCreated(SurfaceHolder holder)  //创建时被调用  
	{
        this.thread.setFlag(true);                    //设置线程标志位
        this.thread.start();                          //启动线程
	}
	
	
	public void surfaceDestroyed(SurfaceHolder holder)//摧毁时被调用 
	{
        boolean retry = true;                         //循环标志位
        thread.setFlag(false);                        //设置循环标志位
        while (retry) 
        {
            try {
                thread.join();                        //等待线程结束
                retry = false;
            } 
            catch (InterruptedException e)            //不断地循环，直到刷帧线程结束
            {}
        }
	}
	
	
	@Override
	public boolean onTouchEvent(MotionEvent event)    //屏幕监听
	{
		if(event.getAction() == MotionEvent.ACTION_DOWN)
		{
			if(event.getX()>256 && event.getX()<256+ok.getWidth()
					&& event.getY()>198 && event.getY()<198+ok.getHeight())    //点击了确定按钮
			{
				Message msg1 = activity.myHandler.obtainMessage(7); 
				activity.myHandler.sendMessage(msg1);                          //向主activity发送Handler消息
			}  
		}
		return super.onTouchEvent(event);
	}
	
	
	
	class TutorialThread extends Thread               //刷帧线程
	{
		private int span = 100;                       //睡眠的毫秒数 
		private SurfaceHolder surfaceHolder;
		private HelpView2 helpView;                   //HelpView2的引用
		private boolean flag = false;                 //循环标志位
		
		
        public TutorialThread(SurfaceHolder surfaceHolder, HelpView2 helpView) //构造器
        {
            this.surfaceHolder = surfaceHolder;
            this.helpView = helpView;                 //得到帮助界面
        }
        
        
        public void setFlag(boolean flag)             //设置标志位 
        {
        	this.flag = flag;
        }
        
        
		@Override
		public void run()                             //重写的run方法  
		{
			Canvas c;                                 //画布
            while (this.flag) 
            {
                c = null;
                try 
                {
                    c = this.surfaceHolder.lockCanvas(null);                   // 锁定整个画布
                    synchronized (this.surfaceHolder) 
                    {
                    	helpView.onDraw(c);           //调用绘制方法
                    }
                } 
                finally                               //使用finally语句保证下面代码一定被执行
                {
                    if (c != null) 
                    {
                    	this.surfaceHolder.unlockCanvasAndPost(c);	           //更新屏幕显示内容
                    }
                }
                try
                {
                	Thread.sleep(span);               //睡眠指定毫秒数
                }
                catch(Exception e)                    //捕获异常信息
                {
                	e.printStackTrace();              //打印异常信息
                }
            }
		}
	}
}