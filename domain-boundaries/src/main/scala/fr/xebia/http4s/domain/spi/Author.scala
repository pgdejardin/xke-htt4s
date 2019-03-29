package fr.xebia.http4s.domain.spi
import io.chrisdavenport.fuuid.FUUID

case class Author(
    identifier: FUUID,
    firstName: String,
    lastName: String,
)
