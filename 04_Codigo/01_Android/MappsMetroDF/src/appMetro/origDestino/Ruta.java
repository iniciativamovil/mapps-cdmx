package appMetro.origDestino;

import java.util.ArrayList;

public class Ruta implements Cloneable {
	private long idNodoInicial;
	private long idNodoFinal;
	private long distancia;
	private long tiempo;
	private long cambiosDeGrupo;
	private ArrayList<Arco> rutas;
	private long numEnlaces;
	
	private long minDistancia;
	private long minTiempo;
	private long minCambiosDeGrupo;
	private long minNumEnlaces;
	
	public Ruta(long idNodoInicial){
		this.setIdNodoInicial(idNodoInicial);
		this.setIdNodoFinal(0);
		this.setCambiosDeGrupo(0);
		this.setNumEnlaces(0);
		this.setDistancia(0);
		this.setTiempo(0);
		this.rutas = new ArrayList<Arco>();

		this.setMinDistancia(999999);
		this.setMinTiempo(999999);
		this.setMinCambiosDeGrupo(999999);
		this.setMinNumEnlaces(999999);
	}

	public long getIdNodoInicial() {
		return this.idNodoInicial;
	}

	public void setIdNodoInicial(long idNodoInicial) {
		this.idNodoInicial = idNodoInicial;
	}

	public long getIdNodoFinal() {
		return this.idNodoFinal;
	}

	public void setIdNodoFinal(long idNodoFinal) {
		this.idNodoFinal = idNodoFinal;
	}

	public long getDistancia() {
		return this.distancia;
	}

	public void setDistancia(long distancia) {
		this.distancia = distancia;
	}

	public long getTiempo() {
		return this.tiempo;
	}

	public void setTiempo(long tiempo) {
		this.tiempo = tiempo;
	}

	public long getCambiosDeGrupo() {
		return this.cambiosDeGrupo;
	}

	public void setCambiosDeGrupo(long cambiosDeGrupo) {
		this.cambiosDeGrupo = cambiosDeGrupo;
	}

	public ArrayList<Arco> getRutas() {
		return this.rutas;
	}

	public void setRutas(ArrayList<Arco> rutas) {
		this.rutas = rutas;
	}

	public long getNumEnlaces() {
		return this.numEnlaces;
	}

	public void setNumEnlaces(long numEnlaces) {
		this.numEnlaces = numEnlaces;
	}
	
	public long getMinDistancia() {
		return this.minDistancia;
	}

	public void setMinDistancia(long minDistancia) {
		this.minDistancia = minDistancia;
	}
	
	public boolean esMinDistancia(long minDistancia) {
		boolean respuesta = false;
		if (minDistancia <= this.getMinDistancia()){
			this.setMinDistancia(minDistancia);
			respuesta = true;
		}
		return respuesta;
	}

	public long getMinTiempo() {
		return this.minTiempo;
	}

	public void setMinTiempo(long minTiempo) {
		this.minTiempo = minTiempo;
	}

	public boolean esMinTiempo(long minTiempo) {
		boolean respuesta = false;
		if (minTiempo <= this.getMinTiempo()){
			this.setMinTiempo(minTiempo);
			respuesta = true;
		}
		return respuesta;
	}
	
	public long getMinCambiosDeGrupo() {
		return this.minCambiosDeGrupo;
	}

	public void setMinCambiosDeGrupo(long minCambiosDeGrupo) {
		this.minCambiosDeGrupo = minCambiosDeGrupo;
	}
	
	public boolean esMinCambiosDeGrupo(long minCambiosDeGrupo) {
		boolean respuesta = false;
		if (minCambiosDeGrupo <= this.getMinCambiosDeGrupo()){
			this.setMinCambiosDeGrupo(minCambiosDeGrupo);
			respuesta=true;
		}
		return respuesta;
	}

	public long getMinNumEnlaces() {
		return this.minNumEnlaces;
	}

	public void setMinNumEnlaces(long minNumEnlaces) {
		this.minNumEnlaces = minNumEnlaces;
	}
	
	public boolean esMinNumEnlaces(long minNumEnlaces) {
		boolean respuesta = false;
		if (minNumEnlaces <= this.getMinNumEnlaces()){
			this.setMinNumEnlaces(minNumEnlaces);	
			respuesta=true;
		}
		return respuesta;
	}

