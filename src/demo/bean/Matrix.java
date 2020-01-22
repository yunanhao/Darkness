package demo.bean;

/**
 * 仿射变换的3X3仿射矩阵
 *
 * <pre>
 * 转换矩阵操作，顺序为：缩放、切变、旋转、平移
 *  [   sx   shx   tx   ]
 *  [   shy  sy    ty   ]
 *  [   0    0     1    ]
 *
 *  [  m00  m01  m02  ]
 *  [  m10  m11  m12  ]
 *  [   0    0    1   ]
 * </pre>
 */
public class Matrix implements Cloneable, java.io.Serializable {
    /**
     * 2维平面仿射变换实例
     */
    public static final Matrix ATX = new Matrix();
    /**
     * 此常量指示此对象定义的变换是恒等变换。恒等变换是一种输出坐标始终与输入坐标相同的变换。如果此变换不是恒等变换，则类型要么是常量
     * GENERAL_TRANSFORM，要么是此变换执行的不同坐标转换的适当标志位的组合。
     */
    public static final int TYPE_IDENTITY = 0;
    /**
     * 此标志位指示此对象定义的变换除了执行其他标志位指示的转换外，还执行平移操作。平移按照常量 x 和 y 移动坐标，不改变向量的长度和角度。
     */
    public static final int TYPE_TRANSLATION = 1;
    /**
     * 此标志位指示此对象定义的变换除了执行其他标志位指示的转换外，还执行统一缩放操作。统一缩放在 x 和 y
     * 方向上使用相同的量乘以向量的长度，不改变向量之间的角度。此标志位与 TYPE_GENERAL_SCALE 标志互斥。
     */
    public static final int TYPE_UNIFORM_SCALE = 2;
    /**
     * 此标志位指示此对象定义的变换除了执行其他标志位指示的转换外，还执行常规缩放操作。常规缩放在 x 和 y
     * 方向上使用不同的量乘以向量的长度，不改变垂直向量之间的角度。此标志位与 TYPE_UNIFORM_SCALE 标志互斥。
     */
    public static final int TYPE_GENERAL_SCALE = 4;
    /**
     * 此常量是所有缩放标志位的位掩码。
     */
    public static final int TYPE_MASK_SCALE = TYPE_UNIFORM_SCALE | TYPE_GENERAL_SCALE;
    /**
     * 此标志位指示此对象定义的变换除了执行其他标志位指示的转换外，还执行关于某个坐标轴的镜像翻转操作，此操作将通常的右旋坐标系更改为左旋坐标系。右旋坐标系按逆时针方向旋转
     * X 正半轴能覆盖 Y 正半轴，类似于凝视右手拇指末端时其余手指弯曲的方向。左旋坐标系按顺时针方向旋转 X 正半轴能覆盖 Y
     * 正半轴，类似于左手手指弯曲的方向。不存在确定初始反转或镜像变换角度的数学方式，因为给定适当的调整旋转后，翻转的所有角度都相同。
     */
    public static final int TYPE_FLIP = 64;
    /**
     * 此标志位指示此对象定义的变换除了执行其他标志位指示的转换外，还通过乘以 90
     * 度的某个倍数执行象限旋转操作。旋转按相同的量更改向量的角度，不管向量的初始方向如何，也不改变向量的长度。此标志位与
     * TYPE_GENERAL_ROTATION 标志互斥。
     */
    public static final int TYPE_QUADRANT_ROTATION = 8;
    /**
     * 此标志位指示此对象定义的变换除了执行其他标志位指示的转换外，还执行任意角度的旋转操作。旋转按相同的量更改向量的角度，不管向量的初始方向如何，也不改变向量的长度。此标志位与
     * TYPE_QUADRANT_ROTATION 标志互斥。
     */
    public static final int TYPE_GENERAL_ROTATION = 16;
    /* NOTE: TYPE_FLIP was added after GENERAL_TRANSFORM was in public
     * circulation and the flag bits could no longer be conveniently
     * renumbered without introducing binary incompatibility in outside
     * code.
     */
    /**
     * 此常量是所有旋转标志位的位掩码。
     */
    public static final int TYPE_MASK_ROTATION = TYPE_QUADRANT_ROTATION | TYPE_GENERAL_ROTATION;
    /**
     * 此常量指示此对象定义的变换执行输入坐标的任意转换操作。如果此变换能归类为上述任意一种常量，则类型要么是常量
     * TYPE_IDENTITY，要么是此变换所执行的各种坐标转换的适当标志位。
     */
    public static final int TYPE_GENERAL_TRANSFORM = 32;
    /**
     * This constant is used for the internal state variable to indicate that no
     * calculations need to be performed and that the source coordinates only need
     * to be copied to their destinations to complete the transformation equation
     * of this transform.
     *
     * @see #APPLY_TRANSLATE
     * @see #APPLY_SCALE
     * @see #APPLY_SHEAR
     * @see #state
     */
    static final int APPLY_IDENTITY = 0;
    /**
     * This constant is used for the internal state variable to indicate that the
     * translation components of the matrix (m02 and m12) need to be added to
     * complete the transformation equation of this transform.
     *
     * @see #APPLY_IDENTITY
     * @see #APPLY_SCALE
     * @see #APPLY_SHEAR
     * @see #state
     */
    static final int APPLY_TRANSLATE = 1;
    /**
     * This constant is used for the internal state variable to indicate that the
     * scaling components of the matrix (m00 and m11) need to be factored in to
     * complete the transformation equation of this transform. If the APPLY_SHEAR
     * bit is also set then it indicates that the scaling components are not both
     * 0.0. If the APPLY_SHEAR bit is not also set then it indicates that the
     * scaling components are not both 1.0. If neither the APPLY_SHEAR nor the
     * APPLY_SCALE bits are set then the scaling components are both 1.0, which
     * means that the x and y components contribute to the transformed coordinate,
     * but they are not multiplied by any scaling factor.
     *
     * @see #APPLY_IDENTITY
     * @see #APPLY_TRANSLATE
     * @see #APPLY_SHEAR
     * @see #state
     */
    static final int APPLY_SCALE = 2;
    /**
     * This constant is used for the internal state variable to indicate that the
     * shearing components of the matrix (m01 and m10) need to be factored in to
     * complete the transformation equation of this transform. The presence of
     * this bit in the state variable changes the interpretation of the
     * APPLY_SCALE bit as indicated in its documentation.
     *
     * @see #APPLY_IDENTITY
     * @see #APPLY_TRANSLATE
     * @see #APPLY_SCALE
     * @see #state
     */
    static final int APPLY_SHEAR = 4;
    private static final long serialVersionUID = 717013554067139658L;
    /**
     * 这个常量只对缓存类型字段有用.这表明类型已清除缓存必须重新计算.
     */
    private static final int TYPE_UNKNOWN = -1;
    /*
     * For methods which combine together the state of two separate
     * transforms and dispatch based upon the combination, these constants
     * specify how far to shift one of the states so that the two states
     * are mutually non-interfering and provide constants for testing the
     * bits of the shifted (HI) state.  The methods in this class use
     * the convention that the state of "this" transform is unshifted and
     * the state of the "other" or "argument" transform is shifted (HI).
     */
    private static final int HI_SHIFT = 3;
    private static final int HI_IDENTITY = APPLY_IDENTITY << HI_SHIFT;
    private static final int HI_TRANSLATE = APPLY_TRANSLATE << HI_SHIFT;
    private static final int HI_SCALE = APPLY_SCALE << HI_SHIFT;
    private static final int HI_SHEAR = APPLY_SHEAR << HI_SHIFT;
    // Utility methods to optimize rotate methods.
    // These tables translate the flags during predictable quadrant
    // rotations where the shear and scale values are swapped and negated.
    private static final int[] rot90conversion = { /* IDENTITY => */ APPLY_SHEAR, /* TRANSLATE (TR) => */ APPLY_SHEAR
            | APPLY_TRANSLATE, /* SCALE (SC) => */ APPLY_SHEAR, /* SC | TR => */ APPLY_SHEAR | APPLY_TRANSLATE,
            /* SHEAR (SH) => */ APPLY_SCALE, /* SH | TR => */ APPLY_SCALE | APPLY_TRANSLATE, /* SH | SC => */ APPLY_SHEAR
            | APPLY_SCALE, /* SH | SC | TR => */ APPLY_SHEAR | APPLY_SCALE | APPLY_TRANSLATE,};
    /**
     * The X coordinate scaling element of the 3x3 affine transformation matrix.
     *
     * @serial
     */
    public double m00;
    /**
     * The Y coordinate shearing element of the 3x3 affine transformation matrix.
     *
     * @serial
     */
    public double m10;
    /**
     * The X coordinate shearing element of the 3x3 affine transformation matrix.
     *
     * @serial
     */
    public double m01;
    /**
     * The Y coordinate scaling element of the 3x3 affine transformation matrix.
     *
     * @serial
     */
    public double m11;
    /**
     * The X coordinate of the translation element of the 3x3 affine
     * transformation matrix.
     *
     * @serial
     */
    public double m02;
    /**
     * The Y coordinate of the translation element of the 3x3 affine
     * transformation matrix.
     *
     * @serial
     */
    public double m12;
    /**
     * This field keeps track of which components of the matrix need to be applied
     * when performing a transformation.
     *
     * @see #APPLY_IDENTITY
     * @see #APPLY_TRANSLATE
     * @see #APPLY_SCALE
     * @see #APPLY_SHEAR
     */
    public transient int state;
    /**
     * This field caches the current transformation type of the matrix.
     *
     * @see #TYPE_IDENTITY
     * @see #TYPE_TRANSLATION
     * @see #TYPE_UNIFORM_SCALE
     * @see #TYPE_GENERAL_SCALE
     * @see #TYPE_FLIP
     * @see #TYPE_QUADRANT_ROTATION
     * @see #TYPE_GENERAL_ROTATION
     * @see #TYPE_GENERAL_TRANSFORM
     * @see #TYPE_UNKNOWN
     * @see #getType
     */
    public transient int type;
    Vector a = new Vector(1, 0);
    Vector b = new Vector(1, 0);

