package fr.xebia.http4s.domain.book
import java.util.UUID

import scala.language.higherKinds

trait BookRepositoryAlgebra[F[_]] {

  def create(book: Book): F[Book]

  def get(isbn: UUID): F[Option[Book]]

  def list(): F[List[Book]]

  def delete(isbn: UUID): F[Option[Book]]

  def findByTitleAndAuthor(title: String, authorId: UUID): F[Option[Book]]
}
