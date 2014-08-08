 
package com.example.shenzhoushihao;                  //声明包语句

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

public class GameView3 extends SurfaceView implements SurfaceHolder.Callback//继承SurfaceView类实现SurfaceHolder和Callback接口
{      
	TankActivity father;					         //TankActivity的引用
	static final int FLING_MIN_DISTANCE = 5;         //最小滑动距离常量，在判断是否不小心触及屏幕时使用。
	float startX=40,startY=17;                       //写introduce的起始坐标      
    String introduce="华为APP设计大赛参赛作品                    中国石油大学（华东）             “臭皮匠”队";//在游戏界面动态显示的参赛作品字样
	int taskArray[][]=new int [6][3];                //任务数组
	float x=95,y=270;                                //炸弹起始位置
	float xx=0,yy=0;                                 //x和y方向的速度
	int status=0;                                    //控制炸弹飞出去和炸弹不在飞得两种状态
	Bitmap bmpBomb;                                  //炸弹
	int bombNumber;                                  //炸弹的数量      
	boolean isDrawCircle=true;                 
	public static int stage=0;                       //当前关卡默认为0
	boolean nextStage;                               //成功过关
	boolean failFlag;                                //闯关失败，也就是用尽了所有炸弹还未过关
    BitmapData[] bitmapData=new BitmapData[6];       //BitmapData类型的数组，存放关卡的图片资源及其属性
    
    String sBomb;                                    //炸弹的个数的String类型的变量
	String s1;                                       //小坦克   已炸中个数
	String s2;                                       //中坦克   已炸中个数
	String s3;                                       //大坦克   已炸中个数	
	
	//线程
    TouchThread3 tt;
    TouchDrawThread3 tdt;
    
    float sca=0;                                     //控制是绘制正常的炸弹还是变形了的炸弹，为0时绘制正常的炸弹 
    float w=1.0f,h=1.0f;                             //控制炸弹变形用的参数。默认是1的情况下炸弹正常，w，h代表宽和高的放缩比列
    
    int initBomb=50;                                 //当炸弹数有更新时起作用
    SoundPool soundPool;		                     //SoundPool对象引用
 	HashMap<Integer,Integer> soundMap;	             //存放声音资源的Map
 	
 	
 	public GameView3(TankActivity father)            //构造方法
 	{                
		super(father);                               //初始化父类
		failFlag=false;                              //没有游戏失败，失败标志默认为false
		nextStage=false;                             //是否开启下一关的标志默认为false
		this.father = father;                        //初始化成员变量
		getHolder().addCallback(this);               //初始化
		initSound(father);                           //调用initSound初始化声音资源
		tt = new TouchThread3(this);                 //线程
		tdt = new TouchDrawThread3(this,getHolder());//线程
		bmpBomb = BitmapFactory.decodeResource(this.getResources(), R.drawable.bomb);//加载炸弹的图片
		BitmapManager3.loadCurrentStageResources(getResources(),stage);              //加载图片
		for(int i=0;i<6;i++)                         //任务数组初始化，起始都为零
			for(int j=0;j<3;j++)
				taskArray[i][j]=0;
		initBitmap();	                             //初始化本关所有图片及其属性及规定的炸弹的数量
	}
 	
 	//初始化本关所有图片及其属性及规定的环的数量的方法
 	public void initBitmap()    
 	{
 		switch(stage)                                //关卡号
 		{
 		case 0:
 			bombNumber=50;                           //本关规定的炸弹数
 			for(int i=0;i<7;i++)                                                  
 				BitmapManager3.flag0[i]=true;
 			for(int i=7;i<13;i++)
 				BitmapManager3.flag0[i]=false;
 			BitmapManager3.level0X[1]=240;           //六辆坦克的初始x坐标                                           
 			BitmapManager3.level0X[2]=385; 
 			BitmapManager3.level0X[3]=110; 
 			BitmapManager3.level0X[4]=240; 
 			BitmapManager3.level0X[5]=40; 
 		    BitmapManager3.level0X[6]=210; 
 			bitmapData[4]=new BitmapData(BitmapManager3.currentStageResources,
 						        BitmapManager3.level0X, BitmapManager3.level0Y,BitmapManager3.flag0);
 			break; 					
 		default:
 			break;
 		}
 	}
 	
