
package com.example.shenzhoushihao;                                            //声明包语句

//引入相关类
import com.example.shenzhoushihao.GameView;               

class TouchThread extends Thread                                               //继承Thread类
{            
	GameView father;                                                           //GameView的对象引用
	boolean flag;                                                              //线程循环标志
	int sleepSpan = 100;                                                       //休眠时间
	boolean ifx,ify,ifMoveY,judge,isRedraw,ifKeep;                             //下面会用到的boolean变量
	float count1=0,count2=0,count3=40;                                         //计数器，并赋初值
	public static final float moveDistance=2.2f;                               //第一段飞行的移动常量，乘以某个数后是每一次循环环移动的x或y距离
	public static final float moveYDistance=0.6f;                              //第二段飞行的移动常量，
	
	boolean oneFlag=true;
	int number[]={20,20,20,20,20,20};                                          //本关被套中的物体的图片编号，初值是随便给的
	
	public TouchThread(GameView father)                                        //构造方法
	{             
		this.father = father;                                                  //初始化成员变量
		flag = true;                                                           //默认为true
	}
	
	
	public void run()                                                          //线程的执行方法
	{                                                                          //启动线程
		while(flag)                                                            //为true时，循环
		{                 
			switch(father.status)
			{                                                                  //在case 里改变status
			case 0:
				initMove();                                                    //初始化方法，初始化很多变量，见下面 
				break;
			case 1:	
				moveCircle();                                                  //本关的飞行方法
				break;
			default:
				break;
				}
			}
		}
	
	
	public void moveCircle()                                                   //飞行一共分为两段，第一段根据划出环的速度，后面那一段的飞行距离是自定义规定的
	{
		father.sca=1;                                                          //sca为1，画变形的环
		if(!ifx)
		{
			if(father.xx>0)                                                    //速度大于零
				{
				father.x+=moveDistance*Math.abs(father.xx/father.yy);          //往右飞，也就是横坐标加，乘以后面那个是为了x坐标和y坐标成比列增加，使环的飞行方向和手划出的方向一致
				father.w+=0.001;                                               //环的宽度增加0.001是调试出来的数字
				}
			else
			   {
				father.x-=moveDistance*Math.abs(father.xx/father.yy);          //往左飞，也就是横坐标减，乘以后面那个是为了x坐标和y坐标成比列减少，使环的飞行方向和手划出的方向一致
				father.w+=0.001;                                               //环的宽度增加
			   }
			}
		
		if(count2<Math.abs(father.yy))                                         //father.yy是计算后最大也就是和屏幕高度一样，飞行一段距离，保证环在屏幕上，不飞出屏幕
		{
			if(father.yy>0)                                                    //正方向在屏幕上滑动时
				{
				father.y+=moveDistance;                                        //y增加
				count2+=moveDistance;                                          //count2增加一定的量，
			   }
			else                                                               //负方向滑动时
			   { 
				father.y-=moveDistance;                                        //y减小
				count2+=moveDistance;                                          //count2增加一定的量
			   }
			}
		else                                                                   //如果count2达到father.yy的绝对值时
			{
			   ify=true;                                                       //置为true
			   ifx=true;                                                       //置为true，下一次循环不进行上面的第一次飞行，直接进入下面的第二次飞行
			 }
		
		//第二次飞行
		if(ify)                                                                //如果第一次飞行结束
			moveY();                                                           //再飞行一次
		if(ifMoveY)	                                                           //如果第二次飘结束了，进行判断
			{
			if(!judge)                                                         //如果还没调整
				{
				judgeLocation();                                               //进行调整，调整后judge为false
				if(ifKeep)  
				{                                                              //判断是否套中一个,	
					isRedraw=true;                                             //改变isReDraw为True
					try
					{
						Thread.sleep(800);                                     //物体被套中后停留的时间
						}
					catch(Exception e)
					{                                                          //捕获并打印异常
						e.printStackTrace();
						}
					}
				}
			else                                                               //调整过了
			{
				if(ifKeep)                                                     //如果套中了一个
					{
					                                                           //如果是第一关
					if(father.failFlag||father.nextStage)                      //如果过关或者失败了
					{                
						try
						{
							Thread.sleep(800);                                 //线程休眠，画面不再变化，800毫秒
						    }
						catch(Exception e)
						{                                                      //捕获异常
							e.printStackTrace();
						    }
						}
					father.bitmapData[GameView.stage].flag[number[GameView.stage]]=false;               //套中的图片消失
					}
				
				else                                                           //如果没套中
				{
					try
					{
						Thread.sleep(800);
						}
				    catch(Exception e)
				    {
				    	e.printStackTrace();
				    	}
				    }
				
				if(father.circleNumber>0)                                      //如果环数还大于0
					father.circleNumber--;                                     //环数减一
				father.status=0;                                               //回到静止状态，等待玩家下一次滑动屏幕上的环
				if(father.circleNumber==0)                                     //如果环数等于0
				{
					try
					{
						Thread.sleep(800);                                     //环等于零时在游戏界面停留的时间
						}
					catch(Exception e)
					{
						e.printStackTrace();
					    }
					father.failFlag=true;                                      //失败标志给位true
					}
				}
			}
		
		
		try
		{                                                                 
			Thread.sleep(3);                                                   //线程休眠
			}
		catch(Exception e)
		{
			e.printStackTrace();
			}
		
	}
	
	
	public void moveY()                                                        //第二次飘动
	{
		if(count3>20)                                                          //大于20
		{          
			father.y-=moveYDistance;                                           //向前飞
			if(father.xx>0)
				father.x+=moveYDistance*Math.abs(father.xx/father.yy);         //向右飞，同样是按比例的
			else
			    father.x-=moveYDistance*Math.abs(father.xx/father.yy);         //向左飞，同样是按比例的
			count3-=moveYDistance;                                             //count3初值为40，所以它减到20，表明y坐标减了20，在第二次的飞行过程中
			father.w-=Math.abs(father.yy/17500);                               //环的宽和高同时缩小                               
			father.h-=Math.abs(father.yy/12500);                                    
			}
		else
			ifMoveY=true;                                                      //改为true时表名第二次飞行结束
		} 
	
	
	public void initMove()                                                     //玩家滑动一次环后，换飞行结束后再次初始化所有需要的重置的变量
	{
		count1=0;
		count2=0;
		count3=40;
		father.sca=0;
		ifx=false;
		ify=false;
		ifMoveY=false;
		judge=false;
		isRedraw=false;
		ifKeep=false;
		father.w=1;
		father.h=1;
		father.x=95;
		father.y=270;
		father.sca=0;			
		}
	
	
	public void judgeLocation()
	{
		judge=true;                                                            //调整过一次就不调整了，为true时表明调整过了
		float distanceX=father.x+15;                                           //环心横坐标
		float distanceY=father.y+15;                                           //环心纵坐标
		switch(GameView.stage)                                                 //关卡号
		{
		case 0:                                                                //第一关
			float a0[]=father.bitmapData[0].X;                                 //得到每一张物品图片的x，y坐标
			float b0[]=father.bitmapData[0].Y;
			for(int i=1;i<10;i++)                                              //除了下标为0的背景图片，遍历
				{
			     //判断是否套中的方法，也是大约的一个计算。即环心离物品图片中心的距离通过坐标计算，距离的平方在一个范围里算套中，通过这个值逐渐增加每一关的难度
				if(father.bitmapData[0].flag[i]&&(distanceX-a0[i]-25)*(distanceX-a0[i]-25)+(distanceY-b0[i]-25)*(distanceY-b0[i]-25)<17*17)
				{
					father.playSound(3, 0);                                    //播放落地声音
					father.father.vib.vibrate(new long[]{100,10,100,200},-1);  //振动
					if(i>0&&i<4)                                               //右下角的计数机制
						father.taskArray[0][0]++;                              //对应的数组元素加一
					else if(i>3&&i<7)                                          //同上
						father.taskArray[0][2]++;
					else if(i>6&&i<10)                                         //同上
						father.taskArray[0][1]++;
			    	father.bitmapData[0].flag[i]=false;                        //不再绘制未套中时的物体图片
			    	father.bitmapData[0].flag[i+9]=true;                       //切换到套中后的物体的图片
			    	number[GameView.stage]=i+9;                                //改变数组元素的值，在飞行方法中会用到
			    	ifKeep=true;                                               //套中
			    	}
				else if(i==9&&!ifKeep)                                         //遍历到最后一个，如果有套中的，则播放播种的庆祝音效
					father.playSound(4, 0);                                    //播放
				} 
			break;		
		default:
			break;
			}
		}
	}

