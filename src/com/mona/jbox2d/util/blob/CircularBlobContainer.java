package com.mona.jbox2d.util.blob;

import com.mona.jbox2d.collision.AABB;
import com.mona.jbox2d.common.Vec2;

/**
 * A circular blob container specified by radius and center.
 */
public class CircularBlobContainer implements BlobContainer {
    private float centerX, centerY;
    private float radius;
    private float radiusSqr;

    public CircularBlobContainer(com.mona.jbox2d.common.Vec2 _center, float _radius) {
        centerX = _center.x;
        centerY = _center.y;
        radius = _radius;
        radiusSqr = _radius * _radius;
    }

    public float getRadius() {
        return radius;
    }

    public void setRadius(float r) {
        radius = r;
        radiusSqr = r * r;
    }

    public com.mona.jbox2d.common.Vec2 getCenter() {
        return new com.mona.jbox2d.common.Vec2(centerX, centerY);
    }

    public void setCenter(com.mona.jbox2d.common.Vec2 c) {
        centerX = c.x;
        centerY = c.y;
    }

    public boolean containsPoint(com.mona.jbox2d.common.Vec2 p) {
        float distSqr = (p.x - centerX) * (p.x - centerX) + (p.y - centerY) * (p.y - centerY);
        return !(distSqr > radiusSqr);
    }

    public AABB getAABB() {
        com.mona.jbox2d.common.Vec2 min = new com.mona.jbox2d.common.Vec2(centerX - radius * 1.2f, centerY - radius * 1.2f);
        com.mona.jbox2d.common.Vec2 max = new Vec2(centerX + radius * 1.2f, centerY + radius * 1.2f);
        return new AABB(min, max);
    }

}
