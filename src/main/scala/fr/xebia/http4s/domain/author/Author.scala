package fr.xebia.http4s.domain.author
import java.util.UUID

import fr.xebia.http4s.domain.book.Book

case class Author(
    id: UUID,
    firstName: String,
    lastName: String,
    books: List[Book] = List.empty
)
