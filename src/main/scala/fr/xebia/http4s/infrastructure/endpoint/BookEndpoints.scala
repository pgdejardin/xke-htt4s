package fr.xebia.http4s.infrastructure.endpoint

import cats.effect.Sync
import fr.xebia.http4s.domain.BookAlreadyExistsError
import fr.xebia.http4s.domain.author.Author
import fr.xebia.http4s.domain.book.{Book, BookService}
import io.circe.generic.auto._
import io.circe.syntax._
import org.http4s.circe._
import org.http4s.dsl.Http4sDsl
import org.http4s.{EntityDecoder, HttpRoutes}

import scala.language.higherKinds

class BookEndpoints[F[_]: Sync] extends Http4sDsl[F] {
  import cats.implicits._

  implicit val bookDecoder: EntityDecoder[F, Book] = jsonOf[F, Book]
  implicit val authorDecoder: EntityDecoder[F, Author] = jsonOf[F, Author]

  private def createBookEndpoint(bookService: BookService[F]): HttpRoutes[F] =
    HttpRoutes.of[F] {
      case req @ POST -> Root / "books" =>
        val action = for {
          book <- req.as[Book]
          result <- bookService.addToLibrary(book).value
        } yield result

        action.flatMap {
          case Right(saved) =>
            Ok(saved.asJson)
          case Left(BookAlreadyExistsError(existingBook)) =>
            Conflict(s"The book ${existingBook.title} already exists")
        }
    }

  private def listBooksEndpoint(bookService: BookService[F]): HttpRoutes[F] =
    HttpRoutes.of[F] {
      case GET -> Root / "books" =>
        for {
          retrieved <- bookService.getAllBooksInLibrary()
          response <- Ok(retrieved.asJson)
        } yield response
    }

  def endpoints(bookService: BookService[F]): HttpRoutes[F] =
    createBookEndpoint(bookService) <+> listBooksEndpoint(bookService)
}

object BookEndpoints {
  def endpoints[F[_]: Sync](bookService: BookService[F]): HttpRoutes[F] =
    new BookEndpoints[F].endpoints(bookService)
}
