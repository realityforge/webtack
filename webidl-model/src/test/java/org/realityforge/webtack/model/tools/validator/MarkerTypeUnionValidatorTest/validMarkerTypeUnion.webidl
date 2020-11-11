[MarkerType, JavaSubPackage=gl]
typedef ( ImageBitmap or ImageData or HTMLImageElement or HTMLCanvasElement or HTMLVideoElement or OffscreenCanvas ) TexImageSource;

interface ImageBitmap {};
interface ImageData {};
interface HTMLImageElement {};
interface HTMLCanvasElement {};
interface HTMLVideoElement {};
interface OffscreenCanvas {};

// Has a non-reference but fine as not a MarkerType
typedef ( ImageBitmap or ImageData or HTMLImageElement or long ) NonMarkerTexImageSource1;

// Has a reference too enum but fine as not a MarkerType
typedef ( ImageBitmap or ImageData or HTMLImageElement or BasicEnumeration ) NonMarkerTexImageSource2;

enum BasicEnumeration { "A" };
