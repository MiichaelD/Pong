package com.vidasconcurrentes.pong;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
@SuppressLint("WrongCall")
public class PGView extends SurfaceView implements SurfaceHolder.Callback, Runnable{

	public static final int UMBRAL_TACTIL =70;
	private Handler mHandler;
	private SurfaceHolder sh;
	private Acelerometro acel;
	private Rect screen,aux;
	private Paint p,p2;
	private boolean run;
	private Thread tTouch,tMove,tBar1,tBar2;
	private Raqueta raqL,raqR;
	private Bola bola;
	private int touchX[]=new int[2],touchY[]=new int[2],origenY[]=new int[2];
	private Rectangulo curRect[]=new Rectangulo[2];
	
	public PGView(Context conText,Handler hand, Acelerometro ac, int L, int Ri) {
		super(conText);
		sh=getHolder();
		sh.addCallback(this);
		mHandler=hand;
		p=new Paint();
		p.setColor(Color.WHITE);
		p.setTextAlign(Align.CENTER);
		p.setTypeface(Typeface.createFromAsset(this.getContext().getAssets(),"fonts/KellySlab-Regular.ttf"));
		p.setTextSize(60);
		p.setAntiAlias(true);
		p2=new Paint();
		p2.setColor(Color.GREEN);
		p2.setAntiAlias(true);
		acel=ac;
		Marcador.getInstance().setMarcador(L,Ri);
		
	}
	
	
	
	@Override
	public void run() {
		Canvas canvas=null;
		while(run) {
		canvas = null;
		try {
			canvas = sh.lockCanvas(null);
		    synchronized(sh) {
		    	onDraw(canvas);
		    }
		} finally {
			if(canvas != null)
				sh.unlockCanvasAndPost(canvas);
			}
		}
	}
	
	public void terminado(int L, int R){
		try{
			Log.i(PongActivity.TAG,"+GAMEOVER()!!");
			if(mHandler!=null)
				if(L>R)
					mHandler.obtainMessage(Game.WIN, L, R).sendToTarget();
				else
					mHandler.obtainMessage(Game.LOSE, L, R).sendToTarget();
		}catch(Exception e){Log.e(PongActivity.TAG, e.getMessage());
		
		
		}
	} 	
	


	public void onDraw(Canvas canvas){
		try{
			
		canvas.drawColor(Color.BLACK);
	/*	int W=5,N=30;
		int x1 = 0, y1 = 0, x2, y2;
	      for ( int i = 1; i <= W*N; ++i ) {
	         double angle = 2*Math.PI*i/(double)N;
	         double radius = i/(double)N * getWidth()/2 / (W+3);
	         x2 = (int)( radius*Math.cos(angle) );
	         y2 = (int)( radius*Math.sin(angle) );
	        
	         canvas.drawLine( getWidth()/2+x1, getHeight()/2+y1, getWidth()/2+x2, getHeight()/2+y2,p2);
	         x1 = x2;
	         y1 = y2;
	      }
	      */
		drawCenterLine(canvas,p);
		drawMarcador(canvas,p);
		canvas.drawRect(raqL.getRect(),p);
		canvas.drawRect(raqR.getRect(),p);
		canvas.drawRect(bola.getRect(),p);
		//canvas.drawCircle(bola.getOrigenX(),bola.getOrigenY(),bola.ancho,p);
		}catch(Exception e){Log.e(PongActivity.TAG,e.toString());}
	}
	

	private void drawCenterLine(Canvas canvas, Paint paint) {
		int w = 6;
		int h = 20;
		int gap = 10;
		int ini = gap / 2; // por estetica, si no seria 0

		for(int i = 0; i < this.getHeight() / (h+gap); i++) {
			canvas.drawRect(this.getWidth()/2 - w/2, ini,
					this.getWidth()/2 + w/2, ini + h, paint);
			ini += h + gap;
		}
	}
	
	private void drawMarcador(Canvas canvas, Paint paint) {
		canvas.drawText(Integer.toString(Marcador.getInstance().getL()),getWidth() / 2 - 80, 80, paint);
		canvas.drawText(Integer.toString(Marcador.getInstance().getR()),getWidth() / 2 + 80, 80, paint);
	}
	
