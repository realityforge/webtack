enum CursorCaptureConstraint {
  "always",
  "motion",
  "never"
};

enum DisplayCaptureSurfaceType {
  "application",
  "browser",
  "monitor",
  "window"
};

dictionary DisplayMediaStreamConstraints {
  ( boolean or MediaTrackConstraints ) audio = false;
  ( boolean or MediaTrackConstraints ) video = true;
};

partial dictionary MediaTrackConstraintSet {
  ConstrainDOMString cursor;
  ConstrainDOMString displaySurface;
  ConstrainBoolean logicalSurface;
  ConstrainBoolean restrictOwnAudio;
};

partial dictionary MediaTrackSettings {
  DOMString cursor;
  DOMString displaySurface;
  boolean logicalSurface;
};

partial dictionary MediaTrackSupportedConstraints {
  boolean cursor = true;
  boolean displaySurface = true;
  boolean logicalSurface = true;
  boolean restrictOwnAudio = true;
};

partial interface MediaDevices {
  Promise<MediaStream> getDisplayMedia( optional DisplayMediaStreamConstraints constraints = {} );
};
