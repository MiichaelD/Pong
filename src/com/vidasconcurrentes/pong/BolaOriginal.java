package com.vidasconcurrentes.pong;

import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.os.Vibrator;
import android.util.Log;

public class BolaOriginal extends Rectangulo implements Movilidad, Runnable {

	public static final int UR = 1, UL = 2, DL = 3, DR = 4, VIBRAR_SENSILLO=1, VIBRAR_FINAL=2;
	private MediaPlayer mp;
	private PGView pgv;
	private Vibrator v = null;
	private int direccion,initX,initY,dormir=5, speedX,speedY;
	private boolean run;
	Raqueta raqL, raqR;
	Rect screen;
	
	public BolaOriginal(Point origen, int ancho, int alto,Raqueta rL,Raqueta rR, Rect scn, Context con, PGView pg) {
		super(origen, ancho, alto);
		direccion=DR;
		run=false;
		//speed=1;
		speedX=speedY=1;
		raqL=rL;
		raqR=rR;
		screen=scn;
		v=(Vibrator)con.getSystemService(Context.VIBRATOR_SERVICE);
		mp=MediaPlayer.create(con, R.raw.confirm);
		initX=getX();
		initY=getY();
		pgv=pg;
	}

	@Override
	public void move(int x, int y) {
		switch(direccion) {
	 case UR:
		 	origen.x+=(x);
		 	origen.y-=(y);
		 	break;
	 case UL:
		 	origen.x-=x;
		 	origen.y-=y;
		 	break;
	 case DL:
		 	origen.x-=x;
		 	origen.y+=y;
		 	break;
	 case DR:
		 	origen.x+=x;
		 	origen.y+=y;
		 	break;
		}
	}
	
	public int rebota(int x, int y, Rect screen,Rect raquetaIzda, Rect raquetaDcha) {
		//if im not able to move within the screen, change direction
		if(!puedoMover(x,y,screen)) {
			switch(direccion) {
			case UR:
				if(origen.y - y <= screen.top)
					direccion=DR;
				else
					return 1;
				//direccion = (origen.y - y <= screen.top) ? 	DR : UL;
				break;
			case UL:
				if(origen.y - y <= screen.top)
					direccion=DL;
				else 
					return -1;
				//direccion = (origen.y - y <= screen.top) ?		DL : UR;
				break;
			case DL:
				if(origen.y + alto + y >= screen.bottom)
					direccion=UL;
				else 
					return -1;
				//direccion = (origen.y + alto + y >= screen.bottom) ?	UL : DR;
				break;
			case DR:
				if( origen.y + alto + y >= screen.bottom)
					direccion = UR;
				else
					return 1;
				//direccion = (origen.y + alto + y >= screen.bottom) ?	UR : DL;
				break;
			}
			
		}
		Rect raqueta = null;
		if(chocaraCon(x, y, raquetaIzda))
			raqueta = raquetaIzda;
		if(chocaraCon(x, y, raquetaDcha))
			raqueta = raquetaDcha;
		if(raqueta != null) {
			switch(direccion) {//ifs within the case blocks are to prevent the ball to get into the bar (STUCK)
			case UR:
				if((origen.y+y)<=raqueta.bottom&&(origen.y+2*speedY)>=raqueta.bottom){
					direccion=DR;
					//Log.v(PongActivity.TAG,"Bola-y:"+(origen.y+y)+" < RaquetaBOTTOM"+raqueta.bottom+" < +y"+(origen.y+2*speed));
				}
				else {
					direccion = (origen.x+ancho < raqueta.left)? 	DR : UL;
					//Log.v(PongActivity.TAG,"Bola-x:"+(origen.x+ancho)+" < RaquetaLeft"+raqueta.left);
				}
				
				break;
			case UL:
				if((origen.y+y)<=raqueta.bottom&&(origen.y+2*speedY)>=raqueta.bottom){
					direccion=DL;
					//Log.v(PongActivity.TAG,"Bola-y:"+(origen.y+y)+" < RaquetaTOP"+raqueta.bottom+" < +y"+(origen.y+2*speed));
				}
				else {
					direccion = (origen.x > raqueta.right) ?		DL : UR;
					//Log.v(PongActivity.TAG,"Bola-x:"+origen.x+" RaquetaRight"+raqueta.right);
				}
				
				break;
			case DL:
				if((origen.y+alto+y)>=raqueta.top&&(origen.y+alto-2*speedY)<=raqueta.top){
					direccion=UL;
					//Log.v(PongActivity.TAG,"Bola-y:"+(origen.y+alto+y)+" > RaquetaTOP"+raqueta.top+" > +y"+(origen.y+alto-2*speed));
				}
				else {
					direccion = (origen.x > raqueta.right) ?	UL : DR;
					//Log.v(PongActivity.TAG,"Bola-x:"+origen.x+" RaquetaRight"+raqueta.right);
				}
				break;
			case DR:
				if((origen.y+alto+y)>=raqueta.top&&(origen.y+alto-2*speedY)<=raqueta.top){
					direccion=UR;
					//Log.v(PongActivity.TAG,"Bola-y:"+(origen.y+alto+y)+" > RaquetaTOP"+raqueta.top+" > +y"+(origen.y+alto-2*speed));
				}
				else {
					direccion = (origen.x+ancho < raqueta.left) ?	UR : DL;
					Log.v(PongActivity.TAG,"Bola-x:"+(origen.x+ancho+x)+" > RaquetaLeft"+raqueta.left);
				}
				break;
			}
		}Log.v(PongActivity.TAG,"Bola - rebota: "+direccion);
		return 0;
	}
	
