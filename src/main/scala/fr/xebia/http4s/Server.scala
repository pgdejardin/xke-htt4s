package fr.xebia.http4s

import cats.effect._
import cats.implicits._
import doobie.util.ExecutionContexts
import fr.xebia.http4s.domain.{BookService, BookValidation}
import fr.xebia.http4s.infrastructure.config.{DatabaseConfig, LibraryConfig}
import fr.xebia.http4s.endpoint.BookEndpoints
import fr.xebia.http4s.infrastructure.repository.doobiedb.DoobieBookRepository
import io.circe.config.parser
import org.http4s.implicits._
import org.http4s.server.blaze.BlazeServerBuilder
import org.http4s.server.{Router, Server => H4Server}

object Server extends IOApp {
  def createServer[F[_]: ContextShift: ConcurrentEffect: Timer]: Resource[F, H4Server[F]] =
    for {
      conf   <- Resource.liftF(parser.decodePathF[F, LibraryConfig]("library"))
      connEc <- ExecutionContexts.fixedThreadPool[F](conf.db.connections.poolSize)
      txnEc  <- ExecutionContexts.cachedThreadPool[F]
      xa     <- DatabaseConfig.dbTransactor(conf.db, connEc, txnEc)
      //      bookRepo = InMemoryBookRepositoryInterpreter[F]()
      bookRepo       = DoobieBookRepository[F](xa)
      bookValidation = BookValidation[F](bookRepo)
      bookService    = BookService[F](bookRepo, bookValidation)
      services       = BookEndpoints.endpoints[F](bookService)
      httpApp        = Router("/" -> services).orNotFound
      _ <- Resource.liftF(DatabaseConfig.initializeDb(conf.db))
      server <- BlazeServerBuilder[F]
        .bindHttp(conf.server.port, conf.server.host)
        .withHttpApp(httpApp)
        .resource
    } yield server

  def run(args: List[String]): IO[ExitCode] = createServer.use(_ => IO.never).as(ExitCode.Success)

}
