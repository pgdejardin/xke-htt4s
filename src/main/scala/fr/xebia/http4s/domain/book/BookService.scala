package fr.xebia.http4s.domain.book

import java.util.UUID

import cats.Monad
import cats.data.EitherT
import fr.xebia.http4s.domain.{BookAlreadyExistsError, BookNotFoundError}

import scala.language.higherKinds

class BookService[F[_]](repository: BookRepositoryAlgebra[F], validation: BookValidationAlgebra[F]) {
  import cats.syntax.all._

  def create(book: Book)(implicit monad: Monad[F]): EitherT[F, BookAlreadyExistsError, Book] =
    for {
      _ <- validation.doesNotExist(book)
      saved <- EitherT.liftF(repository.create(book))
    } yield saved

  def get(isbn: UUID)(implicit M: Monad[F]): EitherT[F, BookNotFoundError.type, Book] =
    EitherT.fromOptionF(repository.get(isbn), BookNotFoundError)
}

object BookService {
  def apply[F[_]: Monad](repository: BookRepositoryAlgebra[F], validation: BookValidationAlgebra[F]): BookService[F] =
    new BookService(repository, validation)
}
