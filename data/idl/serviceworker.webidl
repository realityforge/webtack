[SecureContext, Exposed=(Window,Worker)]
interface ServiceWorker : EventTarget {
  readonly attribute USVString scriptURL;
  readonly attribute ServiceWorkerState state;
  void postMessage(any message, sequence<object> transfer);
  void postMessage(any message, optional PostMessageOptions options = {});

  // event
  attribute EventHandler onstatechange;
};
ServiceWorker includes AbstractWorker;

enum ServiceWorkerState {
  "parsed",
  "installing",
  "installed",
  "activating",
  "activated",
  "redundant"
};

[SecureContext, Exposed=(Window,Worker)]
interface ServiceWorkerRegistration : EventTarget {
  readonly attribute ServiceWorker? installing;
  readonly attribute ServiceWorker? waiting;
  readonly attribute ServiceWorker? active;
  [SameObject] readonly attribute NavigationPreloadManager navigationPreload;

  readonly attribute USVString scope;
  readonly attribute ServiceWorkerUpdateViaCache updateViaCache;

  [NewObject] Promise<void> update();
  [NewObject] Promise<boolean> unregister();

  // event
  attribute EventHandler onupdatefound;
};

enum ServiceWorkerUpdateViaCache {
  "imports",
  "all",
  "none"
};

partial interface Navigator {
  [SecureContext, SameObject] readonly attribute ServiceWorkerContainer serviceWorker;
};

partial interface WorkerNavigator {
  [SecureContext, SameObject] readonly attribute ServiceWorkerContainer serviceWorker;
};

[SecureContext, Exposed=(Window,Worker)]
interface ServiceWorkerContainer : EventTarget {
  readonly attribute ServiceWorker? controller;
  readonly attribute Promise<ServiceWorkerRegistration> ready;

  [NewObject] Promise<ServiceWorkerRegistration> register(USVString scriptURL, optional RegistrationOptions options = {});

  [NewObject] Promise<any> getRegistration(optional USVString clientURL = "");
  [NewObject] Promise<FrozenArray<ServiceWorkerRegistration>> getRegistrations();

  void startMessages();


  // events
  attribute EventHandler oncontrollerchange;
  attribute EventHandler onmessage; // event.source of message events is ServiceWorker object
  attribute EventHandler onmessageerror;
};

dictionary RegistrationOptions {
  USVString scope;
  WorkerType type = "classic";
  ServiceWorkerUpdateViaCache updateViaCache = "imports";
};

[SecureContext, Exposed=(Window,Worker)]
interface NavigationPreloadManager {
  Promise<void> enable();
  Promise<void> disable();
  Promise<void> setHeaderValue(ByteString value);
  Promise<NavigationPreloadState> getState();
};

dictionary NavigationPreloadState {
  boolean enabled = false;
  ByteString headerValue;
};

[Global=(Worker,ServiceWorker), Exposed=ServiceWorker]
interface ServiceWorkerGlobalScope : WorkerGlobalScope {
  [SameObject] readonly attribute Clients clients;
  [SameObject] readonly attribute ServiceWorkerRegistration registration;
  [SameObject] readonly attribute ServiceWorker serviceWorker;

  [NewObject] Promise<void> skipWaiting();

  attribute EventHandler oninstall;
  attribute EventHandler onactivate;
  attribute EventHandler onfetch;

  attribute EventHandler onmessage;
  attribute EventHandler onmessageerror;
};

[Exposed=ServiceWorker]
interface Client {
  readonly attribute USVString url;
  readonly attribute FrameType frameType;
  readonly attribute DOMString id;
  readonly attribute ClientType type;
  void postMessage(any message, sequence<object> transfer);
  void postMessage(any message, optional PostMessageOptions options = {});
};

[Exposed=ServiceWorker]
interface WindowClient : Client {
  readonly attribute VisibilityState visibilityState;
  readonly attribute boolean focused;
  [SameObject] readonly attribute FrozenArray<USVString> ancestorOrigins;
  [NewObject] Promise<WindowClient> focus();
  [NewObject] Promise<WindowClient?> navigate(USVString url);
};

