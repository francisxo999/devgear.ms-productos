* v1.0.0: Inicialización del microservicio de productos.
* v1.1.0: Creación de ProductoController, entidades y conexión a MySQL.
* v1.2.0: Configuración de Spring Security y validación JWT con Azure AD.
* v1.2.1: Limpieza de código e imports sin uso en ProductoController.
* v1.3.0: Creación de Dockerfile multi-etapa para despliegue en AWS.
* v1.3.1: Hotfix en EC2 - Inclusión del servicio app-productos en docker-compose (puerto 8081), corrección de la conexión a base de datos (host db-devgear y esquema productos_db), actualización del emisor JWT a Azure AD v1.0 y relajación de permisos de acceso GET.
* v1.3.2: Corrección definitiva del issuer JWT a Azure AD v2.0 (login.microsoftonline.com) y remoción del audience mal formado.
* v1.3.3: Forzar IPv4 en la JVM (java.net.preferIPv4Stack) para evitar fallo de validación del issuer por IPv6 no ruteable dentro del contenedor.
* v1.3.4: Amplía longitud de columnas nombre/descripcion en Producto, agrega GlobalExceptionHandler para respuestas de error controladas y fix de encoding UTF-8 en la conexión JDBC.
* v1.4.0: Arquitectura DTOs, paginacion, borrado logico y gestion global de excepciones
* v1.4.1: Captura IllegalArgumentException en GlobalExceptionHandler para responder 400 en vez de 500