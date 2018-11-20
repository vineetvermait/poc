package in.techutils.awsproxy.proxy;

import com.amazonaws.services.sns.AmazonSNSAsync;
import com.amazonaws.services.sns.model.ListTopicsResult;
import com.amazonaws.services.sns.model.MessageAttributeValue;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.PublishResult;
import com.amazonaws.services.sns.model.Topic;

import in.techutils.awsproxy.notifier.AWSClients;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SNSProxy {
    private String topicARN;
    private AmazonSNSAsync client;

    public SNSProxy(String region, String topicARN) {
        this.topicARN = topicARN;
        this.client = AWSClients.getAWSSNSClient(region);
    }

    public SNSProxy(String region) {
        this.client = AWSClients.getAWSSNSClient(region);
    }

    public List<String> listTopics() {
        ListTopicsResult result = client.listTopics();
        List<Topic> topics = result.getTopics();
        List<String> resList = new ArrayList<String>();
        for (Topic topic : topics) {
            resList.add(topic.getTopicArn());
        }
        return resList;
    }

    public String sendMessage(String subject, String messageBody, Map<String, String> attributes) {
        PublishRequest req = new PublishRequest(this.topicARN, messageBody, subject);
        req.setTopicArn(this.topicARN);
        Map<String, MessageAttributeValue> messageAttributes = new HashMap<String, MessageAttributeValue>();
        if (attributes != null) {
            MessageAttributeValue mav;
            for (String key : attributes.keySet()) {
                mav = new MessageAttributeValue();
                mav.setStringValue(attributes.get(key));
                messageAttributes.put(key, mav);
            }
        }
        req.setMessageAttributes(messageAttributes);
        PublishResult res = client.publish(req);
        return res.getMessageId();
    }
}