enum FrameType {
  "auxiliary",
  "top-level",
  "nested",
  "none"
};

[Exposed=ServiceWorker]
interface Clients {
  // The objects returned will be new instances every time
  [NewObject] Promise<any> get(DOMString id);
  [NewObject] Promise<FrozenArray<Client>> matchAll(optional ClientQueryOptions options = {});
  [NewObject] Promise<WindowClient?> openWindow(USVString url);
  [NewObject] Promise<void> claim();
};

dictionary ClientQueryOptions {
  boolean includeUncontrolled = false;
  ClientType type = "window";
};

enum ClientType {
  "window",
  "worker",
  "sharedworker",
  "all"
};

[Exposed=ServiceWorker]
interface ExtendableEvent : Event {
  constructor(DOMString type, optional ExtendableEventInit eventInitDict = {});
  void waitUntil(Promise<any> f);
};

dictionary ExtendableEventInit : EventInit {
  // Defined for the forward compatibility across the derived events
};

[Exposed=ServiceWorker]
interface FetchEvent : ExtendableEvent {
  constructor(DOMString type, FetchEventInit eventInitDict);
  [SameObject] readonly attribute Request request;
  readonly attribute Promise<any> preloadResponse;
  readonly attribute DOMString clientId;
  readonly attribute DOMString resultingClientId;
  readonly attribute DOMString replacesClientId;
  readonly attribute Promise<void> handled;

  void respondWith(Promise<Response> r);
};

dictionary FetchEventInit : ExtendableEventInit {
  required Request request;
  Promise<any> preloadResponse;
  DOMString clientId = "";
  DOMString resultingClientId = "";
  DOMString replacesClientId = "";
  Promise<void> handled;
};

[Exposed=ServiceWorker]
interface ExtendableMessageEvent : ExtendableEvent {
  constructor(DOMString type, optional ExtendableMessageEventInit eventInitDict = {});
  readonly attribute any data;
  readonly attribute USVString origin;
  readonly attribute DOMString lastEventId;
  [SameObject] readonly attribute (Client or ServiceWorker or MessagePort)? source;
  readonly attribute FrozenArray<MessagePort> ports;
};

dictionary ExtendableMessageEventInit : ExtendableEventInit {
  any data = null;
  USVString origin = "";
  DOMString lastEventId = "";
  (Client or ServiceWorker or MessagePort)? source = null;
  sequence<MessagePort> ports = [];
};

partial interface mixin WindowOrWorkerGlobalScope {
  [SecureContext, SameObject] readonly attribute CacheStorage caches;
};

[SecureContext, Exposed=(Window,Worker)]
interface Cache {
  [NewObject] Promise<any> match(RequestInfo request, optional CacheQueryOptions options = {});
  [NewObject] Promise<FrozenArray<Response>> matchAll(optional RequestInfo request, optional CacheQueryOptions options = {});
  [NewObject] Promise<void> add(RequestInfo request);
  [NewObject] Promise<void> addAll(sequence<RequestInfo> requests);
  [NewObject] Promise<void> put(RequestInfo request, Response response);
  [NewObject] Promise<boolean> delete(RequestInfo request, optional CacheQueryOptions options = {});
  [NewObject] Promise<FrozenArray<Request>> keys(optional RequestInfo request, optional CacheQueryOptions options = {});
};

dictionary CacheQueryOptions {
  boolean ignoreSearch = false;
  boolean ignoreMethod = false;
  boolean ignoreVary = false;
};

[SecureContext, Exposed=(Window,Worker)]
interface CacheStorage {
  [NewObject] Promise<any> match(RequestInfo request, optional MultiCacheQueryOptions options = {});
  [NewObject] Promise<boolean> has(DOMString cacheName);
  [NewObject] Promise<Cache> open(DOMString cacheName);
  [NewObject] Promise<boolean> delete(DOMString cacheName);
  [NewObject] Promise<sequence<DOMString>> keys();
};

dictionary MultiCacheQueryOptions : CacheQueryOptions {
  DOMString cacheName;
};