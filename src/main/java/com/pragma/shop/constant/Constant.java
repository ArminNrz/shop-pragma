package com.pragma.shop.constant;

public class Constant {

    /*
    common
     */
    public static final String BASE_URL = "/api/shopping";
    public static final String VERSION = "/v1";

    /*
    user
     */
    public static final String APP_USER_NOT_FOUND = "User not found.";
    public static final String APP_USER_NAME_EMPTY = "User name is empty.";
    public static final String APP_USER_PHONE_NUMBER_EMPTY = "User phone number is empty.";
    public static final String APP_USER_PASSWORD_EMPTY = "User password is empty.";
    public static final String APP_USER_PHONE_NUMBER_ITERATED = "User with this phone number is exist before.";

    /*
    Product
     */
    public static final String PRODUCT_NOT_EXIST = "Product not exist.";
    public static final String PRODUCT_COUNT_EMPTY = "Product count is empty.";
    public static final String PRODUCT_NOT_ENOUGH = "Product is not enough for this order.";

    /*
    Cart
     */
    public static final String CART_AMOUNT_NOT_ENOUGH = "Cart order count not enough.";
    public static final String CART_NOT_EXIST = "Cart for this user is not exist.";
}
