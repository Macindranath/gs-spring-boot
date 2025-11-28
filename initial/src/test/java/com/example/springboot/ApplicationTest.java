package com.example.springboot;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

// Tests for the main Application class
@SpringBootTest
class ApplicationTest {

    @Test
    void contextLoads() {
        // This checks if Spring starts up correctly
    }

    @Test
    public void main() {
        // This forces the main method to run, getting you 100% on this class
        Application.main(new String[] {});
    }
}