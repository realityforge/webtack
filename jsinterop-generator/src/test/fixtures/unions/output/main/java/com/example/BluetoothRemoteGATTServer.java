package com.example;

import elemental3.lang.JsArray;
import elemental3.promise.Promise;
import javax.annotation.Generated;
import javax.annotation.Nonnull;
import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsType;

@Generated("org.realityforge.webtack")
@JsType(
    isNative = true,
    namespace = JsPackage.GLOBAL,
    name = "BluetoothRemoteGATTServer"
)
public class BluetoothRemoteGATTServer {
  protected BluetoothRemoteGATTServer() {
  }

  @Nonnull
  public native Promise<BluetoothRemoteGATTService> getPrimaryService(
      @Nonnull BluetoothServiceUUID service);

  @Nonnull
  public native Promise<BluetoothRemoteGATTService> getPrimaryService(@Nonnull String service);

  @Nonnull
  public native Promise<BluetoothRemoteGATTService> getPrimaryService(int service);

  @Nonnull
  public native Promise<JsArray<BluetoothRemoteGATTService>> getPrimaryServices(
      @Nonnull BluetoothServiceUUID service);

  @Nonnull
  public native Promise<JsArray<BluetoothRemoteGATTService>> getPrimaryServices(
      @Nonnull String service);

  @Nonnull
  public native Promise<JsArray<BluetoothRemoteGATTService>> getPrimaryServices(int service);

  @Nonnull
  public native Promise<JsArray<BluetoothRemoteGATTService>> getPrimaryServices();
}
