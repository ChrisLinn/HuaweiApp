 
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
import android.widget.Button;


public class PassView extends SurfaceView implements SurfaceHolder.Callback    //PassView类继承了SurfaceView类，实现了SurfaceHolder和Callback接口
{    
	ShenzhoushihaoActivity father;                                             //ShenzhoushihaoActivity的引用
	PassThread pt;                                                             //PassThread的引用                           
	PassDrawThread pdt;                                                        //PassDrawThread的引用
	int stage;                                                                 //记录当前状态：0为关卡；6为相应的关卡对应的状态
	int alpha=255;                                                             //亮度最大
	int leftX=-130;                                                            //记录本关景物图片左上角坐标
	int rightX=300;                                                            //变量记录环图片左上角坐标
	Bitmap bmpCircle;                                                          //存放环的图片
	Bitmap gamePass;                                                           //存放过关界面的图片
	Bitmap gameOver;                                                           //存放游戏失败的图片
	BitmapData[] bitmapData=new BitmapData[6];                                 //存放关卡数组
	
	Rect save=new Rect(5,270,80,315);                                          //左下角按钮响应范围
	Rect movetonextlevel=new Rect(180,280,235,315);                            //右下角按钮响应范围
    Button myButton=null;
    
	SoundPool soundPool;		                                               //SoundPool对象引用
	HashMap<Integer,Integer> soundMap;                                         //存放声音资源的Map
	public PassView(ShenzhoushihaoActivity father)                             //构造器，初始化主要成员变量
	{                                                                          //构造方法
		super(father);                                                         //初始化父类
	    this.father = father;                                                  //初始化成员变量
	    stage=GameView.stage;                                                  //得到关卡号
	    getHolder().addCallback(this);
	    pt=new PassThread(this);                                               //两个线程
		pdt=new PassDrawThread(this,getHolder());
		initSound(father);                                                     //成员方法，初始化声音
		bmpCircle = BitmapFactory.decodeResource(this.getResources(), R.drawable.circle);               //加载环 的图片
		gamePass = BitmapFactory.decodeResource(father.getResources(), R.drawable.game_pass);           //加载过关 的图片
		gameOver = BitmapFactory.decodeResource(father.getResources(), R.drawable.game_over);           //加载环失败的图片
		BitmapManager.loadCurrentStageResources(getResources(),stage);         //加载对应关卡的图片资源
		initBitmap();                                                          //调用初始化图片的成员方法
		}
	
	
	//方法：绘制屏幕
	@SuppressWarnings("static-access")
	public void doDraw(Canvas canvas)                                          //doDraw方法，在PassDrawThread中反复调用
	{                      
		String currentScore=Integer.toString(father.currentScore[GameView.stage]);                      //显示本关卡得分的字符串
		Paint paint = new Paint();	                                           //创建画笔paint
		paint.setAlpha(alpha);                                                 //设置亮度
		paint.setTextSize(22);                                                 //设置字体大小
	    paint.setTypeface(Typeface.DEFAULT_BOLD);	                           //设置粗体
	    paint.setColor(Color.RED);                                             //设置颜色
		paint.setAntiAlias(true);					                           //设置抗锯齿
		Paint paint1 = new Paint();	                                           //创建画笔paint
		paint1.setAlpha(alpha);                                                //设置亮度
		paint1.setTextSize(15);                                                //设置字体大小
	    paint1.setTypeface(Typeface.DEFAULT_BOLD);	                           //设置粗体
	    paint1.setColor(Color.YELLOW);                                         //设置颜色
		paint1.setAntiAlias(true);					                           //设置抗锯齿
		if(father.gv.failFlag&&!father.gv.nextStage)                           //如果剩余环数为0，failFlag标志为true且过关标志为false
			{
			playSound(3,0);                                                    //播放失败音乐
			canvas.drawBitmap(gameOver,0,0,null);                              //绘制失败图片
			}                            
		else                                                                   //反之成功了
		{   
			playSound(2,0);                                                    //播放过关音乐                              
		    canvas.drawBitmap(gamePass, 0,0,null);                             //绘制过关背景
			switch(stage)                                                      //switch关卡号
			{                      
			case 0:                                                            //第一关为0和 6
				canvas.drawBitmap(bitmapData[0].bit[1], leftX,155,paint);      //三张搜集的图片往右移动，形成动画，leftX在PassThread中改变
				canvas.drawBitmap(bitmapData[0].bit[7], leftX,207,paint);
				canvas.drawBitmap(bitmapData[0].bit[4], leftX,248,paint);
				canvas.drawBitmap(bmpCircle, rightX,150,paint);                //三个环往右飘动，rightX在PassThread中改变
				canvas.drawBitmap(bmpCircle, rightX,200,paint);
				canvas.drawBitmap(bmpCircle, rightX,248,paint);
				break;
		   case 6:                                                             //动画结束后，显示套中物品的图片
			    canvas.drawBitmap(bitmapData[0].bit[10], 81,155,paint);        //炸弹
				canvas.drawBitmap(bitmapData[0].bit[16], 83,207,paint);        //燃料
				canvas.drawBitmap(bitmapData[0].bit[13], 75,248,paint);        //弹药
				leftX=-130;                                                    //恢复初值
				rightX=300;                        
				break;
				default : 
					break;
						}
			if(stage==6)//过关后显示得分
			{  				
				canvas.drawText("X"+father.gv.s3, 118, 183, paint);            //写完成任务的情况
				canvas.drawText("X"+father.gv.s2, 118, 234, paint);
				canvas.drawText("X"+father.gv.s1, 117, 293, paint);
				canvas.drawText("Score in this misson:", 40, 140, paint);                  //根据坐标位置写上规定的字
				canvas.drawText(currentScore, 145, 140, paint);                //绘制当前得分				
				canvas.drawText("Press to start next misson", 27, 310, paint1);         //显示提示信息
				}
			}
		}
	public void initBitmap()       //类对象的初始化
	{
		switch(stage)              //加载本关卡图片
		{            
		case 0:
			bitmapData[0]=new BitmapData(BitmapManager.currentStageResources, BitmapManager.level0X, BitmapManager.level0Y,BitmapManager.flag0);    //参数详见BitmapData的构造函数
			break;
			default:
				break;
				}
		}
			
			
	public void initSound(ShenzhoushihaoActivity father)                       //方法：初始化声音
	{                                                                          //initSound的方法体
		soundPool = new SoundPool(1,AudioManager.STREAM_MUSIC,100);            //初始化soundPool,参数分别为允许同时播放的音乐最多为1个，第二个是类型，最后是音质
		soundMap = new HashMap<Integer,Integer>();                             //初始化存放声音资源的哈希表
		int id1 = soundPool.load(father, R.raw.game_press, 1);                 //按键音，给予id号
		int id2 = soundPool.load(father, R.raw.game_pass, 2);                  //过关声音
		int id3 = soundPool.load(father, R.raw.game_fail, 3);                  //闯关失败声音
		int id4 = soundPool.load(father, R.raw.game_success, 4);               //通关音乐
		soundMap.put(1, id1);	                                               //按照秩序存入声音池，与id号对应  
		soundMap.put(2, id2);	
		soundMap.put(3, id3);
		soundMap.put(4, id4);
		}
	
	
	public void playSound(int sound,int loop)                                  //方法：播放声音
	{                                                                          //重写系统的播放声音的方法，第一个参数是音乐的id，第二个是播放次数，-1为无限循环，0为一次，1为两次，以此类推
		AudioManager am = (AudioManager)getContext().getSystemService(Context.AUDIO_SERVICE);
		float streamVolumeCurrent = am.getStreamVolume(AudioManager.STREAM_MUSIC);   
	    float streamVolumeMax = am.getStreamMaxVolume(AudioManager.STREAM_MUSIC);       
	    float volume = streamVolumeCurrent / streamVolumeMax;   	    
	    soundPool.play(soundMap.get(sound), volume, volume, 1, loop, 1);       //系统的播放声音的方法
	    }
	
	
	public void stopSound(int sound)                                           //方法：停止声音
	{                                                                          //传入id号 
		soundPool.stop(soundMap.get(sound));                                   //在soundMap中找到id为sound的音乐
		}
	
	
	public void surfaceChanged(SurfaceHolder holder, int format, int width,int height)
	{}                                                                         //重写接口surfaceChanged方法
	
	
	public void surfaceCreated(SurfaceHolder holder)                           //重写surfaceCreated方法，进入PassView时调用
	{
		pdt.isDrawOn = true;                                                   //绘制标志为true
		if(!pdt.isAlive())                                                     //如果没启动，则开启
		{         
			pdt.start();
			}
		pt.flag = true;                                                        //绘制标志为true
		if(!pt.isAlive())                                                      //如果没启动，则开启
		{           
			pt.start();
			}
		}
	
	
	public void surfaceDestroyed(SurfaceHolder holder)                         //重写surfaceDestroyed方法，离开PassView时自动调用
	{                          
		pdt.isDrawOn = false;                                                  //改变线程pdt标志，在PassDrawThread类中不在重复绘制屏幕
		}
	
	
	//PassView中个触屏相应
	@SuppressWarnings("unchecked")
	public boolean myTouchEvent(int x,int y)                                   //触屏响应方法，在ShenzhoushihaoActivity中的OnTouchEvent的中调用
	{
		if(save.contains(x,y)&&(!father.gv.nextStage))                         //如果是失败了之后点击左下角的位置
			{
			if(GameView.stage==0)
			{
				if(WelcomeView.wantSound)                                      //声音开关为开时播放按键音
					playSound(1,0);
				father.vib.vibrate(new long[]{100,10,100,70},-1);              //振动
				father.saveRecordDialog();                                     //弹出保存得分纪录的对话框
				pdt.isDrawOn=false;                                            //停止线程绘制和停止在线程里改变变量的值
			    pt.flag=false;                                                 //同上
			    }
			}
		else if(movetonextlevel.contains(x,y)&&(!father.gv.nextStage))         //若是游戏失败后点击右下角的区域
		{ 
			if(WelcomeView.wantSound)                                          //声音为开时
				playSound(1,0);                                                //播放按键声音
			father.vib.vibrate(new long[]{100,10,100,70},-1);                  //振动
			ShenzhoushihaoActivity.currentScore[0]=0;                          //重玩操作，本关得分清零	
			father.myHandler.sendEmptyMessage(7);                              //跳到ShenzhoushihaoActivity中的handler中的case 7，即进度条界面
			pdt.isDrawOn=false;                                                //停止线程绘制和停止在线程里改变变量的值
			pt.flag=false;                                                     //同上
			}
		return true;
		}                                                                      //结束方法
	}

