package com.mona.util;

import com.mona.bean.Vector;

import java.util.ArrayList;

public class Simplex {

    public Vector[] simple = new Vector[3];
    public Vector[] pair = new Vector[2];
    public int count;
    public int last;
    Vector a = new Vector();
    Vector b = new Vector();
    Vector c = new Vector();
    Vector ao = new Vector();
    Vector bo = new Vector();
    Vector co = new Vector();
    Vector ab = new Vector();
    Vector ac = new Vector();
    Vector bc = new Vector();
    Vector abPerp = new Vector();
    Vector acPerp = new Vector();

    public static Vector[] getSimple(Shape A, Shape B) {
        Vector[] VectorS = new Vector[A.points.length * B.points.length];
        int count = 0;
        for (int i = 0; i < A.points.length; i++) {
            for (int j = 0; j < B.points.length; j++) {
                // if (count > 3) {
                // Vector Vector = new Vector(B.points[i].x - A.points[i].x,
                // B.points[i].y - A.points[i].y);
                // }
                VectorS[count] = new Vector(B.points[j].x - A.points[i].x, B.points[j].y - A.points[i].y);
                count++;
            }
        }
        return VectorS;
    }

    public static void main(String[] args) {
        Simplex simplex = new Simplex();
        Vector[] VectorA = new Vector[5];
        VectorA[0] = new Vector(5, 7);
        VectorA[1] = new Vector(7, 3);
        VectorA[2] = new Vector(10, 2);
        VectorA[3] = new Vector(12, 7);
        VectorA[4] = new Vector(10, 10);
        Shape shapeA = new Shape(VectorA);
        double x = 6;
        double y = -8;
        Vector[] VectorB = new Vector[3];
        VectorB[0] = new Vector(x + 4, y + 5);
        VectorB[1] = new Vector(x + 9, y + 9);
        VectorB[2] = new Vector(x + 4, y + 11);
        Shape shapeB = new Shape(VectorB);
        System.out.println(simplex.isCollision(shapeA, shapeB));
        System.out.println(simplex.isCollision3(shapeA, shapeB));
        ArrayList<Vector> lllll = CreatConvex.getConvexHull(getSimple(shapeA, shapeB));
        Vector[] v = new Vector[lllll.size()];
        for (int i = 0; i < lllll.size(); i++) {
            v[i] = lllll.get(i);
        }
        // contains
        System.out.println(new Shape(v).contains());
        Vector a = new Vector(1, 0);
        Vector b = new Vector(1, 1);
        Vector c = new Vector(1, 0);
        System.out.println(simplex.tripleProduct(a, b, c));
        System.out.println(simplex.tripleProduct2(a, b, c));
    }

    public void add(Vector point) {
        if (simple[0] == null) {
            simple[0] = point;
            last = 0;
            return;
        }
        if (simple[1] == null) {
            simple[1] = point;
            last = 1;
            return;
        }
        if (simple[2] == null) {
            simple[2] = point;
            last = 2;
            return;
        }
    }

    public void remove(Vector point) {
        if (simple[0] == point) {
            simple[0] = null;
            return;
        }
        if (simple[1] == point) {
            simple[1] = null;
            return;
        }
        if (simple[2] == point) {
            simple[2] = null;
            return;
        }
    }

    public Vector getLast() {
        return simple[last];
    }

