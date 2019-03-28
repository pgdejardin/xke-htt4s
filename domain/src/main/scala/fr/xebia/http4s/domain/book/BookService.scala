package fr.xebia.http4s.domain.book

import cats.Monad
import cats.implicits._
import cats.data.EitherT
import fr.xebia.http4s.domain.{BookAlreadyExistsError, BookNotFoundError}
import io.chrisdavenport.fuuid.FUUID

import scala.language.higherKinds

class BookService[F[_]](repository: BookRepositoryAlgebra[F], validation: BookValidationAlgebra[F]) {

  def addToLibrary(book: Book)(implicit monad: Monad[F]): EitherT[F, BookAlreadyExistsError, Book] =
    for {
      _     <- validation.doesNotExist(book)
      saved <- EitherT.liftF(repository.create(book))
    } yield saved

  def getABook(isbn: FUUID)(implicit M: Monad[F]): EitherT[F, BookNotFoundError.type, Book] =
    EitherT.fromOptionF(repository.get(isbn), BookNotFoundError)

  def getAllBooksInLibrary: F[List[Book]] =
    repository.list()

  def removeFromLibrary(isbn: FUUID)(implicit monad: Monad[F]): EitherT[F, BookNotFoundError.type, Unit] =
    for {
      _ <- validation.exists(isbn.some)
      _ <- EitherT.liftF(repository.delete(isbn).as(()))
    } yield {}
}

object BookService {
  def apply[F[_]: Monad](repository: BookRepositoryAlgebra[F], validation: BookValidationAlgebra[F]): BookService[F] =
    new BookService(repository, validation)
}
