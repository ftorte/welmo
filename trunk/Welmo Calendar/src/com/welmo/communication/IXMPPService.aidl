package com.welmo.communication;

interface IXMPPService {
  boolean isConnected();
  void closeConnection();
  boolean openConnection();
  void setConnectionServer(String host, String service, String port);
  String getServerInfo();
  void setLoginInfo(String user, String password);
  String getLoginInfo();
  boolean SendMessage(String id, String recipient, String message,String CLSID);
  int PendingMessage();
  void MessageHandled(int nMessages);
}
