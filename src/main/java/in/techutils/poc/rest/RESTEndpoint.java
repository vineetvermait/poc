package in.techutils.poc.rest;

import in.techutils.awsproxy.dto.SQSListenerRegisteryRequest;
import in.techutils.awsproxy.proxy.SNSProxy;
import in.techutils.awsproxy.proxy.SQSProxy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class RESTEndpoint {
    @GetMapping(value = "/SNS/{region}")
    public ResponseEntity listSNSTopics(@PathVariable("region") String region) {
        List<String> topics = null;
        try {
            topics = new SNSProxy(region).listTopics();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e);
        }
        return ResponseEntity.ok()
                .body(topics.toString());

    }

    @GetMapping(value = "/SQS/{region}")
    public ResponseEntity listSQSTopics(@PathVariable("region") String region) {
        List<String> urls = null;
        try {
            urls = new SQSProxy(region).listQueueURLs();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e);
        }
        return ResponseEntity.ok()
                .body(urls.toString());

    }

    @PutMapping("/register/{region}")
    public ResponseEntity registerQueueListener(@PathVariable("region") String region, @ModelAttribute("queueURL") String queueURL,
                                                @ModelAttribute("receivingEndpoint") String receivingEndpoint) {
        SQSListenerRegisteryRequest req = new SQSListenerRegisteryRequest(region, queueURL, receivingEndpoint);
        try {
            SQSProxy.registerQueueListener(req);
            SQSProxy.saveListeners(req);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e);
        }
        return ResponseEntity.accepted().build();
    }

    @PutMapping("/deregister/{region}")
    public ResponseEntity deregisterQueueListener(@PathVariable("region") String region, @ModelAttribute("queueURL") String queueURL,
                                                  @ModelAttribute("receivingEndpoint") String receivingEndpoint) {
        SQSListenerRegisteryRequest req = new SQSListenerRegisteryRequest(region, queueURL, receivingEndpoint);
        try {
            SQSProxy.deregisterQueueListener(req);
            SQSProxy.saveListeners(req);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e);
        }
        return ResponseEntity.accepted().build();
    }

    @PostMapping("/sendtosns/{region}")
    public ResponseEntity publishToSNS(@PathVariable("region") String region, @ModelAttribute("topic") String topic,
                                       @ModelAttribute("subject") String subject, @ModelAttribute("message") String messageBody) {
        String messageId = "";
        try {
            SNSProxy proxy = new SNSProxy(region, topic);
            messageId = proxy.sendMessage(subject, messageBody, null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e);
        }
        return ResponseEntity.ok()
                .body(messageId);
    }

    @PostMapping("/sendtosqs/{region}")
    public ResponseEntity publishToSQS(@PathVariable("region") String region, @ModelAttribute("queueURL") String queueURL,
                                       @ModelAttribute("message") String messageBody) {
        String messageId = "";
        try {
            SQSProxy proxy = new SQSProxy(queueURL, region);
            messageId = proxy.sendMessage(messageBody, null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e);
        }
        return ResponseEntity.ok()
                .body(messageId);
    }
}
