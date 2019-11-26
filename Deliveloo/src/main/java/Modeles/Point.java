package Modeles;

public class Point {

    private Double longitude;
    private Double latitude;

    public Point(Double longitude, Double latitude) {
        this.longitude=longitude;
        this.latitude=latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    @java.lang.Override
    public java.lang.String toString() {
        return "Point{" +
                "longitude=" + longitude +
                ", latitude=" + latitude +
                '}';
    }
}