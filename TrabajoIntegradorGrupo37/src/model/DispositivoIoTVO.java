package model;

public class DispositivoIoTVO extends BaseVO {
	
	private String serial,modelo,ubicacion,firmwareVersion;
	private ConfiguracionRedVO configuracionRed;
	
	
	
	public DispositivoIoTVO(int id, String serial, String modelo, String ubicacion,
			String firmwareVersion) {
		super(id, false);
		this.serial = serial;
		this.modelo = modelo;
		this.ubicacion = ubicacion;
		this.firmwareVersion = firmwareVersion;
	}

public DispositivoIoTVO() {
	
}







	public String getSerial() {
		return serial;
	}



	public void setSerial(String serial) {
		this.serial = serial;
	}



	public String getModelo() {
		return modelo;
	}



	public void setModelo(String modelo) {
		this.modelo = modelo;
	}



	public String getUbicacion() {
		return ubicacion;
	}



	public void setUbicacion(String ubicacion) {
		this.ubicacion = ubicacion;
	}



	public String getFirmwareVersion() {
		return firmwareVersion;
	}



	public void setFirmwareVersion(String firmwareVersion) {
		this.firmwareVersion = firmwareVersion;
	}



	public ConfiguracionRedVO getConfiguracionRed() {
		return configuracionRed;
	}



	public void setConfiguracionRed(ConfiguracionRedVO configuracionRed) {
		this.configuracionRed = configuracionRed;
	}

	@Override
	public String toString() {
		return "DispositivoIoTVO "+id+" [serial=" + serial + ", modelo=" + modelo + ", ubicacion=" + ubicacion
				+ ", firmwareVersion=" + firmwareVersion + ", configuracionRed=" + configuracionRed + "]";
	}









	
	
	
	
}
