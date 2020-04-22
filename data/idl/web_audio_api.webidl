enum AutomationRate {
  "a-rate",
  "k-rate"
};

enum AudioContextState {
  "closed",
  "running",
  "suspended"
};

enum AudioContextLatencyCategory {
  "balanced",
  "interactive",
  "playback"
};

enum OverSampleType {
  "2x",
  "4x",
  "none"
};

enum BiquadFilterType {
  "allpass",
  "bandpass",
  "highpass",
  "highshelf",
  "lowpass",
  "lowshelf",
  "notch",
  "peaking"
};

enum PanningModelType {
  "HRTF",
  "equalpower"
};

enum DistanceModelType {
  "exponential",
  "inverse",
  "linear"
};

enum ChannelCountMode {
  "clamped-max",
  "explicit",
  "max"
};

enum ChannelInterpretation {
  "discrete",
  "speakers"
};

enum OscillatorType {
  "custom",
  "sawtooth",
  "sine",
  "square",
  "triangle"
};

callback AudioWorkletProcessorConstructor = AudioWorkletProcessor ( object options );

callback DecodeSuccessCallback = void ( AudioBuffer decodedData );

callback DecodeErrorCallback = void ( DOMException error );

dictionary PeriodicWaveOptions : PeriodicWaveConstraints {
  sequence<float> imag;
  sequence<float> real;
};

dictionary WaveShaperOptions : AudioNodeOptions {
  sequence<float> curve;
  OverSampleType oversample = "none";
};

dictionary AudioBufferOptions {
  required unsigned long length;
  unsigned long numberOfChannels = 1;
  required float sampleRate;
};

dictionary OfflineAudioContextOptions {
  required unsigned long length;
  unsigned long numberOfChannels = 1;
  required float sampleRate;
};

dictionary ConvolverOptions : AudioNodeOptions {
  AudioBuffer? buffer;
  boolean disableNormalization = false;
};

dictionary StereoPannerOptions : AudioNodeOptions {
  float pan = 0;
};

dictionary ChannelSplitterOptions : AudioNodeOptions {
  unsigned long numberOfOutputs = 6;
};

dictionary MediaElementAudioSourceOptions {
  required HTMLMediaElement mediaElement;
};

dictionary IIRFilterOptions : AudioNodeOptions {
  required sequence<double> feedback;
  required sequence<double> feedforward;
};

dictionary PannerOptions : AudioNodeOptions {
  double coneInnerAngle = 360;
  double coneOuterAngle = 360;
  double coneOuterGain = 0;
  DistanceModelType distanceModel = "inverse";
  double maxDistance = 10000;
  float orientationX = 1;
  float orientationY = 0;
  float orientationZ = 0;
  PanningModelType panningModel = "equalpower";
  float positionX = 0;
  float positionY = 0;
  float positionZ = 0;
  double refDistance = 1;
  double rolloffFactor = 1;
};

dictionary BiquadFilterOptions : AudioNodeOptions {
  float Q = 1;
  float detune = 0;
  float frequency = 350;
  float gain = 0;
  BiquadFilterType type = "lowpass";
};

dictionary ConstantSourceOptions {
  float offset = 1;
};

dictionary OfflineAudioCompletionEventInit : EventInit {
  required AudioBuffer renderedBuffer;
};

dictionary AudioContextOptions {
  ( AudioContextLatencyCategory or double ) latencyHint = "interactive";
  float sampleRate;
};

dictionary OscillatorOptions : AudioNodeOptions {
  float detune = 0;
  float frequency = 440;
  PeriodicWave periodicWave;
  OscillatorType type = "sine";
};

dictionary MediaStreamAudioSourceOptions {
  required MediaStream mediaStream;
};

dictionary AudioProcessingEventInit : EventInit {
  required AudioBuffer inputBuffer;
  required AudioBuffer outputBuffer;
  required double playbackTime;
};

