package com.funkyFunctor.pdfFormFiller

import com.funkyFunctor.pdfFormFiller.FormFillerException.AcroFormNotFound
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.pdmodel.interactive.form.{PDAcroForm, PDField, PDTerminalField}
import zio.{IO, Managed, UIO, ZIO}

import java.io.{ByteArrayOutputStream, InputStream}
import java.util.Collections
import scala.jdk.javaapi.CollectionConverters.asScala

object PdfFormFiller {
  def fillDocument(
      document: InputStream,
      parameters: Map[String, String], // What we are receiving from the app
      fields: Map[String, String], // List of all fields in the pdf: key is the name of the field in the document and value is the name of the parameter key
      defaultValue: String = ""
  ): ZIO[Any, Throwable, Array[Byte]] = {
    val reportValues = mergeFieldsAndParameters(parameters, fields, defaultValue)

    Managed.fromAutoCloseable(ZIO(new ByteArrayOutputStream())).use { baos =>
      for {
        doc                <- openPdfDocument(document)
        resultingDocument  <- replaceAllFieldValues(doc, reportValues)
        resultingDocument2 <- makeReadOnly(resultingDocument)
        _                  <- ZIO(resultingDocument2.save(baos))
      } yield baos.toByteArray
    }
  }

  def mergeFieldsAndParameters(
      parameters: Map[String, String],
      fields: Map[String, String],
      defaultValue: String
  ): Map[String, String] = {
    fields.view.mapValues { paramName =>
      parameters.getOrElse(paramName, defaultValue)
    }.toMap
  }

  def openPdfDocument(inputStream: InputStream): ZIO[Any, Throwable, PDDocument] = {
    ZIO(PDDocument.load(inputStream))
  }

  def makeReadOnly(pdfDocument: PDDocument): ZIO[Any, Throwable, PDDocument] = {
    if (pdfDocument.isEncrypted) {
      pdfDocument.setAllSecurityToBeRemoved(true)
    } else {
      System.err.println("Document is not encrypted - skipping encryption removal")
    }
    ZIO.succeed(pdfDocument)
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

  def replaceAllFieldValues(pdfDocument: PDDocument, fields: Map[String, String]): UIO[PDDocument] =
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
        _ = acroForm.flatten(Collections.singletonList(field), true)
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