	public boolean onTouchEvent(MotionEvent event){
		//if(!Opciones.getInstance().accelerometerEnabled()) {
		if(tBar1==null) {
			int action = event.getAction() & MotionEvent.ACTION_MASK;
	        int pointerIndex = (event.getAction() & MotionEvent.ACTION_POINTER_ID_MASK) >>   MotionEvent.ACTION_POINTER_ID_SHIFT;
	        int pointerId = event.getPointerId(pointerIndex);
	        if(pointerIndex!=pointerId)
	        	 Log.v(PongActivity.TAG,"+DragandDropVIEW DIFERENTES: ID:"+pointerId+" - Index"+pointerIndex);
	        switch (action) {
	         case MotionEvent.ACTION_DOWN:
	         case MotionEvent.ACTION_POINTER_DOWN:
	        	 Log.v(PongActivity.TAG,"+DragandDropVIEW OnTouchEvent(Action_Down) ID:"+pointerId+" - Index"+pointerIndex);
	             touchX[pointerId] = (int)event.getX(pointerIndex);
	             touchY[pointerId] = (int)event.getY(pointerIndex);	             				 
	             aux = new Rect(raqL.getRect());
	             aux.set(aux.left - UMBRAL_TACTIL, aux.top,aux.right + UMBRAL_TACTIL, aux.bottom);
	             if(aux.contains(touchX[pointerId], touchY[pointerId])) {
					curRect[pointerId] = raqL;
					origenY[pointerId] = touchY[pointerId];
					break;
	             }
	             
	             aux = new Rect(raqR.getRect());
	             aux.set(aux.left - UMBRAL_TACTIL, aux.top,aux.right + UMBRAL_TACTIL, aux.bottom);
	             if(aux.contains(touchX[pointerId], touchY[pointerId])) {
	            	 curRect[pointerId] = raqR;
	            	 origenY[pointerId] = touchY[pointerId];
	             }
	             break;
	             
	         case MotionEvent.ACTION_UP:
	         case MotionEvent.ACTION_POINTER_UP:
	         case MotionEvent.ACTION_CANCEL:
	        	 touchX[pointerId] = -1;
	        	 touchY[pointerId] = -1;
	        	 curRect[pointerId]=null;
	        	 Log.v(PongActivity.TAG,"+DragandDropVIEW OnTouchEvent(Action_Up) ID:"+pointerId+" - Index"+pointerIndex);
	        	 break;
	         
	         case MotionEvent.ACTION_MOVE:
	        	 int pointerCount = event.getPointerCount();
	        	 for (int i = 0; i < pointerCount; i++) {
	        		 pointerIndex = i;
	        		 pointerId = event.getPointerId(pointerIndex);
	        		 touchX[pointerId] = (int)event.getX(pointerIndex);
	        		 touchY[pointerId] = (int)event.getY(pointerIndex);
	        		 if(curRect[pointerId]!=null) {
	        			 if(curRect[pointerId].puedoMover(0, touchY[pointerId]-origenY[pointerId],screen))
	        				 ((Raqueta)curRect[pointerId]).move(0, touchY[pointerId]-origenY[pointerId]);
	        		 }
	        		 origenY[pointerId]=touchY[pointerId];
	        	 }
			}
		}
	    return true;
	}
	
	
	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		Log.v(PongActivity.TAG,"+PGVIEW surfaceCreated");
		
		
		
	}
	
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,	int height) {
		Log.v(PongActivity.TAG,"+PGVIEW surfaceChanged");
		
		screen=new Rect(0,0,getWidth(),getHeight());
		if(Opciones.getInstance().accelerometerEnabled())
			raqL=new Raqueta(new Point(50,getHeight()/2-50),20,100,Raqueta.HUM,screen,acel);
		else
			raqL=new Raqueta(new Point(50,getHeight()/2-50),20,100,Raqueta.HUM_TOUCH,null,null);
		
		switch(Opciones.getInstance().getGameType()){
		case Opciones.GAME_HvC:
			raqR=new Raqueta(new Point(getWidth()-70,getHeight()/2-50),20,100,Raqueta.CPU,screen,null);
			break;
		case Opciones.GAME_HvH:
			raqR=new Raqueta(new Point(getWidth()-70,getHeight()/2-50),20,100,Raqueta.HUM_TOUCH,null,null);
			break;
		case Opciones.GAME_CvC:
			raqR=new Raqueta(new Point(getWidth()-70,getHeight()/2-50),20,100,Raqueta.CPU,screen,null);
			raqL=new Raqueta(new Point(50,getHeight()/2-50),20,100,Raqueta.CPU,screen,null);
			break;
		}
		bola=new Bola(new Point(getWidth()/2-5,getHeight()/2-5),10,10,raqL,raqR,screen,getContext(),this);
		

		tMove=new Thread(bola);
		bola.setRunning(true);
		tMove.start();
		
		if(raqL.getGameType()!=Raqueta.HUM_TOUCH){
			tBar1=new Thread(raqL);
			raqL.setRunning(true);
			if(raqL.getGameType()==Raqueta.CPU)
				raqL.setBola(bola);
			tBar1.start();	
		}
		
		if(raqR.getGameType()!=Raqueta.HUM_TOUCH){
			tBar2=new Thread(raqR);
			raqR.setRunning(true);
			if(raqR.getGameType()==Raqueta.CPU)
				raqR.setBola(bola);
			tBar2.start();
		}
		
		
		tTouch=new Thread(this);
		run=true;
		tTouch.start();
		
	}


	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		Log.v(PongActivity.TAG,"+PGVIEW surfaceDestroyed");
		boolean retry=true;
		bola.setRunning(false);
		raqL.setRunning(false);
		raqR.setRunning(false);
		run=false;
		while(retry){
			try {
				
				if(tBar1!=null)
					tBar1.join();
				if(tBar2!=null)
					tBar2.join();
				if(tMove!=null)
					tMove.join();
				if(tTouch!=null)
					tTouch.join();
				retry=false;
			} catch (InterruptedException e) {Log.e(PongActivity.TAG,"PGView-SD: "+e.getMessage());}
		}
	}

}

