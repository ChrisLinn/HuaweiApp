 
package com.example.shenzhoushihao;                                          //声明包语句

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

public class PassView3 extends SurfaceView implements SurfaceHolder.Callback //PassView3类继承了SurfaceView类，
                                                                             //实现了SurfaceHolder和Callback接口
{    
	TankActivity father;                                     //TankActivity的引用
	PassThread3 pt;                                          //PassThread3的引用                           
	PassDrawThread3 pdt;                                     //PassDrawThread3的引用
	int stage;                                               //记录当前状态：0关卡；10为相应的关卡对应的状态
	int alpha=255;                                           //亮度最大
	int leftX=-130;                                          //记录本关景物图片左上角坐标
	int rightX=300;                                          //变量记录炸弹图片左上角坐标
	Bitmap bmpBomb;                                          //存放炸弹的图片
	Bitmap gamePass;                                         //存放过关界面的图片 
	Bitmap gameOver;                                         //存放游戏失败的图片
	Bitmap gameSuccess1;                                     //通关界面的几张图片
	BitmapData[] bitmapData=new BitmapData[6];               //存放关卡
	Rect save        =new Rect(5,270,80,315);                //左下角按钮响应范围
	Rect movetonextlevel   =new Rect(180,280,235,315);       //右下角按钮响应范围
    SoundPool soundPool;		                             //SoundPool对象引用
	HashMap<Integer,Integer> soundMap;                       //存放声音资源的Map
	
	
	//构造器，初始化主要成员变量
	public PassView3(TankActivity father)                    //构造方法
	{                    
		super(father);                                       //初始化父类
		this.father = father;                                //初始化成员变量
		stage=GameView3.stage;                               //得到关卡号
		getHolder().addCallback(this);
		pt=new PassThread3(this);                            //两个线程
		pdt=new PassDrawThread3(this,getHolder());
		initSound(father);                                   //成员方法，初始化声音
		bmpBomb = BitmapFactory.decodeResource(this.getResources(), R.drawable.bomb);                //加载炸弹 的图片
		gameSuccess1= BitmapFactory.decodeResource(this.getResources(), R.drawable.game_success_1);  //加载通关的图片
		gamePass = BitmapFactory.decodeResource(father.getResources(), R.drawable.game_pass);        //加载过关 的图片
		gameOver = BitmapFactory.decodeResource(father.getResources(), R.drawable.game_over);        //加载炸弹失败的图片
		BitmapManager3.loadCurrentStageResources(getResources(),stage);                              //加载对应关卡的图片资源
		initBitmap();                                                                                //调用初始化图片的成员方法
	}
	
	//方法：绘制屏幕
	@SuppressWarnings("static-access")
	public void doDraw(Canvas canvas)                        //doDraw方法，在PassDrawThread3中反复调用
	{                     
		String currentScore=Integer.toString(father.currentScore[GameView3.stage]);    //显示本关卡得分的字符串
		Paint paint = new Paint();	                         //创建画笔paint
		paint.setAlpha(alpha);                               //设置亮度
		paint.setTextSize(22);                               //设置字体大小
	    paint.setTypeface(Typeface.DEFAULT_BOLD);	         //设置粗体
	    paint.setColor(Color.RED);                           //设置颜色
		paint.setAntiAlias(true);					         //设置抗锯齿
		
		if(father.gv.failFlag&&!father.gv.nextStage)         //如果剩余炸弹数为0，failFlag标志为true且过关标志为false
		{
		playSound(3,0);                                      //播放失败音乐
		canvas.drawBitmap(gameOver,0,0,null);                //绘制失败图片
		}                            
	else                                                     //反之成功
	{   
		playSound(2,0);                                      //播放过关音乐                              
        switch(stage)                        
        {                      
        case 0:                                              //第三关为0和10
        	canvas.drawBitmap(gameSuccess1, 0,0,null);       //绘制过关背景
			canvas.drawBitmap(bitmapData[4].bit[7], rightX+10,165,paint);//爆炸后的小坦克
			canvas.drawBitmap(bitmapData[4].bit[9], rightX+5,204,paint); //爆炸后的中坦克
			canvas.drawBitmap(bitmapData[4].bit[11], rightX,240,paint);  //爆炸后的大坦克
			break;			
		case 10:                                            //显示爆炸后的图片位置
			leftX=-130;                         
			rightX=300; 
            break;			
        default :
			break;
		}
		if(stage>3&&GameView3.stage!=3)
		{                                                        //第三关得分情况
			canvas.drawText("X"+father.gv.s3, 150, 183, paint);  //写完成任务的情况
		    canvas.drawText("X"+father.gv.s2, 150, 234, paint);
		    canvas.drawText("X"+father.gv.s1, 150, 275, paint);
		    canvas.drawText("本关得分：       ", 60, 140, paint);        //根据坐标位置写上规定的字
		    canvas.drawText(currentScore, 165, 140, paint);      //绘制当前得分      
		    Paint paint3 = new Paint();	                         //创建画笔
		    paint3.setAlpha(alpha);                              //亮度最大
		    paint3.setTextSize(28);                              //字体大小
	        paint3.setTypeface(Typeface.DEFAULT_BOLD);	         //设置粗体
	        paint3.setColor(Color.RED);                          //设置颜色
		    paint3.setAntiAlias(true);					         //设置抗锯齿
		    paint3.setTextSize(20);                              //重新设置字体大小
		    canvas.drawText("通关", 195, 315, paint3);            //根据坐标位置写上规定的字
		    }
	}
	}
		

	public void initBitmap()                                     //类对象的初始化
	{
		switch(stage)                                            //第三关关卡加载图片
		{             
		    case 0:
			    bitmapData[4]=new BitmapData(BitmapManager3.currentStageResources, BitmapManager3.level0X, BitmapManager3.level0Y,BitmapManager3.flag0);
		        break;		   
		    default:
			    break;
		}
	}
	
	//方法：初始化声音
	public void initSound(TankActivity father)                     //initSound的方法体
	{     
		soundPool = new SoundPool(1,AudioManager.STREAM_MUSIC,100);//初始化soundPool,参数分别为允许同时播放的音乐最多为1个，第二个是类型，最后是音质
		soundMap = new HashMap<Integer,Integer>();                 //初始化存放声音资源的哈希表
		int id1 = soundPool.load(father, R.raw.game_press, 1);     //按键音，给予id号
		int id2 = soundPool.load(father, R.raw.game_pass, 2);      //过关声音
		int id3 = soundPool.load(father, R.raw.game_fail, 3);      //闯关失败声音
		int id4 = soundPool.load(father, R.raw.game_success, 4);   //通关音乐
		soundMap.put(1, id1);	                                   //按照秩序存入声音池,与id号对应  
		soundMap.put(2, id2);	
		soundMap.put(3, id3);	
		soundMap.put(4, id4);			
		
	}
	
	//方法：播放声音
	public void playSound(int sound,int loop)                      //重写系统的播放声音的方法，第一个参数是音乐的id，
	                                                               //第二个是播放次数，-1为无限循环，0为一次，1为两次，以此类推
	{                    
		AudioManager am = (AudioManager)getContext().getSystemService(Context.AUDIO_SERVICE);
		float streamVolumeCurrent = am.getStreamVolume(AudioManager.STREAM_MUSIC);   
	    float streamVolumeMax = am.getStreamMaxVolume(AudioManager.STREAM_MUSIC);       
	    float volume = streamVolumeCurrent / streamVolumeMax;   	    
	    soundPool.play(soundMap.get(sound), volume, volume, 1, loop, 1);   //系统的播放声音的方法
	}
	
	//方法：停止声音
	public void stopSound(int sound)                                       //传入id号
	{                                
		soundPool.stop(soundMap.get(sound));                               //在soundMap中找到id为sound的音乐
	}	

	
	public void surfaceChanged(SurfaceHolder holder, int format, int width,int height)//重写接口surfaceChanged方法
	{}
	
	
	public void surfaceCreated(SurfaceHolder holder)                       //重写surfaceCreated方法，进入PassView3时调用
	{
		pdt.isDrawOn = true;                                               //绘制标志为true
		if(!pdt.isAlive())                                                 //如果没启动，则开启
		{         
			pdt.start();
		}
		pt.flag = true;                                                    //绘制标志为true
		if(!pt.isAlive())                                                  //如果没启动，则开启
		{            
			pt.start();
	     }		
	}
	

	public void surfaceDestroyed(SurfaceHolder holder)                     //重写surfaceDestroyed方法，离开PassView3时自动调用
	{                          
		pdt.isDrawOn = false;                                              //改变线程pdt标志，在PassDrawThread3类中不在重复绘制屏幕
	}
	
	//************PassView3中个触屏相应******************************************************
	@SuppressWarnings("unchecked")
	public boolean myTouchEvent(int x,int y)                               //触屏响应方法，在TankActivity中的OnTouchEvent的中调用
	{
			if(save.contains(x,y)&&(!father.gv.nextStage))                 //点击左下角区域，如果是失败了之后点击左下角的位置
			{
				
				if(GameView3.stage==0)                                     //本关
				{
                  if(WelcomeView3.wantSound)                               //声音开关为开时播放按键音
				     playSound(1,0);
                  father.vib.vibrate(new long[]{100,10,100,70},-1);        //振动
				   father.saveRecordDialog();                              //弹出保存得分纪录的对话框
				   pdt.isDrawOn=false;                                     //停止线程绘制和停止在线程里改变变量的值
			       pt.flag=false;                                          //同上
				}
			}
			
		else if(movetonextlevel.contains(x,y)&&(GameView3.stage!=5&&GameView3.stage!=0||(!father.gv.nextStage)))
			{                                                              //游戏通关回到第三关的开始，英雄榜...界面
				
				if(WelcomeView3.wantSound)                                 //声音为开时
					playSound(1,0);                                        //播放按键声音
				father.vib.vibrate(new long[]{100,10,100,70},-1);          //振动
				if(father.gv.nextStage)                                    //如果过关标志为true
				{  
					GameView3.stage++;                                     //关卡号加一
				}
				
				else                                                       //反之，是游戏失败，我们进行的是重玩操作，之前的积分清零
				{                            
				for(int i=0;i<6;i++)                                       //遍历清零
				     TankActivity.currentScore[i]=0;                       //本关的得分清零
			        }
				father.myHandler.sendEmptyMessage(7);                      //跳到TankActivity中的handler中的case 7，即进度条界面
				pdt.isDrawOn=false;                                        //停止线程绘制和停止在线程里改变变量的值
			     pt.flag=false;                                            //同上
	        } 
		else if(movetonextlevel.contains(x,y)&&GameView3.stage==0)         //点击右下角的区域，通关后，计数器达到上限，也就是动画以播放完毕
			{                          
			       if(WelcomeView3.wantSound)                              //如果声音开关为开
					   playSound(1,0);                                     //播放按键音
					father.setContentView(father.wv);			           //设置当前屏幕为WelcomeView3对象
					father.currentView = father.wv;	                       //记录当前View
					father.wv.status =1;                                   //显示游戏的通关界面
				}	
			return true;
    }                                                                      //结束方法
}


