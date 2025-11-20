package main;

public class MenuDisplay {

	public static void menuPrincipal() {
		System.out.println("Opciones disponibles:\n1 - Administrar configuraciones"
				+ "\n2 - Administrar dispositivos"
				+ "\n3 - Crear dispositivo y configuracion (Ejemplo de rollback)"
				+ "\n0 - Salir del programa");
	}
	
	public static void menuConfiguracion() {
		System.out.println("Opciones de configuracion disponibles: "
				+ "\n1 - Crear nueva configuracion"
				+ "\n2 - Listar configuraciones existentes"
				+ "\n3 - Buscar por ID"
				+ "\n4 - Actualizar configuracion por id"
				+ "\n5 - Actualizar configuracion por dispositivo"
				+ "\n6 - Eliminar una configuracion por id"
				+ "\n7 - Eliminar una configuracion por dispositivo"
				+ "\n0 - Volver");
	}
	
	public static void menuDispositivos() {
		System.out.println("Opciones de dispositivos disponibles: "
				+ "\n1 - Crear nueva dispositivo"
				+ "\n2 - Listar dispositivos existentes"
				+ "\n3 - Buscar por ID"
				+ "\n4 - Buscar por numero de serie"
				+ "\n5 - Actualizar dispositivos"
				+ "\n6 - Eliminar una dispositivo"
				+ "\n0 - Volver");
	}
	
}
