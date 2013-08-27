package appMetro.activityMetro;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import appMetro.origDestino.*;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;

public class ActivitySplashScreen extends Activity{
	protected boolean _active = true;
	protected int _splashTime = 3000 ; 
	ImageView  imagenTren;
	Thread splashTread;
	 public void onCreate(Bundle savedInstanceState) {
	    super.onCreate ( savedInstanceState ) ;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

	    setContentView(R.layout.splashscreen) ;

	    imagenTren = (ImageView) this.findViewById(R.id.imageViewTren);
    	imagenTren.setBackgroundResource(R.drawable.animaciontren);
    	final AnimationDrawable frameAnimation = (AnimationDrawable)imagenTren.getBackground();
    	imagenTren.post( new Runnable(){
            public void run() {
            frameAnimation.start();                
            }            
        }); 

    	splashTread = new Thread ( ) { 
	        public void run ( ) {
	            try { 
	            	sleep (_splashTime);
	                System.gc();
	                finish ();
	                iniciaActividad();
	            } catch ( InterruptedException e ) {
	                // do nothing 
	            } finally { 
	            	
	            } 
	        } 
	    }; 
	    splashTread.start(); 
	} 
	 public void iniciaActividad(){
		 startActivity( new Intent ( this, AppMetroActivity.class ));
	 }

	 public void onBackPressed(){
		splashTread.interrupt();
		splashTread.suspend();
	    finish ();
		super.finish ();
	}
}
