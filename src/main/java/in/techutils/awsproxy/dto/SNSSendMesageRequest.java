package in.techutils.awsproxy.dto;

import java.util.Map;

public class SNSSendMesageRequest {
    private String topicARN, region;
    private String messageBody, subject;
    private Map<String, String> attributes;

    public void setTopicARN(String topicARN) {
        this.topicARN = topicARN;
    }

    public String getTopicARN() {
        return topicARN;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getRegion() {
        return region;
    }

    public void setMessageBody(String messageBody) {
        this.messageBody = messageBody;
    }

    public String getMessageBody() {
        return messageBody;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getSubject() {
        return subject;
    }

    public void setAttributes(Map<String, String> attributes) {
        this.attributes = attributes;
    }

    public Map<String, String> getAttributes() {
        return attributes;
    }
}
