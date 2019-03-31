package fr.xebia.http4s.domain.spi

import fr.xebia.http4s.domain.api.Discount
import io.chrisdavenport.fuuid.FUUID

case class Cart(
    user: Customer,
    books: List[CartBook] = List.empty,
    total: Option[Double] = None,
    discount: Option[Discount] = None
)

case class CartBook(
    book: Book,
    quantity: Int,
    total: Option[Double] = None
)

case class Customer(
    id: FUUID,
    firstName: String,
    lastName: String
)