    /**
     * 构造一个表示恒等变换的新 <code>AffineTransform</code>.
     */
    public Matrix() {
        m00 = m11 = 1.0;
        m01 = m10 = m02 = m12 = 0.0; /* Not needed. */
        state = APPLY_IDENTITY; /* Not needed. */
        type = TYPE_IDENTITY; /* Not needed. */
    }

    /**
     * 根据双精度值数组构造一个新 AffineTransform，该数组要么表示 3x3 变换矩阵的 4 个非平移条目，要么表示它的 6
     * 个可指定条目。从该数组中检索到的值为 { m00 m10 m01 m11 [m02 m12]}。
     *
     * <pre>
     * "scaleX", "shearY", "shearX", "scaleY", "translateX", "translateY"
     * </pre>
     *
     * @param flatmatrix 数组，包含要在新 AffineTransform 对象中设置的值。假定数组的长度至少为 4。如果数组的长度小于 6，则仅采用前 4
     *                   个值。如果数组的长度大于 6，则采用前 6 个值。
     */
    public Matrix(double... flatmatrix) {
        m00 = flatmatrix[0];
        m10 = flatmatrix[1];
        m01 = flatmatrix[2];
        m11 = flatmatrix[3];
        if (flatmatrix.length > 5) {
            m02 = flatmatrix[4];
            m12 = flatmatrix[5];
        }
        updateState();
    }

    /**
     * 根据表示 3x3 变换矩阵 6 个可指定条目的 6 个双精度值构造一个新 AffineTransform
     */
    public Matrix(double m00, double m10, double m01, double m11, double m02, double m12, int state) {
        this.m00 = m00;
        this.m10 = m10;
        this.m01 = m01;
        this.m11 = m11;
        this.m02 = m02;
        this.m12 = m12;
        this.state = state;
        type = TYPE_UNKNOWN;
    }

    // Round values to sane precision for printing
    // Note that Math.sin(Math.PI) has an error of about 10^-16
    private static double _matround(double matval) {
        return Math.rint(matval * 1E15) / 1E15;
    }

    /**
     * 检索描述此变换的变换属性的标志位。返回值要么是常量 TYPE_IDENTITY 或 TYPE_GENERAL_TRANSFORM
     * 中的一个，要么是适当标志位的组合。标志位的有效组合是一个异或操作，该操作除了组合 TYPE_UNIFORM_SCALE 或
     * TYPE_GENERAL_SCALE 标志位以及 TYPE_QUADRANT_ROTATION 或 TYPE_GENERAL_ROTATION
     * 标志位之外，还可以组合 TYPE_TRANSLATION 标志位.
     */
    public int getType() {
        if (type == TYPE_UNKNOWN) {
            calculateType();
        }
        return type;
    }

    /**
     * 这是在未缓存的情况下计算标志位的实用函数.
     */
    @SuppressWarnings("fallthrough")
    private void calculateType() {
        int ret = TYPE_IDENTITY;
        boolean sgn0, sgn1;
        double M0, M1, M2, M3;
        updateState();
        switch (state) {
            default:
                stateError();
                /* NOTREACHED */
            case APPLY_SHEAR | APPLY_SCALE | APPLY_TRANSLATE:
                ret = TYPE_TRANSLATION;
                /* NOBREAK */
            case APPLY_SHEAR | APPLY_SCALE:
                if ((M0 = m00) * (M2 = m01) + (M3 = m10) * (M1 = m11) != 0) {
                    // Transformed unit vectors are not perpendicular...
                    type = TYPE_GENERAL_TRANSFORM;
                    return;
                }
                sgn0 = M0 >= 0.0;
                sgn1 = M1 >= 0.0;
                if (sgn0 == sgn1) {
                    // sgn(M0) == sgn(M1) therefore sgn(M2) == -sgn(M3)
                    // This is the "unflipped" (right-handed) state
                    if (M0 != M1 || M2 != -M3) {
                        ret |= TYPE_GENERAL_ROTATION | TYPE_GENERAL_SCALE;
                    } else if (M0 * M1 - M2 * M3 != 1.0) {
                        ret |= TYPE_GENERAL_ROTATION | TYPE_UNIFORM_SCALE;
                    } else {
                        ret |= TYPE_GENERAL_ROTATION;
                    }
                } else {
                    // sgn(M0) == -sgn(M1) therefore sgn(M2) == sgn(M3)
                    // This is the "flipped" (left-handed) state
                    if (M0 != -M1 || M2 != M3) {
                        ret |= TYPE_GENERAL_ROTATION | TYPE_FLIP | TYPE_GENERAL_SCALE;
                    } else if (M0 * M1 - M2 * M3 != 1.0) {
                        ret |= TYPE_GENERAL_ROTATION | TYPE_FLIP | TYPE_UNIFORM_SCALE;
                    } else {
                        ret |= TYPE_GENERAL_ROTATION | TYPE_FLIP;
                    }
                }
                break;
            case APPLY_SHEAR | APPLY_TRANSLATE:
                ret = TYPE_TRANSLATION;
                /* NOBREAK */
            case APPLY_SHEAR:
                sgn0 = (M0 = m01) >= 0.0;
                sgn1 = (M1 = m10) >= 0.0;
                if (sgn0 != sgn1) {
                    // Different signs - simple 90 degree rotation
                    if (M0 != -M1) {
                        ret |= TYPE_QUADRANT_ROTATION | TYPE_GENERAL_SCALE;
                    } else if (M0 != 1.0 && M0 != -1.0) {
                        ret |= TYPE_QUADRANT_ROTATION | TYPE_UNIFORM_SCALE;
                    } else {
                        ret |= TYPE_QUADRANT_ROTATION;
                    }
                } else {
                    // Same signs - 90 degree rotation plus an axis flip too
                    if (M0 == M1) {
                        ret |= TYPE_QUADRANT_ROTATION | TYPE_FLIP | TYPE_UNIFORM_SCALE;
                    } else {
                        ret |= TYPE_QUADRANT_ROTATION | TYPE_FLIP | TYPE_GENERAL_SCALE;
                    }
                }
                break;
            case APPLY_SCALE | APPLY_TRANSLATE:
                ret = TYPE_TRANSLATION;
                /* NOBREAK */
            case APPLY_SCALE:
                sgn0 = (M0 = m00) >= 0.0;
                sgn1 = (M1 = m11) >= 0.0;
                if (sgn0 == sgn1) {
                    if (sgn0) {
                        // Both scaling factors non-negative - simple scale
                        // Note: APPLY_SCALE implies M0, M1 are not both 1
                        if (M0 == M1) {
                            ret |= TYPE_UNIFORM_SCALE;
                        } else {
                            ret |= TYPE_GENERAL_SCALE;
                        }
                    } else {
                        // Both scaling factors negative - 180 degree rotation
                        if (M0 != M1) {
                            ret |= TYPE_QUADRANT_ROTATION | TYPE_GENERAL_SCALE;
                        } else if (M0 != -1.0) {
                            ret |= TYPE_QUADRANT_ROTATION | TYPE_UNIFORM_SCALE;
                        } else {
                            ret |= TYPE_QUADRANT_ROTATION;
                        }
                    }
                } else {
                    // Scaling factor signs different - flip about some axis
                    if (M0 == -M1) {
                        if (M0 == 1.0 || M0 == -1.0) {
                            ret |= TYPE_FLIP;
                        } else {
                            ret |= TYPE_FLIP | TYPE_UNIFORM_SCALE;
                        }
                    } else {
                        ret |= TYPE_FLIP | TYPE_GENERAL_SCALE;
                    }
                }
                break;
            case APPLY_TRANSLATE:
                ret = TYPE_TRANSLATION;
                break;
            case APPLY_IDENTITY:
                break;
        }
        type = ret;
    }

    /**
     * 返回变换的矩阵表示形式的决定因子。决定因子用于确定变换是否可逆，以及获取表示变换的组合 X 和 Y 缩放的单个值。
     * 如果决定因子非零，则此变换是可逆的，依赖于此逆向变换的各种方法均无需抛出 NoninvertibleTransformException。
     * 如果决定因子为零，则此变换是不可逆的，因为变换将所有输入坐标映射到线或点上。 如果决定因子非常接近零，则逆向变换操作可能不够精确，无法生成有意义结果。
     * <p>
     * 如果像getType方法指示的那样，此变换表示统一缩放，则决定因子还表示统一缩放因子的平方。 所有点都通过该缩放因子从原点展开或向原点收缩。
     * 如果此变换表示非统一缩放或较通用的变换，则决定因子只表示确定逆向变换是否可能，而不表示除此之外任何有用值。
     * <p>
     * 在数学上，使用以下公式计算决定因子：
     *
     * <pre>
     *          |  m00  m01  m02  |
     *          |  m10  m11  m12  |  =  m00 * m11 - m01 * m10
     *          |   0    0    1   |
     * </pre>
     *
     * @return 用于变换坐标的矩阵的决定因子.
     */
    @SuppressWarnings("fallthrough")
    public double getDeterminant() {
        switch (state) {
            default:
                stateError();
                /* NOTREACHED */
            case APPLY_SHEAR | APPLY_SCALE | APPLY_TRANSLATE:
            case APPLY_SHEAR | APPLY_SCALE:
                return m00 * m11 - m01 * m10;
            case APPLY_SHEAR | APPLY_TRANSLATE:
            case APPLY_SHEAR:
                return -(m01 * m10);
            case APPLY_SCALE | APPLY_TRANSLATE:
            case APPLY_SCALE:
                return m00 * m11;
            case APPLY_TRANSLATE:
            case APPLY_IDENTITY:
                return 1.0;
        }
    }

