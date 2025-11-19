package com.university.bookstore.chain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Manager-level discount approval handler.
 * Can approve discounts up to 15%.
 * 
 * @author Navid Mohaghegh
 * @version 3.0
 * @since 2024-09-15
 */
public class ManagerHandler extends DiscountHandler {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(ManagerHandler.class);
    private static final double MAX_DISCOUNT = 0.15; // 15%
    
    @Override
    public void handleRequest(DiscountRequest request) {
        if (request.getRequestedDiscount() <= MAX_DISCOUNT) {
            // Manager can approve this discount
            request.setApproved(true);
            request.setApprovedBy("Manager");
            LOGGER.info("Manager approved {}% discount for {}", 
                       request.getRequestedDiscountPercentage(), request.getMaterial().getTitle());
        } else {
            // Manager cannot approve, pass to next handler
            LOGGER.info("Manager cannot approve discount > {}%. Passing to Director.", 
                       (MAX_DISCOUNT * 100));
            passToNext(request);
        }
    }
    
    @Override
    public String getHandlerName() {
        return "ManagerHandler";
    }
    
    @Override
    public double getMaxDiscount() {
        return MAX_DISCOUNT;
    }
}
