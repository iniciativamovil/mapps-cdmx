package appMetro.origDestino;

public class NodoInfo {

	private int nodoId;
	private String nombre;
	private String descripcion;
	private String imagen;
	private String mapa;
	private String[] comercios;
	private int entradas;
	private int ventanillas;
	
	private boolean interseccion;
	private boolean activo;
	
	public NodoInfo(String nombre){
		this.nombre = nombre;
	}

	public int getNodoId() {
		return nodoId;
	}

	public void setNodoId(int nodoId) {
		this.nodoId = nodoId;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getImagen() {
		return imagen;
	}

	public void setImagen(String imagen) {
		this.imagen = imagen;
	}

	public String getMapa() {
		return mapa;
	}

	public void setMapa(String mapa) {
		this.mapa = mapa;
	}

	public String[] getComercios() {
		return comercios;
	}

	public void setComercios(String[] comercios) {
		this.comercios = comercios;
	}

	public int getEntradas() {
		return entradas;
	}

	public void setEntradas(int entradas) {
		this.entradas = entradas;
	}

	public int getVentanillas() {
		return ventanillas;
	}

	public void setVentanillas(int ventanillas) {
		this.ventanillas = ventanillas;
	}

	public boolean isInterseccion() {
		return interseccion;
	}

	public void setInterseccion(boolean interseccion) {
		this.interseccion = interseccion;
	}

	public boolean isActivo() {
		return activo;
	}

	public void setActivo(boolean activo) {
		this.activo = activo;
	}
	
	
}