    /**
     * Manually recalculates the state of the transform when the matrix changes
     * too much to predict the effects on the state. The following table specifies
     * what the various settings of the state field say about the values of the
     * corresponding matrix element fields. Note that the rules governing the
     * SCALE fields are slightly different depending on whether the SHEAR flag is
     * also set.
     *
     * <pre>
     *                     SCALE            SHEAR          TRANSLATE
     *                    m00/m11          m01/m10          m02/m12
     *
     * IDENTITY             1.0              0.0              0.0
     * TRANSLATE (TR)       1.0              0.0          not both 0.0
     * SCALE (SC)       not both 1.0         0.0              0.0
     * TR | SC          not both 1.0         0.0          not both 0.0
     * SHEAR (SH)           0.0          not both 0.0         0.0
     * TR | SH              0.0          not both 0.0     not both 0.0
     * SC | SH          not both 0.0     not both 0.0         0.0
     * TR | SC | SH     not both 0.0     not both 0.0     not both 0.0
     * </pre>
     */
    public void updateState() {
        if (m01 == 0.0 && m10 == 0.0) {
            if (m00 == 1.0 && m11 == 1.0) {
                if (m02 == 0.0 && m12 == 0.0) {
                    state = APPLY_IDENTITY;
                    type = TYPE_IDENTITY;
                } else {
                    state = APPLY_TRANSLATE;
                    type = TYPE_TRANSLATION;
                }
            } else {
                if (m02 == 0.0 && m12 == 0.0) {
                    state = APPLY_SCALE;
                    type = TYPE_UNKNOWN;
                } else {
                    state = APPLY_SCALE | APPLY_TRANSLATE;
                    type = TYPE_UNKNOWN;
                }
            }
        } else {
            if (m00 == 0.0 && m11 == 0.0) {
                if (m02 == 0.0 && m12 == 0.0) {
                    state = APPLY_SHEAR;
                    type = TYPE_UNKNOWN;
                } else {
                    state = APPLY_SHEAR | APPLY_TRANSLATE;
                    type = TYPE_UNKNOWN;
                }
            } else {
                if (m02 == 0.0 && m12 == 0.0) {
                    state = APPLY_SHEAR | APPLY_SCALE;
                    type = TYPE_UNKNOWN;
                } else {
                    state = APPLY_SHEAR | APPLY_SCALE | APPLY_TRANSLATE;
                    type = TYPE_UNKNOWN;
                }
            }
        }
    }

    /*
     * Convenience method used internally to throw exceptions when
     * a case was forgotten in a switch statement.
     */
    private void stateError() {
        throw new InternalError("missing case in transform state switch");
    }

    /**
     * 将此变换设置为 6 个双精度值指定的矩阵
     *
     * @param m00 the X coordinate scaling element of the 3x3 matrix
     * @param m10 the Y coordinate shearing element of the 3x3 matrix
     * @param m01 the X coordinate shearing element of the 3x3 matrix
     * @param m11 the Y coordinate scaling element of the 3x3 matrix
     * @param m02 the X coordinate translation element of the 3x3 matrix
     * @param m12 the Y coordinate translation element of the 3x3 matrix
     */
    public void setTransform(double m00, double m10, double m01, double m11, double m02, double m12) {
        this.m00 = m00;
        this.m10 = m10;
        this.m01 = m01;
        this.m11 = m11;
        this.m02 = m02;
        this.m12 = m12;
        updateState();
    }

    /**
     * 将此仿射变换的参数设定成指定的变换参数
     */
    public void setTransform(Matrix Tx) {
        m00 = Tx.m00;
        m10 = Tx.m10;
        m01 = Tx.m01;
        m11 = Tx.m11;
        m02 = Tx.m02;
        m12 = Tx.m12;
        state = Tx.state;
        type = Tx.type;
    }

    /**
     * 在 3x3 仿射变换矩阵中检索 6 个可指定值，并将其置于双精度值的数组中。值将以 { m00 m10 m01 m11 m02 m12 }
     * 形式存储到该数组中。也可以指定包含 4 个 double 值的数组，在这种情况下，仅检索表示数组中非变换部分的前四个元素，并将值以 { m00 m10
     * m01 m11 } 的形式存储到数组中。
     */
    public double[] getTransform(double... matrix) {
        if (matrix == null) {
            matrix = new double[6];
        }
        matrix[0] = m00;
        matrix[1] = m10;
        matrix[2] = m01;
        matrix[3] = m11;
        if (matrix.length > 5) {
            matrix[4] = m02;
            matrix[5] = m12;
        }
        return matrix;
    }

    /**
     * Returns the X coordinate scaling element (m00) of the 3x3 affine
     * transformation matrix.
     *
     * @return a double value that is the X coordinate of the scaling element of
     * the affine transformation matrix.
     * @see #getMatrix
     * @since 1.2
     */
    public double getScaleX() {
        return m00;
    }

    /**
     * Returns the Y coordinate scaling element (m11) of the 3x3 affine
     * transformation matrix.
     *
     * @return a double value that is the Y coordinate of the scaling element of
     * the affine transformation matrix.
     * @see #getMatrix
     * @since 1.2
     */
    public double getScaleY() {
        return m11;
    }

    /**
     * Returns the X coordinate shearing element (m01) of the 3x3 affine
     * transformation matrix.
     *
     * @return a double value that is the X coordinate of the shearing element of
     * the affine transformation matrix.
     * @see #getMatrix
     * @since 1.2
     */
    public double getShearX() {
        return m01;
    }

    /**
     * Returns the Y coordinate shearing element (m10) of the 3x3 affine
     * transformation matrix.
     *
     * @return a double value that is the Y coordinate of the shearing element of
     * the affine transformation matrix.
     * @see #getMatrix
     * @since 1.2
     */
    public double getShearY() {
        return m10;
    }

    /**
     * Returns the X coordinate of the translation element (m02) of the 3x3 affine
     * transformation matrix.
     *
     * @return a double value that is the X coordinate of the translation element
     * of the affine transformation matrix.
     * @see #getMatrix
     * @since 1.2
     */
    public double getTranslateX() {
        return m02;
    }

    /**
     * Returns the Y coordinate of the translation element (m12) of the 3x3 affine
     * transformation matrix.
     *
     * @return a double value that is the Y coordinate of the translation element
     * of the affine transformation matrix.
     * @see #getMatrix
     * @since 1.2
     */
    public double getTranslateY() {
        return m12;
    }

    /**
     * 返回仿射矩阵的旋转角(以弧度表示的结果)
     */
    public double getRotateAngle() {
        b.set(a);
        deltaTransform(b);
        return -a.direction(b);
    }

    public final void rotate90() {
        double M0 = m00;
        m00 = m01;
        m01 = -M0;
        M0 = m10;
        m10 = m11;
        m11 = -M0;
        int state = rot90conversion[this.state];
        if ((state & (APPLY_SHEAR | APPLY_SCALE)) == APPLY_SCALE && m00 == 1.0 && m11 == 1.0) {
            state -= APPLY_SCALE;
        }
        this.state = state;
        type = TYPE_UNKNOWN;
    }

    public final void rotate180() {
        m00 = -m00;
        m11 = -m11;
        int state = this.state;
        if ((state & APPLY_SHEAR) != 0) {
            // If there was a shear, then this rotation has no
            // effect on the state.
            m01 = -m01;
            m10 = -m10;
        } else {
            // No shear means the SCALE state may toggle when
            // m00 and m11 are negated.
            if (m00 == 1.0 && m11 == 1.0) {
                this.state = state & ~APPLY_SCALE;
            } else {
                this.state = state | APPLY_SCALE;
            }
        }
        type = TYPE_UNKNOWN;
    }

    public final void rotate270() {
        double M0 = m00;
        m00 = -m01;
        m01 = M0;
        M0 = m10;
        m10 = -m11;
        m11 = M0;
        int state = rot90conversion[this.state];
        if ((state & (APPLY_SHEAR | APPLY_SCALE)) == APPLY_SCALE && m00 == 1.0 && m11 == 1.0) {
            state -= APPLY_SCALE;
        }
        this.state = state;
        type = TYPE_UNKNOWN;
    }

