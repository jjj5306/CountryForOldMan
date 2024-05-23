package com.cbnusoftandriod.countryforoldman.repository;

/**
 * 데이터베이스의 테이블 구조 정의
 */
public class UserEntity {
    public static final String TABLE_NAME = "user";
    public static final String COLUMN_NAME_USERNAME = "username";
    public static final String COLUMN_NAME_PHONENUMBER = "phonenumber";
    public static final String COLUMN_NAME_PASSWORD = "password";
    public static final String COLUMN_NAME_ADDRESS = "address";
    public static final String COLUMN_NAME_ROLE = "role"; // role 정의 수정

    public static final String SQL_CREATE_USER_TABLE =
            "CREATE TABLE " + UserEntity.TABLE_NAME + " (" +
                    "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    UserEntity.COLUMN_NAME_USERNAME + " TEXT," +
                    UserEntity.COLUMN_NAME_PHONENUMBER + " TEXT," +
                    UserEntity.COLUMN_NAME_PASSWORD + " TEXT," +
                    UserEntity.COLUMN_NAME_ADDRESS + " TEXT," +
                    UserEntity.COLUMN_NAME_ROLE + " TEXT)"; // 쉼표 추가

    public static final String SQL_DELETE_USER_ENTRIES =
            "DROP TABLE IF EXISTS " + TABLE_NAME;
}
