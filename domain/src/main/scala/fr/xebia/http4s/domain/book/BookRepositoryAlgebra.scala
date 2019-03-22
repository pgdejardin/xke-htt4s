package fr.xebia.http4s.domain.book
import java.util.UUID

import fr.xebia.http4s.domain.author.Author

import scala.language.higherKinds

trait BookRepositoryAlgebra[F[_]] {

  def create(book: Book): F[Book]

  def get(isbn: UUID): F[Option[Book]]

  def list(): F[List[Book]]

  def delete(isbn: UUID): F[Option[Book]]

  def findByTitleAndAuthor(title: String, author: Author): F[Option[Book]]
}
