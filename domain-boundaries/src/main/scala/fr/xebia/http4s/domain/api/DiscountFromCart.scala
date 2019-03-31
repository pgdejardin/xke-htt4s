package fr.xebia.http4s.domain.api

import cats.Monad
import cats.data.EitherT
import fr.xebia.http4s.domain.DiscountCannotBeApplied
import fr.xebia.http4s.domain.spi.Cart

import scala.language.higherKinds

trait DiscountFromCart[F[_]] {
  def discount(cart: Cart)(implicit monad: Monad[F]): EitherT[F, DiscountCannotBeApplied.type, Discount]

  def applyDiscount(cart: Cart)(implicit monad: Monad[F]): EitherT[F, DiscountCannotBeApplied.type, Cart]
}
