[Global, Exposed=RTCIdentityProviderGlobalScope]
interface RTCIdentityProviderGlobalScope : WorkerGlobalScope {
    readonly        attribute RTCIdentityProviderRegistrar rtcIdentityProvider;
};

[Exposed=RTCIdentityProviderGlobalScope]
interface RTCIdentityProviderRegistrar {
    void register (RTCIdentityProvider idp);
};

dictionary RTCIdentityProvider {
    required GenerateAssertionCallback generateAssertion;
    required ValidateAssertionCallback validateAssertion;
};

callback GenerateAssertionCallback = Promise<RTCIdentityAssertionResult>
          (DOMString contents, DOMString origin, RTCIdentityProviderOptions options);

callback ValidateAssertionCallback = Promise<RTCIdentityValidationResult>
          (DOMString assertion, DOMString origin);

dictionary RTCIdentityAssertionResult {
    required RTCIdentityProviderDetails idp;
    required DOMString                  assertion;
};

dictionary RTCIdentityProviderDetails {
    required DOMString domain;
             DOMString protocol = "default";
};

dictionary RTCIdentityValidationResult {
    required DOMString identity;
    required DOMString contents;
};

partial interface RTCPeerConnection {
    void               setIdentityProvider (DOMString provider, optional RTCIdentityProviderOptions options = {});
    Promise<DOMString> getIdentityAssertion ();
    readonly        attribute Promise<RTCIdentityAssertion> peerIdentity;
    readonly        attribute DOMString?                    idpLoginUrl;
    readonly        attribute DOMString?                    idpErrorInfo;
};

partial dictionary RTCConfiguration {
  DOMString peerIdentity;
};

dictionary RTCIdentityProviderOptions {
    DOMString protocol = "default";
    DOMString usernameHint;
    DOMString peerIdentity;
};

[Exposed=Window]
interface RTCIdentityAssertion {
    constructor(DOMString idp, DOMString name);
    attribute DOMString idp;
    attribute DOMString name;
};

partial interface RTCError {
  readonly attribute long? httpRequestStatusCode;
};

partial dictionary RTCErrorInit {
  long httpRequestStatusCode;
};

// This is an extension of RTCErrorDetailType from [[WEBRTC-PC]]
// Unfortunately, WebIDL does not support partial enums (yet).
//
// partial enum RTCErrorDetailType {
enum RTCErrorDetailTypeIdp {
  "idp-bad-script-failure",
  "idp-execution-failure",
  "idp-load-failure",
  "idp-need-login",
  "idp-timeout",
  "idp-tls-failure",
  "idp-token-expired",
  "idp-token-invalid",
};

partial dictionary MediaStreamConstraints {
             DOMString peerIdentity;
};

partial interface MediaStreamTrack {
    readonly        attribute boolean      isolated;
                    attribute EventHandler onisolationchange;
};