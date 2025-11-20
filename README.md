# Sistema de Gestión de Dispositivos y Configuraciones de Red

## Trabajo Práctico Integrador - Programación 2

### Descripción del Proyecto

Este Trabajo Práctico Integrador tiene como objetivo demostrar la aplicación práctica de los conceptos fundamentales de Programación Orientada a Objetos y Persistencia de Datos aprendidos durante el cursado de Programación 2. El proyecto consiste en desarrollar un sistema completo de gestión de dispositivos y Configuraciones de red que permita realizar operaciones CRUD (Crear, Leer, Actualizar, Eliminar) sobre estas entidades, implementando una arquitectura robusta y profesional.

### Objetivos Académicos

El desarrollo de este sistema permite aplicar y consolidar los siguientes conceptos clave de la materia:

**1. Arquitectura en Capas (Layered Architecture)**
- Implementación de separación de responsabilidades en 4 capas diferenciadas
- Capa de Presentación (Main/UI): Interacción con el usuario mediante consola
- Capa de Lógica de Negocio (Service): Validaciones y reglas de negocio
- Capa de Acceso a Datos (DAO): Operaciones de persistencia
- Capa de Modelo (Models): Representación de entidades del dominio

**2. Programación Orientada a Objetos**
- Aplicación de principios SOLID (Single Responsibility, Dependency Injection)
- Uso de herencia mediante clase abstracta Base
- Implementación de interfaces genéricas (GenericDAO, GenericService)
- Encapsulamiento con atributos privados y métodos de acceso
- Sobrescritura de métodos (toString y  métodos de Interfaces genéricas)

**3. Persistencia de Datos con JDBC**
- Conexión a base de datos MySQL mediante JDBC
- Implementación del patrón DAO (Data Access Object)
- Uso de PreparedStatements para prevenir SQL Injection
- Gestión de transacciones con commit y rollback
- Manejo de claves autogeneradas (AUTO_INCREMENT)
- Consultas con LEFT JOIN para relaciones entre entidades

**4. Manejo de Recursos y Excepciones**
- Uso del patrón try-with-resources para gestión automática de recursos JDBC
- Implementación de AutoCloseable en TransactionManager
- Manejo apropiado de excepciones con propagación controlada
- Validación multi-nivel: base de datos y aplicación

**5. Patrones de Diseño**
- Factory Pattern (DatabaseConnection)
- Service Layer Pattern (separación lógica de negocio)
- DAO Pattern (abstracción del acceso a datos)
- Soft Delete Pattern (eliminación lógica de registros)
- Dependency Injection manual

**6. Validación de Integridad de Datos**
- Validación de unicidad (Numero de serie único por dispositvo)
- Validación de campos obligatorios en múltiples niveles
- Validación de integridad referencial (Foreign Keys)
- Implementación de eliminación segura para prevenir referencias huérfanas

### Funcionalidades Implementadas

El sistema permite gestionar dos entidades principales con las siguientes operaciones:

## Características Principales

- **Gestión de Dispositivos**: Crear, listar, actualizar y eliminar dispositivos con validación de numero de serie único
- **Gestión de Configuracion de red**: Administrar la configuracion de red de forma independiente o asociados a dispositivos
- **Búsqueda Inteligente**: Filtrar dispositivos por numero de serie 
- **Soft Delete**: Eliminación lógica que preserva la integridad de datos
- **Seguridad**: Protección contra SQL injection mediante PreparedStatements
- **Validación Multi-capa**: Validaciones en capa de servicio y base de datos
- **Transacciones**: Soporte para operaciones atómicas con rollback automático

## Requisitos del Sistema

| Componente | Versión Requerida |
|------------|-------------------|
| Java JDK | 17 o superior |
| MySQL | 8.0 o superior |
| Sistema Operativo | Windows, Linux o macOS |

## Instalación

### 1. Configurar Base de Datos

Ejecutar el siguiente script SQL en MySQL:

