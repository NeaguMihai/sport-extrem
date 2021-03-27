package com.mihaineagu.web.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.junit.jupiter.api.BeforeAll;

public abstract class AbstractControllerTest {

    public static final String URI = "/mock/1";

    static ObjectWriter objectWriter;

    @BeforeAll
    static void createWriter(){

        objectWriter = new ObjectMapper().writer().withDefaultPrettyPrinter();

    }
}
