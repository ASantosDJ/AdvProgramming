public abstract class MemberInfo {

    private String id;
    private String serverHost;
    private String serverPort;
    private String clientName;
    private Boolean isCoordinator;

    public MemberInfo(String id, String serverHost, String serverPort, String clientName, Boolean isCoordinator) {
        this.id = id;
        this.serverHost = serverHost;
        this.serverPort = serverPort;
        this.clientName = clientName;
        this.isCoordinator = isCoordinator;
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
    public Boolean getIsCoordinator() {
    	return isCoordinator;
    }

    @Override
    public String toString() {
        return "MemberInfo{" +
                "id='" + id + '\'' +
                ", serverHost='" + serverHost + '\'' +
                ", serverPort='" + serverPort + '\'' +
                ", clientName='" + clientName + '\'' +
                ", Is Coordinator='" + isCoordinator + '\'' +
                '}';
    }
}
