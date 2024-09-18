package com.example.demo.model;

public class ResponseModel<T> {
    private String message;
    private int status;
    private T data;

    public ResponseModel(int status,String message,T data){
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public int getStatus(){
        return status;
    }
    public String getMessage(){
        return message;
    }
    public T getData(){
        return data;
    }

    public void setStatus(int status){
        this.status = status;
    }
    public void setMessage(String message){
        this.message = message;
    }
    public void setData(T data){
        this.data = data;
    }

    
}
