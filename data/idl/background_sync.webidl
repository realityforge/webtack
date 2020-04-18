partial interface ServiceWorkerRegistration {
  readonly attribute SyncManager sync;
};

[Exposed=(Window,Worker)]
interface SyncManager {
  Promise<void> register(DOMString tag);
  Promise<sequence<DOMString>> getTags();
};

partial interface ServiceWorkerGlobalScope {
  attribute EventHandler onsync;
};

[Constructor(DOMString type, SyncEventInit init), Exposed=ServiceWorker]
interface SyncEvent : ExtendableEvent {
  readonly attribute DOMString tag;
  readonly attribute boolean lastChance;
};

dictionary SyncEventInit : ExtendableEventInit {
  required DOMString tag;
  boolean lastChance = false;
};