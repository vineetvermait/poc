package in.techutils.awsproxy.dto;

public class SNSSendMessageResponse {
    private String messageId;

    public SNSSendMessageResponse(String messageId) {
        this.messageId = messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getMessageId() {
        return messageId;
    }
}
