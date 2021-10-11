package com.evg

import org.scalatest.FunSuite

import scala.io.Source

class LogFileParserSpec extends FunSuite {

  test("Json parsed correctly and filename retrieved") {
    val fileName = Source.fromURL(getClass.getResource("/testInput.txt"))
    val parser = new LogFileParser

    val filenames = fileName.getLines().map(line => parser.getFileName(parser.parseLine(line))).toSeq
    assert(filenames == List(
      Some("phkkrw.ext"), Some("asdf.pdf"), Some("phkkrw.ext"), Some("phkkrw.ext"), Some("phkkrw"), Some("phkkrw")))
  }

  test("Json parsed correctly for empty filenames") {
    val fileName = Source.fromURL(getClass.getResource("/testInput3.txt"))
    val parser = new LogFileParser

    val filenames = fileName.getLines().map(line => parser.getFileName(parser.parseLine(line))).toSeq
    assert(filenames == List(
      Some(""), Some(""), Some("")))
  }

  test("The input log file parsed correctly and counted") {
    val fileName = Source.fromURL(getClass.getResource("/testInput.txt"))
    val parser = new LogFileParser

    val handle = parser.parseAndHandle(fileName)
    val result = parser.countFilesWithExtensions(handle)

    val expected = Seq("ext : 3", "pdf : 1", "no_extension : 2")
    assert(result == expected)
  }

  test("Filenames without extension counted correctly") {
    val fileName = Source.fromURL(getClass.getResource("/testInput2.txt"))
    val parser = new LogFileParser

    val handle = parser.parseAndHandle(fileName)
    val result = parser.countFilesWithExtensions(handle)

    val expected = Seq("no_extension : 3")
    assert(result == expected)
  }

  test("The map added data correctly") {
    val parser = new LogFileParser

    val empty: Map[String, List[String]] = Map.empty[String, List[String]]
    val newOne: Map[String, List[String]] = parser.addValue(empty, ("one", "value"))
    val newTwo: Map[String, List[String]] = parser.addValue(newOne, ("one", "two"))//newOne + ("one" -> List("value"))
    val newThree: Map[String, List[String]] = parser.addValue(newTwo, ("three", "Zozo"))//newTwo + ("three" -> List("value"))
    assert(empty == Map.empty[String, List[String]])
    assert(newOne == Map(("one", List("value"))))
    assert(newTwo == Map(("one", List("two", "value"))))
    assert(newThree == Map(("one", List("two", "value")), ("three", List("Zozo"))))
    assert(parser.addValue(empty, ("no_extension", "value")) == Map(("no_extension", List("value"))))
  }


}
