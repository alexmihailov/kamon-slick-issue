package com.alexmihailov

import com.typesafe.config.ConfigFactory
import kamon.Kamon
import mu.KotlinLogging
import scala.compat.java8.FutureConverters
import scala.reflect.`ClassTag$`
import slick.basic.`DatabaseConfig$`
import slick.jdbc.H2Profile
import slick.jdbc.JdbcBackend
import slick.util.ClassLoaderUtil
import java.util.concurrent.CompletableFuture

private val log = KotlinLogging.logger {}

fun main() {
    val config = ConfigFactory.load()
    Kamon.init(config)

    val dbConf = `DatabaseConfig$`.`MODULE$`.forConfig<H2Profile>(
        "",
        config,
        ClassLoaderUtil.defaultClassLoader(),
        `ClassTag$`.`MODULE$`.apply(H2Profile::class.java)
    )
    val db = dbConf.db() as JdbcBackend.DatabaseDef
    val futures = mutableListOf<CompletableFuture<*>>()
    for (i in 0 .. 3) {
        val f = db.io {
            // It is necessary that the task be queued
            // java/util/concurrent/ThreadPoolExecutor.java:1347
            // java.util.concurrent.BlockingQueue.offer(E)
            Thread.sleep(1000)
        }
        futures.add(
            FutureConverters.toJava(f)
                .handle { _, ex -> if (ex != null) log.error(ex) { "Error!"} }
                .toCompletableFuture()
        )
    }
    CompletableFuture.allOf(*futures.toTypedArray()).get()
    Kamon.stop()
}
