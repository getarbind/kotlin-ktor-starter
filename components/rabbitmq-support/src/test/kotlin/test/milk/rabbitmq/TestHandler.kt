package test.milk.rabbitmq

import com.rabbitmq.client.Channel
import com.rabbitmq.client.Delivery
import io.collective.start.rabbitmq.ChannelDeliverCallback

class TestHandler(private val function: () -> Unit) : ChannelDeliverCallback {

    override fun setChannel(channel: Channel) {
    }

    override fun handle(consumerTag: String, message: Delivery) {
        function()
    }
}
