package fr.xebia.http4s.infrastructure.repository.inmemory
import java.util.UUID

import cats.Applicative
import fr.xebia.http4s.domain.book.{Book, BookRepositoryAlgebra}

class BookRepositoryInMemoryInterpreter[F[_]: Applicative] extends BookRepositoryAlgebra[F] {

  override def create(book: Book): F[Book] = ???
  override def update(pet: Book): F[Option[Book]] = ???
  override def get(isbn: UUID): F[Option[Book]] = ???
  override def delete(isbn: UUID): F[Option[Book]] = ???
  override def findByTitleAndAuthorId(title: String, authorId: Long): F[Option[Book]] = ???
}

object BookRepositoryInMemoryInterpreter {
  def apply[F[_]: Applicative]() = new BookRepositoryInMemoryInterpreter[F]()
}
