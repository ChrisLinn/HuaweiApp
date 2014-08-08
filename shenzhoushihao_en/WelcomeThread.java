 
package com.example.shenzhoushihao;                                            //声明包语句

//引入相关类
import android.graphics.Color; 

@SuppressWarnings("unused")
public class WelcomeThread extends Thread                                      //继承了Thread类    
{                       
	@SuppressWarnings("unchecked")
	WelcomeView father;                                                        //WelcomeView的对象引用
	boolean flag;                                                              //线程执行标志
	int sleepSpan = 50;                                                        //休眠时间
	int characterCounter=0;	                                                   //执行3次循环，才会加一个字
	int charNumber=0;                                                          //计算字数的计数器
	
	@SuppressWarnings("unchecked")
	public WelcomeThread(WelcomeView father)                                   //构造方法
	{          
		this.father = father;                                                  //初始化成员变量
		flag = true;                                                           //默认True，线程执行标志
		}
	
	@SuppressWarnings("unchecked")
	public void run()                                                          //线程的执行方法
	{                                                                          //run方法
		while(flag)
		{                                                                      //标志为true时
			switch(father.status)                                              //switch  WelcomeView中的status 
			{        
			case 0:                                                            //正在显示闪屏图片
				father.alpha -=2;                                              //逐渐变暗消失
				if(father.alpha <0)                                            //完全消失时
				{             
					father.status =8;                                          //跳到WelcomeView中的显示声音开关选择界面
					father.alpha=255;                                          //屏幕亮度到最亮
					}
				break;
		     case 8:                                                           //不执行任何操作
				break;
			 case 1:                                                           //正在滚动屏幕
				father.screenX -=20;                                           //每执行一次向左移20
				if(father.screenX<=0)                                          //移到一定位置时
				{           
					father.status = 3;                                         //开始显示文字
					}
			    break;
			 case 3:                                                           //显示文字
				 characterCounter++;                                           //每执行一次计数器加一
				 if(characterCounter == 3)                                     //等于三时
				 {                  
					 characterCounter = 0;                                     //置零
					 father.characterNumber++;                                 //WelcomeView中显示的字数加1
					 charNumber++;                                             //写字计数器加1
					 if(charNumber == father.str.length)                       //字数全部显示完成时
					 {	  			
						 father.status = 7;                                    //跳到WelconeView的case 7，绘制主功能界面
						 }					
					 }
				 break;
			 default:
				 break;					
				 }
			
				try
				{
					Thread.sleep(sleepSpan);     //线程休眠
					}										
				catch(Exception e)
				{
					e.printStackTrace();
					}                            //捕获并打印异常
				}
		}
	}

