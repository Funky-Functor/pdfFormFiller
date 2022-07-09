package com.funkyFunctor.pdfFormFiller

import com.funkyFunctor.pdfFormFiller.TestUtilities._
import com.funkyFunctor.pdfFormFiller.model.{FormFieldRepresentation, FormParameters, LocalData, Payer, Winner, Winning}
import org.apache.pdfbox.pdmodel.interactive.form.PDField
import zio._
import zio.test.Assertion._
import zio.test.environment.TestEnvironment
import zio.test.{Assertion, DefaultRunnableSpec, TestResult, ZSpec, assert, assertM}

import java.io.ByteArrayInputStream
import java.nio.file.{Files, Paths, StandardCopyOption}
import java.time.LocalDate

object W2gTests extends DefaultRunnableSpec {
//  val W2G_FILE = "w2g.pdf"
//
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
  ).map(str => str -> FormFieldRepresentation(str, "Tx", "")).toMap

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
  ).map(str => str -> FormFieldRepresentation(str, "Btn", "Off")).toMap

  val fieldMap: Map[String, FormFieldRepresentation] = textFields ++ buttonFields

  def spec: ZSpec[TestEnvironment, Any] = suite("Testing against the W2-G form")(
    test01,
    test02
  )

  private val test01 = testM("Read data from the classpath and return the expected list of fields in the document") {
    val fields: ZIO[Any, Throwable, List[PDField]] = Managed
      .fromAutoCloseable(W2G.pdfTemplate)
      .use { is =>
        PdfFormFiller
          .openPdfDocument(is)
          .flatMap(PdfFormFiller.listFields)
      }

    val nameList = fields.map(list => list.map(f => FormFieldRepresentation(f)).sortBy(_.fullyQualifiedName))

    nameList.map(resultNameList => assert(resultNameList)(hasSameElements(fieldMap.values)))
  }

  private val test02 = testM("Fill the form with the expected values") {
    Managed.fromAutoCloseable(W2G.pdfTemplate).use { is =>
      val formInput: FormParameters = FormParameters(
        payerInformation = Payer(
          federalIdentificationNumber = "payerID",
          telNumber = Some("(123) 456-7890"),
          name = "Mr Payer",
          address = """line1
              |line 2
              |line 3
              |Payer City, NY 12345
              |""".stripMargin
        ),
        winnerInformation = Winner(
          name = "Mrs Winner",
          streetAddress = """line1
              |line2
              |""".stripMargin,
          cityAndZipAddress = "Winner City, NJ 67890",
          fedTaxIdentifier = "payerSSN",
          stateIdentifier = None,
          firstIdentification = Some("Driver Licence"),
          secondIdentification = Some("Passport")
        ),
        winning = Winning(
          reportableAmount = 1000L,
          dateWon = LocalDate.now(),
          typeOfWager = "Sport bet",
          federalTaxWithholdings = 0L,
          transaction = None,
          race = None,
          winningsFromIdenticalWagers = 0L,
          cashier = None,
          window = None
        ),
        local = LocalData(
          0L,
          0L,
          0L,
          0L,
          "Sunny Town"
        ),
        calendarYear = "22"
      )
      val parameters: Map[String, String] = formInput.toMap()
      val fields: Map[String, String]     = W2G.fieldsMapping

      val result = PdfFormFiller
        .fillDocument(is, parameters, fields)
        .flatMap { ba =>
          val bais            = new ByteArrayInputStream(ba)
          val destinationFile = Paths.get("""D:\Work\FunkyFunctor\PdfFormFiller\output.pdf""")
          ZIO(Files.copy(bais, destinationFile, StandardCopyOption.REPLACE_EXISTING))
        }
        .unit

      assertM(result)(Assertion.isUnit)
//      val txtFields: Map[String, String] = textFields.keys.toList.sorted.zipWithIndex.map { case (k, v) =>
//        System.out.println(s"'$k' | $v")
//        k -> v.toString
//      }.toMap
//
//      val pdfDocument = PdfFormFiller.openPdfDocument(is)
//      val resultingDocument = pdfDocument.flatMap(PdfFormFiller.replaceAllFieldValues(_, txtFields)).map { doc =>
//        doc.save("""D:\Work\FunkyFunctor\PdfFormFiller\output.pdf""")
//        doc.close()
//      }
//
//      assertM(resultingDocument)(Assertion.isUnit)
    }
  }
}
