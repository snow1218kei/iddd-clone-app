package com.saasovation.collaboration.application.calendar;

public class CalendarApplicationServiceTest {

    @Mock
    private CalendarRepository calendarRepository;
    @Mock
    private CalendarEntryRepository calendarEntryRepository;
    @Mock
    private CalendarIdentityService calendarIdentityService;
    @Mock
    private CollaboratorService collaboratorService;
    @Mock
    private Tenant tenant;
    @Mock
    private Calendar calendar;

    @InjectMocks
    private CalendarApplicationService calendarApplicationService;

    @Before
    private void initMocks() {
       MockitoAnnotations.initMocks(this);
    }

    public void ChangeCalendarDescription() throws Exception {


        Calendar calendar = this.calendarAggregate();

        DomainRegistry.calendarRepository().save(calendar);

        calendarApplicationService
            .changeCalendarDescription(
                    calendar.tenant().id(),
                    calendar.calendarId().id(),
                    "This is a changed description.");

        Calendar changedCalendar =
                DomainRegistry
                    .calendarRepository()
                    .calendarOfId(
                            calendar.tenant(),
                            calendar.calendarId());

        assertNotNull(changedCalendar);
        assertFalse(calendar.description().equals(changedCalendar.description()));
        assertEquals("This is a changed description.", changedCalendar.description());
    }

