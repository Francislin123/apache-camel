package com.walmart.feeds.api.resources.feed.validator.annotation;

public @interface ValidSlug {
    String message() default "{com.walmart.validation.constraints.ValidSlug.message}";
    boolean isValid() default true;
}
