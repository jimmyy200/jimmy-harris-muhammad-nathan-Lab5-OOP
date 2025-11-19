classDiagram
direction BT
class AdvancedMaterialFactory {
  + AdvancedMaterialFactory() 
  - validateMagazineProperties(Map~String, Object~) void
  - getRequiredInt(Map~String, Object~, String) int
  - getRequiredStringList(Map~String, Object~, String) List~String~
  - createPrintedBook(Map~String, Object~) PrintedBook
  + validateRequiredProperties(String, Map~String, Object~) void
  - createAudioBook(Map~String, Object~) AudioBook
  - validateEBookProperties(Map~String, Object~) void
  - getRequiredDouble(Map~String, Object~, String) double
  - createEBook(Map~String, Object~) EBook
  + createMaterial(String, Map~String, Object~) Material
  - createVideoMaterial(Map~String, Object~) VideoMaterial
  - getRequiredEnum(Map~String, Object~, String, Class~T~) T
  - validateVideoMaterialProperties(Map~String, Object~) void
  - validateRequiredKeys(Map~String, Object~, String[]) void
  - createMagazine(Map~String, Object~) Magazine
  - validatePrintedBookProperties(Map~String, Object~) void
  - validateAudioBookProperties(Map~String, Object~) void
  - getRequiredString(Map~String, Object~, String) String
  - getRequiredBoolean(Map~String, Object~, String) boolean
}
class AnalyticsData {
  + AnalyticsData(int, double, int, Map~String, Integer~, Map~String, Integer~, long, long, long, double) 
  - double averageEventRate
  - long lastEventTime
  - double totalInventoryValue
  - int uniqueMaterialCount
  - long firstEventTime
  - Map~String, Integer~ materialTypeStatistics
  - int totalEvents
  - Map~String, Integer~ eventStatistics
  - long eventTimeSpan
  + toString() String
   int totalEvents
   int uniqueMaterialCount
   long eventTimeSpan
   double totalInventoryValue
   Map~String, Integer~ materialTypeStatistics
   double averageEventRate
   long lastEventTime
   long firstEventTime
   Map~String, Integer~ eventStatistics
}
class AnalyticsObserver {
  + AnalyticsObserver() 
  - int totalEvents
  - long lastEventTime
  - long firstEventTime
  + clear() void
  + onEvent(MaterialEvent) void
  + getMaterialTypeCount(String) int
  + toString() String
  + getEventCount(String) int
   int totalEvents
   int uniqueMaterialCount
   long eventTimeSpan
   double totalInventoryValue
   Map~String, Integer~ materialTypeStatistics
   AnalyticsData analyticsData
   double averageEventRate
   String observerName
   long lastEventTime
   long firstEventTime
   Map~String, Integer~ eventStatistics
}
class ApprovalStats {
  + ApprovalStats(int, int, int, double, double) 
  - int totalRequests
  - int approvedRequests
  - double totalSavings
  - int rejectedRequests
  - double approvalRate
  + toString() String
   double approvalRate
   int rejectedRequests
   int totalRequests
   double totalSavings
   int approvedRequests
}
class AudioBook {
  + AudioBook(String, String, String, String, double, int, int, String, double, MediaQuality, String, boolean) 
  - MediaQuality quality
  - String language
  - double fileSize
  - int duration
  - String narrator
  - boolean streamingOnly
  - String author
  - String format
  - boolean unabridged
  - String isbn
  - validateFileSize(double) double
  + toString() String
  + getAdjustedDuration(double) int
  - validateDuration(int) int
  - validateIsbn(String) String
  + calculateListeningSessions(int) int
   String format
   String language
   double fileSize
   String displayInfo
   boolean unabridged
   String isbn
   MediaQuality quality
   boolean streamingOnly
   String author
   int duration
   String narrator
   double discountRate
   String creator
}
class AuditLogEntry {
  + AuditLogEntry(long, String, String, String, String) 
  - String eventType
  - String materialTitle
  - long timestamp
  - String description
  - String materialId
  + toString() String
   String description
   String eventType
   long timestamp
   String materialId
   String materialTitle
}
class AuditLogObserver {
  + AuditLogObserver(int) 
  + AuditLogObserver() 
  - List~AuditLogEntry~ auditLog
  + clearAuditLog() void
  + getAuditLogForMaterial(String) List~AuditLogEntry~
  + getAuditLogForEventType(String) List~AuditLogEntry~
  + onEvent(MaterialEvent) void
  + getAuditLogForTimeRange(long, long) List~AuditLogEntry~
  + toString() String
  + printAuditLog() void
   int logSize
   AuditLogStats auditLogStats
   List~AuditLogEntry~ auditLog
   String observerName
}
class AuditLogStats {
  + AuditLogStats(int, int, int, long, long) 
  - int priceChangedCount
  - long newestTimestamp
  - int totalEntries
  - int materialAddedCount
  - long oldestTimestamp
  + toString() String
   long newestTimestamp
   int priceChangedCount
   int totalEntries
   int materialAddedCount
   long oldestTimestamp
}
class BatchOperationResult {
  + BatchOperationResult(int, int, List~String~) 
  + errors() List~String~
  + failed() int
  + totalOperations() int
  + successRate() double
  + successful() int
   boolean completeSuccess
}
class Book {
  + Book(String, String, String, double, int) 
  - String author
  - double price
  - String title
  - int year
  - String isbn
  - validateIsbn(String) String
  + compareTo(Book) int
  - validateStringField(String, String) String
  + equals(Object) boolean
  + toString() String
  - validatePrice(double) double
  + hashCode() int
  - validateYear(int) int
   int year
   double price
   String title
   String isbn
   String author
}
class BookArrayUtils {
  - BookArrayUtils() 
  + filterPriceAtMost(Book[], double) Book[]
  + countBeforeYear(Book[], int) int
  + findOldest(Book[]) Book
  + removeDuplicates(Book[]) Book[]
  + sortByYear(Book[]) void
  + countByAuthor(Book[], String) int
  + countByDecade(Book[]) Map~Integer, Integer~
  + averagePrice(Book[]) double
  + findLongestTitle(Book[]) Book
  + sortByPrice(Book[]) void
  + filterByYearRange(Book[], int, int) Book[]
  + filterByDecade(Book[], int) Book[]
  + merge(Book[], Book[]) Book[]
}
class BookstoreAPI {
<<Interface>>
  + findByPriceRange(double, double) List~Book~
  + size() int
  + snapshotArray() Book[]
  + findByAuthor(String) List~Book~
  + add(Book) boolean
  + findByIsbn(String) Book
  + removeByIsbn(String) boolean
  + findByYear(int) List~Book~
  + inventoryValue() double
  + findByTitle(String) List~Book~
   Book mostExpensive
   Book mostRecent
   List~Book~ allBooks
}
class BookstoreApplication {
  + BookstoreApplication() 
  + materialStore() MaterialStore
  + main(String[]) void
}
class BookstoreArrayList {
  + BookstoreArrayList() 
  + BookstoreArrayList(Collection~Book~) 
  + findByYear(int) List~Book~
  + sortByTitle() void
  + findByPriceRange(double, double) List~Book~
  + sortByYear() void
  + removeByIsbn(String) boolean
  + size() int
  + clear() void
  + toString() String
  + findByIsbn(String) Book
  + add(Book) boolean
  + findByTitle(String) List~Book~
  + snapshotArray() Book[]
  + findByAuthor(String) List~Book~
  + inventoryValue() double
  + sortByPrice() void
   Book mostExpensive
   Book mostRecent
   Map~String, Object~ statistics
   List~Book~ allBooks
}
class Builder {
  + Builder() 
  + withTitle(String) Builder
  + withYearRange(Integer, Integer) Builder
  + withCreator(String) Builder
  + withType(MaterialType) Builder
  + withPriceRange(Double, Double) Builder
  + build() SearchCriteria
}
class BundleService {
  + BundleService() 
  + calculateTotalSavings() double
  + addToBundle(String, Material) void
  + removeFromBundle(String, Material) boolean
  + toString() String
  + createBundle(String, double) MaterialBundle
  + addBundleToBundle(String, String) void
  + addComponentToBundle(String, MaterialComponent) void
  + getBundlesByDiscount(double) List~MaterialBundle~
  + getBundlesByValueRange(double, double) List~MaterialBundle~
  + getBundle(String) Optional~MaterialBundle~
  + calculateBundleSavings(String) double
  + getBundlesByMaterialType(MaterialType) List~MaterialBundle~
  + removeBundle(String) boolean
  + clearAllBundles() void
   double totalBundleValue
   int bundleCount
   List~String~ bundleNames
   boolean empty
   double totalDiscountedBundleValue
   List~MaterialBundle~ allBundles
   BundleStats bundleStats
}
class BundleStats {
  + BundleStats(int, int, double, double, double, double) 
  - double totalDiscountedValue
  - double totalSavings
  - double averageDiscount
  - int totalItems
  - double totalValue
  - int totalBundles
  + toString() String
   int totalBundles
   int totalItems
   double totalValue
   double averageDiscount
   double totalDiscountedValue
   double totalSavings
}
class CacheEntry {
  ~ CacheEntry(List~Material~) 
}
class CacheEntry {
  - CacheEntry(List~Material~, Instant, Instant, AtomicLong) 
  + isStale(Duration) boolean
  + value() List~Material~
  + isExpired(Duration) boolean
  + createdAt() Instant
  + of(List~Material~) CacheEntry
  + lastAccessedAt() Instant
  + accessCount() AtomicLong
  + recordAccess() CacheEntry
}
class CacheStats {
  + CacheStats(long, long, long, long, double, double, int, long) 
  + hitCount() long
  + missCount() long
  + evictionCount() long
  + loadCount() long
  + totalAccessCount() long
  + averageLoadTime() double
  + hitRate() double
  + size() int
   String summary
}
class CacheStats {
  + CacheStats(int, int, double, double, long, long) 
  - double averageAge
  - long totalHits
  - int currentSize
  - int maxSize
  - long totalRequests
  - double hitRatio
  + toString() String
   double averageAge
   double hitRatio
   int maxSize
   int currentSize
   long totalHits
   long totalRequests
}
class CachedSearchService {
  + CachedSearchService(MaterialRepository, int) 
  - initializeTrie() void
  + toString() String
  + searchByPrefixWithLimit(String, int) List~Material~
  + addMaterial(Material) void
  + removeMaterial(Material) void
  - invalidateCacheForMaterial(Material) void
  + refreshIndex() void
  + searchByPrefix(String) List~Material~
  + clear() void
   boolean indexEmpty
   int indexSize
   CacheStats cacheStats
}
class ComponentBuilder~T~ {
<<Interface>>
  + build() T
  + validate() void
  + reset() void
}
class ConcurrentMaterialStore {
  + ConcurrentMaterialStore() 
  + ConcurrentMaterialStore(Collection~Material~) 
  + getMaterialsByPriceRange(double, double) List~Material~
  + size() int
  + groupByType() Map~MaterialType, List~Material~~
  + searchByCreator(String) List~Material~
  + removeMaterial(String) Optional~Material~
  + getMaterialsByYear(int) List~Material~
  + getSorted(Comparator~Material~) List~Material~
  + findWithPredicate(Predicate~Material~) List~Material~
  + findById(String) Optional~Material~
  + toString() String
  + addMaterial(Material) boolean
  + getMaterialsByType(MaterialType) List~Material~
  + clearInventory() void
  + filterMaterials(Predicate~Material~) List~Material~
  + findByCreators(String[]) List~Material~
  + searchByTitle(String) List~Material~
  + findRecentMaterials(int) List~Material~
   List~Media~ mediaMaterials
   List~Material~ discountedMaterials
   List~Material~ allMaterials
   boolean empty
   double totalInventoryValue
   List~String~ allDisplayInfo
   List~Material~ allMaterialsSorted
   InventoryStats inventoryStats
   double totalDiscountAmount
   double totalDiscountedValue
}
class DigitalAnnotationDecorator {
  + DigitalAnnotationDecorator(Material) 
  - List~String~ annotations
  + hasAnnotations() boolean
  + clearAnnotations() void
  + equals(Object) boolean
  + removeAnnotation(int) String
  + addAnnotation(String) void
  + hashCode() int
   String displayInfo
   double price
   List~String~ annotations
   double digitalAnnotationCost
   int annotationCount
}
class DirectorHandler {
  + DirectorHandler() 
  - double MAX_DISCOUNT
  + handleRequest(DiscountRequest) void
   String handlerName
   double MAX_DISCOUNT
}
class DiscountApprovalService {
  + DiscountApprovalService() 
  + DiscountApprovalService(DiscountHandler) 
  - List~DiscountRequest~ processedRequests
  + clearRequests() void
  + getRequestsForCustomer(String) List~DiscountRequest~
  + requestDiscount(Material, double, String, String) DiscountRequest
  + toString() String
  + getRequestsForMaterial(Material) List~DiscountRequest~
  + calculateFinalPrice(DiscountRequest) double
  - buildDefaultChain() DiscountHandler
   String chainInfo
   ApprovalStats approvalStats
   List~DiscountRequest~ approvedRequests
   List~DiscountRequest~ processedRequests
   List~DiscountRequest~ rejectedRequests
}
class DiscountHandler {
  + DiscountHandler() 
  + handleRequest(DiscountRequest) void
  # passToNext(DiscountRequest) void
  + canApprove(double) boolean
  + toString() String
   String handlerName
   double maxDiscount
   int chainLength
   DiscountHandler next
   String handlerInfo
}
class DiscountRequest {
  + DiscountRequest(Material, double, String, String) 
  - String approvedBy
  - boolean approved
  - String rejectionReason
  - Material material
  - String reason
  - String customerId
  - long timestamp
  - double requestedDiscount
  + toString() String
  + equals(Object) boolean
  + hashCode() int
   double requestedDiscountPercentage
   double savingsAmount
   double discountedPrice
   String customerId
   String summary
   long timestamp
   String approvedBy
   Material material
   boolean approved
   String rejectionReason
   String reason
   double requestedDiscount
}
class EBook {
  + EBook(String, String, String, double, int, String, double, boolean, int, MediaQuality) 
  - MediaQuality quality
  - boolean drmEnabled
  - String fileFormat
  - double fileSize
  - String author
  - int wordCount
  + toString() String
  - validateFileSize(double) double
  - validateFileFormat(String) String
  - validateWordCount(int) int
  + hashCode() int
  + equals(Object) boolean
   String description
   String format
   double fileSize
   String displayInfo
   boolean drmEnabled
   MediaQuality quality
   boolean streamingOnly
   String author
   int duration
   String fileFormat
   double discountRate
   int readingTimeMinutes
   int wordCount
   String creator
}
class EBookBuilder {
  + EBookBuilder() 
  - double fileSize
  - String title
  - int year
  - boolean drmEnabled
  - double price
  - String id
  - String fileFormat
  - int wordCount
  - String author
  - MediaQuality quality
  + setHighQuality() EBookBuilder
  + build() EBook
  + enableDRM() EBookBuilder
  + disableDRM() EBookBuilder
  + setLowQuality() EBookBuilder
  + reset() void
  + validate() void
  + toString() String
   int year
   MediaQuality quality
   double price
   String title
   double fileSize
   String author
   String fileFormat
   boolean drmEnabled
   int wordCount
   String id
}
class ExpeditedDeliveryDecorator {
  + ExpeditedDeliveryDecorator(Material, int) 
  - int deliveryDays
  + hashCode() int
  + equals(Object) boolean
   double expeditedDeliveryCost
   String displayInfo
   double price
   int deliveryDays
}
class GiftWrappingDecorator {
  + GiftWrappingDecorator(Material, String) 
  - double GIFT_WRAPPING_COST
  - String wrappingStyle
  + equals(Object) boolean
  + hashCode() int
   String wrappingStyle
   String displayInfo
   double GIFT_WRAPPING_COST
   double price
}
class InvalidMaterialException {
  + InvalidMaterialException(String, Throwable) 
  + InvalidMaterialException(String) 
}
class InventoryObserver {
  + InventoryObserver() 
  - int totalEvents
  + onEvent(MaterialEvent) void
  - handlePriceChanged(MaterialEvent) void
  + getInventoryCount(String) int
  + getTotalValue(String) double
  + toString() String
  - handleMaterialAdded(MaterialEvent) void
  + clear() void
   int totalEvents
   int uniqueMaterialCount
   double totalInventoryValue
   int totalInventoryCount
   Map~String, Integer~ allInventoryCounts
   String observerName
   Map~String, Double~ allTotalValues
}
class InventoryStats {
  + InventoryStats(int, double, double, int, int, int) 
  - double averagePrice
  - int uniqueTypes
  - int printCount
  - double medianPrice
  - int totalCount
  - int mediaCount
  + toString() String
   int uniqueTypes
   int printCount
   double averagePrice
   double medianPrice
   int totalCount
   int mediaCount
}
class JsonMaterialRepository {
  + JsonMaterialRepository(String) 
  - String filePath
  + count() long
  + dataFileExists() boolean
  + findById(String) Optional~Material~
  + deleteAll() void
  + save(Material) void
  - loadAll() List~Material~
  + delete(String) boolean
  + exists(String) boolean
  - validateAndSanitizePath(String) String
  + findAll() List~Material~
   long dataFileSize
   String filePath
}
class LoggerFactory {
  - LoggerFactory() 
  + getLogger(Class~?~) Logger
  + getLogger(String) Logger
}
class Magazine {
  + Magazine(String, String, String, double, int, int, String, String) 
  - String issn
  - String publisher
  - String frequency
  - String category
  - int issueNumber
  - validateIssn(String) String
  + toString() String
  + calculateAnnualSubscription() double
  - validateIssueNumber(int) int
   String frequency
   double discountRate
   String displayInfo
   int issuesPerYear
   String issn
   int issueNumber
   String category
   String publisher
   String creator
}
class ManagerHandler {
  + ManagerHandler() 
  - double MAX_DISCOUNT
  + handleRequest(DiscountRequest) void
   String handlerName
   double MAX_DISCOUNT
}
class Material {
  # Material(String, String, double, int, MaterialType) 
  # String title
  # int year
  # double price
  # String id
  # MaterialType type
  # validatePrice(double) double
  # validateYear(int) int
  + hashCode() int
  # validateId(String) String
  + compareTo(Material) int
  # validateStringField(String, String) String
  + equals(Object) boolean
  + toString() String
   double discountedPrice
   int year
   double price
   String title
   double discountRate
   String displayInfo
   MaterialType type
   String id
   String creator
}
class MaterialAddedEvent {
  + MaterialAddedEvent(Material) 
  - Material material
  - long timestamp
  + toString() String
  + equals(Object) boolean
  + hashCode() int
   String description
   String eventType
   long timestamp
   Material material
}
class MaterialBuilder~T~ {
<<Interface>>
  + validate() void
  + build() T
  + reset() void
}
class MaterialBundle {
  + MaterialBundle(String, double) 
  - List~MaterialComponent~ components
  - String bundleName
  - double bundleDiscount
  - containsBundle(MaterialBundle) boolean
  + addComponent(MaterialComponent) void
  + getMaterialsByType(MaterialType) List~Material~
  + equals(Object) boolean
  + removeComponent(MaterialComponent) boolean
  + hashCode() int
  + containsMaterial(Material) boolean
  + containsType(MaterialType) boolean
  + toString() String
   String description
   List~Material~ materials
   double bundleDiscount
   double price
   boolean leaf
   List~MaterialComponent~ components
   String bundleName
   double discountedPrice
   String title
   double discountRate
   int itemCount
   int componentCount
   double totalSavings
}
class MaterialBundleBuilder {
  + MaterialBundleBuilder() 
  - String bundleName
  - List~MaterialComponent~ components
  - double bundleDiscount
  + setSmallDiscount() MaterialBundleBuilder
  + setLargeDiscount() MaterialBundleBuilder
  + hasComponents() boolean
  + reset() void
  + addComponent(MaterialComponent) MaterialBundleBuilder
  + removeMaterial(Material) MaterialBundleBuilder
  + addMaterial(Material) MaterialBundleBuilder
  + build() MaterialBundle
  + toString() String
  + addMaterials(List~Material~) MaterialBundleBuilder
  + validate() void
  + setMediumDiscount() MaterialBundleBuilder
  + removeComponent(MaterialComponent) MaterialBundleBuilder
  + addComponents(List~MaterialComponent~) MaterialBundleBuilder
  + clearComponents() MaterialBundleBuilder
  + addBundle(MaterialBundle) MaterialBundleBuilder
   double totalPrice
   double bundleDiscount
   double totalDiscountedPrice
   List~MaterialComponent~ components
   double bundleDiscountPercent
   String bundleName
   int componentCount
   double totalSavings
}
class MaterialComponent {
<<Interface>>
   List~Material~ materials
   String description
   double discountedPrice
   double price
   String title
   boolean composite
   boolean leaf
   double discountRate
   int itemCount
}
class MaterialController {
  + MaterialController(MaterialStore) 
  + getMaterialsByType(MaterialType) ResponseEntity~List~Material~~
  + getMaterialById(String) ResponseEntity~Material~
  + searchByCreator(String) ResponseEntity~List~Material~~
  + deleteMaterial(String) ResponseEntity~Void~
  + createMaterial(Material) ResponseEntity~Material~
  + searchByTitle(String) ResponseEntity~List~Material~~
  + updateMaterial(String, Material) ResponseEntity~Material~
  + getRecentMaterials(int) ResponseEntity~List~Material~~
  + getMaterialsByPriceRange(double, double) ResponseEntity~List~Material~~
   ResponseEntity~Integer~ materialCount
   ResponseEntity~InventoryStats~ inventoryStats
   ResponseEntity~List~Material~~ allMaterials
}
class MaterialDecorator {
  + MaterialDecorator(Material) 
  # Material decoratedMaterial
  + hasDecorators() boolean
  + hashCode() int
  + toString() String
  + equals(Object) boolean
   Material baseMaterial
   double discountRate
   Material decoratedMaterial
   String displayInfo
   int decoratorCount
   String creator
}
class MaterialDirector {
  + MaterialDirector(EBookBuilder, MaterialBundleBuilder) 
  - MaterialBundleBuilder bundleBuilder
  + buildCourseBundle(String, List~Material~) MaterialBundle
  + buildCustomBundle(String, List~Material~, double) MaterialBundle
  + createBundleBuilder() MaterialBundleBuilder
  + buildNestedBundle(String, List~MaterialBundle~, double) MaterialBundle
  + buildStarterBundle(String, List~Material~) MaterialBundle
  + createEBookBuilder() EBookBuilder
  + buildStudentEBook(String, String, String, double) Material
  + buildBasicEBook(String, String, String, double) Material
  + buildTextbookBundle(String, List~Material~) MaterialBundle
  + buildPremiumBundle(String, List~Material~) MaterialBundle
  + buildPremiumEBook(String, String, String, double) Material
   MaterialBundleBuilder bundleBuilder
   EBookBuilder EBookBuilder
}
class MaterialEnhancementService {
  + MaterialEnhancementService() 
  + calculateEnhancementCost(Material, Material) double
  + getEnhancementCount(Material) int
  + addDigitalAnnotations(Material) Material
  + addExpeditedDelivery(Material, int) Material
  + getEnhancementSummary(Material) String
  + createDigitalPackage(Material) Material
  + toString() String
  + getBasePrice(Material) double
  + hasEnhancements(Material) boolean
  + getBaseMaterial(Material) Material
  + createPremiumPackage(Material, String, int) Material
  + createGiftPackage(Material, String, int) Material
  + hasEnhancement(Material, Class~MaterialDecorator~) boolean
  + addGiftWrapping(Material, String) Material
}
class MaterialEvent {
<<Interface>>
   String description
   String eventType
   long timestamp
   Material material
}
class MaterialEventPublisher {
  + MaterialEventPublisher() 
  - List~MaterialObserver~ observers
  + publishPriceChanged(Material, double, double) void
  + hasNoObservers() boolean
  + notifyObservers(MaterialEvent) void
  + addObserver(MaterialObserver) void
  + publishEvent(MaterialEvent) void
  + hasObserver(MaterialObserver) boolean
  + publishMaterialAdded(Material) void
  + removeObserver(MaterialObserver) boolean
  + getObserversOfType(Class~T~) List~T~
  + clearObservers() void
  + toString() String
   int observerCount
   List~MaterialObserver~ observers
}
class MaterialFactory {
  + MaterialFactory() 
  - createPrintedBook(Map~String, Object~) PrintedBook
  - createAudioBook(Map~String, Object~) AudioBook
  - getRequiredBoolean(Map~String, Object~, String) boolean
  - createEBook(Map~String, Object~) EBook
  + createMaterial(String, Map~String, Object~) Material
  - getRequiredDouble(Map~String, Object~, String) double
  - getRequiredInteger(Map~String, Object~, String) int
  - createMagazine(Map~String, Object~) Magazine
  - getOptionalString(Map~String, Object~, String, String) String
  - getRequiredVideoType(Map~String, Object~, String) VideoType
  - getOptionalStringList(Map~String, Object~, String) List~String~
  - getRequiredMediaQuality(Map~String, Object~, String) MediaQuality
  - getOptionalDouble(Map~String, Object~, String, double) double
  - getRequiredString(Map~String, Object~, String) String
  - createVideoMaterial(Map~String, Object~) VideoMaterial
  - getOptionalBoolean(Map~String, Object~, String, boolean) boolean
}
class MaterialIterator {
<<Interface>>
  + next() Material
  + reset() void
  + hasNext() boolean
   boolean atBeginning
   int remainingCount
   boolean atEnd
   int totalCount
   int currentPosition
}
class MaterialIteratorFactory {
  + MaterialIteratorFactory() 
  + findFirst(MaterialIterator, Predicate~Material~) Optional~Material~
  + findAll(MaterialIterator, Predicate~Material~) List~Material~
  + createCheapIterator(List~Material~, double) MaterialIterator
  + createMagazineIterator(List~Material~) MaterialIterator
  + collectAll(MaterialIterator) List~Material~
  + getTotalCount(MaterialIterator) int
  + createPriceSortedIterator(List~Material~, boolean) MaterialIterator
  + createPriceRangeIterator(List~Material~, double, double) MaterialIterator
  + count(MaterialIterator, Predicate~Material~) int
  + createEBookIterator(List~Material~) MaterialIterator
  + anyMatch(MaterialIterator, Predicate~Material~) boolean
  + toString() String
  + createTypeIterator(List~Material~, MaterialType) MaterialIterator
  + createExpensiveIterator(List~Material~, double) MaterialIterator
  + createVideoIterator(List~Material~) MaterialIterator
  + getRemainingCount(MaterialIterator) int
  + createBookIterator(List~Material~) MaterialIterator
  + createAudioBookIterator(List~Material~) MaterialIterator
  + allMatch(MaterialIterator, Predicate~Material~) boolean
}
class MaterialLeaf {
  + MaterialLeaf(Material) 
  - Material material
  + equals(Object) boolean
  + toString() String
  + hashCode() int
   String description
   List~Material~ materials
   double price
   boolean leaf
   Material material
   MaterialType type
   String id
   int year
   double discountedPrice
   String title
   double discountRate
   int itemCount
   String creator
}
class MaterialNotFoundException {
  + MaterialNotFoundException(String, Throwable) 
  + MaterialNotFoundException(String) 
}
class MaterialObserver {
<<Interface>>
  + onEvent(MaterialEvent) void
  + onAdded(MaterialSubject) void
  + onRemoved(MaterialSubject) void
   String observerName
}
class MaterialRepository {
<<Interface>>
  + findAll() List~Material~
  + exists(String) boolean
  + count() long
  + save(Material) void
  + findById(String) Optional~Material~
  + delete(String) boolean
  + deleteAll() void
}
class MaterialService {
  + MaterialService(MaterialRepository) 
  + materialExists(String) boolean
  + findMaterial(String) Material
  + addMaterial(Material) void
  + removeMaterial(String) boolean
  - validateMaterial(Material) void
  + findMaterialOptional(String) Optional~Material~
  + clearAllMaterials() void
  + updateMaterial(Material) void
   List~Material~ allMaterials
   long materialCount
}
class MaterialStore {
<<Interface>>
  + clearInventory() void
  + getMaterialsByYear(int) List~Material~
  + findById(String) Optional~Material~
  + findByCreators(String[]) List~Material~
  + addMaterial(Material) boolean
  + getMaterialsByType(MaterialType) List~Material~
  + filterMaterials(Predicate~Material~) List~Material~
  + findRecentMaterials(int) List~Material~
  + getMaterialsByPriceRange(double, double) List~Material~
  + findWithPredicate(Predicate~Material~) List~Material~
  + searchByTitle(String) List~Material~
  + size() int
  + getSorted(Comparator~Material~) List~Material~
  + searchByCreator(String) List~Material~
  + removeMaterial(String) Optional~Material~
   List~Media~ mediaMaterials
   List~Material~ allMaterials
   boolean empty
   double totalInventoryValue
   List~Material~ allMaterialsSorted
   InventoryStats inventoryStats
   double totalDiscountedValue
}
class MaterialStoreImpl {
  + MaterialStoreImpl() 
  + MaterialStoreImpl(Collection~Material~) 
  + getMaterialsByType(MaterialType) List~Material~
  + addMaterial(Material) boolean
  + groupByType() Map~MaterialType, List~Material~~
  + findByCreators(String[]) List~Material~
  + findRecentMaterials(int) List~Material~
  + clearInventory() void
  + getMaterialsByPriceRange(double, double) List~Material~
  + removeMaterial(String) Optional~Material~
  + findById(String) Optional~Material~
  + searchByCreator(String) List~Material~
  + size() int
  + filterMaterials(Predicate~Material~) List~Material~
  + findWithPredicate(Predicate~Material~) List~Material~
  + getMaterialsByYear(int) List~Material~
  + getSorted(Comparator~Material~) List~Material~
  + searchByTitle(String) List~Material~
  + toString() String
   List~Media~ mediaMaterials
   List~Material~ discountedMaterials
   List~Material~ allMaterials
   boolean empty
   double totalInventoryValue
   List~String~ allDisplayInfo
   List~Material~ allMaterialsSorted
   InventoryStats inventoryStats
   double totalDiscountAmount
   double totalDiscountedValue
}
class MaterialSubject {
<<Interface>>
  + notifyObservers(MaterialEvent) void
  + removeObserver(MaterialObserver) boolean
  + hasNoObservers() boolean
  + addObserver(MaterialObserver) void
  + clearObservers() void
   int observerCount
}
class MaterialTrie {
  + MaterialTrie() 
  + insert(Material) void
  + toString() String
  + searchByPrefix(String) List~Material~
  + size() int
  + searchByPrefixWithLimit(String, int) List~Material~
  + clear() void
  + hasPrefix(String) boolean
  - cleanupEmptyNodes(List~TrieNode~) void
  + remove(Material) boolean
  - countPrefixes(TrieNode) int
   List~Material~ allMaterials
   boolean empty
}
class MaterialType {
<<enumeration>>
  - MaterialType(String) 
  - String displayName
  + values() MaterialType[]
  + valueOf(String) MaterialType
   String displayName
}
class MaterialTypeIterator {
  + MaterialTypeIterator(List~Material~, MaterialType) 
  - MaterialType targetType
  - int totalCount
  + next() Material
  + reset() void
  + hasNext() boolean
  + toString() String
  - advanceToNext() void
   boolean atBeginning
   int remainingCount
   boolean atEnd
   int totalCount
   int currentPosition
   MaterialType targetType
}
class MaterialVisitor {
<<Interface>>
  + visit(PrintedBook) void
  + visit(AudioBook) void
  + visit(Magazine) void
  + visit(VideoMaterial) void
  + visit(EBook) void
}
class MaterialsWrapper {
  + MaterialsWrapper() 
  + MaterialsWrapper(List~Material~) 
  - List~Material~ materials
   List~Material~ materials
}
class Media {
<<Interface>>
  + estimateDownloadTime(double) int
   MediaQuality quality
   String playbackInfo
   boolean streamingOnly
   String format
   double fileSize
   int duration
}
class MediaQuality {
<<enumeration>>
  - MediaQuality(String, int) 
  - int bitrate
  - String description
  + toString() String
  + values() MediaQuality[]
  + valueOf(String) MediaQuality
   String description
   int bitrate
}
class ModernConcurrentMaterialStore {
  + ModernConcurrentMaterialStore() 
  + ModernConcurrentMaterialStore(Collection~Material~) 
  - scheduleMaintenanceTasks() void
  + addMaterialsBatchAsync(Collection~Material~) CompletableFuture~Map~String, Boolean~~
  + findWithPredicate(Predicate~Material~) List~Material~
  + toString() String
  + getSorted(Comparator~Material~) List~Material~
  + findRecentMaterials(int) List~Material~
  + groupByType() Map~MaterialType, List~Material~~
  + searchByCreator(String) List~Material~
  + addMaterialAsync(Material) CompletableFuture~Boolean~
  + getMaterialsByYear(int) List~Material~
  + addMaterial(Material) boolean
  + searchByTitleAsync(String) CompletableFuture~List~Material~~
  + findById(String) Optional~Material~
  + findByCreators(String[]) List~Material~
  + getMaterialsByPriceRange(double, double) List~Material~
  - ensureNotClosed() void
  - performMaintenance() void
  + removeMaterial(String) Optional~Material~
  + findByIdsAsync(List~String~) CompletableFuture~Map~String, Material~~
  + getMaterialsByType(MaterialType) List~Material~
  + searchByTitle(String) List~Material~
  + close() void
  + filterMaterials(Predicate~Material~) List~Material~
  + parallelSearchAsync(String, String, MaterialType) CompletableFuture~List~Material~~
  + size() int
  + findByIdAsync(String) CompletableFuture~Optional~Material~~
  + clearInventory() void
   List~Media~ mediaMaterials
   CompletableFuture~InventoryStats~ inventoryStatsAsync
   List~Material~ discountedMaterials
   List~Material~ allMaterials
   boolean empty
   double totalInventoryValue
   List~Material~ allMaterialsSorted
   InventoryStats inventoryStats
   double totalDiscountAmount
   double totalDiscountedValue
   CompletableFuture~Double~ totalInventoryValueAsync
}
class ModernInventoryStats {
  + ModernInventoryStats(int, double, double, int, int, int) 
  + mediaCount() int
  + averagePrice() double
  + empty() ModernInventoryStats
  + uniqueTypes() int
  + totalCount() int
  + medianPrice() double
  + printCount() int
   String summary
   double printPercentage
   boolean empty
   double mediaPercentage
}
class ModernJsonMaterialRepository {
  + ModernJsonMaterialRepository(String) 
  - initializeStorage() void
  + dataFileExists() boolean
  + deleteAll() void
  - saveAtomic(List~Material~) void
  + close() void
  + count() long
  + createBackup() boolean
  + exists(String) boolean
  - validateAndSanitizePath(String) Path
  + deleteAll(Collection~String~) int
  - ensureNotClosed() void
  + restoreFromBackup(String) boolean
  + findAll() List~Material~
  - loadAllInternal() List~Material~
  + delete(String) boolean
  + save(Material) void
  + saveAll(Collection~Material~) void
  + findById(String) Optional~Material~
  + performMaintenance() void
   long dataFileSize
   String filePath
}
class ModernMaterialStore {
<<Interface>>
  + addMaterialsBatchAsync(List~Material~) CompletableFuture~BatchOperationResult~
  + findByIdAsync(String) CompletableFuture~Optional~Material~~
  + searchByTitleAsync(String) CompletableFuture~List~Material~~
  + removeMaterialsBatchAsync(List~String~) CompletableFuture~BatchOperationResult~
  + advancedSearchAsync(SearchCriteria) CompletableFuture~List~Material~~
  + addMaterialAsync(Material) CompletableFuture~Boolean~
   CompletableFuture~ModernInventoryStats~ modernInventoryStatsAsync
}
class ModernSearchCache {
  + ModernSearchCache() 
  + ModernSearchCache(int, Duration, Duration) 
  - performSizeBasedEviction() void
  + put(String, List~Material~) void
  + size() int
  + resetStats() void
  + warmUp(Collection~String~, Function~String, List~Material~~) CompletableFuture~Void~
  + invalidateAll() void
  + invalidateIf(Predicate~String~) int
  + close() void
  + containsKey(String) boolean
  + get(String, Function~String, List~Material~~) List~Material~
  - performMaintenance() void
  + invalidate(String) boolean
  + getAsync(String, Function~String, CompletableFuture~List~Material~~~) CompletableFuture~List~Material~~
  - scheduleMaintenance() void
  + refresh(String, Function~String, List~Material~~) CompletableFuture~List~Material~~
  - ensureNotClosed() void
   CacheStats stats
   boolean empty
}
class PolymorphismDemo {
  + PolymorphismDemo() 
  - demonstrateSOLIDPrinciples(MaterialStore) void
  - demonstrateInterfaceSegregation(MaterialStore) void
  + main(String[]) void
  - processPolymorphically(Material) void
  - demonstrateStreamingVsDownload(MaterialStore) void
  - createSampleStore() MaterialStore
  - demonstrateAbstraction(MaterialStore) void
  - demonstrateDynamicBinding(MaterialStore) void
  - demonstratePolymorphicBehavior(MaterialStore) void
  - demonstrateMediaVersatility(MaterialStore) void
}
class PriceChangedEvent {
  + PriceChangedEvent(Material, double, double) 
  - Material material
  - double oldPrice
  - long timestamp
  - double newPrice
  + hashCode() int
  + equals(Object) boolean
  + toString() String
   String description
   String eventType
   boolean priceIncrease
   long timestamp
   boolean priceDecrease
   Material material
   double oldPrice
   double newPrice
   double priceChangePercentage
   double priceChange
}
class PriceRangeIterator {
  + PriceRangeIterator(List~Material~, double, double) 
  - double minPrice
  - int totalCount
  - double maxPrice
  + hasNext() boolean
  - advanceToNext() void
  + toString() String
  + next() Material
  + reset() void
   double minPrice
   boolean atBeginning
   boolean atEnd
   int currentPosition
   double priceRange
   int remainingCount
   double maxPrice
   int totalCount
}
class PriceSortedIterator {
  + PriceSortedIterator(List~Material~, boolean) 
  - boolean ascending
  + hasNext() boolean
  + next() Material
  + peekNext() Material
  + peek() Material
  + toString() String
  + reset() void
   boolean atBeginning
   int remainingCount
   boolean atEnd
   int totalCount
   int currentPosition
   boolean ascending
}
class PrintedBook {
  + PrintedBook(String, String, String, double, int) 
  + PrintedBook(String, String, String, double, int, int, String, boolean) 
  - String publisher
  - int pages
  - boolean hardcover
  - String isbn
  - String author
  + estimateReadingTime(int) double
  - validateIsbn(String) String
  - validatePages(int) int
  + toString() String
   boolean hardcover
   int pages
   String author
   double discountRate
   String displayInfo
   String isbn
   String publisher
   String creator
}
class PublicationFrequency {
<<enumeration>>
  - PublicationFrequency(String) 
  + values() PublicationFrequency[]
  + valueOf(String) PublicationFrequency
  + toString() String
}
class RepositoryException {
  + RepositoryException(String) 
  + RepositoryException(Throwable) 
  + RepositoryException(String, Throwable) 
}
class SearchCriteria {
  + SearchCriteria(Optional~String~, Optional~String~, Optional~MaterialType~, Optional~Double~, Optional~Double~, Optional~Integer~, Optional~Integer~) 
  + creator() Optional~String~
  + yearTo() Optional~Integer~
  + title() Optional~String~
  + minPrice() Optional~Double~
  + yearFrom() Optional~Integer~
  + maxPrice() Optional~Double~
  + builder() Builder
  + hasAnyCriteria() boolean
  + type() Optional~MaterialType~
}
class SearchResultCache {
  + SearchResultCache(int) 
  - int maxSize
  + containsKey(String) boolean
  - evictLRU() void
  + toString() String
  + clear() void
  + put(String, List~Material~) void
  + get(String) Optional~List~Material~~
  + size() int
  + remove(String) boolean
   CacheStats stats
   int maxSize
   boolean empty
   boolean full
}
class ShippingCostCalculator {
  + ShippingCostCalculator() 
  - double totalShippingCost
  + visit(PrintedBook) void
  + visit(VideoMaterial) void
  + calculateShippingCost(Material) double
  + visit(AudioBook) void
  + reset() void
  + visit(Magazine) void
  + visit(EBook) void
   double totalShippingCost
}
class TrieNode {
  ~ TrieNode() 
}
class VPHandler {
  + VPHandler() 
  - double MAX_DISCOUNT
  + handleRequest(DiscountRequest) void
   String handlerName
   double MAX_DISCOUNT
}
class VideoMaterial {
  + VideoMaterial(String, String, String, double, int, int, String, double, MediaQuality, VideoType, String, List~String~, boolean, String) 
  - VideoType videoType
  - String format
  - String director
  - MediaQuality quality
  - String aspectRatio
  - int duration
  - List~String~ cast
  - double fileSize
  - String rating
  + getCompressedSize(double) double
  + toString() String
  - validateDuration(int) int
  - validateFileSize(double) double
  + hasSubtitles() boolean
   String format
   double streamingBandwidth
   double fileSize
   String displayInfo
   String rating
   MediaQuality quality
   boolean streamingOnly
   String aspectRatio
   int duration
   double discountRate
   List~String~ cast
   boolean featureLength
   String creator
   VideoType videoType
   String director
}
class VideoType {
<<enumeration>>
  - VideoType(String) 
  + toString() String
  + values() VideoType[]
  + valueOf(String) VideoType
}

