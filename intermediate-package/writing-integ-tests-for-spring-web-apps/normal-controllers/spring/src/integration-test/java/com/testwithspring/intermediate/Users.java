package com.testwithspring.intermediate;

import com.testwithspring.intermediate.user.UserRole;

public final class Users {

    private Users() {}

    public static final Long ID_NOT_FOUND = 343L;
    public static final String USERNAME_NOT_FOUND = "notfound";

    public static class JohnDoe {

        public static final Long ID = 1L;
        public static final String NAME = "John Doe";
        public static final String PASSWORD = "user";
        public static final UserRole ROLE = UserRole.ROLE_USER;
        public static final String USERNAME = "johndoe";
    }

    public static class AnneAdmin {

        public static final Long ID = 2L;
        public static final String NAME = "Anne Admin";
        public static final String USERNAME = "admin";
    }
}
