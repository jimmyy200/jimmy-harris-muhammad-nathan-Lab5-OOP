package com.university.bookstore.chain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Director-level discount approval handler.
 * Can approve discounts up to 25%.
 * 
 * @author Navid Mohaghegh
 * @version 3.0
 * @since 2024-09-15
 */
public class DirectorHandler extends DiscountHandler {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(DirectorHandler.class);
    private static final double MAX_DISCOUNT = 0.25; // 25%
    
    @Override
    public void handleRequest(DiscountRequest request) {
        if (request.getRequestedDiscount() <= MAX_DISCOUNT) {
            // Director can approve this discount
            request.setApproved(true);
            request.setApprovedBy("Director");
            LOGGER.info("Director approved {}% discount for {}", 
                       request.getRequestedDiscountPercentage(), request.getMaterial().getTitle());
        } else {
            // Director cannot approve, pass to next handler
            LOGGER.info("Director cannot approve discount > {}%. Passing to VP.", 
                       (MAX_DISCOUNT * 100));
            passToNext(request);
        }
    }
    
    @Override
    public String getHandlerName() {
        return "DirectorHandler";
    }
    
    @Override
    public double getMaxDiscount() {
        return MAX_DISCOUNT;
    }
}
