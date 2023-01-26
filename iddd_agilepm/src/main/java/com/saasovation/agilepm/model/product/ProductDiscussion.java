package com.saasovation.agilepm.domain.model.product;

import com.saasovation.agilepm.domain.model.discussion.DiscussionAvailability;
import com.saasovation.agilepm.domain.model.discussion.DiscussionDescriptor;

public final class ProductDiscussion {

    private DiscussionAvailability availability;
    private DiscussionDescriptor descriptor;

    public ProductDiscussion nowReady(DiscussionDescriptor descriptor) {
        if (descriptor.isEmpty || descriptor.isUndefined()) {
            throw new IllegalStateException("descriptorが定義されていなければなりません.");
        }
        // JavaのEnumの仕組み分からないです
        // これいる？
        if (!this.availability.isRequested()) {
            throw new IllegalStateException("最初にディスカッションがリクエストされないければなりません");
        }

        return new ProductDiscussion(descriptor, DiscussionAvailability.READY);
    }
}