/*
 * JBox2D - A Java Port of Erin Catto's Box2D
 *
 * JBox2D homepage: http://jbox2d.sourceforge.net/
 * Box2D homepage: http://www.box2d.org
 *
 * This software is provided 'as-is', without any express or implied
 * warranty.  In no event will the authors be held liable for any damages
 * arising from the use of this software.
 *
 * Permission is granted to anyone to use this software for any purpose,
 * including commercial applications, and to alter it and redistribute it
 * freely, subject to the following restrictions:
 *
 * 1. The origin of this software must not be misrepresented; you must not
 * claim that you wrote the original software. If you use this software
 * in a product, an acknowledgment in the product documentation would be
 * appreciated but is not required.
 * 2. Altered source versions must be plainly marked as such, and must not be
 * misrepresented as being the original software.
 * 3. This notice may not be removed or altered from any source distribution.
 */

package com.jbox2d.collision;

/*
 * Copyright (c) 2007 Erin Catto http://www.gphysics.com
 *
 * This software is provided 'as-is', without any express or implied
 * warranty.  In no event will the authors be held liable for any damages
 * arising from the use of this software.
 * Permission is granted to anyone to use this software for any purpose,
 * including commercial applications, and to alter it and redistribute it
 * freely, subject to the following restrictions:
 * 1. The origin of this software must not be misrepresented; you must not
 * claim that you wrote the original software. If you use this software
 * in a product, an acknowledgment in the product documentation would be
 * appreciated but is not required.
 * 2. Altered source versions must be plainly marked as such, and must not be
 * misrepresented as being the original software.
 * 3. This notice may not be removed or altered from any source distribution.
 */

import com.jbox2d.common.XForm;

//updated to rev 108->139 of b2Distance.cpp

/**
 * Implements the GJK algorithm for computing distance between shapes.
 */
public class Distance {
    public static int g_GJK_Iterations = 0;

    // GJK using Voronoi regions (Christer Ericson) and region selection
    // optimizations (Casey Muratori).

    // The origin is either in the region of points[1] or in the edge region. The origin is
    // not in region of points[0] because that is the old point.
    protected static int ProcessTwo(com.jbox2d.common.Vec2 x1, com.jbox2d.common.Vec2 x2, com.jbox2d.common.Vec2[] p1s, com.jbox2d.common.Vec2[] p2s, com.jbox2d.common.Vec2[] points) {
        // If in point[1] region
        com.jbox2d.common.Vec2 r = new com.jbox2d.common.Vec2(-points[1].x, -points[1].y);
        com.jbox2d.common.Vec2 d = new com.jbox2d.common.Vec2(points[0].x - points[1].x, points[0].y - points[1].y);
        float length = d.normalize();
        float lambda = com.jbox2d.common.Vec2.dot(r, d);
        if (lambda <= 0.0f || length < com.jbox2d.common.Settings.EPSILON) {
            // The simplex is reduced to a point.
            x1.set(p1s[1]);
            x2.set(p2s[1]);
            p1s[0].set(p1s[1]);
            p2s[0].set(p2s[1]);
            points[0].set(points[1]);
            return 1;
        }

        // Else in edge region
        lambda /= length;
        x1.set(p1s[1].x + lambda * (p1s[0].x - p1s[1].x),
                p1s[1].y + lambda * (p1s[0].y - p1s[1].y));
        x2.set(p2s[1].x + lambda * (p2s[0].x - p2s[1].x),
                p2s[1].y + lambda * (p2s[0].y - p2s[1].y));
        return 2;
    }

