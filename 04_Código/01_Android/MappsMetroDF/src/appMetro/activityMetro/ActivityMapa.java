/**
 * 
 */
package appMetro.activityMetro;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.FloatMath;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.Toast;
import android.widget.ZoomControls;
import appMetro.origDestino.*;
/**
 * @author XM02664
 *
 */
public class ActivityMapa  extends Activity {
	private ImageZoomView IV_mapa;
	private ZoomState mZoomState;
    private Bitmap mBitmap;
    
	InputStream  stream;
	Drawable imagenDrawable;
	
	boolean error=false;
	int anchoOriginal = 0;
	int altoOriginal = 0;

	double widthPantalla = 0;	
	double heightPantalla = 0;
	
	private boolean modeZoom = false;
	PointF posAbsoluta = new PointF();
	PointF posInicial = new PointF(); 
	PointF posFinal = new PointF();
	
	PointF midInicial = new PointF(); 
	PointF midFinal = new PointF();
	
	PointF medVista = new PointF();
	
	float distInicial = 0;
	float distActual = 0;
	float zoomManual = 0;
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toast.makeText(this, "Cargando Imagen", Toast.LENGTH_SHORT).show();
        
		setContentView(R.layout.layoutmapa);
		Intent intent = getIntent();
		String imagen = intent.getStringExtra("imagen");
        
		widthPantalla = getWindowManager().getDefaultDisplay().getWidth();	
		heightPantalla = getWindowManager().getDefaultDisplay().getHeight();
		
        IV_mapa = (ImageZoomView) findViewById(R.id.imageView1);
        if (imagen.equals("Metro")){
        	stream = getResources().openRawResource(R.drawable.mapa_metro);
		}else{
			if (imagen.equals("Metrobus")){
				stream = getResources().openRawResource(R.drawable.mapa_metrobus);
			}else{
				if (imagen.equals("Tren")){
					stream = getResources().openRawResource(R.drawable.mapa_trenligero);
				}else{
					if (imagen.equals("Suburbano")){
						stream = getResources().openRawResource(R.drawable.mapa_suburbano);
					}else{
						if (imagen.equals("MexiBus")){
							stream = getResources().openRawResource(R.drawable.mapa_mexibus);
						}else{
							URL direccion=null;
							setContentView(R.layout.mapageneral);
							try {
								direccion = new URL("http://www.mainware.com.mx/Proyectos/Mapps/Todos/"+imagen+".png");
								HttpURLConnection conn = (HttpURLConnection) direccion.openConnection();
								conn.setReadTimeout(10000);
								conn.setConnectTimeout(10000);
								conn.setRequestMethod("GET");
								conn.setDoInput(true);
								conn.connect();
								stream = conn.getInputStream();
							} catch (MalformedURLException e) {
								e.printStackTrace();
								Toast.makeText(this, "Que mal, no pude conectarme. Porfa verifica tu conexión. :)", Toast.LENGTH_SHORT).show();
								error=true;
							} catch (IOException e) {
								e.printStackTrace();
								Toast.makeText(this, "Que pena, no pudimos cargar la imagen.", Toast.LENGTH_SHORT).show();
								error=true;
							} catch (Exception e) {
								e.printStackTrace();
								Toast.makeText(this, "Lo siento, ocurrió un problema al cargar la imagen." + (char)(10) + e.getMessage(), Toast.LENGTH_SHORT).show();
								error=true;
							}
						}
					}
				}
			}
		}
        if (!error){
            mZoomState = new ZoomState();
            mBitmap = BitmapFactory.decodeStream(stream);
            IV_mapa.setZoomState(mZoomState);
            IV_mapa.setImage(mBitmap);
            resetZoomState();
            
            IV_mapa.setOnTouchListener( new OnTouchListener(){
    			public boolean onTouch(View v, MotionEvent event) {
			        float dx = 0; 
				   	float dy = 0;
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
    			   				mZoomState.setZoom(mZoomState.getZoom() * (float) Math.pow(20,(distActual - distInicial)));
    		   					distInicial = distActual;
    			   			}else{
    			   				posFinal.set(event.getX(), event.getY());
    			   				dx = (float) ((posFinal.x - posInicial.x) / widthPantalla/mZoomState.getZoom()); 
    		               	   	dy = (float) ((posFinal.y - posInicial.y) / heightPantalla/mZoomState.getZoom());
    		               	   	mZoomState.setPanX(mZoomState.getPanX() - dx);
    		               	 	mZoomState.setPanY(mZoomState.getPanY() - dy);
    			   				posInicial.set(posFinal);
    			   			}
    			   			break;
				   	}
				   	mZoomState.notifyObservers();
    			   return true;
    			} 
    		});
        }
        
		ZoomControls ZM_control = (ZoomControls) findViewById(R.id.zoomControls2);
		ZM_control.setOnZoomInClickListener(new OnClickListener(){
			public void onClick(View arg0) {
				zoomManual = 0.1f;
				rePaint();
			}
		});
		
		ZM_control.setOnZoomOutClickListener(new OnClickListener(){
			public void onClick(View arg0) {
				zoomManual = -0.1f;	
				rePaint();
			}
		});
		
		if (!intent.getBooleanExtra("desactivaBusqueda", false)){
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
		}
    }

    private void resetZoomState() {
        mZoomState.setPanX(0.5f);
        mZoomState.setPanY(0.5f);
        mZoomState.setZoom(1.0f);
        mZoomState.notifyObservers();
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
	    
	public void onDestroy(){
		super.onDestroy();
		if (!error){
			IV_mapa.destroyDrawingCache();
			IV_mapa.removeCallbacks(null);
	        IV_mapa.setOnTouchListener(null);
			IV_mapa=null;
	        mBitmap.recycle();
	        mZoomState.deleteObservers();
		}
		System.gc();
	}
	
	public void rePaint(){
		System.gc();
		try {
			mZoomState.setZoom(mZoomState.getZoom() * (float) Math.pow(20,(zoomManual)));
			mZoomState.notifyObservers();
		} catch(Exception e){
			Toast.makeText(this, "Lo siento, ocurrió un problema al cargar la imagen." + (char)(10) + e.getMessage(), Toast.LENGTH_SHORT).show();
		}
	}
	
    public void mensajeError(String titulo,String mensaje){
    	AlertDialog.Builder builder = new AlertDialog.Builder(this);
    	builder.setMessage(mensaje).setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
    		public void onClick(DialogInterface dialog, int id) {                
    			dialog.dismiss();           
    		}       
    	});
    	AlertDialog alert = builder.create();
    	alert.show();
    }    
}
