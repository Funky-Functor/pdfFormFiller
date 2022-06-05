package com.funkyFunctor.pdfFormFiller

import com.funkyFunctor.pdfFormFiller.TestUtilities._
import com.funkyFunctor.pdfFormFiller.model.{FieldType, FormFieldRepresentation}
import org.apache.pdfbox.pdmodel.interactive.form.PDField
import zio._
import zio.console.putStrLn
import zio.test.Assertion._
import zio.test.environment.TestEnvironment
import zio.test.{Assertion, DefaultRunnableSpec, ZSpec, assert, assertM}

import java.io.File

object W2gTests extends DefaultRunnableSpec {
  val W2G_FILE = "w2g.pdf"

  val textFields: Map[String, FormFieldRepresentation] = List(
    "topmostSubform[0].Copy1[0].Col_Left[0].f1_02[0]",
    "topmostSubform[0].Copy1[0].Col_Left[0].f1_03[0]",
    "topmostSubform[0].Copy1[0].Col_Left[0].f1_04[0]",
    "topmostSubform[0].Copy1[0].Col_Left[0].f1_05[0]",
    "topmostSubform[0].Copy1[0].Col_Left[0].f1_06[0]",
    "topmostSubform[0].Copy1[0].Col_Left[0].f1_07[0]",
    "topmostSubform[0].Copy1[0].Col_Right[0].Box15[0].f1_22[0]",
    "topmostSubform[0].Copy1[0].Col_Right[0].Box17[0].f1_24[0]",
    "topmostSubform[0].Copy1[0].Col_Right[0].Box1[0].f1_08[0]",
    "topmostSubform[0].Copy1[0].Col_Right[0].Box7[0].f1_14[0]",
    "topmostSubform[0].Copy1[0].Col_Right[0].f1_09[0]",
    "topmostSubform[0].Copy1[0].Col_Right[0].f1_10[0]",
    "topmostSubform[0].Copy1[0].Col_Right[0].f1_11[0]",
    "topmostSubform[0].Copy1[0].Col_Right[0].f1_12[0]",
    "topmostSubform[0].Copy1[0].Col_Right[0].f1_13[0]",
    "topmostSubform[0].Copy1[0].Col_Right[0].f1_15[0]",
    "topmostSubform[0].Copy1[0].Col_Right[0].f1_16[0]",
    "topmostSubform[0].Copy1[0].Col_Right[0].f1_17[0]",
    "topmostSubform[0].Copy1[0].Col_Right[0].f1_18[0]",
    "topmostSubform[0].Copy1[0].Col_Right[0].f1_19[0]",
    "topmostSubform[0].Copy1[0].Col_Right[0].f1_20[0]",
    "topmostSubform[0].Copy1[0].Col_Right[0].f1_21[0]",
    "topmostSubform[0].Copy1[0].Col_Right[0].f1_23[0]",
    "topmostSubform[0].Copy1[0].Col_Right[0].f1_25[0]",
    "topmostSubform[0].Copy1[0].Copy1Header[0].f1_01[0]",
    "topmostSubform[0].Copy2[0].Col_Left[0].f1_02[0]",
    "topmostSubform[0].Copy2[0].Col_Left[0].f1_03[0]",
    "topmostSubform[0].Copy2[0].Col_Left[0].f1_04[0]",
    "topmostSubform[0].Copy2[0].Col_Left[0].f1_05[0]",
    "topmostSubform[0].Copy2[0].Col_Left[0].f1_06[0]",
    "topmostSubform[0].Copy2[0].Col_Left[0].f1_07[0]",
    "topmostSubform[0].Copy2[0].Col_Right[0].Box15[0].f1_22[0]",
    "topmostSubform[0].Copy2[0].Col_Right[0].Box17[0].f1_24[0]",
    "topmostSubform[0].Copy2[0].Col_Right[0].Box1[0].f1_08[0]",
    "topmostSubform[0].Copy2[0].Col_Right[0].Box7[0].f1_14[0]",
    "topmostSubform[0].Copy2[0].Col_Right[0].f1_09[0]",
    "topmostSubform[0].Copy2[0].Col_Right[0].f1_10[0]",
    "topmostSubform[0].Copy2[0].Col_Right[0].f1_11[0]",
    "topmostSubform[0].Copy2[0].Col_Right[0].f1_12[0]",
    "topmostSubform[0].Copy2[0].Col_Right[0].f1_13[0]",
    "topmostSubform[0].Copy2[0].Col_Right[0].f1_15[0]",
    "topmostSubform[0].Copy2[0].Col_Right[0].f1_16[0]",
    "topmostSubform[0].Copy2[0].Col_Right[0].f1_17[0]",
    "topmostSubform[0].Copy2[0].Col_Right[0].f1_18[0]",
    "topmostSubform[0].Copy2[0].Col_Right[0].f1_19[0]",
    "topmostSubform[0].Copy2[0].Col_Right[0].f1_20[0]",
    "topmostSubform[0].Copy2[0].Col_Right[0].f1_21[0]",
    "topmostSubform[0].Copy2[0].Col_Right[0].f1_23[0]",
    "topmostSubform[0].Copy2[0].Col_Right[0].f1_25[0]",
    "topmostSubform[0].Copy2[0].Copy2Header[0].f1_01[0]",
    "topmostSubform[0].CopyA[0].Col_Left[0].f1_02[0]",
    "topmostSubform[0].CopyA[0].Col_Left[0].f1_03[0]",
    "topmostSubform[0].CopyA[0].Col_Left[0].f1_04[0]",
    "topmostSubform[0].CopyA[0].Col_Left[0].f1_05[0]",
    "topmostSubform[0].CopyA[0].Col_Left[0].f1_06[0]",
    "topmostSubform[0].CopyA[0].Col_Left[0].f1_07[0]",
    "topmostSubform[0].CopyA[0].Col_Right[0].Box15[0].f1_22[0]",
    "topmostSubform[0].CopyA[0].Col_Right[0].Box17[0].f1_24[0]",
    "topmostSubform[0].CopyA[0].Col_Right[0].Box1[0].f1_08[0]",
    "topmostSubform[0].CopyA[0].Col_Right[0].Box7[0].f1_14[0]",
    "topmostSubform[0].CopyA[0].Col_Right[0].f1_09[0]",
    "topmostSubform[0].CopyA[0].Col_Right[0].f1_10[0]",
    "topmostSubform[0].CopyA[0].Col_Right[0].f1_11[0]",
    "topmostSubform[0].CopyA[0].Col_Right[0].f1_12[0]",
    "topmostSubform[0].CopyA[0].Col_Right[0].f1_13[0]",
    "topmostSubform[0].CopyA[0].Col_Right[0].f1_15[0]",
    "topmostSubform[0].CopyA[0].Col_Right[0].f1_16[0]",
    "topmostSubform[0].CopyA[0].Col_Right[0].f1_17[0]",
    "topmostSubform[0].CopyA[0].Col_Right[0].f1_18[0]",
    "topmostSubform[0].CopyA[0].Col_Right[0].f1_19[0]",
    "topmostSubform[0].CopyA[0].Col_Right[0].f1_20[0]",
    "topmostSubform[0].CopyA[0].Col_Right[0].f1_21[0]",
    "topmostSubform[0].CopyA[0].Col_Right[0].f1_23[0]",
    "topmostSubform[0].CopyA[0].Col_Right[0].f1_25[0]",
    "topmostSubform[0].CopyA[0].CopyAHeader[0].f1_01[0]",
    "topmostSubform[0].CopyB[0].Col_Left[0].f1_02[0]",
    "topmostSubform[0].CopyB[0].Col_Left[0].f1_03[0]",
    "topmostSubform[0].CopyB[0].Col_Left[0].f1_04[0]",
    "topmostSubform[0].CopyB[0].Col_Left[0].f1_05[0]",
    "topmostSubform[0].CopyB[0].Col_Left[0].f1_06[0]",
    "topmostSubform[0].CopyB[0].Col_Left[0].f1_07[0]",
    "topmostSubform[0].CopyB[0].Col_Right[0].Box15[0].f1_22[0]",
    "topmostSubform[0].CopyB[0].Col_Right[0].Box17[0].f1_24[0]",
    "topmostSubform[0].CopyB[0].Col_Right[0].Box1[0].f1_08[0]",
    "topmostSubform[0].CopyB[0].Col_Right[0].Box7[0].f1_14[0]",
    "topmostSubform[0].CopyB[0].Col_Right[0].f1_09[0]",
    "topmostSubform[0].CopyB[0].Col_Right[0].f1_10[0]",
    "topmostSubform[0].CopyB[0].Col_Right[0].f1_11[0]",
    "topmostSubform[0].CopyB[0].Col_Right[0].f1_12[0]",
    "topmostSubform[0].CopyB[0].Col_Right[0].f1_13[0]",
    "topmostSubform[0].CopyB[0].Col_Right[0].f1_15[0]",
    "topmostSubform[0].CopyB[0].Col_Right[0].f1_16[0]",
    "topmostSubform[0].CopyB[0].Col_Right[0].f1_17[0]",
    "topmostSubform[0].CopyB[0].Col_Right[0].f1_18[0]",
    "topmostSubform[0].CopyB[0].Col_Right[0].f1_19[0]",
    "topmostSubform[0].CopyB[0].Col_Right[0].f1_20[0]",
    "topmostSubform[0].CopyB[0].Col_Right[0].f1_21[0]",
    "topmostSubform[0].CopyB[0].Col_Right[0].f1_23[0]",
    "topmostSubform[0].CopyB[0].Col_Right[0].f1_25[0]",
    "topmostSubform[0].CopyB[0].CopyBHeader[0].f1_01[0]",
    "topmostSubform[0].CopyC[0].Col_Left[0].f1_02[0]",
    "topmostSubform[0].CopyC[0].Col_Left[0].f1_03[0]",
    "topmostSubform[0].CopyC[0].Col_Left[0].f1_04[0]",
    "topmostSubform[0].CopyC[0].Col_Left[0].f1_05[0]",
    "topmostSubform[0].CopyC[0].Col_Left[0].f1_06[0]",
    "topmostSubform[0].CopyC[0].Col_Left[0].f1_07[0]",
    "topmostSubform[0].CopyC[0].Col_Right[0].Box15[0].f1_22[0]",
    "topmostSubform[0].CopyC[0].Col_Right[0].Box17[0].f1_24[0]",
    "topmostSubform[0].CopyC[0].Col_Right[0].Box1[0].f1_08[0]",
    "topmostSubform[0].CopyC[0].Col_Right[0].Box7[0].f1_14[0]",
    "topmostSubform[0].CopyC[0].Col_Right[0].f1_09[0]",
    "topmostSubform[0].CopyC[0].Col_Right[0].f1_10[0]",
    "topmostSubform[0].CopyC[0].Col_Right[0].f1_11[0]",
    "topmostSubform[0].CopyC[0].Col_Right[0].f1_12[0]",
    "topmostSubform[0].CopyC[0].Col_Right[0].f1_13[0]",
    "topmostSubform[0].CopyC[0].Col_Right[0].f1_15[0]",
    "topmostSubform[0].CopyC[0].Col_Right[0].f1_16[0]",
    "topmostSubform[0].CopyC[0].Col_Right[0].f1_17[0]",
    "topmostSubform[0].CopyC[0].Col_Right[0].f1_18[0]",
    "topmostSubform[0].CopyC[0].Col_Right[0].f1_19[0]",
    "topmostSubform[0].CopyC[0].Col_Right[0].f1_20[0]",
    "topmostSubform[0].CopyC[0].Col_Right[0].f1_21[0]",
    "topmostSubform[0].CopyC[0].Col_Right[0].f1_23[0]",
    "topmostSubform[0].CopyC[0].Col_Right[0].f1_25[0]",
    "topmostSubform[0].CopyC[0].CopyCHeader[0].f1_01[0]",
    "topmostSubform[0].CopyD[0].Col_Left[0].f1_02[0]",
    "topmostSubform[0].CopyD[0].Col_Left[0].f1_03[0]",
    "topmostSubform[0].CopyD[0].Col_Left[0].f1_04[0]",
    "topmostSubform[0].CopyD[0].Col_Left[0].f1_05[0]",
    "topmostSubform[0].CopyD[0].Col_Left[0].f1_06[0]",
    "topmostSubform[0].CopyD[0].Col_Left[0].f1_07[0]",
    "topmostSubform[0].CopyD[0].Col_Right[0].Box15[0].f1_22[0]",
    "topmostSubform[0].CopyD[0].Col_Right[0].Box17[0].f1_24[0]",
    "topmostSubform[0].CopyD[0].Col_Right[0].Box1[0].f1_08[0]",
    "topmostSubform[0].CopyD[0].Col_Right[0].Box7[0].f1_14[0]",
    "topmostSubform[0].CopyD[0].Col_Right[0].f1_09[0]",
    "topmostSubform[0].CopyD[0].Col_Right[0].f1_10[0]",
    "topmostSubform[0].CopyD[0].Col_Right[0].f1_11[0]",
    "topmostSubform[0].CopyD[0].Col_Right[0].f1_12[0]",
    "topmostSubform[0].CopyD[0].Col_Right[0].f1_13[0]",
    "topmostSubform[0].CopyD[0].Col_Right[0].f1_15[0]",
    "topmostSubform[0].CopyD[0].Col_Right[0].f1_16[0]",
    "topmostSubform[0].CopyD[0].Col_Right[0].f1_17[0]",
    "topmostSubform[0].CopyD[0].Col_Right[0].f1_18[0]",
    "topmostSubform[0].CopyD[0].Col_Right[0].f1_19[0]",
    "topmostSubform[0].CopyD[0].Col_Right[0].f1_20[0]",
    "topmostSubform[0].CopyD[0].Col_Right[0].f1_21[0]",
    "topmostSubform[0].CopyD[0].Col_Right[0].f1_23[0]",
    "topmostSubform[0].CopyD[0].Col_Right[0].f1_25[0]",
    "topmostSubform[0].CopyD[0].CopyDHeader[0].f1_01[0]"
  ).map(str => str -> FormFieldRepresentation(str, FieldType.Text, "")).toMap

