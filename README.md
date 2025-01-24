# TallerGrupal
Taller grupal por: Carlos Sánchez, John Calle, Marco Abarca

## Wiki: Conectando Scala a Bases de Datos SQL

### Introducción

Hola! En esta guia, te voy a mostrar cómo conectar aplicaciones de Scala a bases de datos SQL, específicamente enfocándonos en MySQL. Vamos a cubrir todo, desde la configuración de tu entorno, el establecimiento de una conexión con la base de datos, la ejecución de consultas SQL y la gestión efectiva de los recursos de la base de datos.

### Requisitos Previos

Antes de comenzar, asegúrate de tener lo siguiente:

- **Scala**: Necesitas tener Scala instalado en tu máquina. Puedes descargarlo desde el [sitio oficial de Scala](https://www.scala-lang.org/download/).
- **SBT**: Instala SBT (Scala Build Tool) para gestionar las dependencias. Las instrucciones se pueden encontrar en el [sitio web de SBT](https://www.scala-sbt.org/download.html).
- **Base de Datos MySQL**: Asegúrate de tener un servidor MySQL en funcionamiento. Puedes descargarlo desde el [sitio web de MySQL](https://dev.mysql.com/downloads/mysql/).

### Paso 1: Configurando Tu Proyecto

1. **Crea un Nuevo Proyecto SBT**:
   Primero, crea un nuevo directorio para tu proyecto y navega dentro de él:
   ```bash
   mkdir scala-sql-example
   cd scala-sql-example
   ```

2. **Inicializa SBT**:
   Crea un archivo llamado `build.sbt` en la raíz del proyecto con el siguiente contenido:
   ```scala
   name := "ScalaSqlExample"

   version := "0.1"

   scalaVersion := "2.13.8"

   libraryDependencies ++= Seq(
     "org.scalatest" %% "scalatest" % "3.2.9" % Test,
     "mysql" % "mysql-connector-java" % "8.0.26",
     "org.tpolecat" %% "doobie-core" % "1.0.0",
     "org.tpolecat" %% "doobie-hikari" % "1.0.0"
   )
   ```

### Paso 2: Estableciendo una Conexión con la Base de Datos

1. **Crea el Objeto de Conexión a la Base de Datos**:
   En `src/main/scala/EstudiantesExerc/Database.scala`, crea un objeto para gestionar la conexión a la base de datos:
   ```scala
   package EstudiantesExerc

   import cats.effect.{IO, Resource}
   import doobie._
   import doobie.hikari.HikariTransactor
   import doobie.implicits._
   import cats.effect.ExecutionContexts

   object Database {
     def transactor: Resource[IO, Transactor[IO]] = for {
       ce <- ExecutionContexts.fixedThreadPool[IO](32)
       tx <- HikariTransactor.newHikariTransactor[IO](
         "com.mysql.cj.jdbc.Driver",
         "jdbc:mysql://localhost:3306/nombre_de_tu_base_de_datos", // Cambia a tu nombre de base de datos
         "tu_usuario", // Cambia a tu nombre de usuario MySQL
         "tu_contraseña", // Cambia a tu contraseña MySQL
         ce
       )
     } yield tx
   }
   ```

### Paso 3: Ejecutando Consultas SQL

1. **Insertar Datos en la Base de Datos**:
   Agrega un método para insertar datos en tu base de datos:
   ```scala
   def insertEstudiante(estudiante: Estudiante)(implicit tx: Transactor[IO]): IO[Int] = {
     sql"INSERT INTO estudiantes(nombre, edad, calificacion, genero) VALUES (${estudiante.nombre}, ${estudiante.edad}, ${estudiante.calificacion}, ${estudiante.genero})"
       .update.run.transact(tx)
   }
   ```

2. **Obtener Datos de la Base de Datos**:
   Agrega un método para obtener datos de tu base de datos:
   ```scala
   def fetchAllEstudiantes(implicit tx: Transactor[IO]): IO[List[(Int, String, Int, Int, Char)]] = {
     sql"SELECT * FROM estudiantes".query[(Int, String, Int, Int, Char)].to[List].transact(tx)
   }
   ```

### Paso 4: Ejecutando Tu Aplicación

1. **Crea el Archivo Principal de la Aplicación**:
   En `src/main/scala/EstudiantesExerc/Main.scala`, configura la lógica principal de tu aplicación:
   ```scala
   package EstudiantesExerc

   import cats.effect.{IO, Resource}
   import cats.effect.unsafe.implicits.global

   object Main extends App {
     val transactorResource = Database.transactor

     val (tx, cleanup) = transactorResource.allocated.unsafeRunSync()

     try {
       // Ejemplo de insertar un Estudiante
       val estudiante = Estudiante("Andrés", 10, 20, 'M')
       Database.insertEstudiante(estudiante)(tx).unsafeRunSync()
       println(s"Inserido: ${estudiante.nombre}")

       // Obtener e imprimir todos los estudiantes desde la base de datos
       val allEstudiantes = Database.fetchAllEstudiantes(tx).unsafeRunSync()
       allEstudiantes.foreach { case (id, nombre, edad, calificacion, genero) =>
         println(s"ID: $id | Nombre: $nombre | Edad: $edad | Calificacion: $calificacion | Genero: $genero")
       }

     } finally {
       cleanup.unsafeRunSync()
     }
   }
   ```

### Paso 5: Ejecutando Tu Proyecto

- Usa SBT para compilar y ejecutar tu proyecto:
  ```bash
  sbt run
  ```

### Conclusiones

Siguiendo esta guía, habrás configurado con éxito una aplicación Scala que se conecta a una base de datos MySQL utilizando Doobie para manejar operaciones SQL. Ahora puedes expandir esta base agregando consultas más complejas y funcionalidades según sea necesario.

### Recursos Adicionales

- [Documentación de Doobie](https://tpolecat.github.io/doobie/)
- [Documentación del Lenguaje Scala](https://docs.scala-lang.org/)
- [Documentación de MySQL](https://dev.mysql.com/doc/)

Espero que esta wiki te haya sido útil y que ahora te sientas más cómodo integrando bases de datos SQL con aplicaciones Scala. Si tienes alguna pregunta o necesitas más asistencia, no dudes en preguntar. ¡Buena suerte!
