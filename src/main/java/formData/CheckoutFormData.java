package formData;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CheckoutFormData {
    private String firstName;
    private String lastName;
    private String country;
    private String address;
    private String city;
    private String state;
    private String postcode;
    private String phone;

}
