package com.codingexercise.configuration;

import java.time.LocalDateTime;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonFormat;

public class CustomErrorResponse {

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
  private LocalDateTime timestamp;
  private int status;
  private String error;
  private String path;
  private List<String> messages;

  public CustomErrorResponse(LocalDateTime timestamp, int status, String error, String path,
      List<String> messages) {
    this.timestamp = timestamp;
    this.status = status;
    this.error = error;
    this.path = path;
    this.messages = messages;
  }

  public LocalDateTime getTimestamp() {
    return timestamp;
  }

  public int getStatus() {
    return status;
  }

  public String getError() {
    return error;
  }

  public List<String> getMessages() {
    return messages;
  }

  public String getPath() {
    return path;
  }
}
