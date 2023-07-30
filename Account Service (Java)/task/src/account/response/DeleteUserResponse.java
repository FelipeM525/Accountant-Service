package account.response;

public class DeleteUserResponse {
    private String user;
    private final String status = "Deleted successfully!";

    public DeleteUserResponse() {
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getUser() {
        return user;
    }

    public String getStatus() {
        return status;
    }
}
