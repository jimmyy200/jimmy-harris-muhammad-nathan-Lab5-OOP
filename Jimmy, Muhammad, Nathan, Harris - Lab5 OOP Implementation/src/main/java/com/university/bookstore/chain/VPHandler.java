package com.university.bookstore.chain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * VP-level discount approval handler.
 * Can approve discounts up to 40%.
 * 
 * @author Navid Mohaghegh
 * @version 3.0
 * @since 2024-09-15
 */
public class VPHandler extends DiscountHandler {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(VPHandler.class);
    private static final double MAX_DISCOUNT = 0.40; // 40%
    
    @Override
    public void handleRequest(DiscountRequest request) {
        if (request.getRequestedDiscount() <= MAX_DISCOUNT) {
            // VP can approve this discount
            request.setApproved(true);
            request.setApprovedBy("VP");
            LOGGER.info("VP approved {}% discount for {}", 
                       request.getRequestedDiscountPercentage(), request.getMaterial().getTitle());
        } else {
            // VP cannot approve, reject the request
            request.setRejectionReason("Discount too high - exceeds VP approval limit");
            LOGGER.info("VP rejected discount > {}%", (MAX_DISCOUNT * 100));
        }
    }
    
    @Override
    public String getHandlerName() {
        return "VPHandler";
    }
    
    @Override
    public double getMaxDiscount() {
        return MAX_DISCOUNT;
    }
}
