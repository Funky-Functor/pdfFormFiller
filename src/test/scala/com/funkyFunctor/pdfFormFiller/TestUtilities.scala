package com.funkyFunctor.pdfFormFiller

import org.apache.pdfbox.pdmodel.PDDocument
import zio.ZIO

import java.io.{File, InputStream}

object TestUtilities {
  def loadFromClasspath(path: String): ZIO[Any, Throwable, InputStream] = {
    ZIO(getClass.getClassLoader.getResourceAsStream(path))
  }

  def saveDocumentToTempFile(pdfDocument: PDDocument, outputFile: File = File.createTempFile("output", ".pdf")) = {
    for {
      _ <- ZIO(pdfDocument.save(outputFile))
    } yield outputFile.getAbsolutePath
  }
}
