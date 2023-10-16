package test.milk.rabbitmq

import com.rabbitmq.client.AMQP
import com.rabbitmq.client.Channel
import com.rabbitmq.client.DefaultConsumer
import com.rabbitmq.client.Envelope
import org.slf4j.LoggerFactory

class TestConsumer(
    private val name: String,
    private val function: () -> Unit,
    channel: Channel
) : DefaultConsumer(channel) {
    private val logger = LoggerFactory.getLogger(this.javaClass)

    override fun handleDelivery(
        consumerTag: String,
        envelope: Envelope,
        properties: AMQP.BasicProperties,
        body: ByteArray
    ) {
        function()
    }
}
