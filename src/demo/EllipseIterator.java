package demo;

import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.PathIterator;
import java.util.NoSuchElementException;

/**
 * A utility class to iterate over the path segments of an ellipse
 * through the PathIterator interface.
 *
 * @author Jim Graham
 */
public class EllipseIterator implements PathIterator {
    // ArcIterator.btan(Math.PI/2)
    public static final double CtrlVal = 0.5522847498307933;
    /*
     * ctrlpts包含一套位于0.5,0.5近似一个半径为0.5的圆4次Bezier曲线的控制点，
     */
    private static final double pcv = 0.5 + CtrlVal * 0.5;
    private static final double ncv = 0.5 - CtrlVal * 0.5;
    private static double ctrlpts[][] = {
            {1.0, pcv, pcv, 1.0, 0.5, 1.0},
            {ncv, 1.0, 0.0, pcv, 0.0, 0.5},
            {0.0, ncv, ncv, 0.0, 0.5, 0.0},
            {pcv, 0.0, 1.0, ncv, 1.0, 0.5}
    };
    double x, y, w, h;
    AffineTransform affine;
    int index;

    EllipseIterator(Ellipse2D e, AffineTransform at) {
        this.x = e.getX();
        this.y = e.getY();
        this.w = e.getWidth();
        this.h = e.getHeight();
        this.affine = at;
        if (w < 0 || h < 0) {
            index = 6;
        }
    }

    /**
     * 返回用于确定路径迭代的旋绕规则
     *
     * @see #WIND_EVEN_ODD
     * @see #WIND_NON_ZERO
     */
    @Override
    public int getWindingRule() {
        return WIND_NON_ZERO;
    }

    /**
     * 测试迭代是否完成。
     *
     * @return 如果已经读取了所有的段，则返回 true；否则返回 false。
     */
    @Override
    public boolean isDone() {
        return index > 5;
    }

    /**
     * 只要最初的遍历方向上还存在点，就沿该方向将迭代器移动到下一个路径段。
     */
    @Override
    public void next() {
        index++;
    }

    /**
     * 使用迭代返回当前路径段的坐标和类型。
     * 返回值就是路径段类型：SEG_MOVETO、SEG_LINETO、
     * SEG_QUADTO、SEG_CUBICTO 或 SEG_CLOSE。
     * 必须传入长度为 6 的 float 数组，该数组可用于存储点的坐标。
     * 每个点都存储为一对 float x、y 坐标。
     * SEG_MOVETO 和 SEG_LINETO 类型返回一个点，
     * SEG_QUADTO 返回两个点，
     * SEG_CUBICTO 返回 3 个点，
     * SEG_CLOSE 不返回任何点。
     *
     * @see #SEG_MOVETO
     * @see #SEG_LINETO
     * @see #SEG_QUADTO
     * @see #SEG_CUBICTO
     * @see #SEG_CLOSE
     */
    @Override
    public int currentSegment(float[] coords) {
        if (isDone()) throw new NoSuchElementException("ellipse iterator out of bounds");
        if (index == 5) return SEG_CLOSE;
        if (index == 0) {
            double ctrls[] = ctrlpts[3];
            coords[0] = (float) (x + ctrls[4] * w);
            coords[1] = (float) (y + ctrls[5] * h);
            if (affine != null) {
                affine.transform(coords, 0, coords, 0, 1);
            }
            return SEG_MOVETO;
        }
        double ctrls[] = ctrlpts[index - 1];
        coords[0] = (float) (x + ctrls[0] * w);
        coords[1] = (float) (y + ctrls[1] * h);
        coords[2] = (float) (x + ctrls[2] * w);
        coords[3] = (float) (y + ctrls[3] * h);
        coords[4] = (float) (x + ctrls[4] * w);
        coords[5] = (float) (y + ctrls[5] * h);
        if (affine != null) {
            affine.transform(coords, 0, coords, 0, 3);
        }
        return SEG_CUBICTO;
    }

    /**
     * 使用迭代返回当前路径段的坐标和类型。
     * 返回值就是路径段类型：SEG_MOVETO、SEG_LINETO、
     * SEG_QUADTO、SEG_CUBICTO 或 SEG_CLOSE。
     * 必须传入长度为 6 的 float 数组，该数组可用于存储点的坐标。
     * 每个点都存储为一对 float x、y 坐标。
     * SEG_MOVETO 和 SEG_LINETO 类型返回一个点，
     * SEG_QUADTO 返回两个点，
     * SEG_CUBICTO 返回 3 个点，
     * SEG_CLOSE 不返回任何点。
     *
     * @see #SEG_MOVETO
     * @see #SEG_LINETO
     * @see #SEG_QUADTO
     * @see #SEG_CUBICTO
     * @see #SEG_CLOSE
     */
    @Override
    public int currentSegment(double[] coords) {
        if (isDone()) throw new NoSuchElementException("ellipse iterator out of bounds");
        if (index == 5) return SEG_CLOSE;
        if (index == 0) {
            double ctrls[] = ctrlpts[3];
            coords[0] = x + ctrls[4] * w;
            coords[1] = y + ctrls[5] * h;
            if (affine != null) {
                affine.transform(coords, 0, coords, 0, 1);
            }
            return SEG_MOVETO;
        }
        double ctrls[] = ctrlpts[index - 1];
        coords[0] = x + ctrls[0] * w;
        coords[1] = y + ctrls[1] * h;
        coords[2] = x + ctrls[2] * w;
        coords[3] = y + ctrls[3] * h;
        coords[4] = x + ctrls[4] * w;
        coords[5] = y + ctrls[5] * h;
        if (affine != null) {
            affine.transform(coords, 0, coords, 0, 3);
        }
        return SEG_CUBICTO;
    }
}