    // Possible regions:
    // - points[2]
    // - edge points[0]-points[2]
    // - edge points[1]-points[2]
    // - inside the triangle
    protected static int ProcessThree(com.jbox2d.common.Vec2 x1, com.jbox2d.common.Vec2 x2, com.jbox2d.common.Vec2[] p1s, com.jbox2d.common.Vec2[] p2s, com.jbox2d.common.Vec2[] points) {
        com.jbox2d.common.Vec2 a = points[0];
        com.jbox2d.common.Vec2 b = points[1];
        com.jbox2d.common.Vec2 c = points[2];

        com.jbox2d.common.Vec2 ab = b.sub(a);
        com.jbox2d.common.Vec2 ac = c.sub(a);
        com.jbox2d.common.Vec2 bc = c.sub(b);

        float sn = -com.jbox2d.common.Vec2.dot(a, ab), sd = com.jbox2d.common.Vec2.dot(b, ab);
        float tn = -com.jbox2d.common.Vec2.dot(a, ac), td = com.jbox2d.common.Vec2.dot(c, ac);
        float un = -com.jbox2d.common.Vec2.dot(b, bc), ud = com.jbox2d.common.Vec2.dot(c, bc);

        // In vertex c region?
        if (td <= 0.0f && ud <= 0.0f) {
            // Single point
            x1.set(p1s[2]);
            x2.set(p2s[2]);
            p1s[0].set(p1s[2]);
            p2s[0].set(p2s[2]);
            points[0].set(points[2]);
            return 1;
        }

        // Should not be in vertex a or b region.
        //B2_NOT_USED(sd);
        //B2_NOT_USED(sn);
        assert (sn > 0.0f || tn > 0.0f);
        assert (sd > 0.0f || un > 0.0f);

        float n = com.jbox2d.common.Vec2.cross(ab, ac);

        // Should not be in edge ab region.
        float vc = n * com.jbox2d.common.Vec2.cross(a, b);
        assert (vc > 0.0f || sn > 0.0f || sd > 0.0f);

        // In edge bc region?
        float va = n * com.jbox2d.common.Vec2.cross(b, c);
        if (va <= 0.0f && un >= 0.0f && ud >= 0.0f && (un + ud) > 0.0f) {
            assert (un + ud > 0.0f);
            float lambda = un / (un + ud);
            x1.set(p1s[1].x + lambda * (p1s[2].x - p1s[1].x),
                    p1s[1].y + lambda * (p1s[2].y - p1s[1].y));
            x2.set(p2s[1].x + lambda * (p2s[2].x - p2s[1].x),
                    p2s[1].y + lambda * (p2s[2].y - p2s[1].y));
            p1s[0].set(p1s[2]);
            p2s[0].set(p2s[2]);
            points[0].set(points[2]);
            return 2;
        }

        // In edge ac region?
        float vb = n * com.jbox2d.common.Vec2.cross(c, a);
        if (vb <= 0.0f && tn >= 0.0f && td >= 0.0f && (tn + td) > 0.0f) {
            assert (tn + td > 0.0f);
            float lambda = tn / (tn + td);
            x1.set(p1s[0].x + lambda * (p1s[2].x - p1s[0].x),
                    p1s[0].y + lambda * (p1s[2].y - p1s[0].y));
            x2.set(p2s[0].x + lambda * (p2s[2].x - p2s[0].x),
                    p2s[0].y + lambda * (p2s[2].y - p2s[0].y));
            p1s[1].set(p1s[2]);
            p2s[1].set(p2s[2]);
            points[1].set(points[2]);
            return 2;
        }

        // Inside the triangle, compute barycentric coordinates
        float denom = va + vb + vc;
        assert (denom > 0.0f);
        denom = 1.0f / denom;
        float u = va * denom;
        float v = vb * denom;
        float w = 1.0f - u - v;
        x1.set(u * p1s[0].x + v * p1s[1].x + w * p1s[2].x,
                u * p1s[0].y + v * p1s[1].y + w * p1s[2].y);
        x2.set(u * p2s[0].x + v * p2s[1].x + w * p2s[2].x,
                u * p2s[0].y + v * p2s[1].y + w * p2s[2].y);
        return 3;
    }

    protected static boolean InPoints(com.jbox2d.common.Vec2 w, com.jbox2d.common.Vec2[] points, int pointCount) {
        float k_tolerance = 100.0f * com.jbox2d.common.Settings.EPSILON;
        for (int i = 0; i < pointCount; ++i) {
            com.jbox2d.common.Vec2 d = com.jbox2d.common.Vec2.abs(w.sub(points[i]));
//				new Vec2( Math.abs(w.x-points[i].x), Math.abs(w.y-points[i].y));//Vec2.abs(w - points[i]);
            com.jbox2d.common.Vec2 m = com.jbox2d.common.Vec2.max(com.jbox2d.common.Vec2.abs(w), com.jbox2d.common.Vec2.abs(points[i]));

            if (d.x < k_tolerance * (m.x + 1.0f) &&
                    d.y < k_tolerance * (m.y + 1.0f)) {
                return true;
            }
        }

        return false;
    }


