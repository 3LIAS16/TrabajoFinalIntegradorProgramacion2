package main;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

import config.DatabaseConnection;
import config.TransactionManager;
import model.ConfiguracionRedVO;
import model.DispositivoIoTVO;
import service.DispositivoIoTService;

public class MenuHandler {

	private Scanner sc;
	private DispositivoIoTService dispositivoService;

	public MenuHandler(Scanner scanner, DispositivoIoTService dispositivoService) {
		if (scanner == null) {
			throw new IllegalArgumentException("Scanner no puede ser null");
		}
		if (dispositivoService == null) {
			throw new IllegalArgumentException("dispositivoService no puede ser null");
		}
		this.dispositivoService = dispositivoService;
		this.sc = scanner;
	}

	public void crearConfiguracion() {

		try {
			System.out.println("Creando configuracion");
			ConfiguracionRedVO configuracion = nuevaConfiguracion();
			dispositivoService.getConfiguracionRedService().insertar(configuracion);
			System.out.println("Configuracion creada con ID:" + configuracion.getId());
		} catch (Exception e) {
			System.out.println("Error: " + e.getMessage());
		}
	}

	public void listarConfiguraciones() {
		try {
			List<ConfiguracionRedVO> configuraciones = dispositivoService.getConfiguracionRedService().getAll();
			if (configuraciones.isEmpty()) {
				System.out.println("No se encontraron configuraciones.");
				return;
			}
			for (ConfiguracionRedVO c : configuraciones) {
				System.out.println(c);
			}

		} catch (Exception e) {
			System.err.println("Error al listar configuraciones: " + e.getMessage());
		}
	}

	public void buscarConfiguracionPorId() {
		try {
			System.out.print("ID de la configuracion para buscar: ");
			int id = Integer.parseInt(sc.nextLine());
			ConfiguracionRedVO c = dispositivoService.getConfiguracionRedService().getById(id);

			if (c == null) {
				System.out.println("configuracion no encontrada.");
				return;
			}

			System.out.println(c);
		} catch (Exception e) {

		}
	}

	public void actualizarConfiguracionPorId() {
		try {
			System.out.print("ID de la configuracion para actualizar: ");
			int id = Integer.parseInt(sc.nextLine());
			ConfiguracionRedVO c = dispositivoService.getConfiguracionRedService().getById(id);

			if (c == null) {
				System.out.println("configuracion no encontrada.");
				return;
			}

			c = actualizarConfiguracion(c);
			dispositivoService.getConfiguracionRedService().actualizar(c);
			System.out.println("Configuracion actualizada exitosamente");
		} catch (Exception e) {
			System.out.println("Error al actualizar la configuracion: " + e.getMessage());
		}
	}

	public void eliminarConfiguracionPorId() {
		try {
			System.out.print("ID de la configuracion a eliminar: ");
			int id = Integer.parseInt(sc.nextLine());
			dispositivoService.getConfiguracionRedService().eliminar(id);
			System.out.println("configuracion eliminada exitosamente.");
		} catch (Exception e) {
			System.err.println("Error al eliminar la configuracion: " + e.getMessage());
		}
	}

	private ConfiguracionRedVO nuevaConfiguracion() {
		boolean dhcpHabilitado;
		System.out.println("dhcp habilitado (S/N)");
		String dhcp = sc.nextLine();
		if (dhcp.equalsIgnoreCase("s")) {
			dhcpHabilitado = true;
		} else {
			dhcpHabilitado = false;
		}
		System.out.println("IP: ");
		String ip = sc.nextLine();
		System.out.println("mascara: ");
		String mascara = sc.nextLine();
		System.out.println("gateway: ");
		String gateway = sc.nextLine();
		System.out.println("dnsPrimario: ");
		String dns = sc.nextLine();
		return new ConfiguracionRedVO(0, dhcpHabilitado, ip, mascara, gateway, dns);
	}

	private ConfiguracionRedVO actualizarConfiguracion(ConfiguracionRedVO c) {

		boolean dhcpHabilitado;
		System.out.println("dhcp habilitado (S/N): ");
		String dhcp = sc.nextLine();
		if (dhcp.equalsIgnoreCase("s")) {
			dhcpHabilitado = true;
		} else {
			dhcpHabilitado = false;
		}
		if (!dhcp.isEmpty()) {
			c.setDhcpHabilitado(dhcpHabilitado);
		}
		System.out.println("Nueva ip (actual: " + c.getIp() + ", Enter para mantener):");
		String ip = sc.nextLine().trim();
		if (!ip.isEmpty()) {
			c.setIp(ip);
		}
		System.out.println("Nueva mascara (actual: " + c.getMascara() + ", Enter para mantener):");
		String mascara = sc.nextLine().trim();
		if (!mascara.isEmpty()) {
			c.setMascara(mascara);
		}
		System.out.println("Nueva gateway (actual: " + c.getGateway() + ", Enter para mantener):");
		String gateway = sc.nextLine().trim();
		if (!gateway.isEmpty()) {
			c.setGateway(gateway);
		}
		System.out.println("Nuevo dns (actual: " + c.getDnsPrimario() + ", Enter para mantener):");
		String dns = sc.nextLine().trim();
		if (!dns.isEmpty()) {
			c.setDnsPrimario(dns);
		}
		return c;

	}

