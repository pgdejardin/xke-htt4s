package fr.xebia.http4s.endpoint

import cats.effect.Sync
import fr.xebia.http4s.domain.api.DiscountFromCart
import fr.xebia.http4s.domain.spi.Cart
import org.http4s.dsl.Http4sDsl
import io.chrisdavenport.fuuid.circe._
import org.http4s.{EntityDecoder, HttpRoutes}
import io.circe.generic.auto._
import io.circe.syntax._
import org.http4s.circe._

import scala.language.higherKinds

class CartEndpoints[F[_]: Sync] extends Http4sDsl[F] {
  import cats.implicits._

  implicit val cartDecoder: EntityDecoder[F, Cart] = jsonOf[F, Cart]

  private def applyDiscountToCart(discountFromCart: DiscountFromCart[F]): HttpRoutes[F] =
    HttpRoutes.of[F] {
      case req @ POST -> Root / "carts" =>
        val action = for {
          cart             <- req.as[Cart]
          cartWithDiscount <- discountFromCart.applyDiscount(cart).value
        } yield cartWithDiscount

        action.flatMap {
          case Right(value) => Ok(value.asJson)
          case _            => BadRequest()
        }
    }

  def endpoints(discountFromCart: DiscountFromCart[F]): HttpRoutes[F] = applyDiscountToCart(discountFromCart)
}

object CartEndpoints {
  def endpoints[F[_]: Sync](discountFromCart: DiscountFromCart[F]): HttpRoutes[F] =
    new CartEndpoints[F].endpoints(discountFromCart)
}