```sql
CREATE SCHEMA IF NOT EXISTS `mydb` DEFAULT CHARACTER SET utf8mb3 ;
USE `mydb` ;

-- -----------------------------------------------------
-- Table `mydb`.`configuracionred`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`configuracionred` (
  `idConfiguracionRed` INT NOT NULL AUTO_INCREMENT,
  `eliminado` TINYINT NULL DEFAULT '0',
  `ip` VARCHAR(45) NULL DEFAULT NULL,
  `mascara` VARCHAR(45) NULL DEFAULT NULL,
  `gateway` VARCHAR(45) NULL DEFAULT NULL,
  `dnsPrimario` VARCHAR(45) NULL DEFAULT NULL,
  `dhcpHabilitado` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`idConfiguracionRed`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb3;


-- -----------------------------------------------------
-- Table `mydb`.`dispositivoiot`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`dispositivoiot` (
  `idDispositivoIoT` INT NOT NULL AUTO_INCREMENT,
  `eliminado` TINYINT NULL DEFAULT '0',
  `serial` VARCHAR(50) NOT NULL,
  `modelo` VARCHAR(50) NOT NULL,
  `ubicacion` VARCHAR(120) NULL DEFAULT NULL,
  `firmwareVersion` VARCHAR(30) NULL DEFAULT NULL,
  `idConfiguracion` INT NULL,
  PRIMARY KEY (`idDispositivoIoT`),
  UNIQUE INDEX `serial_UNIQUE` (`serial` ASC) VISIBLE,
  INDEX `fk_DispositivoIoT_ConfiguracionRed_idx` (`idConfiguracion` ASC) VISIBLE,
  CONSTRAINT `fk_DispositivoIoT_ConfiguracionRed`
    FOREIGN KEY (`idConfiguracion`)
    REFERENCES `mydb`.`configuracionred` (`idConfiguracionRed`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb3;

```



### 2. Configurar Conexión (Opcional)

Por defecto conecta a:
- **Host**: localhost:3306
- **Base de datos**: mydb
- **Usuario**: root
- **Contraseña**: (vacía)

Para cambiar la configuración, usar propiedades del sistema:

```bash
java -Ddb.url=jdbc:mysql://localhost:3306/mydb \
     -Ddb.user=usuario \
     -Ddb.password=clave \
     -cp ...
```

## Ejecución

### Opción 1: Desde IDE
1. Abrir proyecto en netBeans o Eclipse
2. Ejecutar clase `Main.java`

### Opción 2: Línea de comandos

**Windows:**
```bash
# Localizar JAR de MySQL
dir /s /b %USERPROFILE%\.gradle\caches\*mysql-connector-j-8.4.0.jar

# Ejecutar (reemplazar <ruta-mysql-jar>)
java -cp "build\classes\java\main;<ruta-mysql-jar>" Main.java
```



### Verificar Conexión

```bash
# Usar TestConexion para verificar conexión a BD
java -cp "build/classes/java/main:<ruta-mysql-jar>" Main.TestConexion
```

Salida esperada:
```
Conexion exitosa a la base de datos
Usuario conectado: root@localhost
Base de datos: mydb
URL: jdbc:mysql://localhost:3306/mydb
Driver: MySQL Connector/J v8.4.0
```

## Uso del Sistema

### Menú Principal

```
========= MENU Principal =========
Opciones disponibles:
1 - Administrar configuraciones
2 - Administrar dispositivos
0 - Salir del programa
```
### Menú de Configuraciones

```
========= MENU Configuraciones =========

Opciones de configuracion disponibles:
1 - Crear nueva configuracion
2 - Listar configuraciones existentes
3 - Buscar por ID
4 - Actualizar configuracion por id
5 - Actualizar configuracion por dispositivo
6 - Eliminar una configuracion por id
7 - Eliminar una configuracion por dispositivo
0 - Volver

```
#### 1. Crear Configuracion de Red
- Solicita ip, mascara, gateway, DNSprimario, DHCP habilitado (SI/NO)
- Crea configuracion de red independiente sin asociarlo a persona
- Puede asociarse posteriormente


#### 2. Listar Configuracion de Red
- Listar todos: Muestra todas las configuraciones de red con sus atributos inclusive el ID


#### 3. Buscar por ID
- Buscar: Filtra por ID


#### 4. Actualizar Configuracion de Red por ID
- Actualiza ip, mascara, gateway, DNSprimario, DHCP habilitado
- Requiere ID de la configuracion de red


#### 5. Actualizar Configuracion de Red por Dispositivo
- Actualiza ip, mascara, gateway, DNSprimario, DHCP habilitado
- Requiere ID del dispositivo


