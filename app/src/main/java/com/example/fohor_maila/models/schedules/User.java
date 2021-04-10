package com.example.fohor_maila.models.schedules;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class User {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("fullName")
    @Expose
    private String fullName;
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("passwordChangedAt")
    @Expose
    private String passwordChangedAt;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("lastLoginAt")
    @Expose
    private Object lastLoginAt;
    @SerializedName("remember_token")
    @Expose
    private Object rememberToken;
    @SerializedName("subscribed")
    @Expose
    private Boolean subscribed;
    @SerializedName("gender")
    @Expose
    private String gender;
    @SerializedName("isAdmin")
    @Expose
    private Boolean isAdmin;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("resetPasswordToken")
    @Expose
    private Object resetPasswordToken;
    @SerializedName("resetPasswordExpiresIn")
    @Expose
    private Object resetPasswordExpiresIn;
    @SerializedName("slug")
    @Expose
    private String slug;
    @SerializedName("createdAt")
    @Expose
    private String createdAt;
    @SerializedName("updatedAt")
    @Expose
    private String updatedAt;
    @SerializedName("deletedAt")
    @Expose
    private Object deletedAt;
    @SerializedName("subscription_id")
    @Expose
    private Object subscriptionId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPasswordChangedAt() {
        return passwordChangedAt;
    }

    public void setPasswordChangedAt(String passwordChangedAt) {
        this.passwordChangedAt = passwordChangedAt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Object getLastLoginAt() {
        return lastLoginAt;
    }

    public void setLastLoginAt(Object lastLoginAt) {
        this.lastLoginAt = lastLoginAt;
    }

    public Object getRememberToken() {
        return rememberToken;
    }

    public void setRememberToken(Object rememberToken) {
        this.rememberToken = rememberToken;
    }

    public Boolean getSubscribed() {
        return subscribed;
    }

    public void setSubscribed(Boolean subscribed) {
        this.subscribed = subscribed;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Boolean getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(Boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Object getResetPasswordToken() {
        return resetPasswordToken;
    }

    public void setResetPasswordToken(Object resetPasswordToken) {
        this.resetPasswordToken = resetPasswordToken;
    }

    public Object getResetPasswordExpiresIn() {
        return resetPasswordExpiresIn;
    }

    public void setResetPasswordExpiresIn(Object resetPasswordExpiresIn) {
        this.resetPasswordExpiresIn = resetPasswordExpiresIn;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Object getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(Object deletedAt) {
        this.deletedAt = deletedAt;
    }

    public Object getSubscriptionId() {
        return subscriptionId;
    }

    public void setSubscriptionId(Object subscriptionId) {
        this.subscriptionId = subscriptionId;
    }

}
