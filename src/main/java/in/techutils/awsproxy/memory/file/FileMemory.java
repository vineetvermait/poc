package in.techutils.awsproxy.memory.file;

import java.io.Serializable;

import java.util.List;

public class FileMemory implements Serializable {
    public FileMemory() {
        super();
    }

    public void setQueueURL(String queueURL) {
        this.queueURL = queueURL;
    }

    public String getQueueURL() {
        return queueURL;
    }

    public void setListeners(List<String> listeners) {
        this.listeners = listeners;
    }

    public List<String> getListeners() {
        return listeners;
    }

    private String queueURL;
    private List<String> listeners;
}
