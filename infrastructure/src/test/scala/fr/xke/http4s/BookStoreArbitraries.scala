package fr.xke.http4s

import java.util.UUID

import fr.xebia.http4s.domain.author.Author
import fr.xebia.http4s.domain.book.Book
import org.scalacheck.Arbitrary.arbitrary
import org.scalacheck.{Arbitrary, Gen}
import cats.syntax.option._

trait BookStoreArbitraries {

  val bookTitleLength            = 24
  val bookDescriptionLength      = 250
  val authorNameLength           = 16
  val authorNameGen: Gen[String] = Gen.listOfN(authorNameLength, Gen.alphaChar).map(_.mkString)

  implicit val book: Arbitrary[Book] = Arbitrary[Book] {
    for {
      title       <- Gen.listOfN(bookTitleLength, Gen.alphaStr).map(_.mkString)
      description <- Gen.listOfN(bookDescriptionLength, Gen.alphaStr).map(_.mkString)
      authorId    <- arbitrary[UUID]
      isbn        <- Gen.option(Gen.uuid)
    } yield Book(title, isbn, description, authorId.some)
  }

  implicit val author: Arbitrary[Author] = Arbitrary[Author] {
    for {
      firstName <- authorNameGen
      lastName  <- authorNameGen
      id        <- arbitrary[UUID]
    } yield Author(id, firstName, lastName)
  }
}

object BookStoreArbitraries extends BookStoreArbitraries
