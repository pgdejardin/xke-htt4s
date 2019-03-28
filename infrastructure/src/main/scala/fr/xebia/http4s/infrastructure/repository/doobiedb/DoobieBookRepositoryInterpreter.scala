package fr.xebia.http4s.infrastructure.repository.doobiedb

import cats.Monad
import cats.data.OptionT
import cats.effect.Bracket
import cats.implicits._
import doobie._
import doobie.implicits._
import doobie.postgres._
import doobie.postgres.implicits._
import fr.xebia.http4s.domain.book.{Book, BookRepositoryAlgebra}
import io.chrisdavenport.fuuid.FUUID
import io.chrisdavenport.fuuid.doobie.implicits._

import scala.language.higherKinds

class DoobieBookRepositoryInterpreter[F[_]](val xa: Transactor[F])(implicit val ev: Bracket[F, Throwable])
    extends BookRepositoryAlgebra[F] {
  import BookSQL._

  override def create(book: Book): F[Book] =
    insert(book).run.map(_ => book).transact(xa)

  override def get(isbn: FUUID): F[Option[Book]] =
    select(isbn).option.transact(xa)

  override def list(): F[List[Book]] = selectAll.to[List].transact(xa)

  override def delete(isbn: FUUID): F[Option[Book]] =
    OptionT(get(isbn))
      .semiflatMap(book => BookSQL.delete(isbn).run.transact(xa).as(book))
      .value

  override def findByTitleAndAuthor(title: String, authorId: FUUID): F[Option[Book]] =
    selectByTitleAndAuthor(title, authorId).option
      .transact(xa)
}

object DoobieBookRepositoryInterpreter {
  def apply[F[_]: Monad](xa: Transactor[F])(implicit ev: Bracket[F, Throwable]): DoobieBookRepositoryInterpreter[F] =
    new DoobieBookRepositoryInterpreter(xa)
}

private object BookSQL {
  def insert(book: Book): Update0 =
    sql"""
         insert into book(isbn, title, description, author_id)
         values (${book.isbn}, ${book.title}, ${book.description}, ${book.authorId})
       """.update

  def selectAll: Query0[Book] =
    sql"""
         select title, isbn, description, author_id from book order by title
       """.query

  def select(isbn: FUUID): Query0[Book] =
    sql"""
         select title, isbn, description, author_id from book where isbn = $isbn
       """.query

  def delete(isbn: FUUID): Update0 =
    sql"""
         delete from book where isbn = $isbn
       """.update

  def selectByTitleAndAuthor(title: String, authorId: FUUID): Query0[Book] =
    sql"""
         select title, isbn, description, author_id
         from book where title = $title
         and author_id = $authorId
       """.query
}
