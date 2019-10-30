package com.mona.jbox2d.dynamics.contacts;

import com.mona.jbox2d.collision.ContactID;
import com.mona.jbox2d.collision.Shape;
import com.mona.jbox2d.common.Vec2;

// Updated to rev 139 of b2Contact.h

/**
 * This structure is used to report contact point results.
 */
public class ContactResult {
    /**
     * The first shape
     */
    public com.mona.jbox2d.collision.Shape shape1;
    /**
     * The second shape
     */
    public Shape shape2;
    /**
     * Position in world coordinates
     */
    public Vec2 position;
    /**
     * Points from shape1 to shape2
     */
    public Vec2 normal;
    /**
     * The normal impulse applied to body2
     */
    public float normalImpulse;
    /**
     * The tangent impulse applied to body2
     */
    public float tangentImpulse;
    /**
     * The contact id identifies the features in contact
     */
    public com.mona.jbox2d.collision.ContactID id;

    public ContactResult() {
        position = new Vec2();
        normal = new Vec2();
        id = new ContactID();
    }
}
