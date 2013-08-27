package appMetro.origDestino;

import java.util.ArrayList;

public class Nodo {
	private long idNodo;
	private String Nodo;
	private float posX;
	private float posY;
	private ArrayList<Arco> enlaces;
	private int numEnlaces;
	private boolean visitado;
	
	public Nodo(long idNodo, String Nodo,float posX, float posY ){
		this.setIdNodo(idNodo);
		this.setNodo(Nodo);
		this.setPosX(posX);
		this.setPosY(posY);
		this.numEnlaces = 0;
		this.enlaces = new ArrayList<Arco>();
		this.setNoVisitado();
	}

	public long getIdNodo() {
		return idNodo;
	}

	public void setIdNodo(long idNodo) {
		this.idNodo = idNodo;
	}

	public String getNodo() {
		return Nodo;
	}

	public void setNodo(String nodo) {
		Nodo = nodo;
	}

	public float getPosX() {
		return posX;
	}

	public void setPosX(float posX) {
		this.posX = posX;
	}

	public float getPosY() {
		return posY;
	}

	public void setPosY(float posY) {
		this.posY = posY;
	}
	
	public int getNumEnlaces(){
		return this.numEnlaces;
	}
	
	public ArrayList<Arco> getEnlaces() {
		return enlaces;
	}
	
	public Arco getEnlace(int numEnlace) {
		Arco enlaceRC = null;
		if (numEnlace < this.numEnlaces){
			enlaceRC = enlaces.get(numEnlace);	
			enlaces.get(numEnlace).setVisitado();
		}
		return enlaceRC;
	}
	
	public Arco sigEnlace() {
		Arco enlaceRC = null;
		int contador = 0;
		while (contador < this.numEnlaces && enlaceRC == null){
			if (!enlaces.get(contador).isVisitado()){
				enlaceRC = enlaces.get(contador);	
				enlaces.get(contador).setVisitado();
			}
			contador++;
		}
		return enlaceRC;
	}
	
	public Arco sigEnlace(Nodo grafo[],long nodoFinal) {
		Arco enlaceRC = null;
		int contador = 0;
		int enlaceMenor = -1;
		float distanciaMin=999999999;
		float cxNodoFin = grafo[(int)nodoFinal].getPosX();
		float cyNodoFin = grafo[(int)nodoFinal].getPosY();
		
		
		while (contador < this.numEnlaces){
			if (!enlaces.get(contador).isVisitado()){
				float cxNodoIni = grafo[(int) enlaces.get(contador).getIdNodoFinal()].getPosX();
				float cyNodoIni = grafo[(int) enlaces.get(contador).getIdNodoFinal()].getPosY();
				float distancia = (float) Math.sqrt(Math.pow((cxNodoFin - cxNodoIni),2) +  Math.pow((cyNodoFin - cyNodoIni),2));
				if (distancia < distanciaMin){
					enlaceMenor = contador;
					distanciaMin = distancia;
				}
			}
			contador++;
		}
		
		if (enlaceMenor >=0){
			enlaceRC = enlaces.get(enlaceMenor);
			enlaces.get(enlaceMenor).setVisitado();
		}
		return enlaceRC;
	}
	
	public boolean hayEnlaces() {
		boolean faltaVisitar=false;
		int contador = -1;
		while (++contador < this.numEnlaces && !faltaVisitar){
			if (!enlaces.get(contador).isVisitado()){
				faltaVisitar = true;				
			}
		}
		return faltaVisitar;
	}
	
	public void iniciaEnlaces() {
		int contador = 0;
		while (contador < this.numEnlaces){
			enlaces.get(contador++).setNoVisitado();				
		}
	}

	public void creaEnlace(Arco enlace) {
		this.enlaces.add(enlace);
		this.numEnlaces++;
	}
	
	public void creaEnlace(long idNodoInicial, long idNodoFinal, long grupo, long distancia, long tiempo) {
		this.creaEnlace(new Arco(idNodoInicial,idNodoFinal,grupo,distancia,tiempo));
	}
	
	public void borraEnlace(int enlace){
		this.enlaces.remove(enlace);
	}
	
	public void borraEnlace(Arco enlace){
		this.borraEnlace(enlaces.indexOf(enlace));
	}
	
	public boolean isVisitado() {
		return visitado;
	}

	public void setVisitado() {
		this.visitado = true;
	}
	
	public void setNoVisitado() {
		this.iniciaEnlaces();
		this.visitado = false;
	}
	
	public void ordenaEnlaces(Nodo destino) {
	}
}
