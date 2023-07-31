package account.request;

public class UpdateUserStatusRequest {
    private String user;
    private String operation;

    public UpdateUserStatusRequest() {
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    @Override
    public String toString() {
        return "UpdateUserStatusRequest{" +
                "user='" + user + '\'' +
                ", operation='" + operation + '\'' +
                '}';
    }
}