     //方法：绘制屏幕
 	@SuppressWarnings("static-access")
 	public void doDraw(Canvas canvas)                //touchDrawThread反复调用doDraw方法进行重绘
 	{  
 		Paint paint = new Paint();	                 //创建画笔
 		paint.setAlpha(255);                         //亮度最大
 		if(bombNumber>0)
 		{
 			switch(stage)
 			{                                        //关卡号
 			case 0:                                                                  
 				canvas.drawBitmap(bitmapData[4].bit[0], bitmapData[4].X[0],bitmapData[4].Y[0],paint);            //先单独绘制背景图片
 				for(int i=1;i<bitmapData[4].X.length;i++)//遍历所有图片
 				{
 					if(bitmapData[4].flag[i])        //标志为true的都绘制
 						{
 							canvas.drawBitmap(bitmapData[4].bit[i], bitmapData[4].X[i],bitmapData[4].Y[i],paint);//绘制
 							if(!tt.ifKeep)           //如果没有炸中坦克
 							moveTank(i);             //继续是调用moveTank方法使坦克运功
 						 }
 				}
 				break;
 		   default:
 				break;
 				                                     //结束switch	
 			}                                        //炸弹的数量大于零
 					
 		}
 		drawAll(canvas);                             //调用drawAll方法绘制任务进度，剩余炸弹数，关卡的字样，及上方的大赛规定字样
 					
 		if(failFlag||nextStage)                      //失败或者成功
 		{                                   
 			father.currentScore[stage]=(bombNumber-1)*10;//计算当前关卡的得分
 			if(WelcomeView3.wantSound)               //声音标志为true时
 				stopSound(1);                        //停止游戏的声音
 			father.myHandler.sendEmptyMessage(6);    //跳转到TankActivity中的handler中的case 6进入过关或失败界面
 			soundPool.release();                     //释放音乐池
 			soundPool=null;                  
 			tdt.isDrawOn = false;                    //停止线程绘制图片
 			tt.flag=false;	                         //停止线程绘制图片
 		}
 		isSuccess();                                 //判断是否完成任务
 	}
 	
 	
 	public void drawAll(Canvas canvas)
 	{
 		Paint paint2 = new Paint();                  //创建画笔pant2
 		paint2.setAlpha(255);                        //亮度
 		paint2.setTextSize(16);                      //字体大小
 		paint2.setTypeface(Typeface.DEFAULT_BOLD);	 //设置粗体
 		paint2.setColor(Color.BLUE);                 //设置颜色
 		paint2.setAntiAlias(true);					 //设置抗锯齿
 		if(bombNumber!=0&&!nextStage)                //如果炸弹数不为零而且没有过关
 			canvas.drawText(introduce, startX, startY, paint2);//继续绘制大赛介绍字样
 		startX-=0.8;                                 //字样没执行一次doDraw方法往左移0.8
 	    if(startX<-750)                              //调试出来的数据，刚显示完所有字后，再一次从头再显示
 			startX=240;
 		Paint paint = new Paint();	                 //创建画笔paint
 		paint.setAlpha(255);	                     //亮度
 		if(sca==1&&!tt.isRedraw)                     //炸弹在飞行中且还没消失之前
 		{               
 			Paint paint1=new Paint();                //创建画笔
 			Matrix m=new Matrix();                   //声明矩阵
 			m.preTranslate(x,y);                     //下面进行矩阵变换
 			m.preScale(w,h);
 			canvas.drawBitmap(bmpBomb, m ,paint1);   //绘制变形了的炸弹
 			if(bombNumber>1)                         //炸弹数大于1时
 				canvas.drawBitmap(bmpBomb, 95,270, paint);//绘制中间正下方的炸弹
 		}
 		else if(sca==0&&tt.isRedraw)                 //炸弹没飞行且消失标志显示的是消失了
 		{}       
 			                                         //不作任何操作
 		else if(sca==1&&tt.isRedraw)                 //炸弹在飞行，这时候针对的是炸弹已经消失了，但是线程还没将sca的值改回来
 		{
 			if(bombNumber>1)                         //炸弹数大于1时
 				canvas.drawBitmap(bmpBomb, 95,270, paint);//绘制
 		}
 		else                                         //否则，
 		{
 			if(bombNumber>0)                         //炸弹数大于零时，主要是针对炸弹数等于1时
 				canvas.drawBitmap(bmpBomb, 95,270, paint);//绘制
 		}
 		//显示炸弹的数量
 		Paint paintCircle = new Paint();	         //创建画笔，使用的是默认字体
 		paintCircle.setTypeface(Typeface.DEFAULT_BOLD);//设置粗体
 		paintCircle.setColor(Color.RED);             //设置颜色
 		paintCircle.setAntiAlias(true);			     //设置抗锯齿
 		sBomb=Integer.toString(bombNumber);          //显示剩余炸弹的个数
 		canvas.drawText("炸弹x"+sBomb, 30, 317, paintCircle);
 		if(stage!=5)
 		{                                            //绘制任务进度
 			s1=Integer.toString(taskArray[stage][2]);       //转换成字符串类型后，绘制右下角的进度  ，下同
 		    s2=Integer.toString(taskArray[stage][1]);
 		    s3=Integer.toString(taskArray[stage][0]);
 		    //显示
 		    canvas.drawText("x"+s1, 159, 317, paintCircle); //在不同的坐标绘制，下同
 		    canvas.drawText("x"+s2, 188, 317, paintCircle);
 		    canvas.drawText("x"+s3, 216, 317, paintCircle);
 	     }
 		canvas.drawText("第", 75, 285, paintCircle);         //绘制是第几关的字样
 		canvas.drawText("   "+Integer.toString(stage+3), 70, 300, paintCircle);//第三关
 		canvas.drawText("关", 75, 315, paintCircle);
 	}
 	
 	
 	public void isSuccess()                                 //本关的任务分配
 	{
 		switch(stage)
 		{
 		case 0:
 			if(taskArray[0][0]>=4&&taskArray[0][1]>=3&&taskArray[0][2]>=2)//本关的任务
 				nextStage=true;
 			break;
 		default:
 			break;
 		}
 	}
 	
