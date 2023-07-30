package account.mapper;

import account.domain.User;
import account.response.ChangePasswordResponse;
import account.response.EmployeePaymentResponse;
import account.response.UserResponse;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


@Component
public class Mapper {
    private final ModelMapper modelMapper;

    public Mapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public UserResponse convertUserToUserResponse(User user) {
        return modelMapper.map(user, UserResponse.class);
    }

    public UserResponse convertUserDetailsToUserResponse(UserDetails userDetails) {
        return modelMapper.map(userDetails, UserResponse.class);
    }

    public ChangePasswordResponse convertUserToChangePaswordResponse(User user) {
        return modelMapper.map(user, ChangePasswordResponse.class);
    }

    public EmployeePaymentResponse converPaymentToEmployeePaymentResponse(account.domain.Payment payment) {
        return modelMapper.map(payment, EmployeePaymentResponse.class);
    }

    public Date convertPeriodStringToDate(String period) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("MM-yyyy");
            return dateFormat.parse(period);
        } catch (ParseException e) {
            throw new RuntimeException("Wrong Date");

        }
    }


}
