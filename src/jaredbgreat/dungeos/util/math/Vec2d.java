package jaredbgreat.dungeos.util.math;

import java.util.Arrays;

/**
 *
 * @author Jared Blackburn
 */
public class Vec2d {
    private final double[] data = new double[2];
    
    public Vec2d() {}
    
    
    public Vec2d(double x, double y) {
        data[0] = x;
        data[1] = y;
    }
    
    
    public void add(Vec2d in) {
        data[0] += in.data[0];
        data[1] += in.data[1];
    }
    
    
    public static Vec2d add(Vec2d a, Vec2d b) {
        Vec2d out = new Vec2d();
        out.data[0] = a.data[0] + b.data[0];
        out.data[1] = a.data[1] + b.data[1];
        return out;
    }
    
    
    public static void add(Vec2d a, Vec2d b, Vec2d out) {
        out.data[0] = a.data[0] + b.data[0];
        out.data[1] = a.data[1] + b.data[1];
    }
    
    
    public void sub(Vec2d in) {
        data[0] -= in.data[0];
        data[1] -= in.data[1];
    }
    
    
    public static Vec2d sub(Vec2d a, Vec2d b) {
        Vec2d out = new Vec2d();
        out.data[0] = a.data[0] - b.data[0];
        out.data[1] = a.data[1] - b.data[1];
        return out;
    }
    
    
    public static void sub(Vec2d a, Vec2d b, Vec2d out) {
        out.data[0] = a.data[0] - b.data[0];
        out.data[1] = a.data[1] - b.data[1];
    }
    
    
    public void mul(double in) {
        data[0] *= in;
        data[1] *= in;
    }
    
    
    public void mul(Vec2d in) {
        data[0] *= in.data[0];
        data[1] *= in.data[1];
    }
    
    
    public static Vec2d mul(Vec2d a, Vec2d b) {
        Vec2d out = new Vec2d();
        out.data[0] = a.data[0] * b.data[0];
        out.data[1] = a.data[1] * b.data[1];
        return out;
    }
    
    
    public static Vec2d mul(Vec2d a, double b) {
        Vec2d out = new Vec2d();
        out.data[0] = a.data[0] * b;
        out.data[1] = a.data[1] * b;
        return out;
    }
    
    
    public static void mul(Vec2d a, Vec2d b, Vec2d out) {
        out.data[0] = a.data[0] * b.data[0];
        out.data[1] = a.data[1] * b.data[1];
    }
    
    
    public static void mul(Vec2d a, double b, Vec2d out) {
        out.data[0] = a.data[0] * b;
        out.data[1] = a.data[1] * b;
    }
    
    
    public void div(double in) {
        data[0] /= in;
        data[1] /= in;
    }
    
    
    public void div(Vec2d in) {
        data[0] /= in.data[0];
        data[1] /= in.data[1];
    }
    
    
    public static Vec2d div(Vec2d a, Vec2d b) {
        Vec2d out = new Vec2d();
        out.data[0] = a.data[0] / b.data[0];
        out.data[1] = a.data[1] / b.data[1];
        return out;
    }
    
    
    public static Vec2d div(Vec2d a, double b) {
        Vec2d out = new Vec2d();
        out.data[0] = a.data[0] / b;
        out.data[1] = a.data[1] / b;
        return out;
    }
    
    
    public static void div(Vec2d a, Vec2d b, Vec2d out) {
        out.data[0] = a.data[0] / b.data[0];
        out.data[1] = a.data[1] / b.data[1];
    }
    
    
    public static void div(Vec2d a, double b, Vec2d out) {
        out.data[0] = a.data[0] * b;
        out.data[1] = a.data[1] * b;
    }
    
    
    public double dot(Vec2d in) {
        return ((data[0] * in.data[0]) + (data[1] * in.data[1]));
    }
    
    
    public static double dot(Vec2d a, Vec2d b) {
        return ((a.data[0] * b.data[0]) + (a.data[1] * b.data[1]));    
    }
    
    
    public double length() {
        return Math.sqrt((data[0] * data[0]) + (data[1] * data[1]));
    }
    
    
    public double sqLength() {
        return ((data[0] * data[0]) + (data[1] * data[1]));
    }
    
    
    public void normalize() {
        double length = Math.sqrt((data[0] * data[0]) + (data[1] * data[1]));
        data[0] /= length;
        data[1] /= length;
    }
    
    
    public Vec2d getNormalize() {
        double length = Math.sqrt((data[0] * data[0]) + (data[1] * data[1]));
        return new Vec2d((float)(data[0] / length), (float)(data[1] / length));
    }
    
    
    public void getNormalize(Vec2d out) {
        double length = Math.sqrt((data[0] * data[0]) + (data[1] * data[1]));
        out.set((float)(data[0] / length), (float)(data[1] / length));
    }
    
    
    public void rotate(float angle) {
        double sine = Math.sin(angle);
        double cosine = Math.cos(angle);
        data[0] = (float) ((data[0] * cosine) - (data[1] * sine));
        data[1] = (float) ((data[0] * sine)   + (data[1] * cosine));
    }
    
    
    public static Vec2d rotate(Vec2d in, float angle) {
        double sine = Math.sin(angle);
        double cosine = Math.cos(angle);
        return new Vec2d((float)((in.data[0] * cosine) - (in.data[1] * sine)),
                         (float)((in.data[0] * sine)   + (in.data[1] * cosine)));
    }
    
    
    public static void rotate(Vec2d in, float angle, Vec2d out) {
        double sine = Math.sin(angle);
        double cosine = Math.cos(angle);
        out.set((float)((in.data[0] * cosine) - (in.data[1] * sine)),
                         (float)((in.data[0] * sine)   + (in.data[1] * cosine)));
    }
    
    
    public double get(int index) {
        return data[index];
    }
    
    
    public double getX() {
        return data[0];
    }
    
    
    public double getY() {
        return data[1];
    }
    
    
    public void setX(double in) {
        data[0] = in;
    }
    
    
    public void setY(double in) {
        data[1] = in;
    }
    
    
    public void set(double dx, double dy) {
        data[0] = dx;
        data[1] = dy;
    }
    
    
    public void setLength(double in) {
        normalize();
        mul(in);
    }
    
    
    @Override
    public boolean equals(Object other) {
        if(!(other instanceof Vec2d)) {
            return false;
        } else {
            Vec2d v = (Vec2d)other;
            return (v.data[0] == data[0]) && (v.data[1] == data[1]);
        }
    }
    

    @Override
    public int hashCode() {
        return (215 + Arrays.hashCode(this.data)) * 17;
    }
    
    
    @Override
    public String toString() {
        return " (" + data[0] + ", " + data[1] + ") ";
    }
}