    /**
     * 连接此变换与旋转变换。 这等效于调用 concatenate(R)，其中 R 为 AffineTransform，由以下矩阵表示：
     *
     * <pre>
     *          [   cos(theta)    -sin(theta)    0   ]
     *          [   sin(theta)     cos(theta)    0   ]
     *          [       0              0         1   ]
     * </pre>
     */
    public void rotate(double theta) {
        double sin = Math.sin(theta);
        if (sin == 1.0) {
            rotate90();
        } else if (sin == -1.0) {
            rotate270();
        } else {
            double cos = Math.cos(theta);
            if (cos == -1.0) {
                rotate180();
            } else if (cos != 1.0) {
                double M0, M1;
                M0 = m00;
                M1 = m01;
                m00 = cos * M0 + sin * M1;
                m01 = -sin * M0 + cos * M1;
                M0 = m10;
                M1 = m11;
                m10 = cos * M0 + sin * M1;
                m11 = -sin * M0 + cos * M1;
                updateState();
            }
        }
    }

    /**
     * 连接此变换与绕锚点旋转坐标的变换。此操作等效于：
     *
     * <pre>
     * 平移坐标使锚点位于原点 (S1)
     * 然后绕新原点 (S2)旋转它们
     * 最后再平移，将中间原点恢复为初始锚点 (S3) 的坐标
     * </pre>
     * <p>
     * 此操作等效于以下调用序列：
     *
     * <pre>
     * translate(anchorx, anchory); // S3: final translation
     * rotate(theta); // S2: rotate around anchor
     * translate(-anchorx, -anchory); // S1: translate anchor to origin
     * </pre>
     * <p>
     * 用正角度 theta 进行的旋转将 X 正半轴上的点向 Y 正半轴旋转。还要注意上文处理 90 度旋转的讨论。
     */
    public void rotate(double theta, double anchorx, double anchory) {
        // REMIND: Simple for now - optimize later
        translate(anchorx, anchory);
        rotate(theta);
        translate(-anchorx, -anchory);
    }

    /**
     * 连接此变换与根据旋转向量旋转坐标的变换。 所有坐标按相同的量绕原点旋转。 旋转量使沿原 X轴正半轴的坐标随后将与从坐标原点指向指定向量坐标的向量对齐。
     * 如果 vecx 和 vecy 都是 0.0，则没有附加的旋转添加到此变换。此操作等效于调用：
     *
     * <pre>
     * rotate(Math.atan2(vecy, vecx));
     * </pre>
     */
    public void rotate(double vecx, double vecy) {
        if (vecy == 0.0) {
            if (vecx < 0.0) {
                rotate180();
            }
            // If vecx > 0.0 - no rotation
            // If vecx == 0.0 - undefined rotation - treat as no rotation
        } else if (vecx == 0.0) {
            if (vecy > 0.0) {
                rotate90();
            } else { // vecy must be < 0.0
                rotate270();
            }
        } else {
            double len = Math.sqrt(vecx * vecx + vecy * vecy);
            double sin = vecy / len;
            double cos = vecx / len;
            double M0, M1;
            M0 = m00;
            M1 = m01;
            m00 = cos * M0 + sin * M1;
            m01 = -sin * M0 + cos * M1;
            M0 = m10;
            M1 = m11;
            m10 = cos * M0 + sin * M1;
            m11 = -sin * M0 + cos * M1;
            updateState();
        }
    }

    /**
     * 连接此变换与根据旋转向量绕锚点旋转坐标的变换。 所有坐标按相同的量绕指定的锚坐标旋转。 旋转量使沿原
     * X轴正半轴的坐标随后将与从坐标原点指向指定向量坐标的向量对齐。 如果 vecx 和 vecy 都是
     * 0.0，则不以任何方式修改此变换。此方法等效于调用：
     *
     * <pre>
     * rotate(Math.atan2(vecy, vecx), anchorx, anchory);
     * </pre>
     */
    public void rotate(double vecx, double vecy, double anchorx, double anchory) {
        // REMIND: Simple for now - optimize later
        translate(anchorx, anchory);
        rotate(vecx, vecy);
        translate(-anchorx, -anchory);
    }

    /**
     * Concatenates this transform with a scaling transformation. This is
     * equivalent to calling concatenate(S), where S is an
     * <code>AffineTransform</code> represented by the following matrix:
     *
     * <pre>
     *          [   sx   0    0   ]
     *          [   0    sy   0   ]
     *          [   0    0    1   ]
     * </pre>
     *
     * @param sx the factor by which coordinates are scaled along the X axis
     *           direction
     * @param sy the factor by which coordinates are scaled along the Y axis
     *           direction
     * @since 1.2
     */
    @SuppressWarnings("fallthrough")
    public void scale(double sx, double sy) {
        int state = this.state;
        switch (state) {
            default:
                stateError();
                /* NOTREACHED */
            case APPLY_SHEAR | APPLY_SCALE | APPLY_TRANSLATE:
            case APPLY_SHEAR | APPLY_SCALE:
                m00 *= sx;
                m11 *= sy;
                /* NOBREAK */
            case APPLY_SHEAR | APPLY_TRANSLATE:
            case APPLY_SHEAR:
                m01 *= sy;
                m10 *= sx;
                if (m01 == 0 && m10 == 0) {
                    state &= APPLY_TRANSLATE;
                    if (m00 == 1.0 && m11 == 1.0) {
                        type = state == APPLY_IDENTITY ? TYPE_IDENTITY : TYPE_TRANSLATION;
                    } else {
                        state |= APPLY_SCALE;
                        type = TYPE_UNKNOWN;
                    }
                    this.state = state;
                }
                return;
            case APPLY_SCALE | APPLY_TRANSLATE:
            case APPLY_SCALE:
                m00 *= sx;
                m11 *= sy;
                if (m00 == 1.0 && m11 == 1.0) {
                    this.state = state &= APPLY_TRANSLATE;
                    type = state == APPLY_IDENTITY ? TYPE_IDENTITY : TYPE_TRANSLATION;
                } else {
                    type = TYPE_UNKNOWN;
                }
                return;
            case APPLY_TRANSLATE:
            case APPLY_IDENTITY:
                m00 = sx;
                m11 = sy;
                if (sx != 1.0 || sy != 1.0) {
                    this.state = state | APPLY_SCALE;
                    type = TYPE_UNKNOWN;
                }
                return;
        }
    }

    /**
     * Concatenates this transform with a shearing transformation. This is
     * equivalent to calling concatenate(SH), where SH is an
     * <code>AffineTransform</code> represented by the following matrix:
     *
     * <pre>
     *          [   1   shx   0   ]
     *          [  shy   1    0   ]
     *          [   0    0    1   ]
     * </pre>
     *
     * @param shx the multiplier by which coordinates are shifted in the direction
     *            of the positive X axis as a factor of their Y coordinate
     * @param shy the multiplier by which coordinates are shifted in the direction
     *            of the positive Y axis as a factor of their X coordinate
     * @since 1.2
     */
    public void shear(double shx, double shy) {
        int state = this.state;
        switch (state) {
            default:
                stateError();
                /* NOTREACHED */
                return;
            case APPLY_SHEAR | APPLY_SCALE | APPLY_TRANSLATE:
            case APPLY_SHEAR | APPLY_SCALE:
                double M0, M1;
                M0 = m00;
                M1 = m01;
                m00 = M0 + M1 * shy;
                m01 = M0 * shx + M1;

                M0 = m10;
                M1 = m11;
                m10 = M0 + M1 * shy;
                m11 = M0 * shx + M1;
                updateState();
                return;
            case APPLY_SHEAR | APPLY_TRANSLATE:
            case APPLY_SHEAR:
                m00 = m01 * shy;
                m11 = m10 * shx;
                if (m00 != 0.0 || m11 != 0.0) {
                    this.state = state | APPLY_SCALE;
                }
                type = TYPE_UNKNOWN;
                return;
            case APPLY_SCALE | APPLY_TRANSLATE:
            case APPLY_SCALE:
                m01 = m00 * shx;
                m10 = m11 * shy;
                if (m01 != 0.0 || m10 != 0.0) {
                    this.state = state | APPLY_SHEAR;
                }
                type = TYPE_UNKNOWN;
                return;
            case APPLY_TRANSLATE:
            case APPLY_IDENTITY:
                m01 = shx;
                m10 = shy;
                if (m01 != 0.0 || m10 != 0.0) {
                    this.state = state | APPLY_SCALE | APPLY_SHEAR;
                    type = TYPE_UNKNOWN;
                }
                return;
        }
    }

