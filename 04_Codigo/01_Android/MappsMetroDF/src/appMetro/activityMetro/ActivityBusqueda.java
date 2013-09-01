package appMetro.activityMetro;

import java.util.ArrayList;
import db.Conecciones.DbAdapter;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import appMetro.origDestino.R;
import appMetro.origDestino.adapterImage;
import appMetro.origDestino.filaDeLista;

public class ActivityBusqueda extends Activity {
	TextView TV_tipoBusqueda;
	ListView listaCategorias;
	ListView listaGrupos;
	ListView listaResultados;
	LinearLayout listasSeleccionables;
	ImageButton	 botonBuscar;
	TextView textBusquedaAvanzada;
    DbAdapter baseDatos = new DbAdapter(this);
	
    String[] arrayCategorias = {"No hay datos para mostrar..."};
	Long[] arrayIDCategorias = new Long[1];
	
	private String BUSQUEDA_AVANZADA = "Mas Opciones...";
	private String HIDE_BUSQUEDA_AVANZADA = "Esconder Opciones";
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.busquedaavanzada);
        Intent intent = getIntent();
        
        textBusquedaAvanzada = (TextView) this.findViewById(R.id.textBusquedaAvanzada);
        listaCategorias = (ListView) this.findViewById(R.id.listClasesSeleccion);
        listaGrupos = (ListView) this.findViewById(R.id.listGruposSeleccion);
        listaResultados = (ListView) this.findViewById(R.id.listResultadoBusqueda);
        listasSeleccionables = (LinearLayout) this.findViewById(R.id.listSeleccionables);
        botonBuscar = (ImageButton) this.findViewById(R.id.botonBusquedaAvanzada);
        TV_tipoBusqueda = (TextView) this.findViewById(R.id.textTipoBusqueda);

		textBusquedaAvanzada.clearFocus();
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(textBusquedaAvanzada.getWindowToken(), 0);
        
//		listaCategorias.setVisibility(View.GONE);
//		listaGrupos.setVisibility(View.GONE);
//		listasSeleccionables.setVisibility(View.GONE);
//		TV_tipoBusqueda.setVisibility(View.GONE);
        TV_tipoBusqueda.setOnClickListener(new OnClickListener(){
			public void onClick(View arg0) {
				if (TV_tipoBusqueda.getText().toString().equals(BUSQUEDA_AVANZADA)){
					esconderOpciones(false);
				}else{
					esconderOpciones(true);
				}
			}
        });

        botonBuscar.setOnClickListener(new OnClickListener(){
			public void onClick(View v) {
				boolean error = false;
				int longTexto = textBusquedaAvanzada.getText().length();
				for (int i=0 ; i < longTexto && !error ; i++){
					if ((String.valueOf(textBusquedaAvanzada.getText().charAt(i)).equals("%")) ||
						(String.valueOf(textBusquedaAvanzada.getText().charAt(i)).equals("-")) ||
						(String.valueOf(textBusquedaAvanzada.getText().charAt(i)).equals("'"))){
						error = true;
					}
				}
				if (error){
					Toast.makeText(v.getContext(), "Texto ingresado invalido. Quita los caracteres %(porcentaje), -(guion) y '(comilla). Gracias.", Toast.LENGTH_SHORT).show();
				}else{
					esconderOpciones(true);
					realizaBusqueda(textBusquedaAvanzada.getText().toString());
				}
			}
        });
        
		listaResultados.setOnItemClickListener(new OnItemClickListener(){
			public void onItemClick(AdapterView<?> arg0, View v, int posicion,
					long arg3) {
				String estacion = ((filaDeLista) listaResultados.getAdapter().getItem(posicion)).getTitulo();
				Intent myIntent = new Intent(v.getContext(), ActivityDescripcion.class);
				myIntent.putExtra("estacionSel", estacion);
				startActivity(myIntent);
			}
		});
		
        TextView headerCategs = new TextView(this);
        headerCategs.setText("Comercios y Servicios");
        headerCategs.setBackgroundResource(R.drawable.bg_header_naranja);
//		headerCategs.setTextColor(Color.DKGRAY);
        headerCategs.setTextColor(Color.WHITE);
        headerCategs.setGravity(Gravity.CENTER);
        headerCategs.setMaxHeight(20);
        headerCategs.setSingleLine(false);
        headerCategs.setHeight(50);
        listaCategorias.addHeaderView(headerCategs);
        
        TextView headerGrupos = new TextView(this);
        headerGrupos.setText("Referencias y Otros...");
        headerGrupos.setBackgroundResource(R.drawable.bg_header_naranja);
//		headerGrupos.setTextColor(Color.DKGRAY);
        headerGrupos.setTextColor(Color.WHITE);
        headerGrupos.setGravity(Gravity.CENTER);
        headerGrupos.setHeight(50);
        headerGrupos.setSingleLine(false);
        listaGrupos.addHeaderView(headerGrupos);

        TextView headerResultados = new TextView(this);
        headerResultados.setText("Resultado de la Busqueda");
        headerResultados.setBackgroundResource(R.drawable.bg_header_naranja);
        headerResultados.setTextColor(Color.WHITE);
        headerResultados.setGravity(Gravity.CENTER);
        headerResultados.setHeight(10);
        headerResultados.setSingleLine(true);
        listaResultados.addHeaderView(headerResultados);
        
        baseDatos.open();
    	Cursor curBusClases = baseDatos.fetchAllTable("tb_clases",new String[] {"id_clase","nombre","imagen"});
