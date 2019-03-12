package fr.xebia.http4s.domain.book
import java.util.UUID

import cats.data.EitherT
import fr.xebia.http4s.domain.{BookAlreadyExistsError, BookNotFoundError}

trait BookValidationAlgebra[F[_]] {
  def doesNotExist(book: Book): EitherT[F, BookAlreadyExistsError, Unit]
  def exists(isbn: Option[UUID]): EitherT[F, BookNotFoundError.type, Unit]
}
