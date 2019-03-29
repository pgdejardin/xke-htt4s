package fr.xebia.http4s.infrastructure.repository.inmemory

import cats._
import cats.effect.IO
import cats.syntax.applicative._
import cats.syntax.option._
import fr.xebia.http4s.domain.spi.{Book, BookRepository}
import io.chrisdavenport.fuuid.FUUID

import scala.collection.concurrent.TrieMap
import scala.language.higherKinds

class InMemoryBookRepository[F[_]: Applicative] extends BookRepository[F] {

  private val cache = new TrieMap[FUUID, Book]

  override def create(book: Book): F[Book] = {
    val isbn       = FUUID.randomFUUID[IO].unsafeRunSync()
    val bookToSave = book.copy(isbn = isbn.some)
    cache += (isbn -> book.copy(isbn = isbn.some))
    bookToSave.pure[F]
  }

  override def get(isbn: FUUID): F[Option[Book]] = cache.get(isbn).pure[F]

  override def list(): F[List[Book]] = cache.values.toList.sortBy(_.title).pure[F]

  override def delete(isbn: FUUID): F[Option[Book]] = cache.remove(isbn).pure[F]

  override def findByTitleAndAuthor(title: String, authorId: FUUID): F[Option[Book]] =
    cache.values.toList.find(book => book.title == title && book.authorId == authorId).pure[F]
}

object InMemoryBookRepository {
  def apply[F[_]: Applicative]() = new InMemoryBookRepository[F]()
}
