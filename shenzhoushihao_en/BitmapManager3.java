 
package com.example.shenzhoushihao;                                                             //���������

//���������
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;


public class BitmapManager3 
{
	private static Bitmap[] welcomePublic;                                                      //���ϵͳ��Ҫ�õ���ͼƬ��Դ
	public static Bitmap[] currentStageResources;                                               //�󱳾�����Ҫ��Ʒ
	public static float[] level0X={0,240,385,110,240,40,210,240,385,110,240,40,210};            //���صı�������ƷͼƬ��x����
	public static float[] level0Y={0,55,55,93,93,145,145,55,55,93,93,145,145};                  //���صı�������ƷͼƬ��y����
	public static boolean[] flag0={true,true,true,true,true,true,true,false,false,false,false,false,false};
	private BitmapManager3(){}                                                                  //�չ��췽��
		
	//����ͼƬ�ķ�������GameView3�еĹ��췽���е���
	public static void loadCurrentStageResources(Resources r,int stage)
	{
		switch(stage)//����ͼƬ
		{
		case 0:
			currentStageResources = new Bitmap[13];                                             //�������ͼƬ�����鲢����ռ�
			currentStageResources[0] = BitmapFactory.decodeResource(r, R.drawable.third_main);  //�����س���
			currentStageResources[1] = BitmapFactory.decodeResource(r, R.drawable.third_stank); //С̹��
			currentStageResources[2] = BitmapFactory.decodeResource(r, R.drawable.third_stank); //С̹��
			currentStageResources[3] = BitmapFactory.decodeResource(r, R.drawable.third_mtank); //��̹��
		    currentStageResources[4] = BitmapFactory.decodeResource(r, R.drawable.third_mtank); //��̹��
			currentStageResources[5] = BitmapFactory.decodeResource(r, R.drawable.third_ltank); //��̹��
			currentStageResources[6] = BitmapFactory.decodeResource(r, R.drawable.third_ltank); //��̹��
						
			currentStageResources[7] = BitmapFactory.decodeResource(r, R.drawable.third_stanke); //��ը���С̹��
			currentStageResources[8] = BitmapFactory.decodeResource(r, R.drawable.third_stanke); //��ը���С̹��
			currentStageResources[9] = BitmapFactory.decodeResource(r, R.drawable.third_mtanke); //��ը�����̹��
			currentStageResources[10] = BitmapFactory.decodeResource(r, R.drawable.third_mtanke);//��ը�����̹��
			currentStageResources[11] = BitmapFactory.decodeResource(r, R.drawable.third_ltanke);//��ը��Ĵ�̹��
			currentStageResources[12] = BitmapFactory.decodeResource(r, R.drawable.third_ltanke);//��ը��Ĵ�̹��
			break;
		default:
			break;
		}
	}
	
	//���ػ�ӭ�����ͼƬ
	public static void loadWelcomePublic(Resources r)                                  //���ػ�ӭ�����ͼƬ
	{ 
		welcomePublic = new Bitmap[13];
		welcomePublic[0] = BitmapFactory.decodeResource(r, R.drawable.welcome_back3);  //��ӭ���汳��
		welcomePublic[1] = BitmapFactory.decodeResource(r, R.drawable.ipad);           //ipad��Ļ
		welcomePublic[2] = BitmapFactory.decodeResource(r, R.drawable.welcome_menu3);   //���˵�
		welcomePublic[3] = BitmapFactory.decodeResource(r, R.drawable.welcome_sound);  //������ر������Ľ��汳��
		welcomePublic[6] = BitmapFactory.decodeResource(r, R.drawable.skip);           //��������������
		welcomePublic[7] = BitmapFactory.decodeResource(r, R.drawable.txt_1);          //����ʼ��
		welcomePublic[8] = BitmapFactory.decodeResource(r, R.drawable.txt_2);          //��������
		welcomePublic[9] = BitmapFactory.decodeResource(r, R.drawable.txt_3);          //��Ӣ�۰�
		welcomePublic[10] = BitmapFactory.decodeResource(r, R.drawable.txt_4);         //��������
		welcomePublic[11] = BitmapFactory.decodeResource(r, R.drawable.txt_5);         //�����ڡ�
		welcomePublic[12] = BitmapFactory.decodeResource(r, R.drawable.txt_6);         //���˳���
	}

	//����������ͼƬID����ϵͳ��ӭ����ͼƬ
	public static void drawWelcomePublic(int imgId,Canvas canvas,int x,int y,Paint paint)
	{
		canvas.drawBitmap(welcomePublic[imgId], x, y, paint);
	}
}