package co.nilin.springamqp.rest.async;


import co.nilin.springamqp.data.entity.User;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RabbitListener(queues = "hello")
public class AsyncReceiver {

    @RabbitHandler
    public void receive(User in) {


        //preparing the restTemplate Parameters
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, Object> multiValueMap = new LinkedMultiValueMap<>();
        multiValueMap.add("receptor", in.getPhoneNumber());
        multiValueMap.add("message", in.getVerificationCode());
        HttpEntity<MultiValueMap<String, Object>> registerDtoHttpEntity = new HttpEntity<MultiValueMap<String, Object>>(multiValueMap, headers);

        //preparing the uri
        String url = "https://api.kavenegar.com/v1/49634F384F6752574B654F3261756E744B5A4A6C57513D3D/sms/send";
        URI uri = UriComponentsBuilder.fromUriString(url).build().toUri();

        //exchanging the rest request
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.exchange(uri, HttpMethod.POST, registerDtoHttpEntity, String.class);
    }
}

