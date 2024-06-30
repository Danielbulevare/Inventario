- IDE: Apache NetBeans IDE 21
- Versión del lenguaje de programación utilizado: OpenJDK jdk-21.0.2
- DBMS utilizado y su versión: 8.0.36 MySQL Community Server - GPL

Para poder ejecutar la aplicación en tu equipo, sigue los siguientes pasos en orden:

1) Debes de tener instalado en el disco "C:" el servidor Apache TomEE, la versión apache-tomee-plume-10.0.0-M1. Puedes descargarlo del siguiente enlace oficial: https://tomee.apache.org/download.html
2) Para poder instalar y configurar el servidor Apache TomEE en NetBeans, mira el siguiente video: https://www.youtube.com/watch?v=mk4wpq7pFv0
3) Cuando tengas instalado y configurado todo lo anterior, debes de ingresar a NetBeans, ir a la pestaña a de Services>Servers y ejecutar el servidor Apache TomEE
4) Ingresa a MySQL Workbench o desde la terminal de MySQL, y ejecuta todos los scripts en el orden en que aparecen en el archivo que esta en el repositorio de la siguiente url: https://github.com/Danielbulevare/ScriptsSQLInventario/blob/main/BD_inventario.sql
5) Descarga la aplicación web (la cual contiene las apis que consumen la app cliente) que está en el siguiente repositorio, y abrelo en NetBeans: https://github.com/Danielbulevare/RESTfulInventario-1
6) Abre el paquete "modelo" y verás una clase llamada ConexionBD, debes de poner el usuario y contraseña de tu SGBD (MySQL), así como la ip y el puerto, si es que tienes configurado otro
7) Selecciona el proyecto>click derecho>Run para que de se haga el deploy, esto te deberá abrir el navegador, si es así, ciérralo
8)Descarga la aplicación cliente (la cual consume las api de la aplicación web), abre la aplicación en NetBeans y da click en Run, para probar la aplicación

Para probar la app como el rol de ADMINISTRADOR o ALMACENISTA, entra la BD Inventario, y usa las credenciales de los usuarios registrados