    /**
     * Distance between any two objects that implement SupportsGenericDistance.
     * Note that x1 and x2 are passed so that they may store results - they must
     * be instantiated before being passed, and the contents will be lost.
     *
     * @param x1     Set to closest point on shape1 (result parameter)
     * @param x2     Set to closest point on shape2 (result parameter)
     * @param shape1 Shape to test
     * @param xf1    Transform of shape1
     * @param shape2 Shape to test
     * @param xf2    Transform of shape2
     * @return
     */
    public static float DistanceGeneric(com.jbox2d.common.Vec2 x1, com.jbox2d.common.Vec2 x2,
                                        SupportsGenericDistance shape1, XForm xf1,
                                        SupportsGenericDistance shape2, XForm xf2) {
        com.jbox2d.common.Vec2[] p1s = new com.jbox2d.common.Vec2[3];
        com.jbox2d.common.Vec2[] p2s = new com.jbox2d.common.Vec2[3];
        com.jbox2d.common.Vec2[] points = new com.jbox2d.common.Vec2[3];

        for (int i = 0; i < 3; ++i) {
            p1s[i] = new com.jbox2d.common.Vec2();
            p2s[i] = new com.jbox2d.common.Vec2();
            points[i] = new com.jbox2d.common.Vec2();
        }

        int pointCount = 0;

        x1.set(shape1.getFirstVertex(xf1));
        x2.set(shape2.getFirstVertex(xf2));

        float vSqr = 0.0f;
        int maxIterations = 20;
        for (int iter = 0; iter < maxIterations; ++iter) {
            com.jbox2d.common.Vec2 v = x2.sub(x1);
            com.jbox2d.common.Vec2 w1 = shape1.support(xf1, v);
            com.jbox2d.common.Vec2 w2 = shape2.support(xf2, v.negate());

            vSqr = com.jbox2d.common.Vec2.dot(v, v);
            com.jbox2d.common.Vec2 w = w2.sub(w1);
            float vw = com.jbox2d.common.Vec2.dot(v, w);
            if (vSqr - vw <= 0.01f * vSqr || InPoints(w, points, pointCount)) // or w in points
            {
                if (pointCount == 0) {
                    x1.set(w1);
                    x2.set(w2);
                }
                g_GJK_Iterations = iter;
                return (float) Math.sqrt(vSqr);
            }

            switch (pointCount) {
                case 0:
                    p1s[0].set(w1);
                    p2s[0].set(w2);
                    points[0].set(w);
                    x1.set(p1s[0]);
                    x2.set(p2s[0]);
                    ++pointCount;
                    break;

                case 1:
                    p1s[1].set(w1);
                    p2s[1].set(w2);
                    points[1].set(w);
                    pointCount = ProcessTwo(x1, x2, p1s, p2s, points);
                    break;

                case 2:
                    p1s[2].set(w1);
                    p2s[2].set(w2);
                    points[2].set(w);
                    pointCount = ProcessThree(x1, x2, p1s, p2s, points);
                    break;
            }

            // If we have three points, then the origin is in the corresponding triangle.
            if (pointCount == 3) {
                g_GJK_Iterations = iter;
                return 0.0f;
                //
            }

            float maxSqr = -Float.MAX_VALUE;// -FLT_MAX;
            for (int i = 0; i < pointCount; ++i) {
                maxSqr = Math.max(maxSqr, com.jbox2d.common.Vec2.dot(points[i], points[i]));
            }

            if (pointCount == 3 || vSqr <= 100.0f * com.jbox2d.common.Settings.EPSILON * maxSqr) {
                g_GJK_Iterations = iter;
                v.set(x2.x - x1.x, x2.y - x1.y);// x2 - x1
                vSqr = com.jbox2d.common.Vec2.dot(v, v);

                return (float) Math.sqrt(vSqr);
                //
            }
        }

        g_GJK_Iterations = maxIterations;
        return (float) Math.sqrt(vSqr);
        //
    }