	public void crearDispositivo() {

		try {
			System.out.print("Numero de serie: ");
			String serial = sc.nextLine().trim();
			System.out.print("Modelo: ");
			String modelo = sc.nextLine().trim();
			System.out.print("Ubicacion: ");
			String ubicacion = sc.nextLine().trim();
			System.out.print("Version de firmware: ");
			String firmwareVersion = sc.nextLine().trim();

			ConfiguracionRedVO config = null;
			System.out.print("¿Desea agregar una configuracion ahora? (s/n): ");
			if (sc.nextLine().equalsIgnoreCase("s")) {
				config = nuevaConfiguracion();
			}

			DispositivoIoTVO dispositivo = new DispositivoIoTVO(0, serial, modelo, ubicacion, firmwareVersion);
			dispositivo.setConfiguracionRed(config);
			dispositivoService.insertar(dispositivo);
			System.out.println("Dispositivo creado exitosamente con ID: " + dispositivo.getId());
		} catch (Exception e) {
			System.err.println("Error al crear persona: " + e.getMessage());
		}
	}

	public void listarDispositivos() {
		try {
			List<DispositivoIoTVO> dispositivos = dispositivoService.getAll();
			if (dispositivos.isEmpty()) {
				System.out.println("No se encontraron configuraciones.");
				return;
			}
			for (DispositivoIoTVO d : dispositivos) {
				System.out.println(d);
			}

		} catch (Exception e) {
			System.err.println("Error al listar dispositivos: " + e.getMessage());
		}
	}

	public void buscarPorSerial() {
		try {
			System.out.print("Ingrese el numero de serie para buscar: ");
			String serial = sc.nextLine();
			DispositivoIoTVO d = dispositivoService.getBySerial(serial);

			if (d == null) {
				System.out.println("dispositivo no encontrado.");
				return;
			}

			System.out.println(d);
		} catch (Exception e) {

		}
	}

	public void actualizarDispositivoPorId() {
		try {
			System.out.print("ID del dispositivo para actualizar: ");
			int id = Integer.parseInt(sc.nextLine());
			DispositivoIoTVO d = dispositivoService.getById(id);

			if (d == null) {
				System.out.println("configuracion no encontrada.");
				return;
			}

			d = actualizarDispositivo(d);
			actualizarConfiguracionDeDispositivo(d);
			dispositivoService.actualizar(d);
			System.out.println("dispositivo actualizado exitosamente");
		} catch (Exception e) {
			System.out.println("Error al actualizar la configuracion: " + e.getMessage());
		}
	}

	private DispositivoIoTVO actualizarDispositivo(DispositivoIoTVO d) {

		System.out.println("Nuevo numero serial (actual: " + d.getSerial() + ", Enter para mantener):");
		String serial = sc.nextLine().trim();
		if (!serial.isEmpty()) {
			d.setSerial(serial);
		}
		System.out.println("Nuevo modelo (actual: " + d.getModelo() + ", Enter para mantener):");
		String modelo = sc.nextLine().trim();
		if (!modelo.isEmpty()) {
			d.setModelo(modelo);
		}
		System.out.println("Nueva ubicacion (actual: " + d.getUbicacion() + ", Enter para mantener):");
		String ubicacion = sc.nextLine().trim();
		if (!ubicacion.isEmpty()) {
			d.setUbicacion(ubicacion);
		}
		System.out.println("Nueva version de firmware (actual: " + d.getFirmwareVersion() + ", Enter para mantener):");
		String firmwareVersion = sc.nextLine().trim();
		if (!firmwareVersion.isEmpty()) {
			d.setFirmwareVersion(firmwareVersion);
		}
		return d;
	}

	public void eliminarDispositivo() {
		try {
			System.out.print("Ingrese la ID del dispositivo para buscar: ");
			int id = Integer.parseInt(sc.nextLine());
			dispositivoService.eliminar(id);
			System.out.println("Dispositivo eliminado exitosamente");
		} catch (Exception e) {
		}
	}

	public void buscarDispositivoPorId() {
		try {
			System.out.print("Ingrese la ID del dispositivo para buscar: ");
			int id = Integer.parseInt(sc.nextLine());
			DispositivoIoTVO d = dispositivoService.getById(id);

			if (d == null) {
				System.out.println("dispositivo no encontrado.");
				return;
			}

			System.out.println(d);
		} catch (Exception e) {

		}
	}

