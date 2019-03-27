package fr.xebia.http4s.domain.author
import java.util.UUID

case class Author(
    identifier: UUID,
    firstName: String,
    lastName: String,
)
