package com.friday;

public record User(long id, String prefix, String firstName, String middleName, String lastName, String suffix,String email, String phone) {
}