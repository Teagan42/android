package org.owntracks.android.messages;

import android.app.Notification;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import org.owntracks.android.support.IncomingMessageProcessor;
import org.owntracks.android.support.MessageWaypointCollection;
import org.owntracks.android.support.OutgoingMessageProcessor;
import org.owntracks.android.support.Preferences;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import timber.log.Timber;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXTERNAL_PROPERTY, property = "_type")
public class MessageNotification
          extends MessageBase{

    public class Action {
        private String title;

        public Action() {}

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }

    static final String TYPE = "notify";
    private static final String BASETOPIC_SUFFIX = "/cmd";
    private Map<String,Object> map = new TreeMap<>();

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String title;
    private String content;
    private List<MessageNotificationAction> actions;

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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<MessageNotificationAction> getActions() {
        return actions;
    }

    public void setActions(List<MessageNotificationAction> actions) {
        this.actions = actions;
    }

    @JsonAnyGetter
    @JsonPropertyOrder(alphabetic=true)
    public Map<String,Object> any() {
        Timber.v("getting map. length: %s", map.size());
        return map;
    }

    @JsonAnySetter
    public void set(String key, Object value) {
        if(value instanceof String && "".equals(value))
            return;
        Timber.v("load key:%s, value:%s", key, value);

        map.put(key, value);
    }

    // TID would not be included in map for load otherwise
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public void setTid(String tid) {
        set(Preferences.Keys.TRACKER_ID, tid);
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
