package account.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@JsonPropertyOrder({"id", "date", "action", "subject", "object", "path"})
public class SecurityEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;
    @JsonIgnore
    private LocalDateTime date;
    @JsonProperty("action")
    private String eventName;

    private String subject;
    private String object;
    private String path;

    public SecurityEvent() {
    }

    public SecurityEvent(LocalDateTime date, String eventName, String subject, String object, String path) {
        this.date = date;
        this.eventName = eventName;
        this.subject = subject;
        this.object = object;
        this.path = path;
    }

    public SecurityEvent(String eventName, String subject, String object, String path) {
        this.date = LocalDateTime.now();
        this.eventName = eventName;
        this.subject = subject;
        this.object = object;
        this.path = path;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
