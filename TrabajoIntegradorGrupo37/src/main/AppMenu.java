package main;

import java.sql.SQLException;
import java.util.Scanner;

import dao.ConfiguracionRedDAO;
import dao.DispositivoIoTDAO;
import service.ConfiguracionRedService;
import service.DispositivoIoTService;


public class AppMenu {
	private Scanner sc;
	private boolean running;
	private MenuHandler menuHandler;

	
	public AppMenu() {
        this.sc = new Scanner(System.in);
        DispositivoIoTService dispositivoService = crearDispositivoService();
        this.menuHandler = new MenuHandler(sc, dispositivoService);
        this.running = true;
    }
	
	private DispositivoIoTService crearDispositivoService() {
		ConfiguracionRedDAO configuracionDAO = new ConfiguracionRedDAO();
		DispositivoIoTDAO dispositivoDAO = new DispositivoIoTDAO(configuracionDAO);
		ConfiguracionRedService  configuracionService = new ConfiguracionRedService(configuracionDAO);
		return new DispositivoIoTService(dispositivoDAO, configuracionService);
	}

	public void run()  {
		while (running) {
			try {
				MenuDisplay.menuPrincipal();
				int opcion = Integer.parseInt(sc.nextLine());
				processOptionPrincipal(opcion);
			} catch (NumberFormatException e) {
				System.out.println("Entrada invalida. Por favor, ingrese un numero.");
			}
		}
		sc.close();
	}

	private void processOptionPrincipal(int opcion) {
		switch (opcion) {
		case 1 -> {
			MenuDisplay.menuConfiguracion();
			opcion = Integer.parseInt(sc.nextLine());
			processOptionConfiguracion(opcion);
		}
		case 2 -> {
			MenuDisplay.menuDispositivos();
			opcion = Integer.parseInt(sc.nextLine());
			processOptionDispositivos(opcion);
		}
		case 3 ->menuHandler.crearDispositivoYConfiguracionIndependientes();
		case 0 -> {
			System.out.println("saliendo...");
			running = false;
		}
		default -> System.out.println("Opcion no valida");
		}
	}

	private void processOptionDispositivos(int opcion) {
		switch(opcion) {
		case 1 -> menuHandler.crearDispositivo();
        case 2 -> menuHandler.listarDispositivos();
        case 3 -> menuHandler.buscarDispositivoPorId();
        case 4 -> menuHandler.buscarPorSerial();
        case 5 -> menuHandler.actualizarDispositivoPorId();
        case 6 -> menuHandler.eliminarDispositivo();
        case 0 -> {
			System.out.println("volviendo...");
		}
        default -> System.out.println("Opcion no valida");
		}
	}

	private void processOptionConfiguracion(int opcion) {
		switch(opcion) {
		case 1 -> menuHandler.crearConfiguracion();
        case 2 -> menuHandler.listarConfiguraciones();
        case 3 -> menuHandler.buscarConfiguracionPorId();
        case 4 -> menuHandler.actualizarConfiguracionPorId();
        case 5 -> menuHandler.actualizarConfiguracionPorDispositivo();
        case 6 -> menuHandler.eliminarConfiguracionPorId();
        case 7 -> menuHandler.eliminarConfiguracionPorDispositivo();

        case 0 -> {
			System.out.println("volviendo...");
		}
        default -> System.out.println("Opcion no valida");
		}
	}
}


