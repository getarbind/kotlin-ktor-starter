package io.collective.start.rabbitmq

import com.rabbitmq.client.ConnectionFactory

class BasicRabbitConfiguration(
    private val exchange: String,
    private val queue: String,
    private val routingKey: String
) {
    fun setUp() {
        val connectionFactory = ConnectionFactory().apply {
            useBlockingIo()
            host = System.getenv("RABBITMQ_URL") ?: "localhost"
        }
        val connection = connectionFactory.newConnection()

        connection.createChannel().use { channel ->
            channel.exchangeDeclare(exchange, "direct", false, false, null)
            channel.queueDeclare(queue, false, false, false, null)
            channel.queueBind(queue, exchange, routingKey)
        }
    }
}
