package appMetro.origDestino;

public class filaDeResultado {
	private int noRuta;
	private String txtTiempos;
	private String txtRuta;
	private String txtRutaCompleta;
	private boolean seleccionado;
	
	public filaDeResultado(){
		super();
	}
	
	public filaDeResultado(int noRuta, String txtTiempos, String txtRuta, String txtRutaCompleta, boolean seleccionado){
		this.setNoRuta(noRuta);
		this.setTxtTiempos(txtTiempos);
		this.setTxtRuta(txtRuta);
		this.setTxtRutaCompleta(txtRutaCompleta);
		this.setSeleccionado(seleccionado);
	}

	public int getNoRuta() {
		return noRuta;
	}

	private void setNoRuta(int noRuta) {
		this.noRuta = noRuta;
	}

	public String getTxtTiempos() {
		return txtTiempos;
	}

	private void setTxtTiempos(String txtTiempos) {
		this.txtTiempos = txtTiempos;
	}

	public String getTxtRuta() {
		return txtRuta;
	}

	private void setTxtRuta(String txtRuta) {
		this.txtRuta = txtRuta;
	}

	public boolean isSeleccionado() {
		return seleccionado;
	}

	public void setSeleccionado(boolean seleccionado) {
		this.seleccionado = seleccionado;
	}

	public String getTxtRutaCompleta() {
		return txtRutaCompleta;
	}

	public void setTxtRutaCompleta(String txtRutaCompleta) {
		this.txtRutaCompleta = txtRutaCompleta;
	}
}