/*
//if(!Opciones.getInstance().accelerometerEnabled()) {
		if(tBar1==null) {
			for(int i=0;i<pointers;i++){
				touchX[i]=(int) event.getX(i);
				touchY[i]=(int) event.getY(i);
			}	
			
			switch(event.getAction()){
/*			Touch Original:
 			case MotionEvent.ACTION_DOWN:
				Log.v(PongActivity.TAG,"+DragandDropVIEW OnTouchEvent(Action_Down)");
				if(raqL.getRect().contains(touchX,touchY)){
					curRect=raqL;
					origenY=touchY;
					break;
				}
				if(raqR.getRect().contains(touchX,touchY)){
					curRect=raqR;
					origenY=touchY;
					break;
				}
				break;	
/			
			//Touch Mejorado:
			case MotionEvent.:
				pointers=event.getPointerCount();
				touchX=new int[pointers];
				touchY=new int[pointers];
				curRect=new Rectangulo[pointers];
				origenY=new int[pointers];
				for(int i=0;i<pointers;i++){
					touchX[i]=(int) event.getX(i);
					touchY[i]=(int) event.getY(i);
				}	
				
				Log.v(PongActivity.TAG,"+DragandDropVIEW OnTouchEvent(Action_Down) pointers="+pointers);
				for(int i=0;i<pointers;i++){
					aux = new Rect(raqL.getRect());
					aux.set(aux.left - UMBRAL_TACTIL, aux.top,aux.right + UMBRAL_TACTIL, aux.bottom);
					if(aux.contains(touchX[i], touchY[i])) {
						Log.e(PongActivity.TAG,"+DragandDropVIEW touch:"+i+" toca:raqL");
						curRect[i] = raqL;
						origenY[i] = touchY[i];
						continue;
					}
					
					aux = new Rect(raqR.getRect());
					aux.set(aux.left - UMBRAL_TACTIL, aux.top,aux.right + UMBRAL_TACTIL, aux.bottom);
					if(aux.contains(touchX[i], touchY[i])) {
						Log.e(PongActivity.TAG,"+DragandDropVIEW touch:"+i+" toca:raqR");
						curRect[i] = raqR;
						origenY[i] = touchY[i];
					}
				}
				break;

						
			case MotionEvent.ACTION_MOVE:
				Log.v(PongActivity.TAG,"+DragandDropVIEW OnTouchEvent(Action_Move) pointers="+pointers);
				for(int i=0;i<pointers;i++){
					if(curRect[i]!=null) {
						if(curRect[i].puedoMover(0, touchY[i]-origenY[i],screen))
							((Raqueta)curRect[i]).move(0, touchY[i]-origenY[i]);
					}
					origenY[i]=touchY[i];
				}
				break;
			
			
			case MotionEvent.ACTION_UP:
				pointers=event.getPointerCount();
				curRect=null;
				Log.v(PongActivity.TAG,"+DragandDropVIEW OnTouchEvent(Action_Up) pointers="+pointers);
				break;
			}
		}
		return true;
	}
 
 
 
 * */
