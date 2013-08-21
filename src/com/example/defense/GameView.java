package com.example.defense;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class GameView extends SurfaceView
{
	private List<Bullet> ball = new ArrayList<Bullet>();	
	private Player player;

	Bitmap players;
    /**������ ������ GameLoopThread*/
    private GameThread mThread;
    
    public int shotX;
    public int shotY; 
    
    /**���������� ����������� ����� ���������*/
    private boolean running = false;
    
  //-------------Start of GameThread--------------------------------------------------\\
    
    @SuppressLint("WrongCall")
	public class GameThread extends Thread
    {
        /**������ ������*/
        private GameView view;	 
        
        /**����������� ������*/
        public GameThread(GameView view) 
        {
              this.view = view;
        }

        /**������� ��������� ������*/
        public void setRunning(boolean run) 
        {
              running = run;
        }

        /** ��������, ����������� � ������ */
        public void run()
        {
            while (running)
            {
                Canvas canvas = null;
                try
                {
                    // ���������� Canvas-�
                    canvas = view.getHolder().lockCanvas();
                    synchronized (view.getHolder())
                    {
                        // ���������� ���������
                        onDraw(canvas);
                    }
                }
                catch (Exception e) { }
                finally
                {
                    if (canvas != null)
                    {
                    	view.getHolder().unlockCanvasAndPost(canvas);
                    }
                }
            }
                }

}

    //-------------End of GameThread--------------------------------------------------\\
    
    public GameView(Context context) 
    {
        super(context);
        
        mThread = new GameThread(this);
        
        /*������ ��� ���� ������� � ��� ��� ���*/
        getHolder().addCallback(new SurfaceHolder.Callback() 
        {
      	  	 /*** ����������� ������� ��������� */
               public void surfaceDestroyed(SurfaceHolder holder) 
               {
            	   boolean retry = true;
            	    mThread.setRunning(false);
            	    while (retry)
            	    {
            	        try
            	        {
            	            // �������� ���������� ������
            	            mThread.join();
            	            retry = false;
            	        }
            	        catch (InterruptedException e) { }
            	    }
               }

               /** �������� ������� ��������� */
               public void surfaceCreated(SurfaceHolder holder) 
               {
            	   mThread.setRunning(true);
            	   mThread.start();
               }

               /** ��������� ������� ��������� */
               public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) 
               {
               }
        });
        players= BitmapFactory.decodeResource(getResources(), R.drawable.player2);
        		player= new Player(this, guns);
    }
    
     /**������� �������� ��� ������� � ���*/
    protected void onDraw(Canvas canvas) {     	
        canvas.drawColor(Color.WHITE);
        
        Iterator<Bullet> j = ball.iterator();
        while(j.hasNext()) {
      	  Bullet b = j.next();
      	  if(b.x >= 1000 || b.x <= 1000) {
      		  b.onDraw(canvas);
      	  } else {
      		  j.remove();
      	  }
        }
        canvas.drawBitmap(guns, 5, 120, null);
  }
}