    private Calendar calendarAggregate() {
        Tenant tenant = new Tenant("01234567");

        Calendar calendar =
            new Calendar(
                    tenant,
                    DomainRegistry.calendarRepository().nextIdentity(),
                    "John Doe's Calendar",
                    "John Doe's everyday work calendar.",
                    new Owner("jdoe", "John Doe", "jdoe@saasovation.com"),
                    new TreeSet<CalendarSharer>());

        return calendar;
    }
//
//     public void testCreateCalendar() throws Exception {
//
//         CalendarCommandResult result = new CalendarCommandResult() {
//             @Override
//             public void resultingCalendarId(String aCalendarId) {
//                 calendarId = aCalendarId;
//             }
//             @Override
//             public void resultingCalendarEntryId(String aCalendarEntryId) {
//                 throw new UnsupportedOperationException("Should not be reached.");
//             }
//         };
//
//         String tenantId = "01234567";
//
//         Set<String> sharerWith = new HashSet<String>(0);
//         sharerWith.add("participant1");
//
//         calendarApplicationService
//             .createCalendar(
//                     tenantId,
//                     "Personal Training",
//                     "My personal training calendar.",
//                     "owner1",
//                     sharerWith,
//                     result);
//
//         Calendar calendar =
//                 DomainRegistry
//                     .calendarRepository()
//                     .calendarOfId(
//                             new Tenant(tenantId),
//                             new CalendarId(calendarId));
//
//         assertNotNull(calendar);
//         assertEquals(tenantId, calendar.tenant().id());
//         assertEquals(calendarId, calendar.calendarId().id());
//         assertEquals("Personal Training", calendar.name());
//         assertEquals("My personal training calendar.", calendar.description());
//         assertEquals("owner1", calendar.owner().identity());
//         assertEquals(1, calendar.allSharedWith().size());
//         assertEquals("participant1", calendar.allSharedWith().iterator().next().participant().identity());
//     }
//
//     public void testRenameCalendar() throws Exception {
//
//         Calendar calendar = this.calendarAggregate();
//
//         DomainRegistry.calendarRepository().save(calendar);
//
//         calendarApplicationService
//             .renameCalendar(
//                     calendar.tenant().id(),
//                     calendar.calendarId().id(),
//                     "My Training Calendar");
//
//         Calendar changedCalendar =
//                 DomainRegistry
//                     .calendarRepository()
//                     .calendarOfId(
//                             calendar.tenant(),
//                             calendar.calendarId());
//
//         assertNotNull(changedCalendar);
//         assertFalse(calendar.name().equals(changedCalendar.name()));
//         assertEquals("My Training Calendar", changedCalendar.name());
//     }
//
//     public void testScheduleCalendarEntry() throws Exception {
//
//         Calendar calendar = this.calendarAggregate();
//
//         DomainRegistry.calendarRepository().save(calendar);
//
//         CalendarCommandResult result = new CalendarCommandResult() {
//             @Override
//             public void resultingCalendarId(String aCalendarId) {
//                 calendarId = aCalendarId;
//             }
//             @Override
//             public void resultingCalendarEntryId(String aCalendarEntryId) {
//                 calendarEntryId = aCalendarEntryId;
//             }
//         };
//
//         Date now = new Date();
//         Date nextWeek = new Date(now.getTime() + (86400000L * 7L));
//         Date nextWeekPlusOneHour = new Date(nextWeek.getTime() + (1000 * 60 * 60));
//
//         calendarApplicationService
//             .scheduleCalendarEntry(
//                     calendar.tenant().id(),
//                     calendar.calendarId().id(),
//                     "My annual checkup appointment",
//                     "Family Health Offices",
//                     "owner1",
//                     nextWeek,
//                     nextWeekPlusOneHour,
//                     "DoesNotRepeat",
//                     nextWeekPlusOneHour,
//                     "Hours",
//                     8,
//                     new HashSet<String>(0),
//                     result);
//
//         CalendarEntry calendarEntry =
//                 DomainRegistry
//                     .calendarEntryRepository()
//                     .calendarEntryOfId(
//                             calendar.tenant(),
//                             new CalendarEntryId(calendarEntryId));
//
//         assertNotNull(calendarEntry);
//         assertEquals("My annual checkup appointment", calendarEntry.description());
//         assertEquals("Family Health Offices", calendarEntry.location());
//         assertEquals("owner1", calendarEntry.owner().identity());
//     }
//
//     public void testShareCalendarWith() throws Exception {
//
//         Calendar calendar = this.calendarAggregate();
//
//         DomainRegistry.calendarRepository().save(calendar);
//
//         Set<String> sharerWith = new HashSet<String>(0);
//         sharerWith.add("participant1");
//         sharerWith.add("participant2");
//
//         calendarApplicationService
//             .shareCalendarWith(
//                     calendar.tenant().id(),
//                     calendar.calendarId().id(),
//                     sharerWith);
//
//         Calendar sharedCalendar =
//                 DomainRegistry
//                     .calendarRepository()
//                     .calendarOfId(
//                             calendar.tenant(),
//                             calendar.calendarId());
//
//         assertNotNull(sharedCalendar);
//         assertEquals(2, sharedCalendar.allSharedWith().size());
//
//         for (CalendarSharer sharer : sharedCalendar.allSharedWith()) {
//             assertTrue(sharer.participant().identity().equals("participant1") ||
//                        sharer.participant().identity().equals("participant2"));
//         }
//     }
//
//     public void testUnshareCalendarWith() throws Exception {
//
//         Calendar calendar = this.calendarAggregate();
//
//         DomainRegistry.calendarRepository().save(calendar);
//
//         Set<String> sharerWith = new HashSet<String>(0);
//         sharerWith.add("participant1");
//         sharerWith.add("participant2");
//         sharerWith.add("participant3");
//
//         calendarApplicationService
//             .shareCalendarWith(
//                     calendar.tenant().id(),
//                     calendar.calendarId().id(),
//                     sharerWith);
//
//         Calendar sharedCalendar =
//                 DomainRegistry
//                     .calendarRepository()
//                     .calendarOfId(
//                             calendar.tenant(),
//                             calendar.calendarId());
//
//         assertNotNull(sharedCalendar);
//         assertEquals(3, sharedCalendar.allSharedWith().size());
//
//         Set<String> unsharerWith = new HashSet<String>(sharerWith);
//         assertTrue(unsharerWith.remove("participant2"));
//
//         calendarApplicationService
//             .unshareCalendarWith(
//                     calendar.tenant().id(),
//                     calendar.calendarId().id(),
//                     unsharerWith);
//
//         sharedCalendar =
//                 DomainRegistry
//                     .calendarRepository()
//                     .calendarOfId(
//                             calendar.tenant(),
//                             calendar.calendarId());
//
//         assertEquals(1, sharedCalendar.allSharedWith().size());
//         assertEquals("participant2", sharedCalendar.allSharedWith().iterator().next().participant().identity());
//     }
}