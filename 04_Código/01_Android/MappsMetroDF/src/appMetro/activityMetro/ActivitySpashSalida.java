package appMetro.activityMetro;

import android.app.Activity;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import appMetro.origDestino.R;

public class ActivitySpashSalida extends Activity {
	protected boolean _active = true;
	protected int _splashTime = 4000 ; 
	ImageView  imagenLogo;
	Thread splashTread;
	 public void onCreate(Bundle savedInstanceState) {
	    super.onCreate ( savedInstanceState ) ;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

	    setContentView(R.layout.splashscreensalida) ;
	    imagenLogo = (ImageView) this.findViewById(R.id.imageSplashLogo);
	    imagenLogo.setImageResource(R.drawable.animacionlogo);
    	final AnimationDrawable frameAnimation = (AnimationDrawable)imagenLogo.getDrawable();
    	imagenLogo.post( new Runnable(){
            public void run() {
            frameAnimation.start();                
            }            
        }); 

    	splashTread = new Thread ( ) { 
	        public void run ( ) {
	            try { 
	            	sleep (_splashTime);
	                System.gc();
	            } catch ( InterruptedException e ) {
	                // do nothing 
	            } finally { 
	            	finish ();
	            } 
	        } 
	    }; 
	    splashTread.start(); 
	} 

	 public void onBackPressed(){
	    finish ();
		super.finish ();
	 }
}
