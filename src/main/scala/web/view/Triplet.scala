package web.view

case class Triplet[A](previous: Option[A], current: A, next: Option[A])
