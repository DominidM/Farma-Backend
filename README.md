# Farma-Backend

Sistema backend para la gestión de una farmacia, desarrollado con **Java Spring Boot** y **MySQL**.

## Funcionalidades principales

- Gestión de productos, inventario y proveedores
- Registro y control de ventas y compras
- Control de usuarios (empleados/cajeros)
- Soporte para diferentes precios de compra por producto (trazabilidad de inventario)
- Cálculo de utilidades y reportes
- API RESTful para integración con frontend Angular
- [Integración con lectura de código de barras (a través del frontend)]

## Tecnologías utilizadas

- Java 17+
- Spring Boot
- Spring Data JPA (Hibernate)
- MySQL
- Maven

## Requisitos

- Java 17 o superior
- MySQL 8 o superior
- Maven 3.8+

## Configuración

1. Clona el repositorio:
   ```bash
   git clone https://github.com/tu-usuario/Farma-Backend.git
   ```

2. Configura la conexión a la base de datos en `src/main/resources/application.properties`:
   ```
   spring.datasource.url=jdbc:mysql://localhost:3306/farmacia_db
   spring.datasource.username=TU_USUARIO
   spring.datasource.password=TU_PASSWORD
   ```

3. Ejecuta las migraciones (si tienes scripts) o deja que JPA cree las tablas automáticamente.

4. Ejecuta la aplicación:
   ```bash
   mvn spring-boot:run
   ```

## Endpoints principales

- `/api/productos`
- `/api/ventas`
- `/api/compras`
- `/api/usuarios`
- ... (ver documentación de la API)

## Notas

- El cálculo de utilidades considera el precio real de compra (historial de compras).
- La lectura de códigos de barras se realiza desde el frontend, pero el backend expone los endpoints necesarios para buscar productos por código de barras.

