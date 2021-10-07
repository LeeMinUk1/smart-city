package kr.ac.uos.smartcity.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class Response<T> {
    private T payload;
    private boolean succeeded;
    private String message;

    public Response(T payload) {
        this.succeeded = true;
        this.payload = payload;
    }

    public Response(String message) {
        this.succeeded = false;
        this.message = message;
    }
}
