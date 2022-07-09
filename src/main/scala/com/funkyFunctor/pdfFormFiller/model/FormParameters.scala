package com.funkyFunctor.pdfFormFiller.model

import java.time.LocalDate

object FormParameters {
  val PAYER_ADDRESS                  = "PAYER_ADDRESS"
  val PAYER_TAX_ID                   = "PAYER_TAX_ID"
  val PAYER_TEL_NB                   = "PAYER_TEL_NB"
  val WINNER_NAME                    = "WINNER_NAME"
  val WINNER_STREET_ADDRESS          = "WINNER_STREET_ADDRESS"
  val WINNER_CITY_TOWN               = "WINNER_CITY_TOWN"
  val REPORTABLE_WINNINGS            = "REPORTABLE_WINNINGS"
  val DATE_WON                       = "DATE_WON"
  val WAGER_TYPE                     = "WAGER_TYPE"
  val FEDERAL_TAX_WITHHELD           = "FEDERAL_TAX_WITHHELD"
  val TRANSACTION                    = "TRANSACTION"
  val RACE                           = "RACE"
  val WINNINGS_FROM_IDENTICAL_WAGERS = "WINNINGS_FROM_IDENTICAL_WAGERS"
  val CASHIER                        = "CASHIER"
  val WINNER_SSN                     = "WINNER_SSN"
  val WINDOW                         = "WINDOW"
  val FIRST_ID                       = "FIRST_ID"
  val SECOND_ID                      = "SECOND_ID"
  val STATE_ID_NO                    = "STATE_ID_NO"
  val STATE_WINNINGS                 = "STATE_WINNINGS"
  val STATE_TAX_WITHHELD             = "STATE_TAX_WITHHELD"
  val LOCAL_WINNINGS                 = "LOCAL_WINNINGS"
  val LOCAL_TAX_WITHHELD             = "LOCAL_TAX_WITHHELD"
  val LOCALITY_NAME                  = "LOCALITY_NAME"
  val CALENDAR_YEAR                  = "CALENDAR_YEAR"
}

case class FormParameters(
    payerInformation: Payer,
    winnerInformation: Winner,
    winning: Winning,
    local: LocalData,
    calendarYear: String
) {
  import FormParameters._

  def toMap(defaultValue: String = ""): Map[String, String] = Map(
    PAYER_ADDRESS                  -> (payerInformation.name + System.lineSeparator() + payerInformation.address),
    PAYER_TAX_ID                   -> payerInformation.federalIdentificationNumber,
    PAYER_TEL_NB                   -> payerInformation.telNumber.getOrElse(defaultValue),
    WINNER_NAME                    -> winnerInformation.name,
    WINNER_STREET_ADDRESS          -> winnerInformation.streetAddress,
    WINNER_CITY_TOWN               -> winnerInformation.cityAndZipAddress,
    REPORTABLE_WINNINGS            -> winning.reportableAmount.toString,
    DATE_WON                       -> winning.dateWon.toString,
    WAGER_TYPE                     -> winning.typeOfWager,
    FEDERAL_TAX_WITHHELD           -> winning.federalTaxWithholdings.toString,
    TRANSACTION                    -> winning.transaction.getOrElse(defaultValue),
    RACE                           -> winning.race.getOrElse(defaultValue),
    WINNINGS_FROM_IDENTICAL_WAGERS -> winning.winningsFromIdenticalWagers.toString,
    CASHIER                        -> winning.cashier.getOrElse(defaultValue),
    WINNER_SSN                     -> winnerInformation.fedTaxIdentifier,
    WINDOW                         -> winning.window.getOrElse(defaultValue),
    FIRST_ID                       -> winnerInformation.firstIdentification.getOrElse(defaultValue),
    SECOND_ID                      -> winnerInformation.secondIdentification.getOrElse(defaultValue),
    STATE_ID_NO                    -> winnerInformation.stateIdentifier.getOrElse(defaultValue),
    STATE_WINNINGS                 -> local.stateWinnings.toString,
    STATE_TAX_WITHHELD             -> local.stateIncomeTax.toString,
    LOCAL_WINNINGS                 -> local.localWinnings.toString,
    LOCAL_TAX_WITHHELD             -> local.localIncomeTax.toString,
    LOCALITY_NAME                  -> local.nameOfLocality,
    CALENDAR_YEAR                  -> calendarYear
  )
}

case class Payer(
    federalIdentificationNumber: String,
    telNumber: Option[String],
    name: String,
    address: String
)

case class Winner(
    name: String,
    streetAddress: String,
    cityAndZipAddress: String,
    fedTaxIdentifier: String,
    stateIdentifier: Option[String],
    firstIdentification: Option[Identification], //
    secondIdentification: Option[Identification]
)

case class Winning(
    reportableAmount: Currency,
    dateWon: LocalDate,
    typeOfWager: String, // Use an enum when possible
    federalTaxWithholdings: Currency,
    transaction: Option[String],
    race: Option[String],
    winningsFromIdenticalWagers: Currency,
    cashier: Option[String],
    window: Option[String]
)

case class LocalData(
    stateWinnings: Currency,
    stateIncomeTax: Currency,
    localWinnings: Currency,
    localIncomeTax: Currency,
    nameOfLocality: String
)
