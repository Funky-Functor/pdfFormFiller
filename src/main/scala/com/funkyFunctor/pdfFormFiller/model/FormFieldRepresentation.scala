package com.funkyFunctor.pdfFormFiller.model

import org.apache.pdfbox.pdmodel.interactive.form.PDField

sealed trait FieldType

object FieldType {
  case object Text extends FieldType {
    override val toString: String = "Tx"
  }
  case object Button extends FieldType {
    override val toString: String = "Btn"
  }

  def apply(str: String): Option[FieldType] = str match {
    case Text.toString   => Some(Text)
    case Button.toString => Some(Button)
    case _               => None
  }
}

case class FormFieldRepresentation(
    fullyQualifiedName: String,
    fieldType: FieldType,
    initialValue: String,
    newValue: Option[String] = None
)

object FormFieldRepresentation {
  def apply(field: PDField): Option[FormFieldRepresentation] = {
    FieldType(field.getFieldType).map { ft =>
      val fqn = field.getFullyQualifiedName
      val iv  = field.getValueAsString

      FormFieldRepresentation(fqn, ft, iv)
    }
  }
}
