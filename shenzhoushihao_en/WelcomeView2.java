 
package com.example.shenzhoushihao;              //声明包语句

//引入相关类
import java.util.HashMap;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.AudioManager;
import android.media.SoundPool;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/*
 * 欢迎界面
 *
 */

public class WelcomeView2 extends SurfaceView implements SurfaceHolder.Callback 
{
	PlaneActivity activity;                      //activity的引用
	private TutorialThread thread;               //刷帧的线程
	private WelcomeViewThread2 welcomeThread;    //绘制线程
	int status = 1;                              //当前的状态值
	int k = 0;                                   //状态为2时用到的切换图片
	int alpha = 255;                             //透明度
    SoundPool soundPool;                         //声音
	HashMap<Integer, Integer> soundPoolMap; 
	Bitmap background;                           //背景图片
	Bitmap background2;                          //背景图片2
	Bitmap image1,image2,image3,image4,image5,image6,image7,image8,image9,image10;       //动画帧
	Bitmap startGame;                            //开始游戏菜单
	Bitmap help;                                 //帮助菜单
	Bitmap openSound;                            //打开声音菜单
	Bitmap closeSound;                           //关闭声音菜单
	Bitmap exit;                                 //退出菜单
	int backgroundY = -200;                      //背景的坐标
	int background2Y = 40;
    Paint paint;                                 //用于改变透明度
	Paint paint2;                                //正常绘制
	
	
	public WelcomeView2(PlaneActivity activity)  //构造器 
	{
		super(activity);
		this.activity = activity;                //得到activity的引用
		if(activity.processView != null)         //走加载界面进度
		{
        	activity.processView.process += 10;
        }
        getHolder().addCallback(this);
        this.thread = new TutorialThread(getHolder(), this);
        this.welcomeThread = new WelcomeViewThread2(this);
        if(activity.processView != null)         //走加载界面进度
        {
        	activity.processView.process += 10;
        }
        initSounds();                            //初始化声音 
        initBitmap();                            //初始化图片资源
        playSound(1);                            //播放音乐
	}
	
	
	public void initSounds()                     //初始化声音的方法
	{
	     soundPool = new SoundPool(4, AudioManager.STREAM_MUSIC, 100);                   //初始化SoundPool
	     soundPoolMap = new HashMap<Integer, Integer>();                                //初始化HashMap
	     soundPoolMap.put(1, soundPool.load(getContext(), R.raw.welcome1, 1));
	} 
	
	
	public void playSound(int sound)             //播放声音的方法
	{
	    AudioManager mgr = (AudioManager)getContext().getSystemService(Context.AUDIO_SERVICE);   
	    float streamVolumeCurrent = mgr.getStreamVolume(AudioManager.STREAM_MUSIC);   
	    float streamVolumeMax = mgr.getStreamMaxVolume(AudioManager.STREAM_MUSIC);       //设置最大音量       
	    float volume = streamVolumeCurrent/streamVolumeMax;                             //设备的音量
	    soundPool.play(soundPoolMap.get(sound), volume, volume, 1, 0, 1f);              //播放
	}
	
	
	public void initBitmap()                     //初始化图片资源的方法
	{
		paint = new Paint();                     //生成画笔
		paint2 = new Paint();                    //生成画笔
        if(activity.processView != null)         //走加载界面进度
        {
        	activity.processView.process += 10;
        }
		background = BitmapFactory.decodeResource(getResources(), R.drawable.background);
		image1 = BitmapFactory.decodeResource(getResources(), R.drawable.image1);
        if(activity.processView != null)         //走加载界面进度
        {
        	activity.processView.process += 10;
        }
		image2 = BitmapFactory.decodeResource(getResources(), R.drawable.image2);        //初始化动画帧
		image3 = BitmapFactory.decodeResource(getResources(), R.drawable.image3);        //初始化动画帧
		image8 = BitmapFactory.decodeResource(getResources(), R.drawable.image8);        //初始化动画帧
        if(activity.processView != null)         //走加载界面进度
        {
        	activity.processView.process += 10;
        }
		image4 = BitmapFactory.decodeResource(getResources(), R.drawable.image4);        //初始化动画帧
		image5 = BitmapFactory.decodeResource(getResources(), R.drawable.image5);        //初始化动画帧
		image9 = BitmapFactory.decodeResource(getResources(), R.drawable.image9);        //初始化动画帧
        if(activity.processView != null)         //走加载界面进度
        {
        	activity.processView.process += 10;
        }
		image6 = BitmapFactory.decodeResource(getResources(), R.drawable.image6);        //初始化动画帧
		image7 = BitmapFactory.decodeResource(getResources(), R.drawable.image7);        //初始化动画帧
		image10 = BitmapFactory.decodeResource(getResources(), R.drawable.image10);      //初始化动画帧
		
        if(activity.processView != null)         //走加载界面进度
        {
        	activity.processView.process += 10;
        }
		background2 = BitmapFactory.decodeResource(getResources(), R.drawable.background2);        //初始化背景图片
		startGame = BitmapFactory.decodeResource(getResources(), R.drawable.startgame);            //初始化开始游戏
        if(activity.processView != null)         //走加载界面进度
        {
        	activity.processView.process += 10;
        }
		help = BitmapFactory.decodeResource(getResources(), R.drawable.help);                      //帮助
		openSound = BitmapFactory.decodeResource(getResources(), R.drawable.opensound);            //打开声音
        if(activity.processView != null)         //走加载界面进度
        {
        	activity.processView.process += 10;
        }
		closeSound = BitmapFactory.decodeResource(getResources(), R.drawable.closesound);          //关闭声音
		exit = BitmapFactory.decodeResource(getResources(), R.drawable.exit);                      //退出游戏
        if(activity.processView != null)         //走加载界面进度
        {
        	activity.processView.process += 10;
        }
	}
	
	
	public void onDraw(Canvas canvas)            //绘制方法
	{
		//画的内容是z轴的，后画的会覆盖前面画的
		canvas.drawColor(Color.BLACK);           //背景色
		if(this.status == 1 || this.status == 2)
		{
			//绘制进入游戏前的动态图片
			if(k == 0)
			{
				canvas.drawBitmap(image1, 0, 30, paint); 
			}
			else if(k > 0 && k<=1)
			{
				canvas.drawBitmap(image2, 0, 30, paint); 
			}
			else if(k>1 && k<=2)
			{
				canvas.drawBitmap(image3, 0, 30, paint); 
			}
			else if(k>2 && k<=3)
			{
				canvas.drawBitmap(image4, 0, 30, paint); 
			}
			else if(k>3 && k<=4)
			{
				canvas.drawBitmap(image5, 0, 30, paint); 
			}
			else if(k>4 && k<=5)
			{
				canvas.drawBitmap(image6, 0, 30, paint); 
			}
			else if(k>5 && k<=6)
			{
				canvas.drawBitmap(image7, 0, 30, paint); 
			}
			else if(k>6 && k<=7)
			{
				canvas.drawBitmap(image8, 0, 30, paint); 
			}
			else if(k>7 && k<=8)
			{
				canvas.drawBitmap(image9, 0, 30, paint); 
			}
			else if(k>8 && k<=10)
			{
				canvas.drawBitmap(image10, 0, 30, paint); 
			}
			canvas.drawRect(0, 0, 320, 10, paint);                   //绘制上下的黑框
			canvas.drawRect(0, 230, 320, 240, paint);                //绘制矩形		
		}
		else if(status == 3)
		{
			paint.setAlpha(alpha);               //设置透明度
			canvas.drawBitmap(background2, 0, background2Y, paint);  //绘制背景图
			canvas.drawRect(0, 0, 320, 10, paint2);                  //绘制上下的黑框
			canvas.drawRect(0, 230, 320, 240, paint2);               //绘制矩形框
		}
		else if(status == 4)                     //菜单状态
		{
			canvas.drawBitmap(background2, 0, background2Y, paint);  //绘制背景图
			canvas.drawBitmap(startGame, 0, 10, paint2);             //绘制开始游戏按钮
			canvas.drawBitmap(help, 240, 10, paint2);                //绘制帮助按钮
			canvas.drawBitmap(exit, 240, 200, paint2);               //绘制退出按钮
			if(activity.isSound)
			{
				canvas.drawBitmap(closeSound, 0, 200, paint2);       //绘制关闭声音菜单
			}
			else
			{
				canvas.drawBitmap(openSound, 0, 200, paint2);        //绘制打开声音
			}
			canvas.drawRect(0, 0, 320, 10, paint2);                  //绘制上下的黑框
			canvas.drawRect(0, 230, 320, 240, paint2);
		}
	}
	
	
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) 
	{}

	
	public void surfaceCreated(SurfaceHolder holder)                 //创建时被调用
	{
        this.thread.setFlag(true);               //设置循环标记位
        this.thread.start();                     //启动绘制线程
        this.welcomeThread.setFlag(true);        //设置循环标记位
        this.welcomeThread.start();              //启动动画线程
	}
	
	
	public void surfaceDestroyed(SurfaceHolder holder)               //摧毁时被调用
	{
        boolean retry = true;                    //循环标记
        thread.setFlag(false);
        while (retry) 
        {
            try 
            {
                thread.join();                   //等待线程的结束
                retry = false;                   //设置循环标记停止循环
            } 
            catch (InterruptedException e)       //不断地循环，直到刷帧线程结束
            {}
        }
	}
	
	
	public boolean onTouchEvent(MotionEvent event)                   //屏幕监听
	{
		if(event.getAction() == MotionEvent.ACTION_DOWN)             //屏幕被按下
		{
			if(this.status != 4)                                     //当不是菜单状态时返回
			{
				return false;
			}
			double x = event.getX();                                 //得到X坐标
			double y = event.getY();                                 //得到Y坐标
			if(x>0 && x<0 + openSound.getWidth()
					&& y>10 && y<10 + openSound.getHeight())         //点击了开始游戏按钮
			{
				activity.myHandler.sendEmptyMessage(2);              //发送消息
			}
			else if(x>240 && x<240 + help.getWidth()
					&& y>10 && y<10 + help.getHeight())              //点击了帮助按钮
			{
				activity.myHandler.sendEmptyMessage(3);              //发送消息
			}
			else if(x>0 && x<0+ openSound.getWidth()
					&& y>200 && y<200 + openSound.getHeight())       //点击了声音按钮
			{
				activity.isSound = !activity.isSound;                //将声音标志位置反
			}
			else if(x>240 && x<240 + exit.getWidth()
					&& y>200 && y<200 + exit.getHeight())            //点击了退出按钮
			{
				System.exit(0);                                      //退出游戏
			}
			else
			{}
		}
		return super.onTouchEvent(event);                            //调用基类的方法
	}
	
	
	
	class TutorialThread extends Thread                              //刷帧线程
	{
		private int span = 100;                                      //睡眠的毫秒数 
		private SurfaceHolder surfaceHolder;
		private WelcomeView2 welcomeView;                            //欢迎界面的引用
		private boolean flag = false;
		
		
        public TutorialThread(SurfaceHolder surfaceHolder, WelcomeView2 welcomeView) {//构造器
            this.surfaceHolder = surfaceHolder;                      //SurfaceHolder的引用
            this.welcomeView = welcomeView;                          //欢迎界面的引用
        }
        
        
        public void setFlag(boolean flag)                            //设置标准位
        {
        	this.flag = flag;
        }
        
        
		public void run()                        //重写的run方法
		{
			Canvas c;
            while (this.flag)                    //循环
            {
                c = null;
                try 
                {
                	//锁定整个画布
                    c = this.surfaceHolder.lockCanvas(null);
                    synchronized (this.surfaceHolder)                //同步
                    {
                    	welcomeView.onDraw(c);                       //调用绘制方法
                    }
                } 
                finally 
                {                                                    //用finally保证一定被执行
                    if (c != null) 
                    {
                    	//更新屏幕显示内容
                        this.surfaceHolder.unlockCanvasAndPost(c);
                    }
                }
                try
                {
                	Thread.sleep(span);                              //睡眠指定毫秒数
                }
                catch(Exception e)                                   //捕获异常
                {
                	e.printStackTrace();                             //打印异常信息
                }
            }
		}
	}
}