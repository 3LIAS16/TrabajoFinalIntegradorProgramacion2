package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import config.DatabaseConnection;
import model.ConfiguracionRedVO;
import model.DispositivoIoTVO;

public class DispositivoIoTDAO implements GenericDAO<DispositivoIoTVO>{
	
	private static final String INSERT_SQL = "INSERT INTO DispositivoIoT (serial, modelo, ubicacion, firmwareVersion, idConfiguracion) VALUES (?, ?, ?, ?, ?)";

	private static final String UPDATE_SQL = "UPDATE DispositivoIoT SET serial = ?, modelo = ?, ubicacion = ?, firmwareVersion = ?, idConfiguracion = ? WHERE idDispositivoIoT = ?";

	private static final String DELETE_SQL = "UPDATE DispositivoIoT SET eliminado = TRUE WHERE idDispositivoIoT = ?";

	private static final String SELECT_BY_ID_SQL = "SELECT d.idDispositivoIoT, d.serial, d.modelo, d.ubicacion, d.firmwareVersion, d.idConfiguracion, c.idConfiguracionRed, c.ip, c.mascara, c.gateway, c.dnsPrimario, c.dhcpHabilitado "
			+ "FROM DispositivoIoT d LEFT JOIN ConfiguracionRed c ON d.idConfiguracion = c.idConfiguracionRed "
			+ "WHERE d.idDispositivoIoT = ? AND d.eliminado = FALSE";

	private static final String SELECT_ALL_SQL = "SELECT d.idDispositivoIoT, d.serial, d.modelo, d.ubicacion, d.firmwareVersion, d.idConfiguracion, c.idConfiguracionRed, c.ip, c.mascara, c.gateway, c.dnsPrimario, c.dhcpHabilitado "
			+ "FROM DispositivoIoT d LEFT JOIN ConfiguracionRed c ON d.idConfiguracion = c.idConfiguracionRed "
			+ "WHERE d.eliminado = FALSE";
	
	   private static final String SEARCH_BY_SERIAL_SQL = "SELECT d.idDispositivoIoT, d.serial, d.modelo, d.ubicacion, d.firmwareVersion, d.idConfiguracion, c.idConfiguracionRed, c.ip, c.mascara, c.gateway, c.dnsPrimario, c.dhcpHabilitado "
				+ "FROM DispositivoIoT d LEFT JOIN ConfiguracionRed c ON d.idConfiguracion = c.idConfiguracionRed "
				+ "WHERE d.eliminado = FALSE AND d.serial = ?";
	  
	private final ConfiguracionRedDAO configDAO;
	
	public DispositivoIoTDAO(ConfiguracionRedDAO configDAO) {
		 if (configDAO == null) {
	            throw new IllegalArgumentException("configuracionDAO no puede ser null");
	        }
		this.configDAO = configDAO;
	}

	
	
