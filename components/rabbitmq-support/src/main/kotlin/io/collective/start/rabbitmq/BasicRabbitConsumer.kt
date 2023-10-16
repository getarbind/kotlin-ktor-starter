package io.collective.start.rabbitmq

import com.rabbitmq.client.Channel
import com.rabbitmq.client.ConnectionFactory
import com.rabbitmq.client.DefaultConsumer
import java.lang.Thread.sleep
import kotlin.concurrent.thread

class BasicRabbitConsumer(
    private val queue: String,
    private val consumerFactory: (Channel) -> DefaultConsumer,
    private val autoAck: Boolean = true,
) {
    private var running = true
    private val connectionFactory = ConnectionFactory().apply {
        useBlockingIo()
        host = System.getenv("RABBITMQ_URL") ?: "localhost"
    }

    fun start() {
        thread {
            connectionFactory.newConnection().use { connection ->
                connection.createChannel().use { channel ->
                    val consumer = consumerFactory(channel)

                    while (running) {
                        try {
                            channel.basicConsume(queue, autoAck, consumer)
                        } catch (e: Exception) {
                            e.printStackTrace()
                            stop();
                        }
                        sleep(100)
                    }
                }
            }
        }
    }

    fun stop() {
        running = false
        sleep(100)
    }
}
