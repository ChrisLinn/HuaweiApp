 
package com.example.shenzhoushihao;                                            //���������

//���������
import com.example.shenzhoushihao.GameView;               

class TouchThread extends Thread                                               //�̳�Thread��
{            
	GameView father;                                                           //GameView�Ķ�������
	boolean flag;                                                              //�߳�ѭ����־
	int sleepSpan = 100;                                                       //����ʱ��
	boolean ifx,ify,ifMoveY,judge,isRedraw,ifKeep;                             //������õ���boolean����
	float count1=0,count2=0,count3=40;                                         //��������������ֵ
	public static final float moveDistance=2.2f;                               //��һ�η��е��ƶ�����������ĳ��������ÿһ��ѭ�����ƶ���x��y����
	public static final float moveYDistance=0.6f;                              //�ڶ��η��е��ƶ�������
	
	boolean oneFlag=true;
	int number[]={20,20,20,20,20,20};                                          //���ر����е������ͼƬ��ţ���ֵ��������
	
	public TouchThread(GameView father)                                        //���췽��
	{             
		this.father = father;                                                  //��ʼ����Ա����
		flag = true;                                                           //Ĭ��Ϊtrue
	}
	
	
	public void run()                                                          //�̵߳�ִ�з���
	{                                                                          //�����߳�
		while(flag)                                                            //Ϊtrueʱ��ѭ��
		{                 
			switch(father.status)
			{                                                                  //��case ��ı�status
			case 0:
				initMove();                                                    //��ʼ����������ʼ���ܶ������������ 
				break;
			case 1:	
				moveCircle();                                                  //���صķ��з���
				break;
			default:
				break;
				}
			}
		}
	
	
	public void moveCircle()                                                   //����һ����Ϊ���Σ���һ�θ��ݻ��������ٶȣ�������һ�εķ��о������Զ���涨��
	{
		father.sca=1;                                                          //scaΪ1�������εĻ�
		if(!ifx)
		{
			if(father.xx>0)                                                    //�ٶȴ�����
				{
				father.x+=moveDistance*Math.abs(father.xx/father.yy);          //���ҷɣ�Ҳ���Ǻ�����ӣ����Ժ����Ǹ���Ϊ��x�����y����ɱ������ӣ�ʹ���ķ��з�����ֻ����ķ���һ��
				father.w+=0.001;                                               //���Ŀ������0.001�ǵ��Գ���������
				}
			else
			   {
				father.x-=moveDistance*Math.abs(father.xx/father.yy);          //����ɣ�Ҳ���Ǻ�����������Ժ����Ǹ���Ϊ��x�����y����ɱ��м��٣�ʹ���ķ��з�����ֻ����ķ���һ��
				father.w+=0.001;                                               //���Ŀ������
			   }
			}
		
		if(count2<Math.abs(father.yy))                                         //father.yy�Ǽ�������Ҳ���Ǻ���Ļ�߶�һ��������һ�ξ��룬��֤������Ļ�ϣ����ɳ���Ļ
		{
			if(father.yy>0)                                                    //����������Ļ�ϻ���ʱ
				{
				father.y+=moveDistance;                                        //y����
				count2+=moveDistance;                                          //count2����һ��������
			   }
			else                                                               //�����򻬶�ʱ
			   { 
				father.y-=moveDistance;                                        //y��С
				count2+=moveDistance;                                          //count2����һ������
			   }
			}
		else                                                                   //���count2�ﵽfather.yy�ľ���ֵʱ
			{
			   ify=true;                                                       //��Ϊtrue
			   ifx=true;                                                       //��Ϊtrue����һ��ѭ������������ĵ�һ�η��У�ֱ�ӽ�������ĵڶ��η���
			 }
		
		//�ڶ��η���
		if(ify)                                                                //�����һ�η��н���
			moveY();                                                           //�ٷ���һ��
		if(ifMoveY)	                                                           //����ڶ���Ʈ�����ˣ������ж�
			{
			if(!judge)                                                         //�����û����
				{
				judgeLocation();                                               //���е�����������judgeΪfalse
				if(ifKeep)  
				{                                                              //�ж��Ƿ�����һ��,	
					isRedraw=true;                                             //�ı�isReDrawΪTrue
					try
					{
						Thread.sleep(800);                                     //���屻���к�ͣ����ʱ��
						}
					catch(Exception e)
					{                                                          //���񲢴�ӡ�쳣
						e.printStackTrace();
						}
					}
				}
			else                                                               //��������
			{
				if(ifKeep)                                                     //���������һ��
					{
					                                                           //����ǵ�һ��
					if(father.failFlag||father.nextStage)                      //������ػ���ʧ����
					{                
						try
						{
							Thread.sleep(800);                                 //�߳����ߣ����治�ٱ仯��800����
						    }
						catch(Exception e)
						{                                                      //�����쳣
							e.printStackTrace();
						    }
						}
					father.bitmapData[GameView.stage].flag[number[GameView.stage]]=false;               //���е�ͼƬ��ʧ
					}
				
				else                                                           //���û����
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
				
				if(father.circleNumber>0)                                      //�������������0
					father.circleNumber--;                                     //������һ
				father.status=0;                                               //�ص���ֹ״̬���ȴ������һ�λ�����Ļ�ϵĻ�
				if(father.circleNumber==0)                                     //�����������0
				{
					try
					{
						Thread.sleep(800);                                     //��������ʱ����Ϸ����ͣ����ʱ��
						}
					catch(Exception e)
					{
						e.printStackTrace();
					    }
					father.failFlag=true;                                      //ʧ�ܱ�־��λtrue
					}
				}
			}
		
		
		try
		{                                                                 
			Thread.sleep(3);                                                   //�߳�����
			}
		catch(Exception e)
		{
			e.printStackTrace();
			}
		
	}
	
	
	public void moveY()                                                        //�ڶ���Ʈ��
	{
		if(count3>20)                                                          //����20
		{          
			father.y-=moveYDistance;                                           //��ǰ��
			if(father.xx>0)
				father.x+=moveYDistance*Math.abs(father.xx/father.yy);         //���ҷɣ�ͬ���ǰ�������
			else
			    father.x-=moveYDistance*Math.abs(father.xx/father.yy);         //����ɣ�ͬ���ǰ�������
			count3-=moveYDistance;                                             //count3��ֵΪ40������������20������y�������20���ڵڶ��εķ��й�����
			father.w-=Math.abs(father.yy/17500);                               //���Ŀ�͸�ͬʱ��С                               
			father.h-=Math.abs(father.yy/12500);                                    
			}
		else
			ifMoveY=true;                                                      //��Ϊtrueʱ�����ڶ��η��н���
		} 
	
	
	public void initMove()                                                     //��һ���һ�λ��󣬻����н������ٴγ�ʼ��������Ҫ�����õı���
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
		judge=true;                                                            //������һ�ξͲ������ˣ�Ϊtrueʱ������������
		float distanceX=father.x+15;                                           //���ĺ�����
		float distanceY=father.y+15;                                           //����������
		switch(GameView.stage)                                                 //�ؿ���
		{
		case 0:                                                                //��һ��
			float a0[]=father.bitmapData[0].X;                                 //�õ�ÿһ����ƷͼƬ��x��y����
			float b0[]=father.bitmapData[0].Y;
			for(int i=1;i<10;i++)                                              //�����±�Ϊ0�ı���ͼƬ������
				{
			     //�ж��Ƿ����еķ�����Ҳ�Ǵ�Լ��һ�����㡣����������ƷͼƬ���ĵľ���ͨ��������㣬�����ƽ����һ����Χ�������У�ͨ�����ֵ������ÿһ�ص��Ѷ�
				if(father.bitmapData[0].flag[i]&&(distanceX-a0[i]-25)*(distanceX-a0[i]-25)+(distanceY-b0[i]-25)*(distanceY-b0[i]-25)<17*17)
				{
					father.playSound(3, 0);                                    //�����������
					father.father.vib.vibrate(new long[]{100,10,100,200},-1);  //��
					if(i>0&&i<4)                                               //���½ǵļ�������
						father.taskArray[0][0]++;                              //��Ӧ������Ԫ�ؼ�һ
					else if(i>3&&i<7)                                          //ͬ��
						father.taskArray[0][2]++;
					else if(i>6&&i<10)                                         //ͬ��
						father.taskArray[0][1]++;
			    	father.bitmapData[0].flag[i]=false;                        //���ٻ���δ����ʱ������ͼƬ
			    	father.bitmapData[0].flag[i+9]=true;                       //�л������к�������ͼƬ
			    	number[GameView.stage]=i+9;                                //�ı�����Ԫ�ص�ֵ���ڷ��з����л��õ�
			    	ifKeep=true;                                               //����
			    	}
				else if(i==9&&!ifKeep)                                         //���������һ������������еģ��򲥷Ų��ֵ���ף��Ч
					father.playSound(4, 0);                                    //����
				} 
			break;		
		default:
			break;
			}
		}
	}

