//package in.techutils.awsproxy;
//
//import in.techutils.awsproxy.dto.SQSListenerRegisteryRequest;
//import in.techutils.awsproxy.proxy.SNSProxy;
//import in.techutils.awsproxy.proxy.SQSProxy;
//
//import java.util.List;
//
//import javax.ws.rs.FormParam;
//import javax.ws.rs.GET;
//import javax.ws.rs.POST;
//import javax.ws.rs.PUT;
//import javax.ws.rs.Path;
//import javax.ws.rs.PathParam;
//import javax.ws.rs.Produces;
//import javax.ws.rs.core.Response;
//
//
//@Path("/v1")
//public class AWSProxyService {
//
//    @GET
//    @Path("/SNS/{region}")
//    @Produces("text/plain")
//    public Response listSNSTopics(@PathParam("region") String region) {
//        List<String> topics = null;
//        try {
//            topics = new SNSProxy(region).listTopics();
//        } catch (Exception e) {
//            return Response.serverError()
//                           .entity(e.getMessage())
//                           .build();
//        }
//        return Response.ok()
//                       .entity(topics.toString())
//                       .build();
//
//    }
//
//    @GET
//    @Path("/SQS/{region}")
//    @Produces("text/plain")
//    public Response listSQSQueueUrls(@PathParam("region") String region) {
//        List<String> urls = null;
//        try {
//            urls = new SQSProxy(region).listQueueURLs();
//        } catch (Exception e) {
//            return Response.serverError()
//                           .entity(e.getMessage())
//                           .build();
//        }
//        return Response.ok()
//                       .entity(urls.toString())
//                       .build();
//    }
//
//    @PUT
//    @Produces("text/plain")
//    @Path("/register/{region}")
//    public Response registerQueueListener(@PathParam("region") String region, @FormParam("queueURL") String queueURL,
//                                          @FormParam("receivingEndpoint") String receivingEndpoint) {
//        SQSListenerRegisteryRequest req = new SQSListenerRegisteryRequest(region, queueURL, receivingEndpoint);
//        try {
//            SQSProxy.registerQueueListener(req);
//            SQSProxy.saveListeners(req);
//        } catch (Exception e) {
//            return Response.serverError()
//                           .entity(e.getMessage())
//                           .build();
//        }
//        return Response.accepted().build();
//    }
//
//    @PUT
//    @Produces("text/plain")
//    @Path("/deregister/{region}")
//    public Response deregisterQueueListener(@PathParam("region") String region, @FormParam("queueURL") String queueURL,
//                                            @FormParam("receivingEndpoint") String receivingEndpoint) {
//        SQSListenerRegisteryRequest req = new SQSListenerRegisteryRequest(region, queueURL, receivingEndpoint);
//        try {
//            SQSProxy.deregisterQueueListener(req);
//            SQSProxy.saveListeners(req);
//        } catch (Exception e) {
//            return Response.serverError()
//                           .entity(e.getMessage())
//                           .build();
//        }
//        return Response.accepted().build();
//    }
//
//    @POST
//    @Produces("text/plain")
//    @Path("/sendtosns/{region}")
//    public Response publishToSNS(@PathParam("region") String region, @FormParam("topic") String topic,
//                                 @FormParam("subject") String subject, @FormParam("message") String messageBody) {
//        String messageId = "";
//        try {
//            SNSProxy proxy = new SNSProxy(region, topic);
//            messageId = proxy.sendMessage(subject, messageBody, null);
//        } catch (Exception e) {
//            return Response.serverError()
//                           .entity(e.getMessage())
//                           .build();
//        }
//        return Response.ok()
//                       .entity(messageId)
//                       .build();
//    }
//
//    @POST
//    @Produces("text/plain")
//    @Path("/sendtosqs/{region}")
//    public Response publishToSQS(@PathParam("region") String region, @FormParam("queueURL") String queueURL,
//                                 @FormParam("message") String messageBody) {
//        String messageId = "";
//        try {
//            SQSProxy proxy = new SQSProxy(queueURL, region);
//            messageId = proxy.sendMessage(messageBody, null);
//        } catch (Exception e) {
//            return Response.serverError()
//                           .entity(e.getMessage())
//                           .build();
//        }
//        return Response.ok()
//                       .entity(messageId)
//                       .build();
//    }
//
//}