	@Override
	public void insertar(DispositivoIoTVO dispositivo) throws Exception {
		try (Connection conn = DatabaseConnection.getConnection();
	             PreparedStatement stmt = conn.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {
			
			setDispositivoParameters(stmt, dispositivo);
            stmt.executeUpdate();
            setGeneratedId(stmt, dispositivo);
		}
		System.out.println("insertao dispositivo");
	}

	@Override
	public void insertTx(DispositivoIoTVO dispositivo, Connection conn) throws Exception {
		try (PreparedStatement stmt = conn.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {
			setDispositivoParameters(stmt, dispositivo);
			stmt.executeUpdate();
			setGeneratedId(stmt, dispositivo);
		}
	}

	@Override
	public void actualizar(DispositivoIoTVO dispositivo) throws Exception {
		 try (Connection conn = DatabaseConnection.getConnection();
	             PreparedStatement stmt = conn.prepareStatement(UPDATE_SQL)) {
			 
			 	stmt.setString(1, dispositivo.getSerial());
				stmt.setString(2, dispositivo.getModelo());
				stmt.setString(3, dispositivo.getUbicacion());
				stmt.setString(4, dispositivo.getFirmwareVersion());
				setConfiguracionId(stmt, 5, dispositivo.getConfiguracionRed());
				stmt.setInt(6, dispositivo.getId());
				
				int rowsAffected = stmt.executeUpdate();
	            if (rowsAffected == 0) {
	                throw new SQLException("No se pudo actualizar el dispositivo con ID: " + dispositivo.getId());
	            }
		 }
	}

	@Override
	public void eliminar(int id) throws Exception {
		try (Connection conn = DatabaseConnection.getConnection();
	             PreparedStatement stmt = conn.prepareStatement(DELETE_SQL)) {

	            stmt.setInt(1, id);
	            
	            int rowsAffected = stmt.executeUpdate();
	            if (rowsAffected == 0) {
	                throw new SQLException("No se encontro dispositivo con ID: " + id);
	            }
	        }
	}

	@Override
	public DispositivoIoTVO getById(int id) throws Exception {
		try (Connection conn = DatabaseConnection.getConnection();
	             PreparedStatement stmt = conn.prepareStatement(SELECT_BY_ID_SQL)) {

	            stmt.setInt(1, id);

	            try (ResultSet rs = stmt.executeQuery()) {
	                if (rs.next()) {
	                    return mapResultSetToDispositivo(rs);
	                }
	            }
	        } catch (SQLException e) {
	            throw new Exception("Error al obtener dispositivo por ID: " + e.getMessage(), e);
	        }
	        return null;
	}
	
	public List<DispositivoIoTVO> getAll() throws Exception {
		 List<DispositivoIoTVO> dispositivos = new ArrayList<>();
		 
		 try (Connection conn = DatabaseConnection.getConnection();
	             Statement stmt = conn.createStatement();
	             ResultSet rs = stmt.executeQuery(SELECT_ALL_SQL)) {

	            while (rs.next()) {
	            	dispositivos.add(mapResultSetToDispositivo(rs));
	            }
	        } catch (SQLException e) {
	            throw new Exception("Error al obtener todos los dispositivos: " + e.getMessage(), e);
	        }
		return dispositivos;
	}
	

	public DispositivoIoTVO buscarPorSerial(String serial) throws SQLException {
		if (serial == null || serial.trim().isEmpty()) {
            throw new IllegalArgumentException("El serial no puede estar vacio");
        }

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SEARCH_BY_SERIAL_SQL)) {

            stmt.setString(1, serial.trim());

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToDispositivo(rs);
                }
            }
        }
        return null;
	}
	
	
	private void setGeneratedId(PreparedStatement stmt, DispositivoIoTVO dispositivo) throws SQLException {
		try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
			if (generatedKeys.next()) {
				dispositivo.setId(generatedKeys.getInt(1));
			} else {
				throw new SQLException("Fallo la carga del dispositivo, no se obtuvo ID generado");
			}
		}
		}


	private void setDispositivoParameters(PreparedStatement stmt, DispositivoIoTVO dispositivo) throws SQLException {
		stmt.setString(1, dispositivo.getSerial());
		stmt.setString(2, dispositivo.getModelo());
		stmt.setString(3, dispositivo.getUbicacion());
		stmt.setString(4, dispositivo.getFirmwareVersion());
		setConfiguracionId(stmt, 5, dispositivo.getConfiguracionRed());
		}

	private void setConfiguracionId(PreparedStatement stmt, int parameterIndex, ConfiguracionRedVO config) throws SQLException {
	    if (config != null && config.getId() > 0) {
	        stmt.setInt(parameterIndex, config.getId());
	    } else {
	        stmt.setNull(parameterIndex, Types.INTEGER);
	    }
	}

	private DispositivoIoTVO mapResultSetToDispositivo(ResultSet rs) throws SQLException {
		
		DispositivoIoTVO dispositivo = new DispositivoIoTVO();
		dispositivo.setId(rs.getInt("idDispositivoIoT"));
		dispositivo.setModelo(rs.getString("modelo"));
		dispositivo.setSerial(rs.getString("serial"));
		dispositivo.setUbicacion(rs.getString("ubicacion"));
		dispositivo.setFirmwareVersion(rs.getString("firmwareVersion"));

       int configID = rs.getInt("idConfiguracion");
       
       if (configID > 0 && !rs.wasNull()) {
           ConfiguracionRedVO config = new ConfiguracionRedVO();
           config.setId(rs.getInt("idConfiguracionRed"));
           config.setDhcpHabilitado(rs.getBoolean("dhcpHabilitado"));
           config.setDnsPrimario(rs.getString("dnsPrimario"));
           config.setGateway(rs.getString("gateway"));
           config.setIp(rs.getString("ip"));
           config.setMascara(rs.getString("mascara"));
           
           dispositivo.setConfiguracionRed(config);
       }
		return dispositivo;
	}
}
