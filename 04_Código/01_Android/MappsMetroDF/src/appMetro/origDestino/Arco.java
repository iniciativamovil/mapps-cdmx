package appMetro.origDestino;

public class Arco {
	private long idNodoInicial;
	private long idNodoFinal;
	private long grupo;
	private long distancia;
	private long tiempo;
	private boolean visitado;
	
	public Arco(long idNodoInicial, long idNodoFinal, long grupo, long distancia, long tiempo){
		this.setIdNodoFinal(idNodoFinal);
		this.setIdNodoInicial(idNodoInicial);
		this.setGrupo(grupo);
		this.setDistancia(distancia);
		this.setTiempo(tiempo);
		this.setNoVisitado();
	}

	public long getIdNodoInicial() {
		return idNodoInicial;
	}

	public void setIdNodoInicial(long idNodoInicial) {
		this.idNodoInicial = idNodoInicial;
	}

	public long getIdNodoFinal() {
		return idNodoFinal;
	}

	public void setIdNodoFinal(long idNodoFinal) {
		this.idNodoFinal = idNodoFinal;
	}

	public long getGrupo() {
		return grupo;
	}

	public void setGrupo(long grupo) {
		this.grupo = grupo;
	}

	public long getDistancia() {
		return distancia;
	}

	public void setDistancia(long distancia) {
		this.distancia = distancia;
	}

	public long getTiempo() {
		return tiempo;
	}

	public void setTiempo(long tiempo) {
		this.tiempo = tiempo;
	}
	
	public boolean isVisitado(){
		return this.visitado;
	}
	
	public void setVisitado(){
		this.visitado = true;
	}
	
	public void setNoVisitado(){
		this.visitado = false;
	}
}
