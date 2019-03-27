package fr.xebia.http4s.infrastructure.repository.inmemory

import java.util.UUID

import cats._
import cats.syntax.applicative._
import cats.syntax.option._
import fr.xebia.http4s.domain.book.{Book, BookRepositoryAlgebra}

import scala.collection.concurrent.TrieMap
import scala.language.higherKinds

class InMemoryBookRepositoryInterpreter[F[_]: Applicative] extends BookRepositoryAlgebra[F] {

  private val cache = new TrieMap[UUID, Book]

  override def create(book: Book): F[Book] = {
    val isbn = UUID.randomUUID()
    val bookToSave = book.copy(isbn = isbn.some)
    cache += (isbn -> book.copy(isbn = isbn.some))
    bookToSave.pure[F]
  }

  override def get(isbn: UUID): F[Option[Book]] = cache.get(isbn).pure[F]

  override def list(): F[List[Book]] = cache.values.toList.sortBy(_.title).pure[F]

  override def delete(isbn: UUID): F[Option[Book]] = cache.remove(isbn).pure[F]

  override def findByTitleAndAuthor(title: String, authorId: UUID): F[Option[Book]] =
    cache.values.toList.find(book => book.title == title && book.authorId == authorId).pure[F]
}

object InMemoryBookRepositoryInterpreter {
  def apply[F[_]: Applicative]() = new InMemoryBookRepositoryInterpreter[F]()
}
