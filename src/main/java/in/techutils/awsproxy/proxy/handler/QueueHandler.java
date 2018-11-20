package in.techutils.awsproxy.proxy.handler;

import com.amazonaws.AmazonWebServiceRequest;
import com.amazonaws.handlers.AsyncHandler;
import com.amazonaws.services.sqs.AmazonSQSAsync;
import com.amazonaws.services.sqs.model.DeleteMessageRequest;
import com.amazonaws.services.sqs.model.DeleteMessageResult;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageResult;

import in.techutils.awsproxy.notifier.AWSClients;
//import in.techutils.awsproxy.notifier.MessageNotificationService;

import in.techutils.awsproxy.notifier.MessageNotificationService;

import in.techutils.awsproxy.proxy.SQSProxy;

import java.util.List;

public class QueueHandler<REQUEST extends AmazonWebServiceRequest, RESULT extends Object> implements AsyncHandler {
    private String queueUrl;
    private String region;

    public QueueHandler(String queueURL, String region) {
        this.queueUrl = queueURL;
        this.region = region;
    }

    @Override
    public void onError(Exception exception) {
        System.out.println(exception);
        exception.printStackTrace();
    }

    @Override
    public void onSuccess(AmazonWebServiceRequest awsReq, Object object) {
        AmazonSQSAsync client = AWSClients.getAWSSQSClient(region);
        ReceiveMessageResult result = (ReceiveMessageResult) object;
        List<Message> messages = result.getMessages();
        for (Message message : messages) {
            System.out.println("deleting" + message.getBody());
            DeleteMessageRequest req = new DeleteMessageRequest();
            req.setReceiptHandle(message.getReceiptHandle());
            req.setQueueUrl(queueUrl);
            DeleteMessageResult res = client.deleteMessage(req);
            System.out.println("DeleteMessageResult:::: " + res);
            System.out.println("Message to publish:: " + message.toString());

            try {
                List<String> receivers = SQSProxy.getRegistry().get(queueUrl);
                if (receivers != null) {
                    for (String endpoint : receivers) {
                        System.out.println("Notifying: " + endpoint);
                        MessageNotificationService.Notify(endpoint, message.toString());
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
