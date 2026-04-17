package com.ucc.system.interfaces;
 
public interface MicroserviceProxy<T> {
    T execute(String operation, Object... params);
}