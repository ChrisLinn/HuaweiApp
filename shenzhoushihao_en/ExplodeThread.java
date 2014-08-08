 
package com.example.shenzhoushihao;              //���������

//���������
import java.util.ArrayList;

/*
 * ����Ϊ��ը�Ļ�֡�߳�
 * û��һ��ʱ���GameView��explodeList�еı�ը��һ֡
 *
 */

public class ExplodeThread extends Thread
{
	private boolean flag = true;                 //ѭ�����λ 
	private int span = 100;                      //˯�ߵĺ�����
	GameView2 gameView;                          //GameView2������
	ArrayList<Explode> deleteExplodes = new ArrayList<Explode>();//������ʱ�����Ҫɾ���ı�ը
	
	
	public ExplodeThread(GameView2 gameView)     //������
	{
		this.gameView = gameView;                //�õ�gameView������
	}
	
	
	public void setFlag(boolean flag)            //����ѭ�����λ
	{
		this.flag = flag;
	}
	
	
	public void run()                            //��д��run����
	{
		while(flag)
		{
			try                                  //��ֹ�������ʵ��쳣
			{
				for(Explode e : gameView.explodeList)
				{
					if(e.nextFrame())
					{}
					else                         //��û����һ֡ʱɾ���ñ�ը
					{
						deleteExplodes.add(e);
					}
				}
				gameView.explodeList.removeAll(deleteExplodes);
				deleteExplodes.clear();
			}
			catch(Exception e)                   //�����쳣��Ϣ
			{
				e.printStackTrace();             //��ӡ�쳣��Ϣ
			}
			try
			{
				Thread.sleep(span);              //˯��ָ��������
			}
			catch(Exception e)                   //�����쳣��Ϣ
			{
				e.printStackTrace();             //��ӡ�쳣��Ϣ
			}
		}
	}
}