package websiteschema.mpsegment.web.ui.model;

public class UserDto {
    public int id;
    public String firstName;
    public String lastName;
    public String email;

    public static UserDto toDto(User user) {
        UserDto userDto = new UserDto();
        userDto.id = user.getId();
        userDto.firstName = user.getFirstName();
        userDto.lastName = user.getLastName();
        userDto.email = user.getEmail();
        return userDto;
    }
}