    public Vector[] support(Shape A, Shape B, Vector direction) {
        double temp;
        int Amin = 0, Amax = 0;
        int Bmin = 0, Bmax = 0;
        double min, max;
        max = min = A.points[0].x * direction.x + A.points[0].y * direction.y;
        for (int i = 1; i < A.points.length; i++) {
            temp = A.points[i].x * direction.x + A.points[i].y * direction.y;
            if (temp < min) {
                min = temp;
                Amin = i;
            } else if (temp == min) {
                if (A.points[i].x < A.points[Amin].x) {
                    min = temp;
                    Amin = i;
                } else if (A.points[i].x == A.points[Amin].x) {
                    if (A.points[i].y <= A.points[Amin].y) {
                        min = temp;
                        Amin = i;
                    }
                }
            }
            if (temp > max) {
                max = temp;
                Amax = i;
            } else if (temp == max) {
                if (A.points[i].x > A.points[Amin].x) {
                    max = temp;
                    Amax = i;
                } else if (A.points[i].x == A.points[Amin].x) {
                    if (A.points[i].y >= A.points[Amin].y) {
                        max = temp;
                        Amax = i;
                    }
                }
            }
        }
        max = min = B.points[0].x * direction.x + B.points[0].y * direction.y;
        for (int i = 1; i < B.points.length; i++) {
            temp = B.points[i].x * direction.x + B.points[i].y * direction.y;
            if (temp < min) {
                min = temp;
                Bmin = i;
            } else if (temp == min) {
                if (B.points[i].x < B.points[Bmin].x) {
                    min = temp;
                    Bmin = i;
                } else if (B.points[i].x == B.points[Bmin].x) {
                    if (B.points[i].y <= B.points[Bmin].y) {
                        min = temp;
                        Bmin = i;
                    }
                }
            }
            if (temp > max) {
                max = temp;
                Bmax = i;
            } else if (temp == max) {
                if (B.points[i].x > B.points[Bmin].x) {
                    max = temp;
                    Bmax = i;
                } else if (B.points[i].x == B.points[Bmin].x) {
                    if (B.points[i].y >= B.points[Bmin].y) {
                        max = temp;
                        Bmax = i;
                    }
                }
            }
        }
        Vector AB = new Vector(B.points[Bmax].x - A.points[Amin].x, B.points[Bmax].y - A.points[Amin].x);
        Vector BA = new Vector(B.points[Bmin].x - A.points[Amax].x, B.points[Bmin].y - A.points[Amax].x);
        pair[0] = AB;
        pair[1] = BA;
        return pair;
    }

    /**
     * (aXb)Xc = -cX(aXb) = -a(cb)+b(ca)
     */
    public Vector tripleProduct(Vector a, Vector b, Vector c) {
        double cb = c.x * b.x + c.y * b.y;
        double ca = c.x * a.x + c.y * a.y;
        double x = b.x * ca - a.x * cb;
        double y = b.y * ca - a.y * cb;
        return new Vector(x, y);
    }

    /**
     * 计算a×b×c
     * a×(b×c) = b(a·c)−c(a·b)
     * (a×b)×c = -c×(a×b) = -a·(c·b)+b·(c·a)
     */
    public Vector tripleProduct2(Vector a, Vector b, Vector c) {
        return new Vector(a.y * b.x * c.y - a.x * b.y * c.y, a.x * b.y * c.x - a.y * b.x * c.x);
    }

    public boolean isCollision(Shape A, Shape B) {
        Vector direction = new Vector(0, 0);
        direction.x = B.center.x - A.center.x;
        direction.y = B.center.y - A.center.y;
        pair = support(A, B, direction);
        add(pair[0]);
        add(pair[1]);
        direction.negate();
        while (true) {
            // make sure that the last point we added actually passed the origin
            if (getLast().dot(direction) <= 0) {
                // if the point added last was not past the origin in the direction of d
                // then the Minkowski Sum cannot possibly contain the origin since
                // the last point added is on the edge of the Minkowski Difference
                return false;
            } else {
                // otherwise we need to determine if the origin is in
                // the current simplex
                if (containsOrigin(direction)) {
                    // if it does then we know there is a collision
                    return true;
                }
            }
            add(support(A, B, direction)[0]);
        }
    }

