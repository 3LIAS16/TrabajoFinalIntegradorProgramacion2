package service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import dao.DispositivoIoTDAO;
import model.DispositivoIoTVO;

public class DispositivoIoTService implements GenericService<DispositivoIoTVO> {

	private DispositivoIoTDAO dispositivoIoTDAO;
	private ConfiguracionRedService configuracionRedService;

	public DispositivoIoTService(DispositivoIoTDAO dispositivoIoTDAO, ConfiguracionRedService configuracionRedService) {
		if (dispositivoIoTDAO == null) {
			throw new IllegalArgumentException("dispositivoIoTDAO no puede ser null");
		}
		if (configuracionRedService == null) {
			throw new IllegalArgumentException("configuracionRedService no puede ser null");
		}

		this.dispositivoIoTDAO = dispositivoIoTDAO;
		this.configuracionRedService = configuracionRedService;
	}

	private void validateDispositivo(DispositivoIoTVO dispo) {
        if (dispo == null) {
            throw new IllegalArgumentException("La configuracion no puede ser null");
        }
        if (dispo.getSerial() == null || dispo.getSerial().trim().isEmpty()) {
            throw new IllegalArgumentException("La Ip no se puede dejar vacia");
        }
        if (dispo.getModelo() == null || dispo.getModelo().trim().isEmpty()) {
            throw new IllegalArgumentException("La mascara no puede estar vacia");
        }
        if (dispo.getUbicacion() == null || dispo.getUbicacion().trim().isEmpty()) {
            throw new IllegalArgumentException("El gateway no puede estar vacio");
        }
        if (dispo.getFirmwareVersion() == null || dispo.getFirmwareVersion().trim().isEmpty()) {
            throw new IllegalArgumentException("el DNSPrimario no se puede dejar vacio");
        }
    }
	
	private void serialUnique(String serial, Integer id) throws SQLException {
		DispositivoIoTVO dispositivo = dispositivoIoTDAO.buscarPorSerial(serial);
		if(dispositivo!=null) {
			if(id==null || dispositivo.getId()!=id) {
				throw new IllegalArgumentException("Ya existe un dispositivo con el numero de serie: " + serial);
			}
		}
	}

	@Override
	public void insertar(DispositivoIoTVO dispositivo) throws Exception {
		validateDispositivo(dispositivo);
		serialUnique(dispositivo.getSerial(), null);
		
        //coordinacion con ConfiguracionRedService (transaccional)
        if (dispositivo.getConfiguracionRed() != null) {
            if (dispositivo.getConfiguracionRed().getId() == 0) {
                // Config nueva: insertar primero para obtener ID autogenerado
                configuracionRedService.insertar(dispositivo.getConfiguracionRed());
            } else {
                // Config existente: actualizar datos
            	configuracionRedService.actualizar(dispositivo.getConfiguracionRed());
            }
       
	}
        dispositivoIoTDAO.insertar(dispositivo);
	}

	@Override
	public void actualizar(DispositivoIoTVO dispositivo) throws Exception {
		 validateDispositivo(dispositivo);
	        if (dispositivo.getId() <= 0) {
	            throw new IllegalArgumentException("El ID del dispositivo debe ser mayor a 0 para actualizar");
	        }
	        serialUnique(dispositivo.getSerial(), dispositivo.getId());
	        dispositivoIoTDAO.actualizar(dispositivo);
	}

	@Override
	public void eliminar(int id) throws Exception {
		 if (id <= 0) {
	            throw new IllegalArgumentException("El ID debe ser mayor a 0");
	        }
	        dispositivoIoTDAO.eliminar(id);
	}

	@Override
	public DispositivoIoTVO getById(int id) throws Exception {
		 if (id <= 0) {
	            throw new IllegalArgumentException("El ID debe ser mayor a 0");
	        }
		return dispositivoIoTDAO.getById(id);
	}
	
	public DispositivoIoTVO getBySerial(String serial) throws Exception {
		 if (serial==null) {
	            throw new IllegalArgumentException("El numero de serie no puede ser nulo");
	        }
		return dispositivoIoTDAO.buscarPorSerial(serial);
	}

	@Override
	public List<DispositivoIoTVO> getAll() throws Exception {
		return dispositivoIoTDAO.getAll();
	}

	public ConfiguracionRedService getConfiguracionRedService() {
		return configuracionRedService;
	}
	
	public void eliminarConfiguracionDeDispositivo(int configId, int dispoId) throws Exception {
		if (configId <= 0 || dispoId <= 0) {
            throw new IllegalArgumentException("Los IDs deben ser mayores a 0");
        }

        DispositivoIoTVO dispo = dispositivoIoTDAO.getById(dispoId);
        if (dispo == null) {
            throw new IllegalArgumentException("Dispositivo no encontrado con ID: " + dispoId);
        }

        if (dispo.getConfiguracionRed() == null || dispo.getConfiguracionRed().getId() != configId) {
            throw new IllegalArgumentException("La configuracion no pertenece a este dispositivo");
        }

        dispo.setConfiguracionRed(null);
        dispositivoIoTDAO.actualizar(dispo);
        configuracionRedService.eliminar(configId);
	}

	@Override
	public void insertTx(DispositivoIoTVO dispositivo, Connection conn) throws Exception {
		validateDispositivo(dispositivo);
		serialUnique(dispositivo.getSerial(), null);
        dispositivoIoTDAO.insertTx(dispositivo, conn);
	}
	
	
	
}
