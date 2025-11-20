package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import config.DatabaseConnection;
import model.ConfiguracionRedVO;

public class ConfiguracionRedDAO implements GenericDAO<ConfiguracionRedVO> {

	private static final String INSERT_SQL = "INSERT INTO ConfiguracionRed (ip, mascara, gateway, dnsPrimario, dhcpHabilitado) VALUES (?, ?, ?, ?, ?)";

	private static final String UPDATE_SQL = "UPDATE ConfiguracionRed SET ip = ?, mascara = ?, gateway = ?, dnsPrimario = ?, dhcpHabilitado = ? WHERE idConfiguracionRed = ?";

	private static final String DELETE_SQL = "UPDATE ConfiguracionRed SET eliminado = TRUE WHERE idConfiguracionRed = ?";

	private static final String SELECT_BY_ID_SQL = "SELECT * FROM ConfiguracionRed WHERE idConfiguracionRed = ? AND eliminado = FALSE";
	
    private static final String SELECT_ALL_SQL = "SELECT * FROM ConfiguracionRed WHERE eliminado = FALSE";


	@Override
	public void insertar(ConfiguracionRedVO config) throws Exception {
		try (Connection conn = DatabaseConnection.getConnection();
				PreparedStatement stmt = conn.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {

			setConfiguracionParameters(stmt, config);
			stmt.executeUpdate();

			setGeneratedId(stmt, config);
		}
		System.out.println("insertao config");
	}

	@Override
	public void insertTx(ConfiguracionRedVO config, Connection conn) throws Exception {
		try (PreparedStatement stmt = conn.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {
			setConfiguracionParameters(stmt, config);
			stmt.executeUpdate();
			setGeneratedId(stmt, config);
		}
	}

	@Override
	public void actualizar(ConfiguracionRedVO config) throws Exception {
		try (Connection conn = DatabaseConnection.getConnection();
				PreparedStatement stmt = conn.prepareStatement(UPDATE_SQL)) {

			stmt.setString(1, config.getIp());
			stmt.setString(2, config.getMascara());
			stmt.setString(3, config.getGateway());
			stmt.setString(4, config.getDnsPrimario());
			stmt.setBoolean(5, config.isDhcpHabilitado());
			stmt.setInt(6, config.getId());

			int rowsAffected = stmt.executeUpdate();
			if (rowsAffected == 0) {
				throw new SQLException("No se pudo actualizar la configuracion con ID: " + config.getId());
			}
			System.out.println("actualizao config");
		}

	}

	@Override
	public void eliminar(int id) throws Exception {
		try (Connection conn = DatabaseConnection.getConnection();
				PreparedStatement stmt = conn.prepareStatement(DELETE_SQL)) {
			stmt.setInt(1, id);

			int rowsAffected = stmt.executeUpdate();
			if (rowsAffected == 0) {
				throw new SQLException("No se encontro configuarion con ID: " + id);
			}
		}
	}

	@Override
	public ConfiguracionRedVO getById(int id) throws Exception {
		try (Connection conn = DatabaseConnection.getConnection();
				PreparedStatement stmt = conn.prepareStatement(SELECT_BY_ID_SQL)) {

			stmt.setInt(1, id);
			try (ResultSet rs = stmt.executeQuery()) {
				if (rs.next()) {
					return mapResultSetToConfig(rs);
				}
			}
		}
		return null;
	}


	private void setGeneratedId(PreparedStatement stmt, ConfiguracionRedVO config) throws SQLException {
		try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
			if (generatedKeys.next()) {
				config.setId(generatedKeys.getInt(1));
			} else {
				throw new SQLException("Fallo la carga de la configuracion, no se obtuvo ID generado");
			}
		}
	}

	private void setConfiguracionParameters(PreparedStatement stmt, ConfiguracionRedVO config) throws SQLException {
		stmt.setString(1, config.getIp());
		stmt.setString(2, config.getMascara());
		stmt.setString(3, config.getGateway());
		stmt.setString(4, config.getDnsPrimario());
		stmt.setBoolean(5, config.isDhcpHabilitado());

	}
	
	private ConfiguracionRedVO mapResultSetToConfig(ResultSet rs) throws SQLException {
		ConfiguracionRedVO config = new ConfiguracionRedVO(rs.getInt(1),rs.getBoolean(7), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6));
		return config;
	}

	public List<ConfiguracionRedVO> getAll() throws SQLException {
		List<ConfiguracionRedVO> configs = new ArrayList<>();
		 try (Connection conn = DatabaseConnection.getConnection();
	             Statement stmt = conn.createStatement();
	             ResultSet rs = stmt.executeQuery(SELECT_ALL_SQL)) {

	            while (rs.next()) {
	                configs.add(mapResultSetToConfig(rs));
	            }
	        }
		return configs;
	}
	

	

}
