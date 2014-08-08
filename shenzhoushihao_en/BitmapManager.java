 
package com.example.shenzhoushihao;                                            //声明包语句

//引入相关类
import android.content.res.Resources;		
import android.graphics.Bitmap;				
import android.graphics.BitmapFactory;		
import android.graphics.Canvas;				
import android.graphics.Paint;				


/*
 * 该类提供一些静态方法和成员，封装了游戏用到的所有图片资源，防止引用外泄，统一管理，易于维护
 *
 */

public class BitmapManager 
{
	private static Bitmap[] welcomePublic;                                     //存放系统总要用到的图片资源
	public static Bitmap[] currentStageResources;                              //大背景和需要搜集的物品
	
	public static float[] level0X={0,40,200,45,170,90,150,160,20,90 ,40,200,45,170,90,150,160,20,90 };                 //第一关的背景和物品图片的x坐标
	public static float[] level0Y={0, 160,80,30, 150,100,40 ,110,100,35 ,160,80,30, 150,100,40 ,110,100,35 };          //第一关的背景和物品图片的y坐标
	public static boolean[] flag0={true,true,true,true,true,true,true,true,true,true,false,false,false,false,false,false,false,false,false};    //绘制标志，true则绘制
	
	
	private BitmapManager(){}                                                  //空构造方法
	
	
	public static void loadCurrentStageResources(Resources r,int stage)        //加载图片的方法，在GameView中的构造方法中调用
	{
		switch(stage)
		{                                                                      //根据关卡号加载图片
		case 0:                                                                //第一关
					currentStageResources = new Bitmap[19];                    //声明存放图片的数组并分配空间 
					currentStageResources[0] = BitmapFactory.decodeResource(r, R.drawable.first_main);                 //第一关场景
					currentStageResources[1] = BitmapFactory.decodeResource(r, R.drawable.bomb1);                      //炸弹
					currentStageResources[2] = BitmapFactory.decodeResource(r, R.drawable.bomb1);                      //炸弹
					currentStageResources[3] = BitmapFactory.decodeResource(r, R.drawable.bomb1);                      //炸弹
					currentStageResources[4] = BitmapFactory.decodeResource(r, R.drawable.danyao);                     //弹药
					currentStageResources[5] = BitmapFactory.decodeResource(r, R.drawable.danyao);                     //弹药
					currentStageResources[6] = BitmapFactory.decodeResource(r, R.drawable.danyao);                     //弹药
					currentStageResources[7] = BitmapFactory.decodeResource(r, R.drawable.ranliao);                    //燃料
					currentStageResources[8] = BitmapFactory.decodeResource(r, R.drawable.ranliao);                    //燃料
					currentStageResources[9] = BitmapFactory.decodeResource(r, R.drawable.ranliao);                    //燃料
					
					currentStageResources[10] = BitmapFactory.decodeResource(r, R.drawable.first_fbomb);               //套圈后前面的炸弹
					currentStageResources[11] = BitmapFactory.decodeResource(r, R.drawable.first_mbomb);               //套圈后中间的炸弹
					currentStageResources[12] = BitmapFactory.decodeResource(r, R.drawable.first_bbomb);               //套圈后后面的炸弹
					currentStageResources[13] = BitmapFactory.decodeResource(r, R.drawable.first_fdanyao);             //套圈后前面的弹药
					currentStageResources[14] = BitmapFactory.decodeResource(r, R.drawable.first_mdanyao);             //套圈后中间的弹药
					currentStageResources[15] = BitmapFactory.decodeResource(r, R.drawable.first_bdanyao);             //套圈后后面的弹药
					currentStageResources[16] = BitmapFactory.decodeResource(r, R.drawable.first_mlranliao);           //套圈后中间的燃料
					currentStageResources[17] = BitmapFactory.decodeResource(r, R.drawable.first_mrranliao);           //套圈后后面的燃料
					currentStageResources[18] = BitmapFactory.decodeResource(r, R.drawable.first_branliao);            //套圈后后面的燃料
					break;
					default:
						break;
						}
			 }


public static void loadWelcomePublic(Resources r)//加载欢迎界面图片
{                                                                                                                      //加载欢迎界面的图片
	welcomePublic = new Bitmap[13];
	welcomePublic[0] = BitmapFactory.decodeResource(r, R.drawable.welcome_back);                                       //欢迎界面背景
	welcomePublic[1] = BitmapFactory.decodeResource(r, R.drawable.ipad);                                               //ipad背景作为出现文字的背景
	welcomePublic[2] = BitmapFactory.decodeResource(r, R.drawable.welcome_menu);                                       //主菜单
	welcomePublic[3] = BitmapFactory.decodeResource(r, R.drawable.welcome_sound);                                      //开启或关闭声音的界面背景
	welcomePublic[6] = BitmapFactory.decodeResource(r, R.drawable.skip);                                               //“跳过”两个字
	welcomePublic[7] = BitmapFactory.decodeResource(r, R.drawable.txt_1);                                              //“开始”
	welcomePublic[8] = BitmapFactory.decodeResource(r, R.drawable.txt_2);                                              //”继续“
	welcomePublic[9] = BitmapFactory.decodeResource(r, R.drawable.txt_3);                                              //“英雄榜”
	welcomePublic[10] = BitmapFactory.decodeResource(r, R.drawable.txt_4);                                             //“帮助”
	welcomePublic[11] = BitmapFactory.decodeResource(r, R.drawable.txt_5);                                             //“关于”
	welcomePublic[12] = BitmapFactory.decodeResource(r, R.drawable.txt_6);                                             //“退出”
}


public static void drawWelcomePublic(int imgId,Canvas canvas,int x,int y,Paint paint)                                  //方法：根据图片ID绘制系统欢迎界面图片
{
	canvas.drawBitmap(welcomePublic[imgId], x, y, paint);
	}
}

