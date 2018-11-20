package in.techutils.awsproxy.dto;

public class SQSListenerRegisteryRequest {
    private String region, queueURL, receivingEndpoint;

    public SQSListenerRegisteryRequest(String region, String queueURL, String receivingEndpoint) {
        this.region = region;
        this.queueURL = queueURL;
        this.receivingEndpoint = receivingEndpoint;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getRegion() {
        return region;
    }

    public void setQueueURL(String queueURL) {
        this.queueURL = queueURL;
    }

    public String getQueueURL() {
        return queueURL;
    }

    public void setReceivingEndpoint(String receivingEndpoint) {
        this.receivingEndpoint = receivingEndpoint;
    }

    public String getReceivingEndpoint() {
        return receivingEndpoint;
    }

}
