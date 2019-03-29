package fr.xebia.http4s.domain.api
import cats.data.EitherT
import fr.xebia.http4s.domain.spi.Book
import fr.xebia.http4s.domain.{BookAlreadyExistsError, BookNotFoundError}
import io.chrisdavenport.fuuid.FUUID

trait BookValidation[F[_]] {
  def doesNotExist(book: Book): EitherT[F, BookAlreadyExistsError, Unit]
  def exists(isbn: Option[FUUID]): EitherT[F, BookNotFoundError.type, Unit]
}
