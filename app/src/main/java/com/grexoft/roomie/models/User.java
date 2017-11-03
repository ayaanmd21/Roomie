package com.grexoft.roomie.models;

public class User {
    private final String name;
    private final String email;
    private final int gender;
    private final String photoUrl;
    private final String phone;
    private final String address;
    private final long userId;



    private User(UserBuilder builder) {
        this.name = builder.name;
        this.email = builder.email;
        this.gender = builder.gender;
        this.phone = builder.phone;
        this.address = builder.address;
        this.photoUrl = builder.photoUrl;
        this.userId = builder.userId;
    }
    public long getUserId() {
        return userId;
    }
    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public int getGender() {
        return gender;
    }

    public String getPhone() {
        return phone;
    }

    public String getAddress() {
        return address;
    }

    public static class UserBuilder {
        private final String name;
        private String email;
        private int gender;
        private String photoUrl;
        private String phone;
        private String address;
        private long userId;

        public UserBuilder(String name) {
            this.name = name;
        }

        public UserBuilder gender(int gender) {
            this.gender = gender;
            return this;
        }

        public UserBuilder phone(String phone) {
            this.phone = phone;
            return this;
        }

        public UserBuilder address(String address) {
            this.address = address;
            return this;
        }

        public UserBuilder email(String email) {
            this.email = email;
            return this;
        }

        public UserBuilder photoUrl(String photoUrl) {
            this.photoUrl = photoUrl;
            return this;
        }

        public UserBuilder userId(long userId) {
            this.userId = userId;
            return this;
        }

        public User build() {
            return new User(this);
        }

    }
}