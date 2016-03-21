package com.vidasconcurrentes.pong;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
public class Game extends Activity {

	public static final int WIN=1,LOSE=2;
	private Acelerometro acel=null;
	private Dialog menu;
	private WakeLock wl;
	private PGView view;
	public Game game;
	int ptsL,ptsR;
	
	   @Override
	   public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        game=this;
	        if(PongActivity.DEBUG)
	        	Log.v(PongActivity.TAG,"+OnCreate GAME+");
	        
	        acel=new Acelerometro(getApplicationContext());      
	        requestWindowFeature(Window.FEATURE_NO_TITLE);
	        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
	        
	        if(savedInstanceState!=null){
            	onRestoreInstanceState(savedInstanceState);
            	if(PongActivity.DEBUG) Log.i(PongActivity.TAG,"+OnCreate GAME L=+"+ptsL+" R="+ptsR);
            	view = new PGView(this, mHandler,acel, ptsL, ptsR);
        	}
            else
            	if(getIntent().getExtras() != null) {
            		ptsL = getIntent().getExtras().getInt("PuntosL");
            		ptsR = getIntent().getExtras().getInt("PuntosR");
            		if(ptsL == Marcador.MAX_PUNT ||ptsR == Marcador.MAX_PUNT)
	        	    		view = new PGView(this,mHandler, acel, 0, 0);
            		else
	        	    	view = new PGView(this,mHandler, acel, ptsL, ptsR);
            	}else
            		view = new PGView(this,mHandler, acel, 0, 0);
	        
