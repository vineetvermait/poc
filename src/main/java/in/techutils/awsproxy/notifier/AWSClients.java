package in.techutils.awsproxy.notifier;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.sns.AmazonSNSAsync;
import com.amazonaws.services.sns.AmazonSNSAsyncClientBuilder;
import com.amazonaws.services.sqs.AmazonSQSAsync;
import com.amazonaws.services.sqs.AmazonSQSAsyncClientBuilder;

import java.util.HashMap;
import java.util.Map;

public class AWSClients {
    private static Map<String, AmazonSQSAsync> SQSClientMap = new HashMap<String, AmazonSQSAsync>();
    private static Map<String, AmazonSNSAsync> SNSClientMap = new HashMap<String, AmazonSNSAsync>();

    private AWSClients() {
    }

    private static AWSCredentialsProvider getCredentials() {
        String accessKey = "mock-credentials-accessKey";
        String secretKey = "mock-credentials-secretKey";
        AWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);
        AWSCredentialsProvider credentialsProvider = new AWSStaticCredentialsProvider(credentials);
        return credentialsProvider;
    }

    public static AmazonSQSAsync getAWSSQSClient(String region) {
        if (SQSClientMap.get(region) == null) {
            SQSClientMap.put(region, AmazonSQSAsyncClientBuilder
                    .standard()
                    .withRegion(region)
//                  .withEndpointConfiguration(getEndpointConfiguration("sqs", region))
                    .withCredentials(getCredentials())
                    .build());
        }
        return SQSClientMap.get(region);
    }

    public static AmazonSNSAsync getAWSSNSClient(String region) {
        if (SNSClientMap.get(region) == null) {
            SNSClientMap.put(region, AmazonSNSAsyncClientBuilder
                    .standard()
                    .withRegion(region)
//                    .withEndpointConfiguration(getEndpointConfiguration("sns", region))
                    .withCredentials(getCredentials())
                    .build());
        }
        return SNSClientMap.get(region);
    }

//    private static AwsClientBuilder.EndpointConfiguration getEndpointConfiguration(String service, String region) {
//        return new AwsClientBuilder.EndpointConfiguration("http://" + service + "." + region + ".amazonaws.com", region);
//    }
}
