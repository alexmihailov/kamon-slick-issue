# Overview

This repository contains an example of reproducing the ClassCastException error when using Slick and Kamon:

```
java.lang.ClassCastException: class kamon.instrumentation.executor.CaptureContextOnSubmitInstrumentation$ContextAwareRunnable cannot be cast to class slick.util.AsyncExecutor$PrioritizedRunnable (kamon.instrumentation.executor.CaptureContextOnSubmitInstrumentation$ContextAwareRunnable and slick.util.AsyncExecutor$PrioritizedRunnable are in unnamed module of loader 'app')
	at slick.util.ManagedArrayBlockingQueue.offer(ManagedArrayBlockingQueue.scala:17)
	at java.base/java.util.concurrent.ThreadPoolExecutor.execute(ThreadPoolExecutor.java:1347)
	at slick.util.AsyncExecutor$$anon$1$$anon$4.execute(AsyncExecutor.scala:163)
	at scala.concurrent.impl.Promise$Transformation.submitWithValue(Promise.scala:429)
	at scala.concurrent.impl.Promise$DefaultPromise.submitWithValue(Promise.scala:338)
	at scala.concurrent.impl.Promise$DefaultPromise.dispatchOrAddCallbacks(Promise.scala:312)
	at scala.concurrent.impl.Promise$DefaultPromise.map(Promise.scala:182)
	at scala.concurrent.Future$.apply(Future.scala:678)
	at slick.jdbc.JdbcBackend$DatabaseDef.io(JdbcBackend.scala:65)
```

The error appears when using kamon-jdbc with configuration settings:

```hocon
kanela.modules.executor-service-capture-on-submit.enabled = true
```

Also, to reproduce the error, it is necessary that HicariCP did not immediately process the tasks, 
but queued them by calling the `java.util.concurrent method.BlockingQueue.offer(E)`.
To do this, you can specify in the configuration:
```hocon
db {
  numThreads = 1
  maxConnections = 1
  minConnections = 1
}
```

# Run
You can run the example through the gradle `run` task:
```
.\gradlew clean run     
```