package fr.xebia.http4s.domain.book
import java.util.UUID

trait BookRepositoryAlgebra[F[_]] {

  def create(book: Book): F[Book]

  def update(book: Book): F[Option[Book]]

  def get(isbn: UUID): F[Option[Book]]

  def delete(isbn: UUID): F[Option[Book]]

  def findByTitleAndAuthorId(title: String, authorId: UUID): F[Option[Book]]
}
