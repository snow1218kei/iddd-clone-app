@SpringBootTest
public class ForumTest {

    protected Discussion discussion;
    protected DiscussionId discussionId;
    protected Forum forum;
    protected ForumId forumId;
    protected Post post;
    protected Post postAgain;
    protected PostId postId;
    protected String subject;
    protected Tenant tenant;

    @Test
    public void CreateForum() throws Exception {

        forum = this.forumAggregate();

        assertNotNull(forum.tenant());
        assertNotNull(forum.tenant().id());
        assertEquals(8, forum.tenant().id().length());
        assertEquals("jdoe", forum.creator().identity());
        assertEquals("jdoe@saasovation.com", forum.moderator().emailAddress());
        assertTrue(forum.isModeratedBy(forum.moderator()));
        assertEquals("John Doe Does DDD", forum.subject());
        assertEquals("A set of discussions about DDD for anonymous developers.", forum.description());

        DomainRegistry.forumRepository().save(forum);

        expectedEvents(1);
        expectedEvent(ForumStarted.class);

        expectedNotifications(1);
        expectedNotification(ForumStarted.class);
    }

    @Test
    public void AssignModerator() throws Exception {

        forum = this.forumAggregate();

        forum.assignModerator(new Moderator("zdoe", "Zoe Doe", "zdoe@saasovation.com"));

        assertEquals("zdoe", forum.moderator().identity());
        assertEquals("zdoe@saasovation.com", forum.moderator().emailAddress());

        DomainRegistry.forumRepository().save(forum);

        expectedEvents(2);
        expectedEvent(ForumStarted.class);
        expectedEvent(ForumModeratorChanged.class);

        expectedNotifications(2);
        expectedNotification(ForumStarted.class);
        expectedNotification(ForumModeratorChanged.class);
    }

    @Test
    public void ChangeDescription() throws Exception {

        forum = this.forumAggregate();

        forum.changeDescription("And Zoe knows...");

        assertEquals("And Zoe knows...", forum.description());

        DomainRegistry.forumRepository().save(forum);

        expectedEvents(2);
        expectedEvent(ForumStarted.class);
        expectedEvent(ForumDescriptionChanged.class);

        expectedNotifications(2);
        expectedNotification(ForumStarted.class);
        expectedNotification(ForumDescriptionChanged.class);
    }

    @Test
    public void ChangeSubject() throws Exception {

        forum = this.forumAggregate();

        forum.changeSubject("Zoe Likes DDD");

        assertEquals("Zoe Likes DDD", forum.subject());

        DomainRegistry.forumRepository().save(forum);

        expectedEvents(2);
        expectedEvent(ForumStarted.class);
        expectedEvent(ForumSubjectChanged.class);

        expectedNotifications(2);
        expectedNotification(ForumStarted.class);
        expectedNotification(ForumSubjectChanged.class);
    }

    @Test
    public void Close() throws Exception {

        forum = this.forumAggregate();

        forum.close();

        assertTrue(forum.isClosed());

        boolean failed = false;

        try {
            forum.changeDescription("Blah...");

            fail("Should have thrown exception.");

        } catch (Exception e) {
            failed = true;
        }

        assertTrue(failed);

        DomainRegistry.forumRepository().save(forum);

        expectedEvents(2);
        expectedEvent(ForumStarted.class);
        expectedEvent(ForumClosed.class);

        expectedNotifications(2);
        expectedNotification(ForumStarted.class);
        expectedNotification(ForumClosed.class);
    }

