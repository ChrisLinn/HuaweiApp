 
package com.example.shenzhoushihao;              //���������

/*
 * ������ΪWelcomeView2������߳��࣬
 * ͨ����WelcomeView2��status���ж�ִ�в�ͬ�Ĳ���
 * ��WelcomeView2��ͬʵ�ֶ���Ч��
 * ��Ҫ�Ĳ����ǻ�֡�����޸�ͼƬ����
 *
 */

public class WelcomeViewThread2 extends Thread
{
	private boolean flag = true;                 //ѭ�����λ 
	private int span = 100;                      //ѭ��˯��ʱ��
	WelcomeView2 welcomeView;                    //WelcomeView2������
	
	
	public WelcomeViewThread2(WelcomeView2 welcomeView)//������
	{
		this.welcomeView = welcomeView;          //�õ�welcomeView2������
	}
	
	
	public void setFlag(boolean flag)            //����ѭ�����λ 
	{
		this.flag = flag;
	}
	
	
	public void run()                            //��д��run���� 
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
				Thread.sleep(span);              //˯���ƶ������� 
			}
			catch(Exception e)                   //�����쳣��Ϣ
			{
				e.printStackTrace();             //��ӡ�쳣��Ϣ
			}
		}
	}
}