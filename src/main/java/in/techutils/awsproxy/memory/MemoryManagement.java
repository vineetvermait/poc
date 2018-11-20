package in.techutils.awsproxy.memory;

import in.techutils.awsproxy.dto.SQSListenerRegisteryRequest;
import in.techutils.awsproxy.memory.database.DataBaseMemory;
import in.techutils.awsproxy.memory.file.FileMemory;
import in.techutils.awsproxy.proxy.SQSProxy;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import java.io.ObjectInputStream;

import java.io.ObjectOutputStream;

import java.sql.SQLException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

public class MemoryManagement {
    private static String memoryManager = "INMEMORY";
    private static String seperator = "###";
    private static String path = "/";
    private static Properties prop = new Properties();

    static {
        InputStream input = null;
        try {
            input = MemoryManagement.class.getClassLoader().getResourceAsStream("config.properties");
            prop.load(input);
            memoryManager = prop.getProperty("mode");
            path = prop.getProperty("path");
        } catch (Exception ex) {
            System.out.println("Couldnt Find Props File. Failing Back to InMemory");
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        System.out.println(memoryManager);
    }

    public static void save(String region, String queueUrl, List<String> listeners) throws SQLException {
        if ("FILE".equals(memoryManager)) {
            String fileName = path + region + ".ser";
            Map<String, List<String>> regionFiles = new HashMap<String, List<String>>();
            try {
                FileInputStream fis = new FileInputStream(fileName);
                ObjectInputStream ois = new ObjectInputStream(fis);
                regionFiles = (Map<String, List<String>>) ois.readObject(); //
            } catch (FileNotFoundException e) {
                System.out.println("File Not found");
                regionFiles.put(queueUrl, new ArrayList<String>());
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

            regionFiles.put(queueUrl, listeners);

            try {
                FileOutputStream fos = new FileOutputStream(fileName);
                ObjectOutputStream oos = new ObjectOutputStream(fos);
                oos.writeObject(regionFiles);
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else if ("DB".equals(memoryManager)) {
            DataBaseMemory.saveToDatabase(region, queueUrl, listeners);
        }
    }

    public static void reinitializeFromDatabase() throws SQLException {
        if ("FILE".equals(memoryManager)) {
            File directory = new File(path);
            File[] files = directory.listFiles();

            for (File file : files) {
                String region = file.getName().replace(".ser", "");
                String fileName = path + region + ".ser";
                System.out.println(fileName);
                Map<String, List<String>> regionFiles = new HashMap<String, List<String>>();
                try {
                    FileInputStream fis = new FileInputStream(fileName);
                    ObjectInputStream ois = new ObjectInputStream(fis);
                    regionFiles = (Map<String, List<String>>) ois.readObject();
                } catch (FileNotFoundException e) {
                    System.out.println("File Not found");
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                List<SQSListenerRegisteryRequest> data = new ArrayList<SQSListenerRegisteryRequest>();

                for (String queueUrl : regionFiles.keySet()) {
                    List<String> listeners = regionFiles.get(queueUrl);
                    System.out.println(region);
                    System.out.println(queueUrl);
                    System.out.println(listeners);
                    for (String listener : listeners) {
                        System.out.println(region + "\t" + queueUrl + "\t" + listener);
                        data.add(new SQSListenerRegisteryRequest(region, queueUrl, listener));
                    }
                }
                for (SQSListenerRegisteryRequest sQSListenerRegisteryRequest : data) {
                    SQSProxy.registerQueueListener(sQSListenerRegisteryRequest);
                }
            }

        } else if ("DB".equals(memoryManager)) {
            DataBaseMemory.reinitializeFromDatabase();
        }
    }

    public static long getRunStamp() throws SQLException {
        long versionNumber = 0L;
        if ("FILE".equals(memoryManager)) {
            versionNumber = Long.parseLong(prop.getProperty("version_number"));
        } else if ("DB".equals(memoryManager)) {
            versionNumber = DataBaseMemory.getRunStamp();
        }
        return versionNumber;
    }
}
