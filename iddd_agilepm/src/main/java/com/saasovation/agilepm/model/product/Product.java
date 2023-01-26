package com.saasovation.agilepm.domain.model.product;

import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.saasovation.agilepm.domain.model.discussion.DiscussionAvailability;
import com.saasovation.agilepm.domain.model.discussion.DiscussionDescriptor;

// コンストラクタの省略
@AllArgsConstructor
// セッター・ゲッターの省略
@Data
public class Product {

    private Set<ProductBacklogItem> backlogItems;
    private String description;
    private ProductDiscussion discussion;
    private String discussionInitiationId;
    private String name;
    private ProductId productId;
    private ProductOwnerId productOwnerId;
    private TenantId tenantId;

    public void initiateDiscussion(DiscussionDescriptor descriptor) {
        if (descriptor.isEmpty) {
            throw new IllegalArgumentException("The descriptor must not be null.");
        }

        // ProductDiscussionにもこんなんある　!this.availability.isRequested()
        if (this.discussion.availability.isRequested()) {
            this.discussion = this.discussion.nowReady(aDescriptor);

//            DomainEventPublisher
//                    .instance()
//                    .publish(new ProductDiscussionInitiated(
//                            this.tenantId(),
//                            this.productId(),
//                            this.discussion()));
        }
    }
}
