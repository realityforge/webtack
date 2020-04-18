[Exposed=(Window,Worker),
 LegacyWindowAlias=webkitURL]
interface URL {
  constructor(USVString url, optional USVString base);

  stringifier attribute USVString href;
  readonly attribute USVString origin;
           attribute USVString protocol;
           attribute USVString username;
           attribute USVString password;
           attribute USVString host;
           attribute USVString hostname;
           attribute USVString port;
           attribute USVString pathname;
           attribute USVString search;
  [SameObject] readonly attribute URLSearchParams searchParams;
           attribute USVString hash;

  USVString toJSON();
};

[Exposed=(Window,Worker)]
interface URLSearchParams {
  constructor(optional (sequence<sequence<USVString>> or record<USVString, USVString> or USVString) init = "");

  void append(USVString name, USVString value);
  void delete(USVString name);
  USVString? get(USVString name);
  sequence<USVString> getAll(USVString name);
  boolean has(USVString name);
  void set(USVString name, USVString value);

  void sort();

  iterable<USVString, USVString>;
  stringifier;
};