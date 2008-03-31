package com.welmo.communication;

interface IXMPPService {
  boolean isConnected();
  void closeConnection();
  boolean openConnection();
  void setConnectionServer(String host, String service, String port);
  void setLoginInfo(String user, String password);
  boolean SendMessage(String recipient, String message);
}
