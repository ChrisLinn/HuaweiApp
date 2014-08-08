 
package com.example.shenzhoushihao;              //���������

//���������
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

/*
 * ����Ϊ���ԳԵ��ı���ҷɻ��ӵ�����
 *
 */

public class ChangeBullet 
{
	int x = ConstantUtil.screenWidth;            //����
	int y;
	Bitmap bitmap;
	boolean status;                              //״̬
	long touchPoint;                             //������	
	int start;                                   //��ǰ������
	int target;                                  //��ǰĿ���
	int step;                                    //��ǰ���ڵ�ǰ·��Ƭ���еڼ���
	int[][] path;
	
	
	public ChangeBullet(int start,int target,int step,int[][] path, boolean status,long touchPoint)     //������
	{
		this.start=start;
		this.target=target;
		this.step=step;
		this.path=path;
		this.status = status;
		this.touchPoint = touchPoint;
		this.x=path[0][start];
		this.y=path[1][start];
	}
	
	
	public void draw(Canvas canvas)              //���Ʒ���
	{
		canvas.drawBitmap(bitmap, x, y, new Paint());
	}
	
	
	public void move()
	{
		if(step==path[2][start])                 //һ��·������,����һ��·����ʼ
		{
			step=0;
			start=(start+1)%(path[0].length);
			target=(target+1)%(path[0].length);
			this.x=path[0][start];
			this.y=path[1][start];
		}
		else                                     //һ��·��û�����꣬������
		{
			int xSpan=(path[0][target]-path[0][start])/path[2][start];
			int ySpan=(path[1][target]-path[1][start])/path[2][start];
			this.x=this.x+xSpan;
			this.y=this.y+ySpan;
			step++;
		}
	}
}