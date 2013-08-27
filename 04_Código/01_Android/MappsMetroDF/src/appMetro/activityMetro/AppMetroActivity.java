package appMetro.activityMetro;

import android.net.Uri;
import android.os.Bundle;
import android.content.*;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import android.app.Activity;
import android.app.AlertDialog;
import appMetro.origDestino.*;

public class AppMetroActivity extends Activity  {
    /** Called when the activity is first created. */
    ImageButton BT_ruta;
    ImageButton BT_mapas;
    ImageButton BT_directorio;
    ImageButton BT_banner;
    String estacion;
    
    public static final int GET_CODE = 0;
    
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        
//		  ******************************************************************************************
//        *** Código para poner la barra de título, personalizado. (La que acabamos de esconder) ***
//		  ******************************************************************************************
//        final boolean customTitleSupported = requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);        
//        if(customTitleSupported){
//        	getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.titlebar);
//        }

        setContentView(R.layout.main);
        
        BT_ruta = (ImageButton) this.findViewById(R.id.rutaBoton);
        BT_ruta.setOnClickListener(new OnClickListener(){
			public void onClick(View v) {
	    		Intent myIntent = new Intent(v.getContext(), ActivityRutaMain.class);
	    		startActivity(myIntent);
			}
        });
        
        BT_mapas = (ImageButton) this.findViewById(R.id.mapasBoton);
        BT_mapas.setOnClickListener(new OnClickListener(){
			public void onClick(View v) {
	    		Intent myIntent = new Intent(v.getContext(), MenuMapasActivity.class);
	    		startActivity(myIntent);
			}
        });
        
        BT_directorio = (ImageButton) this.findViewById(R.id.directorioBoton);
        BT_directorio.setOnClickListener(new OnClickListener(){
			public void onClick(View v) {
				Intent myIntent = new Intent(v.getContext(), ActivityMuestraLista.class);
//				startActivityForResult(myIntent, GET_CODE);
				myIntent.putExtra("siguienteActivity", "ActivityDescripcion");
				startActivity(myIntent);
			}
        });
        
        BT_banner = (ImageButton) this.findViewById(R.id.bannerBoton);
        BT_banner.setOnClickListener(new OnClickListener(){
			public void onClick(View v) {
				String url = "http://www.iniciativamovil.com.mx/formulario.html";
				Intent myIntent = new Intent(Intent.ACTION_VIEW);
				myIntent.setData(Uri.parse(url));
				startActivity(myIntent);
			}
        });
        
        AutoCompleteTextView TV_busqueda;
        TV_busqueda = (AutoCompleteTextView) findViewById(R.id.textBusqueda);
        ImageButton BT_busqueda;        
        BT_busqueda = (ImageButton) this.findViewById(R.id.botonBusqueda);
        BT_busqueda.setOnClickListener(new OnClickListener(){
			public void onClick(View v) {
				Intent myIntent = new Intent(v.getContext(), ActivityBusqueda.class);
				String campo = "";
				AutoCompleteTextView TV_busqueda = (AutoCompleteTextView) findViewById(R.id.textBusqueda);
				if(TV_busqueda.toString().trim().length() > 0){
					campo = TV_busqueda.getText().toString().trim();
				}
				myIntent.putExtra("busqueda", campo);
	    		startActivity(myIntent);
			}
        });
        TV_busqueda.clearFocus();
		InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(TV_busqueda.getWindowToken(), 0);
    }
    
    protected void onActivityResult(int resultCode, int requestCode, Intent data){
    	String resultado = data.getStringExtra("Estacion");
		if (resultado.compareTo("") != 0){
			estacion = resultado;
			Intent myIntent = new Intent(this, ActivityDescripcion.class);
			myIntent.putExtra("estacionSel", estacion);
			startActivity(myIntent);
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
    
    public void onBackPressed(){
    	finish ();
    	Intent myIntent = new Intent(this, ActivitySpashSalida.class);
		startActivity(myIntent);
		super.finish ();
    }
}
