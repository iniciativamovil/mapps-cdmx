package appMetro.activityMetro;

import java.util.ArrayList;
import db.Conecciones.DbAdapter;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;
import appMetro.origDestino.*;

public class ActivityMuestraLista extends Activity {
	private ListView lv_main;
	private adapterImage arrayAdapterLin;
	private adapterImage arrayAdapterEst;
	private Bundle savedInstanceState;
	private boolean banderaEstaciones;
	private ArrayList<filaDeLista> datosLista;

	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.savedInstanceState = savedInstanceState;
    	setContentView(R.layout.listanodos);
    	
    	datosLista = new ArrayList<filaDeLista>();
    	arrayAdapterLin = new adapterImage(this,R.layout.list_item_image_gcs, llenaLineas(datosLista));

    	Toast.makeText(getApplicationContext(), "Porfavor, selecciona la lista de estaciones que quieres ver.", Toast.LENGTH_SHORT).show();
    	
    	lv_main = (ListView) findViewById(R.id.listViewNodos);
    	lv_main.setAdapter(arrayAdapterLin);
    	lv_main.setOnItemClickListener(new OnItemClickListener(){
			public void onItemClick(AdapterView<?> arg0, View v, int posicion,
					long arg3) {
				int linea;
				banderaEstaciones = true;
				datosLista = new ArrayList<filaDeLista>();

				Toast.makeText(getApplicationContext(), "Porfavor, selecciona una de las estaciones para mostrarte mas información.", Toast.LENGTH_SHORT).show();
				linea = arrayAdapterLin.getItem(posicion).getID();
				datosLista = llenaEstaciones(linea);	
				arrayAdapterEst = new adapterImage(v.getContext(),R.layout.list_item_image_gcs, datosLista);
				
				lv_main.setAdapter(arrayAdapterEst);
				lv_main.setOnItemClickListener(new OnItemClickListener(){
					public void onItemClick(AdapterView<?> arg0, View v, int posicion,
							long arg3) {						
						String estacion = arrayAdapterEst.getItem(posicion).getTitulo();
						Intent data = getIntent();
						String siguienteActivity = data.getStringExtra("siguienteActivity");
						if ((siguienteActivity != null)){
							if (siguienteActivity.equalsIgnoreCase("ActivityDescripcion")){
								Intent myIntent;
								myIntent = new Intent(v.getContext(), ActivityDescripcion.class);
								myIntent.putExtra("estacionSel", estacion);
								startActivity(myIntent);	
							}else{
								Intent datosRespuesta = new Intent();
								datosRespuesta.putExtra("Estacion", estacion);
								setResult(0, (datosRespuesta));
								finish();
							}
						}else{
							Intent datosRespuesta = new Intent();
							datosRespuesta.putExtra("Estacion", estacion);
							setResult(0, (datosRespuesta));
							finish();	
						}
					}
				});
			}
    	});
    	
