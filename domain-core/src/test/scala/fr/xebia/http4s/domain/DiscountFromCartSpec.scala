package fr.xebia.http4s.domain

import cats.effect.IO
import cats.syntax.option._
import fr.xebia.http4s.domain.spi._
import io.chrisdavenport.fuuid.FUUID
import org.scalatest.{FlatSpec, Matchers}

class DiscountFromCartSpec extends FlatSpec with Matchers {
  "A cart" should "calculate a discount" in {
    // Given
    val customer = Customer(FUUID.randomFUUID[IO].unsafeRunSync(), "firstName", "lastName")
    val book1 =
      Book("title1", FUUID.randomFUUID[IO].unsafeRunSync().some, "", FUUID.randomFUUID[IO].unsafeRunSync(), 10)
    val book2 = Book("title2", FUUID.randomFUUID[IO].unsafeRunSync().some, "", FUUID.randomFUUID[IO].unsafeRunSync(), 5)
    val books = List(CartBook(book1, 1), CartBook(book2, 1))
    val cart  = Cart(customer, books)
    val cartRepository = new CartRepository[IO] {
      override def save(cart: Cart): IO[Cart] = IO(cart)
    }
    val discountFromCart = DiscountFromCart[IO](cartRepository)

    // When
    val discountFromCartApplied = discountFromCart.applyDiscount(cart)

    // Then
    discountFromCartApplied.value.unsafeRunSync() match {
      case Left(_) => fail("discount should have been applied")
      case Right(cartWithDiscount) =>
        cartWithDiscount.user should equal(customer)
        cartWithDiscount.discount match {
          case Some(discount) => discount.discount should equal(1.5D)
          case _              => fail("discount should have a discount value")
        }
    }

  }
}