#### 6. Eliminar Configuracion de Red por ID
- Eliminación lógica (marca como eliminado, no borra físicamente)
- Requiere ID de la configuracion de red


#### 7. Eliminar Configuracion de Red por ID
- Eliminación lógica (marca como eliminado, no borra físicamente)
- Requiere ID del dispositivo



### Menu de Dispositivos

```
========= MENU Dispositivos =========

Opciones de dispositivos disponibles:
1 - Crear nueva dispositivo
2 - Listar dispositivos existentes
3 - Buscar por ID
4 - Buscar por numero de serie
5 - Actualizar dispositivos
6 - Eliminar una dispositivo
0 - Volver

```
#### 1. Crear Dispositivo
- Solicita serial, modelo, ubicacion, firmware
- Permite agregar configuracion opcionalmente
- Valida el numero de serie único (no permite duplicados)


#### 2. Listar Dispositivos
- Listar todos: Muestra todas los dispositivos


#### 3. Buscar por ID
- Buscar: Filtra por ID


#### 4. buscar por numero de serie
- Buscar: Filtra por numero de serie


#### 5. Actualizar Dispositivos
- Permite modificar serial, modelo, ubicacion, firmware
- Permite actualizar o agregar configuracion
- Presionar Enter sin escribir mantiene el valor actual


#### 6. Eliminar Dispositivo
- Eliminación lógica (marca como eliminado, no borra físicamente)
- Requiere ID del dispositivo


## Arquitectura

### Estructura en Capas

```
┌─────────────────────────────────────┐
│     Main / UI Layer                 │
│  (Interacción con usuario)          │
│  AppMenu, MenuHandler, MenuDisplay  │
└───────────┬─────────────────────────┘
            │
┌───────────▼─────────────────────────┐
│     Service Layer                   │
│  (Lógica de negocio y validación)   │
│  DispositivoIoTService              │
│  ConfiguracionRedService            │
└───────────┬─────────────────────────┘
            │
┌───────────▼─────────────────────────┐
│     DAO Layer                       │
│  (Acceso a datos)                   │
│  DispositvoIotDAO,                  │
│   ConfiguracionRedDAO               │
└───────────┬─────────────────────────┘
            │
┌───────────▼─────────────────────────┐
│     Models Layer                    │
│  (Entidades de dominio)             │
│ DispostivoIoTVO, ConfiguracionRedVO │
│    , BaseVO                         │
└─────────────────────────────────────┘
```

### Componentes Principales

