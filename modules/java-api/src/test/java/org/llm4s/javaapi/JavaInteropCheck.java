package org.llm4s.javaapi;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.llm4s.error.LLMError;
import org.llm4s.llmconnect.model.Conversation;

/**
 * Real Java source compiled by sbt (mixed compilation) against the module's public API. It proves
 * that the documented Java quick-start shapes (static factories, lambdas passed as
 * {@code java.util.function} types, {@link Optional}, {@link CompletableFuture}, try-with-resources)
 * compile and behave from Java, which Scala-only tests cannot show.
 *
 * <p>The methods take already-built clients so that no Scala-only construction is needed here.
 */
public final class JavaInteropCheck {
  private JavaInteropCheck() {}

  /** An LLMError implemented from Java: the failure path must be constructible without Scala syntax. */
  static final class JavaError implements LLMError {
    private final String msg;

    JavaError(String msg) {
      this.msg = msg;
    }

    @Override
    public String message() {
      return msg;
    }

    @Override
    public int productArity() {
      return 1;
    }

    @Override
    public Object productElement(int n) {
      return msg;
    }

    @Override
    public boolean canEqual(Object that) {
      return that instanceof JavaError;
    }
  }

  /** Drives the quick-start from the Scaladoc against a client that answers "4". */
  public static List<String> quickStart(JLlmClient client) throws Exception {
    List<String> log = new ArrayList<>();
    try (JLlmClient c = client) {
      LlmResult<String> r = c.complete("What is 2+2?");
      r.ifSuccess(s -> log.add("ok:" + s)).ifFailure(e -> log.add("err:" + e.getMessage()));
      log.add("get:" + r.get());
      log.add("len:" + r.map(String::length).get());
      Optional<String> opt = r.toOptional();
      log.add("opt:" + opt.orElse("none"));
      CompletableFuture<String> f = r.toCompletableFuture();
      log.add("cf:" + f.get());

      Conversation conv =
          ConversationBuilder.create().system("be brief").user("hi").assistant("hello").user("2+2").build();
      log.add("conv:" + c.complete(conv).get());
      // NOTE: complete(Conversation, CompletionOptions) is intentionally not driven here:
      // CompletionOptions cannot be built from Java without scala.Option/scala.Seq arguments
      // (known gap, see the PR review).
    }
    return log;
  }

  /** Failure handling from Java: getOrNull, getError, Optional, CompletableFuture, get() throwing. */
  public static List<String> failurePath() throws Exception {
    List<String> log = new ArrayList<>();
    LlmResult<String> r = LlmResult.failure(new JavaError("boom"));
    log.add("isFailure:" + r.isFailure());
    log.add("orNull:" + r.getOrNull());
    log.add("opt:" + r.toOptional().isPresent());
    log.add("err:" + r.getError().getMessage());
    r.ifSuccess(s -> log.add("UNEXPECTED")).ifFailure(e -> log.add("cb:" + e.getMessage()));
    try {
      r.get();
      log.add("UNEXPECTED-NO-THROW");
    } catch (LlmException e) {
      log.add("caught:" + e.getMessage());
    }
    try {
      r.toCompletableFuture().get();
    } catch (ExecutionException e) {
      log.add("cf:" + (e.getCause() instanceof LlmException));
    }
    return log;
  }
}