	public void inicializaMinimos(){
		this.setMinDistancia(999999);
		this.setMinTiempo(999999);
		this.setMinCambiosDeGrupo(999999);
		this.setMinNumEnlaces(999999);
	}
	
	public void agregaEnlace(Arco enlace){
		this.distancia+=enlace.getDistancia();
		this.tiempo+= enlace.getTiempo();
		int ultimoEnlace = (int)this.numEnlaces - 1;
		if (ultimoEnlace >= 0){
			if (enlace.getGrupo() != this.rutas.get(ultimoEnlace).getGrupo()){
				this.cambiosDeGrupo++;
			}	
		}
		this.setIdNodoFinal(enlace.getIdNodoFinal());
		this.rutas.add(enlace);
		this.numEnlaces++;
	}
	
	public void eliminaUltimoEnlace(){
		int penultimoEnlace = (int)this.numEnlaces - 2;
		int ultimoEnlace = (int)this.numEnlaces - 1;
		this.distancia-=this.rutas.get(ultimoEnlace).getDistancia();
		this.tiempo-= this.rutas.get(ultimoEnlace).getTiempo();
		if (this.rutas.get(penultimoEnlace).getGrupo() != this.rutas.get(ultimoEnlace).getGrupo()){
			this.cambiosDeGrupo--;
		}
		this.setIdNodoFinal(this.rutas.get(penultimoEnlace).getIdNodoFinal());
		this.rutas.remove(ultimoEnlace);
		this.numEnlaces--;
	}
	
	public Arco obtieneEliminaUltimoEnlace(){
		if (numEnlaces >0){
			int penultimoEnlace = (int)numEnlaces - 2;
			int ultimoEnlace = (int)numEnlaces - 1;
			this.distancia-=this.rutas.get(ultimoEnlace).getDistancia();
			this.tiempo-= this.rutas.get(ultimoEnlace).getTiempo();
			if (ultimoEnlace > 0){
				if (this.rutas.get(penultimoEnlace).getGrupo() != this.rutas.get(ultimoEnlace).getGrupo()){
					this.cambiosDeGrupo--;
				}
			}
			this.setIdNodoFinal(this.rutas.get(ultimoEnlace).getIdNodoInicial());
			this.numEnlaces--;
			return this.rutas.remove(ultimoEnlace);
		}else{
			return null;
		}
	}
	
	@SuppressWarnings("unchecked")
	public Ruta clone() {
		Ruta Obj = new Ruta(this.idNodoInicial);
		Obj.setCambiosDeGrupo(this.cambiosDeGrupo);
		Obj.setDistancia(this.distancia);
		Obj.setIdNodoFinal(this.idNodoFinal);
		Obj.setNumEnlaces(this.numEnlaces);
		Obj.setTiempo(this.tiempo);
		Obj.setRutas((ArrayList<Arco>) this.rutas.clone());
		return Obj;
	}
	
	public int compareToTiempo(Ruta rutaCompare){
		long returnCode = rutaCompare.getTiempo() - this.tiempo;
		return (int) returnCode;
	}
	public int compareToCambiosDeGrupo(Ruta rutaCompare){
		long returnCode = rutaCompare.getCambiosDeGrupo() - this.cambiosDeGrupo;
		return (int) returnCode;
	}
	public int compareToDistancia(Ruta rutaCompare){
		long returnCode = rutaCompare.getDistancia() - this.distancia;
		return (int) returnCode;
	}
	public int compareToNumEnlaces(Ruta rutaCompare){
		long returnCode = rutaCompare.getNumEnlaces() - this.numEnlaces;
		return (int) returnCode;
	}
	
	public int compareTo(Ruta rutaCompare){
		int returnCode = this.compareToTiempo(rutaCompare);
		if (0 == returnCode){
			returnCode = this.compareToCambiosDeGrupo(rutaCompare);
			if (0 == returnCode){
				returnCode = this.compareToDistancia(rutaCompare);
				if (0 == returnCode){
					returnCode = this.compareToNumEnlaces(rutaCompare);
				}
			}
		}
		return returnCode;
	}
	
	public boolean compareToMinimos(){
		boolean minimo = false;
		if ((this.esMinNumEnlaces(this.getNumEnlaces())) ||
			(this.esMinDistancia(this.getDistancia())) ||
			(this.esMinCambiosDeGrupo(this.getCambiosDeGrupo())) ||
			(this.esMinTiempo(this.getTiempo()))){
			minimo = true;
		}
		return minimo;
	}
}
