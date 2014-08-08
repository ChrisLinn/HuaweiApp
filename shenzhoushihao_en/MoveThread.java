 
package com.example.shenzhoushihao;              //���������

//���������
import java.util.ArrayList;

/*
 * �����ҷ��ɻ��������ƶ�����ƶ��߳�
 *
 */

public class MoveThread extends Thread
{
	private int sleepSpan = 10;                  //˯�ߵĺ�����
	private boolean flag = true;                 //ѭ����־λ 
	GameView2 gameView;                          //GameView2������
	ArrayList<Bullet> deleteBollet = new ArrayList<Bullet>();
	ArrayList<EnemyPlane> deleteEnemy = new ArrayList<EnemyPlane>();
	ArrayList<Life> deleteLife = new ArrayList<Life>();
	ArrayList<ChangeBullet> deleteChangeBollets = new ArrayList<ChangeBullet>();
	private int countBolletMove = 0;             //�ҷ��ӵ��ƶ��ļ�����
	private int countMyBolletN = 3;              //ÿ���ٴ�ѭ���ƶ�һ��
	private int countEnemyMove = 0;              //�л��ƶ�������
	private int countEnemyN = 8;                 //ÿ���ٴ�ѭ���ƶ�һ��
	private int countEnemyFire = 0;              //�л����ӵ�������
	private int countEnemyFireN = 20;            //ÿ���ٴ�ѭ����һ���ӵ� 
	private int countEnemyBolletMove = 0;        //�з��ӵ��ƶ��ļ�����
	private int countEnemyBolletN = 3;           //ÿ���ٴ�ѭ���ƶ�һ�� 
	
	
	public MoveThread(GameView2 gameView)        //������
	{
		this.gameView = gameView;                //�õ�gameView������
	} 
	
	
	public void setFlag(boolean flag)            //�����̱߳�־λ
	{
		this.flag = flag;
	}
	
	
	public void run()                            //��д��run����
	{
		while(flag)
		{
			if(countBolletMove == 0)             //�ҷ��ӵ��ƶ�
			{
				try{
					for(Bullet b : gameView.goodBollets)
					{
						b.move();
						if(b.x<0 || b.x>ConstantUtil.screenWidth
								|| b.y<0 || b.y>ConstantUtil.screenHeight)
						{
							deleteBollet.add(b);                               //��������Ļ��Χ��ɾ���ӵ�
						}
						else                                                   //����Ļ��Χ��
						{
							for(EnemyPlane ep : gameView.enemyPlanes)
							{
								if(ep.status == true)
								{
									if(ep.contain(b,gameView))                 //���ел�
									{
										Explode e = new Explode(ep.x, ep.y, gameView);
										gameView.explodeList.add(e);
										deleteBollet.add(b);
									}
								}
							}
						}
					}	
					gameView.goodBollets.removeAll(deleteBollet);
					deleteBollet.clear();
				}
				catch(Exception e)
				{}				 
			}
			if(countEnemyMove == 0)              //�л��ƶ���Ѫ���ƶ������˸ı��ӵ���������ƶ�
			{
				try{
					for(EnemyPlane ep : gameView.enemyPlanes)
					{
						if(ep.status == true)
						{
							ep.move();
							if(ep.getX()<-ep.bitmap.getWidth() || ep.getX()>ConstantUtil.screenWidth
									|| ep.getY()<-ep.bitmap.getHeight() || ep.getY()>ConstantUtil.screenHeight)
							{
								deleteEnemy.add(ep);
							}
							else
							{
								if(gameView.plane.contain(ep))
								{
									Explode e = new Explode(ep.x, ep.y, gameView);
									gameView.explodeList.add(e);
									ep.life--;
									if(ep.life <=0)
									{
										deleteEnemy.add(ep);
									}
								}
							}
						}
					}
					gameView.enemyPlanes.removeAll(deleteEnemy);
					deleteEnemy.clear();
				}
				catch(Exception e)               //�����쳣��Ϣ
				{}
				try                              
				{
					for(Life l : gameView.lifes)
					{
						if(l.status == true)
						{
							l.move();            //�ƶ�Ѫ��
							if(l.x<-l.bitmap.getWidth() || l.x>ConstantUtil.screenWidth
									|| l.y<-l.bitmap.getHeight() || l.y>ConstantUtil.screenHeight)
							{
								deleteLife.add(l);
							}
							else
							{
								if(gameView.plane.contain(l))
								{
									deleteLife.add(l);
								}
							}
						}
					}
					gameView.lifes.removeAll(deleteLife);
					deleteLife.clear();
				}
				catch(Exception e)               //�����쳣��Ϣ
				{}
				try                             
				{
					for(ChangeBullet cb : gameView.changeBollets)
					{
						if(cb.status == true)
						{
							cb.move();           //�ƶ����˸ı��ӵ�������
							if(cb.x<-cb.bitmap.getWidth() || cb.x>ConstantUtil.screenWidth
									|| cb.y<-cb.bitmap.getHeight() || cb.y>ConstantUtil.screenHeight)
							{
								deleteChangeBollets.add(cb);
							}
							else
							{
								if(gameView.plane.contain(cb))
								{
									deleteChangeBollets.add(cb);
								}
							}
						}
					}
					gameView.changeBollets.removeAll(deleteChangeBollets);
					deleteChangeBollets.clear();
				}
				catch(Exception e)               //�����쳣��Ϣ
				{}
			}
			if(countEnemyFire == 0)              //�л����ӵ�
			{
				try
				{
					for(EnemyPlane ep : gameView.enemyPlanes)
					{
						if(ep.status == true)
						{
							ep.fire(gameView);
						}
					}
				}
				catch(Exception e)               //�����쳣��Ϣ
				{
					e.printStackTrace();
				}
			}
			if(countEnemyBolletMove == 0)        //�з��ӵ��ƶ�
			{
				try
				{
					for(Bullet b : gameView.badBollets)
					{
						b.move();
						if(b.x<0 || b.x>ConstantUtil.screenWidth
								|| b.y<0 || b.y>ConstantUtil.screenHeight)
						{
							deleteBollet.add(b);
						}
						else
						{
							if(gameView.plane.contain(b))                      //��ײ����Ƿ�����ҷ��ɻ�
							{
								Explode e = new Explode(b.x, b.y, gameView);
								gameView.explodeList.add(e);
								deleteBollet.add(b);                           //���к�ɾ���ӵ�
							}
						}
					}
					gameView.badBollets.removeAll(deleteBollet);
					deleteBollet.clear();
				}
				catch(Exception e)               //�����쳣��Ϣ
				{
					e.printStackTrace();
				}
			}
			countBolletMove = (countBolletMove+1)%countMyBolletN;              //ѭ�����Լ�
			countEnemyMove = (countEnemyMove+1)%countEnemyN;                   //ѭ�����Լ�
			countEnemyFire = (countEnemyFire+1)%countEnemyFireN;               //ѭ�����Լ�
			countEnemyBolletMove = (countEnemyBolletMove+1)%countEnemyBolletN; //ѭ�����Լ�
			try                                  //˯����Ϣ
			{
				Thread.sleep(sleepSpan);
			}
			catch(Exception e)                   //�����쳣��Ϣ
			{
				e.printStackTrace();
			}
		}
	}
}