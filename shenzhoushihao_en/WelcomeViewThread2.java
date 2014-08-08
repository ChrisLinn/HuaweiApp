 
package com.example.shenzhoushihao;              //声明包语句

/*
 * 该类是为WelcomeView2服务的线程类，
 * 通过对WelcomeView2中status的判断执行不同的操作
 * 与WelcomeView2共同实现动画效果
 * 主要的操作是换帧，与修改图片坐标
 *
 */

public class WelcomeViewThread2 extends Thread
{
	private boolean flag = true;                 //循环标记位 
	private int span = 100;                      //循环睡眠时间
	WelcomeView2 welcomeView;                    //WelcomeView2的引用
	
	
	public WelcomeViewThread2(WelcomeView2 welcomeView)//构造器
	{
		this.welcomeView = welcomeView;          //得到welcomeView2的引用
	}
	
	
	public void setFlag(boolean flag)            //设置循环标记位 
	{
		this.flag = flag;
	}
	
	
	public void run()                            //重写的run方法 
	{
		while(flag)
		{
			if(welcomeView.status == 1)
			{
				welcomeView.backgroundY += 8;
				if(welcomeView.backgroundY>80)
				{
					welcomeView.status = 2;
				}
			}
			else if(welcomeView.status == 2)
			{
				welcomeView.k++;
				if(welcomeView.k == 11)
				{
					welcomeView.status = 3;
				}
			}
			else if(welcomeView.status == 3)
			{
				welcomeView.background2Y -= 2;
				welcomeView.alpha -= 2;
				if(welcomeView.alpha < 125)
				{
					welcomeView.alpha = 125;
				}
				if(welcomeView.background2Y < -90)
				{
					welcomeView.status = 4;
				}
			}
			try
			{
				Thread.sleep(span);              //睡眠制定毫秒数 
			}
			catch(Exception e)                   //捕获异常信息
			{
				e.printStackTrace();             //打印异常信息
			}
		}
	}
}