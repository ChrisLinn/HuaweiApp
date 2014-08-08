 
package com.example.shenzhoushihao;             //声明包语句

//引入相关类
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Message;

/*
 * 该类为敌机的封装类
 *
 */

public class EnemyPlane 
{
	int x = ConstantUtil.screenWidth;           //飞机的坐标
	int y;
	boolean status;                             //飞机的状态，飞机是否可见
	long touchPoint;                            //触发点，当到一定时间时会将status设为true，即飞机可见可移动
	int type;                                   //飞机的类型
	int life;                                   //生命     比较强的飞机可能有多条生命
	int spanX = 10;                             //飞机移动的X像素
	int spanY = 5;                              //飞机移动的X像素
	Bitmap bitmap;
	int start;                                  //当前出发点
	int target;                                 //当前目标点
	int step;                                   //当前处于当前路径片段中第几步
	int[][] path;
	
	
	public EnemyPlane(int start,int target,int step,int[][] path, boolean status,long touchPoint, int type, int life)   //构造器
	{
		this.start=start;
		this.target=target;
		this.step=step;
		this.path=path;
		this.status = status;
		this.touchPoint = touchPoint;
		this.type = type;
		this.life = life;
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
	
	
	public void fire(GameView2 gameView)         //打子弹的方法
	{
		if(type == 3 && Math.random()<ConstantUtil.BooletSpan2)      //关卡时的敌机
		{  
			Bullet b1 = new Bullet(x, y, 2, ConstantUtil.DIR_LEFT,gameView);
			Bullet b2 = new Bullet(x, y, 2, ConstantUtil.DIR_LEFT_DOWN,gameView);
			Bullet b3 = new Bullet(x, y, 2, ConstantUtil.DIR_LEFT_UP,gameView);
			gameView.badBollets.add(b1);
			gameView.badBollets.add(b2);
			gameView.badBollets.add(b3);
		}
		else if(Math.random()<ConstantUtil.BooletSpan)               //继续生成敌机
		{
			if(this.type == 4)
			{
				Bullet b = new Bullet(x, y, 2, ConstantUtil.DIR_RIGHT,gameView);
				gameView.badBollets.add(b);
			}
			else
			{
				Bullet b = new Bullet(x, y, 2, ConstantUtil.DIR_LEFT,gameView);
				gameView.badBollets.add(b);
			}		
		}
	}
	
	
	private boolean isContain(int otherX, int otherY, int otherWidth, int otherHeight)   //判断两个矩形是否碰撞
	{
		int xd = 0;                              //大的x
		int yd = 0;                              //大的y
		int xx = 0;                              //小的x
		int yx = 0;                              //小的y
		int width = 0;
		int height = 0;
		boolean xFlag = true;                    //玩家飞机x是否在前
		boolean yFlag = true;                    //玩家飞机y是否在前
		
		
		if(this.x >= otherX)                     //如果x大
		{
			xd = this.x;
			xx = otherX;
			xFlag = false;
		}
		else
		{
			xd = otherX;
			xx = this.x;
			xFlag = true;
		}
		if(this.y >= otherY)                     //如果y大
		{
			yd = this.y;
			yx = otherY;
			yFlag = false;
		}
		else
		{
			yd = otherY;
			yx = this.y;
			yFlag = true;
		}
		if(xFlag == true)                        //若玩家飞机x在前
		{
			width = this.bitmap.getWidth();
		}
		else
		{
			width = otherWidth;
		}
		if(yFlag == true)                        //若玩家飞机y在前
		{
			height = this.bitmap.getHeight();
		}
		else
		{
			height = otherHeight;
		}
		if(xd>=xx&&xd<=xx+width-1&&
				yd>=yx&&yd<=yx+height-1)                             //首先判断两个矩形有否重叠
		{
		    double Dwidth=width-xd+xx;                               //重叠区域宽度		
			double Dheight=height-yd+yx;                             //重叠区域高度
			if(Dwidth*Dheight/(otherWidth*otherHeight)>=0.20)        //重叠面积超20%则判定为碰撞
			{
				return true;
			}
		}
		return false;
	}
	

	public boolean contain(Bullet b,GameView2 gameView)              //判断子弹是否打中敌方飞机
	{
		if(isContain(b.x, b.y, b.bitmap.getWidth(), b.bitmap.getHeight()))
		{
			this.life--;                                             //自己的生命减1
			if(this.life<=0)
			{                                                        //当生命小于0时
				if(gameView.activity.isSound)
				{
					gameView.playSound(2,0);                         //播放爆炸声音
				}
				this.status = false;                                 //使自己不可见
				if(this.type == 3)                                   //是关口时
				{
					gameView.status = 3;                             //状态换成胜利状态
					if(gameView.mMediaPlayer.isPlaying())
					{
						gameView.mMediaPlayer.stop();                //将游戏背景音乐停止
					}
					//游戏已胜利，切换到胜利界面
					Message msg1 = gameView.activity.myHandler.obtainMessage(5);
					gameView.activity.myHandler.sendMessage(msg1);   //向主activity发送Handler消息
				}
			}
			return true;
		}
		return false;
	}
	
	
	public int getX()                            //获取x的值
	{
		return x;
	}
	
	
	public void setX(int x)                      //设置x的值
	{
		this.x = x;
	}
	
	
	public int getY()                            //获取y的值
	{
		return y;
	}
	
	
	public void setY(int y)                      //设置y的值 
	{
		this.y = y;
	}
}