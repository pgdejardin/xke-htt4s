package fr.xebia.http4s.domain

import cats.Monad
import cats.data.EitherT
import cats.implicits._
import fr.xebia.http4s.domain.api.{BookValidation => BookValidationAPI}
import fr.xebia.http4s.domain.spi.{Book, BookRepository}
import io.chrisdavenport.fuuid.FUUID

import scala.language.higherKinds

class BookValidation[F[_]: Monad](repository: BookRepository[F]) extends BookValidationAPI[F] {

  def doesNotExist(book: Book): EitherT[F, BookAlreadyExistsError, Unit] = EitherT {
    repository.findByTitleAndAuthor(book.title, book.authorId).map { matches =>
      if (matches.isEmpty) {
        Right(())
      } else {
        Left(BookAlreadyExistsError(book))
      }
    }
  }

  override def exists(isbn: Option[FUUID]): EitherT[F, BookNotFoundError.type, Unit] = EitherT {
    isbn match {
      case Some(id) =>
        repository.get(id).map {
          case Some(_) => Right(())
          case _       => Left(BookNotFoundError)
        }
      case None =>
        Either.left[BookNotFoundError.type, Unit](BookNotFoundError).pure[F]
    }
  }

}

object BookValidation {
  def apply[F[_]: Monad](repository: BookRepository[F]): BookValidation[F] =
    new BookValidation[F](repository)
}
