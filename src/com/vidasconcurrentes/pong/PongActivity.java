package com.vidasconcurrentes.pong;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

public class PongActivity extends Activity {
	
	public static String TAG="Pong";
	private final static int PGVIEW=1;
	public static boolean DEBUG=BuildConfig.DEBUG;
	private int ptsLiniciales=0,ptsRiniciales=0;
	Intent juego;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(DEBUG){
        	if(savedInstanceState==null)
        		Log.v(TAG,"+OnCreate PongActivity+");
        	else 
        		Log.i(TAG,"+OnCreate+");
        }
        //Remove title bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //Remove status bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.main);
        
        //Applying functionality to "buttons"
        TextView tv = (TextView)findViewById(R.id.play_button);
        tv.setOnClickListener(new OnClickListener() {
         @Override
         public void onClick(View v) {
          startGame();
         }
        });
        
        tv = (TextView)findViewById(R.id.options_button);
        tv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
            	juego = new Intent(getApplicationContext(),OpcionesActivity.class);
            	startActivity(juego);
            	
            }
           });
        
        tv = (TextView)findViewById(R.id.exit_button);
        tv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
             finish();
            }
           });
        
        ImageView logo = (ImageView)findViewById(R.id.imageView1);
        logo.setOnClickListener(new OnClickListener() {
        	@Override
        	public void onClick(View v) {
        		Intent viewIntent = new Intent("android.intent.action.VIEW",Uri.parse("http://www.facebook.com/MiichaelD"));
        		startActivity(viewIntent);
        	}
        });
        
    }
    
    
    private void startGame() {
    	juego= new Intent(this, Game.class);
    	juego.putExtra("PuntosL", ptsLiniciales);
    	juego.putExtra("PuntosR", ptsRiniciales);
    	this.startActivityForResult(juego, PGVIEW);
    }
    
    public void onStart(){
    	super.onStart();
    	if(DEBUG){
    		Log.v(TAG,"++OnStart++");
    	}
    }
    
    public void onResume(){
    	super.onResume();
    	if(DEBUG){
    		Log.v(TAG,"+++OnResume+++");
    	}
    }
    
    public void onPause(){
    	super.onPause();
    	if(DEBUG){
    		Log.v(TAG,"---OnPause---");
    	}
    }
    
    public void onStop(){
    	super.onStop();
    	if(DEBUG){
    		Log.v(TAG,"--OnStop--");
    	}
    }
    
    public void onDestroy(){
    	super.onDestroy();
    	if(DEBUG){
    		Log.v(TAG,"-OnDestroy-");
    	}
    }
    /** Called Before onPause(). */
    public void onSaveInstanceState(Bundle savedInstanceState) {
    	super.onSaveInstanceState(savedInstanceState);
      	if(DEBUG){
    		Log.i(TAG,"**OnSaveInstanceState**");
    	}
    	  // Save UI state changes to the savedInstanceState.
    	  // This bundle will be passed to onCreate if the process is
    	  // killed and restarted.
    	  // etc.
    	}

    /** Called Before onResume */
    public void onRestoreInstanceState(Bundle savedInstanceState) {
    	  super.onRestoreInstanceState(savedInstanceState);
    	  if(DEBUG){
      		Log.i(TAG,"*OnRestoreInstanceState*");
      	}
    	  // Restore UI state from the savedInstanceState.
    	  // This bundle has also been passed to onCreate.
    	}
    
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
    	super.onActivityResult(requestCode, resultCode, data);
    	switch(requestCode) {
    	case PGVIEW:
    		if (resultCode == Activity.RESULT_OK) {
    			if (data.getExtras() != null) {
    				ptsLiniciales = data.getExtras().getInt("PuntosL");
    				ptsRiniciales = data.getExtras().getInt("PuntosR");
    				Log.d(PongActivity.TAG,"+OnActivityResult L:"+ptsLiniciales+" R:"+ptsRiniciales);
    			}
    			else{
    				ptsLiniciales=0;
    				ptsRiniciales=0;
    				Log.d(PongActivity.TAG,"+OnActivityResult");
    			}
    		}
    		else{
    			ptsLiniciales=0;
				ptsRiniciales=0;}
			break;
    	}
    }

    
}