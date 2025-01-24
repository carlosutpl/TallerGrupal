import ReadStudents.readCSV
import StudentsQuery.{insertStudent, listAllStudents}
import cats.effect.unsafe.implicits.global
import cats.effect.{IO, Resource}
import com.typesafe.config.ConfigFactory
import doobie.hikari.HikariTransactor
import doobie.implicits._

import scala.concurrent.ExecutionContext

object StudentMain extends App{
  private val pathStudents = "C:\\Users\\V I C T U S\\IdeaProjects\\StudentsDatabase\\files\\students.csv"
  private val connectEC: ExecutionContext = ExecutionContext.global

  // Configurar transactor para students:
  def transactor: Resource[IO, HikariTransactor[IO]] = {
    val config = ConfigFactory.load().getConfig("db")
    HikariTransactor.newHikariTransactor[
      IO
    ](
      config.getString("driver"),
      config.getString("url"),
      config.getString("user"),
      config.getString("password"),
      connectEC // ExecutionContext requerido para Doobie
    )

  }


  // Leer el archivo .csv
//  val studentsList = readCSV(pathStudents)
//  studentsList.foreach(current => println(s"🔥" + current))
//
//  //  // Iterar por toda la colección del .csv y añadir a la database:
//    studentsList.foreach{ current =>
//      transactor.use { xa =>
//         insertStudent(current.nombre, current.edad, current.calificacion, current.genero).transact(xa)
//      }.unsafeRunSync()
//    }

  //Leer csv:
  // Leer la tabla Students:
  // Consulta de todos los registros de películas
  transactor.use { xa =>
    listAllStudents().transact(xa).flatMap { result =>
      IO(println(s"🔥🔥🔥Imprimiendo los resultados: " + result))
    }
  }.unsafeRunSync()

}