	        setContentView(view);
	        PowerManager pm=(PowerManager) getSystemService(Context.POWER_SERVICE);
	        wl=pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK, PongActivity.TAG);
	        
	        menu = new Dialog(Game.this);
	        menu.setContentView(R.layout.gameover);
	        menu.setTitle(R.string.gameover);
	        menu.setCancelable(false);
	        //there are a lot of settings, for dialog, check them all out!
	        
	        //set up text
	        TextView text = (TextView) menu.findViewById(R.id.TextView01);
	        text.setTypeface(Typeface.createFromAsset(getApplicationContext().getAssets(),"fonts/KellySlab-Regular.ttf"));
	        text.setGravity(Gravity.CENTER);
	        
	        //set up image view
	        ImageView img = (ImageView) menu.findViewById(R.id.ImageView01);
	        img.setImageResource(R.drawable.icon);

	        //set up button
	        Button button = (Button) menu.findViewById(R.id.Button01);
	        button.setOnClickListener(new OnClickListener() {
	        @Override
	            public void onClick(View v) {
	       	 	if(PongActivity.DEBUG)
	       	 		Log.i(PongActivity.TAG,"Restart-Accepted");
	                try {
	               	menu.cancel();
	               	setContentView((view= new PGView(getApplicationContext(),mHandler,acel,0,0)));
					} catch (Throwable e) {	if(PongActivity.DEBUG) 		Log.e(PongActivity.TAG,e.getMessage()+"\t Click");}
	            }
	        });
	        
	        button = (Button) menu.findViewById(R.id.Button02);
	        button.setOnClickListener(new OnClickListener() {
	        @Override
	            public void onClick(View v) {
	       	 	if(PongActivity.DEBUG)
	       	 		Log.i(PongActivity.TAG,"Exit-Cancelled");
	                try {
	               	 menu.cancel();
	               	 finish();
					} catch (Throwable e) {	if(PongActivity.DEBUG) 		Log.e(PongActivity.TAG,e.getMessage()+"\t Click");}
	            }
	        });
	        
	    }
	    

	   
	   private final Handler mHandler = new Handler() {
	        public void handleMessage(Message msg) {
	        	 try{
	        		 Log.w(PongActivity.TAG,"Starting GAMEOVER()");
	        		 TextView text = (TextView) menu.findViewById(R.id.TextView01);
	        		 text.setText(R.string.gameover);
	        		 switch (msg.what) {
	        		 case WIN:
	        			 if(PongActivity.DEBUG) 
	        				 Log.i(PongActivity.TAG, "GameOver-WIN: "+msg.arg1+"-"+msg.arg2);
	        			 text.append("\n Ganaste :)!! "+msg.arg1+"-"+msg.arg2);
	        			 menu.show();
	        			 break;
	        		 case LOSE:
	        			 if(PongActivity.DEBUG) 
	        				 Log.i(PongActivity.TAG, "GameOver-LOSE: "+msg.arg1+"-"+msg.arg2);	                
	        			 text.append("\n Perdiste :(  "+msg.arg1+"-"+msg.arg2);
	        			 menu.show();
	        			 break;
	        		 }
	        		Log.w(PongActivity.TAG,"DONE GAMEOVER()");
	      		 }catch(Exception ex){Log.e(PongActivity.TAG,ex.getMessage());}
	        }
	    };
	   
	   
	   @Override
	   public void onBackPressed() {
		   if(Marcador.getInstance().getL()==Marcador.MAX_PUNT||Marcador.getInstance().getR()==Marcador.MAX_PUNT){
			   Log.v(PongActivity.TAG,"+OnBackPressed() GAME Finalizado+");
		       setResult(Activity.RESULT_OK);
		       super.onBackPressed();
		   }
		   else{
			   Log.v(PongActivity.TAG,"+OnBackPressed() ++GAME+++");
			   Bundle bundle = new Bundle();
			   bundle.putInt("PuntosL", Marcador.getInstance().getL());
			   bundle.putInt("PuntosR", Marcador.getInstance().getR());
			   
			   Intent mIntent = new Intent();
			   mIntent.putExtras(bundle);
			   setResult(Activity.RESULT_OK, mIntent);
			   super.onBackPressed();
		   }
	   }
	   
	    public void onStart(){
	    	super.onStart();
	    	if(PongActivity.DEBUG){
	    		Log.v(PongActivity.TAG,"++OnStart GAME++");
	    	}
	    }
	    public void onResume(){
	    	super.onResume();
	    	if(PongActivity.DEBUG){
	    		Log.v(PongActivity.TAG,"+++OnResume GAME+++");
	    	}
	    	acel.register();
	    	if(Opciones.getInstance().accelerometerEnabled()){
	    		wl.acquire();
	    	}
	    }
	    public void onPause(){
	    	super.onPause();
	    	if(PongActivity.DEBUG){
	    		Log.v(PongActivity.TAG,"---OnPause GAME---");
	    	}
	    }
	    public void onStop(){
	    	super.onStop();
	    	if(PongActivity.DEBUG){
	    		Log.v(PongActivity.TAG,"--OnStop GAME--");
	    	}
	    	acel.unregister();
	    }
	    public void onDestroy(){
	    	super.onDestroy();
	    	if(PongActivity.DEBUG){
	    		Log.v(PongActivity.TAG,"-OnDestroy GAME-");
	    	}
	    }
	    /** Called Before onPause(). */
	    public void onSaveInstanceState(Bundle savedInstanceState) {
	    	super.onSaveInstanceState(savedInstanceState);
	      	if(PongActivity.DEBUG){
	    		Log.i(PongActivity.TAG,"**OnSaveInstanceState GAME**");
	    	}
		       savedInstanceState.putInt("PuntosL", Marcador.getInstance().getL());
		       savedInstanceState.putInt("PuntosR", Marcador.getInstance().getR());
	    	}

	    /** Called Before onResume */
	    public void onRestoreInstanceState(Bundle savedInstanceState) {
	    	super.onRestoreInstanceState(savedInstanceState);
	    	if(PongActivity.DEBUG){
	      		Log.i(PongActivity.TAG,"*OnRestoreInstanceState GAME**");
	      	}
	    	ptsL=savedInstanceState.getInt("PuntosL");
	    	ptsR=savedInstanceState.getInt("PuntosR");
	    }

	    
	}