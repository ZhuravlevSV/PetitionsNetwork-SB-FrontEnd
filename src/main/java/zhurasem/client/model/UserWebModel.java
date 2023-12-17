package zhurasem.client.model;

import zhurasem.client.model.UserDto;

public class UserWebModel extends UserDto {
    public boolean someError;

    public UserWebModel() {}

    public UserWebModel(boolean someError, UserDto userDto) {
        super(userDto.getUsername(), userDto.getEmail(), userDto.getPassword());
        this.someError = someError;
    }

    public boolean isSomeError() {
        return someError;
    }

    public void setSomeError(boolean someError) {
        this.someError = someError;
    }
}
