package appMetro.activityMetro;
import java.util.ArrayList;
import java.util.Locale;
import db.Conecciones.DbAdapter;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import appMetro.origDestino.*;
 	
/**
 * @author GCS
 *
 */
public class ActivityResultado extends Activity implements OnInitListener{
	private TextToSpeech mTts;
	int MY_DATA_CHECK_CODE = 0;
	int resNumAnt;
	Nodo grafo[];
	String eOrigen;
	String eDestino;
	String resultado[][];
	float  coordenadas[][];
	ArrayList<Ruta> rutas;
	ArrayList<filaDeResultado> datosLista = new ArrayList<filaDeResultado>();
	
	TextView tx_textListaResultado;
	
	ListView LV_Resultado;
	ImageButton BT_verRuta;
	LinearLayout LL_verRuta;
	TextView TV_verRuta;
	
	ImageButton BT_escucharRuta;
	LinearLayout LL_escucharRuta;
	TextView TV_escucharRuta;
	
	ImageButton BT_compartir;
	LinearLayout LL_compartir;
	TextView TV_compartir;
	
	long tiempo = 0;
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.muestraresultado);
		
		mTts = new TextToSpeech(this, this);
		
		Intent intent = getIntent();
		eOrigen = intent.getStringExtra("Origen");
		eDestino = intent.getStringExtra("Destino");
        rutas = calculaRuta(eOrigen,eDestino);
        datosLista =  muestraResultados(rutas);
        
        adapterFilaResultado arrayAdapterRes = new adapterFilaResultado(this,R.layout.list_item_layout_gcs,datosLista);
        
        LV_Resultado = (ListView) findViewById(R.id.listView1);
		LV_Resultado.setAdapter(arrayAdapterRes);

		this.resNumAnt = -1;
		validaEnviaResultados(0);
		LV_Resultado.setOnItemClickListener(new OnItemClickListener () {
			public void onItemClick(AdapterView<?> a, View v, int posicion, long id) {
				if (mTts.isSpeaking()){
					LL_escucharRuta.setSelected(false);
					mTts.stop();
				}
				if (resNumAnt < 0){
					((filaDeResultado)(a.getAdapter().getItem(0))).setSeleccionado(false);	
				}else{
					((filaDeResultado)(a.getAdapter().getItem(resNumAnt))).setSeleccionado(false);
				}
				((filaDeResultado)(a.getAdapter().getItem(posicion))).setSeleccionado(true);
				
				for (int posQuitaSeleccion=0; posQuitaSeleccion<LV_Resultado.getChildCount();posQuitaSeleccion++){
					LV_Resultado.getChildAt(posQuitaSeleccion).setBackgroundResource(R.drawable.bgresultado_inactivo);
					((ImageView)LV_Resultado.getChildAt(posQuitaSeleccion).findViewById(R.id.imgRelog)).setImageResource(R.drawable.reloj_inactivo);
					((TextView)LV_Resultado.getChildAt(posQuitaSeleccion).findViewById(R.id.txtRutaCorto)).setText(datosLista.get(posicion).getTxtRuta());
					((TextView)LV_Resultado.getChildAt(posQuitaSeleccion).findViewById(R.id.txtRutaCorto)).setTextColor(Color.DKGRAY);
					((TextView)LV_Resultado.getChildAt(posQuitaSeleccion).findViewById(R.id.txtNoResultado)).setTextColor(Color.DKGRAY);
					((TextView)LV_Resultado.getChildAt(posQuitaSeleccion).findViewById(R.id.txtTiempos)).setTextColor(Color.DKGRAY);
				}
				v.setBackgroundResource(R.drawable.bgresultado_activo);
				((ImageView)v.findViewById(R.id.imgRelog)).setImageResource(R.drawable.reloj_activo);
				((TextView)v.findViewById(R.id.txtRutaCorto)).setText(datosLista.get(posicion).getTxtRutaCompleta());
				((TextView)v.findViewById(R.id.txtRutaCorto)).setTextColor(Color.WHITE);
				((TextView)v.findViewById(R.id.txtNoResultado)).setTextColor(Color.WHITE);
				((TextView)v.findViewById(R.id.txtTiempos)).setTextColor(Color.WHITE);
				
				validaEnviaResultados(posicion);
			}
		});
        
		TV_verRuta = (TextView) findViewById(R.id.textVerRuta);
		TV_verRuta.setOnClickListener(new OnClickListener(){
			public void onClick(View v) {
				BT_verRuta.performClick();
			} 
		});
		TV_verRuta.setOnTouchListener(new OnTouchListener(){
			public boolean onTouch(View arg0, MotionEvent arg1) {
				BT_verRuta.setPressed(true);
				return false;
			}
		});
		
		LL_verRuta = (LinearLayout) findViewById(R.id.linearVerRuta);
		LL_verRuta.setOnClickListener(new OnClickListener(){
			public void onClick(View v) {
				BT_verRuta.performClick();
			} 
		});
		
		BT_verRuta = (ImageButton) findViewById(R.id.botonVerRuta);
		BT_verRuta.setOnClickListener(new OnClickListener(){
			public void onClick(View v) {
				Intent myIntent = new Intent(v.getContext(), ActivityTrazaMapa.class);
				myIntent.putExtra("numEnlaces", rutas.get(resNumAnt).getRutas().size());
				myIntent.putExtra("Coordenadas0", coordenadas[0]); 
	    		myIntent.putExtra("Coordenadas1", coordenadas[1]);
	    		startActivity(myIntent);
			} 
		});
		
		TV_escucharRuta = (TextView) findViewById(R.id.textEscucharRuta);
		TV_escucharRuta.setOnClickListener(new OnClickListener(){
			public void onClick(View v) {
				BT_escucharRuta.performClick();
			} 
		});
		
		TV_escucharRuta.setOnTouchListener(new OnTouchListener(){
			public boolean onTouch(View arg0, MotionEvent arg1) {
				BT_escucharRuta.setPressed(true);
				return false;
			}
		});
		
		LL_escucharRuta = (LinearLayout) findViewById(R.id.linearEscucharRuta);
		LL_escucharRuta.setOnClickListener(new OnClickListener(){
			public void onClick(View v) {
				BT_escucharRuta.performClick();
			} 
		});
		
		BT_escucharRuta = (ImageButton) findViewById(R.id.botonEscucharRuta);
		BT_escucharRuta.setOnClickListener(new OnClickListener(){
			public void onClick(View v) {
				if(BT_escucharRuta.isSelected()){
					LL_escucharRuta.setSelected(false);
					mTts.stop();
				}else{
					LL_escucharRuta.setSelected(true);
					Intent checkIntent = new Intent();
			        checkIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
			        startActivityForResult(checkIntent, MY_DATA_CHECK_CODE);
				}
			} 
		});
		
		TV_compartir = (TextView) findViewById(R.id.textCompartir);
		TV_compartir.setOnClickListener(new OnClickListener(){
			public void onClick(View v) {
				BT_compartir.performClick();
			} 
		});
		
		TV_compartir.setOnTouchListener(new OnTouchListener(){
			public boolean onTouch(View arg0, MotionEvent arg1) {
				BT_compartir.setPressed(true);
				return false;
			}
		});
		
		LL_compartir = (LinearLayout) findViewById(R.id.linearCompartir);
		LL_compartir.setOnClickListener(new OnClickListener(){
			public void onClick(View v) {
				BT_compartir.performClick();
			} 
		});
		
		BT_compartir = (ImageButton) findViewById(R.id.botonCompartir);
		BT_compartir.setOnClickListener(new OnClickListener(){
			public void onClick(View v) {
				if (mTts.isSpeaking()){
					LL_escucharRuta.setSelected(false);
					mTts.stop();
				}
				
				Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
				shareIntent.setType("text/plain");
				StringBuffer shareText = new StringBuffer(datosLista.get(resNumAnt).getTxtRutaCompleta());
				shareText.append((char)10).append("http://www.mapps.com");
//				shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,"Metro DF - Ruta");
				shareIntent.putExtra(android.content.Intent.EXTRA_TEXT,shareText.toString());
				shareIntent.putExtra(android.content.Intent.EXTRA_TEXT,datosLista.get(resNumAnt).getTxtRutaCompleta());
				startActivity(Intent.createChooser(shareIntent,"Selecciona el medio para compartir"));
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
	
	public void onDestroy(){
		if (mTts != null) {
			mTts.stop();
			mTts.shutdown();
		}
		super.onDestroy();
		System.gc();
	}
	
	public void validaEnviaResultados(int resNum){
		if (resNum != this.resNumAnt){
			int numGrafo = 0;
			int numEnlaces = rutas.get(resNum).getRutas().size();
			coordenadas = new float[2][numEnlaces+1];
			numGrafo = (int) rutas.get(resNum).getRutas().get(0).getIdNodoInicial();
			coordenadas[0][0] = grafo[numGrafo].getPosX();
			coordenadas[1][0] = grafo[numGrafo].getPosY();	
			for (int i=0; i< numEnlaces;i++){
				numGrafo = (int) rutas.get(resNum).getRutas().get(i).getIdNodoFinal();
				coordenadas[0][i+1] = grafo[numGrafo].getPosX();
				coordenadas[1][i+1] = grafo[numGrafo].getPosY();
			}
			this.resNumAnt = resNum;
		}
	}
	
	public void iniciaGrafo(Long destino){
		for (int x=1; x<grafo.length;x++){
			grafo[x].setNoVisitado();
			grafo[x].ordenaEnlaces(grafo[destino.intValue()]);
		}
	}
	
	public void llenaGrafo(){
		DbAdapter baseDatos = new DbAdapter(this);
		baseDatos.open();
		Cursor Cur_nodos = baseDatos.fetchAllTable("tb_nodos",new String[] {"id_nodo","nombre","coordenadaX","coordenadaY"});
		grafo = new Nodo[Cur_nodos.getCount()+1];
		int numNodos = 0;
		while (Cur_nodos.moveToPosition(numNodos++)){
			long idNodoAct = Cur_nodos.getLong(0);
			grafo[(int)idNodoAct] = new Nodo(idNodoAct,Cur_nodos.getString(1),Cur_nodos.getFloat(2),Cur_nodos.getFloat(3));
			int numAdyacencia = 0;
			Cursor Cur_adyacencias = baseDatos.fetchAllTable("tb_adyacencias",new String[] {"id_nodo_ini","id_nodo_final","id_grupo","distancia", "tiempo"},"id_nodo_ini",idNodoAct);
			while (Cur_adyacencias.moveToPosition(numAdyacencia++)){
				grafo[(int)idNodoAct].creaEnlace(Cur_adyacencias.getLong(0), Cur_adyacencias.getLong(1), Cur_adyacencias.getLong(2), Cur_adyacencias.getLong(3), Cur_adyacencias.getLong(4));	
			}
			Cur_adyacencias.close();
		}
		Cur_nodos.close();
    	baseDatos.close();
	}
	
	public ArrayList<Ruta> calculaRuta(String origen, String destino){
    	DbAdapter baseDatos = new DbAdapter(this);
    	return calculaRuta(origen, destino, baseDatos);
    }
    
	public ArrayList<Ruta> calculaRuta(String origen, String destino, DbAdapter baseDatos){
    	baseDatos.open();
    	Cursor curNodoOrigen = baseDatos.fetchReg("tb_nodos",new String[] {"id_nodo"}, "nombre" ,origen);
    	Long origenID = curNodoOrigen.getLong(0);
    	curNodoOrigen.close();
    	Cursor curNodoDestino =baseDatos.fetchReg("tb_nodos",new String[] {"id_nodo"}, "nombre" ,destino); 
    	Long destinoID = curNodoDestino.getLong(0);
    	curNodoDestino.close();
        baseDatos.close();
		return calculaRuta(origenID, destinoID, baseDatos);
	}
	
	public ArrayList<Ruta> calculaRuta(Long origen, Long destino,DbAdapter baseDatos){
		boolean regresarNodo = false;
    	boolean finGrafo = false;
		//int contadorLoop1=0;
		int numNodo = origen.intValue();
		int numNodoSig;
		
		Ruta ruta = new Ruta(origen);
		ArrayList<Ruta> rutasMin = new ArrayList<Ruta>();
		ArrayList<Ruta> rutax = new ArrayList<Ruta>();
		Arco arcoActual;
		llenaGrafo();
    	this.iniciaGrafo(destino);
		baseDatos.open();
    	do{
    		grafo[numNodo].setVisitado();
    		//arcoActual = grafo[numNodo].sigEnlace();
    		arcoActual = grafo[numNodo].sigEnlace(grafo,destino);
    		if (arcoActual != null){
				numNodoSig = (int) arcoActual.getIdNodoFinal();
				if (grafo[numNodoSig].isVisitado()){
				}else{
		    		ruta.agregaEnlace(arcoActual);
					numNodo = numNodoSig;
					if (numNodoSig == destino.intValue()){
		    			rutax.add(ruta.clone());
		    			if (ruta.compareToMinimos()){
		    				int i=0;
		    				boolean agregado = false;
		    				while ((i<=rutasMin.size()) && (!agregado)){
		    					if (i == rutasMin.size()){
		    						rutasMin.add(ruta.clone());
		    						agregado = true;
		    					}else{
		    						if (rutasMin.get(i).compareTo(ruta) <= 0){
			    						rutasMin.add(i,ruta.clone());
			    						agregado = true;
			    					}	
		    					}
		    					i++;
		    				}
		    			}
		    			regresarNodo = true;
					}else{
						if (ruta.getNumEnlaces() >= (ruta.getMinNumEnlaces() + 3) ){
							regresarNodo = true;
						}
					}
				}
    		}else{
    			regresarNodo = true;
    		}
    		if (regresarNodo){
    			if (numNodo == origen){
    				finGrafo=true;
    			}else{
    				grafo[numNodo].setNoVisitado();
    				numNodo = (int) ruta.obtieneEliminaUltimoEnlace().getIdNodoInicial();
    			}	
    			regresarNodo = false;
    		}
	    	//contadorLoop1++;
	    	//if(contadorLoop1 >= 100000){
	    	//	finGrafo = true;
	    	//}
    	}while (!finGrafo);
		baseDatos.close();    	
   		return rutasMin;   
	}  
	
    public ArrayList<filaDeResultado> muestraResultados(ArrayList<Ruta> ruta){
    	int numRes = ruta.size();
    	if (numRes>= 5){
    		numRes = 5;
    	}
    	Ruta rutaArray[] = new Ruta[numRes];
    	for (int i=0;i<numRes;i++){
    		rutaArray[i] = ruta.get(i);	
    	}
    	return muestraResultados(rutaArray,numRes);
    }
    
    public ArrayList<filaDeResultado> muestraResultados(Ruta[] ruta, int numRutas){
    	ArrayList<filaDeResultado> lista = new ArrayList<filaDeResultado>();
    	DbAdapter baseDatos = new DbAdapter(this);
		baseDatos.open();
		boolean seleccionado = true;
		for (int N=0; N < numRutas; N++){
			String[] rutaString = new String[2];
			rutaString= muestraResultados(ruta[N],baseDatos);
			
//			if(rutaString[2].length()>100){
//				lista.add(new filaDeResultado(N+1, rutaString[0], rutaString[1].substring(0, 100)+"...", rutaString[1], seleccionado));				
//			}else{
				lista.add(new filaDeResultado(N+1, rutaString[0], rutaString[2], rutaString[1], seleccionado));
//			}
			seleccionado = false;
    	}
		baseDatos.close();
		return lista;
    }
    
    public String[] muestraResultados(Ruta ruta, DbAdapter baseDatos){
    	String [] rutaString = new String[3];
    	StringBuilder mensajeFinStr = new StringBuilder();
    	Cursor estacionIni = null;
    	Cursor estacionFin = null;
		int numEnlaces = (int) ruta.getNumEnlaces();
		
		mensajeFinStr.append(ruta.getTiempo()/ 60).append(" Min.").append((char)(10))
					 .append(ruta.getNumEnlaces()).append(" Estaciones").append((char)(10))
					 .append(ruta.getCambiosDeGrupo()).append(" Transbordos");
		rutaString[0] = mensajeFinStr.toString();

		estacionIni = baseDatos.fetchReg("tb_nodos",new String[] {"nombre"}, "id_nodo", ruta.getRutas().get(0).getIdNodoInicial());
		mensajeFinStr = new StringBuilder();
		mensajeFinStr.append(estacionIni.getString(0));
		estacionIni.close();
		for (int M=0; M < numEnlaces ;M++){
			ArrayList<Arco> mnsjRuta =ruta.getRutas();
			Arco mensaje = mnsjRuta.get(M);
    		if (mensaje != null){
    			estacionFin = baseDatos.fetchReg("tb_nodos",new String[] {"nombre"}, "id_nodo", mensaje.getIdNodoFinal());
        		mensajeFinStr.append("-").append(estacionFin.getString(0));
        		estacionFin.close();
    		}
		}
		rutaString[2] = mensajeFinStr.toString();
		
		rutaString[1] = muestraIndicaciones(ruta,baseDatos);
		ruta=null;
		baseDatos=null;
		mensajeFinStr=null;
		
		return rutaString; 
    }
    
    public String muestraIndicaciones(Ruta ruta, DbAdapter baseDatos){
    	StringBuilder mensajeFinStr = new StringBuilder();
    	Cursor estacionIni = null;
    	Cursor estacionFin = null;
		int numEnlaces = (int) ruta.getNumEnlaces();

		estacionIni = baseDatos.fetchReg("tb_nodos",new String[] {"nombre"}, "id_nodo", ruta.getRutas().get(0).getIdNodoInicial());
		Long grupo = ruta.getRutas().get(0).getGrupo();
		Cursor curGrupo = baseDatos.fetchReg("tb_grupos",new String[] {"descripcion"}, "id_grupo", grupo);
		mensajeFinStr.append(" Tome la estación ").append(estacionIni.getString(0)).append(" (").append(curGrupo.getString(0)).append(") ");
		estacionIni.close();
		curGrupo.close();
		for (int M=0; M < numEnlaces ;M++){
			ArrayList<Arco> mnsjRuta =ruta.getRutas();
			Arco mensaje = mnsjRuta.get(M);
    		if (mensaje != null){
    			if (grupo != mensaje.getGrupo()){
        			grupo = mensaje.getGrupo();
    				curGrupo = baseDatos.fetchReg("tb_grupos",new String[] {"descripcion","nombre"}, "id_grupo", grupo);
    				estacionIni = baseDatos.fetchReg("tb_nodos",new String[] {"nombre"}, "id_nodo", mensaje.getIdNodoInicial());
            		mensajeFinStr.append(" y continúe hasta llegar a la estación ").append(estacionIni.getString(0));
            		mensajeFinStr.append(". ").append((char)10).append("En ").append(estacionIni.getString(0)).append(", transborde a la linea ");
            		mensajeFinStr.append(curGrupo.getString(1)).append(" (").append(curGrupo.getString(0)).append(") ");
            		estacionIni.close();
        			curGrupo.close();
    			}
    			if (M == numEnlaces - 1){
        			estacionFin = baseDatos.fetchReg("tb_nodos",new String[] {"nombre"}, "id_nodo", mensaje.getIdNodoFinal());
            		mensajeFinStr.append(", hasta llegar a la estación destino ").append(estacionFin.getString(0)).append(".");
            		estacionFin.close();
    			}
    		}
		}
		return mensajeFinStr.toString();
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
    
	public void onInit(int status) {
		if (status == TextToSpeech.SUCCESS){}{
			Locale loc = new Locale("es", "","");
			if(mTts.isLanguageAvailable(loc) >= TextToSpeech.LANG_AVAILABLE){
				mTts.setLanguage(loc);
			}else{
				mTts.setLanguage(Locale.ITALY);
			}
				
		}
	} 
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == MY_DATA_CHECK_CODE) {
			if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {
				mTts.speak("Buen día", TextToSpeech.QUEUE_FLUSH, null);
				mTts.speak(datosLista.get(resNumAnt).getTxtRutaCompleta(), TextToSpeech.QUEUE_ADD, null);
			} else {
				mensajeError("Error","Lo lamento, su dispositivo no cuenta con el módulo de lectura activado. Instale el módulo e intentelo de nuevo. Gracias.");
				Intent installIntent = new Intent();            
				installIntent.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
				startActivity(installIntent);
			}    
		}
	}
}