package lab_4;

import java.awt.geom.Rectangle2D;


/**
 * This class provides the common interface and operations for fractal
 * generators that can be viewed in the Fractal Explorer.
 */
public abstract class FractalGenerator {

    /**
     * This static helper function takes an integer coordinate and converts it
     * into a double-precision value corresponding to a specific range.  It is
     * used to convert pixel coordinates into double-precision values for
     * computing fractals, etc.
     *
     * @param rangeMin the minimum value of the floating-point range
     * @param rangeMax the maximum value of the floating-point range
     *
     * @param size the size of the dimension that the pixel coordinate is from.
     *        For example, this might be the image width, or the image height.
     *
     * @param coord the coordinate to compute the double-precision value for.
     *        The coordinate should fall in the range [0, size].
     */
    public static double getCoord(double rangeMin, double rangeMax,
                                  int size, int coord) {

        assert size > 0;
        assert coord >= 0 && coord < size;

        double range = rangeMax - rangeMin;
        return rangeMin + (range * (double) coord / (double) size);
    }


    /**
     * Sets the specified rectangle to contain the initial range suitable for
     * the fractal being generated.
     */
    public abstract void getInitialRange(Rectangle2D.Double range);


    /**
     * Updates the current range to be centered at the specified coordinates,
     * and to be zoomed in or out by the specified scaling factor.
     */
    public void recenterAndZoomRange(Rectangle2D.Double range,
                                     double centerX, double centerY, double scale) {

        double newWidth = range.width * scale;
        double newHeight = range.height * scale;

        range.x = centerX - newWidth / 2;
        range.y = centerY - newHeight / 2;
        range.width = newWidth;
        range.height = newHeight;
    }


    /**
     * Given a coordinate <em>x</em> + <em>iy</em> in the complex plane,
     * computes and returns the number of iterations before the fractal
     * function escapes the bounding area for that point.  A point that
     * doesn't escape before the iteration limit is reached is indicated
     * with a result of -1.
     */
    public abstract int numIterations(double x, double y);

}

class Mandelbrot extends FractalGenerator {
    public void getInitialRange(Rectangle2D.Double range) { // Double класс определяет диапазон (range) прямоугольника в координатах х и у
        range.x = -2; //x для Мандельброта
        range.y = -1.5; //y для Мандельброта
        range.width = 3; // Ширина для Мандельброта
        range.height = 3; // Высота для Мандельброта
    }

    public static final int MAX_ITERATIONS = 2000; //Константа с максимальным количеством итераций

    public int numIterations(double x, double y) { // Реализует итеративную функцию для фрактала Мандельброта (рассчитывает количество итераций для соответсвующей координаты
        int iteration = 0;
        double real = 0;
        double imaginary = 0;

        // Вычисления повторяются до тех пор, пока |z| > 2 (в данной ситуации точка находится не во множестве Мандельброта), или пока
        // число итераций не достигнет максимального значения, например, 2000 (в этом случае делается предположение, что точка находится в наборе).
        // Модуль комплексного числа в квадрате: |z|^2=x^2+y^2

        while ((iteration < MAX_ITERATIONS) && (real * real + imaginary * imaginary) < 4) {
            // Фрактал (последовательность) Мандельброта: Z0=0, Z1=(Z0)^2+c, Z2=(Z1)^2+c, ...
            // c - определенная точка фрактала, которую мы отображаем на экране, с=x+iy
            // Если переформулировать эти выражения в виде итеративной последовательности значений координат комплексной плоскости (x,y),
            // то есть заменив Zn = Xn+iYn, a c=X0+iY0, то мы получим:
            // X(n+1)=(Xn)^2-(Yn)^2+X0
            // Y(n+1)=2*Xn*Yn+Y0
            double realUpdated = real * real - imaginary * imaginary + x;
            double imaginaryUpdated = 2 * real * imaginary + y;
            real = realUpdated;
            imaginary = imaginaryUpdated;
            iteration += 1; // После обновления координат x и y, итерация +1
        }
        if (iteration == MAX_ITERATIONS) {
            return -1;
        }
        return iteration;
    }
}
