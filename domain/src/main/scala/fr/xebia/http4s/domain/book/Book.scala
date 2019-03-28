package fr.xebia.http4s.domain.book

import io.chrisdavenport.fuuid.FUUID

case class Book(
    title: String,
    isbn: Option[FUUID] = None,
    description: String,
    authorId: FUUID,
)
