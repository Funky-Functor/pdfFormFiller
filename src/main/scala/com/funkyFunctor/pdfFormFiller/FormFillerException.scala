package com.funkyFunctor.pdfFormFiller

sealed abstract class FormFillerException(msg: String)
    extends Exception(msg) {
  val nestedException: Option[Throwable] = None
}

object FormFillerException {
  case object AcroFormNotFound extends FormFillerException("No form found in PDF document")
}
