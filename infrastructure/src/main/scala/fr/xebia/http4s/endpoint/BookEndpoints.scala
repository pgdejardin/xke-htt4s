package fr.xebia.http4s.endpoint

import cats.effect.Sync
import fr.xebia.http4s.domain.api.BookService
import fr.xebia.http4s.domain.spi.Book
import fr.xebia.http4s.domain.{BookAlreadyExistsError, BookNotFoundError}
import io.chrisdavenport.fuuid.http4s.FUUIDVar
import io.circe.generic.auto._
import io.circe.syntax._
import org.http4s.circe._
import org.http4s.dsl.Http4sDsl
import org.http4s.{EntityDecoder, HttpRoutes}
import io.chrisdavenport.fuuid.circe._

import scala.language.higherKinds

class BookEndpoints[F[_]: Sync] extends Http4sDsl[F] {
  import cats.implicits._

  implicit val bookDecoder: EntityDecoder[F, Book] = jsonOf[F, Book]

  private def createBookEndpoint(bookService: BookService[F]): HttpRoutes[F] =
    HttpRoutes.of[F] {
      case req @ POST -> Root / "books" =>
        val action = for {
          book   <- req.as[Book]
          result <- bookService.addToLibrary(book).value
        } yield result

        action.flatMap {
          case Right(saved) =>
            Ok(saved.asJson)
          case Left(BookAlreadyExistsError(existingBook)) =>
            Conflict(s"The book ${existingBook.title} already exists")
        }
    }

  private def getBookEndpoint(bookService: BookService[F]) =
    HttpRoutes.of[F] {
      case GET -> Root / "books" / FUUIDVar(isbn) =>
        bookService.getABook(isbn).value.flatMap {
          case Right(found)            => Ok(found.asJson)
          case Left(BookNotFoundError) => NotFound("The book was not found")
        }
    }

  private def listBooksEndpoint(bookService: BookService[F]): HttpRoutes[F] =
    HttpRoutes.of[F] {
      case GET -> Root / "books" =>
        for {
          retrieved <- bookService.getAllBooksInLibrary
          response  <- Ok(retrieved.asJson)
        } yield response
    }

  private def deleteBooks(bookService: BookService[F]) =
    HttpRoutes.of[F] {
      case DELETE -> Root / "books" / FUUIDVar(isbn) =>
        bookService.removeFromLibrary(isbn).value.flatMap {
          case Right(_)                => Ok()
          case Left(BookNotFoundError) => NotFound("The book was not found")
        }
    }

  def endpoints(bookService: BookService[F]): HttpRoutes[F] =
    createBookEndpoint(bookService) <+> listBooksEndpoint(bookService) <+> getBookEndpoint(bookService) <+>
      deleteBooks(bookService)
}

object BookEndpoints {
  def endpoints[F[_]: Sync](bookService: BookService[F]): HttpRoutes[F] =
    new BookEndpoints[F].endpoints(bookService)
}
