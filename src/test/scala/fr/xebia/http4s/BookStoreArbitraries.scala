package fr.xebia.http4s

import cats.effect.IO
import cats.syntax.option._
import fr.xebia.http4s.domain.spi.{Author, Book}
import io.chrisdavenport.fuuid.FUUID
import org.scalacheck.{Arbitrary, Gen}

trait BookStoreArbitraries {

  val bookTitleLength            = 24
  val bookDescriptionLength      = 250
  val authorNameLength           = 16
  val authorNameGen: Gen[String] = Gen.listOfN(authorNameLength, Gen.alphaChar).map(_.mkString)

  implicit val book: Arbitrary[Book] = Arbitrary[Book] {
    for {
      title       <- Gen.listOfN(bookTitleLength, Gen.alphaStr).map(_.mkString)
      description <- Gen.listOfN(bookDescriptionLength, Gen.alphaStr).map(_.mkString)
      authorId = FUUID.randomFUUID[IO].unsafeRunSync()
      isbn     = FUUID.randomFUUID[IO].unsafeRunSync()
    } yield Book(title, isbn.some, description, authorId)
  }

  implicit val author: Arbitrary[Author] = Arbitrary[Author] {
    for {
      firstName <- authorNameGen
      lastName  <- authorNameGen
      id = FUUID.randomFUUID[IO].unsafeRunSync()
    } yield Author(id, firstName, lastName)
  }
}

object BookStoreArbitraries extends BookStoreArbitraries
