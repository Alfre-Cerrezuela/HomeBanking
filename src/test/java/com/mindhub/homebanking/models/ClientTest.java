//package com.mindhub.homebanking.models;
//
//import org.junit.jupiter.api.Test;
//
//import java.time.LocalDateTime;
//import java.util.Set;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//class ClientTest {
//
//    Client clientTest = new Client("pedro", "prueba", "pedrito@prueba.com", "123prueba");
//    @Test
//    void getId() {
//        assertEquals(0, clientTest.getId());
//    }
//
//    @Test
//    void getFirstName() {
//        assertTrue("pedro" == clientTest.getFirstName());
//    }
//
//    @Test
//    void setFirstName() {
//        clientTest.setFirstName("maria");
//        assertEquals("maria", clientTest.getFirstName());
//    }
//
//    @Test
//    void getLastName() {
//        assertEquals("prueba", clientTest.getLastName());
//    }
//
//    @Test
//    void setLastName() {
//        clientTest.setLastName("gonzales");
//        assertEquals("gonzales", clientTest.getLastName());
//
//    }
//
//    @Test
//    void getEmail() {
//        assertSame("pedrito@prueba.com", clientTest.getEmail());
//    }
//
//    @Test
//    void EmailNotNull() {
//        assertNotNull( clientTest.getEmail());
//    }
//
//    @Test
//    void setEmail() {
//    clientTest.setEmail("pedro@gmail.com");
//    assertFalse("pedrito@prueba.com".equals(clientTest.getEmail()) );
//    }
//
//    @Test
//    void getPassword() {
//        assertEquals("123prueba", clientTest.getPassword());
//    }
//
//    @Test
//    void setPassword() {
//        clientTest.setPassword("prueba123");
//        assertNotSame("123prueba", clientTest.getPassword());
//    }
//
//}