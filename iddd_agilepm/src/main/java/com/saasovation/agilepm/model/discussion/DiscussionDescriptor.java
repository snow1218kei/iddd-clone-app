package com.saasovation.agilepm.domain.model.discussion;

public class DiscussionDescriptor {

    public static final String UNDEFINED_ID = "UNDEFINED";

    private String id;

    public boolean isUndefined() {
        return this.id().equals(UNDEFINED_ID);
    }

    //　何を比較しているのかがよく分からない
    public boolean equals(Object anObject) {
        boolean equalObjects = false;

        if (anObject != null && this.getClass() == anObject.getClass()) {
            DiscussionDescriptor typedObject = (DiscussionDescriptor) anObject;
            equalObjects = this.id().equals(typedObject.id());
        }

        return equalObjects;
    }
}