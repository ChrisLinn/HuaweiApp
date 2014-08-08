 
package com.example.shenzhoushihao;              //���������

/* 
 * ����Ϊ���̼����߳���
 * ��ʱ��⵱ǰ���̵�״̬
 * Ȼ�����״̬������Ӧ�Ĵ��� 
 *
 */

public class KeyThread extends Thread
{
	int action;                                  //����״̬��
	PlaneActivity activity;                      //PlaneActivity������
	private boolean flag = true;                 //ѭ����־
	int span = 20;                               //˯�ߵĺ�����
	int countMove = 0;                           //�ɻ��ƶ��ļ�����
	int countFine = 0;                           //�ɻ����ӵ��ļ�����
	int moveN = 3;                               //ÿ����ѭ���ƶ�һ��
	int fineN = 5;                               //ÿ���ѭ����һ���ӵ�
		
	private boolean KEY_UP = false;              //���ϼ��Ƿ񱻰���
	private boolean KEY_DOWN = false;            //���¼��Ƿ񱻰���
	private boolean KEY_LEFT = false;            //����ļ��Ƿ񱻰���
	private boolean KEY_RIGHT = false;           //���ҵļ��Ƿ񱻰���
	private boolean KEY_A = false;               //A�ļ��Ƿ񱻰���
	
	
	public KeyThread(PlaneActivity activity)     //������
	{
		this.activity = activity;                //�õ�activity������
	}
	
	
	public void setFlag(boolean flag)            //���ñ�־λ
	{
		this.flag = flag;
	}
	
	
	public void run()                            //��д�ķ���
	{
		while(flag)
		{
			action = activity.action;            //�õ���ǰ���̵�״̬��
			if((action & 0x20) != 0)             //���ϼ�
			{
				KEY_UP = true;
			}
			else
			{
				KEY_UP = false;
			}
			if((action & 0x10) != 0)             //���¼�
			{
				KEY_DOWN = true;
			}
			else
			{
				KEY_DOWN = false;
			}
			if((action & 0x08) != 0)             //�����
			{
				KEY_LEFT = true;
			}
			else
			{
				KEY_LEFT = false;
			}
			if((action & 0x04) != 0)             //���Ҽ�
			{
				KEY_RIGHT = true;
			}
			else
			{
				KEY_RIGHT = false;
			}
			if((action & 0x02) != 0)             //A��
			{
				KEY_A = true;
			}
			else
			{
				KEY_A = false;
			}
			if(activity.gameView.status == 1 || activity.gameView.status == 3)
			{
				if(countMove == 0)               //ÿmoveN���ƶ�һ��
				{
					if(KEY_UP == true)           //���ϼ�������
					{
						if(!((activity.gameView.plane.getY() - activity.gameView.plane.getSpan()) < ConstantUtil.top))
						{
							activity.gameView.plane.setY(activity.gameView.plane.getY() - activity.gameView.plane.getSpan());
						}
						activity.gameView.plane.setDir(ConstantUtil.DIR_UP);
					}
					if(KEY_DOWN == true)         //���¼�������
					{
						if(!((activity.gameView.plane.getY() + activity.gameView.plane.getSpan()) > ConstantUtil.screenHeight - activity.gameView.plane.bitmap1.getHeight()))
						{
							activity.gameView.plane.setY(activity.gameView.plane.getY() + activity.gameView.plane.getSpan());
						}
						activity.gameView.plane.setDir(ConstantUtil.DIR_DOWN);
					}
					if(KEY_LEFT == true)         //�����������
					{
						if(!((activity.gameView.plane.getX() - activity.gameView.plane.getSpan()) < -40))
						{
							activity.gameView.plane.setX(activity.gameView.plane.getX() - activity.gameView.plane.getSpan());
						}
					}
					if(KEY_RIGHT == true)        //���Ҽ�������
					{
						if(!((activity.gameView.plane.getX() + activity.gameView.plane.getSpan()) > ConstantUtil.screenWidth - activity.gameView.plane.bitmap1.getWidth()))
						{
							activity.gameView.plane.setX(activity.gameView.plane.getX() + activity.gameView.plane.getSpan());
						}
					}
					if(KEY_RIGHT == false && KEY_LEFT == false && KEY_DOWN == false && KEY_UP == false)
					{
						activity.gameView.plane.setDir(ConstantUtil.DIR_STOP);
					}
					if(countFine == 0)           //ÿfineN��һ���ӵ�
					{
						if(KEY_A == true)        //A��������
						{
							activity.gameView.plane.fire();
						}
					}
				}
				countMove = (countMove+1)%moveN;
				countFine = (countFine+1)%fineN;
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