dictionary AudioNodeOptions {
  unsigned long channelCount;
  ChannelCountMode channelCountMode;
  ChannelInterpretation channelInterpretation;
};

dictionary AnalyserOptions : AudioNodeOptions {
  unsigned long fftSize = 2048;
  double maxDecibels = -30;
  double minDecibels = -100;
  double smoothingTimeConstant = 0.8;
};

dictionary GainOptions : AudioNodeOptions {
  float gain = 1.0;
};

dictionary ChannelMergerOptions : AudioNodeOptions {
  unsigned long numberOfInputs = 6;
};

dictionary PeriodicWaveConstraints {
  boolean disableNormalization = false;
};

dictionary AudioParamDescriptor {
  AutomationRate automationRate = "a-rate";
  float defaultValue = 0;
  float maxValue = 3.4028235e38;
  float minValue = -3.4028235e38;
  required DOMString name;
};

dictionary MediaStreamTrackAudioSourceOptions {
  required MediaStreamTrack mediaStreamTrack;
};

dictionary AudioBufferSourceOptions {
  AudioBuffer? buffer;
  float detune = 0;
  boolean loop = false;
  double loopEnd = 0;
  double loopStart = 0;
  float playbackRate = 1;
};

dictionary AudioTimestamp {
  double contextTime;
  DOMHighResTimeStamp performanceTime;
};

dictionary DynamicsCompressorOptions : AudioNodeOptions {
  float attack = 0.003;
  float knee = 30;
  float ratio = 12;
  float release = 0.25;
  float threshold = -24;
};

dictionary DelayOptions : AudioNodeOptions {
  double delayTime = 0;
  double maxDelayTime = 1;
};

dictionary AudioWorkletNodeOptions : AudioNodeOptions {
  unsigned long numberOfInputs = 1;
  unsigned long numberOfOutputs = 1;
  sequence<unsigned long> outputChannelCount;
  record<DOMString, double> parameterData;
  object processorOptions;
};

[Exposed=Window]
interface OscillatorNode : AudioScheduledSourceNode {
  readonly attribute AudioParam detune;
  readonly attribute AudioParam frequency;
  attribute OscillatorType type;
  constructor( BaseAudioContext context, optional OscillatorOptions options = {} );
  void setPeriodicWave( PeriodicWave periodicWave );
};

[Exposed=Window]
interface OfflineAudioContext : BaseAudioContext {
  readonly attribute unsigned long length;
  attribute EventHandler oncomplete;
  constructor( OfflineAudioContextOptions contextOptions );
  constructor( unsigned long numberOfChannels, unsigned long length, float sampleRate );
  Promise<void> resume();
  Promise<AudioBuffer> startRendering();
  Promise<void> suspend( double suspendTime );
};

[Exposed=Window]
interface WaveShaperNode : AudioNode {
  attribute Float32Array? curve;
  attribute OverSampleType oversample;
  constructor( BaseAudioContext context, optional WaveShaperOptions options = {} );
};

[Exposed=Window]
interface AudioDestinationNode : AudioNode {
  readonly attribute unsigned long maxChannelCount;
};

[Exposed=Window]
interface AudioProcessingEvent : Event {
  readonly attribute AudioBuffer inputBuffer;
  readonly attribute AudioBuffer outputBuffer;
  readonly attribute double playbackTime;
  constructor( DOMString type, AudioProcessingEventInit eventInitDict );
};

[Exposed=Window]
interface PeriodicWave {
  constructor( BaseAudioContext context, optional PeriodicWaveOptions options = {} );
};

[Exposed=Window]
interface ConvolverNode : AudioNode {
  attribute AudioBuffer? buffer;
  attribute boolean normalize;
  constructor( BaseAudioContext context, optional ConvolverOptions options = {} );
};

