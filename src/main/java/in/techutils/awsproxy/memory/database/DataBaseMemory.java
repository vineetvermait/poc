package in.techutils.awsproxy.memory.database;

import in.techutils.awsproxy.dto.SQSListenerRegisteryRequest;
import in.techutils.awsproxy.proxy.SQSProxy;

import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;

public class DataBaseMemory {
    public DataBaseMemory() {
        super();
    }

    public static void saveToDatabase(String region, String queueUrl, List<String> listeners) throws SQLException {
        DataSourceClient dsc = DataSourceClient.getInstance();
        QueryRunner run = new QueryRunner(dsc.getDataSource());

        String purgeOldData =
            "delete from sqs_listeners where region='" + region + "' and queue_url='" + queueUrl + "'";
        run.update(purgeOldData);

        for (String req : listeners) {
            String createNewData =
                "insert into sqs_listeners (region, queue_url, listener_url) values ('" + region + "', '" + queueUrl +
                "', '" + req + "')";
            run.update(createNewData);
        }
    }

    public static void reinitializeFromDatabase() throws SQLException { //throws Exception
        DataSourceClient dsc = DataSourceClient.getInstance();
        QueryRunner run = new QueryRunner(dsc.getDataSource());

        run.update("update runstamp set version_number=" + Calendar.getInstance().getTimeInMillis());

        ResultSetHandler<List<SQSListenerRegisteryRequest>> h =
            new ResultSetHandler<List<SQSListenerRegisteryRequest>>() {
                @Override
                public List<SQSListenerRegisteryRequest> handle(ResultSet rs) throws SQLException {
                    List<SQSListenerRegisteryRequest> data = new ArrayList<SQSListenerRegisteryRequest>();
                    while (rs.next()) {
                        String region = rs.getString("REGION");
                        String queueUrl = rs.getString("QUEUE_URL");
                        String listener = rs.getString("LISTENER_URL");

                        SQSListenerRegisteryRequest req = new SQSListenerRegisteryRequest(region, queueUrl, listener);
                        data.add(req);
                    }
                    return data;
                }
            };

        List<SQSListenerRegisteryRequest> data = run.query("select * from sqs_listeners", h);

        for (SQSListenerRegisteryRequest req : data) {
            SQSProxy.registerQueueListener(req);
        }
    }

    public static long getRunStamp() throws SQLException {
        DataSourceClient dsc = DataSourceClient.getInstance();
        QueryRunner run = new QueryRunner(dsc.getDataSource());
        ResultSetHandler<Long> h = new ResultSetHandler<Long>() {
            @Override
            public Long handle(ResultSet rs) throws SQLException {
                while (rs.next()) {
                    return rs.getLong(1);
                }
                return 0L;
            }
        };

        long runstamp = run.query("select version_number from runstamp", h);
        return runstamp;
    }
}
