package in.techutils.awsproxy.proxy;

import com.amazonaws.handlers.AsyncHandler;
import com.amazonaws.services.sqs.AmazonSQSAsync;
import com.amazonaws.services.sqs.model.MessageAttributeValue;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.amazonaws.services.sqs.model.ReceiveMessageResult;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageResult;

//import in.techutils.awsproxy.memory.database.DataBaseMemory;
import in.techutils.awsproxy.dto.SQSListenerRegisteryRequest;
import in.techutils.awsproxy.memory.MemoryManagement;
import in.techutils.awsproxy.notifier.AWSClients;
import in.techutils.awsproxy.proxy.handler.QueueHandler;

import java.sql.SQLException;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SQSProxy implements Runnable {

    private String region, queueURL;
    private AmazonSQSAsync client;
    private static boolean running = true;
    private static Map<String, List<String>> registry = new HashMap<String, List<String>>();
    private long runstamp = Calendar.getInstance().getTimeInMillis();


    public SQSProxy(String queueURL, String region) {
        this.region = region;
        this.queueURL = queueURL;
        client = AWSClients.getAWSSQSClient(region);
    }

    public SQSProxy(String region) {
        this.region = region;
        client = AWSClients.getAWSSQSClient(region);
        System.out.println("Initialized");
    }

    public static void saveListeners(SQSListenerRegisteryRequest req) {
        try {
            List<String> listeners = registry.get(req.getQueueURL());
            MemoryManagement.save(req.getRegion(), req.getQueueURL(), listeners);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public static void registerQueueListener(SQSListenerRegisteryRequest req) throws SQLException {
        List<String> listeners = registry.get(req.getQueueURL());
        System.out.println("listeners:" + listeners);
        if (listeners == null || listeners.size() == 0) { //Start of Thread
            listeners = new ArrayList<String>();
            registry.put(req.getQueueURL(), listeners);
            SQSProxy proxy = new SQSProxy(req.getQueueURL(), req.getRegion());
            new Thread(proxy).start();
        }

        if (!listeners.contains(req.getReceivingEndpoint())) {
            listeners.add(req.getReceivingEndpoint());
        }
        System.out.println("Registered Listener: " + req.getReceivingEndpoint());
    }

    public static void deregisterQueueListener(SQSListenerRegisteryRequest req) throws SQLException {
        List<String> listeners = registry.get(req.getQueueURL());
        if (listeners == null) {
            listeners = new ArrayList<String>();
        }
        while (listeners.remove(req.getReceivingEndpoint())) {
            System.out.println("removed:" + req.getReceivingEndpoint());
        }

        if (listeners.size() == 0) {
            System.out.println("Cleaning Registry");
            registry.put(req.getQueueURL(), null);
            System.out.println(registry);
        }
        System.out.println(req.getQueueURL() + ": " + listeners);
        System.out.println("Deregistered Listener: " + req.getReceivingEndpoint());
    }

    public void run() {
        System.out.println("Started Listening on Queue: " + queueURL);
        while (running) {

            try {
                long dbrunstamp = MemoryManagement.getRunStamp();
                if (dbrunstamp > runstamp) {
                    System.out.println("New Version Deployed. Ending Listeners of version " + runstamp);
                    break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            List<String> listeners = registry.get(queueURL);
            if (listeners == null || listeners.size() == 0) {
                //                running = false;
                registry.remove(queueURL);
                System.out.println("Killing Thread Listening on " + queueURL);
                break;
            }
            if (running && queueURL != null) {
                AsyncHandler<ReceiveMessageRequest, ReceiveMessageResult> handler =
                        new QueueHandler<ReceiveMessageRequest, ReceiveMessageResult>(queueURL, region);
                ReceiveMessageRequest awsReq = new ReceiveMessageRequest(queueURL);
                awsReq.setMaxNumberOfMessages(10);
                awsReq.setWaitTimeSeconds(20);
                client.receiveMessageAsync(awsReq, handler);
            }
            try {
                System.out.println("Will try in another 10 seconds for queue: " + queueURL + " !!!");
                Thread.sleep(10000); //10 Sec Sleep
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public List<String> listQueueURLs() {
        return client.listQueues().getQueueUrls();
    }

    public String sendMessage(String messageBody, Map<String, String> attributes) {
        if (queueURL != null) {
            SendMessageRequest req = new SendMessageRequest(this.queueURL, messageBody);
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
            SendMessageResult res = client.sendMessage(req);
            return res.getMessageId();
        }
        return null;
    }

    public static void setRegistry(Map<String, List<String>> registry) {
        SQSProxy.registry = registry;
    }

    public static Map<String, List<String>> getRegistry() {
        return registry;
    }

}