[Exposed=Window]
interface BaseAudioContext : EventTarget {
  [SameObject, SecureContext]
  readonly attribute AudioWorklet audioWorklet;
  readonly attribute double currentTime;
  readonly attribute AudioDestinationNode destination;
  readonly attribute AudioListener listener;
  readonly attribute float sampleRate;
  readonly attribute AudioContextState state;
  attribute EventHandler onstatechange;
  AnalyserNode createAnalyser();
  BiquadFilterNode createBiquadFilter();
  AudioBuffer createBuffer( unsigned long numberOfChannels, unsigned long length, float sampleRate );
  AudioBufferSourceNode createBufferSource();
  ChannelMergerNode createChannelMerger( optional unsigned long numberOfInputs = 6 );
  ChannelSplitterNode createChannelSplitter( optional unsigned long numberOfOutputs = 6 );
  ConstantSourceNode createConstantSource();
  ConvolverNode createConvolver();
  DelayNode createDelay( optional double maxDelayTime = 1.0 );
  DynamicsCompressorNode createDynamicsCompressor();
  GainNode createGain();
  IIRFilterNode createIIRFilter( sequence<double> feedforward, sequence<double> feedback );
  OscillatorNode createOscillator();
  PannerNode createPanner();
  PeriodicWave createPeriodicWave( sequence<float> real, sequence<float> imag, optional PeriodicWaveConstraints constraints = {} );
  ScriptProcessorNode createScriptProcessor( optional unsigned long bufferSize = 0, optional unsigned long numberOfInputChannels = 2, optional unsigned long numberOfOutputChannels = 2 );
  StereoPannerNode createStereoPanner();
  WaveShaperNode createWaveShaper();
  Promise<AudioBuffer> decodeAudioData( ArrayBuffer audioData, optional DecodeSuccessCallback? successCallback, optional DecodeErrorCallback? errorCallback );
};

[Exposed=Window]
interface GainNode : AudioNode {
  readonly attribute AudioParam gain;
  constructor( BaseAudioContext context, optional GainOptions options = {} );
};

[Global=(Worklet,AudioWorklet), Exposed=AudioWorklet]
interface AudioWorkletGlobalScope : WorkletGlobalScope {
  readonly attribute unsigned long long currentFrame;
  readonly attribute double currentTime;
  readonly attribute float sampleRate;
  void registerProcessor( DOMString name, AudioWorkletProcessorConstructor processorCtor );
};

[Exposed=Window]
interface AnalyserNode : AudioNode {
  readonly attribute unsigned long frequencyBinCount;
  attribute unsigned long fftSize;
  attribute double maxDecibels;
  attribute double minDecibels;
  attribute double smoothingTimeConstant;
  constructor( BaseAudioContext context, optional AnalyserOptions options = {} );
  void getByteFrequencyData( Uint8Array array );
  void getByteTimeDomainData( Uint8Array array );
  void getFloatFrequencyData( Float32Array array );
  void getFloatTimeDomainData( Float32Array array );
};

[Exposed=Window, SecureContext]
interface AudioWorkletNode : AudioNode {
  readonly attribute AudioParamMap parameters;
  readonly attribute MessagePort port;
  attribute EventHandler onprocessorerror;
  constructor( BaseAudioContext context, DOMString name, optional AudioWorkletNodeOptions options = {} );
};

[Exposed=Window]
interface ChannelMergerNode : AudioNode {
  constructor( BaseAudioContext context, optional ChannelMergerOptions options = {} );
};

[Exposed=Window]
interface MediaStreamAudioSourceNode : AudioNode {
  [SameObject]
  readonly attribute MediaStream mediaStream;
  constructor( AudioContext context, MediaStreamAudioSourceOptions options );
};

[Exposed=Window]
interface MediaStreamTrackAudioSourceNode : AudioNode {
  constructor( AudioContext context, MediaStreamTrackAudioSourceOptions options );
};

