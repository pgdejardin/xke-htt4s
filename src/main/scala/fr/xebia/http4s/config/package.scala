package fr.xebia.http4s

import io.circe.Decoder
import io.circe.generic.semiauto._

package object config {
  implicit val serverDecoder: Decoder[ServerConfig] = deriveDecoder
  implicit val dbConnectionDecoder: Decoder[DatabaseConnectionsConfig] = deriveDecoder
  implicit val dbDecoder: Decoder[DatabaseConfig] = deriveDecoder
  implicit val configDecoder: Decoder[LibraryConfig] = deriveDecoder
}
