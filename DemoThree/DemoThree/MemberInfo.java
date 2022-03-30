public class MemberInfo {

    private String id;
    private String serverHost;
    private String serverPort;
    private String clientName;

    public MemberInfo(String id, String serverHost, String serverPort, String clientName) {
        this.id = id;
        this.serverHost = serverHost;
        this.serverPort = serverPort;
        this.clientName = clientName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getServerHost() {
        return serverHost;
    }

    public void setServerHost(String serverHost) {
        this.serverHost = serverHost;
    }

    public String getServerPort() {
        return serverPort;
    }

    public void setServerPort(String serverPort) {
        this.serverPort = serverPort;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    @Override
    public String toString() {
        return "MemberInfo{" +
                "id='" + id + '\'' +
                ", serverHost='" + serverHost + '\'' +
                ", serverPort='" + serverPort + '\'' +
                ", clientName='" + clientName + '\'' +
                '}';
    }
}
