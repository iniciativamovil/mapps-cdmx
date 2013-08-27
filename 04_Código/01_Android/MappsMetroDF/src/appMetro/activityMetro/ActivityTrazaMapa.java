package appMetro.activityMetro;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PointF;
import android.os.Bundle;
import android.util.FloatMath;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ZoomControls;
import appMetro.origDestino.*;

public class ActivityTrazaMapa  extends Activity{
	private static final String TAG = "Touch"; 
	private static final float MIN_ZOOM = 0.5f; 
	private static final float MAX_ZOOM = 5.0f;
	
	private boolean modeZoom = false;
	PointF posAbsoluta = new PointF();
	PointF posInicial = new PointF(); 
	PointF posFinal = new PointF();
	
	PointF midInicial = new PointF(); 
	PointF midFinal = new PointF();
	
	PointF medVista = new PointF();
	
	float distInicial = 0;
	float distActual = 0;
	double widthPantalla = 0;
	double factor = 1;
	float  coordenadas[][];
	
	PintaPantalla resultadoMapa;
	LinearLayout FL_Paint2;
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trazamapa);
    	Intent intent = getIntent();
    	int numEnlaces = intent.getIntExtra("numEnlaces",1);
    	coordenadas = new  float[numEnlaces+1][2];
    	coordenadas[0] = intent.getFloatArrayExtra("Coordenadas0");
    	coordenadas[1] = intent.getFloatArrayExtra("Coordenadas1");
        
		widthPantalla = getWindowManager().getDefaultDisplay().getWidth();
		factor = widthPantalla/530;
		midInicial.set((int)(530 * factor), (int) (550 * factor));
		
		ZoomControls ZM_control = (ZoomControls) findViewById(R.id.zoomControls1);
		ZM_control.setOnZoomInClickListener(new OnClickListener(){
			public void onClick(View arg0) {
				factor = factor + 0.1;
				rePaint();
			}
		});
		ZM_control.setOnZoomOutClickListener(new OnClickListener(){
			public void onClick(View arg0) {
				factor = factor - 0.1;
				rePaint();
			}
		});
        
        ImageButton BT_busqueda;
        BT_busqueda = (ImageButton) this.findViewById(R.id.botonBusqueda);
        BT_busqueda.setOnClickListener(new OnClickListener(){
			public void onClick(View v) {
		        AutoCompleteTextView TV_busqueda;
		        TV_busqueda = (AutoCompleteTextView) findViewById(R.id.textBusqueda);
				Intent myIntent = new Intent(v.getContext(), ActivityBusqueda.class);
				String campo = "";
				if(TV_busqueda.getText().toString().trim().length() > 0){
					campo = TV_busqueda.getText().toString().trim();
				}
				myIntent.putExtra("busqueda", campo);
	    		startActivity(myIntent);
			}
        });
		
        FL_Paint2 = (LinearLayout) findViewById(R.id.linearLayout5);
		FL_Paint2.setOnTouchListener( new OnTouchListener(){
			public boolean onTouch(View v, MotionEvent event) {
			   switch (event.getAction() & MotionEvent.ACTION_MASK) {
			   		case MotionEvent.ACTION_DOWN: //first finger down only 
			   			posInicial.set(event.getX(), event.getY());
			   			modeZoom = false;
			   			break;
			   		case MotionEvent.ACTION_UP: //first finger lifted
			   		case MotionEvent.ACTION_POINTER_UP: //second finger lifted 
			   			modeZoom = false;
			   			break;
			   		case MotionEvent.ACTION_POINTER_DOWN: //second finger down 
			   			distInicial = spacing(event,v);
		   				modeZoom = true;
			   			break;
			   		case MotionEvent.ACTION_MOVE:  
			   			if (modeZoom) {
			   				distActual = spacing(event,v); 
		   					factor = factor * (float) Math.pow(20,(distActual - distInicial));
		   					distInicial = distActual;
		   					rePaint();
			   			}else{
			   				posFinal.set(event.getX(), event.getY());
			   				recorrerPantalla((posInicial.x - posFinal.x),(posInicial.y - posFinal.y));
			   				posInicial.set(posFinal);
			   			}
			   			break;
			   } 
			   return true;
			} 
		});
		rePaint();
	}
	
	public void onDestroy(){
		FL_Paint2.getBackground().setCallback(null);
		FL_Paint2.setBackgroundDrawable(null);
		FL_Paint2.destroyDrawingCache();
		FL_Paint2 = null;
		resultadoMapa.getBackground().setCallback(null);
		resultadoMapa.setBackgroundDrawable(null);
		resultadoMapa.destroyDrawingCache();
		resultadoMapa = null;
		super.onDestroy();
		System.gc();
	}
	
	/**
	 * Dibuja nuevamente la imagen en el plano, 
	 * apartir de los parámetros (Zoom) actuales.
	 */
	public void rePaint(){
		if (factor > MAX_ZOOM){
				factor=MAX_ZOOM;	
		}else{
			if (factor < MIN_ZOOM){
				factor=MIN_ZOOM;	
			}
		}
		midFinal.set((int)(530 * factor), (int) (550 * factor));
		resultadoMapa = new PintaPantalla(this,coordenadas,factor);
		resultadoMapa.setBackgroundResource(R.drawable.mapametrocuadromedblanco);
		FL_Paint2.removeAllViews();
		FL_Paint2.addView(resultadoMapa,(int)midFinal.x, (int) midFinal.y);
		recorrerPantalla((midFinal.x - midInicial.x )/2,(midFinal.y - midInicial.y )/2);
		midInicial.set(midFinal);
	}
	
	/** 
	 * Da el efecto de Scroll a la imagen 
	 */
	private void recorrerPantalla(float x, float y) {
		posAbsoluta.set(posAbsoluta.x + x, posAbsoluta.y + y);
		medVista.set(FL_Paint2.getWidth(), FL_Paint2.getHeight());
		
		if ((530 * factor) > medVista.x){
			float width = (float) ((530 * factor) - medVista.x);
			if (posAbsoluta.x > width){
				posAbsoluta.x = (float) width;
			}
			if (posAbsoluta.x < 0){
				posAbsoluta.x = 0;
			}
		}else{
			posAbsoluta.x = 0;
		}

		if ((550 * factor) > medVista.y){
			float height = (float) ((550 * factor) - medVista.y);
			if (posAbsoluta.y > height){
				posAbsoluta.y = (float) height;
			}
			if (posAbsoluta.y < 0){
				posAbsoluta.y = 0;
			}
		}else{
			posAbsoluta.y = 0;
		}
		FL_Paint2.scrollTo((int) posAbsoluta.x, (int) posAbsoluta.y);
	 }
	
	/** 
	 * Calcula la distancia proporcional entre 2 puntos dados en el plano.
	 * Para poder realizar Zoom a la imagen. 
	 */
    private float spacing(MotionEvent event, View v) { 
 	   float dx = (event.getX(0) - event.getX(1)) / v.getWidth(); 
 	   float dy = (event.getY(0) - event.getY(1)) / v.getHeight(); 
 	   return FloatMath.sqrt(dx * dx + dy * dy);
 	} 
 	 
 	/** 
 	 * (Debug - Seguimiento)
 	 *  Rescata el evento dado, para registrarlo en la consola LogCat. 
 	 */ 
 	@SuppressWarnings("unused")
	private void dumpEvent(MotionEvent event) { 
 	   String names[] = { "DOWN" , "UP" , "MOVE" , "CANCEL" , "OUTSIDE" , 
 	      "POINTER_DOWN" , "POINTER_UP" , "7?" , "8?" , "9?" }; 
 	   StringBuilder sb = new StringBuilder(); 
 	   int action = event.getAction(); 
 	   int actionCode = action & MotionEvent.ACTION_MASK; 
 	   sb.append("event ACTION_" ).append(names[actionCode]); 
 	   if (actionCode == MotionEvent.ACTION_POINTER_DOWN 
 	         || actionCode == MotionEvent.ACTION_POINTER_UP) { 
 	      sb.append("(pid " ).append( 
 	      action >> MotionEvent.ACTION_POINTER_ID_SHIFT); 
 	      sb.append(")" ); 
 	   } 
 	   sb.append("[" ); 
 	   for (int i = 0; i < event.getPointerCount(); i++) { 
 	      sb.append("#" ).append(i); 
 	      sb.append("(pid " ).append(event.getPointerId(i)); 
 	      sb.append(")=" ).append((int) event.getX(i)); 
 	      sb.append("," ).append((int) event.getY(i)); 
 	      if (i + 1 < event.getPointerCount()) 
 	         sb.append(";" ); 
 	   } 
 	   sb.append("]" ); 
 	   Log.d(TAG, sb.toString()); 
 	}
}