 	//方法：初始化声音
	public void initSound(TankActivity father)//initSound的方法体
	{
		soundPool = new SoundPool(1000,AudioManager.STREAM_MUSIC,100);          //初始化soundPool,参数分别为允许同时播放的音乐最多为四个，第二个是类型，最后是音质
		soundMap = new HashMap<Integer,Integer>();                              //初始化存放声音资源的哈希表
		int id1 = soundPool.load(father, R.raw.game_background, 1);             //加载声音，游戏背景音
		int id2 = soundPool.load(father, R.raw.bulletsound1, 2);                //发射炸弹的声音
		int id3 = soundPool.load(father, R.raw.explode, 3);                     //爆炸的声音	
		int id4= soundPool.load(father, R.raw.game_landing, 4);                 //落地声音
		int id5= soundPool.load(father, R.raw.game_sink, 5);                    //落水的声音
		int id6= soundPool.load(father, R.raw.game_hit_sixth, 6);               //第六关套中的声音
		int id7= soundPool.load(father, R.raw.game_press, 7);                   //按键声音
		soundMap.put(1, id1);	                                                //加入soundMap，指定id号
		soundMap.put(2, id2);	
		soundMap.put(3, id3);	
		soundMap.put(4, id4);
		soundMap.put(5, id5);	
		soundMap.put(6, id6);
		soundMap.put(7, id7);	
	}
	
	//方法：播放声音
	public void playSound(int sound,int loop)               //重写系统的播放声音的方法，第一个参数是音乐的id，
	                                                        //第二个是播放次数，-1为无限循环，0为一次，1为两次，以此类推
	{                                                                           
		if(WelcomeView3.wantSound)
		{
			AudioManager am = (AudioManager)getContext().getSystemService(Context.AUDIO_SERVICE);
			float streamVolumeCurrent = am.getStreamVolume(AudioManager.STREAM_MUSIC);   
			float streamVolumeMax = am.getStreamMaxVolume(AudioManager.STREAM_MUSIC);       
			float volume = streamVolumeCurrent / streamVolumeMax;   	    
			soundPool.play(soundMap.get(sound), volume, volume, 1, loop, 1);//系统的播放声音的方法
		}
	}
	
	//方法：停止声音
	public void stopSound(int sound)                       //传入id号
	{                               
		soundPool.stop(soundMap.get(sound));               //在soundMap中找到id为sound的音乐
	}	
	
	
	public void surfaceChanged(SurfaceHolder holder, int format, int width,int height)//重写接口surfaceChanged方法
	{}                                                   
	
	
	public void surfaceCreated(SurfaceHolder holder)
	{                                                      //重写surfaceCreated方法，进入GameView3时调用
		tdt.isDrawOn = true;                               //绘制标志为true
		if(!tdt.isAlive())                                 //如果没启动，则开启
		{         
			tdt.start();
		}
		tt.flag = true;                                    //绘制标志为true
		if(!tt.isAlive())
		{                                                  //如果没启动，则开启
			tt.start();
		}		
	}

	
	public void surfaceDestroyed(SurfaceHolder holder) 
	{                                                      //重写surfaceDestroyed方法，离开GameView3时自动调用
		tdt.isDrawOn = false;                              //改变线程tdt标志，在TouchDrawThread类中不在重复绘制屏幕
	}
	
	
	public void moveTank(int i)                            //坦克移动方法的方法体
	{
		if(i==1||i==2)                                     //两辆小坦克
		{
			bitmapData[4].X[i]-=0.3;                       //移动最慢，一次0.2
		}
		else if(i==3||i==4)
		{                                                  //两辆中型坦克
			bitmapData[4].X[i]-=0.6;                       //移动速度也是中等的
		}
		else if(i==5||i==6)
		{                                                  //大坦克移动最快
			bitmapData[4].X[i]-=1;                         //一次减一
		}
		bitmapData[4].X[i+6]=bitmapData[4].X[i];           //爆炸后坦克同时改变x坐标，爆炸中时切换图片时方便
		if(bitmapData[4].X[i]<-44)                         //走到最左端置坦克全身都消失时
			bitmapData[4].X[i]=260;                        //从最右端出来
    }

}
	
