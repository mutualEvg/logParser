package com.evg

import io.circe
import io.circe.Json
import io.circe.optics.JsonPath.root

import scala.io.BufferedSource

class LogFileParser {

  def getFileName(json: Json): Option[String] =
    root
      .nm
      .as[String]
      .getOption(json)

  def parseAndHandle(lines: BufferedSource): Map[String, List[String]] = {
    lines.getLines().foldLeft(Map.empty[String, List[String]]) { (map, line) => {
      val parseResult: Json = parseLine(line)
      getFileName(parseResult) match {
        case Some(fNameExt) => {
          val fileNameWithExt = fNameExt.split("\\.")
          val fNameExtChecked: Array[String] = fileNameWithExt.length match {
            case 0 => Array(fNameExt)
            case _ => fileNameWithExt
          }
          fNameExtChecked.length match {
            case 1 => addValue(map, ("no_extension" -> fNameExtChecked.head))
            case 2 => addValue(map, (fNameExtChecked.tail.head -> fNameExtChecked.head))
            case _ => map
          }
        }
        case None => map // case with no filename
      }
    }
    }
  }

  def parseLine(line: String): Json = {
    circe.parser.parse(line).getOrElse(Json.Null)
  }

  def addValue(map: Map[String, List[String]], value: (String, String)): Map[String, List[String]] = {
    map.get(value._1) match {
      case Some(_) => map + (value._1 -> (value._2 :: map.get(value._1).get))
      case None => map + (value._1 -> List(value._2))
    }
  }

  def countFilesWithExtensions(map: Map[String, List[String]]): Seq[String] = {
    map.map { entry =>
      s"${entry._1} : ${entry._2.toSet.size}"
    }.toSeq
  }
}