    @Test
    public void Reopen() throws Exception {

        forum = this.forumAggregate();

        forum.close();

        assertTrue(forum.isClosed());

        forum.reopen();

        assertFalse(forum.isClosed());

        try {
            forum.changeDescription("Blah...");

        } catch (Exception e) {
            fail("Should have succeeded.");
        }

        assertEquals("Blah...", forum.description());

        DomainRegistry.forumRepository().save(forum);

        expectedEvents(4);
        expectedEvent(ForumStarted.class);
        expectedEvent(ForumClosed.class);
        expectedEvent(ForumReopened.class);
        expectedEvent(ForumDescriptionChanged.class);

        expectedNotifications(4);
        expectedNotification(ForumStarted.class);
        expectedNotification(ForumClosed.class);
        expectedNotification(ForumReopened.class);
        expectedNotification(ForumDescriptionChanged.class);
    }

    @Test
    public void StartDiscussion() throws Exception {

        forum = this.forumAggregate();

        DomainRegistry.forumRepository().save(forum);

        DomainEventPublisher
                .instance()
                .subscribe(new DomainEventSubscriber<DiscussionStarted>() {
                    public void handleEvent(DiscussionStarted aDomainEvent) {
                        tenant = aDomainEvent.tenant();
                        forumId = aDomainEvent.forumId();
                        discussionId = aDomainEvent.discussionId();
                        subject = aDomainEvent.subject();
                    }
                    public Class<DiscussionStarted> subscribedToEventType() {
                        return DiscussionStarted.class;
                    }
                });

        discussion = forum.startDiscussion(
                DomainRegistry.forumIdentityService(),
                new Author("jdoe", "John Doe", "jdoe@saasovation.com"),
                "All About DDD");

        DomainRegistry.discussionRepository().save(discussion);

        assertNotNull(tenant);
        assertEquals(tenant, forum.tenant());
        assertNotNull(forumId);
        assertEquals(forumId, forum.forumId());
        assertNotNull(discussionId);
        assertNotNull(discussion);
        assertEquals(discussionId, discussion.discussionId());
        assertEquals("jdoe", discussion.author().identity());
        assertNotNull(subject);

        expectedEvents(2);
        expectedEvent(ForumStarted.class);
        expectedEvent(DiscussionStarted.class);

        expectedNotifications(2);
        expectedNotification(ForumStarted.class);
        expectedNotification(DiscussionStarted.class);
    }

    @Test
    public void ModeratedPostContent() throws Exception {

        forum = this.forumAggregate();

        DomainRegistry.forumRepository().save(forum);

        DomainEventPublisher
                .instance()
                .subscribe(new DomainEventSubscriber<DiscussionStarted>() {
                    public void handleEvent(DiscussionStarted aDomainEvent) {
                        discussionId = aDomainEvent.discussionId();
                    }
                    public Class<DiscussionStarted> subscribedToEventType() {
                        return DiscussionStarted.class;
                    }
                });

        discussion = forum.startDiscussion(
                DomainRegistry.forumIdentityService(),
                new Author("jdoe", "John Doe", "jdoe@saasovation.com"),
                "All About DDD");

        DomainRegistry.discussionRepository().save(discussion);

        post = discussion.post(
                DomainRegistry.forumIdentityService(),
                new Author("jdoe", "John Doe", "jdoe@saasovation.com"),
                "Subject",
                "Body text.");

        DomainRegistry.postRepository().save(post);

        post = DomainRegistry.postRepository().postOfId(post.tenant(), post.postId());

        forum.moderatePost(
                post,
                forum.moderator(),
                "MODERATED: Subject",
                "MODERATED: Body text.");

        DomainRegistry.postRepository().save(post);

        assertTrue(post.subject().startsWith("MODERATED: "));
        assertTrue(post.bodyText().startsWith("MODERATED: "));

        expectedEvents(4);
        expectedEvent(ForumStarted.class);
        expectedEvent(DiscussionStarted.class);
        expectedEvent(PostedToDiscussion.class);
        expectedEvent(PostContentAltered.class);

        expectedNotifications(4);
        expectedNotification(ForumStarted.class);
        expectedNotification(DiscussionStarted.class);
        expectedNotification(PostedToDiscussion.class);
        expectedNotification(PostContentAltered.class);
    }
}