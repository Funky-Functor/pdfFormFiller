package com.funkyFunctor.pdfFormFiller

import com.funkyFunctor.pdfFormFiller.FormFillerException.AcroFormNotFound
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.pdmodel.interactive.form.{PDAcroForm, PDField, PDTerminalField}
import zio.{IO, UIO, ZIO}

import java.io.InputStream
import java.io.OutputStream
import scala.jdk.javaapi.CollectionConverters.asScala

object PdfFormFiller {
  def fillDocument(
      document: InputStream,
      parameters: Map[String, String],
      fields: Map[String, String]
  ): ZIO[Any, Throwable, OutputStream] = {
    ???
  }

  def openPdfDocument(inputStream: InputStream): ZIO[Any, Throwable, PDDocument] = {
    ZIO(PDDocument.load(inputStream))
  }

  def listFields(pdfDocument: PDDocument): ZIO[Any, Throwable, List[PDField]] = {
    getAcroForm(pdfDocument)
      .map(form =>
        asScala(form.getFieldIterator)
          .filter(field => field.isInstanceOf[PDTerminalField])
          .toList
      )
  }

  def getAcroForm(pdfDocument: PDDocument): IO[Throwable, PDAcroForm] = {
    val catalog  = pdfDocument.getDocumentCatalog
    val acroForm = Option(catalog.getAcroForm)

    ZIO
      .fromOption(acroForm)
      .orElseFail(AcroFormNotFound)
  }

  def replaceAllFieldValues(pdfDocument: PDDocument, fields: Map[String, String]) : UIO[PDDocument] =
    fields.foldLeft(ZIO.succeed(pdfDocument)) { (pdfZio, tuple) =>
      val (fieldFQN, fieldValue) = tuple
      pdfZio.flatMap(pdf => replaceFieldValue(pdf, fieldFQN, fieldValue))
    }

  def replaceFieldValue(pdfDocument: PDDocument, fieldFQN: String, newValue: String): UIO[PDDocument] = {
    {
      for {
        acroForm <- getAcroForm(pdfDocument)
        field    <- getField(acroForm, fieldFQN)
        _ = field.setValue(newValue)
      } yield pdfDocument
    }.fold(
      _ => pdfDocument,
      identity
    )
  }

  def getField(acroForm: PDAcroForm, fieldFQN: String): IO[Unit, PDField] = {
    val optResult = Option(acroForm.getField(fieldFQN))

    ZIO.fromOption(optResult).orElseFail()
  }
}
