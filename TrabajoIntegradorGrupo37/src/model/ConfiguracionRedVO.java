package model;

public class ConfiguracionRedVO extends BaseVO{
	private boolean dhcpHabilitado ;
	private String ip, mascara, gateway, dnsPrimario;
	
	
	public ConfiguracionRedVO(int id,boolean dhcpHabilitado, String ip, String mascara,
			String gateway, String dnsPrimario) {
		super(id, false);
		this.dhcpHabilitado = dhcpHabilitado;
		this.ip = ip;
		this.mascara = mascara;
		this.gateway = gateway;
		this.dnsPrimario = dnsPrimario;
		
	}
	
	public ConfiguracionRedVO () {
		
	}

	public boolean isDhcpHabilitado() {
		return dhcpHabilitado;
	}


	public void setDhcpHabilitado(boolean dhcpHabilitado) {
		this.dhcpHabilitado = dhcpHabilitado;
	}


	public String getIp() {
		return ip;
	}


	public void setIp(String ip) {
		this.ip = ip;
	}


	public String getMascara() {
		return mascara;
	}


	public void setMascara(String mascara) {
		this.mascara = mascara;
	}


	public String getGateway() {
		return gateway;
	}


	public void setGateway(String gateway) {
		this.gateway = gateway;
	}


	public String getDnsPrimario() {
		return dnsPrimario;
	}


	public void setDnsPrimario(String dnsPrimario) {
		this.dnsPrimario = dnsPrimario;
	}

	@Override
	public String toString() {
		return "ConfiguracionRedVO "+id+" [dhcpHabilitado=" + dhcpHabilitado + ", ip=" + ip + ", mascara=" + mascara
				+ ", gateway=" + gateway + ", dnsPrimario=" + dnsPrimario + "]";
	}

	
	
	
	
}
