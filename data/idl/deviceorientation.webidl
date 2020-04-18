partial interface Window {
    [SecureContext] attribute EventHandler ondeviceorientation;
};

[Exposed=Window, SecureContext]
interface DeviceOrientationEvent : Event {
    constructor(DOMString type, optional DeviceOrientationEventInit eventInitDict = {});
    readonly attribute double? alpha;
    readonly attribute double? beta;
    readonly attribute double? gamma;
    readonly attribute boolean absolute;

    static Promise<PermissionState> requestPermission();
};

dictionary DeviceOrientationEventInit : EventInit {
    double? alpha = null;
    double? beta = null;
    double? gamma = null;
    boolean absolute = false;
};

enum PermissionState {
    "granted",
    "denied",
};

partial interface Window {
    [SecureContext] attribute EventHandler ondeviceorientationabsolute;
};

partial interface Window {
    attribute EventHandler oncompassneedscalibration;
};

partial interface Window {
    [SecureContext] attribute EventHandler ondevicemotion;
};

[Exposed=Window, SecureContext]
interface DeviceMotionEventAcceleration {
    readonly attribute double? x;
    readonly attribute double? y;
    readonly attribute double? z;
};

[Exposed=Window, SecureContext]
interface DeviceMotionEventRotationRate {
    readonly attribute double? alpha;
    readonly attribute double? beta;
    readonly attribute double? gamma;
};

[Exposed=Window, SecureContext]
interface DeviceMotionEvent : Event {
    constructor(DOMString type, optional DeviceMotionEventInit eventInitDict = {});
    readonly attribute DeviceMotionEventAcceleration? acceleration;
    readonly attribute DeviceMotionEventAcceleration? accelerationIncludingGravity;
    readonly attribute DeviceMotionEventRotationRate? rotationRate;
    readonly attribute double interval;

    static Promise<PermissionState> requestPermission();
};

dictionary DeviceMotionEventAccelerationInit {
    double? x = null;
    double? y = null;
    double? z = null;
};

dictionary DeviceMotionEventRotationRateInit {
    double? alpha = null;
    double? beta = null;
    double? gamma = null;
};

dictionary DeviceMotionEventInit : EventInit {
    DeviceMotionEventAccelerationInit acceleration;
    DeviceMotionEventAccelerationInit accelerationIncludingGravity;
    DeviceMotionEventRotationRateInit rotationRate;
    double interval = 0;
};