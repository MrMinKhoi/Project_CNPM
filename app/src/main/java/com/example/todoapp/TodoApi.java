package com.example.todoapp;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.*;

public interface TodoApi {
    @GET("/api/todos")
    Call<List<Todo>> getTodos();

    @GET("/api/todos/{id}")
    Call<Todo> getTodo(@Path("id") Long id);

    @POST("/api/todos")
    Call<Todo> createTodo(@Body Todo todo);

    @PUT("/api/todos/{id}")
    Call<Todo> updateTodo(@Path("id") Long id, @Body Todo todo);

    @DELETE("/api/todos/{id}")
    Call<Void> deleteTodo(@Path("id") Long id);
}