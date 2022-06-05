import sbt._

object Versions {
  val zioVersion    = "1.0.15"
  val pdfBoxVersion = "2.0.22"
}

object Libraries {
  import Versions._

  val zioCore = "dev.zio"          %% "zio"    % zioVersion
  val pdfBox  = "org.apache.pdfbox" % "pdfbox" % pdfBoxVersion

  val zioTestSbt = "dev.zio" %% "zio-test-sbt" % zioVersion % Test
  val zioTest    = "dev.zio" %% "zio-test"     % zioVersion % Test
}
