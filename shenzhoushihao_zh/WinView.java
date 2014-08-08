 
package com.example.shenzhoushihao;              //声明包语句

//引入相关类
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.graphics.Color;
import android.graphics.Typeface;

/*
 * 游戏胜利界面，即过关界面
 *
 */

public class WinView extends SurfaceView implements SurfaceHolder.Callback 
{
	PlaneActivity activity;                      //PlaneActivity的引用
	private TutorialThread thread;               //刷帧的线程
	Bitmap winviewbackground;                    //背景
    Paint paint;                                 //画笔
	Paint paint1;                                //画笔1
	
	int alpha=255;                               //亮度最大
	
	
	public WinView(PlaneActivity activity)       //构造器
	{ 
		super(activity);
		this.activity = activity;                //得到activity的引用
        getHolder().addCallback(this);
        this.thread = new TutorialThread(getHolder(), this);
        initBitmap();                            //初始化图片资源 
	}
	
	
	public void initBitmap()                     //初始化图片资源的方法
	{ 
		paint = new Paint();                     //生成画笔
		paint.setAlpha(alpha);                   //设置亮度
		paint.setTextSize(22);                   //设置字体大小
	    paint.setTypeface(Typeface.DEFAULT_BOLD);//设置粗体
	    paint.setColor(Color.RED);               //设置颜色
		paint.setAntiAlias(true);				 //设置抗锯齿
		paint1 = new Paint();                    //生成画笔
		paint1.setAlpha(alpha);                  //设置亮度
		paint1.setTextSize(18);                  //设置字体大小
	    paint1.setTypeface(Typeface.DEFAULT_BOLD);//设置粗体
	    paint1.setColor(Color.YELLOW);           //设置颜色
		paint1.setAntiAlias(true);				 //设置抗锯齿
		
		winviewbackground = BitmapFactory.decodeResource(getResources(), R.drawable.bg);
}
	
	
	public void onDraw(Canvas canvas)            //绘制方法
	{
		String currentScore=Integer.toString(activity.currentScore);                      //显示当前关卡得分的字符串
		//画的内容是z轴的，后画的会覆盖前面画的
		canvas.drawBitmap(winviewbackground, 0, 10, paint);	
		canvas.drawText("本关得分：       ", 25, 185, paint);                                    //根据坐标位置写上规定的字  
		canvas.drawText("点击屏幕的任意位置进入下一关", 15, 110, paint1);                          //根据坐标位置写上规定的字  
		canvas.drawText(currentScore, 135, 185, paint);                                   //绘制当前得分   
		canvas.drawRect(0, 0, 320, 10, paint);                                            //绘制上下的黑框
		canvas.drawRect(0, 230, 320, 240, paint);
	}
	
	
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height)
	{}
	
	
	public void surfaceCreated(SurfaceHolder holder)                                     //创建时被调用 
	{
        this.thread.setFlag(true);               //设置线程标志位
        this.thread.start();                     //启动线程
	}
	
	
	public void surfaceDestroyed(SurfaceHolder holder)                                   //摧毁时被调用 
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
	
	class TutorialThread extends Thread          //刷帧线程
	{
		private int span = 500;                  //睡眠的毫秒数 
		private SurfaceHolder surfaceHolder;
		private WinView winView;                 //WinView的引用
		private boolean flag = false;            //循环标志位
		
		
        public TutorialThread(SurfaceHolder surfaceHolder, WinView winView)              //构造器
        {
            this.surfaceHolder = surfaceHolder;
            this.winView = winView;              //得到游戏胜利界面            
        }        
        
        public void setFlag(boolean flag)        //设置标志位 
        {
        	this.flag = flag;
        }        
        
		@Override
		public void run()                        //重写的run方法 
		{
			Canvas c;                            //画布
            while (this.flag)                    //循环 
            {
                c = null;
                try 
                {
                	c = this.surfaceHolder.lockCanvas(null);                	         // 锁定整个画布
                    synchronized (this.surfaceHolder) 
                    {
                    	winView.onDraw(c);       //调用绘制方法
                    }
                } 
                finally                          //使用finally语句保证下面代码一定被执行 
                {
                    if (c != null) 
                    {
                        this.surfaceHolder.unlockCanvasAndPost(c);                    	 //更新屏幕显示内容
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