  val buttonFields: Map[String, FormFieldRepresentation] = List(
    "topmostSubform[0].Copy1[0].Copy1Header[0].c1_1[0]",
    "topmostSubform[0].Copy1[0].Copy1Header[0].c1_1[1]",
    "topmostSubform[0].Copy2[0].Copy2Header[0].c1_1[0]",
    "topmostSubform[0].CopyA[0].CopyAHeader[0].c1_1[0]",
    "topmostSubform[0].CopyA[0].CopyAHeader[0].c1_1[1]",
    "topmostSubform[0].CopyB[0].CopyBHeader[0].c1_1[0]",
    "topmostSubform[0].CopyC[0].CopyCHeader[0].c1_1[0]",
    "topmostSubform[0].CopyD[0].CopyDHeader[0].c1_1[0]",
    "topmostSubform[0].CopyD[0].CopyDHeader[0].c1_1[1]"
  ).map(str => str -> FormFieldRepresentation(str, FieldType.Button, "Off")).toMap

  val fieldMap: Map[String, FormFieldRepresentation] = textFields ++ buttonFields

  def spec: ZSpec[TestEnvironment, Any] = suite("Testing against the W2-G form")(
    test01,
    test02
  )

  private val test01 = testM("Read data from the classpath and return the expected list of fields in the document") {
    val fields: ZIO[Any, Throwable, List[PDField]] = Managed.fromAutoCloseable(loadFromClasspath(W2G_FILE)).use { is =>
      Managed.fromAutoCloseable(PdfFormFiller.openPdfDocument(is)).use {
        PdfFormFiller.listFields
      }
    }

    val nameList = fields.map(list => list.flatMap(f => FormFieldRepresentation(f)).sortBy(_.fullyQualifiedName))

    nameList.map(resultNameList => assert(resultNameList)(hasSameElements(fieldMap.values)))
  }

  private val test02 = testM("Fill the form with the expected values") {
    val txtFields: Map[String, String] = textFields.keys.toList.sorted.zipWithIndex.map { case (k, v) =>
      k -> v.toString
    }.toMap

    Managed.fromAutoCloseable(loadFromClasspath(W2G_FILE)).use { is =>
      Managed.fromAutoCloseable(PdfFormFiller.openPdfDocument(is)).use { pdfDocument =>
        assertM {
          for {
            resultingDocument <- PdfFormFiller.replaceAllFieldValues(pdfDocument, txtFields)
            outputFile        <- TestUtilities.saveDocumentToTempFile(resultingDocument)
            _                 <- putStrLn(s"File written to $outputFile")
          } yield ()
        }(Assertion.isUnit)
      }
    }
  }
}
