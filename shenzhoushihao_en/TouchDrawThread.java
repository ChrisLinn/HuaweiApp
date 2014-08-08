 
package com.example.shenzhoushihao;                                            //声明包语句
 
//引入相关类
import android.graphics.Canvas;          
import android.view.SurfaceHolder;

/*
 * 此类属于典型的DrawThread类，与本游戏中的WelcomeDrawThread和PassDrawThread类完全一样，实现的功能也是开启一个线程后，通过
 * 调用doDraw方法不断重回整个屏幕，由于牵涉到不同的View，线程的启动和停止，为了方便编程，所以这才没把这三个合并
 *
 */

public class TouchDrawThread extends Thread                                    //继承Thread
{                      
	GameView father;                                                           //GameView对象引用
	SurfaceHolder surfaceHolder;                                               //SurfaceHolder对象引用
	boolean flag;                                                              //循环控制变量
	boolean isDrawOn;                                                          //内层循环变量
	
	public TouchDrawThread(GameView father,SurfaceHolder surfaceHolder)        //构造方法
	{         
		super.setName("==TouchDrawThread");                                    //初始化父类
		this.father = father;                                                  //初始化成员变量
		this.surfaceHolder = surfaceHolder;                                    //初始化成员变量      
		flag = true;                                                           //初始化循环控制变量为true
		isDrawOn = true;                                                       //初始化内层循环变量为true
		}
	
	
	public void run()                                                          //线程执行方法
	{                                                 
		while(flag)                                                            //启动线程
		{                                                                      //外层循环
			Canvas canvas = null;
			while(isDrawOn)
			{                                                                  //内层寻黄
				try
				{
					canvas = surfaceHolder.lockCanvas(null);                   //锁定画布
					synchronized(surfaceHolder)
					{
						father.doDraw(canvas);                                 //绘制屏幕
						}
					}
				catch(Exception e)
				{
					e.printStackTrace();
					}
				finally
				{                                                              //释放画布
					if(canvas != null)                                         //画布不为NULL时
					{                    
						surfaceHolder.unlockCanvasAndPost(canvas);             //释放画布
						}
					}
				}
			}
		}
	}
