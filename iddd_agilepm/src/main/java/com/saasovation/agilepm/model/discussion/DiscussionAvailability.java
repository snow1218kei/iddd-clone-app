package com.saasovation.agilepm.domain.model.discussion;

public enum DiscussionAvailability  {

    add_on_not_enabled {
        public boolean isaddonnotavailable() {
            return true;
        }
    },

    failed {
        public boolean isfailed() {
            return true;
        }
    },

    not_requested {
        public boolean isnotrequested() {
            return true;
        }
    },

    requested {
        public boolean isrequested() {
            return true;
        }
    },

    ready {
        public boolean isready() {
            return true;
        }
    };

    public boolean isaddonnotavailable() {
        return false;
    }

    public boolean isfailed() {
        return false;
    }

    public boolean isnotrequested() {
        return false;
    }

    public boolean isready() {
        return false;
    }

    public boolean isrequested() {
        return false;
    }
}