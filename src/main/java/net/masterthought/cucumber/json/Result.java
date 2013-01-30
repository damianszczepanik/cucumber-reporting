package net.masterthought.cucumber.json;

public class Result {

    private String status;
    private String error_message;
    private Long duration;

    public Result() {

    }

    public Result(String status) {
        this.status = status;
    }
    
    public String getStatus() {
        return status;
    }

    public Long getDuration() {
        return duration == null ? 0L : duration;
    }

    public String getErrorMessage() {
        return error_message;
    }
}
