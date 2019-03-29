package fr.xebia.http4s.domain.spi
import io.chrisdavenport.fuuid.FUUID

trait BookRepository[F[_]] {

  def create(book: Book): F[Book]

  def get(isbn: FUUID): F[Option[Book]]

  def list(): F[List[Book]]

  def delete(isbn: FUUID): F[Option[Book]]

  def findByTitleAndAuthor(title: String, authorId: FUUID): F[Option[Book]]
}
