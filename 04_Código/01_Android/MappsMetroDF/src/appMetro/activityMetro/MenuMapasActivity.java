package appMetro.activityMetro;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.ImageView;
import appMetro.origDestino.*;

public class MenuMapasActivity  extends Activity {
	ImageView IV_mapaMetro;
	ImageView IV_mapaTren;
	ImageView IV_mapaMetrobus;
	ImageView IV_mapaSuburbano;
	ImageView IV_mapaMexiBus;
	
	ImageButton banner;
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		setContentView(R.layout.layoutmenumapas);

		IV_mapaMetro = (ImageView) findViewById(R.id.imageView1);
		IV_mapaMetro.setOnClickListener(new View.OnClickListener() {			
        	public void onClick(View v) {
        		Intent myIntent = new Intent(v.getContext(), ActivityMapa.class);
        		myIntent.putExtra("imagen", "Metro"); 
	    		startActivity(myIntent);
        	}
        });
		
		IV_mapaTren = (ImageView) findViewById(R.id.imageView2);
		IV_mapaTren.setOnClickListener(new View.OnClickListener() {			
        	public void onClick(View v) {
        		Intent myIntent = new Intent(v.getContext(), ActivityMapa.class);
        		myIntent.putExtra("imagen", "Metrobus"); 
	    		startActivity(myIntent);
        	}
        });
		
		IV_mapaMetrobus = (ImageView) findViewById(R.id.imageView3);
		IV_mapaMetrobus.setOnClickListener(new View.OnClickListener() {			
        	public void onClick(View v) {
        		Intent myIntent = new Intent(v.getContext(), ActivityMapa.class);
        		myIntent.putExtra("imagen", "Tren"); 
	    		startActivity(myIntent);
        	}
        });
		
		IV_mapaSuburbano = (ImageView) findViewById(R.id.imageView4);
		IV_mapaSuburbano.setOnClickListener(new View.OnClickListener() {			
        	public void onClick(View v) {
        		Intent myIntent = new Intent(v.getContext(), ActivityMapa.class);
        		myIntent.putExtra("imagen", "Suburbano"); 
	    		startActivity(myIntent);
        	}
        });
		
		IV_mapaMexiBus = (ImageView) findViewById(R.id.imageView5);
		IV_mapaMexiBus.setOnClickListener(new View.OnClickListener() {			
        	public void onClick(View v) {
//        		mensajeError("Aviso.","Proximamente en tu App Favorita... El mapa del MexiBus...");
        		Intent myIntent = new Intent(v.getContext(), ActivityMapa.class);
        		myIntent.putExtra("imagen", "MexiBus"); 
	    		startActivity(myIntent);
        	}
        });
		
		banner = (ImageButton) this.findViewById(R.id.bannerBoton);
        banner.setOnClickListener(new OnClickListener(){
			public void onClick(View v) {
//				mensajeError("Aviso.","Hay mucha información en la aplicacion como referencias, tiendas, rutas, mapas y otras curiosidades más..." + (char)(10)+(char)(10)+ " No dejes de visitar cada seccion.");
				String url = "http://www.iniciativamovil.com.mx/formulario.html";
				Intent myIntent = new Intent(Intent.ACTION_VIEW);
				myIntent.setData(Uri.parse(url));
				startActivity(myIntent);
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
        AutoCompleteTextView TV_busqueda = (AutoCompleteTextView) findViewById(R.id.textBusqueda);
        TV_busqueda.clearFocus();
		InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(TV_busqueda.getWindowToken(), 0);
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

	public void onDestroy(){
		super.onDestroy();
		IV_mapaMetro.destroyDrawingCache();
		IV_mapaMetro.removeCallbacks(null);
		IV_mapaMetro=null;
		IV_mapaTren.destroyDrawingCache();
		IV_mapaTren.removeCallbacks(null);
		IV_mapaTren=null;
		IV_mapaMetrobus.destroyDrawingCache();
		IV_mapaMetrobus.removeCallbacks(null);
		IV_mapaMetrobus=null;
		System.gc();
	}
}
