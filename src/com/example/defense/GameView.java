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
    /**Объект класса GameLoopThread*/
    private GameThread mThread;
    
    public int shotX;
    public int shotY; 
    
    /**Переменная запускающая поток рисования*/
    private boolean running = false;
    
  //-------------Start of GameThread--------------------------------------------------\\
    
    @SuppressLint("WrongCall")
	public class GameThread extends Thread
    {
        /**Объект класса*/
        private GameView view;	 
        
        /**Конструктор класса*/
        public GameThread(GameView view) 
        {
              this.view = view;
        }

        /**Задание состояния потока*/
        public void setRunning(boolean run) 
        {
              running = run;
        }

        /** Действия, выполняемые в потоке */
        public void run()
        {
            while (running)
            {
                Canvas canvas = null;
                try
                {
                    // подготовка Canvas-а
                    canvas = view.getHolder().lockCanvas();
                    synchronized (view.getHolder())
                    {
                        // собственно рисование
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
        
        /*Рисуем все наши объекты и все все все*/
        getHolder().addCallback(new SurfaceHolder.Callback() 
        {
      	  	 /*** Уничтожение области рисования */
               public void surfaceDestroyed(SurfaceHolder holder) 
               {
            	   boolean retry = true;
            	    mThread.setRunning(false);
            	    while (retry)
            	    {
            	        try
            	        {
            	            // ожидание завершение потока
            	            mThread.join();
            	            retry = false;
            	        }
            	        catch (InterruptedException e) { }
            	    }
               }

               /** Создание области рисования */
               public void surfaceCreated(SurfaceHolder holder) 
               {
            	   mThread.setRunning(true);
            	   mThread.start();
               }

               /** Изменение области рисования */
               public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) 
               {
               }
        });
        players= BitmapFactory.decodeResource(getResources(), R.drawable.player2);
        		player= new Player(this, guns);
    }
    
     /**Функция рисующая все спрайты и фон*/
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