[Exposed=Window]
interface AudioBuffer {
  readonly attribute double duration;
  readonly attribute unsigned long length;
  readonly attribute unsigned long numberOfChannels;
  readonly attribute float sampleRate;
  constructor( AudioBufferOptions options );
  void copyFromChannel( Float32Array destination, unsigned long channelNumber, optional unsigned long bufferOffset = 0 );
  void copyToChannel( Float32Array source, unsigned long channelNumber, optional unsigned long bufferOffset = 0 );
  Float32Array getChannelData( unsigned long channel );
};

[Exposed=Window]
interface MediaElementAudioSourceNode : AudioNode {
  [SameObject]
  readonly attribute HTMLMediaElement mediaElement;
  constructor( AudioContext context, MediaElementAudioSourceOptions options );
};

[Exposed=Window]
interface StereoPannerNode : AudioNode {
  readonly attribute AudioParam pan;
  constructor( BaseAudioContext context, optional StereoPannerOptions options = {} );
};

[Exposed=Window]
interface ConstantSourceNode : AudioScheduledSourceNode {
  readonly attribute AudioParam offset;
  constructor( BaseAudioContext context, optional ConstantSourceOptions options = {} );
};

[Exposed=Window]
interface DynamicsCompressorNode : AudioNode {
  readonly attribute AudioParam attack;
  readonly attribute AudioParam knee;
  readonly attribute AudioParam ratio;
  readonly attribute float reduction;
  readonly attribute AudioParam release;
  readonly attribute AudioParam threshold;
  constructor( BaseAudioContext context, optional DynamicsCompressorOptions options = {} );
};

[Exposed=AudioWorklet]
interface AudioWorkletProcessor {
  readonly attribute MessagePort port;
  constructor();
};

[Exposed=Window]
interface ChannelSplitterNode : AudioNode {
  constructor( BaseAudioContext context, optional ChannelSplitterOptions options = {} );
};

[Exposed=Window]
interface AudioParamMap {
  readonly maplike<DOMString, AudioParam>;
};

[Exposed=Window]
interface AudioListener {
  readonly attribute AudioParam forwardX;
  readonly attribute AudioParam forwardY;
  readonly attribute AudioParam forwardZ;
  readonly attribute AudioParam positionX;
  readonly attribute AudioParam positionY;
  readonly attribute AudioParam positionZ;
  readonly attribute AudioParam upX;
  readonly attribute AudioParam upY;
  readonly attribute AudioParam upZ;
  void setOrientation( float x, float y, float z, float xUp, float yUp, float zUp );
  void setPosition( float x, float y, float z );
};

[Exposed=Window]
interface BiquadFilterNode : AudioNode {
  readonly attribute AudioParam Q;
  readonly attribute AudioParam detune;
  readonly attribute AudioParam frequency;
  readonly attribute AudioParam gain;
  attribute BiquadFilterType type;
  constructor( BaseAudioContext context, optional BiquadFilterOptions options = {} );
  void getFrequencyResponse( Float32Array frequencyHz, Float32Array magResponse, Float32Array phaseResponse );
};

[Exposed=Window]
interface ScriptProcessorNode : AudioNode {
  readonly attribute long bufferSize;
  attribute EventHandler onaudioprocess;
};

[Exposed=Window]
interface AudioContext : BaseAudioContext {
  readonly attribute double baseLatency;
  readonly attribute double outputLatency;
  constructor( optional AudioContextOptions contextOptions = {} );
  Promise<void> close();
  MediaElementAudioSourceNode createMediaElementSource( HTMLMediaElement mediaElement );
  MediaStreamAudioDestinationNode createMediaStreamDestination();
  MediaStreamAudioSourceNode createMediaStreamSource( MediaStream mediaStream );
  MediaStreamTrackAudioSourceNode createMediaStreamTrackSource( MediaStreamTrack mediaStreamTrack );
  AudioTimestamp getOutputTimestamp();
  Promise<void> resume();
  Promise<void> suspend();
};

[Exposed=Window]
interface MediaStreamAudioDestinationNode : AudioNode {
  readonly attribute MediaStream stream;
  constructor( AudioContext context, optional AudioNodeOptions options = {} );
};

