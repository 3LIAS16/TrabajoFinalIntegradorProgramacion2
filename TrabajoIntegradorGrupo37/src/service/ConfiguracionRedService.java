package service;

import java.sql.Connection;
import java.util.List;

import dao.ConfiguracionRedDAO;
import dao.GenericDAO;
import model.ConfiguracionRedVO;

public class ConfiguracionRedService implements GenericService<ConfiguracionRedVO>{
	
	private GenericDAO<ConfiguracionRedVO> configuracionRedDAO;
	
	public ConfiguracionRedService(GenericDAO<ConfiguracionRedVO> configuracionRedDAO) {
        if (configuracionRedDAO == null) {
            throw new IllegalArgumentException("ConfiguracionRedDAO no puede ser null");
        }
        this.configuracionRedDAO = configuracionRedDAO;
	}
	
	@Override
	public void insertar(ConfiguracionRedVO config) throws Exception {
		validateConfiguracion(config);
		configuracionRedDAO.insertar(config);
	}

	@Override
	public void actualizar(ConfiguracionRedVO config) throws Exception {
		validateConfiguracion(config);
		if(config.getId()<=0) {
			  throw new IllegalArgumentException("El ID de la configuracion debe ser mayor a 0 para actualizar");
		}
		configuracionRedDAO.actualizar(config);
	}

	@Override
	public void eliminar(int id) throws Exception {
		if(id<=0) {
			  throw new IllegalArgumentException("El ID de la configuracion debe ser mayor a 0");
		}	
		configuracionRedDAO.eliminar(id);
	}

	@Override
	public ConfiguracionRedVO getById(int id) throws Exception {
		if(id<=0) {
			  throw new IllegalArgumentException("El ID de la configuracion debe ser mayor a 0");
		}
		return configuracionRedDAO.getById(id);
	}

	private void validateConfiguracion(ConfiguracionRedVO config) {

        if (config == null) {
            throw new IllegalArgumentException("La configuracion no puede ser null");
        }
        if (config.getIp() == null || config.getIp().trim().isEmpty()) {
            throw new IllegalArgumentException("La Ip no se puede dejar vacia");
        }
        if (config.getMascara() == null || config.getMascara().trim().isEmpty()) {
            throw new IllegalArgumentException("La mascara no puede estar vacia");
        }
        if (config.getGateway() == null || config.getGateway().trim().isEmpty()) {
            throw new IllegalArgumentException("El gateway no puede estar vacio");
        }
        if (config.getDnsPrimario() == null || config.getDnsPrimario().trim().isEmpty()) {
            throw new IllegalArgumentException("el DNSPrimario no se puede dejar vacio");
        }
    }
	
	
	  @Override
	public List<ConfiguracionRedVO> getAll() throws Exception {
	        return configuracionRedDAO.getAll();
	    }

	@Override
	public void insertTx(ConfiguracionRedVO config, Connection conn) throws Exception {
		validateConfiguracion(config);
		configuracionRedDAO.insertTx(config, conn);
	}
	
}
