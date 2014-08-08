 
package com.example.shenzhoushihao;             //声明包语句

//引入相关类
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

/*
 * 该类为可以吃到的增加生命值的物体类
 *
 */

public class Life 
{
	int x = ConstantUtil.screenWidth;           //血的坐标
	int y;
	Bitmap bitmap;
	boolean status;                             //血的状态
	long touchPoint;                            //触发点
	int start;                                  //当前出发点
	int target;                                 //当前目标点
	int step;                                   //当前处于当前路径片段中第几步
	int[][] path; 
	
	
	public Life(int start,int target,int step,int[][] path, boolean status,long touchPoint)   //构造器
	{
		//初始化相关参数
		this.start=start;
		this.target=target;
		this.step=step;
		this.path=path;
		this.status = status;
		this.touchPoint = touchPoint;
		this.x=path[0][start];
		this.y=path[1][start];
	}
	
	
	public void draw(Canvas canvas)              //绘制方法
	{
		canvas.drawBitmap(bitmap, x, y, new Paint());
	}
	
	
	public void move()
	{
		if(step==path[2][start])                 //一段路径走完,到下一段路径开始
		{
			step=0;
			start=(start+1)%(path[0].length);
			target=(target+1)%(path[0].length);
			this.x=path[0][start];
			this.y=path[1][start];
		}
		else                                     //一段路径没有走完，继续走
		{
			int xSpan=(path[0][target]-path[0][start])/path[2][start];
			int ySpan=(path[1][target]-path[1][start])/path[2][start];
			this.x=this.x+xSpan;
			this.y=this.y+ySpan;
			step++;
		}
	}
}