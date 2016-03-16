// ILocation.aidl
package com.open_source.joker.concentration;

// Declare any non-default types here with import statements

interface ILocation {

    void start();

    void stop();

    void doRgc(double lat, double lng, int acc, String source, int mapType);

}