**Config/**
- `DatabaseConnection.java`: Gestión de conexiones JDBC con validación en inicialización estática
- `TransactionManager.java`: Manejo de transacciones con AutoCloseable

**Models/**
- `BaseVO.java`: Clase abstracta con campos id y eliminado
- `DispositivosIoTVO.java`: Entidad Dispositivo (serial, modelo, ubicacion, firmware)
- `ConfiguracionDeRedVO.java`: Entidad Configuracion de red (ip, mascara, gateway, DNSprimario, DHCP habilitado (SI/NO))

**Dao/**
- `GenericDAO<T>`: Interface genérica con operaciones CRUD
- `DispositvoIotDAO`: Implementación con queries LEFT JOIN para incluir configuraciones de red
- `ConfiguracionRedDAO`: Implementación para configuraciones de red

**Service/**
- `GenericService<T>`: Interface genérica para servicios
- `DispositivoIoTService`: Validaciones de dispositivo y coordinación con configuraciones de red
- `ConfiguracionRedService`: Validaciones de configuraciones de red

**Main/**
- `Main.java`: Punto de entrada
- `AppMenu.java`: Orquestador del ciclo de menú
- `MenuHandler.java`: Implementación de operaciones CRUD con captura de entrada
- `MenuDisplay.java`: Lógica de visualización de menús
- `TestConexion.java`: Utilidad para verificar conexión a BD

## Modelo de Datos

```
┌────────────────────┐          ┌───────────────────────────┐
│     Dispositivos   │          │   Configuracion de red    │
├────────────────────┤          ├───────────────────────────│
│ id (PK)            │          │ id (PK)                   │
│ serial (UNIQUE)    │          │ ip                        │
│ modelo             │          │ mascara                   │
│ ubicacion          │          │ gateway                   │
│ firmware           │          │ DNS primario              │
│ idConfiguracion(FK)│          │ DHCP Habilitado           │ 
│ eliminado          │          │ eliminado                 │
│                    ││──────┐  └───────────────────────────┘
└────────────────────┘       │
                             │
                             └──▶ Relación 0..1
```

**Reglas:**
- Un dispositivo puede tener 0 o 1 configuracion de red
- Serial es único (constraint en base de datos y validación en aplicación)
- Eliminación lógica: campo `eliminado = TRUE`
- Foreign key `idConfiguracion` puede ser NULL

## Patrones y Buenas Prácticas

### Seguridad
- **100% PreparedStatements**: Prevención de SQL injection
- **Validación multi-capa**: Service layer valida antes de persistir
- **Serial único**: Constraint en BD + validación en `DispositvoIoTService.validateSerialUnique()`

### Gestión de Recursos
- **Try-with-resources**: Todas las conexiones, statements y resultsets
- **AutoCloseable**: TransactionManager cierra y hace rollback automático
- **Scanner cerrado**: En `AppMenu.run()` al finalizar

### Validaciones
- **Input trimming**: Todos los inputs usan `.trim()` inmediatamente
- **Campos obligatorios**: Validación de null y empty en service layer
- **IDs positivos**: Validación `id > 0` en todas las operaciones
- **Verificación de rowsAffected**: En UPDATE y DELETE

### Soft Delete
- DELETE ejecuta: `UPDATE tabla SET eliminado = TRUE WHERE id = ?`
- SELECT filtra: `WHERE eliminado = FALSE`
- No hay eliminación física de datos

## Reglas de Negocio Principales

1. **Serial único**: No se permiten dispositivos con serial duplicado
2. **Campos obligatorios**: Serial y Modelo son requeridos para dispositivo
3. **Validación antes de persistir**: Service layer valida antes de llamar a DAO
4. **Preservación de valores**: En actualización, campos vacíos mantienen valor original

## Solución de Problemas

### Error: "ClassNotFoundException: com.mysql.cj.jdbc.Driver"
**Causa**: JAR de MySQL no está en classpath

**Solución**: Incluir mysql-connector-j-8.4.0.jar en el comando java -cp

### Error: "Communications link failure"
**Causa**: MySQL no está ejecutándose

**Solución**:
```bash
# Linux/macOS
sudo systemctl start mysql
# O
brew services start mysql

# Windows
net start MySQL80
```

### Error: "Access denied for user 'root'@'localhost'"
**Causa**: Credenciales incorrectas

**Solución**: Verificar usuario/contraseña en DatabaseConnection.java o usar -Ddb.user y -Ddb.password

### Error: "Unknown database 'mydb'"
**Causa**: Base de datos no creada

**Solución**: Ejecutar script de creación de base de datos (ver sección Instalación)

### Error: "Table 'personas' doesn't exist"
**Causa**: Tablas no creadas

**Solución**: Ejecutar script de creación de tablas (ver sección Instalación)

## Limitaciones Conocidas

1. **No hay tarea gradle run**: Debe ejecutarse con java -cp manualmente o desde IDE
2. **Interfaz solo consola**: No hay GUI gráfica
3. **Una configuracion por dispositivo**: No soporta múltiples configuraciones
4. **Sin paginación**: Listar todos puede ser lento con muchos registros
5. **Sin pool de conexiones**: Nueva conexión por operación (aceptable para app de consola)


## Tecnologías Utilizadas

- **Lenguaje**: Java 17
- **Build Tool**: Gradle 8.12
- **Base de Datos**: MySQL 8.x
- **JDBC Driver**: mysql-connector-j 8.4.0
- **Testing**: JUnit 5 (configurado, sin tests implementados)


## Convenciones de Código

- **Idioma**: Español (nombres de clases, métodos, variables)
- **Nomenclatura**:
  - Clases: PascalCase 
  - Métodos: camelCase 
  - Constantes SQL: UPPER_SNAKE_CASE
- **Indentación**: 4 espacios
- **Recursos**: Siempre usar try-with-resources
- **SQL**: Constantes privadas static final
- **Excepciones**: Capturar y manejar con mensajes al usuario
