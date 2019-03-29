package fr.xebia.http4s.domain

import cats.Monad
import cats.data.EitherT
import cats.implicits._
import fr.xebia.http4s.domain.api.{BookValidation => BookValidationAPI, BookService => BookServiceAPI}
import fr.xebia.http4s.domain.spi.{Book, BookRepository}
import io.chrisdavenport.fuuid.FUUID

import scala.language.higherKinds

class BookService[F[_]](repository: BookRepository[F], validation: BookValidationAPI[F]) extends BookServiceAPI[F] {

  override def addToLibrary(book: Book)(implicit monad: Monad[F]): EitherT[F, BookAlreadyExistsError, Book] =
    for {
      _     <- validation.doesNotExist(book)
      saved <- EitherT.liftF(repository.create(book))
    } yield saved

  override def getABook(isbn: FUUID)(implicit M: Monad[F]): EitherT[F, BookNotFoundError.type, Book] =
    EitherT.fromOptionF(repository.get(isbn), BookNotFoundError)

  override def getAllBooksInLibrary: F[List[Book]] =
    repository.list()

  override def removeFromLibrary(isbn: FUUID)(implicit monad: Monad[F]): EitherT[F, BookNotFoundError.type, Unit] =
    for {
      _ <- validation.exists(isbn.some)
      _ <- EitherT.liftF(repository.delete(isbn).as(()))
    } yield {}
}

object BookService {
  def apply[F[_]: Monad](repository: BookRepository[F], validation: BookValidation[F]): BookService[F] =
    new BookService(repository, validation)
}