AdvancedMaterialFactory  ..>  AudioBook : «create»
AdvancedMaterialFactory  ..>  EBook : «create»
AdvancedMaterialFactory  ..>  Magazine : «create»
AdvancedMaterialFactory  ..>  PrintedBook : «create»
AdvancedMaterialFactory  ..>  VideoMaterial : «create»
AnalyticsObserver  -->  AnalyticsData 
AnalyticsObserver  ..>  AnalyticsData : «create»
AnalyticsObserver  ..>  MaterialObserver 
DiscountApprovalService  -->  ApprovalStats 
AudioBook  -->  Material 
AudioBook  ..>  Media 
AudioBook "1" *--> "quality 1" MediaQuality 
AuditLogObserver  -->  AuditLogEntry 
AuditLogObserver "1" *--> "auditLog *" AuditLogEntry 
AuditLogObserver  ..>  AuditLogEntry : «create»
AuditLogObserver  ..>  AuditLogStats : «create»
AuditLogObserver  ..>  MaterialObserver 
AuditLogObserver  -->  AuditLogStats 
ModernMaterialStore  -->  BatchOperationResult 
BookArrayUtils  ..>  Book : «create»
BookstoreApplication  ..>  ModernConcurrentMaterialStore : «create»
BookstoreArrayList  ..>  Book : «create»
BookstoreArrayList "1" *--> "inventory *" Book 
BookstoreArrayList  ..>  BookstoreAPI 
SearchCriteria  -->  Builder 
Builder  ..>  SearchCriteria : «create»
BundleService  ..>  BundleStats : «create»
BundleService "1" *--> "bundles *" MaterialBundle 
BundleService  ..>  MaterialBundle : «create»
BundleService  ..>  MaterialLeaf : «create»
BundleService  -->  BundleStats 
CacheEntry "1" *--> "results *" Material 
CacheEntry "1" *--> "value *" Material 
ModernSearchCache  -->  CacheEntry 
SearchResultCache  -->  CacheEntry 
ModernSearchCache  -->  CacheStats 
SearchResultCache  -->  CacheStats 
CachedSearchService "1" *--> "repository 1" MaterialRepository 
CachedSearchService "1" *--> "trie 1" MaterialTrie 
CachedSearchService  ..>  MaterialTrie : «create»
CachedSearchService "1" *--> "cache 1" SearchResultCache 
CachedSearchService  ..>  SearchResultCache : «create»
ComponentBuilder~T~  ..>  MaterialComponent 
ConcurrentMaterialStore  ..>  InventoryStats : «create»
ConcurrentMaterialStore "1" *--> "materials *" Material 
ConcurrentMaterialStore  ..>  MaterialStore 
DigitalAnnotationDecorator  -->  MaterialDecorator 
DirectorHandler  -->  DiscountHandler 
DiscountApprovalService  ..>  ApprovalStats : «create»
DiscountApprovalService  ..>  DirectorHandler : «create»
DiscountApprovalService "1" *--> "chain 1" DiscountHandler 
DiscountApprovalService "1" *--> "processedRequests *" DiscountRequest 
DiscountApprovalService  ..>  DiscountRequest : «create»
DiscountApprovalService  ..>  ManagerHandler : «create»
DiscountApprovalService  ..>  VPHandler : «create»
DiscountRequest "1" *--> "material 1" Material 
EBook  -->  Material 
EBook  ..>  Media 
EBook "1" *--> "quality 1" MediaQuality 
EBookBuilder  ..>  EBook : «create»
EBookBuilder  ..>  MaterialBuilder~T~ 
EBookBuilder "1" *--> "quality 1" MediaQuality 
ExpeditedDeliveryDecorator  -->  MaterialDecorator 
GiftWrappingDecorator  -->  MaterialDecorator 
MaterialService  -->  InvalidMaterialException 
InventoryObserver  ..>  MaterialObserver 
MaterialStore  -->  InventoryStats 
JsonMaterialRepository  ..>  MaterialRepository 
JsonMaterialRepository  ..>  MaterialsWrapper : «create»
JsonMaterialRepository  ..>  RepositoryException : «create»
Magazine  -->  Material 
ManagerHandler  -->  DiscountHandler 
Material "1" *--> "type 1" MaterialType 
MaterialAddedEvent "1" *--> "material 1" Material 
MaterialAddedEvent  ..>  MaterialEvent 
MaterialBuilder~T~  ..>  Material 
MaterialBundle  ..>  MaterialComponent 
MaterialBundle "1" *--> "components *" MaterialComponent 
MaterialBundleBuilder  ..>  ComponentBuilder~T~ 
MaterialBundleBuilder  ..>  MaterialBundle : «create»
MaterialBundleBuilder "1" *--> "components *" MaterialComponent 
MaterialBundleBuilder  ..>  MaterialLeaf : «create»
MaterialController "1" *--> "materialStore 1" MaterialStore 
MaterialDecorator  -->  Material 
MaterialDecorator "1" *--> "decoratedMaterial 1" Material 
MaterialDirector "1" *--> "eBookBuilder 1" EBookBuilder 
MaterialDirector  ..>  EBookBuilder : «create»
MaterialDirector  ..>  MaterialBundleBuilder : «create»
MaterialDirector "1" *--> "bundleBuilder 1" MaterialBundleBuilder 
MaterialEnhancementService  ..>  DigitalAnnotationDecorator : «create»
MaterialEnhancementService  ..>  ExpeditedDeliveryDecorator : «create»
MaterialEnhancementService  ..>  GiftWrappingDecorator : «create»
MaterialEventPublisher  ..>  MaterialAddedEvent : «create»
MaterialEventPublisher "1" *--> "observers *" MaterialObserver 
MaterialEventPublisher  ..>  MaterialSubject 
MaterialEventPublisher  ..>  PriceChangedEvent : «create»
MaterialFactory  ..>  AudioBook : «create»
MaterialFactory  ..>  EBook : «create»
MaterialFactory  ..>  Magazine : «create»
MaterialFactory  ..>  PrintedBook : «create»
MaterialFactory  ..>  VideoMaterial : «create»
MaterialIteratorFactory  ..>  MaterialTypeIterator : «create»
MaterialIteratorFactory  ..>  PriceRangeIterator : «create»
MaterialIteratorFactory  ..>  PriceSortedIterator : «create»
MaterialLeaf "1" *--> "material 1" Material 
MaterialLeaf  ..>  MaterialComponent 
MaterialService  -->  MaterialNotFoundException 
MaterialService  ..>  InvalidMaterialException : «create»
MaterialService  ..>  MaterialNotFoundException : «create»
MaterialService "1" *--> "repository 1" MaterialRepository 
MaterialStoreImpl  ..>  InventoryStats : «create»
MaterialStoreImpl "1" *--> "inventory *" Material 
MaterialStoreImpl  ..>  MaterialStore 
MaterialTrie "1" *--> "root 1" TrieNode 
MaterialTrie  ..>  TrieNode : «create»
Material  -->  MaterialType 
MaterialTypeIterator "1" *--> "materials *" Material 
MaterialTypeIterator  ..>  MaterialIterator 
MaterialTypeIterator "1" *--> "targetType 1" MaterialType 
MaterialsWrapper "1" *--> "materials *" Material 
Media  -->  MediaQuality 
ModernConcurrentMaterialStore  ..>  InventoryStats : «create»
ModernConcurrentMaterialStore "1" *--> "materials *" Material 
ModernConcurrentMaterialStore  ..>  MaterialStore 
ModernMaterialStore  -->  ModernInventoryStats 
ModernJsonMaterialRepository  ..>  MaterialRepository 
ModernJsonMaterialRepository  ..>  MaterialsWrapper : «create»
ModernJsonMaterialRepository  ..>  RepositoryException : «create»
ModernMaterialStore  -->  MaterialStore 
ModernSearchCache "1" *--> "cache *" CacheEntry 
ModernSearchCache  ..>  CacheStats : «create»
PolymorphismDemo  ..>  AudioBook : «create»
PolymorphismDemo  ..>  Magazine : «create»
PolymorphismDemo  ..>  MaterialStoreImpl : «create»
PolymorphismDemo  ..>  PrintedBook : «create»
PolymorphismDemo  ..>  VideoMaterial : «create»
PriceChangedEvent "1" *--> "material 1" Material 
PriceChangedEvent  ..>  MaterialEvent 
PriceRangeIterator "1" *--> "materials *" Material 
PriceRangeIterator  ..>  MaterialIterator 
PriceSortedIterator "1" *--> "sortedMaterials *" Material 
PriceSortedIterator  ..>  MaterialIterator 
PrintedBook  -->  Material 
Magazine  -->  PublicationFrequency 
SearchCriteria  ..>  Builder : «create»
ModernMaterialStore  -->  SearchCriteria 
SearchResultCache  ..>  CacheEntry : «create»
SearchResultCache "1" *--> "cache *" CacheEntry 
SearchResultCache  ..>  CacheStats : «create»
ShippingCostCalculator  ..>  MaterialVisitor 
TrieNode "1" *--> "materials *" Material 
MaterialTrie  -->  TrieNode 
VPHandler  -->  DiscountHandler 
VideoMaterial  -->  Material 
VideoMaterial  ..>  Media 
VideoMaterial "1" *--> "quality 1" MediaQuality 
VideoMaterial "1" *--> "videoType 1" VideoType 
VideoMaterial  -->  VideoType 
