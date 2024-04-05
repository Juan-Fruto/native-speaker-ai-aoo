package com.juanfruto.utils;
import java.util.List;

public class ValidationResult {

    private boolean success;
    private List<String> errors;

    public ValidationResult(boolean success, List<String> errors) {
        this.success = success;
        this.errors = errors;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }

    public boolean getSuccess() {
        return success;
    }

    public List<String> getErrors() {
        return errors;
    }
}
