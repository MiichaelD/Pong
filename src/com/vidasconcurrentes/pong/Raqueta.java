package com.vidasconcurrentes.pong;

import android.graphics.Point;
import android.graphics.Rect;
import android.util.Log;

public class Raqueta extends Rectangulo implements Movilidad, Runnable {

	private boolean run;
	private Integer xInit;
	public final static int UMBRAL=20,HUM_TOUCH=0,HUM=1,CPU=2;
	private int gameType;
	private Acelerometro acel;
	private Rect screen;
	private Bola bola;
	
	public Raqueta(Point origen, int ancho, int alto,int tipo, Rect sc, Acelerometro ac) {
		super(origen, ancho, alto);
		gameType=tipo;
		acel=ac;
		screen=sc;
		xInit=null;
	}
	
	
	@Override
	public void move(int x, int y) {
		origen.y+= y;
	}
	
	public void setRunning(boolean r){
		run=r;
	}

	@Override
	public void run() {
		switch(gameType){
		case HUM: 
			if(acel!=null){
				//if(xInit == null){
					try{
						xInit = new Integer(acel.getXInclination());
						Thread.sleep(150);
						xInit = new Integer(acel.getXInclination());
					}catch(InterruptedException e){Log.e(PongActivity.TAG,e.getMessage());}
					
				//}
				
				while(run) {
					try {
						if(Math.abs(xInit - acel.getXInclination()) < 200)
							Thread.sleep(5);
						else
							Thread.sleep(2);
					} catch (InterruptedException e) {Log.e(PongActivity.TAG,e.getMessage());}
					if(xInit < acel.getXInclination() - UMBRAL ||  xInit < acel.getXInclination() + UMBRAL)
						if(puedoMover(0, 1, screen))
								move(0, 1);
					if(xInit > acel.getXInclination() - UMBRAL || xInit > acel.getXInclination() + UMBRAL)
						if(puedoMover(0, -1, screen))
							move(0, -1);
				}
				Log.v(PongActivity.TAG,"Raqueta - Muere hilo");
				
			}
			else{
				Log.v(PongActivity.TAG,"Raqueta - Acelerador no Inicializado");
			}
			break;
		
		case CPU: 
			while(run) {
				try {
					Thread.sleep(4);
				} catch (InterruptedException e) {Log.e(PongActivity.TAG,e.getMessage());}
				
				if(bola!=null){
				if(bola.origen.y < getY()+(alto/2))
					if(puedoMover(0,-1,screen))
						move(0,-1);
				if(bola.origen.y > getY()+(alto/2))
					if(puedoMover(0,1,screen))
						move(0,1);
				}
			}
			Log.v(PongActivity.TAG,"Raqueta - Muere hilo");
			break;
		}
	}
	
	public void setBola(Bola b){
		bola=b;
	}
	
	
	public int getGameType(){
		return gameType;
	}
	
	
}