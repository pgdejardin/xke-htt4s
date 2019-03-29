package fr.xebia.http4s.domain.api
import cats.Monad
import cats.data.EitherT
import fr.xebia.http4s.domain.spi.Book
import fr.xebia.http4s.domain.{BookAlreadyExistsError, BookNotFoundError}
import io.chrisdavenport.fuuid.FUUID

import scala.language.higherKinds

trait BookService[F[_]] {

  def addToLibrary(book: Book)(implicit monad: Monad[F]): EitherT[F, BookAlreadyExistsError, Book]

  def getABook(isbn: FUUID)(implicit M: Monad[F]): EitherT[F, BookNotFoundError.type, Book]

  def getAllBooksInLibrary: F[List[Book]]

  def removeFromLibrary(isbn: FUUID)(implicit monad: Monad[F]): EitherT[F, BookNotFoundError.type, Unit]
}
