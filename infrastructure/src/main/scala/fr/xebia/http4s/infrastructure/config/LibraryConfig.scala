package fr.xebia.http4s.infrastructure.config

final case class ServerConfig(host: String, port: Int)
final case class LibraryConfig(db: DatabaseConfig, server: ServerConfig)