    public boolean isCollision3(Shape A, Shape B) {
        Vector direction = new Vector(B.center.x - A.center.x, B.center.y - A.center.y);
        pair = support(A, B, direction);
        simple[0] = new Vector(pair[0].x, pair[0].y);
        simple[1] = new Vector(pair[1].x, pair[1].y);
        double temp;
        while (true) {
            ao.x = -simple[0].x;
            ao.y = -simple[0].y;
            ab.x = simple[1].x - simple[0].x;
            ab.y = simple[1].y - simple[0].y;
            if (ao.x * ab.x + ao.y * ab.y < 0) {
                return false;
            }
            bo.x = -simple[1].x;
            bo.y = -simple[1].y;
            ab.negate();
            if (bo.x * ab.x + bo.y * ab.y < 0) {
                return false;
            }
            direction.x = -(simple[1].y - simple[0].y);
            direction.y = simple[1].x - simple[0].x;
            pair = support(A, B, direction);
            ab.negate();
            temp = ab.x * ao.y - ab.y * ao.x;
            if (temp > 0) {
                simple[2].x = pair[0].x;
                simple[2].y = pair[0].y;
            } else if (temp < 0) {
                simple[2].x = pair[1].x;
                simple[2].y = pair[1].y;
            } else {
                return true;
            }
            ac.x = simple[2].x - simple[0].x;
            ac.y = simple[2].y - simple[0].y;
            if (ac.x * ab.y - ac.y * ab.x == 0) {
                return false;
            }
            temp = ac.x * ao.y - ac.y * ao.x;
            if (temp > 0) {
                ac.negate();// get ca
                co.x = -simple[2].x;
                co.y = -simple[2].y;
                temp = co.x * ac.x + co.y * ac.y;
                if (temp < 0) {
                    return false;
                }
                simple[1].x = simple[2].x;
                simple[1].y = simple[2].y;
                continue;
            }
            bc.x = simple[2].x - simple[1].x;
            bc.y = simple[2].y - simple[1].y;
            temp = ac.x * ao.y - ac.y * ao.x;
            if (temp < 0) {
                bc.negate();// get cb
                co.x = -simple[2].x;
                co.y = -simple[2].y;
                temp = co.x * bc.x + co.y * bc.y;
                if (temp < 0) {
                    return false;
                }
                simple[0].x = simple[2].x;
                simple[0].y = simple[2].y;
                continue;
            }
            return true;
        }
    }

    public boolean isCollision2(Shape A, Shape B) {
        Vector direction = new Vector(0, 0);
        direction.x = B.center.x - A.center.x;
        direction.y = B.center.y - A.center.y;
        pair = support(A, B, direction);
        add(pair[0]);
        add(pair[1]);
        // negate d for the next point
        direction.negate();
        // start looping
        while (true) {
            // add a new point to the simplex because we haven't terminated yet
            add(support(A, B, direction)[0]);
            // make sure that the last point we added actually passed the origin
            if (getLast().dot(direction) <= 0) {
                // if the point added last was not past the origin in the direction of d
                // then the Minkowski Sum cannot possibly contain the origin since
                // the last point added is on the edge of the Minkowski Difference
                return false;
            } else {
                // otherwise we need to determine if the origin is in
                // the current simplex
                if (containsOrigin(direction)) {
                    // if it does then we know there is a collision
                    return true;
                }
            }
        }
    }

    public boolean containsOrigin(Vector direction) {
        // get the last point added to the simplex
        a = getLast();
        // compute AO (same thing as -A)
        ao.x = -a.x;
        ao.y = -a.y;
        if (count == 3) {
            // then its the triangle case get b and c
            b = simple[1];
            c = simple[2];
            // compute the edges
            ab.x = b.x - a.x;
            ab.y = b.y - a.y;
            ac.x = c.x - a.x;
            ac.y = c.y - a.y;
            // compute the normals
            abPerp = tripleProduct(ac, ab, ab);
            acPerp = tripleProduct(ab, ac, ac);
            // is the origin in R4
            if (abPerp.dot(ao) > 0) {
                // remove point c
                remove(c);
                // set the new direction to abPerp
                direction.set(abPerp);
            } else {
                // is the origin in R3
                if (acPerp.dot(ao) > 0) {
                    // remove point b
                    remove(b);
                    // set the new direction to acPerp
                    direction.set(acPerp);
                } else {
                    // otherwise we know its in R5 so we can return true
                    return true;
                }
            }
        } else {
            // then its the line segment case
            b = simple[1];
            // compute AB
            ab.x = b.x - a.x;
            ab.y = b.y - a.y;
            // get the perp to AB in the direction of the origin
            abPerp = tripleProduct(ab, ao, ab);
            // set the direction to abPerp
            direction.set(abPerp);
        }
        return false;
    }

    public static class Shape {
        Vector[] points;
        Vector center;

        public Shape() {
        }

        public Shape(Vector[] points) {
            this.points = points;
            center = new Vector();
            for (int i = 0; i < points.length; i++) {
                center.x += points[i].x;
                center.y += points[i].y;
            }
            center.x /= points.length;
            center.y /= points.length;
        }

        public Vector getCenter() {
            return center;
        }

        public boolean contains() {
            double[] p = new double[points.length * 2];
            for (int i = 0; i < points.length; i += 2) {
                p[i] = points[i].x;
                p[i + 1] = points[i].y;
            }
            return ShapeUtil.contains(0, 0, p);
        }
    }

}
