 
package com.example.shenzhoushihao;              //声明包语句

/* 
 * 该类为键盘监听线程类
 * 定时检测当前键盘的状态
 * 然后根据状态调用相应的处理 
 *
 */

public class KeyThread extends Thread
{
	int action;                                  //键盘状态码
	PlaneActivity activity;                      //PlaneActivity的引用
	private boolean flag = true;                 //循环标志
	int span = 20;                               //睡眠的毫秒数
	int countMove = 0;                           //飞机移动的计数器
	int countFine = 0;                           //飞机发子弹的计数器
	int moveN = 3;                               //每三次循环移动一下
	int fineN = 5;                               //每五次循环发一次子弹
		
	private boolean KEY_UP = false;              //向上键是否被按下
	private boolean KEY_DOWN = false;            //向下键是否被按下
	private boolean KEY_LEFT = false;            //向左的键是否被按下
	private boolean KEY_RIGHT = false;           //向右的键是否被按下
	private boolean KEY_A = false;               //A的键是否被按下
	
	
	public KeyThread(PlaneActivity activity)     //构造器
	{
		this.activity = activity;                //得到activity的引用
	}
	
	
	public void setFlag(boolean flag)            //设置标志位
	{
		this.flag = flag;
	}
	
	
	public void run()                            //重写的方法
	{
		while(flag)
		{
			action = activity.action;            //得到当前键盘的状态码
			if((action & 0x20) != 0)             //向上键
			{
				KEY_UP = true;
			}
			else
			{
				KEY_UP = false;
			}
			if((action & 0x10) != 0)             //向下键
			{
				KEY_DOWN = true;
			}
			else
			{
				KEY_DOWN = false;
			}
			if((action & 0x08) != 0)             //向左键
			{
				KEY_LEFT = true;
			}
			else
			{
				KEY_LEFT = false;
			}
			if((action & 0x04) != 0)             //向右键
			{
				KEY_RIGHT = true;
			}
			else
			{
				KEY_RIGHT = false;
			}
			if((action & 0x02) != 0)             //A键
			{
				KEY_A = true;
			}
			else
			{
				KEY_A = false;
			}
			if(activity.gameView.status == 1 || activity.gameView.status == 3)
			{
				if(countMove == 0)               //每moveN次移动一次
				{
					if(KEY_UP == true)           //向上键被按下
					{
						if(!((activity.gameView.plane.getY() - activity.gameView.plane.getSpan()) < ConstantUtil.top))
						{
							activity.gameView.plane.setY(activity.gameView.plane.getY() - activity.gameView.plane.getSpan());
						}
						activity.gameView.plane.setDir(ConstantUtil.DIR_UP);
					}
					if(KEY_DOWN == true)         //向下键被按下
					{
						if(!((activity.gameView.plane.getY() + activity.gameView.plane.getSpan()) > ConstantUtil.screenHeight - activity.gameView.plane.bitmap1.getHeight()))
						{
							activity.gameView.plane.setY(activity.gameView.plane.getY() + activity.gameView.plane.getSpan());
						}
						activity.gameView.plane.setDir(ConstantUtil.DIR_DOWN);
					}
					if(KEY_LEFT == true)         //向左键被按下
					{
						if(!((activity.gameView.plane.getX() - activity.gameView.plane.getSpan()) < -40))
						{
							activity.gameView.plane.setX(activity.gameView.plane.getX() - activity.gameView.plane.getSpan());
						}
					}
					if(KEY_RIGHT == true)        //向右键被按下
					{
						if(!((activity.gameView.plane.getX() + activity.gameView.plane.getSpan()) > ConstantUtil.screenWidth - activity.gameView.plane.bitmap1.getWidth()))
						{
							activity.gameView.plane.setX(activity.gameView.plane.getX() + activity.gameView.plane.getSpan());
						}
					}
					if(KEY_RIGHT == false && KEY_LEFT == false && KEY_DOWN == false && KEY_UP == false)
					{
						activity.gameView.plane.setDir(ConstantUtil.DIR_STOP);
					}
					if(countFine == 0)           //每fineN发一次子弹
					{
						if(KEY_A == true)        //A键被按下
						{
							activity.gameView.plane.fire();
						}
					}
				}
				countMove = (countMove+1)%moveN;
				countFine = (countFine+1)%fineN;
			}
			try
			{
				Thread.sleep(span);              //睡觉指定毫秒数
			}
			catch(Exception e)                   //捕获异常信息
			{
				e.printStackTrace();             //打印异常信息
			}
		}
	}
}