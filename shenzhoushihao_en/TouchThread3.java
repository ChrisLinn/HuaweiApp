 
package com.example.shenzhoushihao;                              //���������

//���������
import com.example.shenzhoushihao.GameView3;               

public class TouchThread3 extends Thread                         //�̳�Thread��
{
	GameView3 father;                                            //GameView3�Ķ�������
	boolean flag;                                                //�߳�ѭ����־
	int sleepSpan = 100;                                         //����ʱ��
	boolean ifx,ify,ifMoveY,judge,isRedraw,ifKeep;               //������õ���boolean����
	float count1=0,count2=0,count3=40;                           //��������������ֵ
	public static final float moveDistance=2.2f;                 //��һ�η��е��ƶ�����������ĳ��������ÿһ��ѭ��ը���ƶ���x��y����
	public static final float moveYDistance=0.6f;                //�ڶ��η��е��ƶ�������
	
	boolean oneFlag=true;
	int number[]={20,20,20,20,20,20};                            //���ر�ը�е������ͼƬ��ţ���ֵ��������
	public TouchThread3(GameView3 father)                        //���췽��
	{             
		this.father = father;                                    //��ʼ����Ա����
		flag = true;                                             //Ĭ��Ϊtrue
	}
	
	
	//�̵߳�ִ�з���
	public void run()
	{                                                            //�����߳�
		while(flag)                                              //Ϊtrueʱ��ѭ��
		{                 
			switch(father.status)                                //��case ��ı�status
			{      
				case 0: 
					initMove();                                  //��ʼ����������ʼ���ܶ������������                             
					break;
				case 1:
					moveBomb();                                  //���صķ��з���
					break;
				default:
					break;
			}
		}
	}
	
	
	public void moveBomb()                                       //���صķ��з���������һ����Ϊ���Σ���һ�θ��ݻ���ը�����ٶȣ�������һ�εķ��о������Զ���涨��
		{
			father.sca=1;                                        //scaΪ1�������ε�ը��
			if(!ifx)
			{
			if(father.xx>0)                                      //�ٶȴ�����
			   {
				father.x+=moveDistance*Math.abs(father.xx/father.yy);//���ҷɣ�Ҳ���Ǻ�����ӣ����Ժ����Ǹ���Ϊ��x�����y����ɱ������ӣ�ʹը���ķ��з�����ֻ����ķ���һ��
				father.w+=0.001;                                  //ը���Ŀ�����ӣ�0.001�ǵ��Գ���������
			   }
			else
			   {
				father.x-=moveDistance*Math.abs(father.xx/father.yy);//����ɣ�Ҳ���Ǻ�����������Ժ����Ǹ���Ϊ��x�����y����ɱ��м��٣�ʹը���ķ��з�����ֻ����ķ���һ��
				father.w+=0.001;                                 //ը���Ŀ������
			   }
			}
			if(count2<Math.abs(father.yy))                       //father.yy�Ǽ�������Ҳ���Ǻ���Ļ�߶�һ��������һ�ξ��룬��֤ը������Ļ�ϣ����ɳ���Ļ
			{                   
				if(father.yy>0)                                  //����������Ļ�ϻ���ʱ
			    {
					father.y+=moveDistance;                      //y����
				    count2+=moveDistance;                        //count2����һ��������
			    }
			else                                                 //�����򻬶�ʱ
			{ 
				father.y-=moveDistance;                          //y��С
				count2+=moveDistance;                            //count2����һ������
			 }
		   }
			else                                                 //���count2�ﵽfather.yy�ľ���ֵʱ
			{
			    ify=true;                                        //��Ϊtrue
			    ifx=true;                                        //��Ϊtrue����һ��ѭ������������ĵ�һ�η��У�ֱ�ӽ�������ĵڶ��η���
			}
			
			//*****�ڶ��η���********
			if(ify)                                              //�����һ�η��н���
				moveY();                                         //�ٷ���һ��
			
			if(ifMoveY)	                                         //����ڶ���Ʈ�����ˣ������ж�
			{
				if(!judge)                                       //�����û����
				{     
					judgeLocation();                             //���е�����������judgeΪfalse
			        if(ifKeep) 
			       {                                             //�ж��Ƿ�ը��һ��,	
			        	isRedraw=true;                           //�ı�isReDrawΪTrue
			     	    try
			     	    {
			     	    	Thread.sleep(800);                   //���屻ը�к�ͣ����ʱ��
					    }
			     	    catch(Exception e)                       //���񲢴�ӡ�쳣
			     	    {                    
			     	    	e.printStackTrace();
					    }	
				    }
			    }
				else                                             //��������
				{               
					if(ifKeep)                                   //���ը����һ��
		            {
						if(GameView3.stage==0)                   //������ʱ
					    {
							father.bitmapData[4].X[number[4]-6]+=290;         //����ûը�е�̹�˵�ͼƬ��ǰ��x����+290��Ȼ����������Ļ�Ҷ˳���
						    father.bitmapData[4].flag[number[4]-6]=true;      //ûը�е�̹�˵�ͼƬ����
						    father.bitmapData[4].flag[number[4]]=false;       //ը�е�̹�˵�ͼƬ��ʧ
					    }
		    	    }
					else                                         //���ûը��
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
					if(father.bombNumber>0)                      //���ը����������0
					    father.bombNumber--;                     //ը������һ
				    father.status=0;                             //�ص���ֹ״̬���ȴ������һ�λ�����Ļ�ϵ�ը��
				    if(father.bombNumber==0)                     //���ը��������0
				    {
				    	try
				    	{
				    		Thread.sleep(800);                   //ը��������ʱ����Ϸ����ͣ����ʱ��
					    }
				    	catch(Exception e)
				    	{
				    		e.printStackTrace();
					    }
				    	father.failFlag=true;                    //ʧ�ܱ�־��λtrue
				    }
				}
			}
			try                                                  //�߳�����
			{                      
				Thread.sleep(3);
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
	
	
	public void moveY()                                          //�ڶ���Ʈ��
	{
		if(count3>20)                                            //����20
		{          
			father.y-=moveYDistance;                             //��ǰ��
			if(father.xx>0)
			    father.x+=moveYDistance*Math.abs(father.xx/father.yy);     //���ҷɣ�ͬ���ǰ�������
			else
			    father.x-=moveYDistance*Math.abs(father.xx/father.yy);     //����ɣ�ͬ���ǰ�������
			count3-=moveYDistance;                                         //count3��ֵΪ40������������20������y�������20���ڵڶ��εķ��й�����
			father.w-=Math.abs(father.yy/17500);                 //ը���Ŀ�͸�ͬʱ��С                               
			father.h-=Math.abs(father.yy/12500);                                    
		}
		else
			ifMoveY=true;                                        //��Ϊtrueʱ�����ڶ��η��н���
	} 
	

	public void initMove()                                       //��һ���һ��ը����ը�����н������ٴγ�ʼ��������Ҫ�����õı���
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
		judge=true;                                              //������һ�ξͲ������ˣ�Ϊtrueʱ������������
		float distanceX=father.x+15;                             //ը��������
		float distanceY=father.y+15;                             //ը��������
		switch(GameView3.stage)                                  //�ؿ���
		{
		case 0:                                                  //����
			float a0[]=father.bitmapData[4].X;                   //�õ�ÿһ����ƷͼƬ��x��y����
			float b0[]=father.bitmapData[4].Y;
			
			for(int i=1;i<7;i++)                                 //�����±�Ϊ0�ı���ͼƬ������
			{//�ж��Ƿ����еķ�����Ҳ�Ǵ�Լ��һ�����㡣��ը��������ƷͼƬ���ĵľ���ͨ��������㣬�����ƽ����һ����Χ�������У�ͨ�����ֵ������ÿһ�ص��Ѷ�				
				if(father.bitmapData[4].flag[i]&&(distanceX-a0[i]-26)*(distanceX-a0[i]-26)+(distanceY-b0[i]-15)*(distanceY-b0[i]-15)<16*15)
			    {
					father.playSound(3, 0);                      //�����������
					father.father.vib.vibrate(new long[]{100,10,100,200},-1);//��
					if(i==1||i==2)                    
						father.taskArray[0][0]++;
					else if(i==3||i==4)
						father.taskArray[0][1]++;
					else if(i==5||i==6)
						father.taskArray[0][2]++;
			    	father.bitmapData[4].flag[i]=false;
			    	father.bitmapData[4].flag[i+6]=true;
			    	number[4]=i+6;
			    	ifKeep=true;                              
			     }
				else if(i==6&&!ifKeep)
					father.playSound(4, 0);
			}
			break;		
		default:
			break;
		}
	}
}


		


