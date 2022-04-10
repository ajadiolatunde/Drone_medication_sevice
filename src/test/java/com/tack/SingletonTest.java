package com.tack;

import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

class SingletonTest {
    @Mock
    JSONObject drone_js;
    @Mock
    JSONObject medication_js;
    @Mock
    Drone selected_drone;
    @Mock
    Singleton single_instance;
    @InjectMocks
    Singleton singleton;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testGetInstance() {
        Singleton result = Singleton.getInstance();
        Assertions.assertEquals(new Singleton(), result);
    }
}

