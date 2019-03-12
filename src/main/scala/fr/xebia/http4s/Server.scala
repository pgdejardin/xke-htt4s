package fr.xebia.http4s

import cats.effect._
import cats.implicits._
import fr.xebia.http4s.domain.book.{BookService, BookValidationInterpreter}
import fr.xebia.http4s.infrastructure.endpoint.BookEndpoints
import fr.xebia.http4s.infrastructure.repository.inmemory.BookRepositoryInMemoryInterpreter
import org.http4s.HttpRoutes
import org.http4s.implicits._
import org.http4s.server.blaze.BlazeServerBuilder
import org.http4s.server.{Router, Server => H4Server}

object Server extends IOApp {
  def booksRoutes[F[_]: Effect]: HttpRoutes[F] = new BookEndpoints[F].routes

  def createServer[F[_]: ContextShift: ConcurrentEffect: Timer]: Resource[F, H4Server[F]] =
    for {
//      conf <- Resource.liftF(parser.decodePathF[F, LibraryConfig]("library"))
      bookRepo <- BookRepositoryInMemoryInterpreter[F]()
      bookValidation = BookValidationInterpreter[F](bookRepo)
      bookService = BookService[F](bookRepo, bookValidation)
      services = BookEndpoints.endpoints[F](bookService)
      httpApp = Router("/" -> services).orNotFound
      server <- BlazeServerBuilder[F]
        .bindHttp(8080, "0.0.0.0")
        .withHttpApp(httpApp)
        .resource
    } yield server

//  def run(args: List[String]): IO[ExitCode] = ServerStream.stream[IO].compile.drain.as(ExitCode.Success)
  def run(args: List[String]): IO[ExitCode] = createServer.use(_ => IO.never).as(ExitCode.Success)

}
