//package com.mindhub.homebanking;
//
//import com.mindhub.homebanking.utils.CardUtils;
//import org.junit.jupiter.api.Test;
//import org.springframework.boot.test.context.SpringBootTest;
//import static org.hamcrest.MatcherAssert.assertThat;
//import static org.hamcrest.Matchers.*;
//
//@SpringBootTest
//public class CardUtilsTests {
//
//    @Test
//    public void cardNumberIsCreatedVISA(){
//        String cardNumberVISA = CardUtils.getCardNumberVISA();
//        assertThat(cardNumberVISA,is(not(emptyOrNullString())));
//    }
//
//
//
//    @Test
//    public void cardNumberIsCreatedMasterdCard(){
//        String cardNumberMasterCard = CardUtils.getCardNumberMasterCard();
//        assertThat(cardNumberMasterCard,is(not(emptyOrNullString())));
//    }
//
//
//    @Test
//    public void cardNumberCVVIsCreated(){
//        int cardCVV = CardUtils.getCardNumberCVV();
//        assertThat(cardCVV,isA(int.class));
//    }
//    @Test
//    public void cardNumberCVVIsAny(){
//        int cardCVV = CardUtils.getCardNumberCVV();
//        assertThat(cardCVV, any(int.class));
//    }
//}
