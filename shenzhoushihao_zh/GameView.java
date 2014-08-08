 
package com.example.shenzhoushihao;                                            //声明包语句

//引入相关类
import java.util.HashMap;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.media.SoundPool;
import android.view.SurfaceHolder;
import android.view.SurfaceView;


public class GameView extends SurfaceView implements SurfaceHolder.Callback    //继承SurfaceView类实现SurfaceHolder和Callback接口
{      
	ShenzhoushihaoActivity father;					                           //ShenzhoushihaoActivity的引用
	static final int FLING_MIN_DISTANCE = 5;                                   //最小滑动距离常量，在判断是否不小心触及屏幕时使用。
	float startX=40,startY=17;                                                 //写introduce的起始坐标      
    String introduce="华为APP设计大赛参赛作品                    中国石油大学（华东）             “臭皮匠”队";//在游戏界面动态显示的参赛作品字样
	int taskArray[][]=new int [6][3];                                          //任务数组
	float x=95,y=270;                                                          //环起始位置
	float xx=0,yy=0;                                                           //x和y方向的速度
	int status=0;                                                              //控制环飞出去和环不在飞得两种状态
	Bitmap bmpCircle;                                                          //环
	int circleNumber;                                                          //环的数量      
	boolean isDrawCircle=true;                 
	public static int stage=0;                                                 //当前关卡默认为0
	boolean nextStage;                                                         //成功过关
	boolean failFlag;                                                          //闯关失败，也就是用尽了所有环还未过关
    BitmapData[] bitmapData=new BitmapData[6];                                 //BitmapData类型的数组，存放关卡的图片资源及其属性
    
    
	String sCircle;                                                            //环的个数的String类型的变量
	String s1;                                                                 //弹药   已搜集个数
	String s2;                                                                 //燃料   已搜集个数
	String s3;                                                                 //炸弹   已搜集个数	
	
	//线程
    TouchThread tt;
    TouchDrawThread tdt;
    
    float sca=0;                                                               //控制是绘制正常的环还是变形了的环，为0时绘制正常的环 
    float w=1.0f,h=1.0f;                                                       //控制环变形用的参数。默认是1的情况下环正常，w，h代表宽和高的放缩比列
    
