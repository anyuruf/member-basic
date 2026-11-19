package net.anyuruf.memberbasic.config;

import reactor.blockhound.BlockHound;
import reactor.blockhound.integration.BlockHoundIntegration;

public class MemberBasicBlockHoundIntegration implements BlockHoundIntegration {

    @Override
    public void applyTo(BlockHound.Builder builder) {
    			     builder
    			     .allowBlockingCallsInside("org.springframework.validation.beanvalidation.SpringValidatorAdapter", "validate");     
    }
}
