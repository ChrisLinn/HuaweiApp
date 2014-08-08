 
package com.example.shenzhoushihao;                 //声明包语句

public class PassThread extends Thread              //继承Thread类
{                   
	PassView father;                                //PassView的对象引用
	boolean flag;                                   //循环标志
	int sleepSpan = 50;                             //休眠时间,50毫秒
	
	public PassThread(PassView father)              //构造方法
	{                   
		this.father = father;                       //初始化成员方法
		flag = true;                                //循环标志默认为true
		}
	
	public void run()                               //线程的执行方法
	{                                               //启动线程
		while(flag)                                 //标志为true时
		{                    
			if(GameView.stage==0)
			{                                       //过关界面时
				father.leftX +=5;                   //物体的绘制坐标变化，+5，形成左右两侧的物体向中间飘
			    father.rightX-=5;                   //每循环一次-5
		    }
			if(father.father.gv.nextStage)          //如果是过关界面而不是失败界面
			{
				switch(father.stage)                //PassView中的stage
				{
				case 0:                             //第一关
					if(father.leftX==95)            //左边的物品到达中间时
						father.stage=6;             //执行PassView中的case 6，也就是绘制套中后的物品
					break;
				default:
					break;
					}
				try
				{
					Thread.sleep(sleepSpan);
					}								//线程休眠
			    catch(Exception e)
			    {
			    	e.printStackTrace();
			    	}							    //捕获并打印异常
			    }                                   //结束if语句
			}                                       //循环
		}                                           //方法
	}                                               //类
