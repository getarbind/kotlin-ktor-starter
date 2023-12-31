package test.milk.rabbitmq

import com.rabbitmq.client.Channel
import com.rabbitmq.client.Delivery
import io.collective.start.rabbitmq.ChannelDeliverCallback
import org.slf4j.LoggerFactory

class ManualAckTestHandler(private val name: String, private val function: () -> Unit) : ChannelDeliverCallback {
    private val logger = LoggerFactory.getLogger(this.javaClass)
    private var channel: Channel? = null

    override fun setChannel(channel: Channel) {
        this.channel = channel
    }

    override fun handle(consumerTag: String, message: Delivery) {
        function()
        channel!!.basicAck(message.envelope.deliveryTag, true)
    }
}
