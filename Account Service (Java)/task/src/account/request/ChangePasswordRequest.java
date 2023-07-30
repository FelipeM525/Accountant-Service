package account.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ChangePasswordRequest {
    @JsonProperty("new_password")
    private String newPassword;

    public ChangePasswordRequest() {
    }

    public ChangePasswordRequest(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
