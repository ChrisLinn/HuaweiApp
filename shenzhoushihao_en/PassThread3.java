 
package com.example.shenzhoushihao;                            //���������

public class PassThread3 extends Thread                        //�̳�Thread��
{                   
	PassView3 father;                                          //PassView3�Ķ�������
	boolean flag;                                              //ѭ����־
	int sleepSpan = 50;                                        //����ʱ��,50����
	

	public PassThread3(PassView3 father)                       //���췽��
	{                   
		this.father = father;                                  //��ʼ����Ա����
		flag = true;                                           //ѭ����־Ĭ��Ϊtrue
	}
	
	//�̵߳�ִ�з���
	public void run()                                          //�����߳�
	{                                      
		while(flag)
		{                                                      //��־Ϊtrueʱ
			if(GameView3.stage!=5)                             //���ؽ���ʱ
			{                
				father.leftX +=5;                              //����Ļ�������仯��+5���γ�����������������м�Ʈ
			    father.rightX-=5;                              //ÿѭ��һ��-5
			}
			
			if(father.father.gv.nextStage)                     //����ǹ��ؽ��������ʧ�ܽ���
			{        
				switch(father.stage)                           //PassView3�е�stage
			    {      
				case 0:                                        //������
					if(father.leftX==95)
					    father.stage=10;                       //ִ��PassView�е�case 10��Ҳ���ǻ���ը�к����Ʒ
				    break;				
			    default:
				    break;
				}
			try
			{
				Thread.sleep(sleepSpan);
			}										           //�߳�����
			catch(Exception e)
			{
				e.printStackTrace();
			}										           //���񲢴�ӡ�쳣
		    }                                                  //����if���
	    }                                                      //ѭ��
    }                                                          //����
}                                                              //��

