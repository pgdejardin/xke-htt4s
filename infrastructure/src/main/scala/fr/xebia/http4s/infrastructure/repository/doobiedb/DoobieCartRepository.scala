package fr.xebia.http4s.infrastructure.repository.doobiedb

import java.util.UUID

import cats.Monad
import cats.effect.Bracket
import cats.implicits._
import doobie._
import doobie.implicits._
import doobie.postgres._
import doobie.postgres.implicits._
import fr.xebia.http4s.domain.spi.{Cart, CartBook, CartRepository}
import io.chrisdavenport.fuuid.FUUID
import io.chrisdavenport.fuuid.doobie.implicits._
import io.chrisdavenport.fuuid.circe._
import io.circe.Json
import io.circe.parser._
import org.postgresql.util.PGobject

import scala.language.higherKinds

class DoobieCartRepository[F[_]](val xa: Transactor[F])(implicit val ev: Bracket[F, Throwable])
    extends CartRepository[F] {
  import CartSQL._

  override def save(cart: Cart): F[Cart] =
    insert(CartDb.fromCart(cart)).run.map(_ => cart).transact(xa)

}

private object CartSQL {

  implicit val jsonMeta: Meta[Json] =
    Meta.Advanced
      .other[PGobject]("json")
      .timap[Json](a => parse(a.getValue).leftMap[Json]((e: Exception) => throw e).merge)(
        a => {
          val o = new PGobject
          o.setType("json")
          o.setValue(a.noSpaces)
          o
        }
      )

  def insert(cart: CartDb): Update0 =
    sql"""
        insert into cart(id, user_id, items, price, discount)
        values (${cart.id}, ${cart.userId}, ${cart.items}, ${cart.price}, ${cart.discount})
       """.update
}

object DoobieCartRepository {
  def apply[F[_]: Monad](xa: Transactor[F])(implicit ev: Bracket[F, Throwable]): DoobieCartRepository[F] =
    new DoobieCartRepository(xa)
}

case class CartDb(
    id: FUUID,
    userId: FUUID,
    items: Json,
    price: Double = 0,
    discount: Double = 0
)

object CartDb {
  import io.circe.generic.auto._
  import io.circe.syntax._

  def fromCart(cart: Cart): CartDb = CartDb(
    FUUID.fromUUID(UUID.randomUUID()),
    cart.user.id,
    cart.books.asJson,
    cart.total.get,
    cart.discount.get.discount
  )
}
