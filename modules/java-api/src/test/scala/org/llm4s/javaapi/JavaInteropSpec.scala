package org.llm4s.javaapi

import org.llm4s.llmconnect.LLMClient
import org.llm4s.llmconnect.model._
import org.llm4s.types.Result
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

import java.lang.reflect.Modifier
import scala.jdk.CollectionConverters._

/**
 * Exercises the public API from real Java source (`src/test/java/.../JavaInteropCheck.java`) and
 * guards the shape of the Java-visible signatures, so a Scala type leaking into the Java surface
 * is a test failure rather than a surprise for the first Java user.
 */
class JavaInteropSpec extends AnyFlatSpec with Matchers {

  // Scala `private[javaapi]` is public in bytecode, so these are knowingly Java-visible. They are
  // internal (documented as such) and allowlisted so any NEW Scala type in a signature fails here.
  private val internalByDesign = Set("from", "underlying")

  private def answering(answer: String): LLMClient = new LLMClient {
    override def complete(c: Conversation, o: CompletionOptions): Result[Completion] =
      Right(Completion("id", 0L, answer, "m", AssistantMessage(answer)))
    override def streamComplete(c: Conversation, o: CompletionOptions, f: StreamedChunk => Unit): Result[Completion] =
      complete(c, o)
    override def getContextWindow(): Int     = 4096
    override def getReserveCompletion(): Int = 512
  }

  "the documented Java quick-start" should "compile from Java and run end to end" in {
    val log = JavaInteropCheck.quickStart(new JLlmClient(answering("4"))).asScala.toList
    log shouldBe List("ok:4", "get:4", "len:1", "opt:4", "cf:4", "conv:4")
  }

  "the Java failure path" should "work with an LLMError implemented in Java" in {
    JavaInteropCheck.failurePath().asScala.toList shouldBe List(
      "isFailure:true",
      "orNull:null",
      "opt:false",
      "err:boom",
      "cb:boom",
      "caught:boom",
      "cf:true"
    )
  }

  "the public Java-visible surface" should "not expose scala.* types outside the allowlisted internals" in {
    val classes = List(
      classOf[LlmResult[_]],
      classOf[JLlmClient],
      classOf[JAgent],
      Class.forName("org.llm4s.javaapi.Llm4s"),
      classOf[ConversationBuilder],
      classOf[LlmException]
    )
    val offenders = for {
      cls <- classes
      m   <- cls.getMethods.toList if Modifier.isPublic(m.getModifiers) && m.getDeclaringClass == cls
      if !internalByDesign(m.getName)
      t <- m.getReturnType :: m.getParameterTypes.toList
      if t.getName.startsWith("scala.")
    } yield s"${cls.getSimpleName}.${m.getName}: ${t.getName}"
    offenders shouldBe Nil
  }
}
