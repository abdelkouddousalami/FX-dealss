package com.bloomberg.fxdeals.controller;

import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;


@ApplicationPath("/")
public class RestApplication extends Application {
    // JAX-RS will automatically discover and register all resources
}
