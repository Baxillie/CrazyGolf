package nl.dke12.bot.pathfinding;

import java.util.ArrayList;

/**
 * Performs spline interpolation given a set of control points.
 *
 */
public class SplineInterpolator
{
    private final ArrayList<Float> sX;
    private final ArrayList<Float> sY;
    private final float[] sM;

    private SplineInterpolator(ArrayList<Float> x, ArrayList<Float> y, float[] m)
    {
        sX = x;
        sY = y;
        sM = m;
    }

    public static SplineInterpolator createMonotoneCubicSpline(ArrayList<Float> x, ArrayList<Float> y)
    {
        if (x == null || y == null || x.size() != y.size() || x.size() < 2)
        {
            throw new IllegalArgumentException("There must be at least two points and the arrays must be of equal length.");
        }

        final int n = x.size();
        float[] d = new float[n - 1]; // could optimize this out
        float[] m = new float[n];

        // Compute slopes of secant lines between successive points.
        for (int i = 0; i < n - 1; i++)
        {
            float h = x.get(i + 1) - x.get(i);
            if (h <= 0f)
            {
                throw new IllegalArgumentException("The control points must all have strictly increasing X values.");
            }
            d[i] = (y.get(i + 1) - y.get(i)) / h;
        }

        // Initialize the tangents as the average of the secants.
        m[0] = d[0];
        for (int i = 1; i < n - 1; i++)
        {
            m[i] = (d[i - 1] + d[i]) * 0.5f;
        }
        m[n - 1] = d[n - 2];

        // Update the tangents to preserve monotonicity.
        for (int i = 0; i < n - 1; i++)
        {
            if (d[i] == 0f) // successive Y values are equal
            {
                m[i] = 0f;
                m[i + 1] = 0f;
            }
            else
            {
                float a = m[i] / d[i];
                float b = m[i + 1] / d[i];
                float h = (float) Math.hypot(a, b);
                if (h > 9f)
                {
                    float t = 3f / h;
                    m[i] = t * a * d[i];
                    m[i + 1] = t * b * d[i];
                }
            }
        }
        return new SplineInterpolator(x, y, m);
    }

    /**
     * Interpolates the value of Y = f(X) for given X. Clamps X to the domain of the spline.
     *
     * @param x
     *            The X value.
     * @return The interpolated Y = f(X) value.
     */
    public float interpolate(float x)
    {
        // Handle the boundary cases.
        final int n = sX.size();
        if (Float.isNaN(x))
        {
            return x;
        }
        if (x <= sX.get(0))
        {
            return sY.get(0);
        }
        if (x >= sX.get(n - 1))
        {
            return sY.get(n - 1);
        }

        // Find the index 'i' of the last point with smaller X.
        // We know this will be within the spline due to the boundary tests.
        int i = 0;
        while (x >= sX.get(i + 1))
        {
            i += 1;
            if (x == sX.get(i))
            {
                return sY.get(i);
            }
        }

        // Perform cubic Hermite spline interpolation.
        float h = sX.get(i + 1) - sX.get(i);
        float t = (x - sX.get(i)) / h;
        return (sY.get(i) * (1 + 2 * t) + h * sM[i] * t) * (1 - t) * (1 - t) + (sY.get(i + 1) * (3 - 2 * t) + h * sM[i + 1] * (t - 1)) * t * t;
    }

    @Override
    public String toString()
    {
        StringBuilder str = new StringBuilder();
        final int n = sX.size();
        str.append("[");
        for (int i = 0; i < n; i++)
        {
            if (i != 0)
            {
                str.append(", ");
            }
            str.append("(").append(sX.get(i));
            str.append(", ").append(sY.get(i));
            str.append(": ").append(sM[i]).append(")");
        }
        str.append("]");
        return str.toString();
    }
}