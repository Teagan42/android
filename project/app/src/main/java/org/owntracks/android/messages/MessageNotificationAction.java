package org.owntracks.android.messages;

import android.content.Intent;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import org.owntracks.android.support.IncomingMessageProcessor;
import org.owntracks.android.support.OutgoingMessageProcessor;
import org.owntracks.android.support.Preferences;

import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import timber.log.Timber;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
              include = JsonTypeInfo.As.EXTERNAL_PROPERTY,
              property = "_type")
public class MessageNotificationAction
          extends MessageBase {

    public static MessageNotificationAction fromIntent(Intent intent) {
        MessageNotificationAction message = new MessageNotificationAction();
        message.setPublishTopic(intent.getStringExtra(EXTRA_TOPIC));
        message.setData(intent.getStringExtra(EXTRA_DATA));
        message.setRetained(intent.getBooleanExtra(EXTRA_RETAIN,
                                                   false));
        message.setNotifiationId(intent.getIntExtra(EXTRA_NOTIFIATION_ID,
                                                    0));

        return message;
    }

    public static final String EXTRA_TOPIC = "topic";
    public static final String EXTRA_DATA = "payload";
    public static final String EXTRA_RETAIN = "retained";
    public static final String EXTRA_NOTIFIATION_ID = "notificationId";

    static final String TYPE = "action";
    private static final String BASETOPIC_SUFFIX = "/cmd";
    private Map<String, Object> map = new TreeMap<>();

    private String title;
    private String publishTopic;
    private String data;
    private int notifiationId;

    @Override
    public void processIncomingMessage(IncomingMessageProcessor handler) {
        handler.processIncomingMessage(this);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPublishTopic() {
        return publishTopic;
    }

    public void setPublishTopic(String publishTopic) {
        this.publishTopic = publishTopic;
    }

    @Override
    public String getTopic() {
        return getPublishTopic();
    }

    @Override
    public void setTopic(String topic) {

    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @JsonIgnore
    public int getNotifiationId() {
        return notifiationId;
    }

    @JsonIgnore
    public void setNotifiationId(int notifiationId) {
        this.notifiationId = notifiationId;
    }

    @JsonAnyGetter
    @JsonPropertyOrder(alphabetic = true)
    public Map<String, Object> any() {
        Timber.v("getting map. length: %s",
                 map.size());
        return map;
    }

    @JsonAnySetter
    public void set(String key,
                    Object value) {
        if (value instanceof String && "".equals(value)) {
            return;
        }
        Timber.v("load key:%s, value:%s",
                 key,
                 value);

        map.put(key,
                value);
    }

    // TID would not be included in map for load otherwise
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public void setTid(String tid) {
        set(Preferences.Keys.TRACKER_ID,
            tid);
    }

    @JsonIgnore
    public Object get(String key) {
        return map.get(key);
    }

    @JsonIgnore
    public boolean containsKey(String key) {
        return map.containsKey(key);
    }

    @Override
    @JsonIgnore
    public void processOutgoingMessage(OutgoingMessageProcessor handler) {
        handler.processOutgoingMessage(this);
    }

    @Override
    @JsonIgnore
    public String getBaseTopicSuffix() {
        return null;
    }


    @JsonIgnore
    public Set<String> getKeys() {
        return map.keySet();
    }

    @JsonIgnore
    public void removeKey(String key) {
        map.remove(key);
    }

}
