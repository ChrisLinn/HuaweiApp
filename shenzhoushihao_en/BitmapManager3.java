 
package com.example.shenzhoushihao;                                                             //声明包语句

//引入相关类
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;


public class BitmapManager3 
{
	private static Bitmap[] welcomePublic;                                                      //存放系统总要用到的图片资源
	public static Bitmap[] currentStageResources;                                               //大背景和需要物品
	public static float[] level0X={0,240,385,110,240,40,210,240,385,110,240,40,210};            //本关的背景和物品图片的x坐标
	public static float[] level0Y={0,55,55,93,93,145,145,55,55,93,93,145,145};                  //本关的背景和物品图片的y坐标
	public static boolean[] flag0={true,true,true,true,true,true,true,false,false,false,false,false,false};
	private BitmapManager3(){}                                                                  //空构造方法
		
	//加载图片的方法，在GameView3中的构造方法中调用
	public static void loadCurrentStageResources(Resources r,int stage)
	{
		switch(stage)//加载图片
		{
		case 0:
			currentStageResources = new Bitmap[13];                                             //声明存放图片的数组并分配空间
			currentStageResources[0] = BitmapFactory.decodeResource(r, R.drawable.third_main);  //第三关场景
			currentStageResources[1] = BitmapFactory.decodeResource(r, R.drawable.third_stank); //小坦克
			currentStageResources[2] = BitmapFactory.decodeResource(r, R.drawable.third_stank); //小坦克
			currentStageResources[3] = BitmapFactory.decodeResource(r, R.drawable.third_mtank); //中坦克
		    currentStageResources[4] = BitmapFactory.decodeResource(r, R.drawable.third_mtank); //中坦克
			currentStageResources[5] = BitmapFactory.decodeResource(r, R.drawable.third_ltank); //大坦克
			currentStageResources[6] = BitmapFactory.decodeResource(r, R.drawable.third_ltank); //大坦克
						
			currentStageResources[7] = BitmapFactory.decodeResource(r, R.drawable.third_stanke); //爆炸后的小坦克
			currentStageResources[8] = BitmapFactory.decodeResource(r, R.drawable.third_stanke); //爆炸后的小坦克
			currentStageResources[9] = BitmapFactory.decodeResource(r, R.drawable.third_mtanke); //爆炸后的中坦克
			currentStageResources[10] = BitmapFactory.decodeResource(r, R.drawable.third_mtanke);//爆炸后的中坦克
			currentStageResources[11] = BitmapFactory.decodeResource(r, R.drawable.third_ltanke);//爆炸后的大坦克
			currentStageResources[12] = BitmapFactory.decodeResource(r, R.drawable.third_ltanke);//爆炸后的大坦克
			break;
		default:
			break;
		}
	}
	
	//加载欢迎界面的图片
	public static void loadWelcomePublic(Resources r)                                  //加载欢迎界面的图片
	{ 
		welcomePublic = new Bitmap[13];
		welcomePublic[0] = BitmapFactory.decodeResource(r, R.drawable.welcome_back3);  //欢迎界面背景
		welcomePublic[1] = BitmapFactory.decodeResource(r, R.drawable.ipad);           //ipad屏幕
		welcomePublic[2] = BitmapFactory.decodeResource(r, R.drawable.welcome_menu3);   //主菜单
		welcomePublic[3] = BitmapFactory.decodeResource(r, R.drawable.welcome_sound);  //开启或关闭声音的界面背景
		welcomePublic[6] = BitmapFactory.decodeResource(r, R.drawable.skip);           //“跳过”两个字
		welcomePublic[7] = BitmapFactory.decodeResource(r, R.drawable.txt_1);          //“开始”
		welcomePublic[8] = BitmapFactory.decodeResource(r, R.drawable.txt_2);          //“继续”
		welcomePublic[9] = BitmapFactory.decodeResource(r, R.drawable.txt_3);          //“英雄榜”
		welcomePublic[10] = BitmapFactory.decodeResource(r, R.drawable.txt_4);         //“帮助”
		welcomePublic[11] = BitmapFactory.decodeResource(r, R.drawable.txt_5);         //“关于”
		welcomePublic[12] = BitmapFactory.decodeResource(r, R.drawable.txt_6);         //“退出”
	}

	//方法：根据图片ID绘制系统欢迎界面图片
	public static void drawWelcomePublic(int imgId,Canvas canvas,int x,int y,Paint paint)
	{
		canvas.drawBitmap(welcomePublic[imgId], x, y, paint);
	}
}