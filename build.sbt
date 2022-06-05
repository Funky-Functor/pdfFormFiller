import Libraries._

ThisBuild / scalaVersion     := "2.13.8"
ThisBuild / version          := "0.1.0-SNAPSHOT"
ThisBuild / organization     := "com.funkyfunctor.pdfFormFiller"
ThisBuild / organizationName := "example"

lazy val root = (project in file("."))
  .settings(
    name := "PdfFormFiller",
    libraryDependencies ++= Seq(
      zioCore,
      pdfBox,
      zioTest,
      zioTestSbt
    ),
    testFrameworks += new TestFramework("zio.test.sbt.ZTestFramework")
  )
