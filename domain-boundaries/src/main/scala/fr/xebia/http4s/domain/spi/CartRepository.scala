package fr.xebia.http4s.domain.spi

import scala.language.higherKinds

trait CartRepository[F[_]] {
  def save(cart: Cart): F[Cart]
}