    /**
     * Concatenates this transform with a translation transformation. This is
     * equivalent to calling concatenate(T), where T is an
     * <code>AffineTransform</code> represented by the following matrix:
     *
     * <pre>
     *          [   1    0    tx  ]
     *          [   0    1    ty  ]
     *          [   0    0    1   ]
     * </pre>
     *
     * @param tx the distance by which coordinates are translated in the X axis
     *           direction
     * @param ty the distance by which coordinates are translated in the Y axis
     *           direction
     * @since 1.2
     */
    public void translate(double tx, double ty) {
        switch (state) {
            default:
                stateError();
                /* NOTREACHED */
                return;
            case APPLY_SHEAR | APPLY_SCALE | APPLY_TRANSLATE:
                m02 = tx * m00 + ty * m01 + m02;
                m12 = tx * m10 + ty * m11 + m12;
                if (m02 == 0.0 && m12 == 0.0) {
                    state = APPLY_SHEAR | APPLY_SCALE;
                    if (type != TYPE_UNKNOWN) {
                        type -= TYPE_TRANSLATION;
                    }
                }
                return;
            case APPLY_SHEAR | APPLY_SCALE:
                m02 = tx * m00 + ty * m01;
                m12 = tx * m10 + ty * m11;
                if (m02 != 0.0 || m12 != 0.0) {
                    state = APPLY_SHEAR | APPLY_SCALE | APPLY_TRANSLATE;
                    type |= TYPE_TRANSLATION;
                }
                return;
            case APPLY_SHEAR | APPLY_TRANSLATE:
                m02 = ty * m01 + m02;
                m12 = tx * m10 + m12;
                if (m02 == 0.0 && m12 == 0.0) {
                    state = APPLY_SHEAR;
                    if (type != TYPE_UNKNOWN) {
                        type -= TYPE_TRANSLATION;
                    }
                }
                return;
            case APPLY_SHEAR:
                m02 = ty * m01;
                m12 = tx * m10;
                if (m02 != 0.0 || m12 != 0.0) {
                    state = APPLY_SHEAR | APPLY_TRANSLATE;
                    type |= TYPE_TRANSLATION;
                }
                return;
            case APPLY_SCALE | APPLY_TRANSLATE:
                m02 = tx * m00 + m02;
                m12 = ty * m11 + m12;
                if (m02 == 0.0 && m12 == 0.0) {
                    state = APPLY_SCALE;
                    if (type != TYPE_UNKNOWN) {
                        type -= TYPE_TRANSLATION;
                    }
                }
                return;
            case APPLY_SCALE:
                m02 = tx * m00;
                m12 = ty * m11;
                if (m02 != 0.0 || m12 != 0.0) {
                    state = APPLY_SCALE | APPLY_TRANSLATE;
                    type |= TYPE_TRANSLATION;
                }
                return;
            case APPLY_TRANSLATE:
                m02 = tx + m02;
                m12 = ty + m12;
                if (m02 == 0.0 && m12 == 0.0) {
                    state = APPLY_IDENTITY;
                    type = TYPE_IDENTITY;
                }
                return;
            case APPLY_IDENTITY:
                m02 = tx;
                m12 = ty;
                if (tx != 0.0 || ty != 0.0) {
                    state = APPLY_TRANSLATE;
                    type = TYPE_TRANSLATION;
                }
                return;
        }
    }

    /**
     * 变换由 vector 指定的相对距离向量，并将结果存储在 vector 中。使用以下方程式变换相对距离向量，不应用仿射变换矩阵的平移组件
     *
     * <pre>
     *  [  x' ]   [  m00  m01 (m02) ] [  x  ]   [ m00x + m01y ]
     *  [  y' ] = [  m10  m11 (m12) ] [  y  ] = [ m10x + m11y ]
     *  [ (1) ]   [  (0)  (0) ( 1 ) ] [ (1) ]   [     (1)     ]
     * </pre>
     */
    public Vector deltaTransform(Vector vector) {
        double x = vector.getX();
        double y = vector.getY();
        switch (state) {
            default:
                stateError();
                /* NOTREACHED */
                return null;
            case APPLY_SHEAR | APPLY_SCALE | APPLY_TRANSLATE:
            case APPLY_SHEAR | APPLY_SCALE:
                vector.set(x * m00 + y * m01, x * m10 + y * m11);
                return vector;
            case APPLY_SHEAR | APPLY_TRANSLATE:
            case APPLY_SHEAR:
                vector.set(y * m01, x * m10);
                return vector;
            case APPLY_SCALE | APPLY_TRANSLATE:
            case APPLY_SCALE:
                vector.set(x * m00, y * m11);
                return vector;
            case APPLY_TRANSLATE:
            case APPLY_IDENTITY:
                vector.set(x, y);
                return vector;
        }
        /* NOTREACHED */
    }

    /**
     * Transforms an array of relative distance vectors by this transform. A
     * relative distance vector is transformed without applying the translation
     * components of the affine transformation matrix using the following
     * equations:
     *
     * <pre>
     *  [  x' ]   [  m00  m01 (m02) ] [  x  ]   [ m00x + m01y ]
     *  [  y' ] = [  m10  m11 (m12) ] [  y  ] = [ m10x + m11y ]
     *  [ (1) ]   [  (0)  (0) ( 1 ) ] [ (1) ]   [     (1)     ]
     * </pre>
     * <p>
     * The two coordinate array sections can be exactly the same or can be
     * overlapping sections of the same array without affecting the validity of
     * the results. This method ensures that no source coordinates are overwritten
     * by a previous operation before they can be transformed. The coordinates are
     * stored in the arrays starting at the indicated offset in the order
     * <code>[x0, y0, x1, y1, ..., xn, yn]</code>.
     */
    public double[] deltaTransform(double... points) {
        double M00, M01, M10, M11; // For caching
        int index = 0;
        int numPts = points.length >> 1;
        double x, y;
        switch (state) {
            default:
                stateError();
                /* NOTREACHED */
                return null;
            case APPLY_SHEAR | APPLY_SCALE | APPLY_TRANSLATE:
            case APPLY_SHEAR | APPLY_SCALE:
                M00 = m00;
                M01 = m01;
                M10 = m10;
                M11 = m11;
                while (--numPts >= 0) {
                    x = points[index];
                    y = points[index + 1];
                    points[index] = x * M00 + y * M01;
                    points[index + 1] = x * M10 + y * M11;
                    index++;
                    index++;
                }
                return points;
            case APPLY_SHEAR | APPLY_TRANSLATE:
            case APPLY_SHEAR:
                M01 = m01;
                M10 = m10;
                while (--numPts >= 0) {
                    x = points[index];
                    points[index] = points[index + 1] * M01;
                    points[index + 1] = x * M10;
                    index++;
                    index++;
                }
                return points;
            case APPLY_SCALE | APPLY_TRANSLATE:
            case APPLY_SCALE:
                M00 = m00;
                M11 = m11;
                while (--numPts >= 0) {
                    points[index] = points[index] * M00;
                    index++;
                    points[index] = points[index] * M11;
                    index++;
                }
                return points;
            case APPLY_TRANSLATE:
            case APPLY_IDENTITY:
                return points;
        }
        /* NOTREACHED */
    }

    /**
     * Transforms the specified <code>ptSrc</code> and stores the result in
     * <code>ptDst</code>. If <code>ptDst</code> is <code>null</code>, a new
     * {@link Vector} object is allocated and then the result of the
     * transformation is stored in this object. In either case,
     * <code>ptDst</code>, which contains the transformed point, is returned for
     * convenience. If <code>ptSrc</code> and <code>ptDst</code> are the same
     * object, the input point is correctly overwritten with the transformed
     * point.
     */
    public Vector transform(Vector vector) {
        double x, y;
        switch (state) {
            default:
                stateError();
                /* NOTREACHED */
                return null;
            case APPLY_SHEAR | APPLY_SCALE | APPLY_TRANSLATE:
                x = vector.x * m00 + vector.y * m01 + m02;
                y = vector.x * m10 + vector.y * m11 + m12;
                return vector.set(x, y);
            case APPLY_SHEAR | APPLY_SCALE:
                x = vector.x * m00 + vector.y * m01;
                y = vector.x * m10 + vector.y * m11;
                return vector.set(x, y);
            case APPLY_SHEAR | APPLY_TRANSLATE:
                x = vector.y * m01 + m02;
                y = vector.x * m10 + m12;
                return vector.set(x, y);
            case APPLY_SHEAR:
                x = vector.y * m01;
                y = vector.x * m10;
                return vector.set(x, y);
            case APPLY_SCALE | APPLY_TRANSLATE:
                x = vector.x * m00 + m02;
                y = vector.y * m11 + m12;
                return vector.set(x, y);
            case APPLY_SCALE:
                x = vector.x * m00;
                y = vector.y * m11;
                return vector.set(x, y);
            case APPLY_TRANSLATE:
                x = vector.x + m02;
                y = vector.y + m12;
                return vector.set(x, y);
            case APPLY_IDENTITY:
                x = vector.y;
                y = vector.x;
                return vector.set(x, y);
        }
    }

    /**
     * Transforms an array of double precision coordinates by this transform. The
     * two coordinate array sections can be exactly the same or can be overlapping
     * sections of the same array without affecting the validity of the results.
     * This method ensures that no source coordinates are overwritten by a
     * previous operation before they can be transformed. The coordinates are
     * stored in the arrays starting at the indicated offset in the order
     * <code>[x0, y0, x1, y1, ..., xn, yn]</code>.
     */
    public double[] transform(double... points) {
        double M00, M01, M02, M10, M11, M12; // For caching
        double x, y;
        int index = 0;
        int numPts = points.length >> 1;
        switch (state) {
            default:
                stateError();
                return null;
            case APPLY_SHEAR | APPLY_SCALE | APPLY_TRANSLATE:
                M00 = m00;
                M01 = m01;
                M02 = m02;
                M10 = m10;
                M11 = m11;
                M12 = m12;
                while (--numPts >= 0) {
                    x = points[index];
                    y = points[index + 1];
                    points[index] = M00 * x + M01 * y + M02;
                    points[index + 1] = M10 * x + M11 * y + M12;
                    index++;
                    index++;
                }
                return points;
            case APPLY_SHEAR | APPLY_SCALE:
                M00 = m00;
                M01 = m01;
                M10 = m10;
                M11 = m11;
                while (--numPts >= 0) {
                    x = points[index];
                    y = points[index + 1];
                    points[index] = M00 * x + M01 * y;
                    points[index + 1] = M10 * x + M11 * y;
                    index++;
                    index++;
                }
                return points;
            case APPLY_SHEAR | APPLY_TRANSLATE:
                M01 = m01;
                M02 = m02;
                M10 = m10;
                M12 = m12;
                while (--numPts >= 0) {
                    x = points[index];
                    points[index] = M01 * points[index + 1] + M02;
                    points[index + 1] = M10 * x + M12;
                    index++;
                    index++;
                }
                return points;
            case APPLY_SHEAR:
                M01 = m01;
                M10 = m10;
                while (--numPts >= 0) {
                    x = points[index];
                    points[index] = M01 * points[index + 1];
                    points[index + 1] = M10 * x;
                    index++;
                    index++;
                }
                return points;
            case APPLY_SCALE | APPLY_TRANSLATE:
                M00 = m00;
                M02 = m02;
                M11 = m11;
                M12 = m12;
                while (--numPts >= 0) {
                    points[index] = M00 * points[index] + M02;
                    index++;
                    points[index] = M11 * points[index] + M12;
                    index++;
                }
                return points;
            case APPLY_SCALE:
                M00 = m00;
                M11 = m11;
                while (--numPts >= 0) {
                    points[index] = M00 * points[index];
                    index++;
                    points[index] = M11 * points[index];
                    index++;
                }
                return points;
            case APPLY_TRANSLATE:
                M02 = m02;
                M12 = m12;
                while (--numPts >= 0) {
                    points[index] = points[index] + M02;
                    index++;
                    points[index] = points[index] + M12;
                    index++;
                }
                return points;
            case APPLY_IDENTITY:
                return points;
        }
    }

