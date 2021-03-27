package com.mihaineagu.web.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.junit.jupiter.api.BeforeAll;

public abstract class AbstractControllerTest {

    public static final String URI = "/mock/1";
    public static final String EXISTS_ERROR = "This entity already exists!";
    public static final String NOT_FOUND = "Requested entity not found!";
    public static final String OPERATION_FAILED = "The operation failed, please try again.";

    static ObjectWriter objectWriter;

    @BeforeAll
    static void createWriter(){

        objectWriter = new ObjectMapper().writer().withDefaultPrettyPrinter();

    }
}
