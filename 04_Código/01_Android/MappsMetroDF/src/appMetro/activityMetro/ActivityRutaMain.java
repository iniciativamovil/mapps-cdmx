package appMetro.activityMetro;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import appMetro.origDestino.R;

public class ActivityRutaMain extends Activity {
	ImageButton bt_calcula;
	TextView tv_calculaRuta;

	ImageButton bt_imageButton1;
	ImageButton bt_imageButton2;
	TextView tv_dondeEstas;
	TextView tv_dondeEstasSub;
	TextView tv_dondeVas;
	TextView tv_dondeVasSub;
	ImageView iv_instrucciones;
	TextView tv_ayuda;
	
	ImageButton bannerBoton;
	
	boolean seleccionOrigen = false;
	boolean seleccionDestino = false;
	String datoObtenido = "Origen";
	String eOrigen; 
	String eDestino;
	public static final int GET_CODE = 0;
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    	setContentView(R.layout.rutamain);
    	
    	tv_dondeEstas 	 = (TextView) findViewById(R.id.textDondeEstas);
    	tv_dondeEstasSub = (TextView) findViewById(R.id.textDondeEstasSub);
    	tv_dondeVas 	 = (TextView) findViewById(R.id.textDondeVas);
    	tv_dondeVasSub 	 = (TextView) findViewById(R.id.textDondeVasSub);
    	
    	
    	tv_dondeEstas.setOnClickListener(new OnClickListener(){
    		public void onClick(View v){
    			bt_imageButton1.performClick();
    		}
    	});
    	tv_dondeEstasSub.setOnClickListener(new OnClickListener(){
    		public void onClick(View v){
    			bt_imageButton1.performClick();
    		}
    	});
    	
        bt_imageButton1 = (ImageButton) findViewById(R.id.imageButton1);
        bt_imageButton1.setOnClickListener(new OnClickListener(){
			public void onClick(View v) {
				datoObtenido = "Origen";
				Intent myIntent = new Intent(v.getContext(), ActivityMuestraLista.class);
				startActivityForResult(myIntent, GET_CODE);
			}
        });
        
        
        tv_dondeVas.setOnClickListener(new OnClickListener(){
    		public void onClick(View v){
    			bt_imageButton2.performClick();
    		}
    	});
        tv_dondeVasSub.setOnClickListener(new OnClickListener(){
    		public void onClick(View v){
    			bt_imageButton2.performClick();
    		}
    	});
        
        bt_imageButton2 = (ImageButton) findViewById(R.id.imageButton2);
        bt_imageButton2.setOnClickListener(new OnClickListener(){
			public void onClick(View v) {
				datoObtenido = "Destino";
				Intent myIntent = new Intent(v.getContext(), ActivityMuestraLista.class);
				startActivityForResult(myIntent, GET_CODE);
			}
        });
        
        tv_ayuda = (TextView) findViewById(R.id.textAyuda);
        tv_ayuda.setOnClickListener(new OnClickListener(){
        	public void onClick(View v){
        		iv_instrucciones.performClick();
        	}
        });
        
        iv_instrucciones = (ImageView) findViewById(R.id.imageAyuda);
        iv_instrucciones.setOnClickListener(new OnClickListener(){
			public void onClick(View arg0) {
		        StringBuffer bs = new StringBuffer();
				bs.append("Hey, tienes dudas?");
				bs.append((char)(10));
				bs.append("Para encontrar los trayectos para llegar a tu destino, sigue los siguientes pasos:");
				bs.append((char)(10));
				bs.append(" 1.- Selecciona  la estación donde te encuentras.");
				bs.append((char)(10));
				bs.append(" 2.- Selecciona la estacion a la que quieres llegar.");
				bs.append((char)(10));
				bs.append(" 3.- Da click en el icono de 'Calcula la Ruta'.");
				bs.append((char)(10));
				bs.append("Que tengas un excelente viaje.");
		        mensajeError(":: Ayuda ::",bs.toString());				
			}
        });
        
        
        tv_calculaRuta = (TextView) findViewById(R.id.textCalcula);
        tv_calculaRuta.setOnClickListener(new OnClickListener(){
        	public void onClick(View v){
        		bt_calcula.performClick();
        	}
        });
        
        bt_calcula = (ImageButton) findViewById(R.id.Button1);
        bt_calcula.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View v) {
        		if (seleccionOrigen && seleccionDestino){
        	    	if (eOrigen.equals(eDestino)){
            	        mensajeError("Error","Para generar una ruta, necesito que elijas diferentes estaciones. Muchas gracias.");
        	    	}else{
        	    		Intent myIntent = new Intent(v.getContext(), ActivityResultado.class);
        	    		myIntent.putExtra("Origen", eOrigen);
        	    		myIntent.putExtra("Destino", eDestino);
        	    		startActivity(myIntent);
        	    	}
        	    }else{
        	    	if (seleccionOrigen){
        	    		mensajeError("Error","Antes de calcular la ruta, necesito que selecciones la estacion a donde quires llegar, dando click en el icono. Gracias.");
        	    	}else{
        	    		mensajeError("Error","Antes de calcular la ruta, necesito que selecciones la estacion en donde estás, dando click en el icono superior. Muchas gracias.");
        	    	}        	        
        	    }  	
        	}
		});
        
        bannerBoton = (ImageButton) findViewById(R.id.bannerBoton);
        bannerBoton.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View v) {
//        		mensajeError("Publicidad Efectiva...","Si quieres ser parte de nuestros patrocinadores. Contactanos en la página web:"+(char)(10)+(char)(10)+" http://www.iniciativamovil.com");
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
    
    protected void onActivityResult(int resultCode, int requestCode, Intent data){
    	String resultado = data.getStringExtra("Estacion");
		if (resultado.compareTo("") != 0){
			if (datoObtenido == "Origen"){
				eOrigen = resultado;
				tv_dondeEstas.setWidth(50);
				tv_dondeEstas.setText(resultado);
				tv_dondeEstas.setTextSize(12);
				tv_dondeEstas.setGravity(Gravity.LEFT);
				tv_dondeEstas.setGravity(Gravity.CENTER_VERTICAL);
				tv_dondeEstas.setPadding(10, 0, 80, 0);
				tv_dondeEstas.setBackgroundResource(R.drawable.letreroestas);
		    	tv_dondeEstasSub.setText("");
				tv_dondeEstasSub.setVisibility(View.GONE);
				seleccionOrigen = true;
			}else{
				tv_dondeEstas.setWidth(50);
		    	tv_dondeVas.setText(resultado);
		    	tv_dondeVas.setBackgroundResource(R.drawable.letrerovas);
		    	tv_dondeVas.setTextSize(12);
		    	tv_dondeVas.setGravity(Gravity.LEFT);
		    	tv_dondeVas.setGravity(Gravity.CENTER_VERTICAL);
		    	tv_dondeVas.setPadding(10, 0, 80, 0);
		    	tv_dondeVasSub.setText("");
		    	tv_dondeVasSub.setVisibility(View.GONE);
				eDestino = resultado;
				seleccionDestino = true;
				if (((resultado.length()*15)+90) >= tv_dondeVas.getWidth()){
					tv_dondeVas.setHeight(tv_dondeVas.getHeight()*2);
				}
			}
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