	private boolean chocaraCon(int x, int y, Rect raqueta) {
		 if(raqueta.contains(origen.x+x, origen.y+y))
		  return true;
		 if(raqueta.contains(origen.x+ancho+x, origen.y+y))
		  return true;
		 if(raqueta.contains(origen.x+x, origen.y+alto+y))
		  return true;
		 if(raqueta.contains(origen.x+ancho+x, origen.y+alto+y))
		  return true;

		 return false;
		}
	
	
	public boolean puedoMover(int x, int y, Rect screen, Rect raquetaIzda, Rect raquetaDcha) {
		if(!puedoMover(x,y,screen)){
			Log.i(PongActivity.TAG,"Bola - choca con Pantalla");
			return false;
		}
		if(raquetaIzda.intersect(getRect())){
			Log.i(PongActivity.TAG,"Bola - choca con RaquetaL");
			return false;
		}
		if(raquetaDcha.intersect(getRect())){
			Log.i(PongActivity.TAG,"Bola - choca con RaquetaR");
			return false;
		}
		return true;
	}
	

	public void vibrar(int tipo){
		switch(tipo){
		case 1: v.vibrate(50); break;
		case 2: v.vibrate(new long[]{0,50,150,50},-1);
			break;
		}
	}
	
	public void reinitBola(){
		setX(initX);
		setY(initY);
	}
	