    	AutoCompleteTextView TV_busqueda = (AutoCompleteTextView) findViewById(R.id.textBusqueda);
        ImageButton BT_busqueda;        
        BT_busqueda = (ImageButton) this.findViewById(R.id.botonBusqueda);
        BT_busqueda.setOnClickListener(new OnClickListener(){
			public void onClick(View v) {
				AutoCompleteTextView TV_busqueda = (AutoCompleteTextView) findViewById(R.id.textBusqueda);
				Intent myIntent = new Intent(v.getContext(), ActivityBusqueda.class);
				String campo = "";
				if(TV_busqueda.getText().toString().trim().length() > 0){
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

    public ArrayList<filaDeLista> llenaLineas(ArrayList<filaDeLista> datosLista){
    	banderaEstaciones = false;
    	datosLista.clear();
    	int resID = R.drawable.lineatodosicono;
    	datosLista.add(new filaDeLista(0, resID, "¿No sabes en que linea está?" , "Ve todas las estaciones en orden alfabetico.",3,R.drawable.lineatodosbarra,Color.WHITE));
    	DbAdapter baseDatos = new DbAdapter(this);
    	baseDatos.open();
    	Cursor cur_lineas = baseDatos.fetchAllTable("tb_grupos",new String[] {"id_grupo", "nombre","imagen","id_nodo_ta","id_nodo_tb","descripcion"});
    	cur_lineas.moveToFirst();
    	StringBuffer a;
    	do{
    		a = new StringBuffer();
			resID = getResources().getIdentifier(cur_lineas.getString(2), "drawable", "appMetro.origDestino");
			Cursor curNodoA = baseDatos.fetchReg("tb_nodos",new String[] {"nombre","imagen"},"id_nodo",cur_lineas.getLong(3));
			a.append(curNodoA.getString(0)).append(" - ");
			curNodoA.close();
			Cursor curNodoB = baseDatos.fetchReg("tb_nodos",new String[] {"nombre","imagen"},"id_nodo",cur_lineas.getLong(4));
			a.append(curNodoB.getString(0));
			curNodoB.close();
			datosLista.add(new filaDeLista(cur_lineas.getInt(0), resID, "Linea " + cur_lineas.getString(5), a.toString(),1));
    	} while (cur_lineas.moveToNext());
        cur_lineas.close();
    	baseDatos.close();
        return datosLista;
    }
    
    public ArrayList<filaDeLista> llenaEstaciones(int linea){
    	DbAdapter baseDatos = new DbAdapter(this);
    	baseDatos.open();
    	ArrayList<filaDeLista> datosLista = new ArrayList<filaDeLista>();
    	if(linea == 0){
    	   	Cursor curNodos;
    	    curNodos = baseDatos.fetchAllTable(true,"tb_nodos",new String[] {"id_nodo","nombre","imagen"});
    	    curNodos.moveToFirst();
    	   	do{
    	   		int resID = getResources().getIdentifier(curNodos.getString(2), "drawable", "appMetro.origDestino");
    	   		filaDeLista filaTemporal = new filaDeLista((int)curNodos.getInt(0),resID, curNodos.getString(1),"", 2);
    	   		int i = 0;
    	   		boolean agregado = false;
    	   		while ((i<=datosLista.size()) && (!agregado)){
					if (i == datosLista.size()){
						datosLista.add(filaTemporal.clone());
						agregado = true;
					}else{
						if (datosLista.get(i).compareTo(filaTemporal) >= 0){
    						datosLista.add(i,filaTemporal.clone());
    						agregado = true;
    					}	
					}
					i++;
				}
    	   	} while (curNodos.moveToNext());
    	    curNodos.close();

    	}else{
	    	Cursor curGrupo = baseDatos.fetchReg("tb_grupos",new String[] {"id_grupo","id_nodo_ta"},"id_grupo",(long) linea);
	    	Long nodoGrupo = curGrupo.getLong(0);
	    	Long nodoIniTemp = curGrupo.getLong(1);
	    	Long nodoFinTemp = curGrupo.getLong(1);
	    	curGrupo.close();
	
	    	Boolean finNodos=false;
	    	Cursor curNodos;
	    	Cursor curAdyacencia;
	    	do{
	            curNodos = baseDatos.fetchReg("tb_nodos",new String[] {"nombre","imagen"},"id_nodo",nodoFinTemp);
	            int resID = getResources().getIdentifier(curNodos.getString(1), "drawable", "appMetro.origDestino");
	            datosLista.add(new filaDeLista(nodoFinTemp.intValue(),resID, curNodos.getString(0),"", 2));
	            curNodos.close();
	            
	    		curAdyacencia = baseDatos.fetchReg("tb_adyacencias",new String[] {"id_nodo_final"},new String[] {"id_grupo =","id_nodo_ini =","id_nodo_final !="},new Long[] {nodoGrupo,nodoFinTemp,nodoIniTemp});
	    		if (curAdyacencia.getCount()==0){
	    			finNodos=true;
	    		}else{
		    		nodoIniTemp = nodoFinTemp;
		    		nodoFinTemp = curAdyacencia.getLong(0);
	    		}
	    		curAdyacencia.close();
	    	} while (!finNodos);
    	}
        baseDatos.close();
        return datosLista;
    }
    
	public void onBackPressed(){
		if (banderaEstaciones == true){
			onCreate(this.savedInstanceState);
		}else{
			Intent datosRespuesta = new Intent();
			datosRespuesta.putExtra("Estacion", "");
			setResult(0, (datosRespuesta));
			super.finish();	
		}
	}   
    
}
