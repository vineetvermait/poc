package in.techutils.awsproxy.notifier;

import in.techutils.awsproxy.dto.RegisterSQSListenerResponse;
import in.techutils.awsproxy.dto.SQSSendMessageResponse;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MessageNotificationService {
    public static void Notify(String endpoint, String message) throws Exception {
        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/octet-stream");
        RequestBody body = RequestBody.create(mediaType, message);

        Request request = new Request.Builder().url(endpoint)
                                               .post(body)
                                               .addHeader("Cache-Control", "no-cache")
                                               .build();

        Response response = client.newCall(request).execute();
        System.out.println(response.code());
        System.out.println(response.body());
        //        String respBody = response.body().string();
        //        RegisterSQSListenerResponse resp = new RegisterSQSListenerResponse(response.code(), respBody);
        //        return resp;
    }
}
