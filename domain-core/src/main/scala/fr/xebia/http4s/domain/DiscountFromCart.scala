package fr.xebia.http4s.domain
import cats.Monad
import cats.data.EitherT
import cats.implicits._
import fr.xebia.http4s.domain.api.Discount.getDiscountTypeFromPrice
import fr.xebia.http4s.domain.api.{Discount, Minus, PerSlice, Percentage, DiscountFromCart => DiscountFromCartApi}
import fr.xebia.http4s.domain.spi.{Cart, CartBook, CartRepository}

import scala.language.higherKinds

class DiscountFromCart[F[_]](cartRepository: CartRepository[F]) extends DiscountFromCartApi[F] {
  override def discount(cart: Cart)(implicit m: Monad[F]): EitherT[F, DiscountCannotBeApplied.type, Discount] = {
    val total: Double = cart.books.foldLeft(0D) { (acc: Double, item: CartBook) =>
      acc + (item.book.price * item.quantity)
    }
    val discountFromCart = getDiscountTypeFromPrice(total) match {
      case Percentage => Right(Discount(total, total * 10 / 100, "10% discount"))
      case Minus      => Right(Discount(total, 10, "10€ discount"))
      case PerSlice   => Right(Discount(total, (total % 100) * 10, "10€ per 100€ discount"))
      case _          => Left(DiscountCannotBeApplied)
    }
    EitherT.fromEither(discountFromCart)
  }

  override def applyDiscount(cart: Cart)(implicit monad: Monad[F]): EitherT[F, DiscountCannotBeApplied.type, Cart] =
    for {
      discountFromCart <- discount(cart)
      saved <- EitherT.liftF(
        cartRepository.save(cart.copy(total = discountFromCart.price.some, discount = discountFromCart.some)))
    } yield saved
}

object DiscountFromCart {
  def apply[F[_]: Monad](cartRepository: CartRepository[F]): DiscountFromCart[F] = new DiscountFromCart(cartRepository)
}
