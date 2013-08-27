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
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.ProgressBar;
import android.widget.Toast;
import appMetro.origDestino.*;
/**
 * @author XM02664
 *
 */
public class ActivityMapaBarrio  extends Activity {
	private ImageView IV_mapa;
    private Bitmap mBitmap;
	InputStream  stream;
	Drawable imagenDrawable;
	ProgressBar progressBar1;
	
	boolean error=false;
	AsyncTask<String, Void, Bitmap> tarea;
	Context context;
	PointF posAbsoluta = new PointF();
	PointF posInicial = new PointF(); 
	PointF posFinal = new PointF();
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
		setContentView(R.layout.layoutmapabarrio);
		Intent intent = getIntent();
		String imagen = intent.getStringExtra("imagen");
		
		Toast.makeText(this, "Cargando el Mapa...", Toast.LENGTH_LONG).show();
		
		IV_mapa = (ImageView) findViewById(R.id.imageView1);
		IV_mapa.setVisibility(View.GONE);
		progressBar1 = (ProgressBar) findViewById(R.id.progressBar1);
		progressBar1.setVisibility(View.VISIBLE);
		
        tarea = new descargaMapaBarrio();
        tarea.execute("http://www.mainware.com.mx/Proyectos/Mapps/Todos/"+imagen+".png");
        
        IV_mapa.setScaleType(ScaleType.MATRIX);
        IV_mapa.setOnTouchListener( new OnTouchListener(){
			public boolean onTouch(View v, MotionEvent event) {
			   switch (event.getAction() & MotionEvent.ACTION_MASK) {
			   		case MotionEvent.ACTION_DOWN: //first finger down only 
			   			posInicial.set(event.getX(), event.getY());
			   			break;
			   		case MotionEvent.ACTION_MOVE:  
		   				posFinal.set(event.getX(), event.getY());
		   				recorrerPantalla((posInicial.x - posFinal.x),(posInicial.y - posFinal.y));
		   				posInicial.set(posFinal);
			   			break;
			   } 
			   return true;
			} 
		});
    }
	
	private class descargaMapaBarrio extends AsyncTask<String, Void, Bitmap> {     
		protected Bitmap doInBackground(String... urls) {         
			return cargaImagenDeRed(urls[0]);     
		}
		protected void onPostExecute(Bitmap result) {         
			if (result == null){
				Toast.makeText(context, "No se logró cargar la imagen, revisa tu conección a internet.", Toast.LENGTH_LONG).show();
			}else{
				IV_mapa.setImageBitmap(result);
				Toast.makeText(context, "Mapa Cargado", Toast.LENGTH_LONG).show();
			}
			progressBar1.setVisibility(View.GONE);
			IV_mapa.setVisibility(View.VISIBLE);
		} 
	}
	
	private Bitmap cargaImagenDeRed(String url){
		Bitmap mBitmapClon = null;
		try{
			URL direccion = new URL(url);
			HttpURLConnection conn = (HttpURLConnection) direccion.openConnection();
			conn.setReadTimeout(10000);
			conn.setConnectTimeout(10000);
			conn.setDoInput(true);
			conn.setRequestMethod("GET");
			conn.connect();
			stream = conn.getInputStream();
	        mBitmap = BitmapFactory.decodeStream(stream);
	        
	        Paint paint = new Paint();
	        Matrix matrix = new Matrix();
	        matrix.setScale(2f, 2f, 0, 0);
	        mBitmapClon = Bitmap.createBitmap(mBitmap.getWidth()*2, mBitmap.getHeight()*2, mBitmap.getConfig());
	        Canvas canvas = new Canvas(mBitmapClon);
	        canvas.drawBitmap(mBitmap, matrix, paint);
		} catch (MalformedURLException e) {
			e.printStackTrace();
			error=true;
		} catch (IOException e) {
			e.printStackTrace();
			error=true;
		} catch (Exception e) {
			e.printStackTrace();
			error=true;
		}
		return mBitmapClon;
	}

	private void recorrerPantalla(float x, float y) {
		posAbsoluta.set(posAbsoluta.x + x, posAbsoluta.y + y);
		if ((mBitmap.getWidth() * 2) > IV_mapa.getWidth()){
			float width = (float) ((mBitmap.getWidth() * 2) -IV_mapa.getWidth());
			if (posAbsoluta.x > width){
				posAbsoluta.x = (float) width;
			}
			if (posAbsoluta.x < 0){
				posAbsoluta.x = 0;
			}
		}else{
			posAbsoluta.x = 0;
		}
	
		if ((mBitmap.getHeight() * 2) > IV_mapa.getHeight()){
			float height = (float) ((mBitmap.getHeight() * 2) - IV_mapa.getHeight());
			if (posAbsoluta.y > height){
				posAbsoluta.y = (float) height;
			}
			if (posAbsoluta.y < 0){
				posAbsoluta.y = 0;
			}
		}else{
			posAbsoluta.y = 0;
		}
		IV_mapa.scrollTo((int) posAbsoluta.x, (int) posAbsoluta.y);
	 }

	public void onDestroy(){
		super.onDestroy();
		if (!error){
			IV_mapa.destroyDrawingCache();
			IV_mapa.removeCallbacks(null);
	        IV_mapa.setOnTouchListener(null);
			IV_mapa=null;
		}
		tarea.cancel(true);
		System.gc();
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