    /**
     * 将此变换设置为它自身的逆变换. 此变换 Tx 的逆向变换 Tx' 将由 Tx 变换的坐标映射回其初始坐标. 换句话说, Tx'(Tx(p)) = p
     * = Tx(Tx'(p)).
     * <p>
     * 如果此变换将所有坐标映射到点或线上，那么它将不能进行逆向变换，因为不位于目标点或线上的坐标没有逆向映射. The
     * <code>getDeterminant</code> 方法可用于确定此变换是否不可逆, 若不可逆则调用 <code>invert</code>
     * 方法时将抛出异常.
     */
    public void invert() {
        double M00, M01, M02;
        double M10, M11, M12;
        double det;
        switch (state) {
            default:
                stateError();
                /* NOTREACHED */
                return;
            case APPLY_SHEAR | APPLY_SCALE | APPLY_TRANSLATE:
                M00 = m00;
                M01 = m01;
                M02 = m02;
                M10 = m10;
                M11 = m11;
                M12 = m12;
                det = M00 * M11 - M01 * M10;
                m00 = M11 / det;
                m10 = -M10 / det;
                m01 = -M01 / det;
                m11 = M00 / det;
                m02 = (M01 * M12 - M11 * M02) / det;
                m12 = (M10 * M02 - M00 * M12) / det;
                break;
            case APPLY_SHEAR | APPLY_SCALE:
                M00 = m00;
                M01 = m01;
                M10 = m10;
                M11 = m11;
                det = M00 * M11 - M01 * M10;
                m00 = M11 / det;
                m10 = -M10 / det;
                m01 = -M01 / det;
                m11 = M00 / det;
                m02 = 0.0;
                m12 = 0.0;
                break;
            case APPLY_SHEAR | APPLY_TRANSLATE:
                M01 = m01;
                M02 = m02;
                M10 = m10;
                M12 = m12;
                m00 = 0.0;
                m10 = 1.0 / M01;
                m01 = 1.0 / M10;
                m11 = 0.0;
                m02 = -M12 / M10;
                m12 = -M02 / M01;
                break;
            case APPLY_SHEAR:
                M01 = m01;
                M10 = m10;
                m00 = 0.0;
                m10 = 1.0 / M01;
                m01 = 1.0 / M10;
                m11 = 0.0;
                m02 = 0.0;
                m12 = 0.0;
                break;
            case APPLY_SCALE | APPLY_TRANSLATE:
                M00 = m00;
                M02 = m02;
                M11 = m11;
                M12 = m12;
                m00 = 1.0 / M00;
                m10 = 0.0;
                m01 = 0.0;
                m11 = 1.0 / M11;
                m02 = -M02 / M00;
                m12 = -M12 / M11;
                break;
            case APPLY_SCALE:
                M00 = m00;
                M11 = m11;
                m00 = 1.0 / M00;
                m10 = 0.0;
                m01 = 0.0;
                m11 = 1.0 / M11;
                m02 = 0.0;
                m12 = 0.0;
                break;
            case APPLY_TRANSLATE:
                m00 = 1.0;
                m10 = 0.0;
                m01 = 0.0;
                m11 = 1.0;
                m02 = -m02;
                m12 = -m12;
                break;
            case APPLY_IDENTITY:
                m00 = 1.0;
                m10 = 0.0;
                m01 = 0.0;
                m11 = 1.0;
                m02 = 0.0;
                m12 = 0.0;
                break;
        }
    }

    /**
     * 逆向变换指定的 Vector，然后将变换的结果存储在此对象中
     */
    public Vector inverseTransform(Vector vector) {
        double x = vector.getX();
        double y = vector.getY();
        switch (state) {
            default:
                stateError();
                /* NOTREACHED */
            case APPLY_SHEAR | APPLY_SCALE | APPLY_TRANSLATE:
                x -= m02;
                y -= m12;
                /* NOBREAK */
            case APPLY_SHEAR | APPLY_SCALE:
                double det = m00 * m11 - m01 * m10;
                if (Math.abs(det) <= Double.MIN_VALUE) {
                    return null;
                }
                vector.set((x * m11 - y * m01) / det, (y * m00 - x * m10) / det);
                return vector;
            case APPLY_SHEAR | APPLY_TRANSLATE:
                x -= m02;
                y -= m12;
                /* NOBREAK */
            case APPLY_SHEAR:
                if (m01 == 0.0 || m10 == 0.0) {
                    return null;
                }
                vector.set(y / m10, x / m01);
                return vector;
            case APPLY_SCALE | APPLY_TRANSLATE:
                x -= m02;
                y -= m12;
                /* NOBREAK */
            case APPLY_SCALE:
                if (m00 == 0.0 || m11 == 0.0) {
                    return null;
                }
                vector.set(x / m00, y / m11);
                return vector;
            case APPLY_TRANSLATE:
                vector.set(x - m02, y - m12);
                return vector;
            case APPLY_IDENTITY:
                vector.set(x, y);
                return vector;
        }

        /* NOTREACHED */
    }

    /**
     * 通过此变换来逆向变换双精度坐标数组。 <code>[x0, y0, x1, y1, ..., xn, yn]</code>.
     */
    public double[] inverseTransform(double... points) {
        double M00, M01, M02, M10, M11, M12; // For caching
        double x, y, det;
        int index = 0;
        int numPts = points.length >> 1;
        switch (state) {
            default:
                stateError();
                return null;
            case APPLY_SHEAR | APPLY_SCALE | APPLY_TRANSLATE:
                M00 = m00;
                M01 = m01;
                M02 = m02;
                M10 = m10;
                M11 = m11;
                M12 = m12;
                det = M00 * M11 - M01 * M10;
                if (Math.abs(det) <= Double.MIN_VALUE) {
                    return null;
                }
                while (--numPts >= 0) {
                    x = points[index] - M02;
                    y = points[index + 1] - M12;
                    points[index] = (x * M11 - y * M01) / det;
                    points[index + 1] = (y * M00 - x * M10) / det;
                    index++;
                    index++;
                }
                return points;
            case APPLY_SHEAR | APPLY_SCALE:
                M00 = m00;
                M01 = m01;
                M10 = m10;
                M11 = m11;
                det = M00 * M11 - M01 * M10;
                if (Math.abs(det) <= Double.MIN_VALUE) {
                    return null;
                }
                while (--numPts >= 0) {
                    x = points[index];
                    y = points[index + 1];
                    points[index] = (x * M11 - y * M01) / det;
                    points[index + 1] = (y * M00 - x * M10) / det;
                    index++;
                    index++;
                }
                return points;
            case APPLY_SHEAR | APPLY_TRANSLATE:
                M01 = m01;
                M02 = m02;
                M10 = m10;
                M12 = m12;
                if (M01 == 0.0 || M10 == 0.0) {
                    return null;
                }
                while (--numPts >= 0) {
                    x = points[index] - M02;
                    points[index] = (points[index + 1] - M12) / M10;
                    points[index + 1] = x / M01;
                    index++;
                    index++;
                }
                return points;
            case APPLY_SHEAR:
                M01 = m01;
                M10 = m10;
                if (M01 == 0.0 || M10 == 0.0) {
                    return null;
                }
                while (--numPts >= 0) {
                    x = points[index];
                    points[index] = points[index + 1] / M10;
                    points[index + 1] = x / M01;
                    index++;
                    index++;
                }
                return points;
            case APPLY_SCALE | APPLY_TRANSLATE:
                M00 = m00;
                M02 = m02;
                M11 = m11;
                M12 = m12;
                if (M00 == 0.0 || M11 == 0.0) {
                    return null;
                }
                while (--numPts >= 0) {
                    points[index] = (points[index] - M02) / M00;
                    index++;
                    points[index] = (points[index] - M12) / M11;
                    index++;
                }
                return points;
            case APPLY_SCALE:
                M00 = m00;
                M11 = m11;
                if (M00 == 0.0 || M11 == 0.0) {
                    return null;
                }
                while (--numPts >= 0) {
                    points[index++] /= M00;
                    points[index++] /= M11;
                }
                return points;
            case APPLY_TRANSLATE:
                M02 = m02;
                M12 = m12;
                while (--numPts >= 0) {
                    points[index++] -= M02;
                    points[index++] -= M12;
                }
                return points;
            case APPLY_IDENTITY:
                return points;
        }
    }

