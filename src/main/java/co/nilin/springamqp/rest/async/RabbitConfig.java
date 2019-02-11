package co.nilin.springamqp.rest.async;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    @Bean
    public Queue hello() {
        return new Queue("hello", true);
    }

    @Bean
    public AsyncReceiver1 asyncReceiver1() {
        return new AsyncReceiver1();
    }


}
