# Minimarket

Backend REST para la gestion de un minimarket. El proyecto esta construido con Spring Boot y organiza la logica en controladores, servicios, repositorios y entidades JPA para administrar productos, categorias, usuarios, roles, carrito, inventario y ventas.

## Tecnologias

- Java 17
- Spring Boot 3.4.1
- Spring Web
- Spring Data JPA
- Spring Security
- H2 Database
- Maven Wrapper
- JUnit 5

## Modulos principales

- `controller`: expone los endpoints REST.
- `service`: define la capa de negocio.
- `service.impl`: implementa la logica de servicios.
- `repository`: repositorios JPA para acceso a datos.
- `entity`: modelo de dominio persistente.
- `security`: configuracion de autenticacion, usuarios y password encoder.

## Modelo de datos

El sistema trabaja con las siguientes entidades:

- `Categoria`: agrupa productos por nombre unico.
- `Producto`: contiene nombre, precio, stock y categoria.
- `Usuario`: contiene username, password y roles.
- `Rol`: representa permisos o perfiles de usuario.
- `Carrito`: relaciona usuario, producto y cantidad.
- `Venta`: registra usuario, fecha y detalles de venta.
- `DetalleVenta`: relaciona venta, producto, cantidad y precio.
- `Inventario`: registra movimientos de entrada o salida de productos.

## Requisitos

- Java 17 instalado.
- Git, si se quiere clonar o versionar el proyecto.
- No es necesario instalar Maven globalmente porque el proyecto incluye Maven Wrapper.

## Configuracion

La configuracion actual esta en `src/main/resources/application.properties`:

```properties
spring.application.name=minimarket
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driver-class-name=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=
spring.h2.console.enabled=true
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.H2Dialect
```

La base de datos H2 se ejecuta en memoria, por lo que los datos se pierden al reiniciar la aplicacion.

## Ejecutar el proyecto

En Windows:

```powershell
.\mvnw.cmd spring-boot:run
```

En Linux o macOS:

```bash
./mvnw spring-boot:run
```

La aplicacion queda disponible por defecto en:

```text
http://localhost:8080
```

## Seguridad

La configuracion actual de Spring Security:

- Permite acceso publico a `/public/**`.
- Requiere autenticacion para el resto de rutas.
- Usa login por formulario de Spring Security.
- Usa `BCryptPasswordEncoder` para contrasenas.
- Incluye una clase `JwtUtil`, pero actualmente no implementa logica JWT.

Endpoint publico de prueba:

```http
GET /public/hola
```

Respuesta:

```text
¡Hola Mundo!
```

## Endpoints REST

Los endpoints bajo `/api/**` requieren autenticacion.

### Categorias

| Metodo | Ruta | Descripcion |
| --- | --- | --- |
| GET | `/api/categorias` | Lista todas las categorias |
| GET | `/api/categorias/{id}` | Obtiene una categoria por id |
| POST | `/api/categorias` | Crea una categoria |
| PUT | `/api/categorias/{id}` | Actualiza una categoria |
| DELETE | `/api/categorias/{id}` | Elimina una categoria |

Ejemplo de categoria:

```json
{
  "nombre": "Bebidas"
}
```

### Productos

| Metodo | Ruta | Descripcion |
| --- | --- | --- |
| GET | `/api/productos` | Lista todos los productos |
| GET | `/api/productos/{id}` | Obtiene un producto por id |
| POST | `/api/productos` | Crea un producto |
| PUT | `/api/productos/{id}` | Actualiza un producto |
| DELETE | `/api/productos/{id}` | Elimina un producto |

Ejemplo de producto:

```json
{
  "nombre": "Coca-Cola 1.5L",
  "precio": 1800,
  "stock": 25,
  "categoria": {
    "id": 1
  }
}
```

### Usuarios

| Metodo | Ruta | Descripcion |
| --- | --- | --- |
| GET | `/api/usuarios` | Lista todos los usuarios |
| GET | `/api/usuarios/{id}` | Obtiene un usuario por id |
| POST | `/api/usuarios` | Crea un usuario |
| PUT | `/api/usuarios/{id}` | Actualiza un usuario |
| DELETE | `/api/usuarios/{id}` | Elimina un usuario |

Ejemplo de usuario:

```json
{
  "username": "admin",
  "password": "password123",
  "roles": [
    {
      "id": 1
    }
  ]
}
```

### Carrito

| Metodo | Ruta | Descripcion |
| --- | --- | --- |
| GET | `/api/carrito` | Lista los productos del carrito |
| GET | `/api/carrito/{id}` | Obtiene un item del carrito por id |
| POST | `/api/carrito` | Agrega un producto al carrito |
| PUT | `/api/carrito/{id}` | Actualiza un item del carrito |
| DELETE | `/api/carrito/{id}` | Elimina un item del carrito |

Ejemplo de item de carrito:

```json
{
  "usuario": {
    "id": 1
  },
  "producto": {
    "id": 1
  },
  "cantidad": 2
}
```

### Inventario

| Metodo | Ruta | Descripcion |
| --- | --- | --- |
| GET | `/api/inventario` | Lista movimientos de inventario |
| GET | `/api/inventario/{id}` | Obtiene un movimiento por id |
| POST | `/api/inventario` | Registra un movimiento |
| PUT | `/api/inventario/{id}` | Actualiza un movimiento |
| DELETE | `/api/inventario/{id}` | Elimina un movimiento |

Ejemplo de movimiento:

```json
{
  "producto": {
    "id": 1
  },
  "cantidad": 10,
  "tipoMovimiento": "Entrada",
  "fechaMovimiento": "2026-07-02T21:00:00.000+00:00"
}
```

### Ventas

| Metodo | Ruta | Descripcion |
| --- | --- | --- |
| GET | `/api/ventas` | Lista todas las ventas |
| GET | `/api/ventas/{id}` | Obtiene una venta por id |
| POST | `/api/ventas` | Crea una venta |

Ejemplo de venta:

```json
{
  "usuario": {
    "id": 1
  },
  "fecha": "2026-07-02T21:00:00.000+00:00"
}
```

### Detalles de venta

| Metodo | Ruta | Descripcion |
| --- | --- | --- |
| GET | `/api/detalle-ventas` | Lista todos los detalles de venta |
| GET | `/api/detalle-ventas/{id}` | Obtiene un detalle por id |
| POST | `/api/detalle-ventas` | Crea un detalle de venta |
| PUT | `/api/detalle-ventas/{id}` | Actualiza un detalle |
| DELETE | `/api/detalle-ventas/{id}` | Elimina un detalle |

Ejemplo de detalle de venta:

```json
{
  "venta": {
    "id": 1
  },
  "producto": {
    "id": 1
  },
  "cantidad": 2,
  "precio": 1800
}
```

## Consola H2

La consola H2 esta habilitada:

```text
http://localhost:8080/h2-console
```

Datos de conexion:

```text
JDBC URL: jdbc:h2:mem:testdb
User Name: sa
Password:
```

## Ejecutar pruebas

En Windows:

```powershell
.\mvnw.cmd test
```

En Linux o macOS:

```bash
./mvnw test
```

## Estado actual del proyecto

- El proyecto tiene CRUD basico para categorias, productos, usuarios, carrito, inventario y detalles de venta.
- Ventas permite listar, obtener por id y crear.
- La autenticacion esta basada en formulario, no en JWT.
- La base de datos es temporal porque H2 esta configurado en memoria.
- No existen datos iniciales cargados automaticamente.

