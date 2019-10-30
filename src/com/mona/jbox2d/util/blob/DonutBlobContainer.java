package com.mona.jbox2d.util.blob;

import com.mona.jbox2d.collision.AABB;
import com.mona.jbox2d.common.Vec2;

/**
 * A donut blob container specified by two radii and a center.
 */
public class DonutBlobContainer implements BlobContainer {
    private float centerX, centerY;
    private float radiusSmall, radiusLarge;
    private float radiusSmallSqr, radiusLargeSqr;

    public DonutBlobContainer(com.mona.jbox2d.common.Vec2 _center, float _radiusSmall, float _radiusLarge) {
        centerX = _center.x;
        centerY = _center.y;
        radiusSmall = _radiusSmall;
        radiusLarge = _radiusLarge;
        radiusSmallSqr = _radiusSmall * _radiusSmall;
        radiusLargeSqr = _radiusLarge * _radiusLarge;
    }


    public boolean containsPoint(com.mona.jbox2d.common.Vec2 p) {
        float distSqr = (p.x - centerX) * (p.x - centerX) + (p.y - centerY) * (p.y - centerY);
        if (distSqr > radiusLargeSqr) return false;
        return !(distSqr < radiusSmallSqr);
    }

    public AABB getAABB() {
        com.mona.jbox2d.common.Vec2 min = new com.mona.jbox2d.common.Vec2(centerX - 1.2f * radiusLarge, centerY - 1.2f * radiusLarge);
        com.mona.jbox2d.common.Vec2 max = new Vec2(centerX + 1.2f * radiusLarge, centerY + 1.2f * radiusLarge);
        return new AABB(min, max);
    }

}