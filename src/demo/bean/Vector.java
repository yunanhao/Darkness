package demo.bean;

/**
 * 2维空间的向量
 */
public final class Vector implements Cloneable {
    /**
     * 向量的实例
     */
    public static final Vector VECTOR = new Vector();
    /**
     * 最小正数
     */
    public static final double E = compute();
    /**
     * 弧度转换成角度时的乘数
     */
    public static final double Degrees = 180.0 / Math.PI;
    /**
     * 角度转换成弧度时的乘数
     */
    public static final double Radians = Math.PI / 180.0;
    /**
     * 方向为左的二进制状态码
     */
    public static final int LEFT = 0B1000;
    /**
     * 方向为右的二进制状态码
     */
    public static final int RIGHT = 0B0010;
    /**
     * 方向为上的二进制状态码
     */
    public static final int UP = 0B0100;
    /**
     * 方向为下的二进制状态码
     */
    public static final int DOWN = 0B0001;
    /**
     * 方向为右所对应的弧度
     */
    public static final double DIR0 = 0;
    /**
     * 方向为右上所对应的弧度
     */
    public static final double DIR45 = Math.PI / 4;
    /**
     * 方向为正上所对应的弧度
     */
    public static final double DIR90 = DIR45 * 2;
    /**
     * 方向为左上所对应的弧度
     */
    public static final double DIR135 = DIR45 * 3;
    /**
     * 方向为正左所对应的弧度
     */
    public static final double DIR180 = Math.PI;
    /**
     * 方向为左下所对应的弧度
     */
    public static final double DIR225 = DIR45 * 5;
    /**
     * 方向为正下所对应的弧度
     */
    public static final double DIR270 = DIR45 * 6;
    /**
     * 方向为右下所对应的弧度
     */
    public static final double DIR315 = DIR45 * 7;
    /**
     * 斜向运动时速率应乘以的倍数
     */
    public static final double Oblique = 0.70710678118654752440084436210485;
    /**
     * 一度所对应的弧度
     */
    public static final double Degre_One = 0.01745329251994329576923690768489;
    /**
     * 表示x轴的向量；在运行时不应该改变这个向量；在内部使用
     */
    public static final Vector X_AXIS = new Vector(1.0, 0.0);
    /**
     * 表示y轴的向量；在运行时不应该改变这个向量；在内部使用
     */
    public static final Vector Y_AXIS = new Vector(0.0, 1.0);
    /**
     * 表示最大的向量；在运行时不应该改变这个向量；在内部使用
     */
    public static final Vector MAX = new Vector(Double.MAX_VALUE, Double.MAX_VALUE);
    /**
     * 向量的坐标
     */
    public double x, y;
    /**
     * 三角函数值
     */
    public double sin, cos;

    /**
     * 默认的无参构造函数
     */
    public Vector() {
    }

    /**
     * 在给定的方向(弧度)上创建单位长度向量.
     */
    public Vector(double direction) {
        x = getCos(direction);
        y = getSin(direction);
        sin = y;
        cos = x;
    }