    protected static float DistanceCC(
            com.jbox2d.common.Vec2 x1, com.jbox2d.common.Vec2 x2,
            CircleShape circle1, XForm xf1,
            CircleShape circle2, XForm xf2) {

        com.jbox2d.common.Vec2 p1 = XForm.mul(xf1, circle1.getLocalPosition());
        com.jbox2d.common.Vec2 p2 = XForm.mul(xf2, circle2.getLocalPosition());

        com.jbox2d.common.Vec2 d = new com.jbox2d.common.Vec2(p2.x - p1.x, p2.y - p1.y);
        float dSqr = com.jbox2d.common.Vec2.dot(d, d);
        float r1 = circle1.getRadius() - com.jbox2d.common.Settings.toiSlop;
        float r2 = circle2.getRadius() - com.jbox2d.common.Settings.toiSlop;
        float r = r1 + r2;
        if (dSqr > r * r) {
            float dLen = d.normalize();
            float distance = dLen - r;
            x1.set(p1.x + r1 * d.x,
                    p1.y + r1 * d.y);
            x2.set(p2.x - r2 * d.x,
                    p2.y - r2 * d.y);
            return distance;
        } else if (dSqr > com.jbox2d.common.Settings.EPSILON * com.jbox2d.common.Settings.EPSILON) {
            d.normalize();
            x1.set(p1.x + r1 * d.x,
                    p1.y + r1 * d.y);
            x2.set(x1);
            return 0.0f;
        }

        x1.set(p1);
        x2.set(x1);
        return 0.0f;
    }


    // GJK is more robust with polygon-vs-point than polygon-vs-circle.
    // So we convert polygon-vs-circle to polygon-vs-point.
    protected static float DistancePC(
            com.jbox2d.common.Vec2 x1, com.jbox2d.common.Vec2 x2,
            PolygonShape polygon, XForm xf1,
            CircleShape circle, XForm xf2) {
        Point point = new Point(new com.jbox2d.common.Vec2(0.0f, 0.0f));
        point.p = XForm.mul(xf2, circle.getLocalPosition());

        float distance = DistanceGeneric(x1, x2, polygon, xf1, point, XForm.identity);

        float r = circle.getRadius() - com.jbox2d.common.Settings.toiSlop;

        if (distance > r) {
            distance -= r;
            com.jbox2d.common.Vec2 d = x2.sub(x1);
            d.normalize();
            x2.x -= r * d.x;
            x2.y -= r * d.y;
        } else {
            distance = 0.0f;
            x2.set(x1);
        }

        return distance;
    }

    /**
     * Find the closest distance between shapes shape1 and shape2,
     * and load the closest points into x1 and x2.
     * Note that x1 and x2 are passed so that they may store results - they must
     * be instantiated before being passed, and the contents will be lost.
     *
     * @param x1     Closest point on shape1 is put here (result parameter)
     * @param x2     Closest point on shape2 is put here (result parameter)
     * @param shape1 First shape to test
     * @param xf1    Transform of first shape
     * @param shape2 Second shape to test
     * @param xf2    Transform of second shape
     */
    public static float distance(com.jbox2d.common.Vec2 x1, com.jbox2d.common.Vec2 x2,
                                 Shape shape1, XForm xf1,
                                 Shape shape2, XForm xf2) {

        ShapeType type1 = shape1.getType();
        ShapeType type2 = shape2.getType();

        if (type1 == ShapeType.CIRCLE_SHAPE && type2 == ShapeType.CIRCLE_SHAPE) {
            return DistanceCC(x1, x2, (CircleShape) shape1, xf1, (CircleShape) shape2, xf2);
        }

        if (type1 == ShapeType.POLYGON_SHAPE && type2 == ShapeType.CIRCLE_SHAPE) {
            return DistancePC(x1, x2, (PolygonShape) shape1, xf1, (CircleShape) shape2, xf2);
        }

        if (type1 == ShapeType.CIRCLE_SHAPE && type2 == ShapeType.POLYGON_SHAPE) {
            return DistancePC(x2, x1, (PolygonShape) shape2, xf2, (CircleShape) shape1, xf1);
        }

        if (type1 == ShapeType.POLYGON_SHAPE && type2 == ShapeType.POLYGON_SHAPE) {
            return DistanceGeneric(x1, x2, (PolygonShape) shape1, xf1, (PolygonShape) shape2, xf2);
        }

        return 0.0f;
    }

}

// This is used for polygon-vs-circle distance.
class Point implements SupportsGenericDistance {
    public com.jbox2d.common.Vec2 p;

    public Point(com.jbox2d.common.Vec2 _p) {
        p = _p.clone();
    }

    public com.jbox2d.common.Vec2 support(XForm xf, com.jbox2d.common.Vec2 v) {
        return p;
    }

    public com.jbox2d.common.Vec2 getFirstVertex(XForm xf) {
        return p;
    }

}