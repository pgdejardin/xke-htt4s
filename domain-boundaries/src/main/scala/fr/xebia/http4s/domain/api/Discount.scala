package fr.xebia.http4s.domain.api

case class Discount(price: Double, discount: Double, display: String)

object Discount {
  def getDiscountTypeFromPrice(price: Double): DiscountType = {
    if (price > 0 && price <= 20) return Percentage
    if (price > 20 && price <= 50) return Minus
    if (price > 50) return PerSlice
    None
  }
}

sealed trait DiscountType
case object Percentage extends DiscountType
case object Minus      extends DiscountType
case object PerSlice   extends DiscountType
case object None       extends DiscountType
