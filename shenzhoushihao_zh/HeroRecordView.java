 
package com.example.shenzhoushihao;                                            //声明包语句

//引入相关类
import java.util.HashMap;                       
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.media.SoundPool;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class HeroRecordView extends SurfaceView implements SurfaceHolder.Callback                       //继承SurfaceView类实现SurfaceHolder和Callback接口
{   
	ShenzhoushihaoActivity father;					                           //ShenzhoushihaoActivity的引用
	Bitmap bmpBack;                                                            //存放英雄榜的背景图片
	Bitmap left;                                                               //存放左箭头
	
    Rect rectLeft = new Rect(1,270,60,310);                                    //左箭头响应范围
    
	SoundPool soundPool;		                                               //SoundPool对象引用
	HashMap<Integer,Integer> soundMap;	                                       //存放声音资源的Map
	User[] user=new User[6];                                                   //存放得分在前六名的英雄，User对象
	
	public HeroRecordView(ShenzhoushihaoActivity father)                       //构造方法
	{              
		super(father);                                                         //初始化父类
		this.father = father;                                                  //初始化成员变量
		getHolder().addCallback(this);
		initSound(father);                                                     //初始化声音资源的方法
		bmpBack = BitmapFactory.decodeResource(father.getResources(), R.drawable.welcome_menu);         //加载背景图片资源
		left = BitmapFactory.decodeResource(father.getResources(), R.drawable.lpoint);                  //加载左箭头
		for(int i=0;i<6;i++)                                                   //给每个User对象分配空间
			user[i]=new User();
		}
	
	
	public void doDraw(Canvas canvas)                                          //doDraw方法，在HeroRecordView被创建调用surfaceCreated时调用
	{                              
		Paint paint = new Paint();	                                           //创建画笔
		paint.setAlpha(255);		                                           //设置最亮
		canvas.drawBitmap(bmpBack, 0, 0,paint);                                //绘制背景
		canvas.drawBitmap(left, 5, 280, paint);                                //绘制箭头
		
		Paint paint1 = new Paint();	                                           //同上
		paint1.setAlpha(255);                                                  //同上
		paint1.setTextSize(32);                                                //设置字体
	    paint1.setTypeface(Typeface.DEFAULT_BOLD);	                           //设置粗体
	    paint1.setColor(Color.rgb(255, 50, 70));                               //设置rgb颜色
		paint1.setAntiAlias(true);		                                       //设置抗锯齿       
		canvas.drawText("英 雄 榜", 70, 50, paint1);		                       //写上大大的英雄榜三个字
		
		Paint paint2 = new Paint();            	                               //创建画笔
		paint2.setAlpha(255);                                                  //设置亮度
		paint2.setTextSize(22);                                                //设置字体大小
	    paint2.setTypeface(Typeface.DEFAULT_BOLD);	                           //设置粗体
	    paint2.setColor(Color.RED);                                            //设置颜色
	    
		Paint paint3 = new Paint();	                                           //创建画笔
		paint3.setAlpha(255);                                                  //同上
		paint3.setTextSize(18);                                                //同上
	    paint3.setTypeface(Typeface.DEFAULT_BOLD);	                           //设置粗体
	    paint3.setColor(Color.rgb(140, 50, 70));                               //设置颜色
	    father.loadHero(user);                                                 //加载英雄User对象付给user数组
	    paint1.setTextSize(23);                                                //给画笔paint1重新设置字体
	    canvas.drawText("英雄大名     辉煌战绩", 18, 85, paint1);                       //写上这八个中号字体的字时
	    for(int i=0;i<6;i++)                                                   //遍历得到的6个对象
	    	if(user[i].userName.length()>10)
	    		user[i].userName=user[i].userName.substring(0, 7);             //名字太长的话，取前八个
	        if(user[0].userName!="空"&&user[0].userName!=null)
	        {   //为空或者为默认值nullnull时不显示
	        	canvas.drawText(user[0].userName, 26, 120, paint2);            //写名字
                canvas.drawText(Integer.toString(user[0].grade) , 174, 120, paint2);//写分数
                }
	    for(int i=1;i<6;i++)
	    {                                                                      //同样的方法写上第二至第五名
	    	if(user[i].userName!="空"&&user[i].userName!=null) 
	    	{                                                                  //同上
	    		canvas.drawText(user[i].userName, 30, 120+i*30, paint3);       //同上
		        canvas.drawText(Integer.toString(user[i].grade) , 177, 120+i*30, paint3);
		        }                                                              //同上
	    	}
	    }
	
	
	public void initSound(ShenzhoushihaoActivity father)                       //方法：初始化声音
	{                                                                          //initSound的方法体
		soundPool = new SoundPool(1,AudioManager.STREAM_MUSIC,100);            //初始化soundPool,参数分别为允许同时播放的音乐最多为一个，第二个是类型，最后是音质
		soundMap = new HashMap<Integer,Integer>();                             //初始化存放声音资源的哈希表
		int id = soundPool.load(father, R.raw.game_press, 2);                  //加载按键声音资源，id为2
		soundMap.put(2, id);	                                               //存入soundMap，指定id号
		}
	
	
	public void playSound(int sound,int loop)                                  //方法：播放声音
	{                                                                          //重写系统的播放声音的方法，第一个参数是音乐的id，第二个是播放次数，-1为无限循环，0为一次，1为两次，以此类推
		AudioManager am = (AudioManager)getContext().getSystemService(Context.AUDIO_SERVICE);
		float streamVolumeCurrent = am.getStreamVolume(AudioManager.STREAM_MUSIC);   
	    float streamVolumeMax = am.getStreamMaxVolume(AudioManager.STREAM_MUSIC);       
	    float volume = streamVolumeCurrent / streamVolumeMax;   	    
	    soundPool.play(soundMap.get(sound), volume, volume, 1, loop, 2);       //系统的播放声音的方法
	    }
	
	
	public void stopSound(int sound)                                           //方法：停止声音
	{                                                                          //重写停止声音的方法，主要母的是可以通过传递音乐资源的id
		soundPool.stop(soundMap.get(sound));                                   //系统停止声音方法 
		}	
		

	public void surfaceChanged(SurfaceHolder holder, int format, int width,int height) 
	{                                                                          //重写接口方法
		}

	
	public void surfaceCreated(SurfaceHolder holder) 
	{                                                                          //重写接口的方法，创建时自动调用
		Canvas canvas=null;
		try 
		{
			canvas = holder.lockCanvas(null);                                  //锁定画布
			doDraw(canvas);                                                    //调用doDraw函数
			} 
		catch (Exception e) 
		{                                                                      //捕获异常
			e.printStackTrace();
			}
		finally
		{                                                                      //最后要处理的
			if(canvas != null)
			{
				holder.unlockCanvasAndPost(canvas);                            //释放画布
				}
			}
		}

	
    public void surfaceDestroyed(SurfaceHolder holder) 
    {                                                                          //重写接口的方法，销毁时自动调用
		}
		
	//HeroRecordView中各触屏响应
	@SuppressWarnings("unchecked")
	public boolean myTouchEvent(int x,int y)                                   //触屏响应方法，在ShenzhoushihaoActivity中的OnTouchEvent的中调用
	{
		if(father.currentView==this&&rectLeft.contains(x,y))
		{
			if(WelcomeView.wantSound)                                          //声音开关为开时 
				playSound(2,0);                                                //放出按键音
			father.vib.vibrate(new long[]{100,10,100,70},-1);                  //按键振动
			father.setContentView(father.wv);						           //设置当前屏幕为WelcomeView对象
		    father.currentView = father.wv;                                    //记录当前View
		    father.wv.status=7;                                                //直接到WelcomeView中case 7
		    }
		return true;
		}
	}
