package io.collective.start.rabbitmq

import com.rabbitmq.client.ConnectionFactory

class RabbitTestSupport {
    private val connectionFactory = ConnectionFactory().apply { useBlockingIo() }

    fun purge(queue: String) {
        connectionFactory.newConnection().use { connection ->
            connection.createChannel().use { channel ->
                channel.queuePurge(queue)
            }
        }
    }
}
