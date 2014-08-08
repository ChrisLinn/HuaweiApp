 
package com.example.shenzhoushihao;              //声明包语句

/*
 * 该类为常量类
 * 记录了相关常量参数
 *
 */

public class ConstantUtil 
{
	/*
	 * GameView2与GameViewBackGroundThread中用到的常量
	 */
	
	public static final int pictureWidth = 50;   //单元图的宽度
	public static final int pictureHeight = 300; //单元图的高度
	public static final int screenWidth = 480;   //屏幕宽度    
	public static final int screenHeight = 320;  //屏幕高度    
	public static final int pictureCount = 50;   //背景图片数量
	public static final int top = 1;             //距上边沿的距离
	
	/*
	 * 下面是游戏中所用到的方向常量
	 * 0静止，1上, 2右上，3右，4右下，5下，6左下，7左，8左上
	 */
	
	public static final int DIR_STOP = 0;
	public static final int DIR_UP = 1;
	public static final int DIR_RIGHT_UP = 2;
	public static final int DIR_RIGHT = 3;
	public static final int DIR_RIGHT_DOWN = 4;
	public static final int DIR_DOWN = 5;
	public static final int DIR_LEFT_DOWN = 6;
	public static final int DIR_LEFT = 7;
	public static final int DIR_LEFT_UP = 8;
	public static final double BooletSpan = 0.1;   //敌机发子弹的概率
	public static final double BooletSpan2 = 0.2;  //关口发子弹的概率
	public static final int life = 5;              //玩家飞机的生命,设置为5的原因:本关的计分机制为在完成本关任务的前提下，即歼灭敌方BOSS宇宙飞船后，以（剩余生命值*20）计算最后得分，设置为5可保证最后满分为100，符合常理
} 