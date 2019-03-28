package fr.xebia.http4s.domain.book

import cats.Monad
import cats.data.EitherT
import cats.implicits._
import fr.xebia.http4s.domain.{BookAlreadyExistsError, BookNotFoundError}
import io.chrisdavenport.fuuid.FUUID

import scala.language.higherKinds

class BookValidationInterpreter[F[_]: Monad](repository: BookRepositoryAlgebra[F]) extends BookValidationAlgebra[F] {

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

object BookValidationInterpreter {
  def apply[F[_]: Monad](repository: BookRepositoryAlgebra[F]): BookValidationInterpreter[F] =
    new BookValidationInterpreter[F](repository)
}
