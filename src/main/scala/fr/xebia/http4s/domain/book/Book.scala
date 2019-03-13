package fr.xebia.http4s.domain.book

import java.util.UUID

import fr.xebia.http4s.domain.author.Author

case class Book(
    title: String,
    description: String,
    author: Author,
    isbn: Option[UUID] = None
)
