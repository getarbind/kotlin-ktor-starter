package io.collective.start.rabbitmq

import com.rabbitmq.client.CancelCallback
import com.rabbitmq.client.ConnectionFactory
import java.lang.Thread.sleep
import kotlin.concurrent.thread

class BasicRabbitListener(
    private val queue: String,
    private val delivery: ChannelDeliverCallback,
    private val cancel: CancelCallback,
    private val autoAck: Boolean = true
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
                    delivery.setChannel(channel)

                    while (running) {
                        try {
                            channel.basicConsume(queue, autoAck, delivery, cancel)
                        } catch (e: Exception) {
                            e.printStackTrace()
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
