 
package com.example.shenzhoushihao;                        //声明包语句

//引入相关类
import java.util.HashMap;   
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.AudioManager;
import android.media.SoundPool;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class HelpView3 extends SurfaceView implements SurfaceHolder.Callback//继承SurfaceView类实现SurfaceHolder和Callback接口
{    
	TankActivity father;					               //TankActivity的引用
	Bitmap menu_help1;                                     //帮助第一页的图片
	Bitmap menu_help2;                                     //帮助第二页的图片
	Bitmap menu_help3;                                     //帮助第三页的图片
	public static int pageNumber=1;                        //默认HelpView的页码为1
	
	Rect rectLeft    =new Rect(5,270,80,315);              //左箭头响应范围，为矩形，参数为左上角和右下角的坐标。屏幕左上角的坐标为（0,0）
	Rect rectRight   =new Rect(180,280,235,315);           //右箭头响应范围，同上
	
	SoundPool soundPool;		                           //SoundPool对象引用
	HashMap<Integer,Integer> soundMap;	                   //存放声音资源的Map
	
	
	public HelpView3(TankActivity father)                  //构造方法
	{   
		super(father);                                     //初始化父类
		this.father = father;                              //初始化成员变量
		getHolder().addCallback(this);
		initSound(father);                                 //初始化声音资源的方法
		menu_help1 = BitmapFactory.decodeResource(father.getResources(), R.drawable.menu_help13);//加载第一页的图片资源
		menu_help2 = BitmapFactory.decodeResource(father.getResources(), R.drawable.menu_help23);//加载第二页的图片资源
		menu_help3 = BitmapFactory.decodeResource(father.getResources(), R.drawable.menu_help33);//加载第三页的图片资源
	}
	
	
	public void doDraw(Canvas canvas)                      //doDraw方法，绘制整个屏幕
	{                  
		Paint paint = new Paint();	                       //创建画笔
		paint.setAlpha(255);                               //设置透明度
		if(pageNumber==1)                                  //若为第一页
		    canvas.drawBitmap(menu_help1, 0, 0,paint);     //绘制第一张图片
		else if(pageNumber==2)                             //若为第二页
			canvas.drawBitmap(menu_help2, 0, 0,paint);     //绘制第二张图片
		else if(pageNumber==3)                             //若为第三页
			canvas.drawBitmap(menu_help3, 0, 0,paint);     //绘制第三张图片
	}
	
	//方法：初始化声音
	public void initSound(TankActivity father)             //initSound的方法体
	{         
		soundPool = new SoundPool(1,AudioManager.STREAM_MUSIC,100);//初始化soundPool,参数分别为允许同时播放的音乐最多为一个，第二个是类型，最后是音质
		soundMap = new HashMap<Integer,Integer>();         //初始化存放声音资源的哈希表
		int id = soundPool.load(father, R.raw.game_press, 2);//加载按键声音资源，id为2
		soundMap.put(2, id);	                           //存入soundMap，指定id号
	}
		
	//方法：播放声音
	public void playSound(int sound,int loop)              //重写系统的播放声音的方法，第一个参数是音乐的id，
	                                                       //第二个是播放次数，-1为无限循环，0为一次，1为两次，以此类推
	{                    
		AudioManager am = (AudioManager)getContext().getSystemService(Context.AUDIO_SERVICE);
		float streamVolumeCurrent = am.getStreamVolume(AudioManager.STREAM_MUSIC);   
		float streamVolumeMax = am.getStreamMaxVolume(AudioManager.STREAM_MUSIC);       
		float volume = streamVolumeCurrent / streamVolumeMax;   	    
		soundPool.play(soundMap.get(sound), volume, volume, 1, loop, 2);//系统的播放声音的方法
	}
	
    //方法：停止声音
	public void stopSound(int sound)                       //重写停止声音的方法，主要母的是可以通过传递音乐资源的id
	{                  
		soundPool.stop(soundMap.get(sound));               //系统停止声音方法
	}	
		
	
	public void surfaceChanged(SurfaceHolder holder, int format, int width,int height)//重写接口方法
	{}

	
	public void surfaceCreated(SurfaceHolder holder)       //创建View时率先执行
	{             
		Canvas canvas=null;
		try 
		{                            
			canvas = holder.lockCanvas(null);              //锁定画布
			doDraw(canvas);                                //调用doDraw方法，绘制整个屏幕
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		finally
		{
			if(canvas != null)
			{
				holder.unlockCanvasAndPost(canvas);        //解锁
			}
		}		
	}
	

	public void surfaceDestroyed(SurfaceHolder holder)     //View被销毁时调用的方法
	{}
		
	//************HelpView3中各触屏响应***********************************************
	@SuppressWarnings("unchecked")
	public boolean myTouchEvent(int x,int y)               //此方法在ShenzhoushihaoActivity调用，当触及屏幕时系统调用TheKingOfMedicineActivity
	{	                                                   //中的onTouchEvent,获取电子坐标后，然后传递参数给此方法
		if(father.currentView==this)                       //当前屏幕停在HelpView3上时
		{
			if(rectLeft.contains(x,y))                     //点击向左的箭头，坐标包含在所规定的矩形内时
			{	
				if(WelcomeView3.wantSound)                 //若声音开关为开
					playSound(2,0);                        //播放按键音效
			    father.vib.vibrate(new long[]{100,10,100,70},-1); //振动
				if(pageNumber==1)                          //如果在第一页
				{
					father.setContentView(father.wv);      //设置当前屏幕为WelcomeView3对象
					father.currentView = father.wv;        //记录当前View
					father.wv.status=7;                    //转到WelcomeView3的主菜单页面
				}
				else
				{                                          //若不是第一页
					pageNumber--;                          //页数减1     
					father.myHandler.sendEmptyMessage(3);  //传个3给myHandler的sendEmptyMessage方法，执行其中的case3，
				}                                          //即进入帮助View，只不过页数减了一
			}
			else if(rectRight.contains(x,y))               //点击了右箭头的区域，原理同上左箭头
			{
				if(pageNumber==1||pageNumber==2)           //当前为第一页或第二页
				{           
					if(WelcomeView3.wantSound)             //声音开关为开则播放声音
						playSound(2,0);
					father.vib.vibrate(new long[]{100,10,100,70},-1); //振动
					pageNumber++;                          //页码+1   
					father.myHandler.sendEmptyMessage(3);  //传个3给myHandler的sendEmptyMessage方法，执行其中的case3，
				}                                          //即进入帮助View，只不过页数加了一
			}
				
		}
		return true;
	}
}

