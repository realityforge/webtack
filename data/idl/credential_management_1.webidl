[Exposed=Window, SecureContext]
interface Credential {
  readonly attribute USVString id;
  readonly attribute DOMString type;
};

[SecureContext]
interface mixin CredentialUserData {
  readonly attribute USVString name;
  readonly attribute USVString iconURL;
};

partial interface Navigator {
  [SecureContext, SameObject] readonly attribute CredentialsContainer credentials;
};

[Exposed=Window, SecureContext]
interface CredentialsContainer {
  Promise<Credential?> get(optional CredentialRequestOptions options);
  Promise<Credential> store(Credential credential);
  Promise<Credential?> create(optional CredentialCreationOptions options);
  Promise<void> preventSilentAccess();
};

dictionary CredentialData {
  required USVString id;
};

dictionary CredentialRequestOptions {
  CredentialMediationRequirement mediation = "optional";
  AbortSignal signal;
};

enum CredentialMediationRequirement {
  "silent",
  "optional",
  "required"
};

dictionary CredentialCreationOptions {
  AbortSignal signal;
};

[Constructor(HTMLFormElement form),
 Constructor(PasswordCredentialData data),
 Exposed=Window,
 SecureContext]
interface PasswordCredential : Credential {
  readonly attribute USVString password;
};
PasswordCredential includes CredentialUserData;

partial dictionary CredentialRequestOptions {
  boolean password = false;
};

dictionary PasswordCredentialData : CredentialData {
  USVString name;
  USVString iconURL;
  required USVString origin;
  required USVString password;
};

typedef (PasswordCredentialData or HTMLFormElement) PasswordCredentialInit;

partial dictionary CredentialCreationOptions {
  PasswordCredentialInit password;
};

[Constructor(FederatedCredentialInit data),
 Exposed=Window,
 SecureContext]
interface FederatedCredential : Credential {
  readonly attribute USVString provider;
  readonly attribute DOMString? protocol;
};
FederatedCredential includes CredentialUserData;

dictionary FederatedCredentialRequestOptions {
  sequence<USVString> providers;
  sequence<DOMString> protocols;
};

partial dictionary CredentialRequestOptions {
  FederatedCredentialRequestOptions federated;
};

dictionary FederatedCredentialInit : CredentialData {
  USVString name;
  USVString iconURL;
  required USVString origin;
  required USVString provider;
  DOMString protocol;
};

partial dictionary CredentialCreationOptions {
  FederatedCredentialInit federated;
};