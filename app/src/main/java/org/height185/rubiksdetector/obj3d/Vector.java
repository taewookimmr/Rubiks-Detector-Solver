package org.height185.rubiksdetector.obj3d;

public class Vector {
    public float x;
    public float y;
    public float z;

    public Vector() {
        this(1, 0);
    }

    public Vector(float x, float y) {
        this(x, y, 0f);
    }
    public Vector(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
}