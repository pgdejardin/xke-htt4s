package fr.xebia.http4s.infrastructure.repository.doobiedb

import java.util.UUID

import cats.Monad
import cats.data.OptionT
import cats.implicits._
import doobie._
import doobie.implicits._
import fr.xebia.http4s.domain.book.{Book, BookRepositoryAlgebra}
import doobie.postgres._
import doobie.postgres.implicits._

import scala.language.higherKinds

class DoobieBookRepositoryInterpreter[F[_]: Monad](val xa: Transactor[F]) extends BookRepositoryAlgebra[F] {
  import BookSQL._

  override def create(book: Book): F[Book] =
    insert(book).run.map(_ => book).transact(xa)

  override def get(isbn: UUID): F[Option[Book]] = select(isbn).option.transact(xa)

  override def list(): F[List[Book]] = selectAll.to[List].transact(xa)

  override def delete(isbn: UUID): F[Option[Book]] =
    OptionT(get(isbn))
      .semiflatMap(book => BookSQL.delete(isbn).run.transact(xa).as(book))
      .value

  override def findByTitleAndAuthor(title: String, authorId: UUID): F[Option[Book]] =
    selectByTitleAndAuthor(title, authorId).option
      .transact(xa)
}

object DoobieBookRepositoryInterpreter {
  def apply[F[_]: Monad](xa: Transactor[F]): DoobieBookRepositoryInterpreter[F] =
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

  def select(isbn: UUID): Query0[Book] =
    sql"""
         select isbn, author_id from book where isbn = $isbn
       """.query

  def delete(isbn: UUID): Update0 =
    sql"""
         delete from book where isbn = $isbn
       """.update

  def selectByTitleAndAuthor(title: String, authorId: UUID): Query0[Book] =
    sql"""
         select isbn, title, description, author_id
         from book where title = $title
         and author_id = $authorId
       """.query
}