    public Vector(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * 得到向量ab
     */
    public Vector(double ax, double ay, double bx, double by) {
        x = bx - ax;
        y = by - ay;
    }

    public Vector(Vector vector) {
        x = vector.x;
        y = vector.y;
    }

    /**
     * 得到向量ab
     */
    public Vector(Vector a, Vector b) {
        x = b.x - a.x;
        y = b.y - a.y;
    }

    /**
     * 计算ε的近似.
     *
     * @return double
     */
    public static final double compute() {
        double e = 0.5;
        while (1.0 + e > 1.0) {
            e *= 0.5;
        }
        return e;
    }

    // Static
    public final static Vector abs(Vector a) {
        return new Vector(a.x <= 0.0D ? 0.0D - a.x : a.x, a.y <= 0.0D ? 0.0D - a.y : a.y);
    }

    public final static Vector max(Vector a, Vector b) {
        return new Vector(a.x > b.x ? a.x : b.x, a.y > b.y ? a.y : b.y);
    }

    public final static Vector min(Vector a, Vector b) {
        return new Vector(a.x < b.x ? a.x : b.x, a.y < b.y ? a.y : b.y);
    }

    public final static Vector sum(Vector v, double x, double y) {
        return new Vector(v.x + x, v.y + y);
    }

    public final static Vector sum(Vector v, Vector vector) {
        return new Vector(v.x + vector.x, v.y + vector.y);
    }

    public final static Vector difference(Vector v, double x, double y) {
        return new Vector(v.x - x, v.y - y);
    }

    public final static Vector difference(Vector v, Vector vector) {
        return new Vector(v.x - vector.x, v.y - vector.y);
    }

    public final static Vector to(Vector v, double x, double y) {
        return new Vector(x - v.x, y - v.y);
    }

    public final static Vector to(Vector v, Vector vector) {
        return new Vector(vector.x - v.x, vector.y - v.y);
    }

    public final static Vector product(Vector v, double scalar) {
        return new Vector(v.x * scalar, v.y * scalar);
    }

    /**
     * 两个向量之间的线性插值
     */
    public final static Vector interpolate(Vector from, Vector to, double alpha) {
        if (alpha <= 0) {
            return from;
        }
        if (alpha >= 1) {
            return to;
        }
        return from.multiply(1 - alpha).add(to.multiply(alpha));
    }

    /**
     * Returns the cross product of this {@link Vector} and the z value of the
     * right {@link Vector}.
     *
     * @param z the z component of the {@link Vector}
     * @return {@link Vector}
     */
    public final static Vector cross(Vector v, double z) {
        return new Vector(-1.0 * v.y * z, v.x * z);
    }

    /**
     * 得到left-handed法向量
     */
    public final static Vector getLeftHandOrthogonalVector(Vector v) {
        return new Vector(v.y, -v.x);
    }

    /**
     * 得到right-handed法向量
     */
    public final static Vector getRightHandOrthogonalVector(Vector v) {
        return new Vector(-v.y, v.x);
    }

    /**
     * Returns a {@link Vector} which is the negative of this {@link Vector}.
     *
     * @return {@link Vector}
     */
    public final static Vector getNegative(Vector v) {
        return new Vector(-v.x, -v.y);
    }

    /**
     * Returns a unit {@link Vector} of this {@link Vector}.
     * <p>
     * This method requires the length of this {@link Vector} is not zero.
     *
     * @return {@link Vector}
     */
    public final static Vector getNormalized(Vector v) {
        double magnitude = v.magnitude();
        if (magnitude <= E) {
            return new Vector();
        }
        magnitude = 1.0 / magnitude;
        return new Vector(v.x * magnitude, v.y * magnitude);
    }

    /**
     * The triple product of {@link Vector}s is defined as:
     *
     * <pre>
     * a x (b x c)
     * </pre>
     * <p>
     * However, this method performs the following triple product:
     *
     * <pre>
     * (a x b) x c
     * </pre>
     * <p>
     * this can be simplified to:
     *
     * <pre>
     * -a * (b &middot; c) + b * (a &middot; c)
     * </pre>
     * <p>
     * or:
     *
     * <pre>
     * b * (a &middot; c) - a * (b &middot; c)
     * </pre>
     *
     * @param a the a {@link Vector} in the above equation
     * @param b the b {@link Vector} in the above equation
     * @param c the c {@link Vector} in the above equation
     * @return {@link Vector}
     */
    public final static Vector tripleProduct(Vector a, Vector b, Vector c) {
        // expanded version of above formula
        Vector r = new Vector();
        // perform a.dot(c)
        double ac = a.x * c.x + a.y * c.y;
        // perform b.dot(c)
        double bc = b.x * c.x + b.y * c.y;
        // perform b * a.dot(c) - a * b.dot(c)
        r.x = b.x * ac - a.x * bc;
        r.y = b.y * ac - a.y * bc;
        return r;
    }

    /**
     * 根据传入的弧度返回正弦值
     */
    public static final double getSin(double direction) {
        if (direction != direction) {
            return 0;
        }
        while (direction > Math.PI) {
            direction -= Math.PI * 2;
        }
        while (direction < -Math.PI) {
            direction += Math.PI * 2;
        }
        if (direction == 0) {
            return 0;// 0度的正弦
        } else if (direction == Math.PI / 6) {
            return 0.5;// 30度的正弦
        } else if (direction == Math.PI / 4) {
            return 0.70710678118654752440084436210485;// 45度的正弦
        } else if (direction == Math.PI / 3) {
            return 0.86602540378443864676372317075294;// 60度的正弦
        } else if (direction == Math.PI / 2) {
            return 1;// 90度的正弦
        } else if (direction == Math.PI * 2 / 3) {
            return 0.86602540378443864676372317075294;// 120度的正弦
        } else if (direction == Math.PI * 3 / 4) {
            return 0.70710678118654752440084436210485;// 135度的正弦
        } else if (direction == Math.PI * 5 / 6) {
            return 0.5;// 150度的正弦
        } else if (direction == Math.PI || direction == -Math.PI) {
            return 0;// 正负180度的正弦
        } else if (direction == -Math.PI * 5 / 6) {
            return -0.5;// -150度的正弦
        } else if (direction == -Math.PI * 3 / 4) {
            return -0.70710678118654752440084436210485;// -135度的正弦
        } else if (direction == -Math.PI * 2 / 3) {
            return -0.86602540378443864676372317075294;// -120度的正弦
        } else if (direction == -Math.PI / 2) {
            return -1;// -90度的正弦
        } else if (direction == -Math.PI / 3) {
            return -0.86602540378443864676372317075294;// -60度的正弦
        } else if (direction == -Math.PI / 4) {
            return -0.70710678118654752440084436210485;// -45度的正弦
        } else if (direction == -Math.PI / 6) {
            return -0.5;// -30度的正弦
        } else {
            return Math.sin(direction);
        }
    }

    /**
     * 根据传入的弧度返回余弦值
     */
    public static final double getCos(double direction) {
        if (direction != direction) {
            return 0;
        }
        while (direction > Math.PI) {
            direction -= Math.PI * 2;
        }
        while (direction < -Math.PI) {
            direction += Math.PI * 2;
        }
        if (direction == 0) {
            return 1;// 0度的余弦
        } else if (direction == Math.PI / 6) {
            return 0.86602540378443864676372317075294;// 30度的余弦
        } else if (direction == Math.PI / 4) {
            return 0.70710678118654752440084436210485;// 45度的余弦
        } else if (direction == Math.PI / 3) {
            return 0.5;// 60度的余弦
        } else if (direction == Math.PI / 2) {
            return 0;// 90度的余弦
        } else if (direction == Math.PI * 2 / 3) {
            return -0.5;// 120度的余弦
        } else if (direction == Math.PI * 3 / 4) {
            return -0.70710678118654752440084436210485;// 135度的余弦
        } else if (direction == Math.PI * 5 / 6) {
            return -0.86602540378443864676372317075294;// 150度的余弦
        } else if (direction == Math.PI || direction == -Math.PI) {
            return -1;// 正负180度的余弦
        } else if (direction == -Math.PI * 5 / 6) {
            return -0.86602540378443864676372317075294;// -150度的余弦
        } else if (direction == -Math.PI * 3 / 4) {
            return -0.70710678118654752440084436210485;// -135度的余弦
        } else if (direction == -Math.PI * 2 / 3) {
            return -0.5;// -120度的余弦
        } else if (direction == -Math.PI / 2) {
            return 0;// -90度的余弦
        } else if (direction == -Math.PI / 3) {
            return 0.5;// -60度的余弦
        } else if (direction == -Math.PI / 4) {
            return 0.70710678118654752440084436210485;// -45度的余弦
        } else if (direction == -Math.PI / 6) {
            return 0.86602540378443864676372317075294;// -30度的余弦
        } else {
            return Math.cos(direction);
        }
    }

    /**
     * 得到法向量
     */
    public final static Vector normalVector(Vector v, boolean isLeft) {
        if (isLeft) {
            return new Vector(v.y, -v.x);
        }
        return new Vector(-v.y, v.x);
    }

    /**
     * 加
     */
    public final Vector add(double x, double y) {
        this.x += x;
        this.y += y;
        return this;
    }

    /**
     * 加
     */
    public final Vector add(Vector vector) {
        x += vector.x;
        y += vector.y;
        return this;
    }

    /**
     * 减
     */
    public final Vector subtract(double x, double y) {
        this.x -= x;
        this.y -= y;
        return this;
    }

    /**
     * 减
     */
    public final Vector subtract(Vector vector) {
        x -= vector.x;
        y -= vector.y;
        return this;
    }

    /**
     * 乘
     */
    public final Vector multiply(double scalar) {
        x *= scalar;
        y *= scalar;
        return this;
    }

    /**
     * 除
     */
    public final Vector divide(double scalar) {
        x /= scalar;
        y /= scalar;
        return this;
    }

    /**
     * 两个向量之间的线性插值
     */
    public final Vector interpolate(Vector v, double alpha) {
        if (alpha <= 0) {
            return this;
        }
        if (alpha >= 1) {
            return v;
        }
        return multiply(1 - alpha).add(v.multiply(alpha));
    }

    /**
     * 点乘(内积、数量积、点积)(互相垂直的两个向量是正交的，正交向量的内积为零)
     */
    public final double dot(double x, double y) {
        return this.x * x + this.y * y;
    }

    /**
     * 点乘(内积、数量积、点积)(互相垂直的两个向量是正交的，正交向量的内积为零)
     */
    public final double dot(Vector vector) {
        return x * vector.x + y * vector.y;
    }

    /**
     * 叉乘(矢积、叉积)(为0则平行)
     */
    public final double cross(double x, double y) {
        return this.x * y - this.y * x;
    }

    /**
     * 叉乘(矢积、叉积)(为0则平行)
     */
    public final double cross(Vector vector) {
        return x * vector.y - y * vector.x;
    }

    /**
     * 将向量置反
     */
    public final Vector negate() {
        x = -x;
        y = -y;
        return this;
    }

    /**
     * 绕点(rx,ry)顺时针(坐标系为Y轴向上为正)旋转指定弧度
     */
    public final Vector roate(double radian, double rx, double ry) {
        double cos, sin, temp;
        cos = getCos(radian);
        sin = getSin(radian);
        temp = (x - rx) * cos - (y - ry) * sin + rx;
        y = (x - rx) * sin + (y - ry) * cos + ry;
        x = temp;
        return this;
    }

    /**
     * 将向量绕原点顺时针(坐标系为Y轴向上为正)旋转指定弧度
     */
    public final Vector rotate(double radian) {
        double cos, sin, temp;
        cos = getCos(radian);
        sin = getSin(radian);
        temp = x * cos - y * sin;
        y = x * sin + y * cos;
        x = temp;
        return this;
    }

    /**
     * 归一化向量(变成长度为1的单位向量)
     */
    public final double normalize() {
        double length = Math.sqrt(x * x + y * y);
        if (length <= E) {
            return 0;
        }
        double invLength = 1.0 / length;
        x *= invLength;
        y *= invLength;
        // return 1.0 / m;
        return length;
    }

    /**
     * 向量(模、长度、大小)
     */
    public final double length() {
        return Math.sqrt(x * x + y * y);
    }

    /**
     * 向量(模、长度、大小)的平方
     */
    public final double lengthSquared() {
        return x * x + y * y;
    }

    /**
     * 向量(模、长度、大小)
     */
    public final double magnitude() {
        return Math.sqrt(x * x + y * y);
    }

    /**
     * 向量(模、长度、大小)的平方
     */
    public final double magnitudeSquared() {
        return x * x + y * y;
    }

    /**
     * 两向量的距离
     */
    public final double distance(double x, double y) {
        return Math.hypot(this.x - x, this.y - y);
    }

    /**
     * 两向量的距离
     */
    public final double distance(Vector v) {
        return Math.hypot(x - v.x, y - v.y);
    }

    /**
     * 两向量的距离平方
     */
    public final double distanceSq(double x, double y) {
        return (this.x - x) * (this.x - x) + (this.y - y) * (this.y - y);
    }

    /**
     * 两向量的距离平方
     */
    public final double distanceSq(Vector v) {
        return (x - v.x) * (x - v.x) + (y - v.y) * (y - v.y);
    }

    /**
     * 获得向量与x轴的夹角 范围为从 -180 到 180
     */
    public final double angle() {
        return direction() * Degrees;
    }

    /**
     * 获得向量与x轴的弧度 范围[π,-π]
     */
    public final double direction() {
        if (x == 0.0 || y == 0.0 || x == y || x == -y) {
            if (x > 0) {
                if (y > 0) {
                    return Math.PI / 4;
                }
                if (y < 0) {
                    return -Math.PI / 4;
                }
                return 0; // 向右
            }
            if (x < 0) {
                if (y > 0) {
                    return Math.PI - Math.PI / 4;
                }
                if (y < 0) {
                    return -Math.PI + Math.PI / 4;
                }
                return Math.PI;// 向左
            }
            if (y > 0) {
                return Math.PI / 2;// 上
            }
            if (y < 0) {
                return -Math.PI / 2;// 下
            }
            return Double.NaN;
        }
        return Math.atan2(y, x);
    }

    public final double angle(Vector vector) {
        return direction(vector) * Degrees;
    }

    /**
     * Returns the smallest angle between the given {@link Vector}s.
     * <p>
     * Returns the angle in radians in the range -&pi; to &pi;.
     *
     * @return 两向量之间的弧度 [-&pi;, &pi;]
     */
    public final double direction(Vector vector) {
        double a = vector.direction() - direction();
        if (a > Math.PI) {
            return a - Math.PI * 2;
        }
        if (a < -Math.PI) {
            return a + Math.PI * 2;
        }
        return a;
    }

    public final Vector project(double x, double y) {
        double dotProd = this.x * x + this.y * y;
        double denominator = x * x + y * y;
        if (denominator <= E) {
            return new Vector();
        }
        denominator = dotProd / denominator;
        return new Vector(denominator * x, denominator * y);
    }

    /**
     * 获取在v上的投影长度
     */
    public final double projectionLength(Vector vector) {
        double dot = x * vector.x + y * vector.y;
        // double length = dot(vector) / vector.magnitude();
        return dot / Math.sqrt(vector.x * vector.x + vector.y * vector.y);
    }

    /**
     * 判断当前向量是否是单位向量
     */
    public final boolean isNormalized() {
        return lengthSquared() == 1.0;
    }

    public final boolean isOrthogonal(double x, double y) {
        double dot = dot(x, y);
        return (dot <= 0.0D ? 0.0D - dot : dot) <= E;
    }

    public final boolean isValid() {
        return x == x && x != Double.NEGATIVE_INFINITY && x != Double.POSITIVE_INFINITY && y == y
                && y != Double.NEGATIVE_INFINITY && y != Double.POSITIVE_INFINITY;
    }

    public final boolean isZero() {
        return (x <= 0.0D ? 0.0D - x : x) <= E && (y <= 0.0D ? 0.0D - y : y) <= E;
    }

    public final Vector left() {
        double temp = x;
        x = y;
        y = -temp;
        return this;
    }

    public final Vector right() {
        double temp = x;
        x = -y;
        y = temp;
        return this;
    }

    public final Vector set(double x, double y) {
        this.x = x;
        this.y = y;
        return this;
    }

    public final Vector set(Vector vector) {
        x = vector.x;
        y = vector.y;
        return this;
    }

    public final Vector setAngle(double radians) {
        // double magnitude = Math.hypot(this.x, this.y);
        double magnitude = Math.sqrt(x * x + y * y);
        if (magnitude == 0) {
            x = getCos(radians);
            y = getSin(radians);
            return this;
        }
        x = magnitude * getCos(radians);
        y = magnitude * getSin(radians);
        return this;
    }

    public final Vector setDirection(double radians) {
        return setAngle(radians);
    }

    public final Vector setLength(double length) {
        return setMagnitude(length);
    }

    public final Vector setMagnitude(double magnitude) {
        // check the given magnitude
        if (Math.abs(magnitude) <= E) {
            x = 0.0;
            y = 0.0;
            return this;
        }
        // is this vector a zero vector?
        if (isZero()) {
            return this;
        }
        // get the magnitude
        double mag = Math.sqrt(x * x + y * y);
        // normalize and multiply by the new magnitude
        mag = magnitude / mag;
        x *= mag;
        y *= mag;
        return this;
    }

    public final double getX() {
        return x;
    }

    public final Vector setX(double x) {
        this.x = x;
        return this;
    }

    public final double getY() {
        return y;
    }

    public final Vector setY(double y) {
        this.y = y;
        return this;
    }

    /**
     * 获取向量所在直线的斜率
     */
    public final double getK() {
        if (x == 0) {
            return Double.NaN;
        }
        if (y == 0) {
            return 0;
        }
        return y / x;
    }

    @Override
    public final int hashCode() {
        final int prime = 31;
        int result = 1;
        long temp;
        temp = Double.doubleToLongBits(x);
        result = prime * result + (int) (temp ^ temp >>> 32);
        temp = Double.doubleToLongBits(y);
        result = prime * result + (int) (temp ^ temp >>> 32);
        return result;
    }

    @Override
    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (this.getClass() != obj.getClass()) {
            return false;
        }
        Vector other = (Vector) obj;
        if (Double.doubleToLongBits(x) != Double.doubleToLongBits(other.x)) {
            return false;
        }
        return Double.doubleToLongBits(y) == Double.doubleToLongBits(other.y);
    }

    @Override
    public final Vector clone() {
        return new Vector(x, y);
    }

    @Override
    public final String toString() {
        return "向量(" + x + "," + y + ")";
    }

}