package org.height185.rubiksdetector.obj3d;

public class Vector {
    public float x;
    public float y;
    public float z;
    public float theta;

    public Vector() {
        this(1,0);
    }
    public Vector(float x, float y) {
        this.x = x;
        this.y = y;
        if (x != 0.0) {
            this.theta = (float)Math.atan(y / x);
        } else {
            if (y != 0) {
                this.theta =  (float) (90 * Math.PI / 180) * (y > 0 ? 1 : -1);
            } else {
                this.theta = 0;
            }
        }
    }
    public Vector(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
        if (x != 0.0) {
            this.theta = (float)Math.atan(y / x);
        } else {
            if (y != 0) {
                this.theta =  (float) (90 * Math.PI / 180) * (y > 0 ? 1 : -1);
            } else {
                this.theta = 0;
            }
        }
    }

    static public float getAbsOfVector   (Vector v) {
        return  (float) Math.sqrt(Math.pow(v.x, 2.0) + Math.pow(v.y, 2.0));
    }
    static public float innerProduct(Vector v1, Vector v2) {
        return v1.x * v2.x + v1.y * v2.y;
    }
    static public Vector scalarProduct(Vector v, float k) {
        Vector result = new Vector();
        result.x = v.x * k;
        result.y = v.y * k;
        return result;
    }
    static public Vector vectorPlus  (Vector v1, Vector v2) {
        Vector result = new Vector();
        result.x = v1.x + v2.x;
        result.y = v1.y + v2.y;
        return result;
    }
    static public Vector vectorMinus (Vector start, Vector end) {
        Vector result = new Vector();
        result.x = end.x - start.x;
        result.y = end.y - start.y;
        return result;
    }
    static public float getDistance (Vector p1, Vector p2) {
        return getAbsOfVector(vectorMinus(p1, p2));
    }
    static public float getAngle    (Vector v1, Vector v2) {
        float cosine = innerProduct(v1, v2) / (getAbsOfVector(v1) * getAbsOfVector(v2));
        float angle  = (float) Math.acos(cosine);
        return angle;
    }
    static public Vector getUnitVector (Vector v) {
        Vector result = new Vector();
        result.x = v.x / getAbsOfVector(v);
        result.y = v.y / getAbsOfVector(v);
        return result;
    }
    static public Vector getOrthogonalUnitVector (Vector v) {
        Vector result = new Vector();
        result.x = (-1)*v.y / getAbsOfVector(v);
        result.y =      v.x / getAbsOfVector(v);
        return result;
    }

}