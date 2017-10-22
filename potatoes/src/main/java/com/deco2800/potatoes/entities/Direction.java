package com.deco2800.potatoes.entities;


/**
 * An enumeration for defining the cardinal & ordinal directions.
 * The directions are described as follows: North, North-East, East,
 * South-East, South, South-West, West, North-West.
 * This class also provides methods for converting to & from an angle.
 *
 * @author petercondoleon, Tazman Schmidt
 */
public enum Direction { N("North"), NE("North-East"), E("East"), SE("South-East"),
    S("South"), SW("South-West"), W("West"), NW("North-West");

    private final String name;

    private Direction(String name) {
        this.name = name;
    }

    /**
     * Returns the string name of the direction. For example,
     * a direction of 'NE' will return 'North-East'.
     */
    @Override
    public String toString() {
        return name;
    }

    /**
     * Converts from this direction to the theta angle in radians
     *
     * @return The angle of this direction in radians
     */
    public float getAngleRad() {

        if (this == NE){
            return 0;
          }
        else if (this == E){
            return 0.78540f;
          }    // 45 deg
        else if (this == SE){
            return 1.57080f;
          }    // 90 deg
        else if (this == S){
            return 2.35619f;
          }    // 135 deg
        else if (this == SW){
            return (float) Math.PI;    // 180 deg
          }
        else if (this == W){
            return 3.92700f;    // 225 deg
          }
        else if (this == NW){
            return 4.71239f;    // 270 deg
          }
        else{
            return 5.49779f;    // 315 deg
          }
    }

    /**
     * Converts from this direction to the theta angle in degrees
     *
     * @return The angle of this direction in degrees
     */
    public float getAngleDeg() {
            return getAngleRad() / (float) Math.PI * 180;
    }

    /**
     * Returns an angle based on the x and y components of
     * a vector representing the movement direction of an entity
     * The angle is in Radians.
     * if xSpeed & ySpeed are both 0, returns 0
     *
     * @param xVector The x component of a movement vector.
     * @param yVector The y component of a movement vector.
     * @return
     */
    public static float getRadFromCoords(float xVector, float yVector) {
        return (float) Math.atan2(yVector, xVector);
    }

    /**
     * Returns a {@code Direction} based on the x and y components of
     * a vector representing the movement direction of an entity
     * if xSpeed & ySpeed are both 0, default NE
     *
     * @param xVector The x component of a movement vector.
     * @param yVector The y component of a movement vector.
     * @return
     */
    public static Direction getFromCoords(float xVector, float yVector) {
        return getFromRad( getRadFromCoords( xVector, yVector) );
    }

    /**
     * Returns a {@code Direction} based on the angle of
     * a vector representing the movement direction of an entity.
     * The angle starts at positive x and rotates counterClockwise
     * The angle is in Degrees, it is preferred to use {@code getFromRad}
     * and work on the angle in Radians.
     *
     * @param angle     The angle of the movement vector of an entity
     * @return          The {@code Direction} closest to the angle
     */
    public static Direction getFromDeg(float angle) {
        return getFromRad(angle / 180 * (float) Math.PI );
    }

    /**
     * Returns a {@code Direction} based on the angle of
     * a vector representing the movement direction of an entity.
     * The angle starts at positive x and rotates counterClockwise
     * The angle is in Radians.
     *
     * @param angle     The angle of the movement vector of an entity
     * @return          The {@code Direction} closest to the angle
     */
    public static Direction getFromRad(float angle) {

        float twoPi = (float) Math.PI * 2;
        float angleMod = (angle % twoPi + twoPi) % twoPi ;

        if (angleMod < 0.392699)    // 22.5 deg
            return NE;
        if (angleMod < 1.178097)    // 67.5 deg
            return E;
        if (angleMod < 1.963495)    // 112.5 deg
            return SE;
        if (angleMod < 2.748894)    // 157.5 deg
            return S;
        if (angleMod < 3.534292)    // 202.5 deg
            return SW;
        if (angleMod < 4.31969)     // 247.5 deg
            return W;
        if (angleMod < 5.105088)    // 292.5 deg
            return NW;
        if (angleMod < 5.890486)    // 337.5 deg
            return N;

        return NE;
    }


}
