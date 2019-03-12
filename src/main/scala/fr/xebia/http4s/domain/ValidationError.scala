package fr.xebia.http4s.domain
import fr.xebia.http4s.domain.book.Book

sealed trait ValidationError extends Product with Serializable
case object BookNotFoundError extends ValidationError
case class BookAlreadyExistsError(book: Book) extends ValidationError