    /**
     * 逆向变换指定的 Vector并获得距离
     */
    public Vector inverseDeltaTransform(Vector vector) {
        Matrix.ATX.setTransform(this);
        Matrix.ATX.invert();
        return Matrix.ATX.deltaTransform(vector);
    }

    public double[] inverseDeltaTransform(double... points) {
        Matrix.ATX.setTransform(this);
        Matrix.ATX.invert();
        return Matrix.ATX.deltaTransform(points);
    }

    /**
     * Concatenates an <code>AffineTransform</code> <code>Tx</code> to this
     * <code>AffineTransform</code> Cx in the most commonly useful way to provide
     * a new user space that is mapped to the former user space by
     * <code>Tx</code>. Cx is updated to perform the combined transformation.
     * Transforming a point p by the updated transform Cx' is equivalent to first
     * transforming p by <code>Tx</code> and then transforming the result by the
     * original transform Cx like this: Cx'(p) = Cx(Tx(p)) In matrix notation, if
     * this transform Cx is represented by the matrix [this] and <code>Tx</code>
     * is represented by the matrix [Tx] then this method does the following:
     *
     * <pre>
     *          [this] = [this] x [Tx]
     * </pre>
     *
     * @param Tx the <code>AffineTransform</code> object to be concatenated with
     *           this <code>AffineTransform</code> object.
     * @see #preConcatenate
     * @since 1.2
     */
    @SuppressWarnings("fallthrough")
    public void concatenate(Matrix Tx) {
        double M0, M1;
        double T00, T01, T10, T11;
        double T02, T12;
        int mystate = state;
        int txstate = Tx.state;
        switch (txstate << HI_SHIFT | mystate) {

            /* ---------- Tx == IDENTITY cases ---------- */
            case HI_IDENTITY | APPLY_IDENTITY:
            case HI_IDENTITY | APPLY_TRANSLATE:
            case HI_IDENTITY | APPLY_SCALE:
            case HI_IDENTITY | APPLY_SCALE | APPLY_TRANSLATE:
            case HI_IDENTITY | APPLY_SHEAR:
            case HI_IDENTITY | APPLY_SHEAR | APPLY_TRANSLATE:
            case HI_IDENTITY | APPLY_SHEAR | APPLY_SCALE:
            case HI_IDENTITY | APPLY_SHEAR | APPLY_SCALE | APPLY_TRANSLATE:
                return;

            /* ---------- this == IDENTITY cases ---------- */
            case HI_SHEAR | HI_SCALE | HI_TRANSLATE | APPLY_IDENTITY:
                m01 = Tx.m01;
                m10 = Tx.m10;
                /* NOBREAK */
            case HI_SCALE | HI_TRANSLATE | APPLY_IDENTITY:
                m00 = Tx.m00;
                m11 = Tx.m11;
                /* NOBREAK */
            case HI_TRANSLATE | APPLY_IDENTITY:
                m02 = Tx.m02;
                m12 = Tx.m12;
                state = txstate;
                type = Tx.type;
                return;
            case HI_SHEAR | HI_SCALE | APPLY_IDENTITY:
                m01 = Tx.m01;
                m10 = Tx.m10;
                /* NOBREAK */
            case HI_SCALE | APPLY_IDENTITY:
                m00 = Tx.m00;
                m11 = Tx.m11;
                state = txstate;
                type = Tx.type;
                return;
            case HI_SHEAR | HI_TRANSLATE | APPLY_IDENTITY:
                m02 = Tx.m02;
                m12 = Tx.m12;
                /* NOBREAK */
            case HI_SHEAR | APPLY_IDENTITY:
                m01 = Tx.m01;
                m10 = Tx.m10;
                m00 = m11 = 0.0;
                state = txstate;
                type = Tx.type;
                return;

            /* ---------- Tx == TRANSLATE cases ---------- */
            case HI_TRANSLATE | APPLY_SHEAR | APPLY_SCALE | APPLY_TRANSLATE:
            case HI_TRANSLATE | APPLY_SHEAR | APPLY_SCALE:
            case HI_TRANSLATE | APPLY_SHEAR | APPLY_TRANSLATE:
            case HI_TRANSLATE | APPLY_SHEAR:
            case HI_TRANSLATE | APPLY_SCALE | APPLY_TRANSLATE:
            case HI_TRANSLATE | APPLY_SCALE:
            case HI_TRANSLATE | APPLY_TRANSLATE:
                translate(Tx.m02, Tx.m12);
                return;

            /* ---------- Tx == SCALE cases ---------- */
            case HI_SCALE | APPLY_SHEAR | APPLY_SCALE | APPLY_TRANSLATE:
            case HI_SCALE | APPLY_SHEAR | APPLY_SCALE:
            case HI_SCALE | APPLY_SHEAR | APPLY_TRANSLATE:
            case HI_SCALE | APPLY_SHEAR:
            case HI_SCALE | APPLY_SCALE | APPLY_TRANSLATE:
            case HI_SCALE | APPLY_SCALE:
            case HI_SCALE | APPLY_TRANSLATE:
                scale(Tx.m00, Tx.m11);
                return;

            /* ---------- Tx == SHEAR cases ---------- */
            case HI_SHEAR | APPLY_SHEAR | APPLY_SCALE | APPLY_TRANSLATE:
            case HI_SHEAR | APPLY_SHEAR | APPLY_SCALE:
                T01 = Tx.m01;
                T10 = Tx.m10;
                M0 = m00;
                m00 = m01 * T10;
                m01 = M0 * T01;
                M0 = m10;
                m10 = m11 * T10;
                m11 = M0 * T01;
                type = TYPE_UNKNOWN;
                return;
            case HI_SHEAR | APPLY_SHEAR | APPLY_TRANSLATE:
            case HI_SHEAR | APPLY_SHEAR:
                m00 = m01 * Tx.m10;
                m01 = 0.0;
                m11 = m10 * Tx.m01;
                m10 = 0.0;
                state = mystate ^ (APPLY_SHEAR | APPLY_SCALE);
                type = TYPE_UNKNOWN;
                return;
            case HI_SHEAR | APPLY_SCALE | APPLY_TRANSLATE:
            case HI_SHEAR | APPLY_SCALE:
                m01 = m00 * Tx.m01;
                m00 = 0.0;
                m10 = m11 * Tx.m10;
                m11 = 0.0;
                state = mystate ^ (APPLY_SHEAR | APPLY_SCALE);
                type = TYPE_UNKNOWN;
                return;
            case HI_SHEAR | APPLY_TRANSLATE:
                m00 = 0.0;
                m01 = Tx.m01;
                m10 = Tx.m10;
                m11 = 0.0;
                state = APPLY_TRANSLATE | APPLY_SHEAR;
                type = TYPE_UNKNOWN;
                return;
        }
        // If Tx has more than one attribute, it is not worth optimizing
        // all of those cases...
        T00 = Tx.m00;
        T01 = Tx.m01;
        T02 = Tx.m02;
        T10 = Tx.m10;
        T11 = Tx.m11;
        T12 = Tx.m12;
        switch (mystate) {
            default:
                stateError();
                /* NOTREACHED */
            case APPLY_SHEAR | APPLY_SCALE:
                state = mystate | txstate;
                /* NOBREAK */
            case APPLY_SHEAR | APPLY_SCALE | APPLY_TRANSLATE:
                M0 = m00;
                M1 = m01;
                m00 = T00 * M0 + T10 * M1;
                m01 = T01 * M0 + T11 * M1;
                m02 += T02 * M0 + T12 * M1;

                M0 = m10;
                M1 = m11;
                m10 = T00 * M0 + T10 * M1;
                m11 = T01 * M0 + T11 * M1;
                m12 += T02 * M0 + T12 * M1;
                type = TYPE_UNKNOWN;
                return;

            case APPLY_SHEAR | APPLY_TRANSLATE:
            case APPLY_SHEAR:
                M0 = m01;
                m00 = T10 * M0;
                m01 = T11 * M0;
                m02 += T12 * M0;

                M0 = m10;
                m10 = T00 * M0;
                m11 = T01 * M0;
                m12 += T02 * M0;
                break;

            case APPLY_SCALE | APPLY_TRANSLATE:
            case APPLY_SCALE:
                M0 = m00;
                m00 = T00 * M0;
                m01 = T01 * M0;
                m02 += T02 * M0;

                M0 = m11;
                m10 = T10 * M0;
                m11 = T11 * M0;
                m12 += T12 * M0;
                break;

            case APPLY_TRANSLATE:
                m00 = T00;
                m01 = T01;
                m02 += T02;

                m10 = T10;
                m11 = T11;
                m12 += T12;
                state = txstate | APPLY_TRANSLATE;
                type = TYPE_UNKNOWN;
                return;
        }
        updateState();
    }

