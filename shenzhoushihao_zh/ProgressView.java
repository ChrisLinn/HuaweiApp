 
package com.example.shenzhoushihao;                                            //声明包语句

//引入相关类
import android.app.ProgressDialog;          
import android.content.DialogInterface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class ProgressView extends SurfaceView implements SurfaceHolder.Callback//继承SurfaceView类实现SurfaceHolder和Callback接口
{
    ProgressDialog progress;                                                   //声明进度条
	ShenzhoushihaoActivity father;					                           //ShenzhoushihaoActivity的引用
    boolean flag;                                                              //线程执行标志
    String title,message;                                                      //进度条的标题和信息
    
    public ProgressView(final ShenzhoushihaoActivity father, final ProgressDialog progress)             //构造方法
    {         
	    super(father);                                                         //初始化父类
		this.father = father;                                                  //初始化成员变量
		this.progress=progress;                                                //初始化成员变量
		flag=true;                                                             //标志默认为true
		getHolder().addCallback(this);    
		progress.setMax(100);                                                  //进度条最大值为100
		progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);            //水平模式
		getMessage();                                                          //调用下面的getMessage方法获取文字信息
		progress.setButton("跳过", new DialogInterface.OnClickListener() 
		{
			public void onClick(DialogInterface dialog, int which) 
			{
				progress.dismiss();                                            //销毁进度条
				father.myHandler.sendEmptyMessage(0);                          //进度条走完后进入游戏界面，执行ShenzhoushihaoActivity中的handler中的case 0			
				flag=false;                                                    //结束循环
				}
			});
		progress.setTitle(title);                                              //设置标题
		progress.setMessage(message);                                          //设置文字
		progress.setIcon(R.drawable.icon);                                     //设置左上角的小图标
		progress.show();                                                       //显示
		progress.setCancelable(false);                                         //是否可以按返回键取消，设为false为不可以
		
		
		new Thread()                                                           //一个新的线程
		{
			public void run()                                                  //启动方法
			{
				int p=1;                                                       //初始进度为1
				while(flag)                                                    //flag为true时循环
				{
					p+=1;                                                      //一次加一，让玩家有足够的时间读完文字信息
					progress.setProgress(p);                                   //设置进度
					if(p>=100)                                                 //达到最大值时
					{
						progress.dismiss();                                    //销毁进度条
						father.myHandler.sendEmptyMessage(0);                  //进度条走完后进入游戏界面，执行ShenzhoushihaoActivity中的handler中的case 0			
						flag=false;                                            //结束循环
						}
					try
					{                                                          //捕获异常并处理
						Thread.sleep(100);
						}
					catch(InterruptedException e)
					{
						e.printStackTrace();
						}
					}
				}
			}.start();
			}
    public void getMessage()                                                   //进度条本关的提示信息
    {
   	 switch(GameView.stage)                                                    //本关的文字介绍，写在进度条对话框上
   	 {                   
   	 case 0:
   			title="第一关：准备发射";
   			message="宇航员为了完成中国人魂牵梦绕的飞天梦想,满怀激情地开始了他的第一段征途:为神舟十号的发射准备物品!                    游戏方法：滑动屏幕下方的环,以一定的速度划出后释放,将环抛出套取物品。";		
   			break;
   			}
   	 }
    
    
    public void surfaceChanged(SurfaceHolder holder, int format, int width,int height) 
    {}                                                                         //重写接口方法
    
    
	public void surfaceCreated(SurfaceHolder holder)                           //重写接口方法，View创建时被调用
	{}             
	
	
	public void surfaceDestroyed(SurfaceHolder holder)                         //重写接口方法，View被销毁时调用
	{}           
	

}