	public void actualizarConfiguracionPorDispositivo() {
		try {
			System.out.print("ID del dispositivo cuya configuracion desea actualizar: ");
			int idDispositivo = Integer.parseInt(sc.nextLine());
			DispositivoIoTVO d = dispositivoService.getById(idDispositivo);

			if (d == null) {
				System.out.println("Dispositivo no encontrado.");
				return;
			}

			if (d.getConfiguracionRed() == null) {
				System.out.println("El dispositivo no tiene una configuracion asociada");
				return;
			}
			ConfiguracionRedVO c = actualizarConfiguracion(d.getConfiguracionRed());
			dispositivoService.getConfiguracionRedService().actualizar(c);
			System.out.println("Configuracion actualizada exitosamente.");
		} catch (Exception e) {
			System.out.println("Error: " + e.getMessage());

		}

	}

	public void eliminarConfiguracionPorDispositivo() {
		try {
			System.out.print("ID del dispositivo cuya configuracion desea eliminar: ");
			int idDispositivo = Integer.parseInt(sc.nextLine());
			DispositivoIoTVO d = dispositivoService.getById(idDispositivo);

			if (d == null) {
				System.out.println("Dispositivo no encontrado.");
				return;
			}

			if (d.getConfiguracionRed() == null) {
				System.out.println("El dispositivo no tiene una configuracion asociada");
				return;
			}
			int configId = d.getConfiguracionRed().getId();
			dispositivoService.eliminarConfiguracionDeDispositivo(configId, idDispositivo);
			System.out.println("Configuracion eliminada exitosamente.");
		} catch (Exception e) {
			System.out.println("Error al eliminar configuracion: " + e.getMessage());

		}
	}

	public void actualizarConfiguracionDeDispositivo(DispositivoIoTVO d) throws Exception {
		if (d.getConfiguracionRed() != null) {
			System.out.print("¿Desea actualizar la configuracion? (s/n): ");
			if (sc.nextLine().equalsIgnoreCase("s")) {
				d.setConfiguracionRed(actualizarConfiguracion(d.getConfiguracionRed()));
				dispositivoService.getConfiguracionRedService().actualizar(d.getConfiguracionRed());
			}
		} else {
			System.out.print("El dispositivo no esta configurado. ¿Desea configurarlo? (s/n): ");
			if (sc.nextLine().equalsIgnoreCase("s")) {
				ConfiguracionRedVO config = nuevaConfiguracion();
				dispositivoService.getConfiguracionRedService().insertar(config);
				d.setConfiguracionRed(config);
			}
		}
	}

	public void crearDispositivoYConfiguracionIndependientes()  {
		try {
		Connection conn = DatabaseConnection.getConnection();
		conn.setAutoCommit(false);
		TransactionManager tm = new TransactionManager(conn);
		try {
			tm.startTransaction();
			crearDispositivoTx(conn);
			System.out.println("Generar error para probar rollback? (S/N)");
			if(sc.nextLine().equalsIgnoreCase("s")) {
				throw new SQLException("Error para prueba de rollback");
			}
			crearConfiguracionTx(conn);
			tm.commit();
		}catch (Exception e) {
			tm.rollback();
			conn.setAutoCommit(true);
			System.out.println("Error en la transaccion, ejecutando rollback: " + e.getMessage());
		}finally {
			conn.setAutoCommit(true);
			tm.close();
		}}catch(Exception e) {
			System.out.println(e);
		}
	}

	public void crearDispositivoTx(Connection conn) {

		try {
			System.out.print("Numero de serie: ");
			String serial = sc.nextLine().trim();
			System.out.print("Modelo: ");
			String modelo = sc.nextLine().trim();
			System.out.print("Ubicacion: ");
			String ubicacion = sc.nextLine().trim();
			System.out.print("Version de firmware: ");
			String firmwareVersion = sc.nextLine().trim();

			ConfiguracionRedVO config = null;

			DispositivoIoTVO dispositivo = new DispositivoIoTVO(0, serial, modelo, ubicacion, firmwareVersion);
			dispositivo.setConfiguracionRed(config);
			dispositivoService.insertTx(dispositivo, conn);
		} catch (Exception e) {
			System.err.println("Error al crear persona: " + e.getMessage());
		}
	}
	
	public void crearConfiguracionTx(Connection conn) {

		try {
			System.out.println("Creando configuracion");
			ConfiguracionRedVO configuracion = nuevaConfiguracion();
			dispositivoService.getConfiguracionRedService().insertTx(configuracion,conn);
			
		} catch (Exception e) {
			System.out.println("Error: " + e.getMessage());
		}
	}
	

}
