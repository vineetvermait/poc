package in.techutils.awsproxy.dto;

public class SQSSendMessageResponse {
    private String messageId;

    public SQSSendMessageResponse(String messageId) {
        this.messageId = messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getMessageId() {
        return messageId;
    }
}