//    	ArrayList<filaDeLista> datosCategorias = new ArrayList<filaDeLista>();
    	if(curBusClases.getCount()>0){
    		curBusClases.moveToFirst();
    		arrayCategorias = new String[curBusClases.getCount()];
    		arrayIDCategorias = new Long[curBusClases.getCount()];
    		int numCateg = 0;
    		do{
    			arrayCategorias[numCateg] = curBusClases.getString(1);
    			arrayIDCategorias[numCateg] = curBusClases.getLong(0);
    			numCateg++;
//    			datosCategorias.add(new filaDeLista((int) curBusClases.getLong(0), (int)curBusClases.getLong(2), curBusClases.getString(1), "",2));
    		}while(curBusClases.moveToNext());
    	}
    	curBusClases.close();
    	baseDatos.close();

//    	adapterImage adaptadorCategorias = new adapterImage(this, R.layout.list_item_image_gcs, datosCategorias);
        ArrayAdapter<?> adaptadorCategorias = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_checked,arrayCategorias);
        listaCategorias.setAdapter(adaptadorCategorias);
        
        String[] arrayGrupos = {"Estaciones","Referencias"};
        ArrayAdapter<?> adaptadorGrupos = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_checked, arrayGrupos);
        listaGrupos.setAdapter(adaptadorGrupos);

        for(int i=0;i<listaCategorias.getCount();i++){
        	listaCategorias.setItemChecked(i, true);
        }
        for(int i=0;i<listaGrupos.getCount();i++){
        	listaGrupos.setItemChecked(i, true);
        }

        esconderOpciones(true);
        
        String campo = intent.getStringExtra("busqueda");
    	textBusquedaAvanzada.setText(campo);
        if (!campo.equals("")){
        	intent.putExtra("busqueda","");
        	botonBuscar.performClick();	
        }else{
        	listaResultados.setVisibility(View.GONE);
        }
	}
	
	public void esconderOpciones(boolean esconder){
		if (esconder){
			listaCategorias.setVisibility(View.GONE);
	        listaGrupos.setVisibility(View.GONE);
	        listasSeleccionables.setVisibility(View.GONE);
			TV_tipoBusqueda.setText(BUSQUEDA_AVANZADA);	
		}else{
			listaCategorias.setVisibility(View.VISIBLE);
            listaGrupos.setVisibility(View.VISIBLE);
            listasSeleccionables.setVisibility(View.VISIBLE);
			TV_tipoBusqueda.setText(HIDE_BUSQUEDA_AVANZADA);
		}
	}
	
	public void realizaBusqueda (String campo){
		campo = campo.trim();
		SparseBooleanArray seleccionadosGrupos = listaGrupos.getCheckedItemPositions();
		SparseBooleanArray seleccionadosCategs = listaCategorias.getCheckedItemPositions();
		
		StringBuffer limitantesCategorias = new StringBuffer();
		limitantesCategorias.append(" id_clase in (");
		boolean primero = true;
		for(int i=1;i<listaCategorias.getCount();i++){
			if (seleccionadosCategs.get(i)){
				if (primero){
					limitantesCategorias.append(arrayIDCategorias[i-1]);
					primero = false;
				}else{
					limitantesCategorias.append(",").append(arrayIDCategorias[i-1]);
				}
			}
        }
		limitantesCategorias.append(") and (");
		
		ArrayList<Long> nodos_busqueda = new ArrayList<Long>();
		StringBuffer query = new StringBuffer();
    	baseDatos.open();
    	query.append(limitantesCategorias.toString()).append("nombre like '%").append(campo).append("%')");
//    	Log.d("Queries", query.toString());
    	Cursor curBusClases =baseDatos.fetchReg("tb_clases",new String[] {"id_clase"}, query.toString());
    	StringBuffer clases = new StringBuffer();
    	if (curBusClases.getCount()>0){
    		clases.append(String.valueOf(curBusClases.getLong(0)));
        	while(curBusClases.moveToNext()){
	        	clases.append(",").append(String.valueOf(curBusClases.getLong(0)));
        	}
    	}
    	curBusClases.close();
    	query = new StringBuffer();
    	query.append(limitantesCategorias.toString()).append("id_clase in (").append(clases.toString()).append(") or nombre like '%").append(campo).append("%' or descripcion like '").append(campo).append("%')");
//    	Log.d("Queries", query.toString());
    	Cursor curBusProp = baseDatos.fetchReg("tb_propiedades",new String[] {"id_propiedad"}, query.toString() );
    	StringBuffer propiedades = new StringBuffer();
    	if (curBusProp.getCount()>0){
    		propiedades.append(String.valueOf(curBusProp.getLong(0)));
        	while(curBusProp.moveToNext()){
        		propiedades.append(",").append(String.valueOf(curBusProp.getLong(0)));
        	}
    	}
    	curBusProp.close();
    	
    	query = new StringBuffer();
    	query.append("id_propiedad in (").append(propiedades.toString()).append(") and estatus = 1");
    	Log.d("Queries", query.toString());
    	Cursor curBusAsigna = baseDatos.fetchReg("tb_asignaciones",new String[] {"id_nodo"}, query.toString());
    	if(curBusAsigna.getCount()>0){
    		do {
    			nodos_busqueda.add(curBusAsigna.getLong(0));
    		}while(curBusAsigna.moveToNext());
    	}
    	curBusAsigna.close();

    	if(seleccionadosGrupos.get(1)){
	    	query = new StringBuffer();
	    	query.append("nombre like '%").append(campo).append("%' or descripcion like '%").append(campo).append("%'");
//	    	Log.d("Queries", query.toString());
	    	Cursor curBusGrupos = baseDatos.fetchReg("tb_grupos",new String[] {"id_grupo"}, query.toString());
	    	StringBuffer grupos = new StringBuffer();
	    	if(curBusGrupos.getCount()>0){
	    		grupos.append(String.valueOf(curBusGrupos.getLong(0)));
	    		do {
	    			grupos.append(",").append(String.valueOf(curBusGrupos.getLong(0)));
	    		}while(curBusGrupos.moveToNext());
	    	}
	    	curBusGrupos.close();
	    	
	    	query = new StringBuffer();
	    	query.append("id_grupo in (").append(grupos.toString()).append(")");
//	    	Log.d("Queries", query.toString());
	    	Cursor curBusAdyacencia =baseDatos.fetchReg("tb_adyacencias",new String[] {"id_nodo_ini"}, query.toString());
	    	if(curBusAdyacencia.getCount()>0){
	    		do {
	    			nodos_busqueda.add(curBusAdyacencia.getLong(0));
	    		}while(curBusAdyacencia.moveToNext());
	    	}
	    	curBusAdyacencia.close();
    	}
    	
    	if(seleccionadosGrupos.get(2)){
	    	query = new StringBuffer();
	    	query.append("des_corta like '%").append(campo).append("%' or des_larga like '%").append(campo).append("%'");
//	    	Log.d("Queries", query.toString());
	    	Cursor curBusRefer =baseDatos.fetchReg("tb_referencias",new String[] {"id_nodo"}, query.toString());
	    	if(curBusRefer.getCount()>0){
	    		do {
	    			nodos_busqueda.add(curBusRefer.getLong(0));
	    		}while(curBusRefer.moveToNext());
	    	}
	    	curBusRefer.close();
    	}

    	StringBuffer nodos = new StringBuffer();
    	if(nodos_busqueda.size()>0){
    		nodos.append(String.valueOf(nodos_busqueda.get(0)));	
        	for (int i=1;i< nodos_busqueda.size();i++){
        		nodos.append(",").append((String.valueOf(nodos_busqueda.get(i))));
        	}
    	}
    	
    	query = new StringBuffer();
    	query.append(" id_nodo in (").append(nodos.toString()).append(")");
    	
    	if(seleccionadosGrupos.get(1)){
    		query.append(" or nombre like '%").append(campo).append("%' or  descripcion like '%").append(campo).append("%'");
    	}
    	
//    	Log.d("Queries", query.toString());
    	Cursor curBusNodos = baseDatos.fetchReg("tb_nodos",new String[] {"nombre","id_nodo","imagen"}, query.toString());
    	int numEncontrados = curBusNodos.getCount();
    	if (numEncontrados > 0){
        	long[] nodos_id_array= new long[numEncontrados];
        	String[] nodos_nombre_array = new String[numEncontrados];
        	ArrayList<filaDeLista> datosLista = new ArrayList<filaDeLista>();
        	int i=0;
        	do{
        		nodos_id_array[i] = curBusNodos.getLong(1);
            	nodos_nombre_array[i] = curBusNodos.getString(0);
            	int resID = getResources().getIdentifier(curBusNodos.getString(2), "drawable", "appMetro.origDestino");
            	datosLista.add(new filaDeLista((int) curBusNodos.getLong(1), resID, curBusNodos.getString(0), "",2));
            	i++;
        	}while(curBusNodos.moveToNext());
        	adapterImage adapterResultados = new adapterImage(this, R.layout.list_item_image_gcs, datosLista);

        	listaResultados.setAdapter(adapterResultados);
    		Toast.makeText(getApplicationContext(), "Porfavor, selecciona la estacion que quieres ver.", Toast.LENGTH_SHORT).show();
    		listaResultados.setVisibility(View.VISIBLE);
    	}else{
    		Toast.makeText(this, "Elementos no encontrados, intenta una nueva busqueda...", Toast.LENGTH_LONG).show();
    		listaResultados.setVisibility(View.GONE);
    	}
    	curBusNodos.close();
    	baseDatos.close();
	}
}