package fr.xebia.http4s.domain.book

import java.util.UUID

case class Book(
    title: String,
    isbn: Option[UUID] = None,
    description: String,
    authorId: Option[UUID] = None,
)
