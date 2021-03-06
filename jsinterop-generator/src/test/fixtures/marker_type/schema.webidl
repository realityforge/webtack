[MarkerType]
typedef ( TexImageSource or MyBufferSource ) TexBufferSource;

[MarkerType]
typedef ( ImageBitmap or ImageData or HTMLImageElement or HTMLCanvasElement or HTMLVideoElement or OffscreenCanvas ) TexImageSource;

interface HTMLCanvasElement {
};

interface HTMLImageElement {
};

interface HTMLVideoElement {
};

interface ImageBitmap {
};

interface ImageData {
};

interface MyBufferSource {
};

interface OffscreenCanvas {
};

interface WebGLRenderingContext {
  undefined texImage2D( TexImageSource source );
};