    /**
     * Concatenates an <code>AffineTransform</code> <code>Tx</code> to this
     * <code>AffineTransform</code> Cx in a less commonly used way such that
     * <code>Tx</code> modifies the coordinate transformation relative to the
     * absolute pixel space rather than relative to the existing user space. Cx is
     * updated to perform the combined transformation. Transforming a point p by
     * the updated transform Cx' is equivalent to first transforming p by the
     * original transform Cx and then transforming the result by <code>Tx</code>
     * like this: Cx'(p) = Tx(Cx(p)) In matrix notation, if this transform Cx is
     * represented by the matrix [this] and <code>Tx</code> is represented by the
     * matrix [Tx] then this method does the following:
     *
     * <pre>
     *          [this] = [Tx] x [this]
     * </pre>
     *
     * @param Tx the <code>AffineTransform</code> object to be concatenated with
     *           this <code>AffineTransform</code> object.
     * @see #concatenate
     * @since 1.2
     */
    @SuppressWarnings("fallthrough")
    public void preConcatenate(Matrix Tx) {
        double M0, M1;
        double T00, T01, T10, T11;
        double T02, T12;
        int mystate = state;
        int txstate = Tx.state;
        switch (txstate << HI_SHIFT | mystate) {
            case HI_IDENTITY | APPLY_IDENTITY:
            case HI_IDENTITY | APPLY_TRANSLATE:
            case HI_IDENTITY | APPLY_SCALE:
            case HI_IDENTITY | APPLY_SCALE | APPLY_TRANSLATE:
            case HI_IDENTITY | APPLY_SHEAR:
            case HI_IDENTITY | APPLY_SHEAR | APPLY_TRANSLATE:
            case HI_IDENTITY | APPLY_SHEAR | APPLY_SCALE:
            case HI_IDENTITY | APPLY_SHEAR | APPLY_SCALE | APPLY_TRANSLATE:
                // Tx is IDENTITY...
                return;

            case HI_TRANSLATE | APPLY_IDENTITY:
            case HI_TRANSLATE | APPLY_SCALE:
            case HI_TRANSLATE | APPLY_SHEAR:
            case HI_TRANSLATE | APPLY_SHEAR | APPLY_SCALE:
                // Tx is TRANSLATE, this has no TRANSLATE
                m02 = Tx.m02;
                m12 = Tx.m12;
                state = mystate | APPLY_TRANSLATE;
                type |= TYPE_TRANSLATION;
                return;

            case HI_TRANSLATE | APPLY_TRANSLATE:
            case HI_TRANSLATE | APPLY_SCALE | APPLY_TRANSLATE:
            case HI_TRANSLATE | APPLY_SHEAR | APPLY_TRANSLATE:
            case HI_TRANSLATE | APPLY_SHEAR | APPLY_SCALE | APPLY_TRANSLATE:
                // Tx is TRANSLATE, this has one too
                m02 = m02 + Tx.m02;
                m12 = m12 + Tx.m12;
                return;

            case HI_SCALE | APPLY_TRANSLATE:
            case HI_SCALE | APPLY_IDENTITY:
                // Only these two existing states need a new state
                state = mystate | APPLY_SCALE;
                /* NOBREAK */
            case HI_SCALE | APPLY_SHEAR | APPLY_SCALE | APPLY_TRANSLATE:
            case HI_SCALE | APPLY_SHEAR | APPLY_SCALE:
            case HI_SCALE | APPLY_SHEAR | APPLY_TRANSLATE:
            case HI_SCALE | APPLY_SHEAR:
            case HI_SCALE | APPLY_SCALE | APPLY_TRANSLATE:
            case HI_SCALE | APPLY_SCALE:
                // Tx is SCALE, this is anything
                T00 = Tx.m00;
                T11 = Tx.m11;
                if ((mystate & APPLY_SHEAR) != 0) {
                    m01 = m01 * T00;
                    m10 = m10 * T11;
                    if ((mystate & APPLY_SCALE) != 0) {
                        m00 = m00 * T00;
                        m11 = m11 * T11;
                    }
                } else {
                    m00 = m00 * T00;
                    m11 = m11 * T11;
                }
                if ((mystate & APPLY_TRANSLATE) != 0) {
                    m02 = m02 * T00;
                    m12 = m12 * T11;
                }
                type = TYPE_UNKNOWN;
                return;
            case HI_SHEAR | APPLY_SHEAR | APPLY_TRANSLATE:
            case HI_SHEAR | APPLY_SHEAR:
                mystate = mystate | APPLY_SCALE;
                /* NOBREAK */
            case HI_SHEAR | APPLY_TRANSLATE:
            case HI_SHEAR | APPLY_IDENTITY:
            case HI_SHEAR | APPLY_SCALE | APPLY_TRANSLATE:
            case HI_SHEAR | APPLY_SCALE:
                state = mystate ^ APPLY_SHEAR;
                /* NOBREAK */
            case HI_SHEAR | APPLY_SHEAR | APPLY_SCALE | APPLY_TRANSLATE:
            case HI_SHEAR | APPLY_SHEAR | APPLY_SCALE:
                // Tx is SHEAR, this is anything
                T01 = Tx.m01;
                T10 = Tx.m10;

                M0 = m00;
                m00 = m10 * T01;
                m10 = M0 * T10;

                M0 = m01;
                m01 = m11 * T01;
                m11 = M0 * T10;

                M0 = m02;
                m02 = m12 * T01;
                m12 = M0 * T10;
                type = TYPE_UNKNOWN;
                return;
        }
        // If Tx has more than one attribute, it is not worth optimizing
        // all of those cases...
        T00 = Tx.m00;
        T01 = Tx.m01;
        T02 = Tx.m02;
        T10 = Tx.m10;
        T11 = Tx.m11;
        T12 = Tx.m12;
        switch (mystate) {
            default:
                stateError();
                /* NOTREACHED */
            case APPLY_SHEAR | APPLY_SCALE | APPLY_TRANSLATE:
                M0 = m02;
                M1 = m12;
                T02 += M0 * T00 + M1 * T01;
                T12 += M0 * T10 + M1 * T11;

                /* NOBREAK */
            case APPLY_SHEAR | APPLY_SCALE:
                m02 = T02;
                m12 = T12;

                M0 = m00;
                M1 = m10;
                m00 = M0 * T00 + M1 * T01;
                m10 = M0 * T10 + M1 * T11;

                M0 = m01;
                M1 = m11;
                m01 = M0 * T00 + M1 * T01;
                m11 = M0 * T10 + M1 * T11;
                break;

            case APPLY_SHEAR | APPLY_TRANSLATE:
                M0 = m02;
                M1 = m12;
                T02 += M0 * T00 + M1 * T01;
                T12 += M0 * T10 + M1 * T11;

                /* NOBREAK */
            case APPLY_SHEAR:
                m02 = T02;
                m12 = T12;

                M0 = m10;
                m00 = M0 * T01;
                m10 = M0 * T11;

                M0 = m01;
                m01 = M0 * T00;
                m11 = M0 * T10;
                break;

            case APPLY_SCALE | APPLY_TRANSLATE:
                M0 = m02;
                M1 = m12;
                T02 += M0 * T00 + M1 * T01;
                T12 += M0 * T10 + M1 * T11;

                /* NOBREAK */
            case APPLY_SCALE:
                m02 = T02;
                m12 = T12;

                M0 = m00;
                m00 = M0 * T00;
                m10 = M0 * T10;

                M0 = m11;
                m01 = M0 * T01;
                m11 = M0 * T11;
                break;

            case APPLY_TRANSLATE:
                M0 = m02;
                M1 = m12;
                T02 += M0 * T00 + M1 * T01;
                T12 += M0 * T10 + M1 * T11;

                /* NOBREAK */
            case APPLY_IDENTITY:
                m02 = T02;
                m12 = T12;

                m00 = T00;
                m10 = T10;

                m01 = T01;
                m11 = T11;

                state = mystate | txstate;
                type = TYPE_UNKNOWN;
                return;
        }
        updateState();
    }

    /**
     * Returns a <code>String</code> that represents the value of this
     * {@link Object}.
     *
     * @return a <code>String</code> representing the value of this
     * <code>Object</code>.
     * @since 1.2
     */
    @Override
    public String toString() {
        return "AffineTransform[[" + _matround(m00) + ", " + _matround(m01) + ", " + _matround(m02) + "], [" + _matround(
                m10) + ", " + _matround(m11) + ", " + _matround(m12) + "]]";
    }

    /**
     * 如果此 AffineTransform 是恒等变换，则返回 true
     */
    public boolean isIdentity() {
        return state == APPLY_IDENTITY || getType() == TYPE_IDENTITY;
    }

    @Override
    public Matrix clone() {
        return new Matrix(m00, m10, m01, m11, m02, m12, state);
    }

    /**
     * Returns the hashcode for this transform.
     */
    @Override
    public int hashCode() {
        long bits = Double.doubleToLongBits(m00);
        bits = bits * 31 + Double.doubleToLongBits(m01);
        bits = bits * 31 + Double.doubleToLongBits(m02);
        bits = bits * 31 + Double.doubleToLongBits(m10);
        bits = bits * 31 + Double.doubleToLongBits(m11);
        bits = bits * 31 + Double.doubleToLongBits(m12);
        return (int) bits ^ (int) (bits >> 32);
    }

    /**
     * Returns <code>true</code> if this <code>AffineTransform</code> represents
     * the same affine coordinate transform as the specified argument.
     *
     * @param obj the <code>Object</code> to test for equality with this
     *            <code>AffineTransform</code>
     * @return <code>true</code> if <code>obj</code> equals this
     * <code>AffineTransform</code> object; <code>false</code> otherwise.
     * @since 1.2
     */
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Matrix)) {
            return false;
        }
        Matrix a = (Matrix) obj;
        return m00 == a.m00 && m01 == a.m01 && m02 == a.m02 && m10 == a.m10 && m11 == a.m11 && m12 == a.m12;
    }

}