[Exposed=Window]
interface IIRFilterNode : AudioNode {
  constructor( BaseAudioContext context, IIRFilterOptions options );
  void getFrequencyResponse( Float32Array frequencyHz, Float32Array magResponse, Float32Array phaseResponse );
};

[Exposed=Window, SecureContext]
interface AudioWorklet : Worklet {
};

[Exposed=Window]
interface OfflineAudioCompletionEvent : Event {
  readonly attribute AudioBuffer renderedBuffer;
  constructor( DOMString type, OfflineAudioCompletionEventInit eventInitDict );
};

[Exposed=Window]
interface PannerNode : AudioNode {
  readonly attribute AudioParam orientationX;
  readonly attribute AudioParam orientationY;
  readonly attribute AudioParam orientationZ;
  readonly attribute AudioParam positionX;
  readonly attribute AudioParam positionY;
  readonly attribute AudioParam positionZ;
  attribute double coneInnerAngle;
  attribute double coneOuterAngle;
  attribute double coneOuterGain;
  attribute DistanceModelType distanceModel;
  attribute double maxDistance;
  attribute PanningModelType panningModel;
  attribute double refDistance;
  attribute double rolloffFactor;
  constructor( BaseAudioContext context, optional PannerOptions options = {} );
  void setOrientation( float x, float y, float z );
  void setPosition( float x, float y, float z );
};

[Exposed=Window]
interface AudioScheduledSourceNode : AudioNode {
  attribute EventHandler onended;
  void start( optional double when = 0 );
  void stop( optional double when = 0 );
};

[Exposed=Window]
interface AudioParam {
  readonly attribute float defaultValue;
  readonly attribute float maxValue;
  readonly attribute float minValue;
  attribute AutomationRate automationRate;
  attribute float value;
  AudioParam cancelAndHoldAtTime( double cancelTime );
  AudioParam cancelScheduledValues( double cancelTime );
  AudioParam exponentialRampToValueAtTime( float value, double endTime );
  AudioParam linearRampToValueAtTime( float value, double endTime );
  AudioParam setTargetAtTime( float target, double startTime, float timeConstant );
  AudioParam setValueAtTime( float value, double startTime );
  AudioParam setValueCurveAtTime( sequence<float> values, double startTime, double duration );
};

[Exposed=Window]
interface AudioBufferSourceNode : AudioScheduledSourceNode {
  readonly attribute AudioParam detune;
  readonly attribute AudioParam playbackRate;
  attribute AudioBuffer? buffer;
  attribute boolean loop;
  attribute double loopEnd;
  attribute double loopStart;
  constructor( BaseAudioContext context, optional AudioBufferSourceOptions options = {} );
  void start( optional double when = 0, optional double offset, optional double duration );
};

[Exposed=Window]
interface DelayNode : AudioNode {
  readonly attribute AudioParam delayTime;
  constructor( BaseAudioContext context, optional DelayOptions options = {} );
};

[Exposed=Window]
interface AudioNode : EventTarget {
  readonly attribute BaseAudioContext context;
  readonly attribute unsigned long numberOfInputs;
  readonly attribute unsigned long numberOfOutputs;
  attribute unsigned long channelCount;
  attribute ChannelCountMode channelCountMode;
  attribute ChannelInterpretation channelInterpretation;
  AudioNode connect( AudioNode destinationNode, optional unsigned long output = 0, optional unsigned long input = 0 );
  void connect( AudioParam destinationParam, optional unsigned long output = 0 );
  void disconnect();
  void disconnect( unsigned long output );
  void disconnect( AudioNode destinationNode );
  void disconnect( AudioNode destinationNode, unsigned long output );
  void disconnect( AudioNode destinationNode, unsigned long output, unsigned long input );
  void disconnect( AudioParam destinationParam );
  void disconnect( AudioParam destinationParam, unsigned long output );
};
