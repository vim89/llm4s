package org.llm4s.core.safety

import org.llm4s.types.Result

import scala.util.{ Failure, Success, Try }

object UsingOps {
  implicit final class TryToResultOps[A](private val t: Try[A]) extends AnyVal {
    def toResult(implicit em: ErrorMapper = DefaultErrorMapper): Result[A] = t match {
      case Success(v) => Right(v)
      case Failure(e) => Left(em(e))
    }
  }
}