	public void setRunning(boolean r){
		run=r;
		if(!r){
			//while(mp.isPlaying()){}
			//mp.stop();
			mp.release();
		}
	}
	
	
	@Override
	public void run() {
		boolean punto=false;
		while(run){
			try{
				Thread.sleep(dormir);
			}catch(InterruptedException e){Log.e(PongActivity.TAG,e.getMessage());}
			if(!puedoMover(speedX,speedY,screen,raqL.getRect(),raqR.getRect())){
				switch(rebota(speedX,speedY,screen,raqL.getRect(),raqR.getRect())) {
					case 0:
						if(!puedoMover(speedX, speedY, screen) && Opciones.getInstance().soundEnabled())
							mp.start();
						if(puedoMover(speedX, speedY, screen) && Opciones.getInstance().vibrationEnabled())
							v.vibrate(50);
						break;
					case -1:
						Marcador.getInstance().addR();
						reinitBola();
						punto = true;
						break;
					case 1:
						Marcador.getInstance().addL();
						reinitBola();
						punto = true;
						break;
				}
			}
			
			if(punto){
				if(Marcador.getInstance().acabado()){
					if(PongActivity.DEBUG)
						Log.i(PongActivity.TAG,"Game Over");
						pgv.terminado(Marcador.getInstance().getL(), Marcador.getInstance().getR());
					while(run){}
				}
				else{
					try{
						Thread.sleep(2000);
					}catch(InterruptedException e){Log.e(PongActivity.TAG,"BolaRun "+e.getMessage());}
					punto=false;
					continue;	
				}
				
			}
			move(speedX, speedY);
		}
		Log.v(PongActivity.TAG,"Bola - Muere hilo");
	}
}

/*
  
 		rebota:
 		
		switch(direccion) {//ifs within the case blocks are to prevent the ball to get into the bar (STUCK)
			case UR:
				if((origen.y+y)<=raqueta.bottom&&(origen.y+2*speed)>=raqueta.bottom){
					//direccion=DR;
					Log.v(PongActivity.TAG,"Bola-y:"+(origen.y+y)+" < RaquetaBOTTOM"+raqueta.bottom+" < +y"+(origen.y+2*speed));
				}
				else {
					direccion = (origen.x+ancho < raqueta.left)? 	DR : UL;
					Log.v(PongActivity.TAG,"Bola-x:"+(origen.x+ancho)+" < RaquetaLeft"+raqueta.left);
				}
				
				break;
			case UL:
				if((origen.y+y)<=raqueta.bottom&&(origen.y+2*speed)>=raqueta.bottom){
					//direccion=DL;
					Log.v(PongActivity.TAG,"Bola-y:"+(origen.y+y)+" < RaquetaTOP"+raqueta.bottom+" < +y"+(origen.y+2*speed));
				}
				else {
					direccion = (origen.x > raqueta.right) ?		DL : UR;
					Log.v(PongActivity.TAG,"Bola-x:"+origen.x+" RaquetaRight"+raqueta.right);
				}
				
				break;
			case DL:
				if((origen.y+alto+y)>=raqueta.top&&(origen.y+alto-2*speed)<=raqueta.top){
					//direccion=UL;
					Log.v(PongActivity.TAG,"Bola-y:"+(origen.y+alto+y)+" > RaquetaTOP"+raqueta.top+" > +y"+(origen.y+alto-2*speed));
				}
				else {
					direccion = (origen.x > raqueta.right) ?	UL : DR;
					Log.v(PongActivity.TAG,"Bola-x:"+origen.x+" RaquetaRight"+raqueta.right);
				}
				break;
			case DR:
				if((origen.y+alto+y)>=raqueta.top&&(origen.y+alto-2*speed)<=raqueta.top){
					//direccion=UR;
					Log.v(PongActivity.TAG,"Bola-y:"+(origen.y+alto+y)+" > RaquetaTOP"+raqueta.top+" > +y"+(origen.y+alto-2*speed));
				}
				else {
					direccion = (origen.x+ancho < raqueta.left) ?	UR : DL;
					Log.v(PongActivity.TAG,"Bola-x:"+(origen.x+ancho+x)+" > RaquetaLeft"+raqueta.left);
				}
				break;
			}




		Rebote ORIGINAL:
					switch(direccion) {
			case UR:
				direccion = ((origen.x+ancho) < raqueta.left) ? 	 UL : DR;
				break;
			case UL:
				direccion = (origen.x > raqueta.right) ? UR : DL;
				break;
			case DL:
				direccion = (origen.x > raqueta.right) ? UL : DR;
				break;
			case DR:
				direccion = ((origen.x+ancho) < raqueta.left) ? 	DL : UR;
				break;
			}







*/