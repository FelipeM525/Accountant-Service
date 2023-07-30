package account.response;

public class ChangePasswordResponse {

    private String email;
    private final String status = "The password has been updated successfully";

    public ChangePasswordResponse() {
    }

    public ChangePasswordResponse(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email.toLowerCase();
    }

    public String getStatus() {
        return status;
    }
}
