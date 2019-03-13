package fr.xebia.http4s.infrastructure.repository.inmemory
import java.util.UUID

import cats._
import cats.implicits._
import fr.xebia.http4s.domain.author.Author
import fr.xebia.http4s.domain.book.{Book, BookRepositoryAlgebra}

import scala.collection.concurrent.TrieMap

class BookRepositoryInMemoryInterpreter[F[_]: Applicative] extends BookRepositoryAlgebra[F] {

  private val cache = new TrieMap[UUID, Book]

  override def create(book: Book): F[Book] = {
    val isbn = UUID.randomUUID()
    val bookToSave = book.copy(isbn = isbn.some)
    cache += (isbn -> book.copy(isbn = isbn.some))
    bookToSave.pure[F]
  }

  override def get(isbn: UUID): F[Option[Book]] = cache.get(isbn).pure[F]

  override def list(): F[List[Book]] = cache.values.toList.sortBy(_.title).pure[F]

  override def delete(isbn: UUID): F[Option[Book]] = ???

  override def findByTitleAndAuthor(title: String, author: Author): F[Option[Book]] =
    cache.values.toList.find(book => book.title == title && book.author == author).pure[F]
}

object BookRepositoryInMemoryInterpreter {
  def apply[F[_]: Applicative]() = new BookRepositoryInMemoryInterpreter[F]()
}