    int initCircle=13;                                                         //当环数有更新时起作用
    SoundPool soundPool;		                                               //SoundPool对象引用
 	HashMap<Integer,Integer> soundMap;	                                       //存放声音资源的Map
 	
 	
	public GameView(ShenzhoushihaoActivity father)                             //构造方法
	{                 
		super(father);                                                         //初始化父类
		failFlag=false;                                                        //没有游戏失败，失败标志默认为false
		nextStage=false;                                                       //是否开启下一关的标志默认为false
		this.father = father;                                                  //初始化成员变量
		getHolder().addCallback(this);                                         //初始化
		initSound(father);                                                     //调用initSound初始化声音资源
		tt = new TouchThread(this);                                            //线程
		tdt = new TouchDrawThread(this,getHolder());                           //线程
		bmpCircle = BitmapFactory.decodeResource(this.getResources(), R.drawable.circle);//加载环的图片
		BitmapManager.loadCurrentStageResources(getResources(),stage);         //根据关卡号加载图片
		for(int i=0;i<6;i++)                                                   //任务数组初始化，起始都为零
			for(int j=0;j<3;j++)
				taskArray[i][j]=0;
		initBitmap();	                                                       //初始化本关所有图片及其属性及规定的环的数量
		}
	public void initBitmap()                                                   //初始化本关所有图片及其属性及规定的环的数量的方法   
	{
		switch(stage)
		{                                                                      //关卡号
		case 0:
			circleNumber=13;                                                   //第一关规定的环数,初始设置为13的原因：本关计分机制为在完成本关任务（套取坦克，弹药，燃料各一个）的前提下，以（剩余环数*10）为最后分数，初始定为13可使最好成绩为100分，符合常理 
			for(int i=0;i<10;i++)                                              //绘制背景图片及没有套中的图片
				BitmapManager.flag0[i]=true;                                   //设为true后便可以绘制
			for(int i=10;i<19;i++)                                             //需要临时切换的图片
				BitmapManager.flag0[i]=false;                                  //一开始不绘制，设为false
			bitmapData[0]=new BitmapData(BitmapManager.currentStageResources,                    
					                           BitmapManager.level0X, BitmapManager.level0Y,BitmapManager.flag0);      //初始化图片
			break;
			default:
			break;
			}
		}
	
	
		//方法：绘制屏幕
		@SuppressWarnings("static-access")
		public void doDraw(Canvas canvas)
		{                                                                      //touchDrawThread反复调用doDraw方法进行重绘
			Paint paint = new Paint();	                                       //创建画笔
			paint.setAlpha(255);                                               //亮度最大
			if(circleNumber>0)
			{
				switch(stage)
				{                                                              //关卡号
				case 0:                                                        //第一关
					for(int i=0;i<bitmapData[0].X.length;i++)                  //遍历数组，绘制每一副应该绘制的图片
						{
						if(bitmapData[0].flag[i])                              //绘制标志为true时便绘制
							{
							canvas.drawBitmap(bitmapData[0].bit[i], bitmapData[0].X[i],bitmapData[0].Y[i],paint);   //参数为Bitmap图片，x坐标，y坐标，画笔
							}
						}
			     break;
			     default:
			    	 break;
			    	 }                                                         //结束switch	
				}                                                              //环的数量大于零

			drawAll(canvas);                                                   //调用drawAll方法绘制任务进度，剩余环数，是哪一个关卡的字样，及上方的大赛规定字样
		
			if(failFlag||nextStage)
			{                                                                  //失败了或者成功了
				father.currentScore[stage]=(circleNumber-1)*10;                //计算当前关卡的得分
				if(WelcomeView.wantSound)                                      //声音标志为true时
				    stopSound(1);                                              //停止游戏的声音
				father.myHandler.sendEmptyMessage(6);                          //跳转到ShenzhoushihaoActivity中的handler中的case 6进入过关或失败界面
				soundPool.release();                                           //释放音乐池
				soundPool=null;                  
				tdt.isDrawOn = false;                                          //停止线程绘制图片
			    tt.flag=false;	                                               //停止线程绘制图片
			}
			isSuccess();                                                       //判断是否完成任务
		}
		
		
		public void drawAll(Canvas canvas)
		{
			Paint paint2 = new Paint();                                        //创建画笔pant2
			paint2.setAlpha(255);                                              //亮度
			paint2.setTextSize(16);                                            //字体大小
		    paint2.setTypeface(Typeface.DEFAULT_BOLD);	                       //设置粗体
		    paint2.setColor(Color.RED);                                        //设置颜色
			paint2.setAntiAlias(true);					                       //设置抗锯齿
			if(circleNumber!=0&&!nextStage)                                    //如果环数不为零而且没有过关
		        canvas.drawText(introduce, startX, startY, paint2);            //继续绘制大赛介绍字样
		    startX-=0.8;                                                       //字样没执行一次doDraw方法往左移0.8
		    if(startX<-750)                                                    //调试出来的数据，刚显示完所有字后，再一次从头再显示
			    startX=240;
			Paint paint = new Paint();	                                       //创建画笔paint
			paint.setAlpha(255);	                                           //亮度
			if(sca==1&&!tt.isRedraw)
			{                                                                  //环在飞行中且还没消失之前
				Paint paint1=new Paint();                                      //创建画笔
				Matrix m=new Matrix();                                         //声明矩阵
				m.preTranslate(x,y);                                           //下面进行矩阵变换
				m.preScale(w,h);
			    canvas.drawBitmap(bmpCircle, m ,paint1);                       //绘制变形了的环
			    if(circleNumber>1)                                             //环数大于1时
			    canvas.drawBitmap(bmpCircle, 95,270, paint);                   //绘制中间正下方的环
			}
			else if(sca==0&&tt.isRedraw)                                       //环没飞行且消失标志显示的是消失了
			{                                                                  //不作任何操作
			}
			else if(sca==1&&tt.isRedraw)                                       //环在飞行，这时候针对的是环已经消失了，但是线程还没将sca的值改回来
			{
				if(circleNumber>1)                                             //环数大于1时
			         canvas.drawBitmap(bmpCircle, 95,270, paint);              //绘制
			}
			else                                                               //否则
			{
				if(circleNumber>0)                                             //环数大于零时，主要是针对环数等于1时
			         canvas.drawBitmap(bmpCircle, 95,270, paint);              //绘制
			}
			
			//显示环的数量
			Paint paintCircle = new Paint();	                               //创建画笔，使用的是默认字体
			paintCircle.setTypeface(Typeface.DEFAULT_BOLD);	                   //设置粗体
			paintCircle.setColor(Color.RED);                                   //设置颜色
			paintCircle.setAntiAlias(true);					                   //设置抗锯齿
			sCircle=Integer.toString(circleNumber);                            //显示剩余环的个数
			canvas.drawText("环数x"+sCircle, 30, 317, paintCircle);
			if(stage==0)
			{                                                                  //绘制任务进度
			s1=Integer.toString(taskArray[stage][2]);                          //转换成字符串类型后，绘制右下角的物品搜集栏  ，下同
			s2=Integer.toString(taskArray[stage][1]);
			s3=Integer.toString(taskArray[stage][0]);
			
			//显示
			canvas.drawText("x"+s1, 159, 317, paintCircle);                    //在不同的坐标绘制，下同
			canvas.drawText("x"+s2, 188, 317, paintCircle);
			canvas.drawText("x"+s3, 216, 317, paintCircle);
			}
			canvas.drawText("第", 75, 285, paintCircle);                        //绘制是第几关的字样
			canvas.drawText("   "+Integer.toString(stage+1), 70, 300, paintCircle);//stage+1为当前关卡数：1
			canvas.drawText("关", 75, 315, paintCircle);
		}
		
		
		public void isSuccess()                                                //此关的任务分配
		{
			switch(stage)
			{
			case 0:
				if(taskArray[0][0]>=1&&taskArray[0][1]>=1&&taskArray[0][2]>=1) //第一关的任务，任务进度数组达到到时
					{
					nextStage=true;//过关标志为true
					//failFlag=true;
					}
				break;
				default:
					break;
			}
		}
		
		
		public void initSound(ShenzhoushihaoActivity father)                   //方法：初始化声音
		{                                                                      //initSound的方法体
			soundPool = new SoundPool(1000,AudioManager.STREAM_MUSIC,100);     //初始化soundPool,参数分别为允许同时播放的音乐最多为四个，第二个是类型，最后是音质
		    soundMap = new HashMap<Integer,Integer>();                         //初始化存放声音资源的哈希表
			int id1 = soundPool.load(father, R.raw.game_background, 1);        //加载声音，游戏背景音
			int id2 = soundPool.load(father, R.raw.game_cast, 2);              //划出的声音
			int id3 = soundPool.load(father, R.raw.game_hit, 3);               //套中的声音	
			int id4 = soundPool.load(father, R.raw.game_landing, 4);           //落地声音
			int id5= soundPool.load(father, R.raw.game_sink, 5);                 
			int id6= soundPool.load(father, R.raw.game_hit_sixth,6);           
			int id7= soundPool.load(father, R.raw.game_press, 7);              //按键声音
			soundMap.put(1, id1);	                                           //加入soundMap，指定id号
			soundMap.put(2, id2);	
			soundMap.put(3, id3);	
			soundMap.put(4, id4);
			soundMap.put(5, id5);
			soundMap.put(6, id6);
			soundMap.put(7, id7);
			}
		
		
		public void playSound(int sound,int loop)                              //方法：播放声音
		{                                                                      //重写系统的播放声音的方法，第一个参数是音乐的id，第二个是播放次数，-1为无限循环，0为一次，1为两次，以此类推
			if(WelcomeView.wantSound)
			{
				AudioManager am = (AudioManager)getContext().getSystemService(Context.AUDIO_SERVICE);
				float streamVolumeCurrent = am.getStreamVolume(AudioManager.STREAM_MUSIC);   
				float streamVolumeMax = am.getStreamMaxVolume(AudioManager.STREAM_MUSIC);       
				float volume = streamVolumeCurrent / streamVolumeMax;   	    
				soundPool.play(soundMap.get(sound), volume, volume, 1, loop, 1);//系统的播放声音的方法
				}
			}
		
		
		public void stopSound(int sound)                                       //方法：停止声音
		{                                                                      //传入id号           
			soundPool.stop(soundMap.get(sound));                               //在soundMap中找到id为sound的音乐
			}
		
		
		public void surfaceChanged(SurfaceHolder holder, int format, int width,int height)
		{                                                                      //重写接口surfaceChanged方法
		    }
		
		
		public void surfaceCreated(SurfaceHolder holder)
		{                                                                      //重写surfaceCreated方法，进入GameView时调用
			tdt.isDrawOn = true;                                               //绘制标志为true
			if(!tdt.isAlive())
			{                                                                  //如果没启动，则开启
				tdt.start();
				}
			tt.flag = true;                                                    //绘制标志为true
			if(!tt.isAlive())
			{                                                                  //如果没启动，则开启
				tt.start();
				}
			}
		
		
		public void surfaceDestroyed(SurfaceHolder holder)
		{                                                                      //重写surfaceDestroyed方法，离开GameView时自动调用
			tdt.isDrawOn = false;                                              //改变线程tdt标志，在TouchDrawThread类中不在重复绘制屏幕
			}
		}
	
