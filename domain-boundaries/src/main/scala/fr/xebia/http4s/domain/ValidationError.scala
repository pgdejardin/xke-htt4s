package fr.xebia.http4s.domain
import fr.xebia.http4s.domain.spi.Book

sealed trait ValidationError                  extends Product with Serializable
case object BookNotFoundError                 extends ValidationError
case class BookAlreadyExistsError(book: Book) extends ValidationError
case object DiscountCannotBeApplied           extends ValidationError
