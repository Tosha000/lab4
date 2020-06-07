package lab_4;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Rectangle2D;

public class FractalExplorer {
    private int size;
    private JImageDisplay display;
    private FractalGenerator gen;
    private Rectangle2D.Double d2;
    public FractalExplorer(int size){
        this.size=size;
        gen=new Mandelbrot();
        d2= new Rectangle2D.Double();
        gen.getInitialRange(d2);
        display=new JImageDisplay(size,size);

    }
    public  void createAndShowGUI (){

        display.setLayout(new BorderLayout());
        JFrame JimageDisplay = new JFrame("Fractal Explorer");
        JimageDisplay.add(display,BorderLayout.CENTER);
        JButton button=new JButton("Reset image");
        ResetHandler handler = new ResetHandler();
        button.addActionListener(handler); // Кнопка сброса в положении SOUTH
        JimageDisplay.add(button, BorderLayout.SOUTH);

        MouseHandler click = new MouseHandler();
        display.addMouseListener( click);// Операция закрытия окна по умолчанию:
                JimageDisplay.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Данные операции правильно разметят содержимое окна, сделают его
        // видимым (окна первоначально не отображаются при их создании для того,
        // чтобы можно было сконфигурировать их прежде, чем выводить на экран), и
        // затем запретят изменение размеров окна.
        JimageDisplay.pack();
        JimageDisplay.setVisible(true);
        JimageDisplay.setResizable(false);
    }

    private void drawFractal() { // Метод для вывода на экран фрактала, должен циклически проходить через каждый пиксель в отображении (т.е. значения x и y будут меняться от 0
        for (int x = 0; x < size; x++) {  //до размера отображения)
            for (int y = 0; y < size; y++) { //x, y - пиксельная координата; xCoord, yCoord - координата в пространстве фрактала
                // Получим координаты x и у, соответствующих координатам пикселя X и У
                double xCoord = gen.getCoord(d2.x, d2.x + d2.width, size, x);
                double yCoord = gen.getCoord(d2.y, d2.y + d2.height, size, y);
                // Вычислим количество итераций для соответствующих координат в области отображения фрактала
                int iteration = gen.numIterations(xCoord, yCoord);

                if (iteration == -1) { // Если число итераций равно -1 (т.е. точка не выходит за границы),установим пиксель в черный цвет (для rgb значение 0).
                    display.drawPixel(x, y, 0);
                }
                else { // Иначе выберем значение цвета, основанное на количестве итераций
                    // Воспользуемся цветовым пространством HSV: поскольку значение цвета
                    // варьируется от 0 до 1, получается плавная последовательность цветов от
                    // красного к желтому, зеленому, синему, фиолетовому и затем обратно к красному
                    float hue = 0.7f + (float) iteration / 200f;
                    int rgbColor = Color.HSBtoRGB(hue, 1f, 1f);
                    // Обновим отображение в соответствии с цветом для каждого пикселя
                    display.drawPixel(x, y, rgbColor);
                }
            }

        }
        display.repaint(); // Обновим JimageDisplay в соответствии с текущим изображением
    }

    // Внутренний класс для обработки событий от кнопки сброса. Обработчик сбрасывает
    // диапазон к начальному, определенному генератором, а затем перерисовает фрактал
    private class ResetHandler implements ActionListener {
        public void actionPerformed(ActionEvent e)
        {
            gen.getInitialRange(d2);
            drawFractal();
        }
    }

    // Внутренний класс для обработки событий с дисплея от мыши
    private class MouseHandler extends MouseAdapter implements MouseListener {

        @Override // Переопределим метод
        // При получении события о щелчке мышью, класс должен
        // отобразить пиксельные кооринаты щелчка в область фрактала, а затем вызвать
        // метод генератора recenterAndZoomRange с координатами, по которым щелкнули, и масштабом 0.5, что приведёт к увеличению фрактала
        public void mouseClicked(MouseEvent e) {
            // Получение координаты х области щелчка мыши
            int x = e.getX();
            double xCoord = gen.getCoord(d2.x, d2.x + d2.width, size, x);
            // Получение координаты у области щелчка мыши
            int y = e.getY();
            double yCoord = gen.getCoord(d2.y, d2.y + d2.height, size, y);
            // Увеличение фрактала
            gen.recenterAndZoomRange(d2, xCoord, yCoord, 0.5);
            // Перерисуем фрактал
            drawFractal();
        }
    }
    public static void main(String[] args)
    {
        FractalExplorer explorer = new FractalExplorer(500);
        explorer.createAndShowGUI();
        explorer.drawFractal();
    }
}
