[LegacyNoInterfaceObject]
interface OES_vertex_array_object {
  const GLenum VERTEX_ARRAY_BINDING_OES = 0x85B5;
  undefined bindVertexArrayOES( WebGLVertexArrayObjectOES? arrayObject );
  WebGLVertexArrayObjectOES? createVertexArrayOES();
  undefined deleteVertexArrayOES( WebGLVertexArrayObjectOES? arrayObject );
  [WebGLHandlesContextLoss]
  GLboolean isVertexArrayOES( WebGLVertexArrayObjectOES? arrayObject );
};

[LegacyNoInterfaceObject]
interface WebGLVertexArrayObjectOES : WebGLObject {
};
