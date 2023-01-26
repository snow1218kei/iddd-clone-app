package com.saasovation.agilepm.application.product;

import lombok.AllArgConstructor;
import lombok.Data;

// コンストラクタの省略
@AllArgsConstructor
// セッター・ゲッターの省略
@Data
public class ProductApplicationService {
    private TimeConstrainedProcessTrackerRepository processTrackerRepository;
    private ProductOwnerRepository productOwnerRepository;
    private ProductRepository productRepository;

    public void initiateDiscussion(String tenantId, String productId, String discussionId) {

        try {
            Product product =
                    this.productRepository
                            .findProductById(
                                    new TenantId(tenantId),
                                    new ProductId(productId)
                            );

            if (product.isEmpty) {
                throwProductNotFoundError(tenantId, productId)
            }

            product.initiateDiscussion(
                    new DiscussionDescriptor(discussionId)
            );

            this.productRepository.save(product);

            trackDiscussionInitiation(product, tenantId, productId)

        } catch (RuntimeException e) {
            // エラーをキャッチするメソッド
        }

        private void throwProductNotFoundError(tenantId, productId) {
            String errorMessage =
                    String.format(
                            "tenant idが%sでproduct idが%sのproductは存在しません",
                            tenantId,
                            productId
                    );

            throw new IllegalStateException(errorMessage);
        }

        private void trackDiscussionInitiation(product, tenantId, productId){
            ProcessId processId = ProcessId.existingProcessId(product.discussionInitiationId());

            TimeConstrainedProcessTracker tracker =
                    this.processTrackerRepository
                            .findTrackerByProcessId(tenantId, processId);

            tracker.completed();

            this.processTrackerRepository().save(tracker);
        }
    }
}