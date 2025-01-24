/*
Author: @marco,jhon,carlos
 */


import kantan.csv.ops.{toCsvInputOps, toCsvOutputOps}
import kantan.csv.rfc
import kantan.csv._
import kantan.csv.ops._
import kantan.csv.generic._
import java.io.File


/*
  Leer temperaturas del .csv proporcionado con la case-class Students
  PATH: C:\Users\V I C T U S\IdeaProjects\StudentsDatabase\files\students.csv
 */
object ReadStudents extends App{
  def readCSV(pathFile : String) : List[Students] = {
    // Configurar lectura del CSV con delimitador ';'
    val dataSource = new File(pathFile)
      .readCsv[List, Students](rfc.withHeader.withCellSeparator(','))

    // Filtrar filas válidas
    dataSource.collect { case Right(students) => students}
  }

  println(readCSV("C:\\Users\\V I C T U S\\IdeaProjects\\StudentsDatabase\\files\\students.csv"))
}

