package com.iniciativamovil.mappsdf;

import android.os.Bundle;
import android.app.Activity;
import android.graphics.drawable.AnimationDrawable;
import android.view.Menu;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

public class SplashScreenActivity extends Activity {
	protected boolean _active = true;
	protected int _splashTime = 3000 ; 
	ImageView  imagenTren;
	Thread splashTread;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
		setContentView(R.layout.activity_splash_screen);
		
		imagenTren = (ImageView) this.findViewById(R.id.imageView2);
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
	                //iniciaActividad();
	        		this.interrupt();
	        	    finish ();
	            } catch ( InterruptedException e ) {
	                // do nothing 
	            } finally { 
	            	
	            } 
	        } 
	    }; 
	    splashTread.start(); 
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.splash_screen, menu);
		return true;
	}
	
	public void onBackPressed(){
		splashTread.interrupt();
	    finish ();
		super.finish ();
	}

}
