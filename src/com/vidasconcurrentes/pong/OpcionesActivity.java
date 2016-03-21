package com.vidasconcurrentes.pong;

import android.app.Activity;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.CheckBox;
import android.widget.Spinner;
public class OpcionesActivity extends Activity {
	
	private Vibrator vi;
	private MediaPlayer mp;
	private CheckBox cb;
	private Spinner sp;
	
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(PongActivity.DEBUG){
        	if(savedInstanceState==null)
        		Log.v(PongActivity.TAG,"+OnCreate OpcionesActivity+");
        	else 
        		Log.i(PongActivity.TAG,"+OnCreate Opciones+");
        }
        //Remove title bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //Remove status bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.options);
        
        mp=MediaPlayer.create(getApplicationContext(), R.raw.confirm);
        vi=(Vibrator)getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
        
        cb = (CheckBox) findViewById(R.id.checkBoxSonido);
        cb.setOnClickListener(new OnClickListener() {
        	@Override
        	public void onClick(View v) {
        		Opciones.getInstance().toggleSound();
        		Log.d(PongActivity.TAG,"Sound: "+Opciones.getInstance().soundEnabled());
        		if(Opciones.getInstance().soundEnabled())
        			mp.start();
        	}
        });
        cb.setChecked(Opciones.getInstance().soundEnabled());
        
        
        cb = (CheckBox) findViewById(R.id.checkBoxVibracion);
        cb.setOnClickListener(new OnClickListener() {
        	@Override
        	public void onClick(View v) {
        		Opciones.getInstance().toggleVibration();
        		Log.d(PongActivity.TAG,"Vibration: "+Opciones.getInstance().vibrationEnabled());
        		if(Opciones.getInstance().vibrationEnabled())
        			vi.vibrate(new long[]{0,50,150,50},-1);
        	}
        });
       
        cb.setChecked(Opciones.getInstance().vibrationEnabled());
        
        cb = (CheckBox) findViewById(R.id.checkBoxAccel);
        cb.setOnClickListener(new OnClickListener() {
        	@Override
        	public void onClick(View v) {
        		Opciones.getInstance().toggleAcelerometro();
        		Log.d(PongActivity.TAG,"Acelerometro: "+Opciones.getInstance().accelerometerEnabled());
        	}
        });

        cb.setChecked(Opciones.getInstance().accelerometerEnabled());
        
        
        sp = (Spinner) findViewById(R.id.spinner1);
        sp.setOnItemSelectedListener(new OnItemSelectedListener(){
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
					switch(pos){
					case Opciones.GAME_HvC:
						cb.setEnabled(true);
						Opciones.getInstance().setGameType(Opciones.GAME_HvC);
						break;
						
					case Opciones.GAME_HvH:
						if(Opciones.getInstance().accelerometerEnabled()){
							Opciones.getInstance().toggleAcelerometro();
							cb.setChecked(false);
						}
						cb.setEnabled(false);
						Opciones.getInstance().setGameType(Opciones.GAME_HvH);
						break;
						
					case Opciones.GAME_CvC:
						if(Opciones.getInstance().accelerometerEnabled()){
							Opciones.getInstance().toggleAcelerometro();
							cb.setChecked(false);
						}
						cb.setEnabled(false);
						Opciones.getInstance().setGameType(Opciones.GAME_CvC);
						break;
					}
				Log.d(PongActivity.TAG,"Tipo de Juego vsCpu: "+Opciones.getInstance().getGameType());
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
        	
        });
        sp.setSelection(Opciones.getInstance().getGameType());
    }
    
    public void onStart(){
    	super.onStart();
    	if(PongActivity.DEBUG){
    		Log.v(PongActivity.TAG,"++OnStart Opciones++");
    	}
    }
    public void onResume(){
    	super.onResume();
    	if(PongActivity.DEBUG){
    		Log.v(PongActivity.TAG,"+++OnResume Opciones+++");
    	}
    }
    public void onPause(){
    	super.onPause();
    	if(PongActivity.DEBUG){
    		Log.v(PongActivity.TAG,"---OnPause Opciones---");
    		
    	}
    }
    public void onStop(){
    	super.onStop();
    	
    	if(PongActivity.DEBUG){
    		Log.v(PongActivity.TAG,"--OnStop Opciones--");
    	}
    }
    public void onDestroy(){
    	super.onDestroy();
    	if(PongActivity.DEBUG){
    		Log.v(PongActivity.TAG,"-OnDestroy Opciones-");
    	}
    	mp.release();
    }
    /** Called Before onPause(). */
    public void onSaveInstanceState(Bundle savedInstanceState) {
    	super.onSaveInstanceState(savedInstanceState);
      	if(PongActivity.DEBUG){
    		Log.i(PongActivity.TAG,"**OnSaveInstanceState Opciones**");
    	}
    	  // Save UI state changes to the savedInstanceState.
    	  // This bundle will be passed to onCreate if the process is
    	  // killed and restarted.
    	  // etc.
    	}

    /** Called Before onResume */
    public void onRestoreInstanceState(Bundle savedInstanceState) {
    	  super.onRestoreInstanceState(savedInstanceState);
    	  if(PongActivity.DEBUG){
      		Log.i(PongActivity.TAG,"*OnRestoreInstanceState Opciones*");
      	}
    	  // Restore UI state from the savedInstanceState.
    	  // This bundle has also been passed to onCreate.
    	}    
}

class Opciones {

	private static Opciones opciones = null;
	public final static int GAME_HvC=0, GAME_HvH=1, GAME_CvC=2 ;
	private boolean sonido, vibracion, acelerometro;
	private int vsCpu;
	

	private Opciones() {
		sonido = true;
		vibracion = true;
		vsCpu=0;
	}

	public static synchronized Opciones getInstance() {
		if(opciones == null)
			opciones = new Opciones();
		return opciones;
	}

	public void toggleSound() {
		sonido = !sonido;
	}

	public void toggleVibration() {
		vibracion = !vibracion;
			
	}

	public boolean soundEnabled() {
		return sonido;
	}

	public boolean vibrationEnabled() {
		return vibracion;
	}
	
	public void setGameType(int b){
		vsCpu=b;
	}
	
	public int getGameType(){
		return vsCpu;
	}
	
	public void toggleAcelerometro() {
		acelerometro = !acelerometro;
	}

	public boolean accelerometerEnabled() {
		return acelerometro;
	}
}