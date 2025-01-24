
import cats.effect._
import cats.effect.unsafe.implicits.global
import doobie._
import doobie.implicits._

object StudentsQuery {


  def listAllStudents(): ConnectionIO[List[Students]] =
    sql"SELECT nombre, edad, calificacion, genero FROM STUDENTS"
      .query[Students]
      .to[List]

  // NUEVA FUNCIÓN: Insertar un nuevo estudiante
  def insertStudent( nombre : String, edad : Int, calificacion : Int, genero : String): ConnectionIO[Int] =
    sql"""
          INSERT INTO STUDENTS (nombre, edad, calificacion, genero)
          VALUES ($nombre, $edad, $calificacion, $genero)
        """.update.run




}
