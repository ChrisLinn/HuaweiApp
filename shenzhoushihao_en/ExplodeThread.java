 
package com.example.shenzhoushihao;              //声明包语句

//引入相关类
import java.util.ArrayList;

/*
 * 该类为爆炸的换帧线程
 * 没隔一段时间对GameView中explodeList中的爆炸换一帧
 *
 */

public class ExplodeThread extends Thread
{
	private boolean flag = true;                 //循环标记位 
	private int span = 100;                      //睡眠的毫秒数
	GameView2 gameView;                          //GameView2的引用
	ArrayList<Explode> deleteExplodes = new ArrayList<Explode>();//用于暂时存放需要删除的爆炸
	
	
	public ExplodeThread(GameView2 gameView)     //构造器
	{
		this.gameView = gameView;                //得到gameView的引用
	}
	
	
	public void setFlag(boolean flag)            //设置循环标记位
	{
		this.flag = flag;
	}
	
	
	public void run()                            //重写的run方法
	{
		while(flag)
		{
			try                                  //防止并发访问的异常
			{
				for(Explode e : gameView.explodeList)
				{
					if(e.nextFrame())
					{}
					else                         //当没有下一帧时删除该爆炸
					{
						deleteExplodes.add(e);
					}
				}
				gameView.explodeList.removeAll(deleteExplodes);
				deleteExplodes.clear();
			}
			catch(Exception e)                   //捕获异常信息
			{
				e.printStackTrace();             //打印异常信息
			}
			try
			{
				Thread.sleep(span);              //睡眠指定毫秒数
			}
			catch(Exception e)                   //捕获异常信息
			{
				e.printStackTrace();             //打印异常信息
			}
		}
	}
}