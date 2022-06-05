package com.funkyFunctor.pdfFormFiller.model

import org.apache.pdfbox.pdmodel.interactive.form.PDField

case class FormFieldRepresentation(fullyQualifiedName: String, fieldType: String, initialValue: String)

object FormFieldRepresentation{
  def apply(field: PDField): FormFieldRepresentation = {
    val fqn = field.getFullyQualifiedName
    val ft = field.getFieldType
    val iv = field.getValueAsString

    FormFieldRepresentation(fqn, ft, iv)
  }
}